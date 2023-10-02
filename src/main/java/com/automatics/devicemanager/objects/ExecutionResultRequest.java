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
package com.automatics.devicemanager.objects;

import java.sql.Timestamp;
import java.util.List;

/**
 * Holds the execution results object
 * 
 * @author akshatha.m1
 *
 */
public class ExecutionResultRequest {

    private Timestamp startTime;

    private String jobStatus;

    private String initialStatus;

    private String finalStatus;

    private Timestamp endTime;

    // private Map<String ,Remarks> remarkMap = new HashMap<String, Remarks>();

    private List<HealthCheckResults> healthCheckResults;

    private int executionDeviceDetailsId;

    private int executionDetailsId;

    private ExecutionDeviceDetailsResponse deviceDTO;

    public Timestamp getStartTime() {
	return this.startTime;
    }

    public void setStartTime(Timestamp startTime) {
	this.startTime = startTime;
    }

    public List<HealthCheckResults> getHealthCheckResults() {
	return this.healthCheckResults;
    }

    public void setHealthCheckResults(List<HealthCheckResults> healthCheckResults) {
	this.healthCheckResults = healthCheckResults;
    }

    public String getJobStatus() {
	return this.jobStatus;
    }

    public void setJobStatus(String jobStatus) {
	this.jobStatus = jobStatus;
    }

    public String getInitialStatus() {
	return this.initialStatus;
    }

    public void setInitialStatus(String initialStatus) {
	this.initialStatus = jobStatus;
    }

    public String getFinalStatus() {
	return this.finalStatus;
    }

    public void setFinalStatus(String finalStatus) {
	this.finalStatus = finalStatus;
    }

    public Timestamp getEndTime() {
	return this.endTime;
    }

    public void setEndTime(Timestamp endTime) {
	this.endTime = endTime;
    }

    public ExecutionDeviceDetailsResponse getDeviceDTO() {
	return this.deviceDTO;
    }

    public void setDeviceDTO(ExecutionDeviceDetailsResponse deviceDTO) {
	this.deviceDTO = deviceDTO;
    }

    public int getExecutionDeviceDetailsId() {
	return executionDeviceDetailsId;
    }

    public void setExecutionDeviceDetailsId(int executionDeviceDetailsId) {
	this.executionDeviceDetailsId = executionDeviceDetailsId;
    }

    public int getExecutionDetailsId() {
	return executionDetailsId;
    }

    public void setExecutionDetailsId(int executionDetailsId) {
	this.executionDetailsId = executionDetailsId;
    }

}
