/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */
package com.yodlee.health.errorsegment.gateway.util;

public interface GatewayConstants {

	String FORMATTER="MM/dd/yyyy:HH:mm:ss";
	String DATE_FORMATTER="yyyy-MM-dd HH:mm:ss";
	String FAILURE="failure";
	String SUCCESS="success";
	String ALL="all";
	String INVALID_REQUEST="invalid request";
	String AUTHENTICATION_URL = "http://172.17.15.21:8888/R/A/L";
	String AUTHENTICATION_KEY = "Authorization";
	String GET_SEGMENT_URL = "http://172.17.15.230:8080/usersegmentation/segments/user-segment?agentName=INPUT_AGENT&cacheItem=INPUT_CII";
	String GET_CACHEITEM_URL = "http://172.17.15.21:8888/segments/user-segment?segmentId=INPUT_SEGID";
	String SEGMENT_LIST_URL = "http://172.17.15.21:8888/segments/user-segment?agentName=";
	String PATH_TRACE_URL = "http://172.17.15.230:8080/usersegmentation/segments/user-segment?agentName=INPUT_AGENT&cacheItem=INPUT_CII";
	String BUGR_CREATE_BUG_URL="http://172.17.15.21:8080/bug-resolution/bug/createupdate";
	String BUGR_STATUS_CHECK_URL="http://172.17.15.21:8080/bug-resolution/bug/status/createupdaterequest";
	
	String BUGR_ADD_COMMENT_URL="http://172.17.15.21:8080/bug-resolution/bug/INPUT_BUGID/comment/add";
	String BUGR_UPDATE_BUG_URL="http://172.17.15.21:8080/bug-resolution/bug/createupdate";
	//String BUGR_CREATE_BUG_URL="http://172.17.15.21:8080/bug-resolution/bug/create";
	String BUGR_SEARCH_HOURLY_BUG_URL= "http://172.17.15.21:8080/bug-resolution/searchbugs";
	String BUG_ID="id";
	String LOG24="24HRGLOG";
	
	String USERNAME_CONSTANT = "username";
	String PASSWORD_CONSTANT = "password";

	String USERNAME_VALUE = "presruser";
	
	

	String UPDATEJNATTACHMENTURL="http://172.17.15.21:8080/bug-resolution/bug/id/attachment/add";

	String BUGR_SEARCH_BUG_URL = "http://172.17.15.21:8080/bug-resolution/bug/INPUT_BUGID";

	String HAMMER_AUTHENTICATION_URL = "https://firemem.tools.yodlee.com/hammer/R/A/L";
	String REST_QC_REST_URL = "https://firemem.tools.yodlee.com/hammer/R/QC/EQ";
	String SERVER_STATS_QUERY_CODE = "queryCode";
	String SERVER_STATS_QUERY_CODE_VALUE = "servStats";
	String COBRAND_ID_CONSTANT = "cobrandId";
	String QUERY_PARAMETERS_CONSTANT = "queryParameters";
	String CACHE_ITEM_ID_CONSTANT = "cache_item_id";

	String AGENT_SEPERATOR_DELIMETER = ",";
	
	String BEARER = "Bearer ";
	String AUTHENTICATION = "authentication";
	String Firemem_Trigger_URL = "https://firemem.tools.yodlee.com/hammer/R/F/NR";
	String Firemem_Status_URL = "https://firemem.tools.yodlee.com/hammer/R/F/RS";


	String HEADER_ACCEPT = "Accept";
	String HEADER_CONTENT_TYPE = "Content-type";
	String HEADER_AUTHORIZATION = "Authorization";
	String HEADER_JSON_VALUE = "application/json";
	int _500statusCode = 500;
	String QC_REST_SERVICE_DOWN_MESSAGE = "WebServiceTransportException: Found [302]";
	String QC_REST_SERVICE_EXCEPTION_MESSAGE = "QC Server Is Down !";

	String HOST = "jdbc:mysql://192.168.57.101:3306/bugs";
	String USERNAME = "readonly";

	String BUGZILLA_VERIFICATION_QUERY = "select thetext from longdescs as ld,bugs as b where ld.bug_id=b.bug_id and ld.thetext LIKE '%"
			+ "INPUT_STACKTRACE"
			+ "%' and b.cf_errorcode LIKE '%INPUT_ERROR_CODE%' and b.cf_agents LIKE '%INPUT_AGENT_NAME%' and cf_workflow_status!='Reassigned' and cf_workflow_status!='Closed' LIMIT 1;";
	String BUGZILLA_DRIVER = "com.mysql.jdbc.Driver";
	String BUGZILLA_GET_BUG_QUERY = "select ld.bug_id from longdescs as ld,bugs as b where (b.status_whiteboard LIKE '%PreSR%' or b.status_whiteboard LIKE '%TTR%' or b.status_whiteboard LIKE '%IAT%') and ld.bug_id=b.bug_id and ld.thetext LIKE '%"
			+ "INPUT_STACKTRACE"
			+ "%' and b.cf_errorcode LIKE '%INPUT_ERROR_CODE%' and b.cf_agents LIKE '%INPUT_AGENT_NAME%' and cf_workflow_status!='Reassigned' and cf_workflow_status!='Closed' LIMIT 1;";
	String BUGZILLA_GET_BUG_QUERY_IMPACT = "select cf_impact,status_whiteboard from bugs where bug_id='" + "BUG_ID"
			+ "';";

	String BUGZILLA_URL = "https://blrbugzilla.yodlee.com";

	String REST_URL = "https://splunkapi.yodlee.com/";
	String SAVED_SEARCH_URL = "servicesNS/admin/search/saved/searches/";
	String DISPATCH_URL = "/dispatch";
	String SEARCH_URL = "services/search/jobs?output_mode=json";
	String PRE_SID_URL = "/services/search/jobs/";
	String POST_SID_URL = "/results?output_mode=json&count=1000000";
	String AUTH_URL = "services/auth/login";

	String SEARCH_KEYWORD = "search";
	String AUTHORIZATION_KEYWORD = "Authorization";
	String SPLUNK_KEYWORD = "Splunk";
	String RESULT_KEYWORD = "results";
	String INPUT_BUGID="INPUT_BUGID";

	//http://172.17.15.22:8080/iat
	String IATAuthenticationURL = "http://172.17.15.22:8080/iat/R/A/L";
	String IATAnalysisURL = "http://172.17.15.22:8080/iat/ERRORCODE/ANALYSIS";
	String IATResponseURL = "http://172.17.15.22:8080/iat/ERRORCODE/ANALYSIS?id=";

	
	String IATAuthenticationURLDev = "http://192.168.113.70:2020/iat/R/A/L";
	String IATAnalysisURLDev = "http://192.168.113.70:2020/iat/ERRORCODE/ANALYSIS";
	String IATResponseURLDev = "http://192.168.113.70:2020/iat/ERRORCODE/ANALYSIS?id="; 
	int BAD_REQUEST = 400;
	int SUCCESS_RESPONSE = 200;
 
	//https://bugr.orphic.yodlee.com/
	String BUGR_CREATE_BUG_SYNTHETIC_URL="https://bugr.orphic.yodlee.com/bug/create";
	String BUGR_FETCH_BUG_SYNTHETIC_URL = "https://bugr.orphic.yodlee.com/bug/INPUT_BUGID";
	String BUGR_ADD_ATTACHMENT_SYNTHETIC_URL="https://bugr.orphic.yodlee.com/bug/INPUT_BUGID/attachment";
	String BUGR_SEARCH_HOURLY_BUG_SYNTHETIC_URL= "https://bugr.orphic.yodlee.com/bug/search";
	String BUGR_UPDATE_BUG_SYNTHETIC_URL="https://bugr.orphic.yodlee.com/bug/INPUT_BUGID/update";


	final String MSA_CII_QUERY="select ci.cache_item_id from cache_info ci, sum_info si where ci.mem_site_acc_id=INPUT_MSA and ci.inactive_ind=0 and si.class_name = INPUT_CLASS and ci.sum_info_id=si.sum_info_id"; 

	
	String SERVER_STATS_CII = "SELECT mem_site_acc_id, cache_item_id, COBRAND_ID, sum_info_id, error_code, "
			+ "to_char(new_time(epoch_to_date(created), 'GMT', 'PST'), 'MM-dd-yyyy HH24:MI') as dump_created_in_pst, "
			+ "script_version, server_type, exception_stack_trace, SUBSTR(DBFILER_EXCEPTION, 0, 1000) DBFILER_EXCEPTION, "
			+ "script_latency / 1000 AS script_latency_in_secs, END_TO_END_TIME_IN_SECS / 1000 as END_TO_END_TIME_IN_SECS, "
			+ "site_id, REPLACE (document_dump_file, document_dump_file, "
			+ "(CONCAT( (CONCAT('https://yoshiee.yodlee.com:2443/Extractor/HTMLExtractor?dumpLink=', "
			+ "( case when REQUEST_TYPE = 'MFA' then '/mfa/' when REQUEST_TYPE = 'Cache' then '/cache/' when REQUEST_TYPE = 'Instant' then '/instant/' "
			+ "when REQUEST_TYPE = 'IAV' then '/IAV/' when REQUEST_TYPE=  'PaymentVerification' then '/paymentverification/' else NULL end))), "
			+ "(CONCAT(document_dump_file, '.gz'))))) as document_dump_file, response_type, response_sequence_num, refresh_source, num_successful_refresh, "
			+ "gatherer_ip, GATH_REQ_RECEIVED_TIME, GATH_SENT_RESPONSE_TIME, gatherer_robot_id, num_of_navigations,"
			+ " to_char(new_time(epoch_to_date(NULLIF(MFA_GATH_USER_RES_REC_TIME, 0)), 'GMT', 'PST'), 'MM-dd-yyyy HH24:MI') as MFA_GATH_USER_RES_REC_TIME, "
			+ "to_char(new_time(epoch_to_date(NULLIF(MFA_GATH_RES_SENT_TIME, 0)), 'GMT', 'PST'), 'MM-dd-yyyy HH24:MI') as MFA_GATH_RES_SENT_TIME, "
			+ "transaction_id, SITE_TXN_ID, NEW_TRANSACTIONS, UPD_TRANSACTIONS, NUM_ITEM_ACCOUNTS, NUM_ITEM_ACCTS_INSERTED,"
			+ " mfa_guid, download_size, is_docs_available, doc_download_latency, num_docs_avail_download, doc_save_latency, "
			+ "document_size_kb, prog_response_seq_num, prog_refresh_status_id FROM server_stats WHERE cache_item_id = ? "
			+ "and rownum<10 Order By Created Desc";
	
	
	String SERVER_STATS_MSA = "SELECT mem_site_acc_id, cache_item_id, COBRAND_ID, sum_info_id, error_code, "
				+ "to_char(new_time(epoch_to_date(created), 'GMT', 'PST'), 'MM-dd-yyyy HH24:MI') as dump_created_in_pst, "
				+ "script_version, server_type, exception_stack_trace, SUBSTR(DBFILER_EXCEPTION, 0, 1000) DBFILER_EXCEPTION, "
				+ "script_latency / 1000 AS script_latency_in_secs, END_TO_END_TIME_IN_SECS / 1000 as END_TO_END_TIME_IN_SECS, "
				+ "site_id, REPLACE (document_dump_file, document_dump_file, "
				+ "(CONCAT( (CONCAT('https://yoshiee.yodlee.com:2443/Extractor/HTMLExtractor?dumpLink=', "
				+ "( case when REQUEST_TYPE = 'MFA' then '/mfa/' when REQUEST_TYPE = 'Cache' then '/cache/' when REQUEST_TYPE = 'Instant' then '/instant/' "
				+ "when REQUEST_TYPE = 'IAV' then '/IAV/' when REQUEST_TYPE=  'PaymentVerification' then '/paymentverification/' else NULL end))), "
				+ "(CONCAT(document_dump_file, '.gz'))))) as document_dump_file, response_type, response_sequence_num, refresh_source, num_successful_refresh, "
				+ "gatherer_ip, GATH_REQ_RECEIVED_TIME, GATH_SENT_RESPONSE_TIME, gatherer_robot_id, num_of_navigations,"
				+ " to_char(new_time(epoch_to_date(NULLIF(MFA_GATH_USER_RES_REC_TIME, 0)), 'GMT', 'PST'), 'MM-dd-yyyy HH24:MI') as MFA_GATH_USER_RES_REC_TIME, "
				+ "to_char(new_time(epoch_to_date(NULLIF(MFA_GATH_RES_SENT_TIME, 0)), 'GMT', 'PST'), 'MM-dd-yyyy HH24:MI') as MFA_GATH_RES_SENT_TIME, "
				+ "transaction_id, SITE_TXN_ID, NEW_TRANSACTIONS, UPD_TRANSACTIONS, NUM_ITEM_ACCOUNTS, NUM_ITEM_ACCTS_INSERTED,"
				+ " mfa_guid, download_size, is_docs_available, doc_download_latency, num_docs_avail_download, doc_save_latency, "
				+ "document_size_kb, prog_response_seq_num, prog_refresh_status_id FROM server_stats WHERE mem_site_acc_id = ? "
				+ "and rownum<10 Order By Created Desc"; 
	 


	String QUERY_REFRESH_STATS="SplunkQuery.refreshStat";
	String QUERY_RAW_STATS="SplunkQuery.rawStat";
}
