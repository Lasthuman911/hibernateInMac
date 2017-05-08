package refactor.ch1;

/**
 * Name: admin
 * Date: 2017/5/8
 * Time: 14:02
 */
public class Rental {

    Movie movie;
    private int daysRented;

    public Rental(Movie movie, int daysRented) {
        this.movie = movie;
        this.daysRented = daysRented;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public int daysRented() {
        return daysRented;
    }

    public void setDaysRented(int daysRented) {
        this.daysRented = daysRented;
    }

    public double getCharge(){
        return movie.getCharge(daysRented);
    }

    public int getFrequentRenterPoints() {
        if ((movie.getMovie().getPriceCode() == Movie.NEW_RELEASE) && (
                daysRented() > 1)) {
            return 2;
        }else
        return 1;
    }
}
