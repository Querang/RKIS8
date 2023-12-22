package ru.sfu.querang.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "car")
public class Car {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "car_name")
    @Size(min = 3, max = 30, message = "Name is not between 3 and 30")
    private String name;

    @Size(min = 3, max = 30, message = "Brand's name length is not between 3 and 30")
    @Column(name = "brand")
    private String brand;

    @Size(min = 3, max = 30, message = "Country's name length is not between 3 and 30")
    @Column(name = "country")
    private String country;

    @Min(value = 1, message = "Price have to be more then 0")
    @Column(name = "price")
    private float price;

    @Min(value = 0, message = "Horsepower have to be 0 or more")
    @Column(name = "quantity")
    private int quantity;

    public Car(String name, String brand, String country, float price, int quantity) {
        this.name = name;
        this.brand = brand;
        this.country = country;
        this.price = price;
        this.quantity = quantity;
    }
}
