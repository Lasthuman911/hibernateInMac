package se.Duck;

/**
 * Name: admin
 * Date: 2017/3/24
 * Time: 15:40
 */
public class Squeak implements QuackBehavior {
    @Override
    public void quack() {
        System.out.println("Squeak");
    }
}
