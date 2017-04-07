package se.ch3;

/**
 * Name: admin
 * Date: 2017/4/7
 * Time: 16:03
 */
public abstract class Beverage {
    String description = "Unknown Beverage";

    public String getDescription(){
        return description;
    }

    public abstract double cost();
}
