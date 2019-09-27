package com.crossoverjie.algorithm;

import com.alibaba.fastjson.JSON;
import org.junit.Test;

public class TwoSumTest {
    @Test
    public void getTwo1() throws Exception {
        TwoSum twoSum = new TwoSum();
        int[] nums = { 1, 3, 5, 7 };
        int[] two1 = twoSum.getTwo1(nums, 12);
        System.out.println(JSON.toJSONString(two1));

    }

    @Test
    public void getTwo2() {
        TwoSum twoSum = new TwoSum();
        int[] nums = { 1, 3, 5, 7 };
        int[] two = twoSum.getTwo2(nums, 10);
        System.out.println(JSON.toJSONString(two));

    }

    @Test
    public void testGetTwo1Test1() throws Exception {
        TwoSum twoSum = new TwoSum() ;
        int[] nums ={};
        int[] two1 = twoSum.getTwo1(nums, 1);
        Assert.assertEquals(two1, null); 
    }

  @Test
  public void testGetTwo1Test2() throws Exception {
    TwoSum twoSum = new TwoSum();
    int[] nums = {1, 2, 3, 4, 5, 6, 8, 9};
    int[] two1 = twoSum.getTwo1(nums, 14);
    final int[] expected = new int[]{7, 4};
    Assert.assertArrayEquals(two1, expected);
  }

}