package org.example.tut.flowable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.tut.flowable.dto.HolidayRequest;
import org.example.tut.flowable.dto.ProcessInstanceResponse;
import org.example.tut.flowable.dto.TaskDetails;
import org.flowable.engine.*;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.runtime.ActivityInstance;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class HolidayService {

    public static final String TASK_CANDIDATE_GROUP = "managers";
    public static final String PROCESS_DEFINITION_KEY = "holidayRequest";
    public static final String EMP_NAME = "empName";
    RuntimeService runtimeService;
    TaskService taskService;
    ProcessEngine processEngine;
    RepositoryService repositoryService;

    public static final String MY_TEST_PROCESS = "myTestProcess";

    //********************************************************** deployment service methods **********************************************************

    public void deployProcessDefinition() {

        Deployment deployment =
                repositoryService
                        .createDeployment()
                        .addClasspathResource("holiday-request.bpmn20.xml")
                        .addClasspathResource("Test_Process.bpmn20.xml")
                        .deploy();


    }


    //********************************************************** process service methods **********************************************************

    public ProcessInstanceResponse applyHoliday(HolidayRequest holidayRequest) {

        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("employee", holidayRequest.getEmpName());
        variables.put("noOfHolidays", holidayRequest.getNoOfHolidays());
        variables.put("description", holidayRequest.getRequestDescription());

        ProcessInstance processInstance =
                runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variables);

        return new ProcessInstanceResponse(processInstance.getId(), processInstance.isEnded());
    }

    public List<TaskDetails> getManagerTasks() {
        List<Task> tasks =
                taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_GROUP).list();
        List<TaskDetails> taskDetails = getTaskDetails(tasks);

        return taskDetails;
    }

    private List<TaskDetails> getTaskDetails(List<Task> tasks) {
        List<TaskDetails> taskDetails = new ArrayList<>();
        for (Task task : tasks) {
            Map<String, Object> processVariables = taskService.getVariables(task.getId());
            taskDetails.add(new TaskDetails(task.getId(), task.getName(), processVariables));
        }
        return taskDetails;
    }


    public void approveHoliday(String taskId,Boolean approved) {

        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("approved", approved.booleanValue());
        taskService.complete(taskId, variables);
    }

    public void acceptHoliday(String taskId) {
        taskService.complete(taskId);
    }


    public List<TaskDetails> getUserTasks() {

        List<Task> tasks = taskService.createTaskQuery().taskCandidateOrAssigned(EMP_NAME).list();
        List<TaskDetails> taskDetails = getTaskDetails(tasks);

        return taskDetails;
    }


    public void checkProcessHistory(String processId) {

        HistoryService historyService = processEngine.getHistoryService();

        List<HistoricActivityInstance> activities =
                historyService
                        .createHistoricActivityInstanceQuery()
                        .processInstanceId(processId)
                        .finished()
                        .orderByHistoricActivityInstanceEndTime()
                        .asc()
                        .list();

        for (HistoricActivityInstance activity : activities) {
            System.out.println(
                    activity.getActivityId() + " took " + activity.getDurationInMillis() + " milliseconds");
        }

        System.out.println("\n \n \n \n");
    }


    public ProcessInstanceResponse startMyTestProcess() {

        Map<String, Object> variables = new HashMap<String, Object>();
        String assignee = "assignee";
        String assigneeValue = "MyLovelyAssignee";
        variables.put(assignee, assigneeValue);
        System.out.println("About to start" + MY_TEST_PROCESS);
        ProcessInstance processInstance =
                runtimeService.startProcessInstanceByKey(MY_TEST_PROCESS, variables);

        System.out.println("Get user task with assignee: " + assigneeValue);

        List<TaskDetails> taskDetails = getMyUserTasks(assigneeValue);
        System.out.println("May found some tasks ...");
        for (TaskDetails details: taskDetails
        ) {
            System.out.println(details.toString());
        }
        return new ProcessInstanceResponse(processInstance.getId(), processInstance.isEnded());
    }


    private List<TaskDetails> getMyUserTasks(String assignee) {
        System.out.println("Try to get user tasks ..., assignee: " + assignee);
        List<Task> tasks = taskService.createTaskQuery().taskAssignee(assignee).list();
        List<TaskDetails> taskDetails = getTaskDetails(tasks);
        return taskDetails;
    }

    public ProcessInstanceResponse completeWithForm(String processInstanceId, String taskId) {
        String formKey = "helloForm";
        System.out.println("Complete task by " + taskId + " and form: "+ formKey);
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("helloMessage", "Default message set programmatically");
        taskService.completeTaskWithForm(taskId,formKey,null,null);
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

    public ProcessInstanceResponse completeMyProcess(String processInstanceId, String taskId) {
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("helloMessage", "Hello from service");
        taskService.complete(taskId, variables);
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

    public ProcessInstanceResponse completeMyProcessWithDecisionTable(String processInstanceId, String taskId, String shouldSayHello) {
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("helloMessage", "Hello from service");
        variables.put("shouldSayHello",shouldSayHello);
        taskService.complete(taskId, variables);
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

    public ProcessInstanceResponse resumeARunningProcess(String processInstanceId){
        List<ActivityInstance> acts =  runtimeService.createActivityInstanceQuery().processInstanceId(processInstanceId).orderByActivityId().asc().list();
        ActivityInstance lastAct = null;
        if (acts.size() >= 1){
            lastAct = acts.get(0);
            for (ActivityInstance a: acts
                 ) {
                System.out.println(a.toString());
            }
        }
        if(lastAct == null){
            return null;
        }
        else{
            String taskId = lastAct.getTaskId();
            System.out.println("Try to complete Task ... " + taskId);
            taskService.complete(taskId);
            System.out.println("About to check whether the whole process is completed");
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
    public ProcessInstanceResponse resumeASuspendedProcess(String processInstanceId){
        runtimeService.activateProcessInstanceById(processInstanceId);
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

    public void suspendAProcess(String processInstanceId){
        runtimeService.suspendProcessInstanceById(processInstanceId);
    }

    public void triggerAExecution(String executionId){
        runtimeService.trigger(executionId);
    }


}
