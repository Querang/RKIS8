package ru.sfu.querang.messaging;

import jakarta.annotation.PreDestroy;
import jakarta.jms.JMSException;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessageReceiver {

    private final JmsTemplate jmsTemplate;
    private final String queueName;
    private final ScheduledExecutorService scheduler;

    @Autowired
    public MessageReceiver(JmsTemplate jmsTemplate, @Value("${queue.name}") String queueName) {
        this.jmsTemplate = jmsTemplate;
        this.queueName = queueName;
        this.scheduler = Executors.newSingleThreadScheduledExecutor();;
    }

    public String receiveMessage() throws JMSException {
        try {
            String receivedMessage = (String) jmsTemplate.receiveAndConvert(queueName);
            return receivedMessage;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public void startReceivingMessages() {
        scheduler.scheduleAtFixedRate(() -> {
            try {
                String receivedMessage = receiveMessage();
                System.out.println(Objects.requireNonNullElse(receivedMessage, "No new messages."));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 200, TimeUnit.MILLISECONDS);
    }

    @PreDestroy
    public void stopReceivingMessages() {
        scheduler.shutdown();
        try {
            scheduler.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
