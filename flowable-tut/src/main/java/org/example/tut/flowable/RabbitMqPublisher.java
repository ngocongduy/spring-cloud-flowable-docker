package org.example.tut.flowable;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
@Component
public class RabbitMqPublisher {
    @Autowired
    private RabbitTemplate rabbitTemplate;

//    @Autowired
//    private ApplicationContext context;
//
//    public RabbitMqPublisher() {
//        this.rabbitTemplate = new RabbitTemplate(context.getBean(ConnectionFactory.class));
//    }
    public RabbitMqPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(String processInstanceId, String routingKey,String message){
        Message msg = new Message(message,processInstanceId);
        System.out.println("Publisher is about to send message ...");
        System.out.println(msg.toString());
//        rabbitTemplate.setMessageConverter(messageConverter);
        System.out.println(rabbitTemplate.toString());
//        rabbitTemplate.convertAndSend( RabbitMqConfig.topicExchangeName, "foo.bar.baz", msg);
        rabbitTemplate.convertAndSend( RabbitMqConfig.topicExchangeName, routingKey, msg);
    }
}
