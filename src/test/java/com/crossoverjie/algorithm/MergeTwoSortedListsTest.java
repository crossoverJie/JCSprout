package com.crossoverjie.algorithm;

import com.crossoverjie.algorithm.MergeTwoSortedLists.ListNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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