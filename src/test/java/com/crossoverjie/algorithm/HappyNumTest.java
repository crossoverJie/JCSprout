package com.crossoverjie.algorithm;

import org.junit.Assert;
import org.junit.Test;

public class HappyNumTest {
    @Test
    public void isHappy() throws Exception {
        HappyNum happyNum = new HappyNum() ;
        boolean happy = happyNum.isHappy(19);
        Assert.assertEquals(happy,true);
    }

    @Test
    public void isHappy2() throws Exception {
        HappyNum happyNum = new HappyNum() ;
        boolean happy = happyNum.isHappy(11);
        Assert.assertEquals(happy,false);
    }

    @Test
    public void isHappy3() throws Exception {
        HappyNum happyNum = new HappyNum() ;
        boolean happy = happyNum.isHappy(100);
        System.out.println(happy);
    }

}