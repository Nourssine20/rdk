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

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.automatics.devicemanager.enums.HealthStatus;

/**
 * 
 * @author akshatha.m1
 *
 */
public class ExecutionDeviceDetailsResponse implements Serializable {

    private static final long serialVersionUID = -2083740817328615518L;

    private Long id;
    private long deviceDetailsId;
    private String deviceCategoryName;
    private String deviceModelName;
    private String ipAddress;
    private String ecmIpAddress;
    private String macAddress;
    private String ecmMacAddress;
    private String gatewayMacAddress;
    private HealthStatus healthStatus;
    private String headEndName;
    private String deviceGroupName;
    private String rackName;
    private String accountId;
    private String phoneNumber;
    private boolean isExcludedFromPool;
    private boolean isIpv6;
    private String slotNumber;
    private String remarks;
    private Set<String> features = new HashSet<String>();
    private int executionDeviceDetailsId;
    private int executionId;
    private boolean isHealthCheckEnabled = true;

    public HealthStatus getHealthStatus() {
	return healthStatus;
    }

    public void setHealthStatus(HealthStatus healthStatus) {
	this.healthStatus = healthStatus;
    }

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public long getDeviceDetailsId() {
	return deviceDetailsId;
    }

    public void setDeviceDetailsId(long deviceDetailsId) {
	this.deviceDetailsId = deviceDetailsId;
    }

    public String getDeviceCategoryName() {
	return deviceCategoryName;
    }

    public void setDeviceCategoryName(String deviceCategoryName) {
	this.deviceCategoryName = deviceCategoryName;
    }

    public String getDeviceModelName() {
	return deviceModelName;
    }

    public void setDeviceModeNamel(String deviceModelName) {
	this.deviceModelName = deviceModelName;
    }

    public String getIpAddress() {
	return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
	this.ipAddress = ipAddress;
    }

    public String getEcmIpAddress() {
	return ecmIpAddress;
    }

    public void setEcmIpAddress(String ecmIpAddress) {
	this.ecmIpAddress = ecmIpAddress;
    }

    public String getMacAddress() {
	return macAddress;
    }

    public void setMacAddress(String macAddress) {
	this.macAddress = macAddress;
    }

    public String getEcmMacAddress() {
	return ecmMacAddress;
    }

    public void setEcmMacAddress(String ecmMacAddress) {
	this.ecmMacAddress = ecmMacAddress;
    }

    public String getGatewayMacAddress() {
	return gatewayMacAddress;
    }

    public void setGatewayMacAddress(String gatewayMacAddress) {
	this.gatewayMacAddress = gatewayMacAddress;
    }

    public String getHeadEndName() {
	return headEndName;
    }

    public void setHeadEndName(String headEndName) {
	this.headEndName = headEndName;
    }

    public String getDeviceGroupName() {
	return deviceGroupName;
    }

    public void setDeviceGroupName(String deviceGroupName) {
	this.deviceGroupName = deviceGroupName;
    }

    public String getRackName() {
	return rackName;
    }

    public void setRackName(String rackName) {
	this.rackName = rackName;
    }

    public int getExecutionDeviceDetailsId() {
	return executionDeviceDetailsId;
    }

    public void setExecutionDeviceDetailsId(int executionDeviceDetailsId) {
	this.executionDeviceDetailsId = executionDeviceDetailsId;
    }

    public int getExecutionId() {
	return executionId;
    }

    public void setExecutionId(int executionId) {
	this.executionId = executionId;
    }

    public String getAccountId() {
	return accountId;
    }

    public void setAccountId(String accountId) {
	this.accountId = accountId;
    }

    public String getPhoneNumber() {
	return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
	this.phoneNumber = phoneNumber;
    }

    public boolean isExcludedFromPool() {
	return isExcludedFromPool;
    }

    public void setExcludedFromPool(boolean isExcludedFromPool) {
	this.isExcludedFromPool = isExcludedFromPool;
    }

    public String getSlotNumber() {
	return slotNumber;
    }

    public void setSlotNumber(String slotNumber) {
	this.slotNumber = slotNumber;
    }

    public String getRemarks() {
	return remarks;
    }

    public void setRemarks(String remarks) {
	this.remarks = remarks;
    }

    public Set<String> getFeatures() {
	return features;
    }

    public void setFeatures(Set<String> features) {
	this.features = features;
    }

    public boolean isIpv6() {
	return isIpv6;
    }

    public void setIpv6(boolean isIpv6) {
	this.isIpv6 = isIpv6;
    }

    public boolean isHealthCheckEnabled() {
	return isHealthCheckEnabled;
    }

    public void setHealthCheckEnabled(boolean isHealthCheckEnabled) {
	this.isHealthCheckEnabled = isHealthCheckEnabled;
    }

    @Override
    public String toString() {
	return "DeviceDTO [id=" + id + ", stbDetailsId=" + deviceDetailsId + ", deviceCategoryName="
		+ deviceCategoryName + ", deviceModelName=" + deviceModelName + ", ipAddress=" + ipAddress
		+ ", ecmIpAddress=" + ecmIpAddress + ", macAddress=" + macAddress + ", ecmMacAddress=" + ecmMacAddress
		+ ", gatewayMacAddress=" + gatewayMacAddress + ", healthStatus=" + healthStatus + ", headEndName="
		+ headEndName + ", deviceGroupName=" + deviceGroupName + ", rackName=" + rackName + ", accountId="
		+ accountId + ", phoneNumber=" + phoneNumber + ", isExcludedFromPool=" + isExcludedFromPool
		+ ", isIpv6=" + isIpv6 + ", slotNumber=" + slotNumber + ", remarks=" + remarks
		+ ", executionDeviceDetailsId=" + executionDeviceDetailsId + ", executionId=" + executionId
		+ ", isHealthCheckEnabled=" + isHealthCheckEnabled + "]";
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((accountId == null) ? 0 : accountId.hashCode());
	result = prime * result + ((deviceCategoryName == null) ? 0 : deviceCategoryName.hashCode());
	result = prime * result + ((deviceGroupName == null) ? 0 : deviceGroupName.hashCode());
	result = prime * result + ((deviceModelName == null) ? 0 : deviceModelName.hashCode());
	result = prime * result + ((ecmIpAddress == null) ? 0 : ecmIpAddress.hashCode());
	result = prime * result + ((ecmMacAddress == null) ? 0 : ecmMacAddress.hashCode());
	result = prime * result + executionId;
	result = prime * result + executionDeviceDetailsId;
	result = prime * result + ((features == null) ? 0 : features.hashCode());
	result = prime * result + ((gatewayMacAddress == null) ? 0 : gatewayMacAddress.hashCode());
	result = prime * result + ((headEndName == null) ? 0 : headEndName.hashCode());
	result = prime * result + ((healthStatus == null) ? 0 : healthStatus.hashCode());
	result = prime * result + ((id == null) ? 0 : id.hashCode());
	result = prime * result + ((ipAddress == null) ? 0 : ipAddress.hashCode());
	result = prime * result + (isExcludedFromPool ? 1231 : 1237);
	result = prime * result + (isHealthCheckEnabled ? 1231 : 1237);
	result = prime * result + (isIpv6 ? 1231 : 1237);
	result = prime * result + ((macAddress == null) ? 0 : macAddress.hashCode());
	result = prime * result + ((phoneNumber == null) ? 0 : phoneNumber.hashCode());
	result = prime * result + ((rackName == null) ? 0 : rackName.hashCode());
	result = prime * result + ((remarks == null) ? 0 : remarks.hashCode());
	result = prime * result + ((slotNumber == null) ? 0 : slotNumber.hashCode());
	result = prime * result + (int) (deviceDetailsId ^ (deviceDetailsId >>> 32));
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	ExecutionDeviceDetailsResponse other = (ExecutionDeviceDetailsResponse) obj;
	if (accountId == null) {
	    if (other.accountId != null)
		return false;
	} else if (!accountId.equals(other.accountId))
	    return false;
	if (deviceCategoryName == null) {
	    if (other.deviceCategoryName != null)
		return false;
	} else if (!deviceCategoryName.equals(other.deviceCategoryName))
	    return false;
	if (deviceGroupName == null) {
	    if (other.deviceGroupName != null)
		return false;
	} else if (!deviceGroupName.equals(other.deviceGroupName))
	    return false;
	if (deviceModelName == null) {
	    if (other.deviceModelName != null)
		return false;
	} else if (!deviceModelName.equals(other.deviceModelName))
	    return false;
	if (deviceModelName == null) {
	    if (other.deviceModelName != null)
		return false;
	} else if (!deviceModelName.equals(other.deviceModelName))
	    return false;
	if (ecmIpAddress == null) {
	    if (other.ecmIpAddress != null)
		return false;
	} else if (!ecmIpAddress.equals(other.ecmIpAddress))
	    return false;
	if (ecmMacAddress == null) {
	    if (other.ecmMacAddress != null)
		return false;
	} else if (!ecmMacAddress.equals(other.ecmMacAddress))
	    return false;
	if (executionId != other.executionId)
	    return false;
	if (executionDeviceDetailsId != other.executionDeviceDetailsId)
	    return false;
	if (features == null) {
	    if (other.features != null)
		return false;
	} else if (!features.equals(other.features))
	    return false;
	if (gatewayMacAddress == null) {
	    if (other.gatewayMacAddress != null)
		return false;
	} else if (!gatewayMacAddress.equals(other.gatewayMacAddress))
	    return false;
	if (headEndName == null) {
	    if (other.headEndName != null)
		return false;
	} else if (!headEndName.equals(other.headEndName))
	    return false;
	if (healthStatus != other.healthStatus)
	    return false;
	if (id == null) {
	    if (other.id != null)
		return false;
	} else if (!id.equals(other.id))
	    return false;
	if (ipAddress == null) {
	    if (other.ipAddress != null)
		return false;
	} else if (!ipAddress.equals(other.ipAddress))
	    return false;

	if (isExcludedFromPool != other.isExcludedFromPool)
	    return false;
	if (isHealthCheckEnabled != other.isHealthCheckEnabled)
	    return false;
	if (isIpv6 != other.isIpv6)
	    return false;
	if (macAddress == null) {
	    if (other.macAddress != null)
		return false;
	} else if (!macAddress.equals(other.macAddress))
	    return false;
	if (phoneNumber == null) {
	    if (other.phoneNumber != null)
		return false;
	} else if (!phoneNumber.equals(other.phoneNumber))
	    return false;
	if (rackName == null) {
	    if (other.rackName != null)
		return false;
	} else if (!rackName.equals(other.rackName))
	    return false;

	if (remarks == null) {
	    if (other.remarks != null)
		return false;
	} else if (!remarks.equals(other.remarks))
	    return false;

	if (slotNumber != other.slotNumber)
	    return false;
	if (deviceDetailsId != other.deviceDetailsId)
	    return false;
	return true;
    }

}
