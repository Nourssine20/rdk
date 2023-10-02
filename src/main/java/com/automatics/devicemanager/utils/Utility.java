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

import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.automatics.devicemanager.constants.Constants;
import com.automatics.devicemanager.dataobjects.DeviceDetailsDTO;
import com.automatics.devicemanager.enums.HealthStatus;
import com.automatics.devicemanager.model.Devices;
import com.automatics.devicemanager.objects.ExecutionDetailsResultsRequest;
import com.automatics.devicemanager.objects.ExecutionDeviceDetailsResponse;
import com.automatics.devicemanager.utils.AutomaticsPropertyUtility;
import com.automatics.devicemanager.utils.CommonMethods;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class Utility {
    private static final Logger LOGGER = LoggerFactory.getLogger(Utility.class);

    private String restServiceUrl = null;

    @Autowired
    public Utility() {
	AutomaticsPropertyUtility.loadProperties();
	restServiceUrl = AutomaticsPropertyUtility.getProperty("DEVICE_MANAGER_BASE_URL");
    }

    /**
     * Update execution results to the Device manager
     * 
     * @param executionResultDTOMap
     */
    public void updateExecutionDetailsStatus(ExecutionDetailsResultsRequest executionResult) {

	Response response = null;
	String url = CommonMethods.getNormalizedUrl(
		restServiceUrl + Constants.PATH_SEPERATOR + "healthCheck/updateExecutionDetailsStatus");

	ResteasyClient client = new ResteasyClientBuilder().build();

	ResteasyWebTarget webTarget = client.target(url);

	ObjectMapper objectMapper = new ObjectMapper();
	String obj;
	try {
	    obj = objectMapper.writeValueAsString(executionResult);
	    LOGGER.info("ExecutionResults : " + obj);
	    response = webTarget.request(MediaType.APPLICATION_JSON)
		    .post(Entity.entity(obj, MediaType.APPLICATION_JSON));
	    if (response.getStatus() == 200) {
		LOGGER.info("Execution Details updated successfully " + executionResult);
	    } else {
		LOGGER.info("Failed in Updating execution Details");
	    }

	} catch (Exception ex) {
	    LOGGER.error("Exception occured in updateExecutionDetailsStatus :: " + ex.getMessage());
	}
    }

    /**
     * Update the Execution details while execution in progress
     * 
     * @param exedeviceid
     */
    public void updateInProgressExecutionDetailsStatus(int exedeviceid) {
	LOGGER.info("Inside updateInProgressExecutionDetailsStatus :: ");
	Response response = null;

	String url = CommonMethods.getNormalizedUrl(restServiceUrl + Constants.PATH_SEPERATOR
		+ "healthCheck/updateInProgressExecutionDeviceDetails?executionDeviceDetailsId=" + exedeviceid);

	ResteasyClient client = new ResteasyClientBuilder().build();

	ResteasyWebTarget webTarget = client.target(url);

	try {
	    response = webTarget.request().get();
	    if (response.getStatus() == 200) {
		LOGGER.info("Execution Details updated successfully " + exedeviceid);
	    } else {
		LOGGER.info("Failed in Updating execution Details");
	    }
	} catch (Exception e) {
	    LOGGER.error("Exception occured in updateInProgressExecutionDetailsStatus :: " + e.getMessage());
	}
	LOGGER.info("Completed updateInProgressExecutionDetailsStatus :: ");
    }

    /**
     * Update the Number of devices passed and failed count to the Device manager
     * 
     * @param executionDetailId
     * @param passCount
     * @param failedCount
     * @param abortedCount
     */
    public void updateExecutionStatusCount(int executionDetailId, int passCount, int failedCount, int abortedCount) {
	LOGGER.info("Inside updateExecutionStatusCount :: ");
	Response response = null;

	String url = CommonMethods
		.getNormalizedUrl(restServiceUrl + Constants.PATH_SEPERATOR + "healthCheck/updateExecutionStatusCount");

	ResteasyClient client = new ResteasyClientBuilder().build();

	ResteasyWebTarget webTarget = client.target(url);

	JSONObject jsonReq = new JSONObject();
	LOGGER.info("PAss Count : " + passCount + " FailedCount : " + failedCount + " Aborted Count : " + abortedCount);
	String obj;
	try {
	    jsonReq.put("executionDetailId", executionDetailId);
	    jsonReq.put("passCount", passCount);
	    jsonReq.put("failedCount", failedCount);
	    jsonReq.put("abortedCount", abortedCount);
	    LOGGER.info("json req : " + jsonReq.toString());
	    obj = jsonReq.toString();
	    LOGGER.info("JSON request : " + obj);
	    response = webTarget.request(MediaType.APPLICATION_JSON)
		    .post(Entity.entity(obj, MediaType.APPLICATION_JSON));
	    if (response.getStatus() == 200) {
		LOGGER.info("Execution Status Count updated successfully ");
	    } else {
		LOGGER.info("Failed in Updating execution status count");
	    }

	} catch (Exception ex) {
	    LOGGER.error("Exception occured in updateExecutionStatusCount :: " + ex.getMessage());
	}
	LOGGER.info("Completed updateExecutionStatusCount :: ");
    }

    /**
     * Get the list of devices assigned for execution
     * 
     * @param execDetailsId
     * @return List<Devices>
     */
    public List<Devices> getDeviceDetailsByExecDeviceDtlsId(int execDetailsId) {
	LOGGER.info("Inside getDeviceDetailsByExecDeviceDtlsId :: execDeviceDetailsId: " + execDetailsId);
	Response response = null;
	JSONObject object = null;
	List<Devices> deviceList = new ArrayList<Devices>();

	String url = CommonMethods.getNormalizedUrl(restServiceUrl + Constants.PATH_SEPERATOR
		+ "healthCheck/getDeviceDetailsByExecDtlsId?executionDetailsId=" + execDetailsId);
	LOGGER.info("ServiceUrl : " + url);
	ResteasyClient client = new ResteasyClientBuilder().build();

	ResteasyWebTarget webTarget = client.target(url);

	try {
	    response = webTarget.request().get();
	    if (response.getStatus() == 200) {
		String str = response.readEntity(String.class);
		JSONArray deviceJsonarray = new JSONArray(str);
		LOGGER.info("Execution-Device-Details " + deviceJsonarray.toString());
		for (int i = 0; i < deviceJsonarray.length(); i++) {
		    object = deviceJsonarray.getJSONObject(i);
		    LOGGER.info("Execution-Device-Detail of  " + i + "  =" + object.toString());
		    DeviceDetailsDTO deviceDetailsDTO = populateDeviceDTO(object);
		    if (null != deviceDetailsDTO) {
			Devices devices = new Devices();
			devices.setDevice(deviceDetailsDTO);
			LOGGER.info("Execution Device Details DO - " + deviceDetailsDTO.toString() + " device :"
				+ devices.toString());
			deviceList.add(devices);
			LOGGER.info("Device list : " + deviceList.size());
		    }
		}
	    } else {
		LOGGER.error("Exception occured response recieved is " + response.getStatus());
		// throw new ApiException("No device DATA Found for this execution id :: ");
	    }
	} catch (Exception e) {
	    LOGGER.error("Exception occured in getDeviceDetailsByExecDeviceDtlsId :: " + e.getMessage());
	}
	LOGGER.info("Device list : " + deviceList.size());
	return deviceList;

    }

    /**
     * Populate device details object
     * 
     * @param object
     * @return DeviceDetailsDTO
     */
    public DeviceDetailsDTO populateDeviceDTO(JSONObject object) {
	LOGGER.info("Inside populateDeviceDTO ::");

	DeviceDetailsDTO deviceDetailsDTO = new DeviceDetailsDTO();
	try {

	    JSONArray devicefeatJsonarray = null;
	    ExecutionDeviceDetailsResponse deviceDto = new ExecutionDeviceDetailsResponse();
	    deviceDto.setDeviceDetailsId(object.getLong("deviceDetailsId"));
	    deviceDto.setIpAddress(object.getString("ipAddress"));
	    deviceDto.setDeviceCategoryName(object.getString("deviceCategoryName"));
	    if (!JSONObject.NULL.equals(object.get("gatewayMacAddress")) && !object.isNull("gatewayMacAddress")
		    && (object.get("gatewayMacAddress") != "null") && !(object.get("gatewayMacAddress").equals("null")))
		deviceDto.setGatewayMacAddress(object.getString("gatewayMacAddress"));

	    deviceDto.setMacAddress(object.getString("macAddress"));
	    deviceDto.setEcmMacAddress(object.getString("ecmMacAddress"));

	    if (!JSONObject.NULL.equals(object.get("ecmIpAddress")) && !object.isNull("ecmIpAddress")
		    && (object.get("ecmIpAddress") != "null") && !(object.get("ecmIpAddress").equals("null")))
		deviceDto.setEcmIpAddress(object.getString("ecmIpAddress"));

	    deviceDto.setHeadEndName(object.getString("headEndName"));
	    deviceDto.setDeviceGroupName(object.getString("deviceGroupName"));

	    if (!JSONObject.NULL.equals(object.get("healthStatus")) && !object.isNull("healthStatus")
		    && (object.get("healthStatus") != "null") && !(object.get("healthStatus").equals("null")))
		deviceDto.setHealthStatus(HealthStatus.fromValue(object.getString("healthStatus")));
	    else
		deviceDto.setHealthStatus(HealthStatus.BAD);

	    deviceDto.setAccountId(object.getString("activationAccountId"));
	    deviceDto.setPhoneNumber(object.getString("activationPhoneNumber"));
	    if (!JSONObject.NULL.equals(object.get("executionDeviceDetailsId"))
		    && !object.isNull("executionDeviceDetailsId") && (object.get("executionDeviceDetailsId") != "null")
		    && !(object.get("executionDeviceDetailsId").equals("null")))
		deviceDto.setExecutionDeviceDetailsId(object.getInt("executionDeviceDetailsId"));

	    if (!JSONObject.NULL.equals(object.get("executionDetailsId")) && !object.isNull("executionDetailsId")
		    && (object.get("executionDetailsId") != "null")
		    && !(object.get("executionDetailsId").equals("null")))
		deviceDto.setExecutionId((object.getInt("executionDetailsId")));

	    if (object.getString("isExcludedFromPool").equalsIgnoreCase("Y"))
		deviceDto.setExcludedFromPool(true);
	    else
		deviceDto.setExcludedFromPool(false);

	    if (!JSONObject.NULL.equals(object.get("featureDOList")) && !object.isNull("featureDOList")
		    && (object.get("featureDOList") != "null") && !(object.get("featureDOList").equals("null"))) {
		devicefeatJsonarray = (JSONArray) object.get("featureDOList");
		for (int i = 0; i < devicefeatJsonarray.length(); i++) {
		    deviceDto.getFeatures().add((String) devicefeatJsonarray.get(i));
		}
	    }

	    if (!JSONObject.NULL.equals(object.get("slotNumber")) && !object.isNull("slotNumber")
		    && (object.get("slotNumber") != "null") && !(object.get("slotNumber").equals("null")))
		deviceDto.setSlotNumber(object.getString("slotNumber"));

	    deviceDetailsDTO.setDeviceDTO(deviceDto);

	    LOGGER.info("deviceDetailsDO : " + deviceDetailsDTO.toString());
	} catch (Exception e) {
	    deviceDetailsDTO = null;
	    LOGGER.error("Exception occured in populateDeviceDTO::" + e.getMessage());

	}
	return deviceDetailsDTO;

    }

}
