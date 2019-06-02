package com.yodlee.health.errorsegment.datatypes;

import lombok.Data;

public @Data class ServerStats {

	private int errorCode ;
	private String scriptVersion ;
	private String stackTrace ;
}



