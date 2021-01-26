package org.example.tut.flowable;

import org.flowable.cmmn.api.CmmnRuntimeService;
import org.flowable.cmmn.api.CmmnTaskService;
import org.flowable.cmmn.api.runtime.CaseInstance;
import org.flowable.cmmn.api.runtime.PlanItemInstance;
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

    private static final String CMM = "testCaseModel";

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
        }
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
