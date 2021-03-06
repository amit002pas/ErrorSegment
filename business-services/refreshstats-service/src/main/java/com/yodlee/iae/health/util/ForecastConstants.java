package com.yodlee.iae.health.util;

public interface ForecastConstants {

	String QC_REST_SERVICE_DOWN_MESSAGE = "WebServiceTransportException: Found [302]";
	String QC_REST_SERVICE_EXCEPTION_MESSAGE = "QC Server Is Down !";
	
	String GENUINE_FAILURE_ERROR_TYPE="Genuine";
	
	int QC_ERROR_COLOUMN_INDEX=3;
	int _500statusCode = 500;
	
	String JDBC_DRIVER = "oracle.jdbc.OracleDriver";
	String COUNTRY_NAME_FETCH_QUERY_REPALDA = "SELECT COUNTRY.COUNTRY_ISO_CODE FROM COUNTRY INNER JOIN LOCALE ON COUNTRY.COUNTRY_ID = LOCALE.LOCALE_ID INNER JOIN SUM_INFO_SPTD_LOCALE ON LOCALE.LOCALE_ID = SUM_INFO_SPTD_LOCALE.LOCALE_ID INNER JOIN SUM_INFO ON SUM_INFO_SPTD_LOCALE.SUM_INFO_ID = SUM_INFO.SUM_INFO_ID AND SUM_INFO.SUM_INFO_ID = SUMINFO";
	String SUM_INFO_CONTAINER_NAME_FETCH_QUERY = "SELECT SUM_INFO.SUM_INFO_ID, TAG.TAG FROM SUM_INFO INNER JOIN TAG ON TAG.TAG_ID = SUM_INFO.TAG_ID AND SUM_INFO.IS_READY = 1 AND SUM_INFO.IS_BETA = 0 AND SUM_INFO.CLASS_NAME = 'AGENT_NAME'";
	String REPALDA_DB_HOST = "jdbc:oracle:thin:@192.168.84.20:1521/repalda";
	String REPALDA_DB_USERNAME = "read";
	String REPALDA_DB_PWD = "read";
	
	String CONTAINER_NAME_FIELD="containerName";
	String LOCALE_NAME_FIELD="localeName";
	String CURRENT_PREDICTION_SEPERATOR="#";
	String DELIMITER="-";
	String YUVA_SEGMENT_CONSTANT="yuvaData";
	String INTERMEDIATE="intermediate";
	String ERROR_BUCKET_CONSTANT="bucketData";
	String ISYUVAWORKING = "isyuvaworking";
}
