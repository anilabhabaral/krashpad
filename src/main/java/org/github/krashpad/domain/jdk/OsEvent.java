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
package org.github.krashpad.domain.jdk;

import java.util.regex.Pattern;

import org.github.krashpad.domain.LogEvent;
import org.github.krashpad.util.jdk.JdkUtil;

/**
 * <p>
 * OS
 * </p>
 * 
 * <p>
 * OS information.
 * </p>
 * 
 * <h2>Example Logging</h2>
 * 
 * <pre>
 * OS:                            Oracle Solaris 11.4 SPARC
 * </pre>
 * 
 * <pre>
 * OS:Red Hat Enterprise Linux Server release 7.7 (Maipo)
 * </pre>
 * 
 * <pre>
 * OS: Windows Server 2016 , 64 bit Build 14393 (10.0.14393.3630)
 * </pre>
 * 
 * <pre>
 * OS:
 * Red Hat Enterprise Linux release 8.5 (Ootpa)
 * </pre>
 * 
 * @author <a href="mailto:mmillson@redhat.com">Mike Millson</a>
 * 
 */
public class OsEvent implements LogEvent {

    public static final Pattern PATTERN = Pattern.compile(OsEvent.REGEX);

    /**
     * Regular expression defining the logging.
     */
    private static final String REGEX = "^(" + OsEvent.REGEX_HEADER
            + "|(CentOS|Oracle|Red Hat Enterprise) Linux.+|Windows|"
            + "[ ]{0,***REMOVED***(Assembled|Copyright|ID|NAME|PATCHLEVEL|(BUG_REPORT|HOME|SUPPORT)_URL|VERSION(_(ID|CODENAME))?|"
            + "***REMOVED*** Please check \\/etc\\/os-release|***REMOVED*** This file is deprecated|"
            + "\\[error occurred during error reporting \\(printing OS information\\))(.+))$";

    /**
     * Regular expression for the header.
     */
    private static final String REGEX_HEADER = "OS:((PRETTY_NAME=\")?(.+)[\"]{0,1***REMOVED***)?";

    /**
     * Determine if the logLine matches the logging pattern(s) for this event.
     * 
     * @param logLine
     *            The log line to test.
     * @return true if the log line matches the event pattern, false otherwise.
     */
    public static final boolean match(String logLine) {
        return logLine.matches(REGEX);
    ***REMOVED***

    /**
     * The log entry for the event.
     */
    private String logEntry;

    /**
     * Create event from log entry.
     * 
     * @param logEntry
     *            The log entry for the event.
     */
    public OsEvent(String logEntry) {
        this.logEntry = logEntry;
    ***REMOVED***

    public String getLogEntry() {
        return logEntry;
    ***REMOVED***

    public String getName() {
        return JdkUtil.LogEventType.OS.toString();
    ***REMOVED***

    /**
     * @return true if the log line is the header false otherwise.
     */
    public boolean isHeader() {
        return logEntry.matches(REGEX_HEADER);
    ***REMOVED***
***REMOVED***
