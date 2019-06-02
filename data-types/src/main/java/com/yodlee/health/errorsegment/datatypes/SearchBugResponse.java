package com.yodlee.health.errorsegment.datatypes;
import java.util.List;

import lombok.Data;

public @Data class SearchBugResponse {
	private String status;
	private String message;
	private String error;
	private List<String> messages;
	private List<Bug> bugs;
}
