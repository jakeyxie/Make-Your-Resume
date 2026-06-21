package com.example.test;

public class dog extends Animal{
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String name;
    public void wang(){
        System.out.println("wang\n");
        System.out.println("我是"+name+",我是"+bigName);
    }

}

