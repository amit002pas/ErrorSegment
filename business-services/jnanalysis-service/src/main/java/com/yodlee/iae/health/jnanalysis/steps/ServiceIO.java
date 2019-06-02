/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 **/
package com.yodlee.iae.health.jnanalysis.steps;


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
