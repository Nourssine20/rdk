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



public class ExecutionDetailsResultsRequest {
	
	/** Variable to hold the unique execution device details id*/
	Integer executionDeviceDetailsId;
	
	/** Variable to hold the execution results**/
	ExecutionResultRequest executionResults;
	
	public Integer getExecutionDeviceDetailsId() {
		return executionDeviceDetailsId;
	}

	public void setExecutionDeviceDetailsId(Integer executionDeviceDetailsId) {
		this.executionDeviceDetailsId = executionDeviceDetailsId;
	}

	public ExecutionResultRequest getExecutionResults() {
		return executionResults;
	}

	public void setexecutionResults(ExecutionResultRequest executionResults) {
		this.executionResults = executionResults;
	}
	
}
