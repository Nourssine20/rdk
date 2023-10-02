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

package com.automatics.devicemanager.constants;

/**
 * This class defines the constants used across the testcase.
 * 
 * @author akshatha.m1
 */
public class Constants {

    /** Ten seconds in millisecond representation. */
    public static final long TEN_SECONDS = 10000;

    public static final String PATH_SEPERATOR = "/";

    /** Custom port for SSH */
    public static final String SSH_CUSTOM_PORT = "ssh_custom_port";

    /** New line character. */
    public static final String NEW_LINE = "\n";

    /** One second in millisecond representation. */
    public static final long ONE_SECOND = 1000;

    public static String END_OF_SSH_CONNECTION_PRIVACY_MESSAGE = "law enforcement.";

    /** List of commands which requires long response wait time */
    public static final String[] ARRAY_COMMANDS_LONG_RESPONSE_TIME = { "find", "ps", "dmesg", "sshtoatom", "top",
	    "grep -ir" };

    /** Empty string. */
    public static final String EMPTY_STRING = "";

    public static final String EMPTY_LINE_REMOVER_REGEX = "(?m)^\\s+$";

    /** Thirty seconds in millisecond representation. */
    public static final int THIRTY_SECONDS_INT = 30000;

    /**
     * The string to check for colon with space.
     */
    public static final String COLON_WITH_SPACE = ": ";

    public static final String ANSI_REGEX = "(\\x9B|\\x1B\\[)[0-?]*[ -\\/]*[@-~]";

    /**
     * The string to identify the sudo password prompt message.
     */
    public static final String SUDO_PASS_WORD_PROMPT = "[sudo] password for ";

    /**
     * The string to identify the warning message for new connection.
     */
    public static final String WARNING_MESSAGE_FOR_NEW_CONNECTION = "Are you sure you want to continue connecting (yes/no)? ";

    /**
     * The string to identify the alternate warning message for new connection.
     */
    public static final String ALTERNATE_WARNING_MESSAGE_FOR_NEW_CONNECTION = "Do you want to continue connecting? (y/n)";

    /** Thirty seconds in millisecond representation. */
    public static final long THIRTY_SECONDS = 30 * ONE_SECOND;

    /** Two seconds in millisecond representation. */
    public static final long TWO_SECONDS = 2000;
    /** Constant to store the token of automatics properties file */
    public static final String AUTOMATICS_PROPERTIES_FILE_TOKEN = "properties.file.token";

    public static final String AUTOMATICS_PROPERTIES_LOGIN_KEY = "l";

    public static final String AUTOMATICS_PROPERTIES_PASSWORD_KEY = "p";

    /** Delimiter Equals. */
    public static final String DELIMITER_EQUALS = "=";

    /** Delimiter Ampersand. */
    public static final String DELIMITER_AMPERSAND = "&";
}
