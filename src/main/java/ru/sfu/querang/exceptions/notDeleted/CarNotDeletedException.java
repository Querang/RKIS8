package ru.sfu.querang.exceptions.notDeleted;

public class CarNotDeletedException extends ru.sfu.querang.exceptions.notDeleted.ModelNotDeletedException {

    public static final String entityType = "Car";

    public CarNotDeletedException(String msg) {
        super(entityType, msg);
    }
}
