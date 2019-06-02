/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */
package com.yodlee.iae.health.errorbucket.steps;



import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yodlee.health.errorsegment.datatypes.forecast.Bucket;
import com.yodlee.health.errorsegment.datatypes.qc.ServerStatsBean;
import com.yodlee.health.errorsegment.gateway.util.GatewayUtils;
import com.yodlee.health.errorsegment.resources.errorsegment.CacheItem;
import com.yodlee.iae.health.gateway.errorbucket.FirememConnect;
import com.yodlee.iae.health.repository.errorsegment.BucketCounterRepository;
import com.yodlee.iae.health.repository.errorsegment.ErrorBucketRepository;

@RunWith(SpringJUnit4ClassRunner.class)
public class IssueAnalyzerStepTest {

	@InjectMocks
	IssueAnalyzerStep issueAnalyzerStep;

	@Mock
	private ErrorBucketRepository errorSegmentRepository;
	
	@Mock
	private BucketCounterRepository bucketCounterRepository;
	@Mock
	private GatewayUtils gatewayUtils;
	@Mock
	private FirememConnect firememConnect;


	@Before
	public void setup() {
		ReflectionTestUtils.setField(issueAnalyzerStep, "noOfUsersToprocess", 2);
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void test() throws JsonParseException, IOException, InterruptedException, JSONException{
		List<Bucket> obj = new ArrayList<>();
		Bucket bucket = new Bucket();
		List<CacheItem> cacheItemIds = new ArrayList<>();
		CacheItem cacheItem = new CacheItem();
		cacheItem.setCacheItemId("12345");
		cacheItem.setCobrandId("11121212");
		cacheItem.setDbId("2222");
		cacheItem.setMsaId("123411234");
		CacheItem cacheItem1 = new CacheItem();
		cacheItem1.setCacheItemId("12345");
		cacheItem1.setCobrandId("11121212");
		cacheItem1.setDbId("2222");
		cacheItem1.setMsaId("123411234");
		CacheItem cacheItem2 = new CacheItem();
		cacheItem2.setCacheItemId("-1");
		cacheItem2.setCobrandId("11121212");
		cacheItem2.setDbId("2222");
		cacheItem2.setMsaId("123411234");
		cacheItemIds.add(cacheItem);
		cacheItemIds.add(cacheItem1);
		cacheItemIds.add(cacheItem2);
		bucket.setCacheItemIds(cacheItemIds);
		bucket.setAgentName("Chase");
		bucket.setErrorCode("403");
		bucket.setErrorGroup("G1");
		bucket.setErrorSegmentId(121212);
		bucket.setErrorType("agent");
		bucket.setLocale("US");
		bucket.setMSAFailure(false);
		bucket.setSumInfo("5");
		bucket.setStacktrace("nullpointerexception");
		bucket.setSiteId("12");
		bucket.setRefreshTime(new Date());

		Bucket bucket1 = new Bucket();
		List<CacheItem> cacheItemIds1 = new ArrayList<>();
		CacheItem cacheItem11 = new CacheItem();
		cacheItem11.setCacheItemId("-1");
		cacheItem11.setCobrandId("11121212");
		cacheItem11.setDbId("2222");
		cacheItem11.setMsaId("123411234");
		cacheItemIds1.add(cacheItem11);
		bucket1.setCacheItemIds(cacheItemIds1);
		bucket1.setAgentName("Wels");
		bucket1.setErrorCode("0");
		bucket1.setErrorGroup("G2");
		bucket1.setErrorSegmentId(121212);
		bucket1.setErrorType("site");
		bucket1.setLocale("US");
		bucket1.setMSAFailure(false);
		bucket1.setSumInfo("6");
		bucket1.setStacktrace("Exceptionpointerexception");
		bucket1.setSiteId("12");
		bucket1.setRefreshTime(new Date());

		Bucket bucket2 = new Bucket();
		List<CacheItem> cacheItemIds22 = new ArrayList<>();
		CacheItem cacheItem111 = new CacheItem();
		cacheItem111.setCacheItemId("12345");
		cacheItem111.setCobrandId("11121212");
		cacheItem111.setDbId("2222");
		cacheItem111.setMsaId("123411234");
		CacheItem cacheItem222 = new CacheItem();
		cacheItem222.setCacheItemId("12345");
		cacheItem222.setCobrandId("11121212");
		cacheItem222.setDbId("2222");
		cacheItem222.setMsaId("123411234");
		CacheItem cacheItem333 = new CacheItem();
		cacheItem333.setCacheItemId("-1");
		cacheItem333.setCobrandId("11121212");
		cacheItem333.setDbId("2222");
		cacheItem333.setMsaId("123411234");
		cacheItemIds22.add(cacheItem111);
		cacheItemIds22.add(cacheItem222);
		cacheItemIds22.add(cacheItem333);
		bucket2.setCacheItemIds(cacheItemIds22);
		bucket2.setAgentName("Chase");
		bucket2.setErrorCode("403");
		bucket2.setErrorGroup("G1");
		bucket2.setErrorSegmentId(121212);
		bucket2.setErrorType("agent");
		bucket2.setLocale("US");
		bucket2.setMSAFailure(true);
		bucket2.setSumInfo("7");
		bucket2.setStacktrace("nullpointerexception");
		bucket2.setSiteId("12");
		bucket2.setRefreshTime(new Date());

		obj.add(bucket);
		obj.add(bucket1);
		obj.add(bucket2);



		List<String> cacheItemIdsss= new ArrayList<>();
		cacheItemIdsss.add("12345");

		Mockito.when(bucketCounterRepository.incrementAndGetAuditCounter()).thenReturn(121212);
		String response = "{\"token\":\"12323871263172\"}";
		ObjectMapper mapper = new ObjectMapper();
		JsonFactory factory = mapper.getFactory();
		JsonParser parser = factory.createParser(response);
		JsonNode actualObj = mapper.readTree(parser);
		Mockito.when(gatewayUtils.generateToken(true)).thenReturn(actualObj);
		Mockito.when(firememConnect.checkCurrentFiremem("12323871263172",cacheItem)).thenReturn(1);
		Mockito.when(firememConnect.checkCurrentFiremem("12323871263172",cacheItem111)).thenReturn(0);
		List<ServerStatsBean> serverStatsList = new ArrayList<>();
		ServerStatsBean serverStatsBean = new ServerStatsBean();
		serverStatsBean.setErrorCode("403");
		serverStatsBean.setScriptVersion("1111");
		serverStatsBean.setExceptionStackTrace("null");
		ServerStatsBean serverStatsBean1 = new ServerStatsBean();
		serverStatsBean1.setErrorCode("403");
		serverStatsBean1.setScriptVersion("1111");
		serverStatsBean1.setExceptionStackTrace("null");

		ServerStatsBean serverStatsBean2 = new ServerStatsBean();
		serverStatsBean2.setErrorCode("403");
		serverStatsBean2.setScriptVersion("1111");
		serverStatsBean2.setExceptionStackTrace("null");

		serverStatsList.add(serverStatsBean);
		serverStatsList.add(serverStatsBean1);
		serverStatsList.add(serverStatsBean2);
	
		issueAnalyzerStep.process(obj);

	}

	@Test
	public void test3() throws JsonParseException, IOException, InterruptedException, JSONException{
		List<Bucket> obj = new ArrayList<>();
		Bucket bucket = new Bucket();
		List<CacheItem> cacheItemIds = new ArrayList<>();
		CacheItem cacheItem = new CacheItem();
		cacheItem.setCacheItemId("12345");
		cacheItem.setCobrandId("11121212");
		cacheItem.setDbId("2222");
		cacheItem.setMsaId("123411234");
		CacheItem cacheItem1 = new CacheItem();
		cacheItem1.setCacheItemId("12345");
		cacheItem1.setCobrandId("11121212");
		cacheItem1.setDbId("2222");
		cacheItem1.setMsaId("123411234");
		CacheItem cacheItem2 = new CacheItem();
		cacheItem2.setCacheItemId("-1");
		cacheItem2.setCobrandId("11121212");
		cacheItem2.setDbId("2222");
		cacheItem2.setMsaId("123411234");
		cacheItemIds.add(cacheItem);
		cacheItemIds.add(cacheItem1);
		cacheItemIds.add(cacheItem2);
		bucket.setCacheItemIds(cacheItemIds);
		bucket.setAgentName("Chase");
		bucket.setErrorCode("403");
		bucket.setErrorGroup("G1");
		bucket.setErrorSegmentId(121212);
		bucket.setErrorType("agent");
		bucket.setLocale("US");
		bucket.setMSAFailure(false);
		bucket.setSumInfo("5");
		bucket.setStacktrace("nullpointerexception");
		bucket.setSiteId("12");
		bucket.setRefreshTime(new Date());


		obj.add(bucket);



		List<String> cacheItemIdsss= new ArrayList<>();
		cacheItemIdsss.add("12345");

		Mockito.when(bucketCounterRepository.incrementAndGetAuditCounter()).thenReturn(121212);
		String response = "{\"token\":\"12323871263172\"}";
		ObjectMapper mapper = new ObjectMapper();
		JsonFactory factory = mapper.getFactory();
		JsonParser parser = factory.createParser(response);
		JsonNode actualObj = mapper.readTree(parser);
		Mockito.when(gatewayUtils.generateToken(true)).thenReturn(actualObj);
		Mockito.when(firememConnect.checkCurrentFiremem("12323871263172",cacheItem)).thenReturn(1);
		List<ServerStatsBean> serverStatsList = new ArrayList<>();
		ServerStatsBean serverStatsBean = new ServerStatsBean();
		serverStatsBean.setErrorCode("400");
		serverStatsBean.setScriptVersion("1112");
		serverStatsBean.setExceptionStackTrace("nullll");
		ServerStatsBean serverStatsBean1 = new ServerStatsBean();
		serverStatsBean1.setErrorCode("403");
		serverStatsBean1.setScriptVersion("1110");
		serverStatsBean1.setExceptionStackTrace("nullss");

		ServerStatsBean serverStatsBean2 = new ServerStatsBean();
		serverStatsBean2.setErrorCode("401");
		serverStatsBean2.setScriptVersion("1109");
		serverStatsBean2.setExceptionStackTrace("nulddl");

		serverStatsList.add(serverStatsBean);
		serverStatsList.add(serverStatsBean1);
		serverStatsList.add(serverStatsBean2);
		issueAnalyzerStep.process(obj);

	}

	@Test
	public void testSuccess() throws JsonParseException, IOException, InterruptedException, JSONException{
		List<Bucket> obj = new ArrayList<>();
		Bucket bucket1 = new Bucket();
		List<CacheItem> cacheItemIds1 = new ArrayList<>();
		CacheItem cacheItem11 = new CacheItem();
		cacheItem11.setCacheItemId("-1");
		cacheItem11.setCobrandId("11121212");
		cacheItem11.setDbId("2222");
		cacheItem11.setMsaId("123411234");
		cacheItemIds1.add(cacheItem11);
		bucket1.setCacheItemIds(cacheItemIds1);
		bucket1.setAgentName("Wels");
		bucket1.setErrorCode("0");
		bucket1.setErrorGroup("G2");
		bucket1.setErrorSegmentId(121212);
		bucket1.setErrorType("site");
		bucket1.setLocale("US");
		bucket1.setMSAFailure(false);
		bucket1.setSumInfo("6");
		bucket1.setStacktrace("Exceptionpointerexception");
		bucket1.setSiteId("12");
		bucket1.setRefreshTime(new Date());

		obj.add(bucket1);

		List<String> cacheItemIdsss= new ArrayList<>();
		cacheItemIdsss.add("12345");

		Mockito.when(bucketCounterRepository.incrementAndGetAuditCounter()).thenReturn(121212);
		String response = "{\"token\":\"12323871263172\"}";
		ObjectMapper mapper = new ObjectMapper();
		JsonFactory factory = mapper.getFactory();
		JsonParser parser = factory.createParser(response);
		JsonNode actualObj = mapper.readTree(parser);
		Mockito.when(gatewayUtils.generateToken(true)).thenReturn(actualObj);
		Mockito.when(firememConnect.checkCurrentFiremem("12323871263172",cacheItem11)).thenReturn(1);
		List<ServerStatsBean> serverStatsList = new ArrayList<>();
		ServerStatsBean serverStatsBean = new ServerStatsBean();
		serverStatsBean.setErrorCode("424");
		serverStatsBean.setScriptVersion("1111");
		serverStatsBean.setExceptionStackTrace("null");
		ServerStatsBean serverStatsBean1 = new ServerStatsBean();
		serverStatsBean1.setErrorCode("403");
		serverStatsBean1.setScriptVersion("1111");
		serverStatsBean1.setExceptionStackTrace("null");

		ServerStatsBean serverStatsBean2 = new ServerStatsBean();
		serverStatsBean2.setErrorCode("403");
		serverStatsBean2.setScriptVersion("1111");
		serverStatsBean2.setExceptionStackTrace("null");

		serverStatsList.add(serverStatsBean);
		serverStatsList.add(serverStatsBean1);
		serverStatsList.add(serverStatsBean2);
		issueAnalyzerStep.process(obj);

	}

	@Test
	public void test2() throws JsonParseException, IOException, InterruptedException, JSONException{
		List<Bucket> obj = new ArrayList<>();
		Bucket bucket1 = new Bucket();
		List<CacheItem> cacheItemIds1 = new ArrayList<>();
		CacheItem cacheItem11 = new CacheItem();
		cacheItem11.setCacheItemId("-1");
		cacheItem11.setCobrandId("11121212");
		cacheItem11.setDbId("2222");
		cacheItem11.setMsaId("123411234");
		cacheItemIds1.add(cacheItem11);
		bucket1.setCacheItemIds(cacheItemIds1);
		bucket1.setAgentName("Wels");
		bucket1.setErrorCode("403");
		bucket1.setErrorGroup("G2");
		bucket1.setErrorSegmentId(121212);
		bucket1.setErrorType("site");
		bucket1.setLocale("US");
		bucket1.setMSAFailure(false);
		bucket1.setSumInfo("6");
		bucket1.setStacktrace("Exceptionpointerexception");
		bucket1.setSiteId("12");
		bucket1.setRefreshTime(new Date());

		obj.add(bucket1);

		List<String> cacheItemIdsss= new ArrayList<>();
		cacheItemIdsss.add("12345");

		Mockito.when(bucketCounterRepository.incrementAndGetAuditCounter()).thenReturn(121212);
		String response = "{\"token\":\"12323871263172\"}";
		ObjectMapper mapper = new ObjectMapper();
		JsonFactory factory = mapper.getFactory();
		JsonParser parser = factory.createParser(response);
		JsonNode actualObj = mapper.readTree(parser);
		Mockito.when(gatewayUtils.generateToken(true)).thenReturn(actualObj);
		Mockito.when(firememConnect.checkCurrentFiremem("12323871263172",cacheItem11)).thenReturn(1);
		List<ServerStatsBean> serverStatsList = new ArrayList<>();
		ServerStatsBean serverStatsBean = new ServerStatsBean();
		serverStatsBean.setErrorCode("402");
		serverStatsBean.setScriptVersion("1111");
		serverStatsBean.setExceptionStackTrace("null");
		ServerStatsBean serverStatsBean1 = new ServerStatsBean();
		serverStatsBean1.setErrorCode("403");
		serverStatsBean1.setScriptVersion("1111");
		serverStatsBean1.setExceptionStackTrace("null");

		ServerStatsBean serverStatsBean2 = new ServerStatsBean();
		serverStatsBean2.setErrorCode("403");
		serverStatsBean2.setScriptVersion("1111");
		serverStatsBean2.setExceptionStackTrace("null");

		serverStatsList.add(serverStatsBean);
		serverStatsList.add(serverStatsBean1);
		serverStatsList.add(serverStatsBean2);
		issueAnalyzerStep.process(obj);

	}

	@Test
	public void testMsa2() throws JsonParseException, IOException, InterruptedException, JSONException{
		List<Bucket> obj = new ArrayList<>();
		Bucket bucket1 = new Bucket();
		List<CacheItem> cacheItemIds1 = new ArrayList<>();
		CacheItem cacheItem11 = new CacheItem();
		cacheItem11.setCacheItemId("-1");
		cacheItem11.setCobrandId("11121212");
		cacheItem11.setDbId("2222");
		cacheItem11.setMsaId("123411234");
		cacheItemIds1.add(cacheItem11);
		bucket1.setCacheItemIds(cacheItemIds1);
		bucket1.setAgentName("Wels");
		bucket1.setErrorCode("403");
		bucket1.setErrorGroup("G2");
		bucket1.setErrorSegmentId(121212);
		bucket1.setErrorType("site");
		bucket1.setLocale("US");
		bucket1.setMSAFailure(false);
		bucket1.setSumInfo("6");
		bucket1.setStacktrace("Exceptionpointerexception");
		bucket1.setSiteId("12");
		bucket1.setRefreshTime(new Date());

		obj.add(bucket1);

		List<String> cacheItemIdsss= new ArrayList<>();

		Mockito.when(bucketCounterRepository.incrementAndGetAuditCounter()).thenReturn(121212);
		String response = "{\"token\":\"12323871263172\"}";
		ObjectMapper mapper = new ObjectMapper();
		JsonFactory factory = mapper.getFactory();
		JsonParser parser = factory.createParser(response);
		JsonNode actualObj = mapper.readTree(parser);
		Mockito.when(gatewayUtils.generateToken(true)).thenReturn(actualObj);
		Mockito.when(firememConnect.checkCurrentFiremem("12323871263172",cacheItem11)).thenReturn(1);
		List<ServerStatsBean> serverStatsList = new ArrayList<>();
		ServerStatsBean serverStatsBean = new ServerStatsBean();
		serverStatsBean.setErrorCode("0");
		serverStatsBean.setScriptVersion("1111");
		serverStatsBean.setExceptionStackTrace("null");
		ServerStatsBean serverStatsBean1 = new ServerStatsBean();
		serverStatsBean1.setErrorCode("403");
		serverStatsBean1.setScriptVersion("1111");
		serverStatsBean1.setExceptionStackTrace("null");

		ServerStatsBean serverStatsBean2 = new ServerStatsBean();
		serverStatsBean2.setErrorCode("403");
		serverStatsBean2.setScriptVersion("1111");
		serverStatsBean2.setExceptionStackTrace("null");

		serverStatsList.add(serverStatsBean);
		serverStatsList.add(serverStatsBean1);
		serverStatsList.add(serverStatsBean2);
		issueAnalyzerStep.process(obj);

	}

	@Test
	public void testMsa3() throws JsonParseException, IOException, InterruptedException, JSONException{
		List<Bucket> obj = new ArrayList<>();
		Bucket bucket1 = new Bucket();
		List<CacheItem> cacheItemIds1 = new ArrayList<>();
		CacheItem cacheItem11 = new CacheItem();
		cacheItem11.setCacheItemId("-1");
		cacheItem11.setCobrandId("11121212");
		cacheItem11.setDbId("2222");
		cacheItem11.setMsaId("123411234");
		cacheItemIds1.add(cacheItem11);
		bucket1.setCacheItemIds(cacheItemIds1);
		bucket1.setAgentName("Wels");
		bucket1.setErrorCode("403");
		bucket1.setErrorGroup("G2");
		bucket1.setErrorSegmentId(121212);
		bucket1.setErrorType("site");
		bucket1.setLocale("US");
		bucket1.setMSAFailure(false);
		bucket1.setSumInfo("6");
		bucket1.setStacktrace("Exceptionpointerexception");
		bucket1.setSiteId("12");
		bucket1.setRefreshTime(new Date());

		obj.add(bucket1);

		List<String> cacheItemIdsss= new ArrayList<>();

		Mockito.when(bucketCounterRepository.incrementAndGetAuditCounter()).thenReturn(121212);
		String response = "{\"token\":\"12323871263172\"}";
		ObjectMapper mapper = new ObjectMapper();
		JsonFactory factory = mapper.getFactory();
		JsonParser parser = factory.createParser(response);
		JsonNode actualObj = mapper.readTree(parser);
		Mockito.when(gatewayUtils.generateToken(true)).thenReturn(actualObj);
		Mockito.when(firememConnect.checkCurrentFiremem("12323871263172",cacheItem11)).thenReturn(1);
		List<ServerStatsBean> serverStatsList = new ArrayList<>();
		ServerStatsBean serverStatsBean = new ServerStatsBean();
		serverStatsBean.setErrorCode("424");
		serverStatsBean.setScriptVersion("1111");
		serverStatsBean.setExceptionStackTrace("null");
		ServerStatsBean serverStatsBean1 = new ServerStatsBean();
		serverStatsBean1.setErrorCode("403");
		serverStatsBean1.setScriptVersion("1111");
		serverStatsBean1.setExceptionStackTrace("null");

		ServerStatsBean serverStatsBean2 = new ServerStatsBean();
		serverStatsBean2.setErrorCode("403");
		serverStatsBean2.setScriptVersion("1111");
		serverStatsBean2.setExceptionStackTrace("null");

		serverStatsList.add(serverStatsBean);
		serverStatsList.add(serverStatsBean1);
		serverStatsList.add(serverStatsBean2);
		issueAnalyzerStep.process(obj);

	}
	@Test
	public void testMsa4() throws JsonParseException, IOException, InterruptedException, JSONException{
		List<Bucket> obj = new ArrayList<>();
		Bucket bucket1 = new Bucket();
		List<CacheItem> cacheItemIds1 = new ArrayList<>();
		CacheItem cacheItem11 = new CacheItem();
		cacheItem11.setCacheItemId("-1");
		cacheItem11.setCobrandId("11121212");
		cacheItem11.setDbId("2222");
		cacheItem11.setMsaId("123411234");
		cacheItemIds1.add(cacheItem11);
		bucket1.setCacheItemIds(cacheItemIds1);
		bucket1.setAgentName("Wels");
		bucket1.setErrorCode("403");
		bucket1.setErrorGroup("G2");
		bucket1.setErrorSegmentId(121212);
		bucket1.setErrorType("site");
		bucket1.setLocale("US");
		bucket1.setMSAFailure(false);
		bucket1.setSumInfo("6");
		bucket1.setStacktrace("Exceptionpointerexception");
		bucket1.setSiteId("12");
		bucket1.setRefreshTime(new Date());

		obj.add(bucket1);

		List<String> cacheItemIdsss= new ArrayList<>();

		Mockito.when(bucketCounterRepository.incrementAndGetAuditCounter()).thenReturn(121212);
		String response = "{\"token\":\"12323871263172\"}";
		ObjectMapper mapper = new ObjectMapper();
		JsonFactory factory = mapper.getFactory();
		JsonParser parser = factory.createParser(response);
		JsonNode actualObj = mapper.readTree(parser);
		Mockito.when(gatewayUtils.generateToken(true)).thenReturn(actualObj);
		Mockito.when(firememConnect.checkCurrentFiremem("12323871263172",cacheItem11)).thenReturn(1);
		List<ServerStatsBean> serverStatsList = new ArrayList<>();
		ServerStatsBean serverStatsBean = new ServerStatsBean();
		serverStatsBean.setErrorCode("419");
		serverStatsBean.setScriptVersion("1111");
		serverStatsBean.setExceptionStackTrace("null");
		ServerStatsBean serverStatsBean1 = new ServerStatsBean();
		serverStatsBean1.setErrorCode("419");
		serverStatsBean1.setScriptVersion("1111");
		serverStatsBean1.setExceptionStackTrace("null");

		ServerStatsBean serverStatsBean2 = new ServerStatsBean();
		serverStatsBean2.setErrorCode("0");
		serverStatsBean2.setScriptVersion("1111");
		serverStatsBean2.setExceptionStackTrace("");

		serverStatsList.add(serverStatsBean);
		serverStatsList.add(serverStatsBean1);
		serverStatsList.add(serverStatsBean2);
		issueAnalyzerStep.process(obj);

	}

	@Test
	public void testMsa5() throws JsonParseException, IOException, InterruptedException, JSONException{
		List<Bucket> obj = new ArrayList<>();
		Bucket bucket = new Bucket();
		List<CacheItem> cacheItemIds = new ArrayList<>();
		CacheItem cacheItem = new CacheItem();
		cacheItem.setCacheItemId("12345");
		cacheItem.setCobrandId("11121212");
		cacheItem.setDbId("2222");
		cacheItem.setMsaId("123411234");
		CacheItem cacheItem1 = new CacheItem();
		cacheItem1.setCacheItemId("12345");
		cacheItem1.setCobrandId("11121212");
		cacheItem1.setDbId("2222");
		cacheItem1.setMsaId("123411234");
		CacheItem cacheItem2 = new CacheItem();
		cacheItem2.setCacheItemId("-1");
		cacheItem2.setCobrandId("11121212");
		cacheItem2.setDbId("2222");
		cacheItem2.setMsaId("123411234");
		cacheItemIds.add(cacheItem);
		cacheItemIds.add(cacheItem1);
		cacheItemIds.add(cacheItem2);
		cacheItemIds.add(cacheItem);
		cacheItemIds.add(cacheItem1);
		cacheItemIds.add(cacheItem2);

		cacheItemIds.add(cacheItem);
		cacheItemIds.add(cacheItem1);
		cacheItemIds.add(cacheItem2);

		cacheItemIds.add(cacheItem);
		cacheItemIds.add(cacheItem1);
		cacheItemIds.add(cacheItem2);

		cacheItemIds.add(cacheItem);
		cacheItemIds.add(cacheItem1);
		cacheItemIds.add(cacheItem2);

		cacheItemIds.add(cacheItem);
		cacheItemIds.add(cacheItem1);
		cacheItemIds.add(cacheItem2);

		cacheItemIds.add(cacheItem);
		cacheItemIds.add(cacheItem1);
		cacheItemIds.add(cacheItem2);
		bucket.setCacheItemIds(cacheItemIds);
		bucket.setAgentName("Wels");
		bucket.setErrorCode("403");
		bucket.setErrorGroup("G1");
		bucket.setErrorSegmentId(121212);
		bucket.setErrorType("agent");
		bucket.setLocale("US");
		bucket.setMSAFailure(false);
		bucket.setSumInfo("6");
		bucket.setStacktrace("nullpointerexception");
		bucket.setSiteId("12");
		bucket.setRefreshTime(new Date());

		Bucket bucket1 = new Bucket();
		List<CacheItem> cacheItemIds1 = new ArrayList<>();
		CacheItem cacheItem11 = new CacheItem();
		cacheItem11.setCacheItemId("-1");
		cacheItem11.setCobrandId("11121212");
		cacheItem11.setDbId("2222");
		cacheItem11.setMsaId("123411234");
		cacheItemIds1.add(cacheItem11);
		bucket1.setCacheItemIds(cacheItemIds1);
		bucket1.setAgentName("Wels");
		bucket1.setErrorCode("0");
		bucket1.setErrorGroup("G2");
		bucket1.setErrorSegmentId(121212);
		bucket1.setErrorType("site");
		bucket1.setLocale("US");
		bucket1.setMSAFailure(false);
		bucket1.setSumInfo("6");
		bucket1.setStacktrace("Exceptionpointerexception");
		bucket1.setSiteId("12");
		bucket1.setRefreshTime(new Date());

		Bucket bucket2 = new Bucket();
		List<CacheItem> cacheItemIds22 = new ArrayList<>();
		CacheItem cacheItem111 = new CacheItem();
		cacheItem111.setCacheItemId("12345");
		cacheItem111.setCobrandId("11121212");
		cacheItem111.setDbId("2222");
		cacheItem111.setMsaId("123411234");
		CacheItem cacheItem222 = new CacheItem();
		cacheItem222.setCacheItemId("12345");
		cacheItem222.setCobrandId("11121212");
		cacheItem222.setDbId("2222");
		cacheItem222.setMsaId("123411234");
		CacheItem cacheItem333 = new CacheItem();
		cacheItem333.setCacheItemId("-1");
		cacheItem333.setCobrandId("11121212");
		cacheItem333.setDbId("2222");
		cacheItem333.setMsaId("123411234");
		cacheItemIds22.add(cacheItem111);
		cacheItemIds22.add(cacheItem222);
		cacheItemIds22.add(cacheItem333);
		bucket2.setCacheItemIds(cacheItemIds22);
		bucket2.setAgentName("Wels");
		bucket2.setErrorCode("403");
		bucket2.setErrorGroup("G1");
		bucket2.setErrorSegmentId(121212);
		bucket2.setErrorType("agent");
		bucket2.setLocale("US");
		bucket2.setMSAFailure(true);
		bucket2.setSumInfo("6");
		bucket2.setStacktrace("nullpointerexception");
		bucket2.setSiteId("12");
		bucket2.setRefreshTime(new Date());

		Bucket bucket3 = new Bucket();
		List<CacheItem> cacheItemIds33 = new ArrayList<>();
		CacheItem cacheItem4 = new CacheItem();
		cacheItem4.setCacheItemId("12345");
		cacheItem4.setCobrandId("11121212");
		cacheItem4.setDbId("2222");
		cacheItem4.setMsaId("123411234");
		bucket3.setCacheItemIds(cacheItemIds33);
		bucket3.setAgentName("Wels");
		bucket3.setErrorCode("403");
		bucket3.setErrorGroup("G1");
		bucket3.setErrorSegmentId(121212);
		bucket3.setErrorType("agent");
		bucket3.setLocale("US");
		bucket3.setMSAFailure(true);
		bucket3.setSumInfo("6");
		bucket3.setStacktrace("nullpointerexception");
		bucket3.setSiteId("12");
		bucket3.setRefreshTime(new Date());

		Bucket bucket4 = new Bucket();
		bucket4.setCacheItemIds(cacheItemIds33);
		bucket4.setAgentName("Wels");
		bucket4.setErrorCode("403");
		bucket4.setErrorGroup("G1");
		bucket4.setErrorSegmentId(121212);
		bucket4.setErrorType("agent");
		bucket4.setLocale("US");
		bucket4.setMSAFailure(true);
		bucket4.setSumInfo("6");
		bucket4.setStacktrace("nullpointerexception");
		bucket4.setSiteId("12");
		bucket4.setRefreshTime(new Date());
		Bucket bucket5 = new Bucket();
		bucket5.setCacheItemIds(cacheItemIds33);
		bucket5.setAgentName("Wels");
		bucket5.setErrorCode("403");
		bucket5.setErrorGroup("G1");
		bucket5.setErrorSegmentId(121212);
		bucket5.setErrorType("agent");
		bucket5.setLocale("US");
		bucket5.setMSAFailure(true);
		bucket5.setSumInfo("6");
		bucket5.setStacktrace("nullpointerexception");
		bucket5.setSiteId("12");
		bucket5.setRefreshTime(new Date());

		Bucket bucket6 = new Bucket();
		bucket6.setCacheItemIds(cacheItemIds33);
		bucket6.setAgentName("Wels");
		bucket6.setErrorCode("403");
		bucket6.setErrorGroup("G1");
		bucket6.setErrorSegmentId(121212);
		bucket6.setErrorType("agent");
		bucket6.setLocale("US");
		bucket6.setMSAFailure(true);
		bucket6.setSumInfo("6");
		bucket6.setStacktrace("nullpointerexception");
		bucket6.setSiteId("12");
		bucket6.setRefreshTime(new Date());
		Bucket bucket7 = new Bucket();
		bucket7.setCacheItemIds(cacheItemIds33);
		bucket7.setAgentName("Wels");
		bucket7.setErrorCode("403");
		bucket7.setErrorGroup("G1");
		bucket7.setErrorSegmentId(121212);
		bucket7.setErrorType("agent");
		bucket7.setLocale("US");
		bucket7.setMSAFailure(true);
		bucket7.setSumInfo("6");
		bucket7.setStacktrace("nullpointerexception");
		bucket7.setSiteId("12");
		bucket7.setRefreshTime(new Date());
		Bucket bucket8 = new Bucket();
		bucket8.setCacheItemIds(cacheItemIds33);
		bucket8.setAgentName("Wels");
		bucket8.setErrorCode("403");
		bucket8.setErrorGroup("G1");
		bucket8.setErrorSegmentId(121212);
		bucket8.setErrorType("agent");
		bucket8.setLocale("US");
		bucket8.setMSAFailure(true);
		bucket8.setSumInfo("6");
		bucket8.setStacktrace("nullpointerexception");
		bucket8.setSiteId("12");
		bucket8.setRefreshTime(new Date());
		Bucket bucket9 = new Bucket();
		bucket9.setCacheItemIds(cacheItemIds33);
		bucket9.setAgentName("Wels");
		bucket9.setErrorCode("403");
		bucket9.setErrorGroup("G1");
		bucket9.setErrorSegmentId(121212);
		bucket9.setErrorType("agent");
		bucket9.setLocale("US");
		bucket9.setMSAFailure(true);
		bucket9.setSumInfo("6");
		bucket9.setStacktrace("nullpointerexception");
		bucket9.setSiteId("12");
		bucket9.setRefreshTime(new Date());
		Bucket bucket10 = new Bucket();
		bucket10.setCacheItemIds(cacheItemIds33);
		bucket10.setAgentName("Wels");
		bucket10.setErrorCode("403");
		bucket10.setErrorGroup("G1");
		bucket10.setErrorSegmentId(121212);
		bucket10.setErrorType("agent");
		bucket10.setLocale("US");
		bucket10.setMSAFailure(true);
		bucket10.setSumInfo("6");
		bucket10.setStacktrace("nullpointerexception");
		bucket10.setSiteId("12");
		bucket10.setRefreshTime(new Date());
		Bucket bucket11 = new Bucket();
		bucket11.setCacheItemIds(cacheItemIds33);
		bucket11.setAgentName("Wels");
		bucket11.setErrorCode("403");
		bucket11.setErrorGroup("G1");
		bucket11.setErrorSegmentId(121212);
		bucket11.setErrorType("agent");
		bucket11.setLocale("US");
		bucket11.setMSAFailure(true);
		bucket11.setSumInfo("6");
		bucket11.setStacktrace("nullpointerexception");
		bucket11.setSiteId("12");
		bucket11.setRefreshTime(new Date());
		Bucket bucket12 = new Bucket();
		bucket12.setCacheItemIds(cacheItemIds33);
		bucket12.setAgentName("Wels");
		bucket12.setErrorCode("403");
		bucket12.setErrorGroup("G1");
		bucket12.setErrorSegmentId(121212);
		bucket12.setErrorType("agent");
		bucket12.setLocale("US");
		bucket12.setMSAFailure(true);
		bucket12.setSumInfo("6");
		bucket12.setStacktrace("nullpointerexception");
		bucket12.setSiteId("12");
		bucket12.setRefreshTime(new Date());
		Bucket bucket13 = new Bucket();
		bucket13.setCacheItemIds(cacheItemIds33);
		bucket13.setAgentName("Wels");
		bucket13.setErrorCode("403");
		bucket13.setErrorGroup("G1");
		bucket13.setErrorSegmentId(121212);
		bucket13.setErrorType("agent");
		bucket13.setLocale("US");
		bucket13.setMSAFailure(true);
		bucket13.setSumInfo("6");
		bucket13.setStacktrace("nullpointerexception");
		bucket13.setSiteId("12");
		bucket13.setRefreshTime(new Date());
		Bucket bucket14 = new Bucket();
		bucket14.setCacheItemIds(cacheItemIds33);
		bucket14.setAgentName("Wels");
		bucket14.setErrorCode("403");
		bucket14.setErrorGroup("G1");
		bucket14.setErrorSegmentId(121212);
		bucket14.setErrorType("agent");
		bucket14.setLocale("US");
		bucket14.setMSAFailure(true);
		bucket14.setSumInfo("6");
		bucket14.setStacktrace("nullpointerexception");
		bucket14.setSiteId("12");
		bucket14.setRefreshTime(new Date());
		Bucket bucket15 = new Bucket();
		bucket15.setCacheItemIds(cacheItemIds33);
		bucket15.setAgentName("Wels");
		bucket15.setErrorCode("403");
		bucket15.setErrorGroup("G1");
		bucket15.setErrorSegmentId(121212);
		bucket15.setErrorType("agent");
		bucket15.setLocale("US");
		bucket15.setMSAFailure(true);
		bucket15.setSumInfo("6");
		bucket15.setStacktrace("nullpointerexception");
		bucket15.setSiteId("12");
		bucket15.setRefreshTime(new Date());
		Bucket bucket16 = new Bucket();
		bucket16.setCacheItemIds(cacheItemIds33);
		bucket16.setAgentName("Wels");
		bucket16.setErrorCode("403");
		bucket16.setErrorGroup("G1");
		bucket16.setErrorSegmentId(121212);
		bucket16.setErrorType("agent");
		bucket16.setLocale("US");
		bucket16.setMSAFailure(true);
		bucket16.setSumInfo("6");
		bucket16.setStacktrace("nullpointerexception");
		bucket16.setSiteId("12");
		bucket16.setRefreshTime(new Date());


		obj.add(bucket);
		obj.add(bucket1);
		obj.add(bucket2);
		obj.add(bucket3);
		obj.add(bucket4);
		obj.add(bucket5);
		obj.add(bucket6);
		obj.add(bucket7);
		obj.add(bucket8);
		obj.add(bucket9);
		obj.add(bucket10);
		obj.add(bucket11);
		obj.add(bucket12);
		obj.add(bucket13);
		obj.add(bucket14);
		obj.add(bucket15);
		obj.add(bucket16);


		List<String> cacheItemIdsss= new ArrayList<>();

		Mockito.when(bucketCounterRepository.incrementAndGetAuditCounter()).thenReturn(121212);
		String response = "{\"token\":\"12323871263172\"}";
		ObjectMapper mapper = new ObjectMapper();
		JsonFactory factory = mapper.getFactory();
		JsonParser parser = factory.createParser(response);
		JsonNode actualObj = mapper.readTree(parser);
		Mockito.when(gatewayUtils.generateToken(true)).thenReturn(actualObj);
		Mockito.when(firememConnect.checkCurrentFiremem("12323871263172",cacheItem11)).thenReturn(1);
		List<ServerStatsBean> serverStatsList = new ArrayList<>();
		ServerStatsBean serverStatsBean = new ServerStatsBean();
		serverStatsBean.setErrorCode("419");
		serverStatsBean.setScriptVersion("1111");
		serverStatsBean.setExceptionStackTrace("null");
		ServerStatsBean serverStatsBean1 = new ServerStatsBean();
		serverStatsBean1.setErrorCode("0");
		serverStatsBean1.setScriptVersion("1111");
		serverStatsBean1.setExceptionStackTrace("null");

		ServerStatsBean serverStatsBean2 = new ServerStatsBean();
		serverStatsBean2.setErrorCode("409");
		serverStatsBean2.setScriptVersion("1111");
		serverStatsBean2.setExceptionStackTrace("");

		serverStatsList.add(serverStatsBean);
		serverStatsList.add(serverStatsBean1);
		serverStatsList.add(serverStatsBean2);
		issueAnalyzerStep.process(obj);

	}
	@Test
	public void testMsa6() throws JsonParseException, IOException, InterruptedException, JSONException{
		List<Bucket> obj = new ArrayList<>();
		Bucket bucket = new Bucket();
		List<CacheItem> cacheItemIds = new ArrayList<>();
		CacheItem cacheItem = new CacheItem();
		cacheItem.setCacheItemId("12345");
		cacheItem.setCobrandId("11121212");
		cacheItem.setDbId("2222");
		cacheItem.setMsaId("123411234");
		CacheItem cacheItem1 = new CacheItem();
		cacheItem1.setCacheItemId("12345");
		cacheItem1.setCobrandId("11121212");
		cacheItem1.setDbId("2222");
		cacheItem1.setMsaId("123411234");
		CacheItem cacheItem2 = new CacheItem();
		cacheItem2.setCacheItemId("-1");
		cacheItem2.setCobrandId("11121212");
		cacheItem2.setDbId("2222");
		cacheItem2.setMsaId("123411234");
		cacheItemIds.add(cacheItem);
		cacheItemIds.add(cacheItem1);
		cacheItemIds.add(cacheItem2);
		cacheItemIds.add(cacheItem);
		cacheItemIds.add(cacheItem1);
		cacheItemIds.add(cacheItem2);

		cacheItemIds.add(cacheItem);
		cacheItemIds.add(cacheItem1);
		cacheItemIds.add(cacheItem2);

		cacheItemIds.add(cacheItem);
		cacheItemIds.add(cacheItem1);
		cacheItemIds.add(cacheItem2);

		cacheItemIds.add(cacheItem);
		cacheItemIds.add(cacheItem1);
		cacheItemIds.add(cacheItem2);

		cacheItemIds.add(cacheItem);
		cacheItemIds.add(cacheItem1);
		cacheItemIds.add(cacheItem2);

		cacheItemIds.add(cacheItem);
		cacheItemIds.add(cacheItem1);
		cacheItemIds.add(cacheItem2);
		bucket.setCacheItemIds(cacheItemIds);
		bucket.setAgentName("Wels");
		bucket.setErrorCode("403");
		bucket.setErrorGroup("G1");
		bucket.setErrorSegmentId(121212);
		bucket.setErrorType("agent");
		bucket.setLocale("US");
		bucket.setMSAFailure(false);
		bucket.setSumInfo("6");
		bucket.setStacktrace("nullpointerexception");
		bucket.setSiteId("12");
		bucket.setRefreshTime(new Date());
		obj.add(bucket);

		List<String> cacheItemIdsss= new ArrayList<>();

		Mockito.when(bucketCounterRepository.incrementAndGetAuditCounter()).thenReturn(121212);
		String response = "{\"token\":\"12323871263172\"}";
		ObjectMapper mapper = new ObjectMapper();
		JsonFactory factory = mapper.getFactory();
		JsonParser parser = factory.createParser(response);
		JsonNode actualObj = mapper.readTree(parser);
		Mockito.when(gatewayUtils.generateToken(true)).thenReturn(actualObj);
		Mockito.when(firememConnect.checkCurrentFiremem("12323871263172",cacheItem)).thenReturn(1);
		List<ServerStatsBean> serverStatsList = new ArrayList<>();
		ServerStatsBean serverStatsBean = new ServerStatsBean();
		serverStatsBean.setErrorCode("419");
		serverStatsBean.setScriptVersion("1111");
		serverStatsBean.setExceptionStackTrace("null");
		ServerStatsBean serverStatsBean1 = new ServerStatsBean();
		serverStatsBean1.setErrorCode("0");
		serverStatsBean1.setScriptVersion("1111");
		serverStatsBean1.setExceptionStackTrace("null");

		ServerStatsBean serverStatsBean2 = new ServerStatsBean();
		serverStatsBean2.setErrorCode("409");
		serverStatsBean2.setScriptVersion("1111");
		serverStatsBean2.setExceptionStackTrace("");

		serverStatsList.add(serverStatsBean);
		serverStatsList.add(serverStatsBean1);
		serverStatsList.add(serverStatsBean2);
		issueAnalyzerStep.process(obj);
	}
	@Test
	public void testMsa7() throws JsonParseException, IOException, InterruptedException, JSONException{
		List<Bucket> obj = new ArrayList<>();
		Bucket bucket = new Bucket();
		List<CacheItem> cacheItemIds = new ArrayList<>();
		CacheItem cacheItem = new CacheItem();
		cacheItem.setCacheItemId("-1");
		cacheItem.setCobrandId("11121212");
		cacheItem.setDbId("2222");
		cacheItem.setMsaId("123411234");
		CacheItem cacheItem1 = new CacheItem();
		cacheItem1.setCacheItemId("-1");
		cacheItem1.setCobrandId("11121212");
		cacheItem1.setDbId("2222");
		cacheItem1.setMsaId("123411234");
		CacheItem cacheItem2 = new CacheItem();
		cacheItem2.setCacheItemId("-1");
		cacheItem2.setCobrandId("11121212");
		cacheItem2.setDbId("2222");
		cacheItem2.setMsaId("123411234");
		cacheItemIds.add(cacheItem);
		cacheItemIds.add(cacheItem1);
		cacheItemIds.add(cacheItem2);
		cacheItemIds.add(cacheItem);
		cacheItemIds.add(cacheItem1);
		cacheItemIds.add(cacheItem2);

		cacheItemIds.add(cacheItem);
		cacheItemIds.add(cacheItem1);
		cacheItemIds.add(cacheItem2);

		cacheItemIds.add(cacheItem);
		cacheItemIds.add(cacheItem1);
		cacheItemIds.add(cacheItem2);

		cacheItemIds.add(cacheItem);
		cacheItemIds.add(cacheItem1);
		cacheItemIds.add(cacheItem2);

		cacheItemIds.add(cacheItem);
		cacheItemIds.add(cacheItem1);
		cacheItemIds.add(cacheItem2);

		cacheItemIds.add(cacheItem);
		cacheItemIds.add(cacheItem1);
		cacheItemIds.add(cacheItem2);
		bucket.setCacheItemIds(cacheItemIds);
		bucket.setAgentName("Wels");
		bucket.setErrorCode("403");
		bucket.setErrorGroup("G1");
		bucket.setErrorSegmentId(121212);
		bucket.setErrorType("agent");
		bucket.setLocale("US");
		bucket.setMSAFailure(false);
		bucket.setSumInfo("6");
		bucket.setStacktrace("nullpointerexception");
		bucket.setSiteId("12");
		bucket.setRefreshTime(new Date());
		obj.add(bucket);

		List<String> cacheItemIdsss= new ArrayList<>();

		Mockito.when(bucketCounterRepository.incrementAndGetAuditCounter()).thenReturn(121212);
		String response = "{\"token\":\"12323871263172\"}";
		ObjectMapper mapper = new ObjectMapper();
		JsonFactory factory = mapper.getFactory();
		JsonParser parser = factory.createParser(response);
		JsonNode actualObj = mapper.readTree(parser);
		Mockito.when(gatewayUtils.generateToken(true)).thenReturn(actualObj);
		Mockito.when(firememConnect.checkCurrentFiremem("12323871263172",cacheItem)).thenReturn(1);
		List<ServerStatsBean> serverStatsList = new ArrayList<>();
		ServerStatsBean serverStatsBean = new ServerStatsBean();
		serverStatsBean.setErrorCode("419");
		serverStatsBean.setScriptVersion("1111");
		serverStatsBean.setExceptionStackTrace("null");
		ServerStatsBean serverStatsBean1 = new ServerStatsBean();
		serverStatsBean1.setErrorCode("0");
		serverStatsBean1.setScriptVersion("1111");
		serverStatsBean1.setExceptionStackTrace("null");

		ServerStatsBean serverStatsBean2 = new ServerStatsBean();
		serverStatsBean2.setErrorCode("409");
		serverStatsBean2.setScriptVersion("1111");
		serverStatsBean2.setExceptionStackTrace("");

		serverStatsList.add(serverStatsBean);
		serverStatsList.add(serverStatsBean1);
		serverStatsList.add(serverStatsBean2);
		issueAnalyzerStep.process(obj);
	}

	@Test
	public void testMsa8() throws JsonParseException, IOException, InterruptedException, JSONException{
		List<Bucket> obj = new ArrayList<>();
		Bucket bucket = new Bucket();
		List<CacheItem> cacheItemIds = new ArrayList<>();
		CacheItem cacheItem = new CacheItem();
		cacheItem.setCacheItemId("121212121");
		cacheItem.setCobrandId("11121212");
		cacheItem.setDbId("2222");
		cacheItem.setMsaId("123411234");
		bucket.setCacheItemIds(cacheItemIds);
		bucket.setAgentName("Wels");
		bucket.setErrorCode("403");
		bucket.setErrorGroup("G1");
		bucket.setErrorSegmentId(121212);
		bucket.setErrorType("agent");
		bucket.setLocale("US");
		bucket.setMSAFailure(false);
		bucket.setSumInfo("6");
		bucket.setStacktrace("nullpointerexception");
		bucket.setSiteId("12");
		bucket.setRefreshTime(new Date());
		obj.add(bucket);

		List<String> cacheItemIdsss= new ArrayList<>();

		Mockito.when(bucketCounterRepository.incrementAndGetAuditCounter()).thenReturn(121212);
		String response = "{\"token\":\"12323871263172\"}";
		ObjectMapper mapper = new ObjectMapper();
		JsonFactory factory = mapper.getFactory();
		JsonParser parser = factory.createParser(response);
		JsonNode actualObj = mapper.readTree(parser);
		Mockito.when(gatewayUtils.generateToken(true)).thenReturn(actualObj);
		Mockito.when(firememConnect.checkCurrentFiremem("12323871263172",cacheItem)).thenReturn(1);
		List<ServerStatsBean> serverStatsList = new ArrayList<>();
		ServerStatsBean serverStatsBean = new ServerStatsBean();
		serverStatsBean.setErrorCode("419");
		serverStatsBean.setScriptVersion("1111");
		serverStatsBean.setExceptionStackTrace("exception");
		ServerStatsBean serverStatsBean1 = new ServerStatsBean();
		serverStatsBean1.setErrorCode("0");
		serverStatsBean1.setScriptVersion("1111");
		serverStatsBean1.setExceptionStackTrace("null");

		ServerStatsBean serverStatsBean2 = new ServerStatsBean();
		serverStatsBean2.setErrorCode("0");
		serverStatsBean2.setScriptVersion("1111");
		serverStatsBean2.setExceptionStackTrace("");

		serverStatsList.add(serverStatsBean);
		serverStatsList.add(serverStatsBean1);
		serverStatsList.add(serverStatsBean2);
		issueAnalyzerStep.process(obj);
	}

	@Test
	public void testMsa9() throws JsonParseException, IOException, InterruptedException, JSONException{
		List<Bucket> obj = new ArrayList<>();
		Bucket bucket = new Bucket();
		List<CacheItem> cacheItemIds = new ArrayList<>();
		CacheItem cacheItem = new CacheItem();
		cacheItem.setCacheItemId("121212121");
		cacheItem.setCobrandId("11121212");
		cacheItem.setDbId("2222");
		cacheItem.setMsaId("123411234");
		cacheItemIds.add(cacheItem);
		bucket.setCacheItemIds(cacheItemIds);
		bucket.setAgentName("Wels");
		bucket.setErrorCode("403");
		bucket.setErrorGroup("G1");
		bucket.setErrorSegmentId(121212);
		bucket.setErrorType("agent");
		bucket.setLocale("US");
		bucket.setMSAFailure(false);
		bucket.setSumInfo("6");
		bucket.setStacktrace("nullpointerexception");
		bucket.setSiteId("12");
		bucket.setRefreshTime(new Date());
		obj.add(bucket);

		List<String> cacheItemIdsss= new ArrayList<>();

		Mockito.when(bucketCounterRepository.incrementAndGetAuditCounter()).thenReturn(121212);
		String response = "{\"token\":\"12323871263172\"}";
		ObjectMapper mapper = new ObjectMapper();
		JsonFactory factory = mapper.getFactory();
		JsonParser parser = factory.createParser(response);
		JsonNode actualObj = mapper.readTree(parser);
		Mockito.when(gatewayUtils.generateToken(true)).thenReturn(actualObj);
		Mockito.when(firememConnect.checkCurrentFiremem("12323871263172",cacheItem)).thenReturn(1);
		List<ServerStatsBean> serverStatsList = new ArrayList<>();
		ServerStatsBean serverStatsBean = new ServerStatsBean();
		serverStatsBean.setErrorCode("419");
		serverStatsBean.setScriptVersion("1111");
		serverStatsBean.setExceptionStackTrace("exception");
		ServerStatsBean serverStatsBean1 = new ServerStatsBean();
		serverStatsBean1.setErrorCode("0");
		serverStatsBean1.setScriptVersion("1111");
		serverStatsBean1.setExceptionStackTrace("null");

		ServerStatsBean serverStatsBean2 = new ServerStatsBean();
		serverStatsBean2.setErrorCode("0");
		serverStatsBean2.setScriptVersion("1111");
		serverStatsBean2.setExceptionStackTrace("");

		serverStatsList.add(serverStatsBean);
		serverStatsList.add(serverStatsBean1);
		serverStatsList.add(serverStatsBean2);
		issueAnalyzerStep.process(obj);
	}

	@Test
	public void testMsa10() throws JsonParseException, IOException, InterruptedException, JSONException{
		List<Bucket> obj = new ArrayList<>();
		Bucket bucket = new Bucket();
		List<CacheItem> cacheItemIds = new ArrayList<>();
		CacheItem cacheItem = new CacheItem();
		cacheItem.setCacheItemId("12345");
		cacheItem.setCobrandId("11121212");
		cacheItem.setDbId("2222");
		cacheItem.setMsaId("123411234");
		CacheItem cacheItem1 = new CacheItem();
		cacheItem1.setCacheItemId("12345");
		cacheItem1.setCobrandId("11121212");
		cacheItem1.setDbId("2222");
		cacheItem1.setMsaId("123411234");
		CacheItem cacheItem2 = new CacheItem();
		cacheItem2.setCacheItemId("-1");
		cacheItem2.setCobrandId("11121212");
		cacheItem2.setDbId("2222");
		cacheItem2.setMsaId("123411234");
		cacheItemIds.add(cacheItem);
		cacheItemIds.add(cacheItem1);
		cacheItemIds.add(cacheItem2);
		cacheItemIds.add(cacheItem);
		cacheItemIds.add(cacheItem1);
		cacheItemIds.add(cacheItem2);

		cacheItemIds.add(cacheItem);
		cacheItemIds.add(cacheItem1);
		cacheItemIds.add(cacheItem2);

		cacheItemIds.add(cacheItem);
		cacheItemIds.add(cacheItem1);
		cacheItemIds.add(cacheItem2);

		cacheItemIds.add(cacheItem);
		cacheItemIds.add(cacheItem1);
		cacheItemIds.add(cacheItem2);

		cacheItemIds.add(cacheItem);
		cacheItemIds.add(cacheItem1);
		cacheItemIds.add(cacheItem2);

		cacheItemIds.add(cacheItem);
		cacheItemIds.add(cacheItem1);
		cacheItemIds.add(cacheItem2);
		bucket.setCacheItemIds(cacheItemIds);
		bucket.setAgentName("Wels");
		bucket.setErrorCode("403");
		bucket.setErrorGroup("G1");
		bucket.setErrorSegmentId(121212);
		bucket.setErrorType("agent");
		bucket.setLocale("US");
		bucket.setMSAFailure(false);
		bucket.setSumInfo("6");
		bucket.setStacktrace("nullpointerexception");
		bucket.setSiteId("12");
		bucket.setRefreshTime(new Date());
		obj.add(bucket);

		List<String> cacheItemIdsss= new ArrayList<>();

		Mockito.when(bucketCounterRepository.incrementAndGetAuditCounter()).thenReturn(121212);
		String response = "{\"token\":\"12323871263172\"}";
		ObjectMapper mapper = new ObjectMapper();
		JsonFactory factory = mapper.getFactory();
		JsonParser parser = factory.createParser(response);
		JsonNode actualObj = mapper.readTree(parser);
		Mockito.when(gatewayUtils.generateToken(true)).thenReturn(actualObj);
		Mockito.when(firememConnect.checkCurrentFiremem("12323871263172",cacheItem)).thenReturn(1);
		List<ServerStatsBean> serverStatsList = new ArrayList<>();
		ServerStatsBean serverStatsBean = new ServerStatsBean();
		serverStatsBean.setErrorCode("419");
		serverStatsBean.setScriptVersion("1111");
		serverStatsBean.setExceptionStackTrace("exception");
		ServerStatsBean serverStatsBean1 = new ServerStatsBean();
		serverStatsBean1.setErrorCode("0");
		serverStatsBean1.setScriptVersion("1111");
		serverStatsBean1.setExceptionStackTrace("null");

		ServerStatsBean serverStatsBean2 = new ServerStatsBean();
		serverStatsBean2.setErrorCode("0");
		serverStatsBean2.setScriptVersion("1111");
		serverStatsBean2.setExceptionStackTrace("");

		serverStatsList.add(serverStatsBean);
		serverStatsList.add(serverStatsBean1);
		serverStatsList.add(serverStatsBean2);
		issueAnalyzerStep.process(obj);
	}

	@Test
	public void testMsa11() throws JsonParseException, IOException, InterruptedException, JSONException{
		List<Bucket> obj = new ArrayList<>();
		Bucket bucket = new Bucket();
		List<CacheItem> cacheItemIds = new ArrayList<>();
		CacheItem cacheItem = new CacheItem();
		cacheItem.setCacheItemId("121212121");
		cacheItem.setCobrandId("11121212");
		cacheItem.setDbId("2222");
		cacheItem.setMsaId("123411234");
		cacheItemIds.add(cacheItem);
		bucket.setCacheItemIds(cacheItemIds);
		bucket.setAgentName("Wels");
		bucket.setErrorCode("403");
		bucket.setErrorGroup("G1");
		bucket.setErrorSegmentId(121212);
		bucket.setErrorType("agent");
		bucket.setLocale("US");
		bucket.setMSAFailure(false);
		bucket.setSumInfo("6");
		bucket.setStacktrace("UnreachableBrowserException");
		bucket.setSiteId("12");
		bucket.setRefreshTime(new Date());
		obj.add(bucket);

		List<String> cacheItemIdsss= new ArrayList<>();

		Mockito.when(bucketCounterRepository.incrementAndGetAuditCounter()).thenReturn(121212);
		String response = "{\"token\":\"12323871263172\"}";
		ObjectMapper mapper = new ObjectMapper();
		JsonFactory factory = mapper.getFactory();
		JsonParser parser = factory.createParser(response);
		JsonNode actualObj = mapper.readTree(parser);
		Mockito.when(gatewayUtils.generateToken(true)).thenReturn(actualObj);
		Mockito.when(firememConnect.checkCurrentFiremem("12323871263172",cacheItem)).thenReturn(1);
		List<ServerStatsBean> serverStatsList = new ArrayList<>();
		ServerStatsBean serverStatsBean = new ServerStatsBean();
		serverStatsBean.setErrorCode("419");
		serverStatsBean.setScriptVersion("1111");
		serverStatsBean.setExceptionStackTrace("UnreachableBrowserException");
		ServerStatsBean serverStatsBean1 = new ServerStatsBean();
		serverStatsBean1.setErrorCode("0");
		serverStatsBean1.setScriptVersion("1111");
		serverStatsBean1.setExceptionStackTrace("null");

		ServerStatsBean serverStatsBean2 = new ServerStatsBean();
		serverStatsBean2.setErrorCode("0");
		serverStatsBean2.setScriptVersion("1111");
		serverStatsBean2.setExceptionStackTrace("");

		serverStatsList.add(serverStatsBean);
		serverStatsList.add(serverStatsBean1);
		serverStatsList.add(serverStatsBean2);
		issueAnalyzerStep.process(obj);
	}

	@Test
	public void testMsa12() throws JsonParseException, IOException, InterruptedException, JSONException{
		List<Bucket> obj = new ArrayList<>();
		Bucket bucket = new Bucket();
		List<CacheItem> cacheItemIds = new ArrayList<>();
		CacheItem cacheItem = new CacheItem();
		cacheItem.setCacheItemId("121212121");
		cacheItem.setCobrandId("11121212");
		cacheItem.setDbId("2222");
		cacheItem.setMsaId("123411234");
		cacheItemIds.add(cacheItem);
		bucket.setCacheItemIds(cacheItemIds);
		bucket.setAgentName("Wels");
		bucket.setErrorCode("403");
		bucket.setErrorGroup("G1");
		bucket.setErrorSegmentId(121212);
		bucket.setErrorType("agent");
		bucket.setLocale("US");
		bucket.setMSAFailure(false);
		bucket.setSumInfo("6");
		bucket.setStacktrace("UnreachableBrowserException");
		bucket.setSiteId("12");
		bucket.setRefreshTime(new Date());
		obj.add(bucket);

		List<String> cacheItemIdsss= new ArrayList<>();

		Mockito.when(bucketCounterRepository.incrementAndGetAuditCounter()).thenReturn(121212);
		String response = "{\"token\":\"12323871263172\"}";
		ObjectMapper mapper = new ObjectMapper();
		JsonFactory factory = mapper.getFactory();
		JsonParser parser = factory.createParser(response);
		JsonNode actualObj = mapper.readTree(parser);
		Mockito.when(gatewayUtils.generateToken(true)).thenReturn(actualObj);
		Mockito.when(firememConnect.checkCurrentFiremem("12323871263172",cacheItem)).thenReturn(1);
		List<ServerStatsBean> serverStatsList = new ArrayList<>();
		ServerStatsBean serverStatsBean = new ServerStatsBean();
		serverStatsBean.setErrorCode("419");
		serverStatsBean.setScriptVersion("1111");
		serverStatsBean.setExceptionStackTrace("UnreachableBrowserException");
		ServerStatsBean serverStatsBean1 = new ServerStatsBean();
		serverStatsBean1.setErrorCode("0");
		serverStatsBean1.setScriptVersion("1111");
		serverStatsBean1.setExceptionStackTrace("null");

		ServerStatsBean serverStatsBean2 = new ServerStatsBean();
		serverStatsBean2.setErrorCode("0");
		serverStatsBean2.setScriptVersion("1110");
		serverStatsBean2.setExceptionStackTrace("");

		serverStatsList.add(serverStatsBean);
		serverStatsList.add(serverStatsBean1);
		serverStatsList.add(serverStatsBean2);
		issueAnalyzerStep.process(obj);
	}
	@Test
	public void testMsa13() throws JsonParseException, IOException, InterruptedException, JSONException{
		List<Bucket> obj = new ArrayList<>();
		Bucket bucket = new Bucket();
		List<CacheItem> cacheItemIds = new ArrayList<>();
		CacheItem cacheItem = new CacheItem();
		cacheItem.setCacheItemId("121212121");
		cacheItem.setCobrandId("11121212");
		cacheItem.setDbId("2222");
		cacheItem.setMsaId("123411234");
		cacheItemIds.add(cacheItem);
		bucket.setCacheItemIds(cacheItemIds);
		bucket.setAgentName("Wels");
		bucket.setErrorCode("403");
		bucket.setErrorGroup("G1");
		bucket.setErrorSegmentId(121212);
		bucket.setErrorType("agent");
		bucket.setLocale("US");
		bucket.setMSAFailure(false);
		bucket.setSumInfo("6");
		bucket.setStacktrace("UnreachableBrowserException");
		bucket.setSiteId("12");
		bucket.setRefreshTime(new Date());
		obj.add(bucket);

		List<String> cacheItemIdsss= new ArrayList<>();

		Mockito.when(bucketCounterRepository.incrementAndGetAuditCounter()).thenReturn(121212);
		String response = "{\"token\":\"12323871263172\"}";
		ObjectMapper mapper = new ObjectMapper();
		JsonFactory factory = mapper.getFactory();
		JsonParser parser = factory.createParser(response);
		JsonNode actualObj = mapper.readTree(parser);
		Mockito.when(gatewayUtils.generateToken(true)).thenReturn(actualObj);
		Mockito.when(firememConnect.checkCurrentFiremem("12323871263172",cacheItem)).thenReturn(1);
		List<ServerStatsBean> serverStatsList = new ArrayList<>();
		ServerStatsBean serverStatsBean = new ServerStatsBean();
		serverStatsBean.setErrorCode("419");
		serverStatsBean.setScriptVersion("1111");
		serverStatsBean.setExceptionStackTrace("UnreachableBrowserException");
		ServerStatsBean serverStatsBean1 = new ServerStatsBean();
		serverStatsBean1.setErrorCode("0");
		serverStatsBean1.setScriptVersion("1110");
		serverStatsBean1.setExceptionStackTrace("null");

		ServerStatsBean serverStatsBean2 = new ServerStatsBean();
		serverStatsBean2.setErrorCode("0");
		serverStatsBean2.setScriptVersion("1109");
		serverStatsBean2.setExceptionStackTrace("");

		serverStatsList.add(serverStatsBean);
		serverStatsList.add(serverStatsBean1);
		serverStatsList.add(serverStatsBean2);
		issueAnalyzerStep.process(obj);
	}

	
}
