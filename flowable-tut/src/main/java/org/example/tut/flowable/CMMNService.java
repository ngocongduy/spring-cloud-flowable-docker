package org.example.tut.flowable;

import org.flowable.cmmn.api.CmmnRuntimeService;
import org.flowable.cmmn.api.CmmnTaskService;
import org.flowable.cmmn.api.runtime.CaseInstance;
import org.flowable.cmmn.api.runtime.PlanItemInstance;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CMMNService {
    @Autowired
    CmmnRuntimeService cmmnRuntimeService;

    @Autowired
    CmmnTaskService cmmnTaskService;

    @Autowired
    TaskService taskService;

    private static final String CMM = "testCaseModel";
    private static final String CMMRPC = "rpcTestCaseModel";

    public CaseInstance start(){
        CaseInstance caseInstance = cmmnRuntimeService.createCaseInstanceBuilder()
                .caseDefinitionKey(CMM)
                .variable("potentialEmployee", "johnDoe")
                .start();
        List<PlanItemInstance> planItemInstances = cmmnRuntimeService.createPlanItemInstanceQuery()
                .caseInstanceId(caseInstance.getId())
                .orderByName().asc()
                .list();

        for (PlanItemInstance planItemInstance : planItemInstances) {
            System.out.println(planItemInstance.getName()
                    + ", state=" + planItemInstance.getState()
                    + ", parent stage=" + planItemInstance.getStageInstanceId());
        }

        System.out.println("Active plan item only .....");

        List<PlanItemInstance> activePlanItemInstances = cmmnRuntimeService.createPlanItemInstanceQuery()
                .caseInstanceId(caseInstance.getId())
                .planItemInstanceStateActive()
                .orderByName().asc()
                .list();

        for (PlanItemInstance planItemInstance : activePlanItemInstances) {
            System.out.println(planItemInstance.getName());
            System.out.println(planItemInstance.toString());
        }
        return caseInstance;
    }
    public CaseInstance startrpc(){
        CaseInstance caseInstance = cmmnRuntimeService.createCaseInstanceBuilder()
                .caseDefinitionKey(CMMRPC)
                .variable("potentialEmployee", "johnDoe")
                .start();

        List<PlanItemInstance> planItemInstances = cmmnRuntimeService.createPlanItemInstanceQuery()
                .caseInstanceId(caseInstance.getId())
                .orderByName().asc()
                .list();

        for (PlanItemInstance planItemInstance : planItemInstances) {
            System.out.println(planItemInstance.getName()
                    + ", state=" + planItemInstance.getState()
                    + ", parent stage=" + planItemInstance.getStageInstanceId());
        }

        System.out.println("Active plan item only .....");

        List<PlanItemInstance> activePlanItemInstances = cmmnRuntimeService.createPlanItemInstanceQuery()
                .caseInstanceId(caseInstance.getId())
                .planItemInstanceStateActive()
                .orderByName().asc()
                .list();

        for (PlanItemInstance planItemInstance : activePlanItemInstances) {
            System.out.println(planItemInstance.getName());
            System.out.println(planItemInstance.toString());
        }

        System.out.println("This works for this cmmn only");
        Task task = taskService.createTaskQuery().caseInstanceId(caseInstance.getId()).singleResult();
        System.out.println("User task waiting: " + task.getId());
        return caseInstance;
    }

    public void complete(String caseId, String taskId){
        cmmnTaskService.complete(taskId);
        List<Task> tasks = cmmnTaskService.createTaskQuery()
                .caseInstanceId(caseId)
                .orderByTaskName().asc()
                .list();
        System.out.println("Remained active task for case instance id = " + caseId);
        for (Task task : tasks) {
            System.out.println("Task for the case id: " + task.toString());
        }
    }

    public void trigger(String planItemId){
        cmmnRuntimeService.triggerPlanItemInstance(planItemId);
    }
}
