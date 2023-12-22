package ru.sfu.querang;

import java.util.Scanner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.sfu.querang.messaging.MessageReceiver;

@SpringBootApplication
public class Application {

    public static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
        MessageReceiver messageReceiver = context.getBean(MessageReceiver.class);
        messageReceiver.startReceivingMessages();
    }

}
