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
 * VM_EVENT
 * </p>
 * 
 * <p>
 * VM events leading up to the crash.
 * </p>
 * 
 * <h3>Example Logging</h3>
 * 
 * <pre>
 * ***REMOVED***
 * Event: 6665.311 Executing VM operation: RevokeBias done
 * Event: 6665.311 Thread 0x00007fefe944f000 Thread exited: 0x00007fefe944f000
 * </pre>
 * 
 * @author <a href="mailto:mmillson@redhat.com">Mike Millson</a>
 * 
 */
public class VmEvent implements LogEvent {

    /**
     * Regular expression defining the logging.
     */
    private static final String REGEX = "^(Events|Event: " + JdkRegEx.TIMESTAMP
            + " (Concurrent|Executing( coalesced safepoint)? VM operation|Loaded shared library|loading class|Pause|"
            + "Protecting memory|Thread)).+$";

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
    public VmEvent(String logEntry) {
        this.logEntry = logEntry;
    ***REMOVED***

    public String getLogEntry() {
        return logEntry;
    ***REMOVED***

    public String getName() {
        return JdkUtil.LogEventType.VM_EVENT.toString();
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
