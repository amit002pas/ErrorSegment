package com.yodlee.health.errorsegment.app.config;

import javax.inject.Named;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.client.RestTemplate;

import com.j2bugzilla.base.BugFactory;
import com.j2bugzilla.base.BugzillaConnector;


/**
 * 
 * @author srai
 * 
 * This class is to configure the project dependencies
 *
 */
@EnableCaching
@Named
@Configuration
public class AppConfiguration {
	
	private static String cacheFile;

	@Value("${cache.file}")
	private void setCacheFile(String path) {
		AppConfiguration.cacheFile = path;
	} 
		
	
	@Bean
	public EhCacheManagerFactoryBean ehCacheCacheManager() {
		EhCacheManagerFactoryBean cmfb = new EhCacheManagerFactoryBean();
		cmfb.setConfigLocation(new ClassPathResource(cacheFile));
		cmfb.setShared(true);
		return cmfb;
	}
	
	@Bean
	public CacheManager cacheManager() {
		return new EhCacheCacheManager(ehCacheCacheManager().getObject());
	}
	
	@Bean
	public BugzillaConnector getBugzillaConnector(){
		return new BugzillaConnector();
	}
	@Bean
	public BugFactory getBugFactory(){
		return new BugFactory();
	} 
	
	/*@Bean
	public RestTemplate restCreation() {
		return new RestTemplate();
	} 
	*/
	
	
	
}
