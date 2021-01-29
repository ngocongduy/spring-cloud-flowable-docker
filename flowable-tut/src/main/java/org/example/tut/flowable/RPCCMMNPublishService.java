package org.example.tut.flowable;

import lombok.extern.slf4j.Slf4j;
import org.flowable.cmmn.api.delegate.DelegatePlanItemInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("RPCCMMNPublishService")
@Slf4j
public class RPCCMMNPublishService {
    @Autowired
    RPCRabbitMqPublisher rpcRabbitMqPublisher;

    public void publish(DelegatePlanItemInstance execution) {
        log.info("About to send test message for RPC with RabbitMq");
        rpcRabbitMqPublisher.sendAsynchronously("test_pid", "ồ rí gi nồ mê sịt async");
        log.info("Message sent!");
    }

    public void publishAsync(DelegatePlanItemInstance execution) {
        log.info("About to send test message for RPC with RabbitMq");
        rpcRabbitMqPublisher.sendAsynchronouslyWithCallback("test_pid", "ồ rí gi nồ mê sịt async with call back");
        log.info("Message sent!");
    }

    public void sendAndForget(DelegatePlanItemInstance execution) {
        log.info("About to send test message for RPC with RabbitMq");
        rpcRabbitMqPublisher.sendAndForget("test_pid", "ồ rí gi nồ mê sịt asyn with other queue");
        log.info("Message sent!");
    }


}
