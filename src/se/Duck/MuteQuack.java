package se.Duck;

/**
 * Name: admin
 * Date: 2017/3/24
 * Time: 15:40
 */
public class MuteQuack implements QuackBehavior {
    @Override
    public void quack() {
        System.out.println("Silence");
    }
}
