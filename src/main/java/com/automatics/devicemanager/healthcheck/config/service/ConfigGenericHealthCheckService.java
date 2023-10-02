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
package com.automatics.devicemanager.healthcheck.config.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.automatics.devicemanager.dataobjects.Remarks;
import com.automatics.devicemanager.enums.HealthStatus;
import com.automatics.devicemanager.enums.Status;
import com.automatics.devicemanager.healthcheck.registry.AdapterService;
import com.automatics.devicemanager.healthcheck.services.ConfigGenericHealthCheck;
import com.automatics.devicemanager.model.Devices;
import com.automatics.devicemanager.objects.ExecutionResultRequest;
import com.automatics.devicemanager.objects.HealthCheckResults;
/**
 * 
 * 
 * @author akshatha.m1
 *
 */
@Service("ConfigGenericHealthCheckService")
public class ConfigGenericHealthCheckService implements AdapterService<Devices> {
    @Autowired
    ConfigGenericHealthCheck configGenericHealthCheck;
    
    private static final Logger log = LoggerFactory.getLogger(ConfigGenericHealthCheckService.class);
    
    
    /**
     * Return the defined health check steps execution results
     * @return ExecutionResultDTO
     * @param Devices
     */
    public ExecutionResultRequest process(Devices inputDevice) {
	log.info("Inside ConfigGenericHealthCheckService process");
	Remarks remarks = null;
	List<HealthCheckResults> finalRemarkMap = new ArrayList<HealthCheckResults>();
	ExecutionResultRequest finalExecResult = new ExecutionResultRequest();
	try {
	    ExecutionResultRequest execResult = configGenericHealthCheck.process(inputDevice);
	    log.info("Health check step :: SSH_CONNECTION result :: " + execResult);
	    finalExecResult.setJobStatus(execResult.getJobStatus());
	    finalExecResult.setStartTime(execResult.getStartTime());
	    finalExecResult.setExecutionDetailsId(execResult.getExecutionDetailsId());
	    finalExecResult.setExecutionDeviceDetailsId(execResult.getExecutionDeviceDetailsId());
	    if (execResult.getHealthCheckResults() != null && execResult.getHealthCheckResults().size() > 0)
		finalRemarkMap.addAll(execResult.getHealthCheckResults());
	    finalExecResult.setJobStatus(HealthStatus.COMPLETED.getValue());
	} catch (Exception e) {
	    log.error("Exception occured in ConfigGenericRdkbHealthCheckService process() :" + e.getMessage());
	    finalExecResult.setJobStatus(HealthStatus.NON_DEVICE_ISSUE.getValue());
	    remarks = new Remarks();
	    remarks.setStatus(Status.ERROR);
	    remarks.getRemarksList().add(e.getMessage());
	    HealthCheckResults res = new HealthCheckResults();
	    res.setMessage("Exception");
	    res.setRemarks(remarks);
	    finalRemarkMap.add(res);
	}
	finalExecResult.setEndTime(Timestamp.valueOf(LocalDateTime.now()));
	finalExecResult.setHealthCheckResults(finalRemarkMap);

	log.info("Result " + finalExecResult.toString());
	return finalExecResult;
    }

}
