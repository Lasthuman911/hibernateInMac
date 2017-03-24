package se.Duck;

import javax.print.Doc;

/**
 * Name: admin
 * Date: 2017/3/24
 * Time: 15:46
 */
public class MinuDuckSimulator {
    public static void main(String[] args) {
        Dock dock = new MallardDuck();
        dock.performFly();
        dock.performQuack();

        Dock dock1 = new ModulDuck();
        dock.performFly();
        dock.setFlyBehavior(new FlyRocketPowed());//动态的改变了行为，让模型鸭子具有了火箭飞行的能力
        dock.performFly();
    }
}
