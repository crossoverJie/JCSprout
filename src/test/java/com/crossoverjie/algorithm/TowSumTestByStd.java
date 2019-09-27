package com.crossoverjie.algorithm;
import org.junit.Assert;
import org.junit.Test;

public class TwoSumTest {
  @Test
  public void twoSum1() throws Exception {
    TwoSum twoSum = new TwoSum();
    int[] nums = [];
    int[] two1 = twoSum.getTwo1(nums, 3);
    Assert.assertArrayEquals(two1, null);
  }

  @Test
  public void twoSum2() throws Exception {
    TwoSum twoSum = new TwoSum();
    int[] nums = [1, 2, 3, 4, 5, 6, 7];
    int[] two1 = twoSum.getTwo1(nums, 11);
    final int[] expected = new int[]{6, 3};
    Assert.assertArrayEquals(two1, expected);
  }

  @Test
  public void twoSum3() throws Exception {
    TwoSum twoSum = new TwoSum();
    int[] nums = [1, 2, 3, 4, 5, 6, 8, 9];
    int[] two1 = twoSum.getTwo1(nums, 14);
    final int[] expected = new int[]{7, 4};
    Assert.assertArrayEquals(two1, expected);
  }
}