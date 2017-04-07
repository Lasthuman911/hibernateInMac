package se.ch3;

/**
 * Name: admin
 * Date: 2017/4/7
 * Time: 16:09
 */
public class Decaf extends Beverage {

    public Decaf(){
        description = "Decaf";
    }
    @Override
    public double cost() {
        return 3.99;
    }
}
