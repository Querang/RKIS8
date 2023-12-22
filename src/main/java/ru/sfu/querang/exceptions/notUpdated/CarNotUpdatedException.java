package ru.sfu.querang.exceptions.notUpdated;

public class CarNotUpdatedException extends ru.sfu.querang.exceptions.notUpdated.ModelNotUpdatedException {

    public static final String entityType = "Car";

    public CarNotUpdatedException(String msg) {
        super(entityType, msg);
    }
}