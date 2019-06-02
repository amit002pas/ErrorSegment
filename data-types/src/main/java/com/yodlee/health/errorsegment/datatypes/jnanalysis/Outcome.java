package com.yodlee.health.errorsegment.datatypes.jnanalysis;

import lombok.Data;

public @Data class Outcome {

	private String action;
	private String output;
	private boolean ttrRaised;
	private Integer bugId;
	
}
