package refactor.ch5;

import java.util.Date;

/**
 * Name: admin
 * Date: 2017/5/10
 * Time: 10:37
 */
public class Param {
    public static void main(String[] args) {
        Date d1 = new Date("1 Apr 98");
        nextDateUpdate(d1);
        System.out.println("d1 after nextDateUpdate is "+d1);

        Date d2 = new Date("1 Apr 98");
        nextDateReplace(d2);
        System.out.println("d2 afterDateReplace is" + d2);
    }

    private static void nextDateReplace(Date date) {
        date = new Date(date.getYear(),date.getMonth(),date.getDate()+1);
        System.out.println("date in nextDateReplace is "+ date);
    }

    private static void nextDateUpdate(Date date){
        date.setDate(date.getDate() + 1);
        System.out.println("date in nextDate is "+ date);
    }
}
