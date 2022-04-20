package com.spring.multipart.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseMessage {

	private String status;
	private String message;
	private Object data;

	public ResponseMessage(String status, String message) {
		this.status = status;
		this.message = message;
	}

}
