package org.example.tut.flowable;

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// This class is dedicated for flowable engine service tasks
@Component("PublisherService")
@Slf4j
public class PublisherService {
    @Autowired
    RabbitMqPublisher rabbitMqPublisher;

    public void publish(DelegateExecution execution) {
        log.info("Sending....");
        String routingKey = "foo.bar.baz";
        rabbitMqPublisher.sendMessage(execution.getId(),routingKey,"Message to BPMN queue");
        log.info("Message sent!");
    }

    public void publishToOtherQueue(DelegateExecution execution) {
        log.info("Sending....");
        String routingKey = "foo.foo.baz";
        rabbitMqPublisher.sendMessage(execution.getId(),routingKey,"Message to other BPMN queue");
        log.info("Message sent!");
    }
}
