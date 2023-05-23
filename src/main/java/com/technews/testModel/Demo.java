package com.technews.testModel;

import java.util.Objects;

public class Demo {
    // Private variables are only accessible in the class in which they're defined
    private String name;
    private int age;
    // Constructor for Demo Model
    public Demo(String name, int age) {
        this.name = name;
        this.age = age;
    }

    // The following "getters" and "setters" Allow us to access the private variables of this class
    public String getName() {
        return name;
    }

    // The void key word when declaring a method allows the method to not require a return if one is not needed
    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    // @Override means that
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Demo demo = (Demo) o;
        return age == demo.age && Objects.equals(name, demo.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }

    @Override
    public String toString() {
        return "Demo{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
