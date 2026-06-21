package com.example.test;

import com.example.entity.Admin;
public class xiaoming extends student{
    public String name;
    xiaoming(){}
    @Override
    public void sayHello() {
        System.out.println("hello");
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
