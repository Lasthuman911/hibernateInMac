package se.Duck;

/**
 * Name: admin
 * Date: 2017/3/24
 * Time: 15:53
 */
public class ModulDuck extends Dock {

    public ModulDuck(){
        flyBehavior = new FlyNoWay();
        quackBehavior = new Squeak();
    }

    @Override
    public void display() {
        System.out.println("lalalala");
    }
}
