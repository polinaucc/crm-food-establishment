package com.crmfoodestablishment.paymentservice.publisher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQProducer {
    private RabbitTemplate template;
    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQProducer.class);

    public RabbitMQProducer(RabbitTemplate template) {
        this.template = template;
    }
    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.key.json}")
    private String routingJsonKey;


    public void sendMessage(String message) {
        LOGGER.info(String.format("Message send -> %s", message));
        template.convertAndSend(exchange, routingJsonKey, message);
    }
}
