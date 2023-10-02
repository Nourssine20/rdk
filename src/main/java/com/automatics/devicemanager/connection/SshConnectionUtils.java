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
package com.automatics.devicemanager.connection;

import java.io.IOException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.http.HttpStatus;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.automatics.devicemanager.connection.auth.Credential;
import com.automatics.devicemanager.connection.auth.CredentialFactory;
import com.automatics.devicemanager.constants.Constants;
import com.automatics.devicemanager.objects.SshRequest;
import com.automatics.devicemanager.objects.SshResponse;
import com.automatics.devicemanager.utils.AutomaticsPropertyUtility;
import com.automatics.devicemanager.utils.CommonMethods;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcraft.jsch.JSchException;

public class SshConnectionUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(SshConnectionUtils.class);
    private static String GET_SSH_DETAILS = "/device/getSshDetailsByIp";

    public static String executeCommand(SshConnection sshConnection, String command, long respTimeout)
	    throws IOException, InterruptedException, JSchException {
	sshConnection.send(command + Constants.NEW_LINE, (int) (respTimeout));

	return sshConnection.getSettopResponse(respTimeout);
    }

    public static String executeCommand(SshConnection sshConnection, String command, long respTimeout,
	    boolean bufferResponse) throws IOException, InterruptedException, JSchException {
	String response = "";
	sshConnection.send(command + Constants.NEW_LINE, (int) (respTimeout));
	if (bufferResponse) {
	    sshConnection.bufferResponse();
	} else {
	    response = sshConnection.getSettopResponse(respTimeout);
	}

	return response;
    }

    public static String executeCommand(SshConnection sshConnection, String command, String expectStr, String[] options)
	    throws IOException, InterruptedException, JSchException {
	return sshConnection.send(command, expectStr, options);

    }

    /**
     * Method to check if the command requires more wait time to get response
     * 
     * @param commandToBeExecuted
     * @return
     */
    public static boolean isCommandNeedMoreWaitTime(String commandToBeExecuted) {
	boolean status = false;

	for (String command : Constants.ARRAY_COMMANDS_LONG_RESPONSE_TIME) {
	    if (commandToBeExecuted.contains(command)) {
		status = true;
	    }
	}
	LOGGER.debug("isCommandNeedMoreWaitTime - " + status);
	return status;
    }

    /**
     * Method to get SshConnection details either from Server.Xml file or devicemanager Api
     * 
     * @param ipAddress
     * @return
     * @throws Exception
     */
    public static Credential getSshDetails(String ipAddress) throws Exception {

	Credential credential = null;
	try {
	    credential = CredentialFactory.get().getServerCredentials(ipAddress);
	    LOGGER.debug("Failed to read config file:");
	} catch (Exception e) {
	    LOGGER.info("Failed to read config file:" + e.getMessage());
	    credential = getApiResponse(ipAddress);
	}
	if (credential == null) {
	    throw new Exception("Unable to fetch ssh connection details for " + ipAddress
		    + " in the serverconfig.xml file and device manager.");
	}
	return credential;

    }

    /**
     * This method will return Sshconnection details from DeviceManager API
     * 
     * @param ipAddress
     * @return
     */
    public static Credential getApiResponse(String ipAddress) {

	SshRequest request = new SshRequest();
	request.setIpAddress(ipAddress);
	Response response = null;
	Credential sshcredential = null;
	String baseUrl = AutomaticsPropertyUtility.getProperty("healthcheck.url");
	SshResponse sshresponse = new SshResponse();
	String url = CommonMethods.getNormalizedUrl(baseUrl + GET_SSH_DETAILS);
	ResteasyClient client = new ResteasyClientBuilder().build();
	ResteasyWebTarget webTarget = client.target(url);
	ObjectMapper objectMapper = new ObjectMapper();
	String obj;
	LOGGER.info("Fetching ssh connection details for device {}  Url Path: {}", request.getIpAddress(), url);
	try {
	    obj = objectMapper.writeValueAsString(request);
	    LOGGER.info("SSH request : " + obj);
	    response = webTarget.request(MediaType.APPLICATION_JSON)
		    .post(Entity.entity(obj, MediaType.APPLICATION_JSON));
	    if (null != response) {
		if (response.getStatus() == HttpStatus.SC_OK) {
		    String respData = response.readEntity(String.class);
		    LOGGER.info("Response: {}", respData);

		    if (CommonMethods.isNotNull(respData)) {
			ObjectMapper mapper = new ObjectMapper();
			try {
			    sshresponse = mapper.readValue(respData, SshResponse.class);
			    String user = sshresponse.getSshUserName();
			    String pass = sshresponse.getSshPassword();
			    String privateKeyLocation = sshresponse.getSshPrivateKeyLocation();
			    String defaultusername = user;
			    sshcredential = new Credential(ipAddress);
			    sshcredential.addLogin(user, pass);
			    sshcredential.setPrivateKeyLocation(privateKeyLocation);
			    sshcredential.setDefaultLogin(defaultusername);

			} catch (JsonProcessingException e1) {
			    LOGGER.error("Exception parsing json data for device {}", request.getIpAddress(), e1);
			} catch (IOException e1) {
			    LOGGER.error("Exception parsing json data for device {}", request.getIpAddress(), e1);
			}
		    }

		} else {
		    LOGGER.debug("Failed to get ssh connection details for {} : Status: {}", request.getIpAddress(),
			    response.getStatus());

		}
	    }
	} catch (Exception e1) {
	    LOGGER.error("Error occured while fetching ssh connection details from devicemanager {} ", e1.getMessage());
	}

	return sshcredential;

    }

    private static ResteasyClient getClient() {
	ResteasyClient client = new ResteasyClientBuilder().build();
	return client;

    }
}
