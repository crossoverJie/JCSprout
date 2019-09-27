package com.crossoverjie.algorithm;

import com.alibaba.fastjson.JSON;
import org.junit.Test;

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
    public int[] getTwo1(int[] array, int sum) {
        int[] result = null;
        for (int i = 0; i < array.length; i++) {
            int num1 = array[i];
            for (int j = array.length - 1; j >= 0; j--) {
                int num2 = array[j];
                if (i != j && (num1 + num2) == sum) {
                    result = new int[] { i, j };
                }
            }
        }
        return result;
    }

}