package tdd;

import junit.framework.TestCase;
import tdd.ch1.Dollar;

/**
 * Created by lszhen on 2017/3/24.
 */
public class TddTest extends TestCase{


    public void testMultiplication(){
        Dollar five = new Dollar(5);
        five.times(2);
        assertEquals(10, five.getAmount());
    }
}
