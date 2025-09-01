package ru.factory.model.supplier;

import ru.factory.model.entities.CorePart;
import ru.factory.model.storage.Storage;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class Supplier<T extends CorePart> extends Thread {
    //время производства
    private int creationTime;
    //хранилище
    private final Storage<T> storage;
    //класс детали (для рефлексии)
    private final Class<T> clazz;
    public Supplier(Storage<T> storage, int creationTime, Class<T> clazz) {
        this.storage = storage;
        this.creationTime = creationTime;
        this.clazz = clazz;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            T part = createPart();
            storage.add(part);
            try {
                Thread.sleep(creationTime);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    //создать двигатель
    public T createPart() {
        try {
            Constructor<T> constructor = clazz.getDeclaredConstructor();
            return constructor.newInstance();
        }
        catch (NoSuchMethodException e) {
            throw new RuntimeException("No constructor found", e);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Error creating part object", e);
        }


    }
    //установить время выполнения
    public void setCreationTime(int millis) {
        creationTime=millis;
    }
}
