package com.crossoverjie.algorithm;

import org.junit.Test;

public class BinaryNodeTest {

    @Test
    public void test1(){
        BinaryNode node = new BinaryNode() ;
        //创建二叉树
        node = node.createNode() ;
        System.out.println(node);

        //层序遍历二叉树
        node.levelIterator(node) ;

    }

}