package com.crossoverjie.algorithm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Function: 在二维数组中判断是否存在查找的数字
 *
 * @author crossoverJie
 *         Date: 09/02/2018 22:19
 * @since JDK 1.8
 */
public class TwoArray {

    private final static Logger LOGGER = LoggerFactory.getLogger(TwoArray.class);




    public static void main(String[] args) {
        int[][] matrix = new int[][]{
                {1, 2, 8, 9},
                {2, 4, 9, 12},
                {4, 7, 10, 13},
                {6, 8, 11, 15},
                {7, 9, 12, 16}
        };

        // 数组的行数
        int rows = matrix.length;
        // 数组行的列数
        int cols = matrix[1].length;

        LOGGER.info(String.valueOf(rows));
        LOGGER.info(String.valueOf(cols));

    }



}
