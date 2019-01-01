package com.eairlv.service.demo1.proxy;

public class DogProxy implements Animal {

    private Animal animal;

    public DogProxy(Animal animal) {
        this.animal = animal;
    }

    @Override
    public String wagTail() {
        return animal.wagTail();
    }
}
