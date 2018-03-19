package com.crossoverjie.design.pattern.factorymethod;

/**
 * Function:
 *
 * @author crossoverJie
 *         Date: 19/03/2018 15:21
 * @since JDK 1.8
 */
public class FishFactory implements AnimalFactory {
    @Override
    public Animal createAnimal() {
        return new Fish() ;
    }
}
