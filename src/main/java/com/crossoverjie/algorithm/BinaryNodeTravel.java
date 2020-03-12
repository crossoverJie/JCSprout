package com.crossoverjie.algorithm;

import java.util.LinkedList;

/**
 * Function: 层序遍历，需要将遍历的节点串联起来
 *
 * @author crossoverJie
 *         Date: 2018/7/27 23:37
 * @since JDK 1.8
 */
public class BinaryNodeTravel {

    private Object data ;
    private BinaryNodeTravel left ;
    private BinaryNodeTravel right ;
    public BinaryNodeTravel next;

    public BinaryNodeTravel() {
    }

    public BinaryNodeTravel(Object data, BinaryNodeTravel left, BinaryNodeTravel right) {
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

    public BinaryNodeTravel getLeft() {
        return left;
    }

    public void setLeft(BinaryNodeTravel left) {
        this.left = left;
    }

    public BinaryNodeTravel getRight() {
        return right;
    }

    public void setRight(BinaryNodeTravel right) {
        this.right = right;
    }


    public BinaryNodeTravel createNode(){
        BinaryNodeTravel nodeA = new BinaryNodeTravel("A",null,null) ;
        BinaryNodeTravel nodeB = new BinaryNodeTravel("B",null,null) ;
        BinaryNodeTravel nodeC = new BinaryNodeTravel("C",null,null) ;
        BinaryNodeTravel nodeD = new BinaryNodeTravel("D",null,null) ;
        BinaryNodeTravel nodeE = new BinaryNodeTravel("E",null,null) ;
        BinaryNodeTravel nodeF = new BinaryNodeTravel("F",null,null) ;

        nodeA.setLeft(nodeB);
        nodeB.setLeft(nodeD);
        nodeA.setRight(nodeC);
        nodeC.setLeft(nodeE);
        nodeC.setRight(nodeF);

        return nodeA ;
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
     *
     * 暂时把上一个节点存起来，每次都把上一节点的 next 指向当前节点
     *
     * 首先将根节点打印出来，接着判断左节点是否为空 不为空则加入队列
     * @param node
     */
    public BinaryNodeTravel levelIterator(BinaryNodeTravel node){
        LinkedList<BinaryNodeTravel> queue = new LinkedList<>() ;


        //暂时存放的上一节点
        BinaryNodeTravel pre = null;

        //先将根节点入队
        queue.offer(node) ;
        BinaryNodeTravel current ;
        while (!queue.isEmpty()){
            current = queue.poll();

            //将上一节点指向当前节点
            if (pre == null){
                pre = current ;
            }else {
                pre.next = current ;
                pre = current;
            }

            if (current.getLeft() != null){
                queue.offer(current.getLeft()) ;
            }
            if (current.getRight() != null){
                queue.offer(current.getRight()) ;
            }
        }

        return node ;
    }
}
