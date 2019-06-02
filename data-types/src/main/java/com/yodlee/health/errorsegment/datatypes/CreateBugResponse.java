package com.yodlee.health.errorsegment.datatypes;
import java.util.List;

import javax.inject.Named;

import lombok.Data;

@Named
public @Data class CreateBugResponse {
	private String status;
	private String message;
	private List<String> messages;
	private String bugID;
	private String syntheticBugId;
}