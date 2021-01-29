package org.example.tut.flowable;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.flowable.cmmn.api.runtime.CaseInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@RequestMapping("cm")
public class CMMNController {
    @Autowired
    CMMNService cmmnService;

    // Start a process
    @GetMapping("/start")
    public String start() {
        CaseInstance caseInstance = cmmnService.start();
        return caseInstance.getId();
    }

    @GetMapping("/startrpc")
    public String startrpc() {
        CaseInstance caseInstance = cmmnService.startrpc();
        return "case id: " + caseInstance.getId();
    }


    @GetMapping("/complete/{caseId}/{taskId}")
    public String complete(
            @PathVariable("caseId") String caseId, @PathVariable("taskId") String taskId
    ) {
        cmmnService.complete(caseId,taskId);
        return caseId + " - "  + taskId + " completed";
    }

    @GetMapping("/trigger/{planInstanceId}")
    public String trigger(
            @PathVariable("planInstanceId") String planInstanceId
    ) {
        cmmnService.trigger(planInstanceId);
        return planInstanceId + " triggered";
    }
}
