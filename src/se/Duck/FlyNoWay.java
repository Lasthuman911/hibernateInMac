package se.Duck;

/**
 * Name: admin
 * Date: 2017/3/24
 * Time: 15:35
 */
public class FlyNoWay implements FlyBehavior {
    @Override
    public void fly() {
        System.out.println("I can't fly");
    }
}
