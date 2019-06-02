package com.yodlee.iae.health.util;

import java.io.IOException;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;

public class ToolsResponsehandler extends DefaultResponseErrorHandler {
	@Override
	public void handleError(ClientHttpResponse response) throws IOException {
		
	}
	@Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return false; 
    }
}