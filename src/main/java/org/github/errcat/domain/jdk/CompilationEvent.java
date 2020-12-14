/**********************************************************************************************************************
 * errcat                                                                                                             *
 *                                                                                                                    *
 * Copyright (c) 2020 Mike Millson                                                                                    *
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
package org.github.errcat.domain.jdk;

import org.github.errcat.domain.LogEvent;
import org.github.errcat.util.jdk.JdkRegEx;
import org.github.errcat.util.jdk.JdkUtil;

/**
 * <p>
 * COMPILATION_EVENT
 * </p>
 * 
 * <p>
 * Compilation information when methods are compiled from Java byte code to native code.
 * </p>
 * 
 * <h3>Example Logging</h3>
 * 
 * <pre>
 * ***REMOVED***
 * Event: 6606.129 Thread 0x00007ff0ec201800 nmethod 21002 0x00007ff0e04fd110 code [0x00007ff0e04fd360, 0x00007ff0e04fe1d0]
 * Event: 6606.129 Thread 0x00007ff0ec201800 20997   !   4       org.eclipse.emf.ecore.xmi.impl.StringSegment::add (297 bytes)
 * </pre>
 * 
 * @author <a href="mailto:mmillson@redhat.com">Mike Millson</a>
 * 
 */
public class CompilationEvent implements LogEvent {

    /**
     * Regular expression defining the logging.
     */
    private static final String REGEX = "^(Compilation events|Event: " + JdkRegEx.TIMESTAMP + " Thread ("
            + JdkRegEx.ADDRESS64 + "|" + JdkRegEx.ADDRESS32 + ") (nmethod|\\d{1,8***REMOVED***)).+$";

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
    public CompilationEvent(String logEntry) {
        this.logEntry = logEntry;
    ***REMOVED***

    public String getLogEntry() {
        return logEntry;
    ***REMOVED***

    public String getName() {
        return JdkUtil.LogEventType.COMPILATION_EVENT.toString();
    ***REMOVED***

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
***REMOVED***
