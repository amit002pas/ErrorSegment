/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */

package com.yodlee.iae.health.errorbucket;


import java.util.function.Consumer;
import java.util.function.Supplier;

import com.yodlee.iae.common.services.ServiceBase;

/**
 * @author akumar23
 *
 */
public abstract class ServiceIO extends ServiceBase implements Consumer<Object>, Supplier<Object> {
	public Object process(Object obj) {
		this.accept(obj);
		this.execute();
		return this.get();
	}
}
