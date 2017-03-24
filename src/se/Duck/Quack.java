package se.Duck;

/**
 * Name: admin
 * Date: 2017/3/24
 * Time: 15:39
 */
public class Quack implements QuackBehavior {
    @Override
    public void quack() {
        System.out.println("Quack");
    }
}
