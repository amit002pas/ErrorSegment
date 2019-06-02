/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */

package com.yodlee.iae.health.gateway.util;

import java.text.MessageFormat;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

/**
 * Class reads the property values from the SplunkQuery.properties file
 * 
 * @author skaipa
 *
 */
@Configuration
@PropertySource("classpath:SplunkQuery.properties")
@Named
public class SplunkQueryReader {

	@Inject
	private Environment env;

	/**
	 * Return property value based on the key
	 * 
	 * @param key
	 * @return property value
	 */
	public String getPropertyByKey(String key) {
		return env.getProperty(key);
	}

	/**
	 * Return property value based on the key, by placing the arguments
	 * 
	 * @param key
	 * @param args
	 * @return
	 */
	public String getPropertyByKey(String key, Object[] args) {
		return MessageFormat.format(getPropertyByKey(key), args);
	}
}
