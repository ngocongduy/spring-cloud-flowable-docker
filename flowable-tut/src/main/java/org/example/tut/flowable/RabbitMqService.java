package org.example.tut.flowable;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.flowable.engine.*;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.example.tut.flowable.dto.ProcessInstanceResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class RabbitMqService {

    RuntimeService runtimeService;
    TaskService taskService;
    ProcessEngine processEngine;
//    @Autowired
//    RabbitMqPublisher rabbitMqPublisher;

    public static final String MY_TEST_PROCESS = "mqTestProcess";

    //********************************************************** process service methods **********************************************************


    public ProcessInstanceResponse startMqTestProcess() {

        Map<String, Object> variables = new HashMap<String, Object>();
        String assignee = "assignee";
        String assigneeValue = "MyLovelyAssignee";
        variables.put(assignee, assigneeValue);
        System.out.println("About to start" + MY_TEST_PROCESS);
        ProcessInstance processInstance =
                runtimeService.startProcessInstanceByKey(MY_TEST_PROCESS, variables);
        String pid = processInstance.getId();
        System.out.println("Super execution id : "+ processInstance.getSuperExecutionId());

        List<Execution> executions = runtimeService.createExecutionQuery().processInstanceId(pid).orderByProcessInstanceId().desc().list();
        for (Execution exec: executions
             ) {
            System.out.println(exec.toString());
        }

        String execId = executions.get(0).getId();
        System.out.println("Execution id: " + execId);
//        System.out.println("Send message to queue");
//        rabbitMqPublisher.sendMessage(execId,"Default message");

//        System.out.println("Suspend the whole process");
//        suspendAProcess(pid);

        return new ProcessInstanceResponse(processInstance.getId(), processInstance.isEnded());
    }


    public ProcessInstanceResponse continueMqTestProcess(String processIntanceId) {

        System.out.println("Continue " + MY_TEST_PROCESS + " with process instance id: " +processIntanceId);
        runtimeService.trigger(processIntanceId);

        System.out.println("Check finish status ... ");
        return checkAProcessFinished(processIntanceId);
    }


//
//    private void suspendAProcess(String processInstanceId){
//        runtimeService.suspendProcessInstanceById(processInstanceId);
//    }
//    private void resumeASuspendedProcess(String processInstanceId){
//        runtimeService.activateProcessInstanceById(processInstanceId);
//    }

    private ProcessInstanceResponse checkAProcessFinished(String processInstanceId){
        HistoryService historyService = processEngine.getHistoryService();
        List<HistoricActivityInstance> activities =
                historyService
                        .createHistoricActivityInstanceQuery()
                        .processInstanceId(processInstanceId)
                        .finished()
                        .orderByHistoricActivityInstanceEndTime()
                        .desc()
                        .list();
        boolean isFound = false;
        for (HistoricActivityInstance instance: activities
        ) {
            System.out.println(instance.toString());
            isFound = true;
        }
        return new ProcessInstanceResponse(processInstanceId, isFound);
    }


}
