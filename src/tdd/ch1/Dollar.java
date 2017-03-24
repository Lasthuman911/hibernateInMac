package tdd.ch1;

/**
 * Created by lszhen on 2017/3/24.
 */
public class Dollar {

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    private int amount;
    public Dollar(int amount){
        this.amount = amount;
    }

    public void times(int multiplier){
        amount *= multiplier;
    }
}
