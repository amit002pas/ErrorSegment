package com.yodlee.health.errorsegment.datatypes;
import java.util.List;


import lombok.Data;

public @Data class BugFetchResponse {

	private String status;
    private BugType bugType;
    private Bug bug;
    private String message;
    private List<String> messages;

}
