package org.example.tut.flowable.handler;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.flowable.cmmn.api.delegate.DelegatePlanItemInstance;
import org.flowable.cmmn.api.delegate.PlanItemJavaDelegate;


@Data
@Slf4j
public class SayHelloCM implements PlanItemJavaDelegate {

    public void execute(DelegatePlanItemInstance planItemInstance) {
        try{
            System.out.println("Going to sleep");
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            System.out.println("Exception happens during sleeping!!!");
        }
        log.info("Say hello is really enjoyable!!!");
        log.info(planItemInstance.getVariables().toString());
        String shouldSayHelloValue = planItemInstance.getVariables().getOrDefault("shouldSayHelloValue","").toString();
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

