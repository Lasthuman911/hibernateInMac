package se.Duck;

/**
 * Name: admin
 * Date: 2017/3/24
 * Time: 15:33
 */
public abstract class Dock {
    FlyBehavior flyBehavior;

    QuackBehavior quackBehavior;

    public abstract void display();//每个子类的display都不同，此方法由子类实现

    public void swing(){
        System.out.println("I can fly");//所有dock都会swing
    }

    public void performFly(){
        flyBehavior.fly();//委托给flyBehavior
    }

    public void performQuack(){
        quackBehavior.quack();//委托给quackBehavior
    }

    public FlyBehavior getFlyBehavior() {
        return flyBehavior;
    }

    public void setFlyBehavior(FlyBehavior flyBehavior) {
        this.flyBehavior = flyBehavior;
    }

    public QuackBehavior getQuackBehavior() {
        return quackBehavior;
    }

    public void setQuackBehavior(QuackBehavior quackBehavior) {
        this.quackBehavior = quackBehavior;
    }
}
