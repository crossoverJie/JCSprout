package com.crossoverjie.design.pattern.factorymethod;

/**
 * Function:
 *
 * @author crossoverJie
 *         Date: 19/03/2018 14:34
 * @since JDK 1.8
 */
public class Main {
    public static void main(String[] args) {
        AnimalFactory factory = new CatFactory() ;
        Animal animal = factory.createAnimal();
        animal.setName("猫咪");
        animal.desc();
    }
}
