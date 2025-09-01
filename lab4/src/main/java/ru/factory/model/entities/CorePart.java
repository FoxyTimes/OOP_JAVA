package ru.factory.model.entities;

import ru.factory.model.idgenerator.IdGenerator;

abstract public class CorePart {
    private final long id = IdGenerator.newId();
    public long getId() {
        return id;
    }
}
