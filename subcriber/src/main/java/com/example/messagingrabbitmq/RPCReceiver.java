package com.example.messagingrabbitmq;


import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RPCReceiver {

//	@RabbitListener(queues = "request-queue", concurrency = "3")
//	public Message receive(Message msg) {
//		try{
//			System.out.println("Process request from message queue");
//			System.out.println(msg.toString());
//			Thread.sleep(30000);
//			System.out.println("End rpc job!");
//		}catch (InterruptedException e){}
//		System.out.println("Receiver change message content");
//		msg.setMessage("Hello from receiver!");
//		return msg;
//	}

	// This listener used for sendAsynchronously & sendAsynchronouslyWithCallback & sendAndForget
	@RabbitListener(queues = "request-queue", concurrency = "3")
	public String receive(Message msg, org.springframework.amqp.core.Message msgObj) {
		try{
			System.out.println("Process request from message queue");
			System.out.println(msg.toString());
			Thread.sleep(10000);
			System.out.println("End rpc job!");
		}catch (InterruptedException e){}
		System.out.println("Receiver change message content");

		System.out.println("Message object handle by framework");
		System.out.println(msgObj);

		msg.setMessage("Hello from receiver!");
		return msg.getMessage();
	}

	// This listener will take care of message in response-queue: used for sendAndForget
	@RabbitListener(queues = "response-queue")
	public void receiveResponseMessage(String msg, org.springframework.amqp.core.Message msgObj) {
		System.out.println("Receive response message");
		System.out.println(msg.toString());

		System.out.println("Message object handle by framework");
		System.out.println(msgObj);

		System.out.println("-----------Finished-----------");
	}

}

