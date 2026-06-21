package com.example.test;

public class Animal {
    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getBigName() {
        return bigName;
    }

    public void setBigName(String bigName) {
        this.bigName = bigName;
    }

    public double weight;
    public String bigName;
    public Animal() {}
}
