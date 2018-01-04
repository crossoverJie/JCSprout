package com.crossoverjie.algorithm;

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

    public boolean isLoop(Node node){
        Node slow = node ;
        Node fast = node.next ;

        while (node.next != null){
            Object dataSlow = slow.data;
            Object dataFast = fast.data;

            //说明有环
            if (dataFast == dataSlow){
                return true ;
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
