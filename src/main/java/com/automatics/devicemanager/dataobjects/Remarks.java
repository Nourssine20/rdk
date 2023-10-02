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

import java.util.ArrayList;
import java.util.List;

import com.automatics.devicemanager.enums.Status;
/**
 * Remarks Object
 * 
 * @author akshatha.m1
 *
 */
public class Remarks {

    private List<String> remarksList = new ArrayList<String>();
    private Status status = Status.FAILED;

    private String data;

    public List<String> getRemarksList() {
	return this.remarksList;
    }

    public void setRemarksList(List<String> remarksList) {
	this.remarksList = remarksList;
    }

    public Status getStatus() {
	return this.status;
    }

    public void setStatus(Status status) {
	this.status = status;
    }

    public String getData() {
	return this.data;
    }

    public void setData(String data) {
	this.data = data;
    }

}
