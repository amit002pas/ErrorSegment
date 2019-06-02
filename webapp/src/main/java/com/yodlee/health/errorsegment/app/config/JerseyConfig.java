/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */
package com.yodlee.health.errorsegment.app.config;

import javax.annotation.PostConstruct;
import javax.inject.Named;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletProperties;
import org.springframework.stereotype.Component;

import com.yodlee.health.errorsegment.jnanalysis.rest.impl.JNAnalysisServiceRestImpl;
import com.yodlee.iae.health.authenticationimpl.AuthenticationServiceImpl;
import com.yodlee.iae.health.bugautocloser.BugAutoCloserServiceRestImpl;
import com.yodlee.iae.health.refresh.impl.RefreshStatsImpl;
import com.yodlee.iae.health.uigroupimpl.CategoryFilterImpl;

import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;

@Named
@Component
//@ApplicationPath("/api")
public class JerseyConfig extends ResourceConfig {
	
	public JerseyConfig() {
		System.out.println("^^^^^I mhere");
		register(JNAnalysisServiceRestImpl.class);
		register(CategoryFilterImpl.class);
		register(RefreshStatsImpl.class);
		register(AuthenticationServiceImpl.class);
		register(BugAutoCloserServiceRestImpl.class);
		
		property(ServletProperties.FILTER_FORWARD_ON_404, true);
		
	}
	
	
	@PostConstruct
	public void init() {
		System.out.println("*****************************************88");
		// Register components where DI is needed
		this.configureSwagger();
	}

	 private void configureSwagger() {
		register(ApiListingResource.class);
		this.register(SwaggerSerializers.class);
		
    	BeanConfig beanConfig = new BeanConfig();
    	beanConfig.setVersion("0.0.1");
    	beanConfig.setSchemes(new String[]{"http"});
    	beanConfig.setHost("localhost:9006");
    	beanConfig.setBasePath("/swagger/index.html");
    	beanConfig.setDescription("Sample");
       	beanConfig.setResourcePackage("com.yodlee.health.errorsegment");
    	beanConfig.setPrettyPrint(true);
    	beanConfig.setScan(true);
    	
	}
} 
