package com.example.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Value("${app.rabbitmq.exchange}")
    private String exchangeName;

    @Value("${app.rabbitmq.send-queue}")
    private String sendQueue;

    @Value("${app.rabbitmq.listen-queue}")
    private String listenQueue;

    @Value("${app.rabbitmq.send-routing-key}")
    private String sendRoutingKey;

    @Value("${app.rabbitmq.listen-routing-key}")
    private String listenRoutingKey;

    @Value("${app.rabbitmq.listen-queue-update}")
    private String listenQueueUpdate;

    @Value("${app.rabbitmq.listen-routing-key-update}")
    private String listenRoutingKeyUpdate;

    @Bean
    public Queue sendQueue() {
        return new Queue(sendQueue, true);
    }

    @Bean
    public Queue listenQueue() {
        return new Queue(listenQueue, true);
    }

    @Bean
    public Queue listenQueueUpdate() {
        return new Queue(listenQueueUpdate, true);
    }

    @Bean
    public Binding bindingSendQueue(@Qualifier("sendQueue") Queue sendQueue, DirectExchange exchange) {
        return BindingBuilder.bind(sendQueue).to(exchange).with(sendRoutingKey);
    }

    @Bean
    public Binding bindingListenQueue(@Qualifier("listenQueue") Queue listenQueue, DirectExchange exchange) {
        return BindingBuilder.bind(listenQueue).to(exchange).with(listenRoutingKey);
    }

    @Bean
    public Binding bindingListenQueueUpdate(
            @Qualifier("listenQueueUpdate") Queue listenQueueUpdate, DirectExchange exchange) {
        return BindingBuilder.bind(listenQueueUpdate).to(exchange).with(listenRoutingKeyUpdate);
    }

    @Bean
    public DirectExchange userExchange() {
        return new DirectExchange(exchangeName);
    }

    // 👇 Dùng JSON converter để gửi message
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // RabbitTemplate dùng để gửi message
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter converter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(converter); // Sử dụng JSON converter
        return template;
    }
}
