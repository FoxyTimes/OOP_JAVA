package ru.factory.model.entities;

import ru.factory.model.Jobs;

import java.util.ArrayList;

public class Car extends CorePart {
    ArrayList<CorePart> coreParts;
    public Car(ArrayList<CorePart> coreParts) {
        this.coreParts = coreParts;
    }

    public Body getBody() {
        return (Body) coreParts.get(Jobs.BODY.getValue());
    }

    public Motor getMotor() {
        return (Motor) coreParts.get(Jobs.MOTORS.getValue());
    }

    public Accessory getAccessory() {
        return (Accessory) coreParts.get(Jobs.ACCESSORY.getValue());
    }
}
