package com.crossoverjie.actual;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class AbstractMapTest {

    private final static Logger LOGGER = LoggerFactory.getLogger(AbstractMapTest.class);

    //hash值相同的字符串
    private final static String s1 = "CE_SxKD?V2ELWeRhnv`bD5Jpiq>gV7lpx9ZpPV?Qfo86afNblg>[w=JuJXrrXhoW";
    private final static String s2 = "_T`KhoXXuEw?T]Q`YqbsPuC7]sh@kO]VD@3nA:Z9rYWdPAMUa@DzRh?:n[^iko^\\";
    private final static String s3 = "vyqHAKlFv89cGZUj<dC=:ySwDIaNANYeC9^ZD<W7usF[:A\\cMXsX;[MVw\\X`j^le";
    private final static String s4 = ";nJAgZ32<69g\\o6O^<UjhbJRCN]AD`WDxhn[orjAFrQSo5ArQMf;RfKf@\\odVphd";
    private final static List<String> sList = Arrays.asList(s1, s2, s3, s4);
    @Test
    public void test(){
        LRUAbstractMap map = new LRUAbstractMap() ;
        map.put(1,1) ;
        map.put(2,2) ;

        Object o = map.get(1);
        LOGGER.info("getSize={}",map.size());

        map.remove(1) ;
        LOGGER.info("getSize"+map.size());
    }
    @Test
    public void testPut(){
        LRUAbstractMap map = new LRUAbstractMap() ;
        int count = 1;
        for (String s : sList) {
            System.out.println(s);
            map.put(s, count++);
        }
        for (String s : sList) {
            System.out.println(map.get(s));
        }
        for (String s : sList) {
            System.out.println(s);
            map.put(s, count++);
        }
        for (String s : sList) {
            System.out.println(map.get(s));
        }

    }

    public static void main(String[] args) {
        LRUAbstractMap map = new LRUAbstractMap() ;
        map.put(1,1) ;
        map.put(2,2) ;

        Object o = map.get(1);
        LOGGER.info("getSize={}",map.size());

        map.remove(1) ;
        map.remove(2) ;
        LOGGER.info("getSize"+map.size());
    }
}