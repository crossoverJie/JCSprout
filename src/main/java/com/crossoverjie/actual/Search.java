package com.crossoverjie.actual;

/**
 * Function:
 *
 * @author crossoverJie
 *         Date: 2018/10/13 20:00
 * @since JDK 1.8
 */

import org.junit.Assert;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Function:
 *
 一个“.”代表一个任意字母。

 注意事项：可以假设所有的单词只包含小写字母“a-z”

 样例：
 addWord(“bad”);
 addWord(“dad”);
 addWord(“mad”);
 search(“pad”);  // return false;
 search(“bad”);  // return true;
 search(“.ad”); // return true;
 search(“b..”); // return true;


 如果有并发的情况下，addword() 怎么处理？
 *
 * @author crossoverJie
 * @since JDK 1.8
 */
public class Search {

    private static Map<String,String> ALL_MAP = new ConcurrentHashMap<>(50000) ;


    /**
     * 换成 ascii码 更省事
     */
    private static final char[] dictionary = {'a','b','c','d','m','p'} ;

    public static void main(String[] args) throws InterruptedException {


        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10000; i++) {
                    addWord(i + "ad");
                }
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10000; i++) {
                    addWord(i + "bd");
                }
            }
        });
        Thread t3 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10000; i++) {
                    addWord(i + "cd");
                }
            }
        });
        Thread t4 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10000; i++) {
                    addWord(i + "dd");
                }
            }
        });
        Thread t5 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10000; i++) {
                    addWord(i + "ed");
                }
            }
        });

        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
        t1.join();
        t2.join();
        t3.join();
        t4.join();
        t5.join();
        System.out.println(ALL_MAP.size());
        Assert.assertEquals(50000,ALL_MAP.size());


        addWord("bad");
        addWord("dad");
        addWord("mad");
        boolean pad = search("pad");
        System.out.println(pad);
        Assert.assertFalse(pad);

        boolean bad = search("bad");
        System.out.println(bad);
        Assert.assertTrue(bad);


        boolean ad = search(".ad");
        System.out.println(ad);
        Assert.assertTrue(ad);


        boolean bsearch = search("b..");
        System.out.println(bsearch);
        Assert.assertTrue(bsearch);

        boolean asearch = search(".a.");
        System.out.println(asearch);


        boolean search = search(".af");
        System.out.println(search);


        boolean search1 = search(null);
        System.out.println(search1);

    }

    public static boolean search(String keyWord){
        boolean result = false ;
        if (null ==  keyWord || keyWord.trim().equals("")){
            return result ;
        }

        //做一次完整匹配
        String whole = ALL_MAP.get(keyWord) ;
        if (whole != null){
            return true ;
        }

        char[] wordChars = keyWord.toCharArray() ;

        for (int i = 0; i < wordChars.length; i++) {
            char wordChar = wordChars[i] ;

            if (46 != (int)wordChar){
                continue ;
            }

            for (char dic : dictionary) {
                wordChars[i] = dic ;
                boolean search = search(String.valueOf(wordChars));

                if (search){
                    return search ;
                }

                String value = ALL_MAP.get(String.valueOf(wordChars));
                if (value != null){
                    return true ;
                }
            }

        }


        return result ;
    }


    public static void addWord(String word){
        ALL_MAP.put(word,word) ;
    }
}