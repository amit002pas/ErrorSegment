package com.yodlee.health.errorsegment.datatypes;

	import java.io.IOException;
	
	import org.springframework.http.client.ClientHttpResponse;
	import org.springframework.web.client.DefaultResponseErrorHandler;


public class ToolsResponseHandler extends DefaultResponseErrorHandler {
       public void handleError(ClientHttpResponse response) throws IOException {
             
       }
       public boolean hasError(ClientHttpResponse response) throws IOException {
        return false; // or whatever you consider an error
    }
} 
