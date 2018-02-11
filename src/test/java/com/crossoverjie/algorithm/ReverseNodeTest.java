package com.crossoverjie.algorithm;

import org.junit.Test;
import static com.crossoverjie.algorithm.ReverseNode.Node;

public class ReverseNodeTest {

    @Test
    public void reverseNode1() throws Exception {
        ReverseNode.Node<String> node4 = new Node<>("4",null) ;
        Node<String> node3 = new Node<>("3",node4);
        Node<String> node2 = new Node<>("2",node3);
        Node<String> node1 = new Node("1",node2) ;

        ReverseNode reverseNode = new ReverseNode() ;
        reverseNode.reverseNode1(node1);
    }

    @Test
    public void reverseNode12() throws Exception {

        Node<String> node1 = new Node("1",null) ;

        ReverseNode reverseNode = new ReverseNode() ;
        reverseNode.reverseNode1(node1);
    }

    @Test
    public void reverseNode13() throws Exception {

        Node<String> node1 = null ;

        ReverseNode reverseNode = new ReverseNode() ;
        reverseNode.reverseNode1(node1);
    }


    /**
     * 头插法
     * @throws Exception
     */
    @Test
    public void reverseHead21() throws Exception {
        Node<String> node4 = new Node<>("4",null) ;
        Node<String> node3 = new Node<>("3",node4);
        Node<String> node2 = new Node<>("2",node3);
        Node<String> node1 = new Node("1",node2) ;

        ReverseNode reverseNode = new ReverseNode() ;
        reverseNode.reverseNode(node1);

    }


    @Test
    public void recNodeTest31(){
        Node<String> node4 = new Node<>("4",null) ;
        Node<String> node3 = new Node<>("3",node4);
        Node<String> node2 = new Node<>("2",node3);
        Node<String> node1 = new Node("1",node2) ;

        ReverseNode reverseNode = new ReverseNode() ;
        reverseNode.recNode(node1);
    }

}