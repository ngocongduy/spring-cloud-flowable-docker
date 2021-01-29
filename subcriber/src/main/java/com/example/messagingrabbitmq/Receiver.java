package com.example.messagingrabbitmq;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import com.google.gson.Gson;
import org.flowable.cmmn.api.CmmnRuntimeService;
import org.flowable.cmmn.api.CmmnTaskService;
import org.flowable.cmmn.api.runtime.PlanItemInstance;
import org.flowable.engine.RuntimeService;
import org.flowable.task.api.Task;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.ws.http.HTTPException;

@Service
public class Receiver {

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private CmmnRuntimeService cmmnRuntimeService;

	public Receiver() {
	}

	public Receiver(RuntimeService runtimeService) {
		this.runtimeService = runtimeService;
	}

	// If we dont config an converter to mapping message, we have to get byte[] and do the mapping ourselves
	// This listener simulate short task for bpmn
//	@RabbitListener(queues = "spring-boot")
//	public void receiveMessage(byte[] messageObj) throws InterruptedException {
//		String msg = new String(messageObj);
//		System.out.println(msg);
//		Message not = new Gson().fromJson(msg, Message.class);
//		System.out.println("Received a new message...");
//		System.out.println(not.toString());
//
//		String pid = not.getProcessInstanceId();
//		System.out.println("Received <" + not.getMessage() + ">");
//		System.out.println("Simulate short task by sleep ...");
//		Thread.sleep(5000);
//		System.out.println("Wake up ...");
//		System.out.println("Activate suspended process: " + pid);
//		Map<String,Object> vars = new HashMap<>();
//		vars.put("message","Message from api call");
//
//		try{
//			runtimeService.trigger(pid, vars);
//		}catch (Exception e) {
//			System.out.println("Catch exception so that receiver wont cry, consequently no message re-queued");
//		}
//
//	}
	@RabbitListener(queues = "spring-boot")
	public void receiveMessage(Message not) throws InterruptedException {
		System.out.println("Received a new message...");
		System.out.println(not.toString());

		String pid = not.getProcessInstanceId();
		System.out.println("Received <" + not.getMessage() + ">");
		System.out.println("Simulate short task by sleep ...");
		Thread.sleep(5000);
		System.out.println("Wake up ...");
		System.out.println("Activate suspended process: " + pid);
		Map<String,Object> vars = new HashMap<>();
		vars.put("message","Message from api call");

		try{
			runtimeService.trigger(pid, vars);
		}catch (Exception e) {
			System.out.println("Catch exception so that receiver wont cry, consequently no message re-queued");
		}

	}

	// This listener simulate long task for bpmn
	@RabbitListener(queues = "other-spring-boot")
	public void receiveMessageFromOtherQueue(Message not) throws InterruptedException {
		System.out.println("Other queue received a new message...");
		System.out.println(not.toString());

		String pid = not.getProcessInstanceId();
		System.out.println("Received <" + not.getMessage() + ">");
		System.out.println("Simulate short task by sleep ...");
		Thread.sleep(15000);
		System.out.println("Wake up ...");
		System.out.println("If every thing work right, the process were end by reaching this end");

		// This code will throw error because the process is ended by above RabbitListener, no executionId found
		try{
			runtimeService.trigger(pid);
		}catch (Exception e) {
			System.out.println("Catch exception so that receiver wont cry, consequently no message re-queued");
		}

	}


	// This listener simulate short task for cmmn
	@RabbitListener(queues = "cmmn-queue")
	public void cmmnLongTask(Message not) throws InterruptedException {

		System.out.println("Short task worker receive a new message...");
		System.out.println(not.toString());

		String pid = not.getProcessInstanceId();
		System.out.println("Received <" + not.getMessage() + ">");
		Thread.sleep(5000);
		System.out.println("Wake up after 5 seconds ...");

		try{
			findManualTaskAndEndCase(pid,"manualTask");
		}catch (Exception e){
			System.out.println("Catch exception to not fail the message receipt");
		}
	}

	// This listener simulate long task for cmmn
	@RabbitListener(queues = "other-cmmn-queue")
	public void cmmnShortTask(Message not) throws InterruptedException {
		System.out.println("Long task worker receive a new message...");
		System.out.println(not.toString());

		String pid = not.getProcessInstanceId();
		System.out.println("Received <" + not.getMessage() + ">");
		Thread.sleep(15000);
		System.out.println("Wake up after 30 second ...");

		System.out.println("This task would throw exception because the case was ended by the RabbitListener above");
		try{
			findManualTaskAndEndCase(pid,"manualTask");
		}catch (Exception e){
			System.out.println("Catch exception to not fail the message receipt");
		}

	}

	private boolean findManualTaskAndEndCase(String caseId, String manualTaskDefKey) {
		// Snippet for external api calls, may be useful in real time
		try{
			// api call
			System.out.println("Call api");
		} catch (HTTPException e) { //Should change to http error exception
			if(true){ //Should check if error is not found
				// delay some time
				try{
					Thread.sleep(5000);
				}catch (InterruptedException ie){
					// Do nothing
				}

				// After back off for a while then requeue message by throwing error
				throw new RuntimeException();
			}
			else {
				// In case of other error, we prefer not to requeue
				throw new AmqpRejectAndDontRequeueException(e);
			}
		}catch (Exception e){
			// Unpredicted exception, we prefer not to requeue
			throw new AmqpRejectAndDontRequeueException(e);
		}

		PlanItemInstance manualTaskPlanItem = cmmnRuntimeService.createPlanItemInstanceQuery().caseInstanceId(caseId).planItemDefinitionId(manualTaskDefKey).singleResult();

		System.out.println("Manual task to end case instance : " + manualTaskPlanItem.toString());
		try {
			cmmnRuntimeService.triggerPlanItemInstance(manualTaskPlanItem.getId());
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;

	}

}

