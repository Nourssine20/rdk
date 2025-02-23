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
package com.automatics.devicemanager.connection;

/**
 * Enum for Command types
 * @author akshatha.m1
 *
 */
public enum ExecuteCommandType {

    REV_SSH_DEVICE_VERIFY,

    TRACE_INIT_COMMAND_GATEWAY,

    ADDLN_TRACE_INIT_COMMAND_GATEWAY,

    SNMP_CODE_DOWNLOAD,

    SNMP_COMMAND,

    XCONF_CONFIG_UPDATE,

    /**
     * Return execution response with banners. eg: when command is executed within device via ssh connection, ssh
     * banners should not be truncated from response
     **/

    COMMAND_RESPONSE_WITH_BANNER,

    REV_SSH

}
