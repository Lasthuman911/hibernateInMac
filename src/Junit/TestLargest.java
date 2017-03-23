package Junit;

import junit.framework.TestCase;

/**
 * Name: admin
 * Date: 2017/3/23
 * Time: 13:26
 */
public class TestLargest extends TestCase {

    public void testSimple2() throws Exception {
        int[] arr = new int[3];
        arr[0] = 7;
        arr[1] = 8;
        arr[2] = 9;
        assertEquals(9, Largest.getLargest(arr));
    }
}
