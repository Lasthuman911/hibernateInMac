package se.ch2.subject;

import se.ch2.observer.Observer;

/**
 * Name: admin
 * Date: 2017/3/25
 * Time: 9:22
 */
public interface Subject {

    void registerObserver(Observer o);
    void removeObserver(Observer o);
    void notifyObservers();
}
