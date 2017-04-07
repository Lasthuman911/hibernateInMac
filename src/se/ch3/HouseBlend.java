package se.ch3;

/**
 * Name: admin
 * Date: 2017/4/7
 * Time: 16:09
 */
public class HouseBlend extends Beverage {

    public HouseBlend(){
        description = "HouseBlend";
    }
    @Override
    public double cost() {
        return 0.99;
    }
}
