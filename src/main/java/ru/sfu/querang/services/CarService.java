package ru.sfu.querang.services;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sfu.querang.exceptions.notFound.CarNotFoundException;
import ru.sfu.querang.models.Car;
import ru.sfu.querang.repositories.CarRepository;

/**
 * Сервис для работы с автомобилями
 */
@Service
@Transactional(readOnly = true)
public class CarService {

    private final CarRepository repository;

    /**
     * Конструктор сервиса
     *
     * @param repository репозиторий автомобилей
     */
    @Autowired
    public CarService(
            CarRepository repository) {
        this.repository = repository;
    }

    /**
     * Получает все автомобили
     *
     * @return список всех автомобилей
     */
    public List<Car> findAll() {
        return repository.findAll().stream().filter(car -> car.getQuantity() > 0)
                .collect(Collectors.toList());
    }

    /**
     * Находит автомобиль по id
     *
     * @param id автомобиля
     * @return автомобиль
     */
    public Car findOne(int id) throws CarNotFoundException {
        return repository.findById(id).orElseThrow(() -> new CarNotFoundException(id));
    }

    /**
     * Сохраняет новый автомобиль
     *
     * @param car объект автомобиля
     */
    @Transactional
    public void save(Car car) {
        repository.save(car);
    }

    /**
     * Обновляет информацию об автомобиле
     *
     * @param id автомобиля
     * @param car  объект автомобиля
     */
    @Transactional
    public void update(int id, Car car) throws CarNotFoundException {
        if (repository.existsById(id)) {car.setId(id);
            repository.save(car);
        } else {
            throw new CarNotFoundException(id);
        }

    }

    /**
     * Удаляет автомобиль по id
     *
     * @param id автомобиля
     */
    @Transactional
    public void delete(int id) throws CarNotFoundException {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        } else {
            throw new CarNotFoundException(id);
        }
    }

    /**
     * Уменьшает число отвечающее за количество в бд
     *
     * @param id автомобиля
     */
    @Transactional
    public void buy(int id) {
        Car car = repository.findById(id).orElseThrow(
                () -> new CarNotFoundException(id)
        );
        if (car.getQuantity() > 0) {
            car.setQuantity(car.getQuantity() - 1);
            repository.save(car);
        } else {
            throw new CarNotFoundException("This car is out of stock");
        }

    }

    /**
     * Фильтрует автомобили по цене не выше максимальной
     *
     * @param maxPrice максимальная цена
     * @return список автомобилей с ценой ниже максимальной
     */
    public List<Car> filterByPrice(float maxPrice) {
        return repository.findByPriceLessThanEqual(maxPrice).stream()
                .filter(car -> car.getQuantity() > 0).collect(Collectors.toList());
    }


    /**
     * Заполняет бд готовыми данными
     */
    @Transactional
    public void fillExample() {
        repository.insertExample();
    }

    /**
     * Удаляет все автомобили
     */
    @Transactional
    public void wipe() {
        repository.deleteAll();
    }
}
