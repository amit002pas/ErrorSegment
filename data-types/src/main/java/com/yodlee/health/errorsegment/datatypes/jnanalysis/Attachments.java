package com.yodlee.health.errorsegment.datatypes.jnanalysis;

import java.util.List;

import javax.inject.Named;

import lombok.Data;

@Named
public @Data class Attachments {

	private List<Attachment> attachments;
}
