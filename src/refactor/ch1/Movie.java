package refactor.ch1;

/**
 * Name: admin
 * Date: 2017/5/8
 * Time: 13:54
 */
public class Movie {

    public static final int CHILDRENS = 2;
    public static final int REGULAR = 0;
    public static final int NEW_RELEASE = 1;

    private String title;
    private int priceCode;

    public Movie(String title, int priceCode) {
        this.title = title;
        this.priceCode = priceCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPriceCode() {
        return priceCode;
    }

    public void setPriceCode(int priceCode) {
        this.priceCode = priceCode;
    }

    public Movie getMovie() {
        return this;
    }

    /**
     * 最好不要在另一个对象的属性上使用switch语句，若不得不使用，在应该在对象自己的数据上使用
     * @param daysRented
     * @return
     */
    //第二次重构：修改变量名称，第三次重构Move to Rental and change methodName
    //第9次重构：加入daysRented，move method to Movie
    public double getCharge(int daysRented) {
        double result = 0;
        switch (getPriceCode()) {
            case Movie.REGULAR:
                result += 2;
                if (daysRented > 2) {
                    result += (daysRented - 2) * 1.5;
                    break;
                }
            case Movie.NEW_RELEASE:
                result += daysRented * 3;
                break;
            case Movie.CHILDRENS:
                result += 1.5;
                if (daysRented > 3)
                    result += (daysRented - 3) * 1.5;
                break;
        }
        return result;
    }
}
