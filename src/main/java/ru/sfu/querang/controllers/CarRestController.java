package ru.sfu.querang.controllers;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.sfu.querang.exceptions.notCreated.CarNotCreatedException;
import ru.sfu.querang.exceptions.notDeleted.CarNotDeletedException;
import ru.sfu.querang.exceptions.notFound.CarNotFoundException;
import ru.sfu.querang.exceptions.notUpdated.CarNotUpdatedException;
import ru.sfu.querang.models.Car;
import ru.sfu.querang.services.CarService;
import ru.sfu.querang.utils.CrudErrorHandlers;

@RestController
@RequestMapping("/cars/api")
public class CarRestController extends CrudErrorHandlers<CarNotCreatedException, CarNotFoundException, CarNotUpdatedException, CarNotDeletedException> {

    private final CarService service;

    @Autowired
    public CarRestController(CarService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Car>> getAll(
            @RequestParam(name = "price", required = false) Float price) {
        if (price != null) {
            return new ResponseEntity<>(service.filterByPrice(price), HttpStatus.OK);
        }
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Car> show(@PathVariable("id") int id) {
        return new ResponseEntity<>(service.findOne(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> create(
            @RequestBody @Valid Car car,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            throw new CarNotCreatedException(getError(bindingResult));
        }
        service.save(car);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> update(
            @RequestBody @Valid Car car,
            BindingResult bindingResult,
            @PathVariable("id") int id
    ) {
        if (bindingResult.hasErrors()) {
            throw new CarNotUpdatedException(getError(bindingResult));
        }
        try {
            service.update(id, car);
        } catch (CarNotFoundException e) {
            throw new CarNotUpdatedException(e.getMessage());
        }
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") int id) {
        try {
            service.delete(id);
        } catch (CarNotFoundException e) {
            throw new CarNotDeletedException(e.getMessage());
        }
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteAll() {
        service.wipe();
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<HttpStatus> fillExample() {
        service.fillExample();
        return ResponseEntity.ok(HttpStatus.OK);
    }

    private String getError(BindingResult bindingResult) {
        StringBuilder errorMsg = new StringBuilder();
        List<FieldError> errors = bindingResult.getFieldErrors();
        for (FieldError error:
                errors) {errorMsg.append(error.getField()).append(" - ").append(error.getDefaultMessage()).append(";");
        }
        return errorMsg.toString();
    }
}
