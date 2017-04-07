package se.ch2;

import se.ch2.javaini.CurrentConditionsDisplay2;
import se.ch2.javaini.WeatherData2;
import se.ch2.observer.CurrentConditionsDisplay;
import se.ch2.subject.WeatherData;

/**
 * Name: admin
 * Date: 2017/4/7
 * Time: 14:07
 */
public class WeatherStation {
    public static void main(String[] args) {
       /* WeatherData weatherData = new WeatherData();

        CurrentConditionsDisplay currentDisplay =
                new CurrentConditionsDisplay(weatherData);

        weatherData.setMeasurements(80, 65, 30.4f);
        weatherData.setMeasurements(80, 60, 29.4f);*/

        WeatherData2 weatherData = new WeatherData2();

        CurrentConditionsDisplay2 currentDisplay =
                new CurrentConditionsDisplay2(weatherData);

        weatherData.setMeasurements(80, 65, 30.4f);
        weatherData.setMeasurements(80, 60, 29.4f);
    }
}
