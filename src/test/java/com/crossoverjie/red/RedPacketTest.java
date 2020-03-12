package com.crossoverjie.red;

import org.junit.Test;

import java.util.List;

public class RedPacketTest {

    @Test
    public void right(){
        RedPacket redPacket = new RedPacket() ;
        List<Integer> redPackets = redPacket.splitRedPacket(20000, 100);
        System.out.println(redPackets) ;

        int sum = 0 ;
        for (Integer red : redPackets) {
            sum += red ;
        }
        System.out.println(sum);
    }

    @Test
    public void right_(){
        RedPacket redPacket = new RedPacket() ;
        List<Integer> redPackets = redPacket.splitRedPacket(40000, 2);
        System.out.println(redPackets) ;

        int sum = 0 ;
        for (Integer red : redPackets) {
            sum += red ;
        }
        System.out.println(sum);
    }

    @Test
    public void right__(){
        RedPacket redPacket = new RedPacket() ;
        List<Integer> redPackets = redPacket.splitRedPacket(100, 101);
        System.out.println(redPackets) ;

        int sum = 0 ;
        for (Integer red : redPackets) {
            sum += red ;
        }
        System.out.println(sum);
    }
}