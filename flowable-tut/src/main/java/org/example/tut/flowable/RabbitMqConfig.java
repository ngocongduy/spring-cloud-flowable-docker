package org.example.tut.flowable;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;

@Configuration
public class RabbitMqConfig {

    static final String topicExchangeName = "spring-boot-exchange";

    static final String queueName = "spring-boot";
    static final String otherQueueName = "other-spring-boot";
    static final String CMMNQueueName = "cmmn-queue";
    static final String otherCMMNQueueName = "other-cmmn-queue";


//    @Bean
//    public ConnectionFactory connectionFactory(){
//        ConnectionFactory connectionFactory = new ConnectionFactory();
//        connectionFactory.setUsername("guest");
//        connectionFactory.setPassword("guest");
//        connectionFactory.setPort(5672);
//        connectionFactory.setHost("localhost");
//        return connectionFactory;
//    }

    @Bean
    public CachingConnectionFactory rabbitConnectionFactory() {
        CachingConnectionFactory connectionFactory =
                new CachingConnectionFactory("host.docker.internal");
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        connectionFactory.setPort(5672);
        return connectionFactory;
    }

    @Bean
    public RabbitTemplate amqpTemplate(CachingConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }

    @Bean
    public MappingJackson2MessageConverter consumerJackson2MessageConverter() {
        return new MappingJackson2MessageConverter();
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    Queue queue() {
        return new Queue(queueName, false);
    }
    @Bean
    Queue otherQueue() {
        return new Queue(otherQueueName, false);
    }

    @Bean
    Queue CMMNQueue() {
        return new Queue(CMMNQueueName, false);
    }
    @Bean
    Queue otherCMMNQueue() {
        return new Queue(otherCMMNQueueName, false);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(topicExchangeName);
    }

    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue()).to(exchange).with("foo.bar.#");
    }

    @Bean
    Binding otherBinding(Queue otherQueue, TopicExchange exchange) {
        return BindingBuilder.bind(otherQueue()).to(exchange).with("foo.foo.#");
    }

    @Bean
    Binding CMMNbinding(Queue CMMNQueue, TopicExchange exchange) {
        return BindingBuilder.bind(CMMNQueue()).to(exchange).with("cmmn.foo.#");
    }

    @Bean
    Binding otherCMMNBinding(Queue otherCMMNQueue, TopicExchange exchange) {
        return BindingBuilder.bind(otherCMMNQueue()).to(exchange).with("cmmn.bar.#");
    }

}
