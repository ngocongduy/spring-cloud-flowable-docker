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
        log.info(execution.getVariables().toString());
        String shouldSayHelloValue = execution.getVariables().getOrDefault("shouldSayHelloValue","").toString();
        if (shouldSayHelloValue.isEmpty()){
            log.info("No value for shouldSayHelloValue or error ...");
        }
        else if (shouldSayHelloValue.equals("true")){
            log.info("shouldSayHelloValue is " + shouldSayHelloValue);
        }
        else if (shouldSayHelloValue.equals("false")){
            log.info("shouldSayHelloValue is " + shouldSayHelloValue);
        }
        else{
            log.info("Unpredictable case!!!");
        }
        log.info("End greeting ---!");
    }
}

