package com.crossoverjie.algorithm;

import org.junit.Assert;
import org.junit.Test;

public class LinkedListMergeSortTest {

  @Test
  public void constructorOutputVoid() {

    // Act, creating object to test constructor
    final LinkedListMergeSort objectUnderTest = new LinkedListMergeSort();

    // Method returns void, testing that no exception is thrown
  }

  @Test
  public void mergeListNotNull() {
    // Arrange
    final LinkedListMergeSort.Node left = new LinkedListMergeSort.Node(-2_147_483_647, null);
    final LinkedListMergeSort.Node right = new LinkedListMergeSort.Node(0, null);

    // Act
    final LinkedListMergeSort.Node retval = new LinkedListMergeSort().mergeList(left, right);

    // Assert result
    Assert.assertNotNull(retval);
    Assert.assertEquals(0, retval.e);
    Assert.assertNotNull(retval.next);
    Assert.assertEquals(-2_147_483_647, retval.next.e);
    Assert.assertNull(retval.next.next);
  }

  @Test
  public void mergeListTwo() {
    // Arrange
    final LinkedListMergeSort.Node left = new LinkedListMergeSort.Node(1, null);
    final LinkedListMergeSort.Node right = new LinkedListMergeSort.Node(-2_147_483_648, null);

    // Act
    final LinkedListMergeSort.Node retval = new LinkedListMergeSort().mergeList(left, right);

    // Assert result
    Assert.assertNotNull(retval);
    Assert.assertEquals(1, retval.e);
    Assert.assertNotNull(retval.next);
    Assert.assertEquals(-2_147_483_648, retval.next.e);
    Assert.assertNull(retval.next.next);
  }

  @Test
  public void mergeListRightNull()  {
    // Arrange
    final LinkedListMergeSort.Node left = new LinkedListMergeSort.Node(-2_147_483_647,null);
    final LinkedListMergeSort.Node right = null;

    // Act
    final LinkedListMergeSort.Node retval = new LinkedListMergeSort().mergeList(left, right);

    // Assert result
    Assert.assertNotNull(retval);
    Assert.assertEquals(-2_147_483_647, retval.e);
    Assert.assertNull(retval.next);
  }

  @Test
  public void mergeListLeftNull()  {
    // Arrange
    final LinkedListMergeSort.Node left = null;
    final LinkedListMergeSort.Node right = new LinkedListMergeSort.Node(0, null);

    // Act
    final LinkedListMergeSort.Node retval = new LinkedListMergeSort().mergeList(left, right);

    // Assert result
    Assert.assertNotNull(retval);
    Assert.assertEquals(0, retval.e);
    Assert.assertNull(retval.next);
  }

  @Test
  public void mergeListBothNull() {
    // Arrange
    final LinkedListMergeSort.Node left = null;
    final LinkedListMergeSort.Node right = null;

    // Act
    final LinkedListMergeSort.Node retval = new LinkedListMergeSort().mergeList(left, right);

    // Assert result
    Assert.assertNull(retval);
  }

  @Test
  public void mergeSortLength2() {
    // Arrange
    final LinkedListMergeSort.Node node =  new LinkedListMergeSort.Node(0, null);
    final LinkedListMergeSort.Node first = new LinkedListMergeSort.Node(-2_147_483_647, node);
    final int length = 2;

    // Act
    final LinkedListMergeSort.Node retval = new LinkedListMergeSort().mergeSort(first, length);

    // Assert result
    Assert.assertNotNull(retval);
    Assert.assertEquals(0, retval.e);
    Assert.assertNotNull(retval.next);
    Assert.assertEquals(-2_147_483_647, retval.next.e);
    Assert.assertNull(retval.next.next);
  }

  @Test
  public void mergeSortNull() {
    // Arrange
    final LinkedListMergeSort.Node first = null;
    final int length = 1;

    // Act
    final LinkedListMergeSort.Node retval = new LinkedListMergeSort().mergeSort(first, length);;

    // Assert result
    Assert.assertNull(retval);
  }

  @Test
  public void mainTest() {
    // Arrange
    final String[] args = {};

    // Act
    LinkedListMergeSort.main(args);

    // Method returns void, testing that no exception is thrown
  }
}
