package ru.sfu.querang.exceptions.notCreated;

public class CarNotCreatedException extends ru.sfu.querang.exceptions.notCreated.ModelNotCreatedException {

    public static final String entityType = "Car";

    public CarNotCreatedException(String msg) {super(entityType, msg);
    }
}
