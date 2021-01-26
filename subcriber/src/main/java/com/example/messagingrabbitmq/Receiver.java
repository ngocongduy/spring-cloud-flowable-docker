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
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Receiver {

	private CountDownLatch latch = new CountDownLatch(1);

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private CmmnRuntimeService cmmnRuntimeService;

	public Receiver() {
	}

	public Receiver(RuntimeService runtimeService) {
		this.runtimeService = runtimeService;
	}
	@RabbitListener(queues = "spring-boot")
	public void receiveMessage(byte[] messageObj) throws InterruptedException {
		String msg = new String(messageObj);
		System.out.println(msg);
		Message not = new Gson().fromJson(msg, Message.class);
		System.out.println("Received a new message...");
		System.out.println(not.toString());

		String pid = not.getProcessInstanceId();
		System.out.println("Received <" + not.getMessage() + ">");
		Thread.sleep(5000);
		System.out.println("Wake up ...");
		System.out.println("Activate suspended process: " + pid);
		Map<String,Object> vars = new HashMap<>();
		vars.put("message","Message from api call");
		runtimeService.trigger(pid, vars);
		latch.countDown();
	}

	@RabbitListener(queues = "other-spring-boot")
	public void receiveMessageFromOtherQueue(byte[] messageObj) throws InterruptedException {
		String msg = new String(messageObj);
		System.out.println(msg);
		Message not = new Gson().fromJson(msg, Message.class);
		System.out.println("Other queue received a new message...");
		System.out.println(not.toString());

		String pid = not.getProcessInstanceId();
		System.out.println("Received <" + not.getMessage() + ">");
		Thread.sleep(5000);
		System.out.println("Wake up ...");
		latch.countDown();
	}



	@RabbitListener(queues = "cmmn-queue")
	public void cmmnLongTask(byte[] messageObj) throws InterruptedException {
		String msg = new String(messageObj);
		System.out.println(msg);
		Message not = new Gson().fromJson(msg, Message.class);
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
		latch.countDown();
	}

	@RabbitListener(queues = "other-cmmn-queue")
	public void cmmnShortTask(byte[] messageObj) throws InterruptedException {
		String msg = new String(messageObj);
		System.out.println(msg);
		Message not = new Gson().fromJson(msg, Message.class);
		System.out.println("Long task worker receive a new message...");
		System.out.println(not.toString());

		String pid = not.getProcessInstanceId();
		System.out.println("Received <" + not.getMessage() + ">");
		Thread.sleep(30000);
		System.out.println("Wake up after 30 second ...");
		try{
			findManualTaskAndEndCase(pid,"manualTask");
		}catch (Exception e){
			System.out.println("Catch exception to not fail the message receipt");
		}

		latch.countDown();
	}

	private boolean findManualTaskAndEndCase(String caseId, String manualTaskDefKey) {
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


	public CountDownLatch getLatch() {
		return latch;
	}



}

