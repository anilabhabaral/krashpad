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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.github.krashpad.domain.HeaderEvent;
import org.github.krashpad.domain.LogEvent;
import org.github.krashpad.util.jdk.JdkUtil;

/**
 * <p>
 * TRANSPARENT_HUGEPAGE_ENABLED
 * </p>
 * 
 * <p>
 * Transparent hugepage enabled information.
 * </p>
 * 
 * <h2>Example Logging</h2>
 * 
 * <pre>
 * /sys/kernel/mm/transparent_hugepage/enabled:
 * [always] madvise never
 * </pre>
 * 
 * @author <a href="mailto:mmillson@redhat.com">Mike Millson</a>
 * 
 */
public class TransparentHugepageEnabled implements LogEvent, HeaderEvent {

    /**
     * Defined THP modes.
     */
    public enum MODE {
        ALWAYS, MADVISE, NEVER, UNKNOWN
    }

    /**
     * Regular expression for the header.
     */
    public static final String _REGEX_HEADER = "/sys/kernel/mm/transparent_hugepage/enabled:";

    /**
     * Regular expression defining the logging.
     */
    private static final String REGEX = "^(" + _REGEX_HEADER + "|(" + _REGEX_HEADER + " )?(\\[always\\] madvise never|"
            + "always \\[madvise\\] never|always madvise \\[never\\]))$";

    /**
     * Determine if the logLine matches the logging pattern(s) for this event.
     * 
     * @param logLine
     *            The log line to test.
     * @return true if the log line matches the event pattern, false otherwise.
     */
    public static final boolean match(String logLine) {
        return logLine.matches(REGEX);
    }

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
    public TransparentHugepageEnabled(String logEntry) {
        this.logEntry = logEntry;
    }

    public String getLogEntry() {
        return logEntry;
    }

    /**
     * @return THP mode.
     */
    public MODE getMode() {
        MODE mode = MODE.UNKNOWN;
        Pattern pattern = Pattern.compile(TransparentHugepageEnabled.REGEX);
        Matcher matcher = pattern.matcher(logEntry);
        if (matcher.find()) {
            if (matcher.group(3) != null) {
                if (matcher.group(3).matches("^\\[always\\] madvise never$")) {
                    mode = MODE.ALWAYS;
                } else if (matcher.group(3).matches("^always \\[madvise\\] never$")) {
                    mode = MODE.MADVISE;
                } else if (matcher.group(3).matches("^always madvise \\[never\\]$")) {
                    mode = MODE.NEVER;
                }
            }
        }
        return mode;
    }

    public String getName() {
        return JdkUtil.LogEventType.TRANSPARENT_HUGEPAGE_ENABLED.toString();
    }

    @Override
    public boolean isHeader() {
        boolean isHeader = false;
        if (this.logEntry != null) {
            isHeader = logEntry.matches(_REGEX_HEADER);
        }
        return isHeader;
    }

    /**
     * @return True if mode setting, false otherwise.
     */
    public boolean isMode() {
        boolean isMode = false;
        Pattern pattern = Pattern.compile(TransparentHugepageEnabled.REGEX);
        Matcher matcher = pattern.matcher(logEntry);
        if (matcher.find()) {
            if (matcher.group(3) != null) {
                isMode = true;
            }
        }
        return isMode;
    }
}
