/**
 * Copyright 2023 Comcast Cable Communications Management, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.automatics.devicemanager.utils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Base64;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.automatics.devicemanager.constants.Constants;

public class CommonMethods {

    /** SLF4J logger instance. */
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonMethods.class);

    /**
     * Method to check if the given value is null or not
     * 
     * @param value
     * @return
     */
    public static boolean isNotNull(String value) {

	boolean isNotNull = !isNull(value);

	return isNotNull;
    }

    /**
     * Method to check if the given value is null or not
     * 
     * @param value
     * @return
     */
    public static boolean isNull(String value) {

	return ((value == null) || (value.trim().length() == 0));
    }

    /**
     * Method to disable SSL
     */
    public static void disableSSL() {

	LOGGER.debug("Entering disableSSL()");

	SSLContext secureContext = null;

	
	class CertificateTrustManager implements X509TrustManager {

	    @Override
	    public void checkClientTrusted(X509Certificate[] certs, String authType) throws CertificateException {
	    }

	    @Override
	    public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException {
	    }

	    @Override
	    public X509Certificate[] getAcceptedIssuers() {
		return null;
	    }
	}
	
	TrustManager[] trustCerts = new TrustManager[] { new CertificateTrustManager() };

	
	try {
	    secureContext = SSLContext.getInstance("SSL");
	    secureContext.init(null, trustCerts, new java.security.SecureRandom());
	    HttpsURLConnection.setDefaultSSLSocketFactory(secureContext.getSocketFactory());
	} catch (Exception exception) {
	    LOGGER.error("Exception while disabling SSL for RACK" + exception.getMessage());
	    LOGGER.debug("Exception while disabling SSL for RACK", exception);
	}

	LOGGER.debug("Exiting disableSSL()");
    }

    /**
     * Method to get HTTP client with TLS support
     * 
     * @return {@code HttpClient}
     */
    public static HttpClient getTlsEnabledHttpClient() {

	HttpClient httpClient = null;
	LOGGER.debug("STARTING Method - getTlsEnabledHttpClient()");

	try {
	    SSLContext sslContext = SSLContext.getDefault();
	    SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext,
		    new String[] { "TLSv1", "TLSv1.1", "TLSv1.2" }, null, new NoopHostnameVerifier());
	    httpClient = HttpClients.custom().setSSLSocketFactory(sslSocketFactory).build();
	} catch (Exception e) {
	    LOGGER.error("Failed to get the http client. " + e);
	}

	LOGGER.debug("COMPLETED Method - getTlsEnabledHttpClient()");
	return httpClient;
    }

    /**
     * Normalizing the url.
     * 
     * @return string, Normalized Url
     */
    public static String getNormalizedUrl(String url) {
	if (isNotNull(url)) {
	    try {
		url = new URI(url).normalize().toString();
	    } catch (URISyntaxException e) {
		LOGGER.error("Invalid URL ", url);
	    }
	}
	return url;
    }

    public static String removeSecurityBannerFromResponse(String response) {
	if (isNotNull(response)) {
	    int indexEndOfPrivacyMessage = response.lastIndexOf(Constants.END_OF_SSH_CONNECTION_PRIVACY_MESSAGE);
	    if (indexEndOfPrivacyMessage > 0) {
		response = response
			.substring(indexEndOfPrivacyMessage + Constants.END_OF_SSH_CONNECTION_PRIVACY_MESSAGE.length());
	    }
	}
	return response;
    }

    public static void sleep(long milliseconds) {

	try {
	    Thread.sleep(milliseconds);
	} catch (InterruptedException e) {
	    LOGGER.error("Sleep interrupted " + e.getMessage());
	}
    }

    /*
     * Method to authenticate automatics core to automatics properties
     */
    public static void fetchData(URLConnection connection, String propertiesToken) throws IOException {

	if (CommonMethods.isNotNull(propertiesToken)) {
	    byte[] byteToken = Base64.getDecoder().decode(propertiesToken);
	    String decodedString = new String(byteToken, StandardCharsets.UTF_8);
	    if (decodedString.contains(":")) {
		String[] codeArray = decodedString.split(":");
		if (codeArray.length == 2) {
		    String loginid = codeArray[0];
		    String password = codeArray[1];
		    StringBuffer key = new StringBuffer(Constants.AUTOMATICS_PROPERTIES_LOGIN_KEY);
		    key.append(Constants.DELIMITER_EQUALS);
		    key.append(loginid);
		    key.append(Constants.DELIMITER_AMPERSAND);
		    key.append(Constants.AUTOMATICS_PROPERTIES_PASSWORD_KEY);
		    key.append(Constants.DELIMITER_EQUALS);
		    key.append(password);
		    DataOutputStream outtputStream = new DataOutputStream(connection.getOutputStream());
		    outtputStream.writeBytes(key.toString());
		    outtputStream.flush();
		    outtputStream.close();
		}
	    }
	}
    }

}
