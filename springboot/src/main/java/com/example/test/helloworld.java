package com.example.test;


import com.example.entity.Admin;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class helloworld {
    public void sayHello() {
        System.out.println("Hello World!!!!!!!!");
    }
    public static void main(String[] args) {
        Admin admin = new Admin();
        admin.setUsername("xf");
        System.out.println(admin.getUsername());
    }
}
