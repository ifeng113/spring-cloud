package com.eairlv.service.demo1.proxy;

public class Run {

    public static void main(String[] args) {

        Dog dog = new Dog();
        System.out.println(dog.wagTail());

        Dog2 dog2 = new Dog2();
        System.out.println(dog2.wagTail());

        Dog3 dog3 = new Dog3(dog);
        System.out.println(dog3.wagTail());

        Dog3 dog32 = new Dog3(dog2);
        System.out.println(dog32.wagTail());

        DogProxy dogProxy = new DogProxy(dog2);
        System.out.println(dogProxy.wagTail());

    }
}
