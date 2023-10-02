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

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.http.HttpStatus;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.automatics.devicemanager.objects.DeviceAllocationResponse;
import com.automatics.devicemanager.objects.DeviceRequest;
import com.automatics.devicemanager.objects.ExecutionDeviceDetailsResponse;
import com.automatics.devicemanager.objects.StatusResponse;
import com.automatics.devicemanager.objects.enums.DeviceAllocationStatus;
import com.automatics.devicemanager.objects.enums.StatusMessage;
import com.automatics.devicemanager.utils.AutomaticsPropertyUtility;
import com.automatics.devicemanager.utils.CommonMethods;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class DeviceUtility {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceUtility.class);

    private String restServiceUrl = null;

    private static String LOCK_DEVICE_PATH = "/deviceManagement/device/lock";
    private static String RELEASE_DEVICE_PATH = "/deviceManagement/device/release";
    private static String LOCK_STATUS_PATH = "/deviceManagement/device/allocationStatus";

    @Autowired
    public DeviceUtility() {
	AutomaticsPropertyUtility.loadProperties();
	restServiceUrl = AutomaticsPropertyUtility.getProperty("DEVICE_MANAGER_BASE_URL");
    }

    /**
     * Locks the device based on the Availability
     * 
     * @param device
     * @return boolean
     */
    public boolean performDeviceLock(ExecutionDeviceDetailsResponse device) {
	boolean isLockSuccess = false;
	LOGGER.info("INIT-{} Check if device already locked " + device.getMacAddress());
	DeviceRequest deviceRequest = new DeviceRequest();
	deviceRequest.setMac(device.getMacAddress());
	if (isLocked(deviceRequest)) {
	    LOGGER.info("INIT-{} Is device already locked :true" + device.getMacAddress());
	} else {
	    LOGGER.info("[INIT LOG] : Locking device " + device.getMacAddress());
	    isLockSuccess = lock(device.getMacAddress());
	}
	return isLockSuccess;
    }

    /**
     * Device manager Rest API is called to lock the device
     * 
     * @param mac
     * @return
     */
    public boolean lock(String mac) {
	boolean lockSuccess = false;
	Response response = null;
	DeviceRequest request = new DeviceRequest();
	request.setMac(mac);
	LOGGER.info("INIT-{} Locking device", mac);
	StatusResponse statusResponse = null;
	String url = CommonMethods.getNormalizedUrl(restServiceUrl + LOCK_DEVICE_PATH);
	LOGGER.info("Locking device {} Url Path: {}", request.getMac(), url);
	ResteasyClient client = new ResteasyClientBuilder().build();
	ResteasyWebTarget webTarget = client.target(url);
	ObjectMapper objectMapper = new ObjectMapper();
	String obj;
	try {
	    obj = objectMapper.writeValueAsString(request);
	    response = webTarget.request(MediaType.APPLICATION_JSON)
		    .post(Entity.entity(obj, MediaType.APPLICATION_JSON));
	    System.out.println(response);

	} catch (Exception ex) {
	    LOGGER.error("Exception while locking the device ", ex);
	}
	if (null != response) {
	    if (response.getStatus() == HttpStatus.SC_OK) {
		String respData = response.readEntity(String.class);
		LOGGER.info("Response: {}", respData);

		if (null != respData && !respData.isEmpty()) {
		    ObjectMapper mapper = new ObjectMapper();
		    try {
			statusResponse = mapper.readValue(respData, StatusResponse.class);
		    } catch (JsonProcessingException e) {
			LOGGER.error("Exception parsing json for device lock {}", request.getMac(), e);
		    }
		}
	    } else {
		LOGGER.info("Failed to lock device {} : Status: {}", request.getMac(), response.getStatus());
	    }

	}
	if (null != statusResponse) {
	    if (StatusMessage.SUCCESS == statusResponse.getStatus()) {
		lockSuccess = true;
		LOGGER.info("INIT-{} Lock success", mac);
	    } else {
		LOGGER.info("INIT-{} Lock failed - {}", mac, statusResponse.getErrorMsg());
	    }
	}
	return lockSuccess;
    }

    /**
     * Check the availability of the device
     * 
     * @param request
     * @return
     */

    public boolean isLocked(DeviceRequest request) {

	boolean isLocked = true;
	Response response = null;
	DeviceAllocationResponse allocResponse = null;
	String url = CommonMethods.getNormalizedUrl(restServiceUrl + LOCK_STATUS_PATH);

	ResteasyClient client = new ResteasyClientBuilder().build();
	ResteasyWebTarget webTarget = client.target(url);
	ObjectMapper objectMapper = new ObjectMapper();
	String obj;
	try {
	    obj = objectMapper.writeValueAsString(request);
	    response = webTarget.request(MediaType.APPLICATION_JSON)
		    .post(Entity.entity(obj, MediaType.APPLICATION_JSON));
	    System.out.println(response);

	} catch (Exception ex) {
	    LOGGER.error("Exception while getting allocation status of the device ", ex);
	}
	if (null != response) {
	    if (response.getStatus() == HttpStatus.SC_OK) {
		String respData = response.readEntity(String.class);
		LOGGER.info("Response: {}", respData);

		if (null != respData && !respData.isEmpty()) {
		    ObjectMapper mapper = new ObjectMapper();
		    try {
			allocResponse = mapper.readValue(respData, DeviceAllocationResponse.class);
			LOGGER.info("DeviceConfig allocation status", request.getMac(),
				allocResponse.getAllocationStatus());
		    } catch (JsonProcessingException e) {
			LOGGER.error("Exception parsing json for device allocation {}", request.getMac(), e);
		    } catch (Exception e) {
			LOGGER.error("Exception parsing json for device allocation {}", request.getMac(), e);
		    }
		}
	    } else {
		LOGGER.info("Failed to get device allocation status {} : Status: {}", request.getMac(),
			response.getStatus());
	    }

	}

	if (null != allocResponse) {
	    if (allocResponse.getAllocationStatus() == DeviceAllocationStatus.AVAILABLE) {
		isLocked = false;
	    } else {
		LOGGER.info("DeviceConfig allocation details:  Mac Address-{} Locked by-{} Duration start-{} end-{}",
			request.getMac(), allocResponse.getUserName(), allocResponse.getStart(),
			allocResponse.getEnd());
	    }
	}
	return isLocked;

    }

    /**
     * Release the locked devices
     * 
     * @param mac
     * @return boolean
     */
    public boolean release(String mac) {
	boolean releasedSuccess = false;
	Response response = null;
	DeviceRequest request = new DeviceRequest();
	request.setMac(mac);
	LOGGER.info("INIT-{} Releasing device", mac);
	StatusResponse statusResponse = null;
	String url = CommonMethods.getNormalizedUrl(restServiceUrl + RELEASE_DEVICE_PATH);
	LOGGER.info("Releasing device {} Url Path: {}", request.getMac(), url);
	ResteasyClient client = new ResteasyClientBuilder().build();
	ResteasyWebTarget webTarget = client.target(url);
	ObjectMapper objectMapper = new ObjectMapper();
	String obj;
	try {
	    obj = objectMapper.writeValueAsString(request);
	    response = webTarget.request(MediaType.APPLICATION_JSON)
		    .post(Entity.entity(obj, MediaType.APPLICATION_JSON));
	    System.out.println(response);

	} catch (Exception ex) {
	    LOGGER.error("Exception occured while releasing the device ", ex);
	}
	if (null != response) {
	    if (response.getStatus() == HttpStatus.SC_OK) {
		String respData = response.readEntity(String.class);
		LOGGER.info("Response: {}", respData);

		if (null != respData && !respData.isEmpty()) {
		    ObjectMapper mapper = new ObjectMapper();
		    try {
			statusResponse = mapper.readValue(respData, StatusResponse.class);
		    } catch (JsonProcessingException e) {
			LOGGER.error("Exception parsing json for device release {}", request.getMac(), e);
		    }
		}
	    } else {
		LOGGER.info("Failed to lock device {} : Status: {}", request.getMac(), response.getStatus());
	    }

	}
	if (null != statusResponse) {
	    if (StatusMessage.SUCCESS == statusResponse.getStatus()) {
		releasedSuccess = true;
		LOGGER.info("INIT-{} Release success", mac);
	    } else {
		LOGGER.info("INIT-{} Release failed - {}", mac, statusResponse.getErrorMsg());
	    }
	}
	return releasedSuccess;
    }
}
