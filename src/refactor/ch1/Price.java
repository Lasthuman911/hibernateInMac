package refactor.ch1;

/**
 * Name: admin
 * Date: 2017/5/9
 * Time: 9:07
 */
public abstract class Price {
    abstract int getPriceCode();
    public abstract double getCharge(int daysRented);

    /**
     * 重构11：处理完所有case之后，将getCharge设置为abstract
     */
/*    public double getCharge(int daysRented) {
        double result = 0;
        switch (getPriceCode()) {
*//*            case Movie.REGULAR:
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
                break;*//*
        }

        return result;
    }*/


    public int getFrequentRenterPoints(int daysRented) {
            return 1;
    }
}

class ChildrensPrice extends Price {

    @Override
    int getPriceCode() {
        return Movie.CHILDRENS;
    }

    @Override
    public double getCharge(int daysRented) {
        double result = 1.5;
        if (daysRented > 3)
            result += (daysRented - 3) * 1.5;

        return result;
    }
}

class NewReleasePrice extends Price {

    @Override
    int getPriceCode() {
        return Movie.NEW_RELEASE;
    }

    @Override
    public double getCharge(int daysRented) {
        return daysRented * 3;
    }

    @Override
    public int getFrequentRenterPoints(int daysRented) {
        return (daysRented > 1) ? 2 : 1;
    }
}

class RegularPrice extends Price {

    @Override
    int getPriceCode() {
        return Movie.REGULAR;
    }

    @Override
    public double getCharge(int daysRented) {
        double result = 2;
        if (daysRented > 2) {
            result += (daysRented - 2) * 1.5;
        }
        return result;
    }
}