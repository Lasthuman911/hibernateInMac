package Junit;

/**
 * Name: admin
 * Date: 2017/3/23
 * Time: 13:20
 */
public class Largest {


    public static int getLargest(int[] list){
        if (list.length ==0){
            throw new RuntimeException("Empty List");
        }
        int index;
        //int max = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (index = 0; index < list.length; index++){
            if (list[index] > max)
                max = list[index];
        }
        return max;
    }

}
