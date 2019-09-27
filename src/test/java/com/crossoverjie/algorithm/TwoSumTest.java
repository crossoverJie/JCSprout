package com.crossoverjie.algorithm;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.Assert;

public class TwoSumTest {
    @Test
    public void getTwo1() throws Exception {
        TwoSum twoSum = new TwoSum() ;
        int[] nums ={1,3,5,7};
        int[] two1 = twoSum.getTwo1(nums, 12);
        System.out.println(JSON.toJSONString(two1));

    }

    @Test
    public void getTwo2(){
        TwoSum twoSum = new TwoSum() ;
        int[] nums ={1,3,5,7};
        int[] two = twoSum.getTwo2(nums, 10);
        System.out.println(JSON.toJSONString(two));

    }
    public void testGetTwo1_tc1() throws Exception {
        TwoSum twoSum = new TwoSum() ;
        int[] nums ={};
        int[] two1 = twoSum.getTwo1(nums, 1);
        // assertEquals(two1, new int[]{i,j}); 
        Assert.assertEquals(two1, null); 
    }
    @Test
    public void testGetTwo1_tc2() throws Exception {
        TwoSum twoSum = new TwoSum() ;
        int[] nums ={1};
        int[] two1 = twoSum.getTwo1(nums, 2);
        // assertEquals(two1, new int[]{i,j}); 
        Assert.assertArrayEquals(two1, null); 
    }
    @Test
    public void testGetTwo1_tc3() throws Exception {
        TwoSum twoSum = new TwoSum() ;
        int[] nums ={1,2};
        int[] two1 = twoSum.getTwo1(nums, 2);
        // assertEquals(two1, new int[]{i,j}); 
        Assert.assertArrayEquals(two1, null); 
    }
    @Test
    public void testGetTwo1_tc4() throws Exception {
        TwoSum twoSum = new TwoSum() ;
        int[] nums ={1,2};
        int[] two1 = twoSum.getTwo1(nums, 3);
        Assert.assertArrayEquals(two1, new int[]{1,0}); 
        // assertEquals(two1, null); 
    }
    @Test
    public void testGetTwo1_tc5() throws Exception {
        TwoSum twoSum = new TwoSum() ;
        int[] nums ={1,2,3};
        int[] two1 = twoSum.getTwo1(nums, 5);
        Assert.assertArrayEquals(two1, new int[]{2,1}); 
        // assertEquals(two1, null); 
    }

    @Test
    public void testGetTwo1_tc6() throws Exception {
        TwoSum twoSum = new TwoSum() ;
        int[] nums ={1,2,3,4,5,6,7,8,9,10};
        int[] two1 = twoSum.getTwo1(nums, 19);
        Assert.assertArrayEquals(two1, new int[]{9,8}); 
        // assertEquals(two1, null); 
    }
    @Test
    public void testGetTwo1_tc7() throws Exception {
        TwoSum twoSum = new TwoSum() ;
        int[] nums ={1};
        int[] two1 = twoSum.getTwo1(nums, 3);
        // assertEquals(two1, new int[]{0,1}); 
        Assert.assertArrayEquals(two1, null); 
    }
    @Test
    public void testGetTwo1_tc8() throws Exception {
        TwoSum twoSum = new TwoSum() ;
        int[] nums ={1,2,3};
        int[] two1 = twoSum.getTwo1(nums, 3);
        Assert.assertArrayEquals(two1, new int[]{1,0}); 
        // assertEquals(two1, null); 
    }
     @Test
    public void testGetTwo1_tc9() throws Exception {
        TwoSum twoSum = new TwoSum() ;
        int[] nums ={1,2,3,4,5,6,7,8,9,10,11};
        int[] two1 = twoSum.getTwo1(nums, 3);
        Assert.assertArrayEquals(two1, new int[]{1,0}); 
        // assertEquals(two1, null); 
    }

}