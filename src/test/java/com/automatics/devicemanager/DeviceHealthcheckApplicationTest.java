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
package com.automatics.devicemanager;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.automatics.devicemanager.DeviceHealthcheckApplication;
import com.automatics.devicemanager.healthcheck.init.HealthCheckInitiatorImpl;
import com.automatics.devicemanager.healthcheck.registry.config.AsyncConfiguration;
import com.automatics.devicemanager.model.Devices;
import com.automatics.devicemanager.objects.ExecutionDetailsResultsRequest;
import com.automatics.devicemanager.objects.ExecutionResultRequest;
import com.automatics.devicemanager.utils.CommonMethods;
import com.automatics.devicemanager.utils.Utility;

@ExtendWith(SpringExtension.class)
@AutoConfigureWebMvc
@AutoConfigureMockMvc
@SpringBootTest(classes = DeviceHealthcheckApplication.class)
class DeviceHealthcheckApplicationTest {

    @Autowired
    AsyncConfiguration asyncConfiguration;

    @Autowired
    Utility utility;

    @Autowired
    HealthCheckInitiatorImpl healthCheckInitiator;

    private static final Logger log = LoggerFactory.getLogger(DeviceHealthcheckApplicationTest.class);

    public static AtomicInteger passCount = new AtomicInteger(0);

    public static String macAddress = "";

    public static AtomicInteger failedCount = new AtomicInteger(0);

    public static AtomicInteger abortedCount = new AtomicInteger(0);

    public static List<String> passDevices = new ArrayList<>();

    public static List<String> failedDevices = new ArrayList<>();

    public static List<String> abortedDevices = new ArrayList<>();

    @Test
    void contextLoads() {
    }

    /**
     * Initiate health check
     */
    @Test
    void initiateHealthCheck() {

	List<CompletableFuture<Map<Integer, ExecutionResultRequest>>> futureResultList = new ArrayList<CompletableFuture<Map<Integer, ExecutionResultRequest>>>();
	CompletableFuture<?>[] futureResultArray = null;
	String executionDetailsIdInput = null;
	List<Devices> devices = null;
	try {
	    executionDetailsIdInput = System.getProperty("executionId");
	    log.info("executionDetailsIdInput ::" + executionDetailsIdInput);
	    if (null != executionDetailsIdInput && !executionDetailsIdInput.isEmpty()) {
		String[] execStbDetailsIdArr = executionDetailsIdInput.split(",");
		for (String executionDetailsId : execStbDetailsIdArr) {
		    log.info("executionDetailsId ::" + executionDetailsId);
		    try {
			devices = utility.getDeviceDetailsByExecDeviceDtlsId(Integer.parseInt(executionDetailsId));
		    } catch (Exception e) {
			e.printStackTrace();
			log.error("initiateHealthCheck" + e.getMessage());
		    }
		    if (null != devices && devices.size() > 0) {

			for (Devices device : devices) {
			    log.info("device" + device);
			    CompletableFuture<Map<Integer, ExecutionResultRequest>> cfarm = getCompetableFutureResult(
				    device);
			    if (cfarm != null)
				futureResultList.add(cfarm);
			    Thread.sleep(5000);
			}
		    }

		    futureResultArray = futureResultList.toArray(new CompletableFuture[futureResultList.size()]);
		    CompletableFuture.allOf(futureResultArray).join();
		    log.info(
			    "\n----------------------------Final Results ------------------------ \n Passed DevicesList = "
				    + passDevices + "\n Failed Deviceslist = " + failedDevices
				    + "\n Aborted DevicesList : " + abortedDevices
				    + "\n---------------------------------------------------------------");
		    utility.updateExecutionStatusCount(Integer.parseInt(executionDetailsId), passCount.get(),
			    failedCount.get(), abortedCount.get());

		}
	    } else {
		log.debug("ExecutionStbDetailsId is null or empty ");
	    }
	} catch (Exception xcn) {
	    log.error("Exception initiateHealthCheck " + xcn.getMessage());
	}
    }

    /**
     * Trigger the health check on the selected devices and update the results to the Device manager
     * 
     * @param device
     * @return
     */
    public CompletableFuture<Map<Integer, ExecutionResultRequest>> getCompetableFutureResult(Devices device) {

	CompletableFuture<Map<Integer, ExecutionResultRequest>> cfar = null;
	try {
	    if (device != null) {
		macAddress = device.getDevice().getDeviceDTO().getMacAddress();
		ThreadPoolTaskExecutor executor = (ThreadPoolTaskExecutor) asyncConfiguration.taskExecutor();
		executor.setThreadFactory(new ThreadFactory() {

		    @Override
		    public Thread newThread(Runnable r) {
			Thread t = new Thread(r, macAddress);
			return t;
		    }
		});
		executor.initialize();
		cfar = CompletableFuture.supplyAsync((Supplier<Map<Integer, ExecutionResultRequest>>) () -> {
		    utility.updateInProgressExecutionDetailsStatus(
			    device.getDevice().getDeviceDTO().getExecutionDeviceDetailsId());
		    Map<Integer, ExecutionResultRequest> resultDTOMap = new HashMap<Integer, ExecutionResultRequest>();
		    ExecutionResultRequest executionResultDTO = healthCheckInitiator.execute(device);
		    log.info("Update execution status");
		    resultDTOMap.put(executionResultDTO.getExecutionDeviceDetailsId(), executionResultDTO);
		    ExecutionDetailsResultsRequest results = new ExecutionDetailsResultsRequest();
		    results.setExecutionDeviceDetailsId(executionResultDTO.getExecutionDeviceDetailsId());
		    results.setexecutionResults(executionResultDTO);
		    utility.updateExecutionDetailsStatus(results);
		    setStatusCount(executionResultDTO.getJobStatus(),
			    executionResultDTO.getDeviceDTO().getHealthStatus().toString(),
			    device.getDevice().getDeviceDTO().getMacAddress());
		    return resultDTOMap;
		}, executor);
	    }
	} catch (Exception cpm) {
	    log.error("Exception getCompetableFutureResult " + cpm.getMessage());
	}
	return cfar;
    }

    /**
     * Update the pass , fail devices count
     * 
     * @param jobStatus
     * @param finalStatus
     */
    public void setStatusCount(String jobStatus, String finalStatus, String mac) {
	try {
	    if (CommonMethods.isNotNull(jobStatus) && CommonMethods.isNotNull(finalStatus)) {
		if (jobStatus.equalsIgnoreCase("COMPLETED")) {
		    if (finalStatus.equalsIgnoreCase("GOOD")) {
			passCount.incrementAndGet();
			passDevices.add(mac);
		    } else if (finalStatus.equalsIgnoreCase("BAD")) {
			failedCount.incrementAndGet();
			failedDevices.add(mac);
		    }
		} else if (jobStatus.equalsIgnoreCase("SUSPENDED") || jobStatus.equalsIgnoreCase("SKIPPED")
			|| jobStatus.equalsIgnoreCase("CANCELLED")) {
		    abortedCount.incrementAndGet();
		    abortedDevices.add(mac);
		} else if (jobStatus.equalsIgnoreCase("FAILED") || jobStatus.equalsIgnoreCase("NON_DEVICE_ISSUE")) {
		    failedCount.incrementAndGet();
		    failedDevices.add(mac);
		}
	    } else if (CommonMethods.isNotNull(jobStatus)) {
		if (jobStatus.equalsIgnoreCase("COMPLETED")) {
		    passCount.incrementAndGet();
		    passDevices.add(mac);
		} else if (jobStatus.equalsIgnoreCase("SUSPENDED") || jobStatus.equalsIgnoreCase("SKIPPED")
			|| jobStatus.equalsIgnoreCase("CANCELLED") || jobStatus.equalsIgnoreCase("NON_RECOVERABLE")) {
		    abortedCount.incrementAndGet();
		    abortedDevices.add(mac);
		} else if (jobStatus.equalsIgnoreCase("FAILED") || jobStatus.equalsIgnoreCase("NON_DEVICE_ISSUE")) {
		    failedCount.incrementAndGet();
		    failedDevices.add(mac);
		}
	    }
	} catch (Exception e) {
	    log.error("Exception updateStatusCount " + e.getMessage());
	    e.printStackTrace();
	}

    }

}
