package com.crossoverjie.design.pattern.factorymethod;

/**
 * Function:
 *
 * @author crossoverJie
 *         Date: 19/03/2018 14:29
 * @since JDK 1.8
 */
public abstract class Animal {

    private String name ;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 描述抽象方法
     */
    protected abstract void desc() ;
}
