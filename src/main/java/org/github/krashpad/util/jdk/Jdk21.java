/**********************************************************************************************************************
 * krashpad                                                                                                           *
 *                                                                                                                    *
 * Copyright (c) 2020-2023 Mike Millson                                                                               *
 *                                                                                                                    * 
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License       * 
 * v. 2.0 which is available at https://www.eclipse.org/legal/epl-2.0, or the Apache License, Version 2.0 which is    *
 * available at https://www.apache.org/licenses/LICENSE-2.0.                                                          *
 *                                                                                                                    *
 * SPDX-License-Identifier: EPL-2.0 OR Apache-2.0                                                                     *
 *                                                                                                                    *
 * Contributors:                                                                                                      *
 *    Mike Millson - initial API and implementation                                                                   *
 *********************************************************************************************************************/
package org.github.krashpad.util.jdk;

import java.util.HashMap;

import org.github.krashpad.domain.jdk.Release;

/**
 * <p>
 * OpenJDK21 release information.
 * </p>
 * 
 * @author <a href="mailto:mmillson@redhat.com">Mike Millson</a>
 * 
 */
public class Jdk21 {

    /**
     * RHEL zip release information.
     */
    public static final HashMap<String, Release> RHEL_ZIPS;

    /**
     * RHEL8 rpm release information.
     */
    public static final HashMap<String, Release> RHEL8_X86_64_RPMS;

    /**
     * RHEL9 rpm release information.
     */
    public static final HashMap<String, Release> RHEL9_X86_64_RPMS;

    /**
     * Windows release information.
     */
    public static final HashMap<String, Release> WINDOWS_ZIPS;

    static {
        /*
         * Notes:
         * 
         * 1) Rpm key is the OpenJDK install directory.
         * 
         * 2) Zip key is build version.
         * 
         * 3) Jan 1 2000 00:00:00 means build date/time unknown/TBD.
         * 
         * 4) Time 00:00:00 means build date/time is estimate.
         */

        // RHEL8 amd64 OpenJDK21 rpm
        RHEL8_X86_64_RPMS = new HashMap<String, Release>();
        RHEL8_X86_64_RPMS.put("LATEST", new Release("2023-11-06T21:59:41Z", 3, "21.0.1+12-LTS"));
        RHEL8_X86_64_RPMS.put("java-21-openjdk-21.0.1.0.12-3.el8.x86_64",
                new Release("2023-11-06T21:59:41Z", 3, "21.0.1+12-LTS"));
        RHEL8_X86_64_RPMS.put("java-21-openjdk-21.0.1.0.12-2.el8.x86_64",
                new Release("2023-10-30T00:33:46Z", 2, "21.0.1+12-LTS"));
        RHEL8_X86_64_RPMS.put("java-21-openjdk-21.0.0.0.35-2.el8.x86_64",
                new Release("2023-08-27T04:16:29Z", 1, "21+35-LTS"));

        // RHEL9 amd64 OpenJDK21 rpm
        RHEL9_X86_64_RPMS = new HashMap<String, Release>();
        RHEL9_X86_64_RPMS.put("LATEST", new Release("2023-11-06T00:00:00Z", 3, "21.0.1+12-LTS"));
        RHEL9_X86_64_RPMS.put("java-21-openjdk-21.0.1.0.12-3.el9.x86_64",
                new Release("2023-11-06T00:00:00Z", 3, "21.0.1+12-LTS"));
        RHEL9_X86_64_RPMS.put("java-21-openjdk-21.0.1.0.12-2.el9.x86_64",
                new Release("2023-10-30T00:00:00Z", 2, "21.0.1+12-LTS"));
        RHEL9_X86_64_RPMS.put("java-21-openjdk-21.0.0.0.35-2.el9.x86_64",
                new Release("2023-08-27T00:00:00Z", 1, "21+35-LTS"));

        // RHEL amd64 OpenJDK21 zip
        RHEL_ZIPS = new HashMap<String, Release>();
        RHEL_ZIPS.put("LATEST", new Release("2023-10-30T00:33:46Z", 1, "21.0.1+12-LTS"));
        RHEL_ZIPS.put("21.0.1+12-LTS", new Release("2023-10-30T00:33:46Z", 1, "21.0.1+12-LTS"));

        // Windows amd64 OpenJDK21 zip
        WINDOWS_ZIPS = new HashMap<String, Release>();
        WINDOWS_ZIPS.put("LATEST", new Release("2023-10-30T00:00:00Z", 1, "21.0.1+12-LTS"));
        WINDOWS_ZIPS.put("21.0.1+12-LTS", new Release("2023-10-30T00:00:00Z", 1, "21.0.1+12-LTS"));
    }

    /**
     * Make default constructor private so the class cannot be instantiated.
     */
    private Jdk21() {

    }
}
