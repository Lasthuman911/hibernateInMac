package se.ch3;

/**
 * Name: admin
 * Date: 2017/4/7
 * Time: 16:09
 */
public class DarkRoast extends Beverage {

    public DarkRoast(){
        description = "DarkRoast";
    }
    @Override
    public double cost() {
        return 2.99;
    }
}
