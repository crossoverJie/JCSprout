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

    @Test
    public void testTwoSumTestCase1() throws Exception{
        TwoSum twoSum = new TwoSum() ;
        int[] nums ={};
        int[] two = twoSum.getTwo2(nums, 1);
        Assert.assertEquals(two, new int[]{0,0}); 
        System.out.println(JSON.toJSONString(two));
    }

    @Test
    public void testTwoSumTestCase2() throws Exception{
        TwoSum twoSum = new TwoSum() ;
        int[] nums ={1};
        int[] two = twoSum.getTwo2(nums, 3);
        Assert.assertEquals(two, new int[]{0,0}); 
        System.out.println(JSON.toJSONString(two));
    }

    @Test
    public void testTwoSumTestCase3() throws Exception{
        TwoSum twoSum = new TwoSum() ;
        int[] nums ={1,2};
        int[] two = twoSum.getTwo2(nums, 3);
        Assert.assertEquals(two, new int[]{0,1}); 
        System.out.println(JSON.toJSONString(two));
    }

    @Test
    public void testTwoSumTestCase4() throws Exception{
        TwoSum twoSum = new TwoSum() ;
        int[] nums ={1,2,4};
        int[] two = twoSum.getTwo2(nums, 6);
        Assert.assertEquals(two, new int[]{1,2}); 
        System.out.println(JSON.toJSONString(two));
    }

    @Test
    public void testTwoSumTestCase5() throws Exception{
        TwoSum twoSum = new TwoSum() ;
        int[] nums ={1,3,5,7,9,11};
        int[] two = twoSum.getTwo2(nums, 18);
        Assert.assertEquals(two, new int[]{3,5}); 
        System.out.println(JSON.toJSONString(two));
    }

}