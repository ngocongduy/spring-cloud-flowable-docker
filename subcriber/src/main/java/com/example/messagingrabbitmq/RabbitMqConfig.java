package com.example.messagingrabbitmq;

import com.rabbitmq.client.Connection;
//import com.rabbitmq.client.ConnectionFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Configuration
public class RabbitMqConfig {

    static final String topicExchangeName = "spring-boot-exchange";

    static final String queueName = "spring-boot";
    static final String otherQueueName = "other-spring-boot";

//    @Bean
//    com.rabbitmq.client.ConnectionFactory factory() {
//        com.rabbitmq.client.ConnectionFactory factory = new com.rabbitmq.client.ConnectionFactory();
//        return factory;
//    }
//    @Bean
//    Connection con(com.rabbitmq.client.ConnectionFactory factory) throws IOException, TimeoutException {
////        ConnectionFactory factory = new ConnectionFactory();
//        // "guest"/"guest" by default, limited to localhost connections
//        factory.setUsername("guest");
//        factory.setPassword("guest");
//        factory.setVirtualHost("/");
//        factory.setHost("localhost");
//        factory.setPort(5672);
//
//        Connection conn = factory.newConnection();
//        return conn;
//    }


//    @Bean
//    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
//                                             MessageListenerAdapter listenerAdapter) {
//        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
//        container.setConnectionFactory(connectionFactory);
//        container.setQueueNames(queueName);
//        container.setMessageListener(listenerAdapter);
//        return container;
//    }
//
//    @Bean
//	MessageListenerAdapter listenerAdapter(Receiver receiver) {
//		return new MessageListenerAdapter(receiver, "receiveMessage");
//	}
//    @Bean
//    MessageListenerAdapter otherListenerAdapter(Receiver receiver) {
//        return new MessageListenerAdapter(receiver, "receiveMessageFromOtherQueue");
//    }

//    @Bean
//    public SimpleMessageListenerContainer simpleRabbitListener1(
//            final ConnectionFactory connectionFactory,
//            MessageListenerAdapter listenerAdapter
//    ) {
//        SimpleMessageListenerContainer listenerContainer = new SimpleMessageListenerContainer(connectionFactory);
//        listenerContainer.setQueueNames(queueName);
//        listenerContainer.setMessageListener(listenerAdapter());
//        return listenerContainer;
//    }

//    private MessageListener listenerAdapter() {
//        return listenerAdapter();
//    }

//    @Bean
//    public SimpleMessageListenerContainer simpleRabbitListener2(
//            final ConnectionFactory connectionFactory,
//            MessageListenerAdapter otherListenerAdapter
//
//    ) {
//        SimpleMessageListenerContainer listenerContainer = new SimpleMessageListenerContainer(connectionFactory);
//        listenerContainer.setQueueNames(otherQueueName);
//        listenerContainer.setMessageListener(otherListenerAdapter());
//        return listenerContainer;
//    }

//    private MessageListener otherListenerAdapter() {
//        return otherListenerAdapter();
//    }

//    @Bean
//    public DirectExchange directExchange() {
//        return new DirectExchange("req-res");
//    }
//
//    @Bean
//    public Queue queue() {
//        return new Queue("request-queue");
//    }
//
//    @Bean
//    public Binding binding(DirectExchange directExchange,
//                           Queue queue) {
//        return BindingBuilder.bind(queue)
//                .to(directExchange)
//                .with("req.res");
//    }

    // Subcriber only need this one to convert incomming message
    @Bean
    public MessageConverter jackson2MessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}
