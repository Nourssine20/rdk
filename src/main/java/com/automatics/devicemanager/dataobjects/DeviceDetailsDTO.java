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
package com.automatics.devicemanager.dataobjects;

import java.io.Serializable;

import com.automatics.devicemanager.objects.ExecutionDeviceDetailsResponse;
/**
 * Device Details Object 
 * 
 * @author akshatha.m1
 *
 */
public class DeviceDetailsDTO implements Serializable {

    private static final long serialVersionUID = 4461454861495125801L;

    private Integer scheduleId;

    private Integer jobManagerId;

    private String scheduleName;

    private Integer macExecutionId;

    private String deviceId;

    private String serviceAccountId;

    private String serialNumber;

    private ExecutionDeviceDetailsResponse deviceDTO;

    private String slotNumber;

    private String statusChangeRemarks;

    public Integer getScheduleId() {
	return this.scheduleId;
    }

    public void setScheduleId(Integer scheduleId) {
	this.scheduleId = scheduleId;
    }

    public Integer getJobManagerId() {
	return this.jobManagerId;
    }

    public void setJobManagerId(Integer jobManagerId) {
	this.jobManagerId = jobManagerId;
    }

    public String getScheduleName() {
	return this.scheduleName;
    }

    public void setScheduleName(String ScheduleName) {
	this.scheduleName = ScheduleName;
    }

    public String getDeviceId() {
	return this.deviceId;
    }

    public void setDeviceId(String deviceId) {
	this.deviceId = deviceId;
    }

    public Integer getMacExecutionId() {
	return this.macExecutionId;
    }

    public void setMacExecutionId(Integer macExecutionId) {
	this.macExecutionId = macExecutionId;
    }

    public String getServiceAccountId() {
	return this.serviceAccountId;
    }

    public void setServiceAccountId(String serviceAccountId) {
	this.serviceAccountId = serviceAccountId;
    }

    public String getSerialNumber() {
	return this.serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
	this.serialNumber = serialNumber;
    }

    public String getSlotNumber() {
	return this.slotNumber;
    }

    public void setSlotNumber(String slotNumber) {
	this.slotNumber = slotNumber;
    }

    public String getStatusChangeRemarks() {
	return this.statusChangeRemarks;
    }

    public void setStatusChangeRemarks(String statusChangeRemarks) {
	this.statusChangeRemarks = statusChangeRemarks;
    }

    public ExecutionDeviceDetailsResponse getDeviceDTO() {
	return this.deviceDTO;
    }

    public void setDeviceDTO(ExecutionDeviceDetailsResponse deviceDTO) {
	this.deviceDTO = deviceDTO;
    }

}