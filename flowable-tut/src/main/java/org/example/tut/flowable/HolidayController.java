package org.example.tut.flowable;

import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.tut.flowable.dto.HolidayRequest;
import org.example.tut.flowable.dto.ProcessInstanceResponse;
import org.example.tut.flowable.dto.TaskDetails;
import org.hibernate.query.QueryParameter;
import org.springframework.web.bind.annotation.*;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class HolidayController {

    HolidayService holidayService;

    //********************************************************** deployment endpoints **********************************************************
    @PostMapping("/deploy")
    public void deployWorkflow() {
        holidayService.deployProcessDefinition();
    }

    //********************************************************** process endpoints **********************************************************
    @PostMapping("/holiday/apply")
    public ProcessInstanceResponse applyHoliday(@RequestBody HolidayRequest holidayRequest) {
        return holidayService.applyHoliday(holidayRequest);
    }

    @GetMapping("/manager/tasks")
    public List<TaskDetails> getTasks() {
        return holidayService.getManagerTasks();
    }


    @PostMapping("/manager/approve/tasks/{taskId}/{approved}")
    public void approveTask(@PathVariable("taskId") String taskId,@PathVariable("approved") Boolean approved){
        holidayService.approveHoliday(taskId,approved);
    }

    @PostMapping("/user/accept/{taskId}")
    public void acceptHoliday(@PathVariable("taskId") String taskId){
        holidayService.acceptHoliday(taskId);
    }

    @GetMapping("/user/tasks")
    public List<TaskDetails> getUserTasks() {
        return holidayService.getUserTasks();
    }


    @GetMapping("/process/{processId}")
    public void checkState(@PathVariable("processId") String processId){
        holidayService.checkProcessHistory(processId);
    }

    // My code start from here

    // Start a process
    @GetMapping("/start")
    public ProcessInstanceResponse testMyProccess() {
        return holidayService.startMyTestProcess();
    }

    // Complete a process, need to find the waiting task (user task) in database
    // request parameter 'say' is used for decision table in the process
    @GetMapping("/complete/{processInstanceId}/{taskId}")
    public ProcessInstanceResponse completeMyProccess(
            @PathVariable("processInstanceId") String processInstanceId,@PathVariable("taskId") String taskId,
            @RequestParam(name = "say") String shouldSayHello
    ) {
        if (shouldSayHello != null){
            return holidayService.completeMyProcessWithDecisionTable(processInstanceId,taskId,shouldSayHello);
        }
        return holidayService.completeMyProcess(processInstanceId, taskId);
    }

    // Naively pick the last waiting task (such as a user tasK) and try to complete that task with hard-coded value to finish the process
    @GetMapping("/resume/{processInstanceId}")
    public ProcessInstanceResponse resumeARunningProcess(
            @PathVariable("processInstanceId") String processInstanceId
    ) {
        return holidayService.resumeARunningProcess(processInstanceId);
    }

    // Complete a process using form engine
    @GetMapping("/form-complete/{processInstanceId}/{taskId}")
    public ProcessInstanceResponse completeMyProccessWithForm(
            @PathVariable("processInstanceId") String processInstanceId,@PathVariable("taskId") String taskId
    ) {
        return holidayService.completeWithForm(processInstanceId, taskId);
    }

    // Resume suspended process
    @GetMapping("/resume-suspend/{processInstanceId}")
    public ProcessInstanceResponse resumeAProcess(
            @PathVariable("processInstanceId") String processInstanceId
    ) {
        return holidayService.resumeASuspendedProcess(processInstanceId);
    }

    // Suspend a process
    @GetMapping("/suspend/{processInstanceId}")
    public String suspendAProcess(
            @PathVariable("processInstanceId") String processInstanceId
    ) {
        holidayService.suspendAProcess(processInstanceId);
        return "Suspend process: " + processInstanceId;
    }

    // Not yet understand ..
    @GetMapping("/trigger/{executionId}")
    public String triggerAnExecution(
            @PathVariable("executionId") String executionId
    ) {
        holidayService.triggerAExecution(executionId);
        return "Trigger exec: " + executionId;
    }


}
