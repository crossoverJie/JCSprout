package com.crossoverjie.algorithm;

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
