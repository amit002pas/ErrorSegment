/**
 *Copyright (c) 2018 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. 
 * Use is subject to license terms. 
 */

package com.yodlee.iae.health.gateway.splunk;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.yodlee.health.errorsegment.gateway.util.GatewayConstants;
import com.yodlee.health.errorsegment.gateway.util.KeystoreGateway;
import com.yodlee.iae.health.gateway.exception.AuthorizationException;
import com.yodlee.iae.health.gateway.util.SSLSecurityProtocol;
import com.yodlee.iae.health.gateway.util.SplunkConstants;

/**
 * 
 * This class make connection with Splunk
 * 
 * @author akumar23 & Skaipa
 *
 */

@Named
public class SplunkConnectImpl implements ISplunkConnect {

	private static String username;
	private static String password;
	private static String splunkSession = null;
	static SSLSocketFactory delegate;
	static SSLSecurityProtocol sslSecurityProtocol1;

	protected static SSLSecurityProtocol sslSecurityProtocol = SSLSecurityProtocol.TLSv1_2;
	private static SSLSocketFactory sslSocketFactory = createSSLFactory();

	@Inject
	private KeystoreGateway keystoreGateway;

	@Value("${splunk-username}")
	private void setUsername(String username) {
		this.username = username;
		System.out.println("Username:" + username);
	}

	@Value("${splunk-password}")
	private void setPassword(String encryptedPassword) {
		//String decryptedPassword = keystoreGateway.getAESDecryptedValue(encryptedPassword);
		this.password = encryptedPassword;

	}

	@Override
	public String executeSplunkServices(String queryString) throws Exception {

		try {
			this.login();
			String finalResults = "";

			String endpoint = GatewayConstants.LOG24;

			String sid = getSid(endpoint, false, queryString);
			if (sid == null) {
				return null;
			}
			int offset = 50000;
			for (int i = 0;; i++) {
				String modified_URL = SplunkConstants.POST_SID_URL + offset * (i) + "&count=50000";
				String url = SplunkConstants.REST_URL + SplunkConstants.PRE_SID_URL + sid + modified_URL;

				Map<String, String> params = new HashMap<String, String>();
				params.put(SplunkConstants.AUTHORIZATION_KEYWORD, SplunkConstants.SPLUNK_KEYWORD + " " + splunkSession);
				String response = get(url, params);
				JSONObject json = new JSONObject(response);
				JSONArray jsonArray = (JSONArray) json.get("results");
				String result = jsonArray.toString();

				result = result.substring(result.indexOf("[") + 1, result.lastIndexOf("]"));
				finalResults += result + ",";
				if (jsonArray.length() < 50000)
					break;
			}
			finalResults = "[" + finalResults.subSequence(0, finalResults.length() - 1) + "]";
			return finalResults;
		} catch (Exception e) {
			System.out.println("Exception :" + e);
		}
		return "";

	}

	/**
	 * Authenticates the user in Splunk.
	 * 
	 * @throws NullPointerException
	 * @throws Exception
	 */
	private void login() throws AuthorizationException {
		String url = GatewayConstants.REST_URL + "services/auth/login";
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.add(GatewayConstants.USERNAME_CONSTANT, username);
		params.add(GatewayConstants.PASSWORD_CONSTANT, password);
		String response = post(url, params);
		if (response != null) {
			splunkSession = response
					.substring(response.indexOf("<sessionKey>") + 12, response.lastIndexOf("</sessionKey>")).trim();
		}
	}

	/**
	 * Executes the Splunk rest URL with POST request method and returns the
	 * response body.
	 * 
	 * @param url
	 *            Splunk URL to be executed
	 * @param params
	 *            Parameters to be used in the request
	 * @return Response body
	 */
	private String post(String url, MultiValueMap<String, String> params) {
		HttpsURLConnection.setDefaultHostnameVerifier(HOSTNAME_VERIFIER);
		HttpsURLConnection.setDefaultSSLSocketFactory(sslSocketFactory);
		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		requestHeaders.add(GatewayConstants.AUTHORIZATION_KEYWORD,
				GatewayConstants.SPLUNK_KEYWORD + " " + splunkSession);
		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params,
				requestHeaders);
		ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);
		if (null == response) {
			return null;
		} else {
			return response.getBody();
		}

	}

	/**
	 * Executes the Splunk rest URL with GET request method and returns the response
	 * body.
	 * 
	 * @param url
	 *            Splunk URL to be executed
	 * @param params
	 *            Parameters to be used in the request
	 * @return Response body
	 */
	private static String get(String url, Map<String, String> params) {
		HttpsURLConnection.setDefaultHostnameVerifier(HOSTNAME_VERIFIER);
		HttpsURLConnection.setDefaultSSLSocketFactory(sslSocketFactory);
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		requestHeaders.add(GatewayConstants.AUTHORIZATION_KEYWORD,
				GatewayConstants.SPLUNK_KEYWORD + " " + splunkSession);
		MultiValueMap<String, String> params1 = new LinkedMultiValueMap<String, String>();
		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(params1,
				requestHeaders);
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
		return response.getBody();
	}

	/**
	 * 
	 * @param endpoint
	 * @param reload
	 * @param queryString
	 * @return
	 */
	private String getSid(String endpoint, boolean reload, String queryString) throws AuthorizationException {
		String sid = null;
		try {
			MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
			String url = GatewayConstants.REST_URL + GatewayConstants.SAVED_SEARCH_URL + endpoint
					+ GatewayConstants.DISPATCH_URL;
			if (queryString != null) {
				url = GatewayConstants.REST_URL + GatewayConstants.SEARCH_URL;
				params.add(GatewayConstants.SEARCH_KEYWORD, queryString);
			}
			String response = post(url, params);
			if (response == null) {
				return null;
			}
			if (queryString != null) {
				sid = response.substring(response.indexOf(":") + 2, response.lastIndexOf("\""));
			} else
				sid = response.substring(response.indexOf("<sid>") + 5, response.lastIndexOf("</sid>")).trim();
			String jobstatus = getJobstatus(sid, 0);

		} catch (Exception e) {
			if (e.getMessage().contains("401 (Unauthorized)")) {
				return null;
			}

		}
		return sid;
	}

	private String getJobstatus(String sid, int count) throws Exception {
		String response = get(GatewayConstants.REST_URL + GatewayConstants.PRE_SID_URL + sid, null);
		String done = response.substring(response.indexOf("<s:key name=\"isDone\">") + 21,
				response.indexOf("<s:key name=\"isDone\">") + 22);
		String status = "";
		if (done.equals("1")) {
			status = response.substring(response.indexOf("<s:key name=\"isFailed\">") + 23,
					response.indexOf("<s:key name=\"isFailed\">") + 24);
			return status;
		} else {
			if (count > 40) {
				return null;
			}
			try {
				Thread.sleep(10000);
			} catch (Exception e) {
			}
			status = getJobstatus(sid, ++count);
		}
		return status;
	}

	public static SSLSocketFactory createSSLFactory() {
		TrustManager[] trustAll = new TrustManager[] { new X509TrustManager() {
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(X509Certificate[] certs, String authType) {
			}

			public void checkServerTrusted(X509Certificate[] certs, String authType) {
			}
		} };
		try {
			SSLContext context;
			switch (sslSecurityProtocol) {
			case TLSv1_2:
			case TLSv1_1:
			case TLSv1:
				context = SSLContext.getInstance("TLS");
				break;
			default:
				context = SSLContext.getInstance("SSL");
			}

			context.init(null, trustAll, new java.security.SecureRandom());
			return new SplunkHttpsSocketFactory(context.getSocketFactory(), sslSecurityProtocol);
		} catch (Exception e) {
			throw new RuntimeException("Error setting up SSL socket factory: " + e, e);
		}
	}

	private static final HostnameVerifier HOSTNAME_VERIFIER = new HostnameVerifier() {
		public boolean verify(String s, SSLSession sslSession) {
			return true;
		}
	};

	private static final class SplunkHttpsSocketFactory extends SSLSocketFactory {
		private final SSLSocketFactory delegate;
		private SSLSecurityProtocol sslSecurityProtocol;

		private SplunkHttpsSocketFactory(SSLSocketFactory delegate) {
			this.delegate = delegate;
		}

		private SplunkHttpsSocketFactory(SSLSocketFactory delegate, SSLSecurityProtocol securityProtocol) {
			this.delegate = delegate;
			this.sslSecurityProtocol = securityProtocol;
		}

		private Socket configure(Socket socket) {
			if (socket instanceof SSLSocket) {
				((SSLSocket) socket).setEnabledProtocols(new String[] { sslSecurityProtocol.toString() });
			}
			return socket;
		}

		@Override
		public String[] getDefaultCipherSuites() {
			return delegate.getDefaultCipherSuites();
		}

		@Override
		public String[] getSupportedCipherSuites() {
			return delegate.getSupportedCipherSuites();
		}

		@Override
		public Socket createSocket(Socket socket, String s, int i, boolean b) throws IOException {
			return configure(delegate.createSocket(socket, s, i, b));
		}

		@Override
		public Socket createSocket() throws IOException {
			return configure(delegate.createSocket());
		}

		@Override
		public Socket createSocket(String s, int i) throws IOException, UnknownHostException {
			return configure(delegate.createSocket(s, i));
		}

		@Override
		public Socket createSocket(String s, int i, InetAddress inetAddress, int i1)
				throws IOException, UnknownHostException {
			return configure(delegate.createSocket(s, i, inetAddress, i1));
		}

		@Override
		public Socket createSocket(InetAddress inetAddress, int i) throws IOException {
			return configure(delegate.createSocket(inetAddress, i));
		}

		@Override
		public Socket createSocket(InetAddress inetAddress, int i, InetAddress inetAddress1, int i1)
				throws IOException {
			return configure(delegate.createSocket(inetAddress, i, inetAddress1, i1));
		}
	}

}
