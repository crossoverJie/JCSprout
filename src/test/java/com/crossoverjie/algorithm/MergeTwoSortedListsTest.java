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
    @Test
    public void testMergeTwoLists_tc1() throws Exception {
        ListNode mergedList = mergeTwoSortedLists.mergeTwoLists(null, null);
	Assert.assertEquals(mergedList,null);
    }
    @Test
    public void testMergeTwoLists_tc2() throws Exception {
        ListNode listNode1 = new ListNode(1);
        ListNode expectedOutput = new ListNode(1);
        ListNode mergedList = mergeTwoSortedLists.mergeTwoLists(listNode1, null);
	Assert.assertEquals(mergedList.toString(),expectedOutput.toString());
    }
    @Test
    public void testMergeTwoLists_tc3() throws Exception {
        ListNode listNode2 = new ListNode(1);
        ListNode expectedOutput = new ListNode(1);
        ListNode mergedList = mergeTwoSortedLists.mergeTwoLists(null, listNode2);
	Assert.assertEquals(mergedList.toString(),expectedOutput.toString());
    }
    @Test
    public void testMergeTwoLists_tc4() throws Exception {
        ListNode listNode1 = new ListNode(1);
        ListNode listNode2 = new ListNode(2);
        ListNode expectedOutput = new ListNode(1);
        expectedOutput.next = new ListNode(2);
        ListNode mergedList = mergeTwoSortedLists.mergeTwoLists(listNode1, listNode2);
	Assert.assertEquals(mergedList.toString(),expectedOutput.toString());
    }
    @Test
    public void testMergeTwoLists_tc5() throws Exception {
        ListNode listNode1 = new ListNode(2);
        ListNode listNode2 = new ListNode(1);
        ListNode expectedOutput = new ListNode(1);
        expectedOutput.next = new ListNode(2);
        ListNode mergedList = mergeTwoSortedLists.mergeTwoLists(listNode1, listNode2);
	Assert.assertEquals(mergedList.toString(),expectedOutput.toString());
    }
    @Test
    public void testMergeTwoLists_tc7() throws Exception {
        ListNode listNode1 = new ListNode(1);
        listNode1.next = new ListNode(3);
        ListNode listNode2 = new ListNode(2);
        listNode2.next = new ListNode(4);
        ListNode expectedOutput = new ListNode(1);
        ListNode index = expectedOutput;
        for(int i=0; i<3; i++){
            index.next = new ListNode(i+2);
            index = index.next;
        }
        ListNode mergedList = mergeTwoSortedLists.mergeTwoLists(listNode1, listNode2);
	Assert.assertEquals(mergedList.toString(),expectedOutput.toString());
    }
}