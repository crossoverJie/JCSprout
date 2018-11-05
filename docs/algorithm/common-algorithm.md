
# 红包算法

# 红包算法


```java
public class RedPacket {

    /**
     * 生成红包最小值 1分
     */
    private static final int MIN_MONEY = 1;

    /**
     * 生成红包最大值 200人民币
     */
    private static final int MAX_MONEY = 200 * 100;

    /**
     * 小于最小值
     */
    private static final int LESS = -1;
    /**
     * 大于最大值
     */
    private static final int MORE = -2;

    /**
     * 正常值
     */
    private static final int OK = 1;

    /**
     * 最大的红包是平均值的 TIMES 倍，防止某一次分配红包较大
     */
    private static final double TIMES = 2.1F;

    private int recursiveCount = 0;

    public List<Integer> splitRedPacket(int money, int count) {
        List<Integer> moneys = new LinkedList<>();

        //金额检查，如果最大红包 * 个数 < 总金额；则需要调大最小红包 MAX_MONEY
        if (MAX_MONEY * count <= money) {
            System.err.println("请调大最小红包金额 MAX_MONEY=[" + MAX_MONEY + "]");
            return moneys ;
        }


        //计算出最大红包
        int max = (int) ((money / count) * TIMES);
        max = max > MAX_MONEY ? MAX_MONEY : max;

        for (int i = 0; i < count; i++) {
            //随机获取红包
            int redPacket = randomRedPacket(money, MIN_MONEY, max, count - i);
            moneys.add(redPacket);
            //总金额每次减少
            money -= redPacket;
        }

        return moneys;
    }

    private int randomRedPacket(int totalMoney, int minMoney, int maxMoney, int count) {
        //只有一个红包直接返回
        if (count == 1) {
            return totalMoney;
        }

        if (minMoney == maxMoney) {
            return minMoney;
        }

        //如果最大金额大于了剩余金额 则用剩余金额 因为这个 money 每分配一次都会减小
        maxMoney = maxMoney > totalMoney ? totalMoney : maxMoney;

        //在 minMoney到maxMoney 生成一个随机红包
        int redPacket = (int) (Math.random() * (maxMoney - minMoney) + minMoney);

        int lastMoney = totalMoney - redPacket;

        int status = checkMoney(lastMoney, count - 1);

        //正常金额
        if (OK == status) {
            return redPacket;
        }

        //如果生成的金额不合法 则递归重新生成
        if (LESS == status) {
            recursiveCount++;
            System.out.println("recursiveCount==" + recursiveCount);
            return randomRedPacket(totalMoney, minMoney, redPacket, count);
        }

        if (MORE == status) {
            recursiveCount++;
            System.out.println("recursiveCount===" + recursiveCount);
            return randomRedPacket(totalMoney, redPacket, maxMoney, count);
        }

        return redPacket;
    }

    /**
     * 校验剩余的金额的平均值是否在 最小值和最大值这个范围内
     *
     * @param lastMoney
     * @param count
     * @return
     */
    private int checkMoney(int lastMoney, int count) {
        double avg = lastMoney / count;
        if (avg < MIN_MONEY) {
            return LESS;
        }

        if (avg > MAX_MONEY) {
            return MORE;
        }

        return OK;
    }


    public static void main(String[] args) {
        RedPacket redPacket = new RedPacket();
        List<Integer> redPackets = redPacket.splitRedPacket(20000, 100);
        System.out.println(redPackets);

        int sum = 0;
        for (Integer red : redPackets) {
            sum += red;
        }
        System.out.println(sum);
    }

}
```



# 二叉树层序遍历

```java
public class BinaryNode {
    private Object data ;
    private BinaryNode left ;
    private BinaryNode right ;

    public BinaryNode() {
    }

    public BinaryNode(Object data, BinaryNode left, BinaryNode right) {
        this.data = data;
        this.left = left;
        this.right = right;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public BinaryNode getLeft() {
        return left;
    }

    public void setLeft(BinaryNode left) {
        this.left = left;
    }

    public BinaryNode getRight() {
        return right;
    }

    public void setRight(BinaryNode right) {
        this.right = right;
    }


    public BinaryNode createNode(){
        BinaryNode node = new BinaryNode("1",null,null) ;
        BinaryNode left2 = new BinaryNode("2",null,null) ;
        BinaryNode left3 = new BinaryNode("3",null,null) ;
        BinaryNode left4 = new BinaryNode("4",null,null) ;
        BinaryNode left5 = new BinaryNode("5",null,null) ;
        BinaryNode left6 = new BinaryNode("6",null,null) ;
        node.setLeft(left2) ;
        left2.setLeft(left4);
        left2.setRight(left6);
        node.setRight(left3);
        left3.setRight(left5) ;
        return node ;
    }

    @Override
    public String toString() {
        return "BinaryNode{" +
                "data=" + data +
                ", left=" + left +
                ", right=" + right +
                '}';
    }


    /**
     * 二叉树的层序遍历 借助于队列来实现 借助队列的先进先出的特性
     *
     * 首先将根节点入队列 然后遍历队列。
     * 首先将根节点打印出来，接着判断左节点是否为空 不为空则加入队列
     * @param node
     */
    public void levelIterator(BinaryNode node){
        LinkedList<BinaryNode> queue = new LinkedList<>() ;

        //先将根节点入队
        queue.offer(node) ;
        BinaryNode current ;
        while (!queue.isEmpty()){
            current = queue.poll();

            System.out.print(current.data+"--->");

            if (current.getLeft() != null){
                queue.offer(current.getLeft()) ;
            }
            if (current.getRight() != null){
                queue.offer(current.getRight()) ;
            }
        }
    }
}

public class BinaryNodeTest {

    @Test
    public void test1(){
        BinaryNode node = new BinaryNode() ;
        //创建二叉树
        node = node.createNode() ;
        System.out.println(node);

        //层序遍历二叉树
        node.levelIterator(node) ;

    }

}
```



# 是否为快乐数字

```java
/**
 * Function: 判断一个数字是否为快乐数字 19 就是快乐数字  11就不是快乐数字
 * 19
 * 1*1+9*9=82
 * 8*8+2*2=68
 * 6*6+8*8=100
 * 1*1+0*0+0*0=1
 *
 * 11
 * 1*1+1*1=2
 * 2*2=4
 * 4*4=16
 * 1*1+6*6=37
 * 3*3+7*7=58
 * 5*5+8*8=89
 * 8*8+9*9=145
 * 1*1+4*4+5*5=42
 * 4*4+2*2=20
 * 2*2+0*0=2
 *
 * 这里结果 1*1+1*1=2 和 2*2+0*0=2 重复，所以不是快乐数字
 * @author crossoverJie
 *         Date: 04/01/2018 14:12
 * @since JDK 1.8
 */
public class HappyNum {

    /**
     * 判断一个数字是否为快乐数字
     * @param number
     * @return
     */
    public boolean isHappy(int number) {
        Set<Integer> set = new HashSet<>(30);
        while (number != 1) {
            int sum = 0;
            while (number > 0) {
                //计算当前值的每位数的平方 相加的和 在放入set中，如果存在相同的就认为不是 happy数字
                sum += (number % 10) * (number % 10);
                number = number / 10;
            }
            if (set.contains(sum)) {
                return false;
            } else {
                set.add(sum);
            }
            number = sum;
        }
        return true;
    }
}

public class HappyNumTest {
    @Test
    public void isHappy() throws Exception {
        HappyNum happyNum = new HappyNum() ;
        boolean happy = happyNum.isHappy(19);
        Assert.assertEquals(happy,true);
    }

    @Test
    public void isHappy2() throws Exception {
        HappyNum happyNum = new HappyNum() ;
        boolean happy = happyNum.isHappy(11);
        Assert.assertEquals(happy,false);
    }

    @Test
    public void isHappy3() throws Exception {
        HappyNum happyNum = new HappyNum() ;
        boolean happy = happyNum.isHappy(100);
        System.out.println(happy);
    }

}
```

# 链表是否有环

```java
/**
 * Function:是否是环链表，采用快慢指针，一个走的快些一个走的慢些 如果最终相遇了就说明是环
 * 就相当于在一个环形跑道里跑步，速度不一样的最终一定会相遇。
 *
 * @author crossoverJie
 *         Date: 04/01/2018 11:33
 * @since JDK 1.8
 */
public class LinkLoop {

    public static class Node{
        private Object data ;
        public Node next ;

        public Node(Object data, Node next) {
            this.data = data;
            this.next = next;
        }

        public Node(Object data) {
            this.data = data ;
        }
    }

    /**
     * 判断链表是否有环
     * @param node
     * @return
     */
    public boolean isLoop(Node node){
        Node slow = node ;
        Node fast = node.next ;

        while (slow.next != null){
            Object dataSlow = slow.data;
            Object dataFast = fast.data;

            //说明有环
            if (dataFast == dataSlow){
                return true ;
            }

            //一共只有两个节点，但却不是环形链表的情况，判断NPE
            if (fast.next == null){
                return false ;
            }
            //slow走慢点  fast走快点
            slow = slow.next ;
            fast = fast.next.next ;

            //如果走的快的发现为空 说明不存在环
            if (fast == null){
                return false ;
            }
        }
        return false ;
    }
}
public class LinkLoopTest {

    /**
     * 无环
     * @throws Exception
     */
    @Test
    public void isLoop() throws Exception {
        LinkLoop.Node node3 = new LinkLoop.Node("3");
        LinkLoop.Node node2 = new LinkLoop.Node("2") ;
        LinkLoop.Node node1 = new LinkLoop.Node("1") ;

        node1.next = node2 ;
        node2.next = node3 ;

        LinkLoop linkLoop = new LinkLoop() ;
        boolean loop = linkLoop.isLoop(node1);
        Assert.assertEquals(loop,false);
    }

    /**
     * 有环
     * @throws Exception
     */
    @Test
    public void isLoop2() throws Exception {
        LinkLoop.Node node3 = new LinkLoop.Node("3");
        LinkLoop.Node node2 = new LinkLoop.Node("2") ;
        LinkLoop.Node node1 = new LinkLoop.Node("1") ;

        node1.next = node2 ;
        node2.next = node3 ;
        node3.next = node1 ;

        LinkLoop linkLoop = new LinkLoop() ;
        boolean loop = linkLoop.isLoop(node1);
        Assert.assertEquals(loop,true);
    }

    /**
     * 无环
     * @throws Exception
     */
    @Test
    public void isLoop3() throws Exception {
        LinkLoop.Node node2 = new LinkLoop.Node("2") ;
        LinkLoop.Node node1 = new LinkLoop.Node("1") ;

        node1.next = node2 ;


        LinkLoop linkLoop = new LinkLoop() ;
        boolean loop = linkLoop.isLoop(node1);
        Assert.assertEquals(loop,false);
    }

}
```

# 从一个数组中返回两个值相加等于目标值的下标

```java
/**
 * Function:{1,3,5,7} target=8 返回{2,3}
 *
 * @author crossoverJie
 *         Date: 04/01/2018 09:53
 * @since JDK 1.8
 */
public class TwoSum {

    /**
     * 时间复杂度为 O(N^2)
     * @param nums
     * @param target
     * @return
     */
    public int[] getTwo1(int[] nums,int target){
        int[] result = new int[2] ;

        for (int i= 0 ;i<nums.length ;i++){
            int a = nums[i] ;
            for (int j = nums.length -1 ;j >=0 ;j--){
                int b = nums[j] ;

                if ((a+b) == target){
                    result = new int[]{i,j} ;
                }
            }
        }
        return result ;
    }


    /**
     * 时间复杂度 O(N)
     * 利用Map Key存放目标值和当前值的差值，value 就是当前的下标
     * 每次遍历是 查看当前遍历的值是否等于差值，如果是等于，说明两次相加就等于目标值。
     * 然后取出 map 中 value ，和本次遍历的下标，就是两个下标值相加等于目标值了。
     *
     * @param nums
     * @param target
     * @return
     */
    public int[] getTwo2(int[] nums,int target){
        int[] result = new int[2] ;
        Map<Integer,Integer> map = new HashMap<>(2) ;
        for (int i=0 ;i<nums.length;i++){

            if (map.containsKey(nums[i])){
                result = new int[]{map.get(nums[i]),i} ;
            }
            map.put(target - nums[i],i) ;
        }
        return result ;
    }
}
public class TwoSumTest {
    @Test
    public void getTwo1() throws Exception {
        TwoSum twoSum = new TwoSum() ;
        int[] nums ={1,3,5,7};
        int[] two1 = twoSum.getTwo1(nums, 12);
        System.out.println(JSON.toJSONString(two1));

    }

    @Test
    public void getTwo2(){
        TwoSum twoSum = new TwoSum() ;
        int[] nums ={1,3,5,7};
        int[] two = twoSum.getTwo2(nums, 10);
        System.out.println(JSON.toJSONString(two));

    }

}
```

# 三种方式反向打印单向链表

```java
/**
 * Function: 三种方式反向打印单向链表
 *
 * @author crossoverJie
 *         Date: 10/02/2018 16:14
 * @since JDK 1.8
 */
public class ReverseNode {


    /**
     * 利用栈的先进后出特性
     * @param node
     */
    public void reverseNode1(Node node){

        System.out.println("====翻转之前====");

        Stack<Node> stack = new Stack<>() ;
        while (node != null){

            System.out.print(node.value + "===>");

            stack.push(node) ;
            node = node.next ;
        }

        System.out.println("");

        System.out.println("====翻转之后====");
        while (!stack.isEmpty()){
            System.out.print(stack.pop().value + "===>");
        }

    }


    /**
     * 利用头插法插入链表
     * @param head
     */
    public  void reverseNode(Node head) {
        if (head == null) {
            return ;
        }

        //最终翻转之后的 Node
        Node node ;

        Node pre = head;
        Node cur = head.next;
        Node next ;
        while(cur != null){
            next = cur.next;

            //链表的头插法
            cur.next = pre;
            pre = cur;

            cur = next;
        }
        head.next = null;
        node = pre;


        //遍历新链表
        while (node != null){
            System.out.println(node.value);
            node = node.next ;
        }

    }


    /**
     * 递归
     * @param node
     */
    public void recNode(Node node){

        if (node == null){
            return ;
        }

        if (node.next != null){
            recNode(node.next) ;
        }
        System.out.print(node.value+"===>");
    }


    public static class Node<T>{
        public T value;
        public Node<T> next ;


        public Node(T value, Node<T> next ) {
            this.next = next;
            this.value = value;
        }
    }
}
//单测
public class ReverseNodeTest {

    @Test
    public void reverseNode1() throws Exception {
        ReverseNode.Node<String> node4 = new Node<>("4",null) ;
        Node<String> node3 = new Node<>("3",node4);
        Node<String> node2 = new Node<>("2",node3);
        Node<String> node1 = new Node("1",node2) ;

        ReverseNode reverseNode = new ReverseNode() ;
        reverseNode.reverseNode1(node1);
    }

    @Test
    public void reverseNode12() throws Exception {

        Node<String> node1 = new Node("1",null) ;

        ReverseNode reverseNode = new ReverseNode() ;
        reverseNode.reverseNode1(node1);
    }

    @Test
    public void reverseNode13() throws Exception {

        Node<String> node1 = null ;

        ReverseNode reverseNode = new ReverseNode() ;
        reverseNode.reverseNode1(node1);
    }


    /**
     * 头插法
     * @throws Exception
     */
    @Test
    public void reverseHead21() throws Exception {
        Node<String> node4 = new Node<>("4",null) ;
        Node<String> node3 = new Node<>("3",node4);
        Node<String> node2 = new Node<>("2",node3);
        Node<String> node1 = new Node("1",node2) ;

        ReverseNode reverseNode = new ReverseNode() ;
        reverseNode.reverseNode(node1);

    }


    @Test
    public void recNodeTest31(){
        Node<String> node4 = new Node<>("4",null) ;
        Node<String> node3 = new Node<>("3",node4);
        Node<String> node2 = new Node<>("2",node3);
        Node<String> node1 = new Node("1",node2) ;

        ReverseNode reverseNode = new ReverseNode() ;
        reverseNode.recNode(node1);
    }

}
```

# 合并两个排好序的链表

```java
/**
 * Function: 合并两个排好序的链表
 *
 * 每次比较两个链表的头结点，将较小结点放到新的链表，最后将新链表指向剩余的链表
 *
 * @author crossoverJie
 *         Date: 07/12/2017 13:58
 * @since JDK 1.8
 */
public class MergeTwoSortedLists {


    /**
     * 1. 声明一个头结点
     * 2. 将头结点的引用赋值给一个临时结点，也可以叫做下一结点。
     * 3. 进行循环比较，每次都将指向值较小的那个结点(较小值的引用赋值给 lastNode )。
     * 4. 再去掉较小值链表的头结点，指针后移。
     * 5. lastNode 指针也向后移，由于 lastNode 是 head 的引用，这样可以保证最终 head 的值是往后更新的。
     * 6. 当其中一个链表的指针移到最后时跳出循环。
     * 7. 由于这两个链表已经是排好序的，所以剩下的链表必定是最大的值，只需要将指针指向它即可。
     * 8. 由于 head 链表的第一个结点是初始化的0，所以只需要返回 0 的下一个结点即是合并了的链表。
     * @param l1
     * @param l2
     * @return
     */
    public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        ListNode head = new ListNode(0) ;
        ListNode lastNode = head ;

        while (l1 != null  && l2 != null){
            if (l1.currentVal < l2.currentVal){
                lastNode.next = l1 ;
                l1 = l1.next ;
            } else {
                lastNode.next = l2 ;
                l2 = l2.next ;
            }
            lastNode =lastNode.next ;
        }

        if (l1 == null){
            lastNode.next = l2 ;
        }
        if (l2 == null){
            lastNode.next = l1 ;
        }

        return head.next ;
    }


    public static class ListNode {
        /**
         * 当前值
         */
        int currentVal;

        /**
         * 下一个节点
         */
        ListNode next;

        ListNode(int val) {
            currentVal = val;
        }

        @Override
        public String toString() {
            return "ListNode{" +
                    "currentVal=" + currentVal +
                    ", next=" + next +
                    '}';
        }
    }

}

//单测
public class MergeTwoSortedListsTest {
    MergeTwoSortedLists mergeTwoSortedLists ;
    @Before
    public void setUp() throws Exception {
        mergeTwoSortedLists = new MergeTwoSortedLists();
    }

    @Test
    public void mergeTwoLists() throws Exception {
        ListNode l1 = new ListNode(1) ;
        ListNode l1_2 = new ListNode(4);
        l1.next = l1_2 ;
        ListNode l1_3 = new ListNode(5) ;
        l1_2.next = l1_3 ;

        ListNode l2 = new ListNode(1) ;
        ListNode l2_2 = new ListNode(3) ;
        l2.next = l2_2 ;
        ListNode l2_3 = new ListNode(6) ;
        l2_2.next = l2_3 ;
        ListNode l2_4 = new ListNode(9) ;
        l2_3.next = l2_4 ;
        ListNode listNode = mergeTwoSortedLists.mergeTwoLists(l1, l2);


        ListNode node1 = new ListNode(1) ;
        ListNode node2 = new ListNode(1);
        node1.next = node2;
        ListNode node3 = new ListNode(3) ;
        node2.next= node3 ;
        ListNode node4 = new ListNode(4) ;
        node3.next = node4 ;
        ListNode node5 = new ListNode(5) ;
        node4.next = node5 ;
        ListNode node6 = new ListNode(6) ;
        node5.next = node6 ;
        ListNode node7 = new ListNode(9) ;
        node6.next = node7 ;
        Assert.assertEquals(node1.toString(),listNode.toString());


    }

    @Test
    public void mergeTwoLists2() throws Exception {

        ListNode l2 = new ListNode(1) ;
        ListNode l2_2 = new ListNode(3) ;
        l2.next = l2_2 ;
        ListNode l2_3 = new ListNode(6) ;
        l2_2.next = l2_3 ;
        ListNode l2_4 = new ListNode(9) ;
        l2_3.next = l2_4 ;
        ListNode listNode = mergeTwoSortedLists.mergeTwoLists(null, l2);

        System.out.println(listNode.toString());


    }

    @Test
    public void mergeTwoLists3() throws Exception {

        ListNode l2 = new ListNode(1) ;
        ListNode l2_2 = new ListNode(3) ;
        l2.next = l2_2 ;
        ListNode l2_3 = new ListNode(6) ;
        l2_2.next = l2_3 ;
        ListNode l2_4 = new ListNode(9) ;
        l2_3.next = l2_4 ;
        ListNode listNode = mergeTwoSortedLists.mergeTwoLists(l2, null);


        ListNode node1 = new ListNode(1) ;
        ListNode node2 = new ListNode(3);
        node1.next = node2;
        ListNode node3 = new ListNode(6) ;
        node2.next= node3 ;
        ListNode node4 = new ListNode(9) ;
        node3.next = node4 ;

        Assert.assertEquals(node1.toString(),listNode.toString());

    }

}
```

# 两个栈实现队列
```java
/**
 * Function: 两个栈实现队列
 *
 * 利用两个栈来实现，第一个栈存放写队列的数据。
 * 第二个栈存放移除队列的数据，移除之前先判断第二个栈里是否有数据。
 * 如果没有就要将第一个栈里的数据依次弹出压入第二个栈，这样写入之后的顺序再弹出其实就是一个先进先出的结构了。
 *
 * 这样出队列只需要移除第二个栈的头元素即可。
 *
 * @author crossoverJie
 *         Date: 09/02/2018 23:51
 * @since JDK 1.8
 */
public class TwoStackQueue<T> {

    /**
     * 写入的栈
     */
    private Stack<T> input = new Stack() ;

    /**
     * 移除队列所出的栈
     */
    private Stack<T> out = new Stack() ;


    /**
     * 写入队列
     * @param t
     */
    public void appendTail(T t){
        input.push(t) ;
    }

    /**
     * 删除队列头结点 并返回删除数据
     * @return
     */
    public T deleteHead(){

        //是空的 需要将 input 出栈写入 out
        if (out.isEmpty()){
            while (!input.isEmpty()){
                out.push(input.pop()) ;
            }
        }

        //不为空时直接移除出栈就表示移除了头结点
        return out.pop() ;
    }


    public int getSize(){
        return input.size() + out.size() ;
    }

}
//单测
public class TwoStackQueueTest {
    private final static Logger LOGGER = LoggerFactory.getLogger(TwoStackQueueTest.class);
    @Test
    public void queue(){
        TwoStackQueue<String> twoStackQueue = new TwoStackQueue<String>() ;
        twoStackQueue.appendTail("1") ;
        twoStackQueue.appendTail("2") ;
        twoStackQueue.appendTail("3") ;
        twoStackQueue.appendTail("4") ;
        twoStackQueue.appendTail("5") ;


        int size = twoStackQueue.getSize();

        for (int i = 0; i< size ; i++){
            LOGGER.info(twoStackQueue.deleteHead());
        }

        LOGGER.info("========第二次添加=========");

        twoStackQueue.appendTail("6") ;

        size = twoStackQueue.getSize();

        for (int i = 0; i< size ; i++){
            LOGGER.info(twoStackQueue.deleteHead());
        }
    }

}
```
# 链表排序
```java
/**
 * 链表排序, 建议使用归并排序，
 * 问题描述，给定一个Int的链表，要求在时间最优的情况下完成链表元素由大到小的排序，
 *     e.g: 1->5->4->3->2
 *     排序后结果 5->4->3->2->1
 *
 * @author 6563699600@qq.com
 * @date 6/7/2018 11:42 PM
 * @since 1.0
 */
public class LinkedListMergeSort {

    /**
     * 定义链表数据结构，包含当前元素，以及当前元素的后续元素指针
     */
    final static class Node {
        int e;
        Node next;

        public Node() {
        }

        public Node(int e, Node next) {
            this.e = e;
            this.next = next;
        }
    }

    public Node mergeSort(Node first, int length) {

        if (length == 1) {
            return first;
        } else {
            Node middle = new Node();
            Node tmp = first;

            /**
             * 后期会对这里进行优化，通过一次遍历算出长度和中间元素
             */
            for (int i = 0; i < length; i++) {
                if (i == length / 2) {
                    break;
                }
                middle = tmp;
                tmp = tmp.next;
            }

            /**
             *  这里是链表归并时要注意的细节
             *  在链表进行归并排序过程中，会涉及到将一个链表打散为两个独立的链表，所以需要在中间元素的位置将其后续指针指为null；
             */
            Node right = middle.next;
            middle.next = null;

            Node leftStart = mergeSort(first, length / 2);
            Node rightStart;
            if (length % 2 == 0) {
                rightStart = mergeSort(right, length / 2);
            } else {
                rightStart = mergeSort(right, length / 2 + 1);
            }
            return mergeList(leftStart, rightStart);
        }
    }

    /**
     * 合并链表，具体的实现细节可参考<code>MergeTwoSortedLists</code>
     *
     * @param left
     * @param right
     * @return
     */
    public Node mergeList(Node left, Node right) {

        Node head = new Node();
        Node result = head;

        /**
         * 思想就是两个链表同时遍历，将更的元素插入结果中，同时更更大的元素所属的链表的指针向下移动
         */
        while (!(null == left && null == right)) {
            Node tmp;
            if (left == null) {
                result.next = right;
                break;
            } else if (right == null) {
                result.next = left;
                break;
            } else if (left.e >= right.e) {
                tmp = left;
                result.next = left;
                result = tmp;
                left = left.next;
            } else {
                tmp = right;
                result.next = right;
                result = tmp;
                right = right.next;
            }
        }

        return head.next;
    }

    public static void main(String[] args) {

        Node head = new Node();

        head.next = new Node(7,
                new Node(2,
                        new Node(5,
                                new Node(4,
                                        new Node(3,
                                                new Node(6,
                                                        new Node(11, null)
                                                )
                                        )
                                )
                        )
                )
        );

        int length = 0;

        for (Node e = head.next; null != e; e = e.next) {
            length++;
        }


        LinkedListMergeSort sort = new LinkedListMergeSort();
        head.next = sort.mergeSort(head.next, length);


        for (Node n = head.next; n != null; n = n.next) {
            System.out.println(n.e);
        }

    }
}
```
# 数组右移 k 次
```java
/**
 * 数组右移K次, 原数组<code> [1, 2, 3, 4, 5, 6, 7]</code> 右移3次后结果为 <code>[5,6,7,1,2,3,4]</code>
 *
 * 基本思路：不开辟新的数组空间的情况下考虑在原属组上进行操作
 * 1 将数组倒置，这样后k个元素就跑到了数组的前面，然后反转一下即可
 * 2 同理后 len-k个元素只需要翻转就完成数组的k次移动
 *
 * @author 656369960@qq.com
 * @date 12/7/2018 1:38 PM
 * @since 1.0
 */
public class ArrayKShift {

    public void arrayKShift(int[] array, int k) {

        /**
         * constrictions
         */

        if (array == null || 0 == array.length) {
            return ;
        }

        k = k % array.length;

        if (0 > k) {
            return;
        }


        /**
         * reverse array , e.g: [1, 2, 3 ,4] to [4,3,2,1]
         */

        for (int i = 0; i < array.length / 2; i++) {
            int tmp = array[i];
            array[i] = array[array.length - 1 - i];
            array[array.length - 1 - i] = tmp;
        }

        /**
         * first k element reverse
         */
        for (int i = 0; i < k / 2; i++) {
            int tmp = array[i];
            array[i] = array[k - 1 - i];
            array[k - 1 - i] = tmp;
        }

        /**
         * last length - k element reverse
         */

        for (int i = k; i < k + (array.length - k ) / 2; i ++) {
            int tmp = array[i];
            array[i] = array[array.length - 1 - i + k];
            array[array.length - 1 - i + k] = tmp;
        }
    }

    public static void main(String[] args) {
        int[] array = {1, 2, 3 ,4, 5, 6, 7};
        ArrayKShift shift = new ArrayKShift();
        shift.arrayKShift(array, 6);

        Arrays.stream(array).forEach(o -> {
            System.out.println(o);
        });

    }
}
```

# 交替打印奇偶数

## lock 版

```java
/**
 * Function: 两个线程交替执行打印 1~100
 *
 * lock 版
 *
 * @author crossoverJie
 *         Date: 11/02/2018 10:04
 * @since JDK 1.8
 */
public class TwoThread {

    private int start = 1;

    /**
     * 对 flag 的写入虽然加锁保证了线程安全，但读取的时候由于 不是 volatile 所以可能会读取到旧值
     *
     */
    private volatile boolean flag = false;

    /**
     * 重入锁
     */
    private final static Lock LOCK = new ReentrantLock();

    public static void main(String[] args) {
        TwoThread twoThread = new TwoThread();

        Thread t1 = new Thread(new OuNum(twoThread));
        t1.setName("t1");


        Thread t2 = new Thread(new JiNum(twoThread));
        t2.setName("t2");

        t1.start();
        t2.start();
    }

    /**
     * 偶数线程
     */
    public static class OuNum implements Runnable {

        private TwoThread number;

        public OuNum(TwoThread number) {
            this.number = number;
        }

        @Override
        public void run() {
            while (number.start <= 1000) {

                if (number.flag) {
                    try {
                        LOCK.lock();
                        System.out.println(Thread.currentThread().getName() + "+-+" + number.start);
                        number.start++;
                        number.flag = false;


                    } finally {
                        LOCK.unlock();
                    }
                }
            }
        }
    }

    /**
     * 奇数线程
     */
    public static class JiNum implements Runnable {

        private TwoThread number;

        public JiNum(TwoThread number) {
            this.number = number;
        }

        @Override
        public void run() {
            while (number.start <= 1000) {

                if (!number.flag) {
                    try {
                        LOCK.lock();
                        System.out.println(Thread.currentThread().getName() + "+-+" + number.start);
                        number.start++;
                        number.flag = true;


                    } finally {
                        LOCK.unlock();
                    }
                }
            }
        }
    }
}
```

## 等待通知版
```java
/**
 * Function:两个线程交替执行打印 1~100
 * 等待通知机制版
 *
 * @author crossoverJie
 *         Date: 07/03/2018 13:19
 * @since JDK 1.8
 */
public class TwoThreadWaitNotify {

    private int start = 1;

    private boolean flag = false;

    public static void main(String[] args) {
        TwoThreadWaitNotify twoThread = new TwoThreadWaitNotify();

        Thread t1 = new Thread(new OuNum(twoThread));
        t1.setName("t1");


        Thread t2 = new Thread(new JiNum(twoThread));
        t2.setName("t2");

        t1.start();
        t2.start();
    }

    /**
     * 偶数线程
     */
    public static class OuNum implements Runnable {
        private TwoThreadWaitNotify number;

        public OuNum(TwoThreadWaitNotify number) {
            this.number = number;
        }

        @Override
        public void run() {

            while (number.start <= 100) {
                synchronized (TwoThreadWaitNotify.class) {
                    System.out.println("偶数线程抢到锁了");
                    if (number.flag) {
                        System.out.println(Thread.currentThread().getName() + "+-+偶数" + number.start);
                        number.start++;

                        number.flag = false;
                        TwoThreadWaitNotify.class.notify();

                    }else {
                        try {
                            TwoThreadWaitNotify.class.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        }
    }


    /**
     * 奇数线程
     */
    public static class JiNum implements Runnable {
        private TwoThreadWaitNotify number;

        public JiNum(TwoThreadWaitNotify number) {
            this.number = number;
        }

        @Override
        public void run() {
            while (number.start <= 100) {
                synchronized (TwoThreadWaitNotify.class) {
                    System.out.println("奇数线程抢到锁了");
                    if (!number.flag) {
                        System.out.println(Thread.currentThread().getName() + "+-+奇数" + number.start);
                        number.start++;

                        number.flag = true;

                        TwoThreadWaitNotify.class.notify();
                    }else {
                        try {
                            TwoThreadWaitNotify.class.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}
```

## 非阻塞版
```java
/**
 * Function: 两个线程交替执行打印 1~100
 * <p>
 * non blocking 版：
 * 两个线程轮询volatile变量(flag) 
 * 线程一"看到"flag值为1时执行代码并将flag设置为0,
 * 线程二"看到"flag值为0时执行代码并将flag设置未1,
 * 2个线程不断轮询直到满足条件退出
 *
 * @author twoyao
 * Date: 05/07/2018
 * @since JDK 1.8
 */

public class TwoThreadNonBlocking implements Runnable {

    /**
     * 当flag为1时只有奇数线程可以执行，并将其置为0
     * 当flag为0时只有偶数线程可以执行，并将其置为1
     */
    private volatile static int flag = 1;

    private int start;
    private int end;
    private String name;

    private TwoThreadNonBlocking(int start, int end, String name) {
        this.name = name;
        this.start = start;
        this.end = end;
    }

    @Override
    public void run() {
        while (start <= end) {
            int f = flag;
            if ((start & 0x01) == f) {
                System.out.println(name + "+-+" + start);
                start += 2;
                // 因为只可能同时存在一个线程修改该值，所以不会存在竞争
                flag ^= 0x1;
            }
        }
    }


    public static void main(String[] args) {
        new Thread(new TwoThreadNonBlocking(1, 100, "t1")).start();
        new Thread(new TwoThreadNonBlocking(2, 100, "t2")).start();
    }
}
```