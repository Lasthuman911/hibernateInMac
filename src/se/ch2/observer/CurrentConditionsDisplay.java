package se.ch2.observer;

import se.ch2.subject.DisplayElement;
import se.ch2.subject.Subject;

/**
 * Name: admin
 * Date: 2017/3/25
 * Time: 11:08
 */
public class CurrentConditionsDisplay implements Observer,DisplayElement {
    private float temperature;
    private float humidity;
    private Subject weatherData;

    public CurrentConditionsDisplay(Subject weatherData){
        this.weatherData = weatherData;
        weatherData.registerObserver(this);
    }

    @Override
    public void update(float temp, float humidity, float pressure) {
        this.temperature = temp;
        this.humidity = humidity;
        display();
    }

    @Override
    public void display() {
        System.out.println("Current:"+temperature+" "+humidity);
    }
}
