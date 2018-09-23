package com.crossoverjie.algorithm;

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
