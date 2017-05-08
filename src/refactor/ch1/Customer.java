package refactor.ch1;

import java.util.Enumeration;
import java.util.Vector;

/**
 * Name: admin
 * Date: 2017/5/8
 * Time: 14:03
 */
public class Customer {

    private String name;
    private Vector _rentals = new Vector();

    public Customer(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addRental(Rental arg) {
        _rentals.addElement(arg);
    }

    public String statement() {
       // double totalAmount = 0;
        int frequentRenterPoints = 0;
        Enumeration rentals = _rentals.elements();
        String result = "Rental Record for " + getName() + "\n";
        while (rentals.hasMoreElements()) {
         //   double thisAmount = 0;
            Rental each = (Rental) rentals.nextElement();

          //  thisAmount = each.getCharge();//第一次重构，提炼函数

            frequentRenterPoints = each.getFrequentRenterPoints();//第5次重构：提取函数+去除无用变量+move method
            result += "\t" + each.movie.getMovie().getTitle() + "\t" +
                    String.valueOf(each.getCharge()) + "\n";
          //  totalAmount += each.getCharge();//第4次重构，去除temp变量,但是付出了性能的代价，价钱被计算了两次，可以在Rental类中做优化
        }

        result += "Amount owed is " + String.valueOf(getTotalCharge()) + "\n";//第6次重构：将计算总价钱的功能提炼出来
        result += "You earned " + String.valueOf(getTotalFrequentRenterPoints()) +
                "frequent renter points";//第7次重构：将计算总f积分的功能提炼出来
        return result;
    }

    /**
     * 采用委托测试，重构3，保留旧函数，去调用新函数，看看是否重构OK
     * @param aRental
     * @return
     */
    public double amountFor(Rental aRental){
            return aRental.getCharge();
        }

    private double getTotalCharge(){
        double result = 0;
        Enumeration rentals = _rentals.elements();
        while (rentals.hasMoreElements()){
            Rental each = (Rental) rentals.nextElement();
            result += each.getCharge();
        }
        return  result;
    }

    private int getTotalFrequentRenterPoints(){
        int result = 0;
        Enumeration rentals = _rentals.elements();
        while (rentals.hasMoreElements()){
            Rental each = (Rental) rentals.nextElement();
            result += each.getFrequentRenterPoints();
        }
        return  result;
    }
}