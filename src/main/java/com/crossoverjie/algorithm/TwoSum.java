package com.crossoverjie.algorithm;

/**
 * Function:
 *
 * @author crossoverJie
 *         Date: 04/01/2018 09:53
 * @since JDK 1.8
 */
public class TwoSum {

    public int[] getTwo1(int[] nums,int target){
        int[] result = new int[2] ;

        for (int i= 0 ;i<nums.length ;i++){
            int a = nums[i] ;
            for (int j = nums.length -1 ;j >=0 ;j--){
                int b = nums[j] ;

                if ((a+b) == target){
                    result = new int[]{i,j} ;
                }
            }
        }
        return result ;
    }
}
