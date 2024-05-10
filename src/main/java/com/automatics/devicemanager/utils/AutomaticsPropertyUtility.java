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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.ws.rs.core.MediaType;

import org.apache.http.HttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.automatics.devicemanager.constants.Constants;

/**
 * Class to handle the property information for Automatics properties
 * 
 * @author akshatha.m1
 * 
 */
public final class AutomaticsPropertyUtility {

    static Properties properties = null;

    /** SLF4J logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(AutomaticsPropertyUtility.class);
    public static final SimpleDateFormat currentDate = new SimpleDateFormat("ddMMyy");

    public static synchronized void loadProperties() {
	if (properties == null) {
	    new AutomaticsPropertyUtility();
	}
    }

    /**
     * Constructor
     */
    private AutomaticsPropertyUtility() {

	URLConnection connection = null;

	if (properties == null) {

	    properties = new Properties();

	    // if the system property is set, then its given 1st priority, else the default value will be taken.
	    String propertyFileLoc = System.getProperty("automatics.properties.file");
	    
	    if (CommonMethods.isNotNull(propertyFileLoc)) {
		LOGGER.info("AutomaticsPropertyUtility: Reading automatics.properties file from " + propertyFileLoc);

		try {
		    /*
		     * Holds the base64 encoded passcode for accessing automatics properties file. The id and password
		     * is retrieved from the passcode which is used to secure the access of the file.
		     */
		    String propertiesToken = System.getProperty(Constants.AUTOMATICS_PROPERTIES_FILE_TOKEN);
		    propertiesToken = "Basic " + propertiesToken;
		    connection = new URL(propertyFileLoc).openConnection();
		    connection.setRequestProperty(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED);
		    connection.setRequestProperty(HttpHeaders.AUTHORIZATION, propertiesToken);
		    connection.setDoInput(true);
		    connection.setDoOutput(false);
		    InputStream inputStream = connection.getInputStream();
		    properties.load(inputStream);
		    FileOutputStream outputStream = new FileOutputStream("target/automatics.properties");
		    properties.store(outputStream, "Generated from URL: " + propertyFileLoc);
		    connection.getInputStream().close();
		    outputStream.close();

		} catch (FileNotFoundException e) {
		    LOGGER.error("AutomaticsPropertyUtility: File Not Found ->" + e.getMessage(), e);
		} catch (IOException e) {
		    LOGGER.error("AutomaticsPropertyUtility: IO error ->" + e.getMessage(), e);
		}
	    } else {
		LOGGER.error("Automatics props url not configured.");
	    }
	}
    }

    /**
     * Method to obtain the property value
     * 
     * @param propertyName
     * @return propertyValue
     */
    public static String getProperty(String propertyName) {

	String propertyValue = null;
	if (properties != null) {
	    propertyValue = properties.getProperty(propertyName);
	}
	if (CommonMethods.isNotNull(propertyValue)) {
	    propertyValue = propertyValue.trim();
	}

	return propertyValue;
    }

    /**
     * Method to obtain the property value. If not obtained ,the default value will be returned
     * 
     * @param propertyName
     * @param defaultValue
     * @return propertyValue
     */
    public static String getProperty(String propertyName, String defaultValue) {

	String propertyValue = null;

	if (properties != null) {
	    propertyValue = properties.getProperty(propertyName, defaultValue);
	}

	if (CommonMethods.isNotNull(propertyValue)) {
	    propertyValue = propertyValue.trim();
	}

	return propertyValue;
    }

    /**
     * Method to set the property
     * 
     * @param propertyName
     * @param propertyValue
     */
    public static void setProperty(String propertyName, String propertyValue) {

	if (CommonMethods.isNotNull(propertyName)) {

	    if (CommonMethods.isNotNull(propertyValue)) {
		propertyValue = propertyValue.trim();

		if (properties == null) {
		    loadProperties();
		}

		if (properties != null) {
		    properties.setProperty(propertyName, propertyValue);
		}
	    }
	}
    }

    /**
     * Utility method to get the property value with given prefix
     * 
     * @param propPrefix
     *            The required prefix
     * @return The list of properties
     */
    public static List<String> getPropsWithGivenPrefix(String propPrefix) throws Exception {
	List<String> propertyValues = new ArrayList<String>();
	if (properties != null) {
	    Set<Object> set = properties.keySet();
	    for (Object obj : set) {
		String str = obj.toString();
		if (str.startsWith(propPrefix)) {
		    propertyValues.add(str);
		}
	    }
	} else {
	    throw new Exception("Failed to load properties");
	}
	Collections.sort(propertyValues);
	return propertyValues;
    }

    public static void readStbProps() throws Exception {

	Properties properties = new Properties();

	// if the system property is set, then its given 1st priority, else the default value will be taken.
	String propertyFileLoc = System.getProperty("automatics.properties.file");

	if (CommonMethods.isNotNull(propertyFileLoc)) {
	    LOGGER.info("AutomaticsPropertyUtility: Reading automatics.properties file from " + propertyFileLoc);

	    try {
		URLConnection connection = new URL(propertyFileLoc).openConnection();
		String propertiesToken = System.getProperty(Constants.AUTOMATICS_PROPERTIES_FILE_TOKEN);
		propertiesToken = "Basic " + propertiesToken;
		connection.setRequestProperty(HttpHeaders.CONTENT_TYPE ,MediaType.APPLICATION_FORM_URLENCODED);
		connection.setRequestProperty(HttpHeaders.AUTHORIZATION, propertiesToken);
	    connection.setDoInput(true);
	    connection.setDoOutput(false);
		InputStream inputStream = connection.getInputStream();
		properties.load(inputStream);
		FileOutputStream outputStream = new FileOutputStream("target/automatics.properties");
		properties.store(outputStream, "Generated from URL: " + propertyFileLoc);
		connection.getInputStream().close();
		outputStream.close();
		
	    } catch (FileNotFoundException e) {
		LOGGER.error("AutomaticsPropertyUtility: File Not Found: {}. Cannot continue execution.",
			e.getMessage());
		throw new Exception("AutomaticsPropertyUtility: File Not Found");
	    } catch (IOException e) {
		LOGGER.error("AutomaticsPropertyUtility: IO error: {}", e.getMessage());
		throw new Exception("AutomaticsPropertyUtility: Exception reading file");
	    }
	} else {
	    LOGGER.error("Automatics props url not configured. Cannot continue execution.");
	    throw new Exception("Automatics props url not configured. Cannot continue execution.");
	}

    }
}
