package com.crossoverjie.algorithm;

import java.util.HashSet;
import java.util.Set;

/**
 * Function: 判断一个数字是否为快乐数字 19 就是快乐数字  11就不是快乐数字
 *
 * @author crossoverJie
 *         Date: 04/01/2018 14:12
 * @since JDK 1.8
 */
public class HappyNum {

    public boolean isHappy(int number) {
        Set<Integer> set = new HashSet<>(30);
        while (number != 1) {
            int sum = 0;
            while (number > 0) {
                //计算当前值的每位数的平方 相加的和 在放入set中，如果存在相同的就认为不是 happy数字
                sum += (number % 10) * (number % 10);
                number = number / 10;
            }
            if (set.contains(sum)) {
                return false;
            } else {
                set.add(sum);
            }
            number = sum;
        }
        return true;
    }


    public static void main(String[] args) {
        int num = 345;
        int i = num % 10;
        int i1 = num / 10;
        int i2 = i1 / 10;
        System.out.println(i);
        System.out.println(i1);
        System.out.println(i2);
    }
}
