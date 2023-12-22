package ru.sfu.querang.exceptions.notFound;

public class CarNotFoundException extends ru.sfu.querang.exceptions.notFound.ModelNotFoundException {

    public static final String entityType = "Car";

    public CarNotFoundException(String msg) {
        super(entityType, msg);
    }

    public CarNotFoundException(int id) {
        super(entityType, id);
    }

}
