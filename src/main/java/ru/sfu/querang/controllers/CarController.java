package ru.sfu.querang.controllers;

import jakarta.validation.Valid;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.sfu.querang.exceptions.notFound.CarNotFoundException;
import ru.sfu.querang.messaging.MessageProducer;
import ru.sfu.querang.models.Car;
import ru.sfu.querang.services.CarService;

/**
 * Контроллер для работы с автомобилями
 */
@Controller
@RequestMapping("/cars")
public class CarController {

    private final MessageProducer messageProducer;

    private final CarService carService;

    private final String queueName;


    /**
     * Конструктор контроллера.
     *
     * @param carService сервис автомобиля
     * @param messageProducer отправщик сообщений
     */
    @Autowired
    public CarController(MessageProducer messageProducer, CarService carService, @Value("${queue.name}") String queueName) {
        this.messageProducer = messageProducer;
        this.carService = carService;
        this.queueName = queueName;
    }


    /**
     * Обрабатывает запрос на получение списка автомобилей
     *
     * @param price  цена фильтрации
     * @param model  объект модели
     * @return  представление списка автомобилей
     */
    @GetMapping()
    public String index(@RequestParam(name = "price", required = false) Float price, Model model) {
        if (price != null) {
            model.addAttribute("cars", carService.filterByPrice(price));
        } else {
            model.addAttribute("cars", carService.findAll());
        }
        return "cars/car_main";
    }

    /**
     * Обработка запроса на получение информации об автомобиля по id
     *
     * @param id  автомобиля
     * @param model  объект модели
     * @return  представление информации об автомобиле
     */
    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        model.addAttribute("car", carService.findOne(id));
        return "cars/show";
    }

    /**
     * Обрабатывает запрос на редактирование информации об автомобиля
     *
     * @param id  автомобиля
     * @param model  объект модели
     * @return  представление для редактирования
     */
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        model.addAttribute("car", carService.findOne(id));
        return "cars/edit";
    }

    /**
     * Обрабатывает запрос на создание автомобиля
     *
     * @param car объект автомобиля
     * @return  представление для создания автомобиля
     */
    @GetMapping("/new")
    public String newCar(@ModelAttribute("car") Car car) {
        return "cars/new";
    }

    /**
     * Обрабатывает запрос на сохранение автомобиля
     *
     * @param car  объект автомобиля
     * @param bindingResult  результаты валидации
     * @return  представление для создания автомобиля или перенаправление на список автомобилей
     */
    @PostMapping()
    public String create(
            @ModelAttribute("car") @Valid Car car,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) { String errors = bindingResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage).collect(Collectors.joining(", "));
            messageProducer.sendMessage("При добавлении объекта  произошла(и) ошибка(и): " + errors);
            return "cars/new";
        }
        messageProducer.sendMessage("Добавлен объект " + car);
        carService.save(car);
        return "redirect:/cars";
    }

    /**
     * Обрабатывает запрос на обновление информации об автомобиле
     *
     * @param car  объект автомобиля
     * @param bindingResult  результаты валидации
     * @param id  автомобиля
     * @return  представление для редактирования автомобиля или перенаправление на список автомобилей
     */
    @PatchMapping("/{id}")
    public String update(
            @ModelAttribute("car") @Valid Car car,
            BindingResult bindingResult,
            @PathVariable("id") int id
    ) {
        if (bindingResult.hasErrors()) { String errors = bindingResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage).collect(Collectors.joining(", "));

            messageProducer.sendMessage("При изменении объекта с id=" + id + " произошла(и) ошибка(и): " + errors);
            return "cars/edit";
        }
        try {
            carService.update(id, car);
            messageProducer.sendMessage("Объект с id=" + id + " изменен на " + car);
        } catch (CarNotFoundException e) {
            messageProducer.sendMessage("При изменении объекта с id=" + id + " произошла ошибка: " + e.getMessage());
        }
        return "redirect:/cars";
    }

    /**
     * Обрабатывает запрос на удаление автомобиля
     *
     * @param id  автомобиля
     * @return  перенаправление на список автомобилей
     */
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        try {
            carService.delete(id);
            messageProducer.sendMessage("Объект с id=" + id + " удален");
        } catch (CarNotFoundException e) {
            messageProducer.sendMessage("При удалении объекта с id=" + id + " произошла ошибка: " + e.getMessage());
        }
        return "redirect:/cars";
    }

    @PatchMapping("/{id}/buy")
    public String buy(@PathVariable("id") int id) {
        try {
            carService.buy(id);
            messageProducer.sendMessage("Произошла покупка id=" + id);
        } catch (CarNotFoundException e) {
            messageProducer.sendMessage("При покупке объекта с id=" + id + " произошла ошибка: " + e.getMessage());
        }
        return String.format("redirect:/cars/%d", id);
    }


    /**
     * Обрабатывает запрос на удаление всех автомобилей
     *
     * @return  перенаправление на список автомобилей
     */
    @DeleteMapping()
    public String deleteAll() {
        carService.wipe();
        messageProducer.sendMessage("Все объекты удалены");
        return "redirect:/cars";
    }

    /**
     * Обрабатывает запрос на заполнение готовых данных
     *
     * @return  перенаправление на список автомобилей
     */
    @PatchMapping()
    public String fillExample() {
        carService.fillExample();
        messageProducer.sendMessage("Таблица заполнена тестовыми данными");
        return "redirect:/cars";
    }
}
