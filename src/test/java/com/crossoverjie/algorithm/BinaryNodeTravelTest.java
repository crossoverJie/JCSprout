package com.crossoverjie.algorithm;

import org.junit.Test;

public class BinaryNodeTravelTest {
    @Test
    public void levelIterator() throws Exception {
        BinaryNodeTravel node = new BinaryNodeTravel() ;
        //创建二叉树
        node = node.createNode() ;

        //层序遍历二叉树
        BinaryNodeTravel binaryNodeTravel = node.levelIterator(node);

        while (binaryNodeTravel != null){
            System.out.print(binaryNodeTravel.getData() +"--->");
            binaryNodeTravel = binaryNodeTravel.next;
        }
    }

}