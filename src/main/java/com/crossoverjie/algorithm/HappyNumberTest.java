package com.crossoverjie.algorithm;

import org.junit.Assert;
import org.junit.Test; 

public class HappyNumTest {
    @Test
    public void isHappy() {
        HappyNum happyNum = new HappyNum() ;
        boolean happy = happyNum.isHappy(-1);
        Assert.assertEquals(happy,true);
    }

    @Test
    public void isHappy2() {
        HappyNum happyNum = new HappyNum() ;
        boolean happy = happyNum.isHappy(0);
        Assert.assertEquals(happy,false);
    }

    @Test
    public void isHappy3() {
        HappyNum happyNum = new HappyNum() ;
        boolean happy = happyNum.isHappy(1);
        System.out.println(happy);
    }

    @Test
    public void isHappy4() {
        HappyNum happyNum = new HappyNum() ;
        boolean happy = happyNum.isHappy(10);
        System.out.println(happy);
    }

    @Test
    public void isHappy5() {
        HappyNum happyNum = new HappyNum() ;
        boolean happy = happyNum.isHappy(11);
        System.out.println(happy);
    }
}