package org.example.tut.flowable;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.tut.flowable.dto.ProcessInstanceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@RequestMapping("/rabbit")
public class RabbitMqController {
    @Autowired
    RabbitMqService rabbitMqService;

    // Start a process
    @GetMapping("/")
    public String sayHello() {
        return "Hello from RabbitMq Service";
    }

    // Start a process
    @GetMapping("/start")
    public ProcessInstanceResponse startMqProcess() {
        return rabbitMqService.startMqTestProcess();
    }

    // Start a process
    @GetMapping("/resume/{processInstanceId}")
    public ProcessInstanceResponse continueMqProcess(@PathVariable("processInstanceId") String pid) {
        return rabbitMqService.continueMqTestProcess(pid);
    }

}
