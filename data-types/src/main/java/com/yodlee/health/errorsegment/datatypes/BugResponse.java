
package com.yodlee.health.errorsegment.datatypes;

import java.util.List;
import javax.inject.Named;

import lombok.Data;
/**
 * 
 * @author srai
 *
 */
@Named
public @Data class BugResponse {
	private String status;
	private String message;
	private List<BugEntity> bugs;
	private String error;
}
