package com.crossoverjie.algorithm;

import org.junit.Assert;
import org.junit.Test; 

public class HappyNumTest {
    @Test
    public void isHappy() {
        HappyNum happyNum = new HappyNum() ;
        boolean happy = happyNum.isHappy(1);
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
        boolean happy = happyNum.isHappy(7);
        Assert.assertEquals(happy,true);
    }

    @Test
    public void isHappy4() {
        HappyNum happyNum = new HappyNum() ;
        boolean happy = happyNum.isHappy(11);
        Assert.assertEquals(happy,false);
    }

}