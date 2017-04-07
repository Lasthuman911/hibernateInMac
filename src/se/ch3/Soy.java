package se.ch3;

/**
 * Name: admin
 * Date: 2017/4/7
 * Time: 16:12
 */
public class Soy extends CondimentDecorator {

    Beverage beverage;

    public Soy(Beverage beverage) {
        this.beverage = beverage;
    }

    @Override
    public String getDescription() {
        return beverage.getDescription() + ", Soy";
    }

    @Override
    public double cost() {
        return 0.30 + beverage.cost();
    }
}
