package ru.sfu.querang.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.sfu.querang.models.Car;

@Service
public class ClientService {

    private final WebClient webClient;

    @Autowired
    public ClientService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<List<Car>> getAll() {
        return webClient.get().uri("/cars/api").retrieve().bodyToFlux(Car.class).collectList();
    }

    public Mono<Car> getById(int id) {
        return webClient.get().uri("/cars/api/" + id).retrieve().bodyToMono(Car.class);
    }

    public Mono<List<Car>> getFilteredByPrice(Float price) {
        return webClient.get().uri(uriBuilder -> uriBuilder.path("/cars/api").queryParam("price", price)
                        .build()).retrieve().bodyToFlux(Car.class).collectList();
    }

    public void create(Car car) {
        webClient.post().uri("/cars/api").contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(car), Car.class).retrieve().bodyToMono(Void.class).block();
    }

    public void update(int id, Car updatedCar) {
        webClient.put().uri("/cars/api/{id}", id).contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(updatedCar)).retrieve().bodyToMono(Void.class).block();
    }

    public void delete(int id) {
        webClient.delete().uri("/cars/api/{id}", id).retrieve().bodyToMono(Void.class).block();
    }
    public void wipeAll() {
        webClient.delete().uri("/cars/api").retrieve().bodyToMono(Void.class).block();
    }

    public void fillExample() {
        webClient.patch().uri("/cars/api").retrieve().bodyToMono(Void.class).block();
    }

}

