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

package com.automatics.devicemanager.deviceconnectionprovider;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.automatics.devicemanager.connection.Connection;
import com.automatics.devicemanager.connection.SshConnection;
import com.automatics.devicemanager.constants.Constants;
import com.automatics.devicemanager.utils.CommonMethods;

import org.springframework.stereotype.Service;
/**
 * Class defined to provide support for device connection
 * 
 * @author akshatha.m1
 *
 */
@Service
public class DeviceConnectionProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceConnectionProvider.class);
    private static long defaultTimeout = 1000;
    private static final int SSH_CONNECTION_MAX_ATTEMPT = 4;

    private int sshConnectMaxAttempt = SSH_CONNECTION_MAX_ATTEMPT;

    public DeviceConnectionProvider() {

    }
    /**
     * Receive response from the device
     * 
     * @param conn
     * @param command
     * @param timeOutMilliSecs
     * @return String
     * @throws Exception
     */
    public static String sendReceive(SshConnection conn, String command, long timeOutMilliSecs) throws Exception {
	LOGGER.info("Executing command: " + command);
	String response = "";

	try {
	    conn.send(command, (int) (timeOutMilliSecs));
	    response = conn.getSettopResponse(timeOutMilliSecs);
	    response = CommonMethods.removeSecurityBannerFromResponse(response);
	    LOGGER.info("\n<===========================  RESPONSE =======================> \n" + response
		    + "\n<=============================================================>");

	} catch (Exception ex) {
	    throw new Exception("SSH CONNECTION FAILURE :" + ex);
	}

	return response;

    }

    /**
     * To get connection to device
     * 
     * @param device
     * @return SSH connection
     * @throws Exception
     */
    public Connection getConnection(String ipAddress) throws Exception {
	LOGGER.info("getConnection method invoked ");
	Connection conn = null;
	conn = createSshConnection(ipAddress);
	return conn;
    }

    /**
     * Execute commands in device
     * 
     * @param device
     * @param command
     * @return response string
     * @throws Exception
     */
    public String execute(String ipAddress, String command) throws Exception {

	String response = Constants.EMPTY_STRING;

	response = executeCommand(ipAddress, command, defaultTimeout);

	return response;
    }

    /**
     * Execute commands in device
     * 
     * @param device
     * @param commandList
     * @return response string
     * @throws Exception
     */
    public String execute(String ipAddress, List<String> commandList) throws Exception {
	StringBuilder response = new StringBuilder();
	SshConnection conn = null;

	LOGGER.info("About to create SSH connection to DeviceIP:" + ipAddress);
	try {
	    conn = createSshConnection(ipAddress);
	    for (String idx : commandList) {

		response.append(sendReceive(conn, idx, defaultTimeout)).append(Constants.NEW_LINE);
	    }

	} finally {
	    if (null != conn) {
		LOGGER.info("Closing SSH connection from DeviceIP:" + ipAddress);
		conn.disconnect();
	    }
	}

	LOGGER.info("Received response: " + response.toString());

	return response.toString();

    }

    /**
     * Execute commands using given device connection
     * 
     * @param device
     * @param deviceConnnection
     * @param command
     * @return response string
     * @throws Exception
     */
    public String execute(String ipAddress, Connection deviceConnnection, String command) throws Exception {
	String response = Constants.EMPTY_STRING;
	SshConnection conn = null;

	try {
	    conn = (SshConnection) conn;

	    response = sendReceive(conn, command, defaultTimeout);
	} finally {
	    if (null != conn) {
		LOGGER.info("Closing SSH connection from DeviceIP:" + ipAddress);
		conn.disconnect();
	    }
	}
	return response;
    }

    private String executeCommand(String ipAddress, String command, long timeOutMilliSecs) throws Exception {
	SshConnection conn = null;
	String response = Constants.EMPTY_STRING;

	LOGGER.info("About to create SSH connection to DeviceIP:" + ipAddress);
	try {
	    conn = createSshConnection(ipAddress);

	    response = sendReceive(conn, command, timeOutMilliSecs);
	} finally {
	    if (null != conn) {
		LOGGER.info("Closing SSH connection from DeviceIP:" + ipAddress);
		conn.disconnect();
	    }
	}
	LOGGER.info("Received response: " + response);
	return response;
    }

    /**
     * Creates ssh connection. Retry if connection failed to create
     * 
     * @param hostIp
     * @return SshConnection instance
     * @throws Exception
     */
    private SshConnection createSshConnection(String hostIp) throws Exception {
	SshConnection connection = null;
	connection = createSshConnection(hostIp, sshConnectMaxAttempt);

	return connection;
    }

    /**
     * Creates ssh connection. Retry if connection failed to create
     * 
     * @param hostIp
     * @param retryCount
     * @return SshConnection instance
     * @throws Exception
     */
    private SshConnection createSshConnection(String hostIp, int retryCount) throws Exception {
	SshConnection connection = null;
	String sshFailureMesaage = "";
	String trying = "Trying once more..";
	LOGGER.info("SSH Host IP : " + hostIp);

	for (int retryIndex = 1; retryIndex <= retryCount; retryIndex++) {
	    try {
		LOGGER.info("SSh connection attempet : " + retryIndex);
		connection = new SshConnection(hostIp);
	    } catch (Exception e) {

		// Trying once more

		if (retryIndex == retryCount) {
		    trying = "";
		}

		LOGGER.info("SSh connection attempet : " + retryIndex + " failed due to " + e.getMessage() + " for "
			+ hostIp + ". " + trying);
		sshFailureMesaage = e.getMessage();
		connection = null;
		if (retryIndex != retryCount) {
		    CommonMethods.sleep(Constants.TEN_SECONDS);
		}

	    }

	    if (null != connection) {
		break;
	    }
	}

	if (null == connection) {
	    throw new Exception("SSH CONNECTION FAILURE : " + sshFailureMesaage);
	}

	return connection;
    }

}
