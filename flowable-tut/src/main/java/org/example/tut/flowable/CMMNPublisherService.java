package org.example.tut.flowable;

import lombok.extern.slf4j.Slf4j;
import org.flowable.cmmn.api.delegate.DelegatePlanItemInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("CMMNPublisherService")
@Slf4j
public class CMMNPublisherService {
    @Autowired
    RabbitMqPublisher rabbitMqPublisher;

    public void publish(DelegatePlanItemInstance execution) {
        log.info("Sending....");
        String routingKey = "cmmn.foo.baz";
        rabbitMqPublisher.sendMessage(execution.getCaseInstanceId(),routingKey,"Short api call ...");
        log.info("Message sent!");
    }

    public void publishToOtherQueue(DelegatePlanItemInstance execution) {
        log.info("Sending....");
        String routingKey = "cmmn.bar.baz";
        rabbitMqPublisher.sendMessage(execution.getCaseInstanceId(),routingKey, "Long api call ...");
        log.info("Message sent!");
    }
}
