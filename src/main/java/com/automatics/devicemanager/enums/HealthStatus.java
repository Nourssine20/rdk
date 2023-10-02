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
package com.automatics.devicemanager.enums;
/**
 * Enum holds the various health stages
 * 
 * @author akshatha.m1
 *
 */
public enum HealthStatus {

    GOOD("GOOD"),
    COMPLETED("COMPLETED"),
    BAD("BAD"),
    NO_AV("NO AV"),
    NO_AUDIO("NO AUDIO"),
    STARTED_PROCESSING("STARTED_PROCESSING"),
    NON_RECOVERABLE("NON_RECOVERABLE"),
    NON_DEVICE_ISSUE("NON_DEVICE_ISSUE"),
    SCHEDULED("SCHEDULED"),
    CANCELLED("CANCELLED"),
    SUSPENDED("SUSPENDED"),
    ALREADY_IN_USE("ALREADY_IN_USE"),
    DEVICE_ALREADY_LOCKED("DEVICE ALREADY LOCKED / ALLOCATED"),
    DEVICE_LOCKING_FAILED("FAILED TO LOCK THE DEVICE"),
    DEVICE_UNLOCKING_FAILED("FAILED TO UNLOCK THE Device"),
    INVALID_DEVICE_LOCK_RESPONSE_CODE("INVALID LOCK RESPONSE CODE"),
    INVALID_DEVICE_INFO_RESPONSE("INVALID/UNPARSABLE RESPONSE FOR DEVICE INFO REQUEST"),
    NO_RESPONSE_FOR_DEVICE_LOCK_REQUEST("NO RESPONSE FOR DEVICE LOCK REQUEST"),
    INVALID_DEVICE_DETAILS("INVALID DEVICE DETAILS OBTAINED FROM DEVICE MANAGER"),
    INVALID_RESPONSE_CODE_FOR_DEVICE_STATUS("INVALID RESPONSE CODE FOR DEVICE STATUS REQUEST"),
    INVALID_DETAILS_FOR_DEVICE_STATUS("INVALID RESPONSE MESSAGE FOR DEVICE STATUS REQUEST"),
    FAILED_TO_GET_DEVICE_DETAILS("FAILED TO GET DEVICE DETAILS"),

    NOT_AVAILABLE("NOT_AVAILABLE");

    String statusValue;

    /**
     * Overriding Default constructor
     */
    private HealthStatus() {
	// overridden default constructor
    }

    /**
     * constructor that accepts statusValue
     * 
     * @param statusValue
     */
    private HealthStatus(String statusValue) {
	this.statusValue = statusValue;
    }

    /**
     * Get the value of the enumerator
     * 
     * @return statusValue
     */
    public String getValue() {
	return this.statusValue;
    }

    public static HealthStatus fromValue(String value) {
	for (HealthStatus availableValue : HealthStatus.values()) {
	    if (availableValue.getValue().equals(value)) {
		return availableValue;
	    }
	}
	throw new IllegalArgumentException(value);
    }
}
