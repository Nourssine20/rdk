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
package com.automatics.devicemanager.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.automatics.devicemanager.dataobjects.Remarks;
import com.automatics.devicemanager.deviceconnectionprovider.DeviceConnectionProvider;
import com.automatics.devicemanager.enums.Status;
import com.automatics.devicemanager.model.Devices;
import com.automatics.devicemanager.objects.ExecutionDeviceDetailsResponse;

/**
 * Utility class which includes common/generic functionality check.
 *
 */

@Service
public class CommonUtility {

    @Autowired
    Utility utility;

    @Autowired
    DeviceConnectionProvider deviceConnectionProvider;

    private static final Logger log = LoggerFactory.getLogger(CommonUtility.class);
    /**
     * Method to check the ssh connection to the device
     * @param devices
     * @return Remarks
     */
    public Remarks checkSShConnection(Devices devices) {
	Remarks sshConnectionRemark = new Remarks();
	String response = "TEST_CONNECTION";
	try {
	    ExecutionDeviceDetailsResponse device = devices.getDevice().getDeviceDTO();
	    if (null != device) {
		response = deviceConnectionProvider.execute(device.getIpAddress(), "echo test_connection");
		log.info("Response of accessibility check: {}", response);

		if (CommonMethods.isNotNull(response) && response.contains("test_connection")) {
		    sshConnectionRemark.setStatus(Status.SUCCESS);
		    sshConnectionRemark.getRemarksList().add(response);
		} else {
		    sshConnectionRemark.setStatus(Status.FAILED);
		    sshConnectionRemark.getRemarksList().add("NO SSH CONNECTION RESPONSE");
		}
	    } else {
		log.error("Device Details is not configured. So could not check device accessibility.");
		sshConnectionRemark.setStatus(Status.FAILED);
		sshConnectionRemark.getRemarksList().add("DEVICE DETAILS NOT FETCHED..");
	    }

	    return sshConnectionRemark;

	} catch (Exception ex) {
	    log.error("[SSH FAILED] : Exception occurred : " + ex.getMessage());
	    sshConnectionRemark.setStatus(Status.FAILED);
	    sshConnectionRemark.getRemarksList().add("SSH_CONNECTION_FAILURE" + " : " + ex.getMessage());

	}

	return sshConnectionRemark;

    }

}
