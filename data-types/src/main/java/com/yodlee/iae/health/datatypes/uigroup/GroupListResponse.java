package com.yodlee.iae.health.datatypes.uigroup;

import java.util.List;

import com.yodlee.iae.health.datatypes.kafkagroup.GroupCategory;

import lombok.Data;

public @Data class GroupListResponse {
		GroupCategory category;
		List<String>  groupNames;

}
