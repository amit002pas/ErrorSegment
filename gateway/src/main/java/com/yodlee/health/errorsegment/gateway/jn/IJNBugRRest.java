/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */

package com.yodlee.health.errorsegment.gateway.jn;

import javax.ws.rs.core.Response;

/**
 * @author vchhetri
 *
 */
public interface IJNBugRRest {
	/**
	 * Method to hit/send bugid to JN for re-analysis.
	 * @param bugIdsForJN
	 * @return Response object.
	 */
	public Response commJnForBugReanalysis(Integer bugIdsForJN);

}
