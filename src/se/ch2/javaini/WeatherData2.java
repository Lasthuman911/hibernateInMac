package se.ch2.javaini;

import java.util.Observable;

/**
 * Name: admin
 * Date: 2017/4/7
 * Time: 14:27
 */
public class WeatherData2 extends Observable {
    private float temperature;
    private float humidity;
    private float pressure;


    public WeatherData2(){}

    public void measurementsChanged(){
        setChanged();
        notifyObservers();//pull
    }

    public void setMeasurements(float temperature, float humidity, float pressure) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        measurementsChanged();
    }

    public float getTemperature() {
        return temperature;
    }

    public float getHumidity() {
        return humidity;
    }

    public float getPressure() {
        return pressure;
    }

}
