package se.ch2.javaini;

import se.ch2.subject.DisplayElement;

import java.util.Observable;
import java.util.Observer;

/**
 * Name: admin
 * Date: 2017/4/7
 * Time: 14:33
 */
public class CurrentConditionsDisplay2 implements Observer, DisplayElement {
    private float temperature;
    private float humidity;
    Observable observable;

    public CurrentConditionsDisplay2(Observable observable) {
        this.observable = observable;
        observable.addObserver(this);
    }

    @Override
    public void update(Observable obs, Object arg) {
        if (obs instanceof WeatherData2) {
            WeatherData2 weatherData = (WeatherData2) obs;
            temperature = weatherData.getTemperature();
            humidity = weatherData.getHumidity();
            display();
        }
    }

    @Override
    public void display () {
        System.out.println("Current:" + temperature + " " + humidity);
    }
}
