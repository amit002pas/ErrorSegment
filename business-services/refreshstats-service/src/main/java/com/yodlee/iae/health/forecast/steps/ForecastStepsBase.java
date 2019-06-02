/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */

package com.yodlee.iae.health.forecast.steps;

public interface ForecastStepsBase<T,U,V> {
	
	/**
	 * Takes input as T and U of interface ForecastStepsBase<T,U,V>
	 */
	public void setInput(T inputObject, U inputObject2);
	
	/**
	 * Consumes input T and U of interface ForecastStepsBase<T,U,V> to compute V
	 */
	public void execute();
	
	/**
	 * 
	 * @returns the third parameter V of interface ForecastStepsBase<T,U,V>
	 */
	public V getOutput();
}
