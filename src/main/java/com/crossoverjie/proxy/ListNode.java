package com.crossoverjie.proxy;

/**
 * Function:
 *
 * @author crossoverJie
 *         Date: 07/12/2017 17:53
 * @since JDK 1.8
 */
public class ListNode {
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
