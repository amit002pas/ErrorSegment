package com.yodlee.health.errorsegment.datatypes;


public class QCRequest {
	String queryCode;
	String cobrandId;
	QueryParameters queryParameters;
	public String getQueryCode() {
		return queryCode;
	}
	public void setQueryCode(String queryCode) {
		this.queryCode = queryCode;
	}
	public String getCobrandId() {
		return cobrandId;
	}
	public void setCobrandId(String cobrandId) {
		this.cobrandId = cobrandId;
	}
	public QueryParameters getQueryParameters() {
		return queryParameters;
	}
	public void setQueryParameters(QueryParameters queryParameters) {
		this.queryParameters = queryParameters;
	}
} 



