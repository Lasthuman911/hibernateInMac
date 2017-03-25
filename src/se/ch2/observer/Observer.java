package se.ch2.observer;

/**
 * Name: admin
 * Date: 2017/3/25
 * Time: 9:25
 */
public interface Observer {

    void update(float temp, float humidity, float pressure);
}
