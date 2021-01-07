package org.example.tut.flowable.handler;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

@Data
@Slf4j
public class SayHello implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {

        log.info("Say hello is really enjoyable!!!");

    }
}
