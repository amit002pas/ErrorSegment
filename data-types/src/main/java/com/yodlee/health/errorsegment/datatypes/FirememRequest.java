package com.yodlee.health.errorsegment.datatypes;

import lombok.Data;

@Data
public class FirememRequest {
	
	String serverType="I";
	String itemType="2";
	String mfaPreference="0";
	String dbName;
	String itemId;
	String refreshRoute="D";
	String customrefreshRoute="C";
	String customRoute="";
	Boolean prodCertified=true;
	String agentFileType="JAVA";
	

}
