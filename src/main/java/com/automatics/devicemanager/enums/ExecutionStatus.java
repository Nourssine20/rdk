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
 * Enum to hold the various execution status codes
 * 
 * @author akshatha.m1
 *
 */
public enum ExecutionStatus {

    SCHEDULED("SCHEDULED"),
    IN_PROGRESS("IN PROGRESS"),
    PASSED("PASSED"),
    CANCELLED("CANCELLED"),
    FAILED("FAILED"),
    COMPLETED("COMPLETED"),
    ABORTED("ABORTED");

    String statusValue;

    private ExecutionStatus() {
	// overridden default constructor
    }

    /**
     * constructor that accepts statusValue
     * 
     * @param statusValue
     */
    private ExecutionStatus(String statusValue) {
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
}
