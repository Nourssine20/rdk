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
package com.automatics.devicemanager.healthcheck.services;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.automatics.devicemanager.dataobjects.Remarks;
import com.automatics.devicemanager.enums.HealthStatus;
import com.automatics.devicemanager.enums.Status;
import com.automatics.devicemanager.healthcheck.registry.AdapterService;
import com.automatics.devicemanager.model.Devices;
import com.automatics.devicemanager.objects.ExecutionResultRequest;
import com.automatics.devicemanager.objects.HealthCheckResults;
import com.automatics.devicemanager.utils.CommonUtility;

/**
 * 
 * @author akshatha.m1
 *
 */
@Service("ConfigGenericRdkbHealthCheck")
public class ConfigGenericHealthCheck implements AdapterService<Devices> {

    @Autowired
    CommonUtility commonUtils;

    private static final Logger log = LoggerFactory.getLogger(ConfigGenericHealthCheck.class);

    /**
     * Execute the basic health check steps defined and return the execution results
     * @param Devices
     * @return ExecutionResultDTO
     */
    @Override
    public ExecutionResultRequest process(Devices request) {
	log.info("ConfigGenericRdkbHealthCheck Process:: ");
	Map<String, Remarks> remarkMap = new HashMap<String, Remarks>();
	HealthStatus initialstatus = request.getDevice().getDeviceDTO().getHealthStatus();
	Remarks temp = new Remarks();
	ExecutionResultRequest execResult = new ExecutionResultRequest();
	try {

	    execResult.setJobStatus(HealthStatus.STARTED_PROCESSING.getValue());
	    execResult.setStartTime(Timestamp.valueOf(LocalDateTime.now()));
	    execResult.setExecutionDetailsId(request.getDevice().getDeviceDTO().getExecutionId());
	    execResult.setExecutionDeviceDetailsId(request.getDevice().getDeviceDTO().getExecutionDeviceDetailsId());
	    if (initialstatus != null)
		execResult.setInitialStatus(initialstatus.getValue());
	    else
		execResult.setInitialStatus(HealthStatus.NOT_AVAILABLE.getValue());

	    log.info("##########################################################################");
	    log.info("Validating SSH Connection");
	    temp = commonUtils.checkSShConnection(request);
	    remarkMap.put("SSH_CONNECTION", temp);
	    if (temp.getStatus() == Status.SUCCESS) {
		request.getDevice().getDeviceDTO().setHealthStatus(HealthStatus.GOOD);
	    } else {
		request.getDevice().getDeviceDTO().setHealthStatus(HealthStatus.BAD);
	    }

	} catch (Exception hlt) {
	    log.error(hlt.getMessage());
	    execResult.setJobStatus(HealthStatus.NON_DEVICE_ISSUE.getValue());
	    temp = new Remarks();
	    temp.setStatus(Status.ERROR);
	    temp.getRemarksList().add(hlt.getMessage());
	    remarkMap.put("Exception", temp);
	}
	
	List<HealthCheckResults> results = new ArrayList<>();
	    for (Map.Entry<String, Remarks> entry : remarkMap.entrySet()) {
		HealthCheckResults res = new HealthCheckResults();
		res.setMessage(entry.getKey());
		res.setRemarks(entry.getValue());
		results.add(res);
	    }
	execResult.setHealthCheckResults(results);

	log.info("Result " + execResult.toString());
	return execResult;

    }

}
