package ru.sfu.querang.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.sfu.querang.models.Car;

/**
 * Репозиторий для работы с автомобилями
 */
@Repository
public interface CarRepository extends JpaRepository<Car, Integer> {

    /**
     * SQL-запрос для вставки готовых данных
     */
    String insertExampleSQL = """
      INSERT INTO car (car_name, brand, country, price, quantity)\s
      VALUES
          ('Solaris', 'Hyundai', 'Korea', 1000000.00, 200),
          ('Creta', 'Hyundai', 'Korea', 1700000.00, 230),
          ('Granta', 'Lada', 'Russia', 400000.00, 150),
          ('Camri', 'Toyota', 'Japan', 2500000.00, 220),
          ('RAV4', 'Toyota', 'Japan', 3500000.00, 270),
          ('BMW 5', 'BMW', 'Germany', 1600000.00, 225),
          ('Octavia', 'Skoda Auto', 'Czechia', 460000.00, 190),
          ('Rapid', 'Skoda Auto', 'Czechia', 600000.00, 210);
      """;

    /**
     * Поиск автомобилей по ниже заданной цены
     *
     * @param price  цена
     * @return  список автомобилей
     */
    List<Car> findByPriceLessThanEqual(float price);

    /**
     * Вставка автомобилей
     */
    @Modifying
    @Query(value = insertExampleSQL, nativeQuery = true)
    void insertExample();

}
