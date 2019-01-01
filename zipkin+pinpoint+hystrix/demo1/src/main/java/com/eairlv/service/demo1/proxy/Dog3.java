package com.eairlv.service.demo1.proxy;

public class Dog3 implements Animal {

    private Dog dog;

    public Dog3(Dog dog) {
        this.dog = dog;
    }

    @Override
    public String wagTail() {
        return dog.wagTail();
    }
}
