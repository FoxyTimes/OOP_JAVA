package ru.factory.model.storage;

import ru.factory.model.entities.CorePart;
import ru.factory.model.transfer.DataObject;
import ru.factory.observer.Notifications;
import ru.factory.observer.Observable;
import ru.factory.observer.Observer;

import javax.swing.plaf.PanelUI;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

public class Storage<T extends CorePart> implements Observable {
    ArrayList<Observer> observers = new ArrayList<>();

    AtomicInteger totalParts = new AtomicInteger(0);

    private final Object LOCK_SIZES = new Object();

    //хранилище с типом деталей T
    private final Queue<T> parts = new LinkedList<>();
    private int maxSize = 0;
    public Storage(int maxSize) {
        this.maxSize = maxSize;
    }
    //добавляем на склад
    public void add(T item) {
        synchronized (parts) {
            try {
                while (parts.size() == maxSize) {
                    parts.wait();
                }
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
            parts.add(item);
            totalParts.incrementAndGet();
            parts.notifyAll();
            notifyObservers(Notifications.PROGRESS_UPDATED, null);
        }
    }

    public AtomicInteger getTotalParts() {
        return totalParts;
    }

    //забираем со склада
    public T poll() {
        synchronized (parts) {
            try {
                while (parts.isEmpty()) {
                    parts.wait();
                }
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            T part = parts.poll();
            parts.notifyAll();
            notifyObservers(Notifications.PROGRESS_UPDATED, null);
            notifyObservers(Notifications.OBJECT_REMOVED, null);
            return part;
        }
    }

    public int getSize() {
        synchronized (LOCK_SIZES) {
            return parts.size();
        }
    }

    public int getMaxSize() {
        synchronized (LOCK_SIZES) {
            return maxSize;
        }
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(Notifications notification, DataObject data) {
        if (!observers.isEmpty()) {
            for (Observer observer : observers) {
                observer.sendNotify(notification, data);
            }
        }
    }
}
