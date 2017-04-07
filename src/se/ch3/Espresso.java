package se.ch3;

/**
 * Name: admin
 * Date: 2017/4/7
 * Time: 16:09
 */
public class Espresso extends Beverage {

    public Espresso(){
        description = "Espresso";
    }
    @Override
    public double cost() {
        return 1.99;
    }
}
