package com.normal;

/**
 * 初始化的执行顺序，static代码块只执行一次
 * Name: admin
 * Date: 2017/4/8
 * Time: 8:39
 */
class Parent {

    static int a;
    static String b;

    {
        System.out.println("父类非静态代码块");
    }

    static {
        System.out.println("父类静态代码块");
    }

    public Parent() {
        System.out.println("父类构造函数");
        System.out.println("a = " + a + ", b = " + b);
        System.out.println("a = " + setA() + ", b = " + setB());
    }

    public static int setA() {
        a = 3;
        return a;
    }

    public static String setB() {
        return (b = "aaa");
    }
}

class Child extends Parent {
    {
        System.out.println("子类非静态代码块");
    }

    static {
        System.out.println("子类静态代码块");
    }

    public Child() {
        System.out.println("子类构造函数--------");
    }
}

public class StaticTest {
    public static void main(String[] args) {
        for (int i = 0; i < 3; i++) {
            new Child();
        }
    }
}
