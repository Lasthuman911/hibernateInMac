package se.Duck;

/**
 * Name: admin
 * Date: 2017/3/24
 * Time: 15:47
 */
public class MallardDuck extends Dock {

    public MallardDuck(){
        quackBehavior = new Quack();
        flyBehavior = new FlyWithWings();
    }

    @Override
    public void display() {
        System.out.println("MallardDock display");
    }
}
