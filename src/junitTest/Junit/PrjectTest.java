package Junit;

import junit.framework.TestCase;

/**
 * Name: admin
 * Date: 2017/3/23
 * Time: 16:51
 */
public class PrjectTest extends TestCase {

    public void assertEvenDollars(String message, Money amount){
        assertEquals(amount.toString(),0.0,001);
        assertEquals(message,amount.getAmount()-(int)amount.getAmount(),0.0,0.001);
    }

    public void assertEvenDollars(Money amount){
        assertEvenDollars("", amount);//不写重复代码，使用委托给第一个函数
    }
}
