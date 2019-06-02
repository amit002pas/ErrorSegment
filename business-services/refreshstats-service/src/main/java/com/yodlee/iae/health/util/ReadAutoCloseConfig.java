/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */
package com.yodlee.iae.health.util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author vchhetri
 *
 */
@Named
@EnableAutoConfiguration
public class ReadAutoCloseConfig {
	@Inject
	private ResourceLoader resourceLoader;

	private Map<Integer, BugAutoCloserConfigModel> configedData = new HashMap<>();
	private final static String ERROR_CODE= "error-code";
	private final static String RETRY_COUNT = "retry-count";
	private final static String RETRY_INTERVAL = "retry-interval";

	@PostConstruct
	public void init() {
		parseAndMapConfigMetaData();
	}
	public BugAutoCloserConfigModel getBugAutoCloserConfigModel(int errorCode){
		if(configedData.isEmpty()){
			parseAndMapConfigMetaData();
		}		
		return configedData.get(errorCode);
	}
	private File readFile() {
		File dbAsFile =new File("bugAutoCloseConfig.xml");
		return dbAsFile;
	}

	private void parseAndMapConfigMetaData() {
		File file = readFile();
		if(null == file) return;
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = null;
		Document doc = null;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			System.out.println("Exception occured : "+e.getMessage());
		}
		try {
			doc = dBuilder.parse(file);
		} catch (SAXException | IOException e) {
			System.out.println("Exception occured : "+e.getMessage());
		}
		doc.getDocumentElement().normalize();
		NodeList nList = doc.getElementsByTagName(ERROR_CODE);
		
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				BugAutoCloserConfigModel bugAutoCloserConfigModel = new BugAutoCloserConfigModel();
				bugAutoCloserConfigModel.setRetryCount(Integer.parseInt(eElement.getElementsByTagName(RETRY_COUNT).item(0).getTextContent()));
				bugAutoCloserConfigModel.setRetryInterval(Integer.parseInt(eElement.getElementsByTagName(RETRY_INTERVAL).item(0).getTextContent()));
				configedData.put(Integer.parseInt(eElement.getAttribute("id")), bugAutoCloserConfigModel);				
			}
		}

	}

}
