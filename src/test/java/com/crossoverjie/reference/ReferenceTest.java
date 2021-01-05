package com.crossoverjie.reference;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Function:
 *
 * @author crossoverJie
 * Date: 2020/12/19 00:18
 * @since JDK 11
 */
public class ReferenceTest {

    @Test
    public void testBasic() {
        int a = 10;
        modifyBasic(a);
        System.out.println(String.format("Final main a==%s", a));
    }


    private void modifyBasic(int aa) {
        System.out.println(String.format("Before modify aa==%s", aa));
        aa = 20;
        System.out.println(String.format("After modify aa==%s", aa));
    }

    @Test
    public void testReference01(){
        Car car1 = new Car("benz");
        modifyCar1(car1);
        System.out.println(String.format("Final modify car1==%s", car1));
    }

    private void modifyCar1(Car car){
        System.out.println(String.format("Before modify1 car==%s", car));
        car.name = "bwm";
        System.out.println(String.format("After modify1 car==%s", car));
    }

    @Test
    public void testReference02(){
        Car car1 = new Car("benz");
        modifyCar(car1);
        System.out.println(String.format("Final modify car1==%s", car1));
    }

    private void modifyCar(Car car2) {
        System.out.println(String.format("Before modify car2==%s", car2));
        car2 = new Car("bmw");
        System.out.println(String.format("After modify car2==%s", car2));
    }

    private class Car{
        private String name;

        public Car(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Car{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }

    @Test
    public void testReference03() {
        List<Integer> a = new ArrayList<>();
        a.add(1);
        modifyReference02(a);
        System.out.println(String.format("Final main a==%s", a));

    }

    private void modifyReference02(List<Integer> aa) {
        System.out.println(String.format("Before modify aa==%s", aa));
        aa.add(2);
        System.out.println(String.format("After modify aa==%s", aa));
    }
}
