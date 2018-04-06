package com.crossoverjie.actual;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Function:
 *
 * 1.在做 key 生成 hashcode 时是用的 HashMap 的 hash 函数
 * 2.在做 put get 时，如果存在 key 相等时候为了简单没有去比较 equal 和 hashcode
 * 3.限制大小， map的最大size是1024， 超过1024后，就淘汰掉最久没有访问的kv 键值对， 当淘汰时，需要调用一个callback   lruCallback(K key, V value)
 * 是利用每次 put 都将值写入一个内部队列，这样只需要判断队列里的第一个即可。
 * 4.具备超时功能， 当键值对1小时内没有被访问， 就被淘汰掉, 当淘汰时， 需要调用一个callback   timeoutCallback(K key, V value);
 * 超时同理，单独开启一个守护进程来处理，取的是队列里的第一个 因为第一个是最早放进去的。
 *
 * 但是像 HashMap 里的扩容，链表在超过阈值之类的没有考虑进来。
 *
 * @author crossoverJie
 *         Date: 02/02/2018 20:47
 * @since JDK 1.8
 */
public class LRUAbstractMap extends java.util.AbstractMap {

    private final static Logger LOGGER = LoggerFactory.getLogger(LRUAbstractMap.class);

    /**
     * 检查是否超期线程
     */
    private ExecutorService checkTimePool ;

    /**
     * map 最大size
     */
    private final static int MAX_SIZE = 1024 ;

    private final static ArrayBlockingQueue<Node> QUEUE = new ArrayBlockingQueue<>(MAX_SIZE) ;

    /**
     * 默认大小
     */
    private final static int DEFAULT_ARRAY_SIZE =1024 ;


    /**
     * 数组长度
     */
    private int arraySize ;

    /**
     * 数组
     */
    private Object[] arrays ;


    /**
     * 判断是否停止 flag
     */
    private volatile boolean flag = true ;


    /**
     * 超时时间
     */
    private final static Long EXPIRE_TIME = 60 * 60 * 1000L ;

    /**
     * 整个 Map 的大小
     */
    private volatile AtomicInteger size  ;


    public LRUAbstractMap() {


        arraySize = DEFAULT_ARRAY_SIZE;
        arrays = new Object[arraySize] ;

        //开启一个线程检查最先放入队列的值是否超期
        executeCheckTime();
    }

    /**
     * 开启一个线程检查最先放入队列的值是否超期 设置为守护线程
     */
    private void executeCheckTime() {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("check-thread-%d")
                .setDaemon(true)
                .build();
        checkTimePool = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(1),namedThreadFactory,new ThreadPoolExecutor.AbortPolicy());
        checkTimePool.execute(new CheckTimeThread()) ;

    }

    @Override
    public Set<Entry> entrySet() {
        return super.keySet();
    }

    @Override
    public Object put(Object key, Object value) {
        int hash = hash(key);
        int index = hash % arraySize ;
        Node currentNode = (Node) arrays[index] ;

        if (currentNode == null){
            arrays[index] = new Node(null,null, key, value);

            //写入队列
            QUEUE.offer((Node) arrays[index]) ;

            sizeUp();
        }else {
            Node cNode = currentNode ;
            Node nNode = cNode ;

            //存在就覆盖
            if (nNode.key == key){
                cNode.val = value ;
            }

            while (nNode.next != null){
                //key 存在 就覆盖 简单判断
                if (nNode.key == key){
                    nNode.val = value ;
                    break ;
                }else {
                    //不存在就新增链表
                    sizeUp();
                    Node node = new Node(nNode,null,key,value) ;

                    //写入队列
                    QUEUE.offer(currentNode) ;

                    cNode.next = node ;
                }

                nNode = nNode.next ;
            }

        }

        return null ;
    }


    @Override
    public Object get(Object key) {

        int hash = hash(key) ;
        int index = hash % arraySize ;
        Node currentNode = (Node) arrays[index] ;

        if (currentNode == null){
            return null ;
        }
        if (currentNode.next == null){

            //更新时间
            currentNode.setUpdateTime(System.currentTimeMillis());

            //没有冲突
            return currentNode ;

        }

        Node nNode = currentNode ;
        while (nNode.next != null){

            if (nNode.key == key){

                //更新时间
                currentNode.setUpdateTime(System.currentTimeMillis());

                return nNode ;
            }

            nNode = nNode.next ;
        }

        return super.get(key);
    }


    @Override
    public Object remove(Object key) {

        int hash = hash(key) ;
        int index = hash % arraySize ;
        Node currentNode = (Node) arrays[index] ;

        if (currentNode == null){
            return null ;
        }

        if (currentNode.key == key){
            sizeDown();
            arrays[index] = null ;

            //移除队列
            QUEUE.poll();
            return currentNode ;
        }

        Node nNode = currentNode ;
        while (nNode.next != null){

            if (nNode.key == key){
                sizeDown();
                //在链表中找到了 把上一个节点的 next 指向当前节点的下一个节点
                nNode.pre.next = nNode.next ;
                nNode = null ;

                //移除队列
                QUEUE.poll();

                return nNode;
            }

            nNode = nNode.next ;
        }

        return super.remove(key);
    }

    /**
     * 增加size
     */
    private void sizeUp(){

        //在put值时候认为里边已经有数据了
        flag = true ;

        if (size == null){
            size = new AtomicInteger() ;
        }
        int size = this.size.incrementAndGet();
        if (size >= MAX_SIZE) {
            //找到队列头的数据
            Node node = QUEUE.poll() ;
            if (node == null){
                throw new RuntimeException("data error") ;
            }

            //移除该 key
            Object key = node.key ;
            remove(key) ;
            lruCallback() ;
        }

    }

    /**
     * 数量减小
     */
    private void sizeDown(){

        if (QUEUE.size() == 0){
            flag = false ;
        }

        this.size.decrementAndGet() ;
    }

    @Override
    public int size() {
        return size.get() ;
    }

    /**
     * 链表
     */
    private class Node{
        private Node next ;
        private Node pre ;
        private Object key ;
        private Object val ;
        private Long updateTime ;

        public Node(Node pre,Node next, Object key, Object val) {
            this.pre = pre ;
            this.next = next;
            this.key = key;
            this.val = val;
            this.updateTime = System.currentTimeMillis() ;
        }

        public void setUpdateTime(Long updateTime) {
            this.updateTime = updateTime;
        }

        public Long getUpdateTime() {
            return updateTime;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "key=" + key +
                    ", val=" + val +
                    '}';
        }
    }


    /**
     * copy HashMap 的 hash 实现
     * @param key
     * @return
     */
    public int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    private void lruCallback(){
        LOGGER.debug("lruCallback");
    }


    private class CheckTimeThread implements Runnable{

        @Override
        public void run() {
            while (flag){
                try {
                    Node node = QUEUE.poll();
                    if (node == null){
                        continue ;
                    }
                    Long updateTime = node.getUpdateTime() ;

                    if ((updateTime - System.currentTimeMillis()) >= EXPIRE_TIME){
                        remove(node.key) ;
                    }
                } catch (Exception e) {
                    LOGGER.error("InterruptedException");
                }
            }
        }
    }

}
