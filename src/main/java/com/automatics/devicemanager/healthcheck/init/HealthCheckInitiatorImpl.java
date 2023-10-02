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
package com.automatics.devicemanager.healthcheck.init;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.automatics.devicemanager.dataobjects.Remarks;
import com.automatics.devicemanager.enums.HealthStatus;
import com.automatics.devicemanager.enums.Status;
import com.automatics.devicemanager.healthcheck.init.HealthCheckInitiator;
import com.automatics.devicemanager.healthcheck.registry.AdapterService;
import com.automatics.devicemanager.healthcheck.registry.ServiceRegistry;
import com.automatics.devicemanager.model.Devices;
import com.automatics.devicemanager.objects.ExecutionResultRequest;
import com.automatics.devicemanager.objects.HealthCheckResults;
import com.automatics.devicemanager.utils.DeviceUtility;

/**
 * @author TATA
 *
 */
@Component
public class HealthCheckInitiatorImpl implements HealthCheckInitiator {

    private static final Logger log = LoggerFactory.getLogger(HealthCheckInitiatorImpl.class);

    @Autowired
    private ServiceRegistry serviceRegistry;

    @Autowired
    private DeviceUtility deviceUtility;

    /**
     * Execute health check steps on the provided device details
     * 
     * @param Devices
     * @return ExecutionResultDTO
     */
    @Override
    public ExecutionResultRequest execute(Devices deviceinput) {

	boolean lockStatus = false;
	boolean healthcheckNot = false;
	AdapterService<?> k = null;
	HealthStatus jobstatus = null;
	Remarks temp = new Remarks();
	ExecutionResultRequest endresult = null;

	try {
	    if (deviceinput != null && deviceinput.getDevice().getDeviceDTO() != null) {
		if (deviceinput.getDevice().getDeviceDTO().isHealthCheckEnabled()) {
		    if (deviceinput.getDevice().getDeviceDTO().getHealthStatus() != (HealthStatus.SUSPENDED)) {
			if (deviceinput.getDevice().getDeviceDTO()
				.getHealthStatus() != (HealthStatus.NON_RECOVERABLE)) {
			    try {
				k = serviceRegistry.getService(getHealthCheckRegistryBean(deviceinput));
			    } catch (NoSuchBeanDefinitionException bean1) {
				healthcheckNot = true;
				log.info("Device specific health check not available");
			    }

			    if (!healthcheckNot) {
				lockStatus = deviceUtility.performDeviceLock(deviceinput.getDevice().getDeviceDTO());
				if (lockStatus) {
				    endresult = k.process(deviceinput);
				    endresult.setDeviceDTO(deviceinput.getDevice().getDeviceDTO());
				    jobstatus = endresult.getDeviceDTO().getHealthStatus();
				    log.info("EndResult After process completed : " + endresult.toString());
				    deviceUtility.release(deviceinput.getDevice().getDeviceDTO().getMacAddress());
				} else {
				    jobstatus = HealthStatus.CANCELLED;
				    temp.setStatus(Status.SKIPPED);
				    temp.getRemarksList().add("UNABLE_TO_LOCK_THE_DEVICE");
				}
			    } else {
				jobstatus = HealthStatus.SUSPENDED;
				temp.setStatus(Status.SKIPPED);
				temp.getRemarksList().add("HEALTH_CHECK_NOT_IMPLEMENTED");
			    }

			} else {
			    jobstatus = HealthStatus.NON_RECOVERABLE;
			    temp.setStatus(Status.SKIPPED);
			    temp.getRemarksList().add("NON_RECOVERABLE");
			}
		    } else {
			jobstatus = HealthStatus.CANCELLED;
			temp.setStatus(Status.SKIPPED);
			temp.getRemarksList().add("HEATH_CHECK_NOT_ENABLED");
		    }
		} else {
		    jobstatus = HealthStatus.SUSPENDED;
		    temp.setStatus(Status.SKIPPED);
		    temp.getRemarksList().add("SUSPENDED_DEVICE");
		}
	    } else {
		jobstatus = HealthStatus.CANCELLED;
		temp.setStatus(Status.SKIPPED);
		temp.getRemarksList().add("DEVICE_NOT_FOUND");
	    }
	} catch (Exception error) {
	    jobstatus = HealthStatus.CANCELLED;
	    temp.setStatus(Status.ERROR);
	    temp.getRemarksList().add("Error in HealthCheck " + error.getMessage());
	    log.error(error.getMessage(), "Error in HealthCheck " + error.getMessage());

	}
	if (endresult == null && jobstatus != null) {
	    Map<String, Remarks> remarkMap = new HashMap<String, Remarks>();
	    HealthStatus finalstatus = deviceinput.getDevice().getDeviceDTO().getHealthStatus();
	    log.info("Final Status : " + deviceinput.getDevice().getDeviceDTO().getHealthStatus().getValue());

	    endresult = new ExecutionResultRequest();
	    endresult.setExecutionDetailsId(deviceinput.getDevice().getDeviceDTO().getExecutionId());
	    endresult.setExecutionDeviceDetailsId(deviceinput.getDevice().getDeviceDTO().getExecutionDeviceDetailsId());
	    endresult.setJobStatus(jobstatus.getValue());
	    endresult.setStartTime(Timestamp.valueOf(LocalDateTime.now()));
	    endresult.setInitialStatus(finalstatus.getValue());
	    endresult.setEndTime(Timestamp.valueOf(LocalDateTime.now()));
	    remarkMap.put("Failed", temp);
	    List<HealthCheckResults> results = new ArrayList<>();
	    for (Map.Entry<String, Remarks> entry : remarkMap.entrySet()) {
		HealthCheckResults res = new HealthCheckResults();
		res.setMessage(entry.getKey());
		res.setRemarks(entry.getValue());
		results.add(res);
	    }
	    endresult.setHealthCheckResults(results);
	    endresult.setFinalStatus(finalstatus.getValue());
	    endresult.setDeviceDTO(deviceinput.getDevice().getDeviceDTO());

	}
	log.info("EndREsult : " + endresult.toString());
	return endresult;
    }

    /**
     * Return the health check bean configured for the provided device details
     * 
     * @param deviceinput
     * @return String
     */
    private String getHealthCheckRegistryBean(Devices deviceinput) {
	String regName = null;
	try {
	    regName = "ConfigGenericHealthCheckService";
	} catch (Exception e) {
	    log.error("Exception occurred in getHealthCheckRegistryBean() :" + e.getMessage());
	}
	return regName;
    }

}
