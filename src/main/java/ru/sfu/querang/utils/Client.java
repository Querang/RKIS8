package ru.sfu.querang.utils;

import static ru.sfu.querang.utils.TestLogger.log;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import ru.sfu.querang.models.Car;
import ru.sfu.querang.services.ClientService;

@Component
public class Client implements CommandLineRunner {

    private final ClientService clientService;
    List<Integer> ids = new ArrayList<>();
    List<Float> prices = new ArrayList<>();


    @Autowired
    public Client(ClientService clientService) {
        this.clientService = clientService;
    }

    public void showAll() {
        Mono<List<Car>> carsMono = clientService.getAll();
        carsMono.doOnSuccess(cars -> log("All notes:\n" + cars.stream()
                        .map(car -> {
                            ids.add(car.getId());
                            prices.add(car.getPrice());
                            return car.toString();
                        })
                        .collect(Collectors.joining("\n")), "Client"))
                .subscribeOn(Schedulers.boundedElastic()).block();
    }

    public void showOne(int id) {
        Mono<Car> carMono = clientService.getById(id);
        carMono.doOnSuccess(car -> log("Note id = " + id + ": " + car, "Client"))
                .subscribeOn(Schedulers.boundedElastic()).block();
    }

    public void showFilteredByPrice(Float price) {
        Mono<List<Car>> carsMono = clientService.getFilteredByPrice(price);
        carsMono.doOnSuccess(cars -> log("Note with max price " + price + " :\n" + cars.stream()
                        .map(Object::toString)
                        .collect(Collectors.joining("\n")), "Client"))
                .subscribeOn(Schedulers.boundedElastic()).block();
    }

    public void addCar() {
        Car car = new Car(
                "testName1",
                "testBrand1",
                "testCountry1",
                100000,
                100
        );
        log("Add car: " + car, "Client");
        clientService.create(car);
    }

    public void updateCar(int id) {
        Car car = new Car(
                "testName2",
                "testBrand2",
                "testCountry2",
                200000,
                200
        );
        log(String.format("Update car id = %d ", id) + car, "Client");
        clientService.update(id, car);
    }

    public void deleteCar(int id) {
        log(String.format("Delete car с id = %d ", id), "Client");
        clientService.delete(id);
    }

    public void wipeCars() {
        log("Wipe all data ", "Client");
        clientService.wipeAll();
    }

    public void fillByExample() {
        log("Insert test data ", "Client");
        clientService.fillExample();
    }


    @Override
    public void run(String... args) {
        String allowRun = System.getProperty("allow.run");

        if (!(allowRun == null || "true".equals(allowRun))) {
            return;
        }
        log("REST CLIENT STARTED", "Client");

        fillByExample();

        log("All notes output:", "Client");
        showAll();

        log("Output one note:", "Client");
        if (!ids.isEmpty()) {
            showOne(ids.get(0));
        } else {
            log("No notes in DB", "Client");
        }

        log("Output all notes filtered by max price:", "Client");
        if (!ids.isEmpty()) {
            showFilteredByPrice(
                    ((float) prices.stream().mapToDouble(Float::doubleValue).average().orElse(1000f)));
        } else {
            log("No note in DB", "Client");
        }

        log("Add note:", "Client");
        addCar();
        showAll();

        log("Edit notes:", "Client");
        if (!ids.isEmpty()) {
            updateCar(ids.get(0));
        } else {
            log("No notes in DB", "Client");
        }
        showAll();

        log("Delete note:", "Client");
        if (!ids.isEmpty()) {
            deleteCar(ids.get(0));
        } else {
            log("No notes in DB", "Client");
        }
        showAll();

        log("Wipe all data:", "Client");
        wipeCars();
        showAll();

        log("Application is working correctly");
    }
}
