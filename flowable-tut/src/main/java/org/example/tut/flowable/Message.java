package org.example.tut.flowable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message implements Serializable {
    private String message;
    private String processInstanceId;

//    public Message() {
//    }
//
//    public Message(String message, String processIntanceId) {
//        this.message = message;
//        this.processInstanceId = processIntanceId;
//    }
//
//    public String getMessage() {
//        return message;
//    }
//
//    public String getProcessInstanceId() {
//        return processInstanceId;
//    }
//
//    public void setMessage(String message) {
//        this.message = message;
//    }
//
//    public void setProcessInstanceId(String processInstanceId) {
//        this.processInstanceId = processInstanceId;
//    }
}
