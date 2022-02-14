/**********************************************************************************************************************
 * krashpad                                                                                                           *
 *                                                                                                                    *
 * Copyright (c) 2020-2021 Mike Millson                                                                               *
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

import org.github.krashpad.domain.LogEvent;
import org.github.krashpad.util.jdk.JdkUtil;

/**
 * <p>
 * INTERNAL_STATISTICS
 * </p>
 * 
 * <p>
 * Metaspace information.
 * </p>
 * 
 * <h3>Example Logging</h3>
 * 
 * <pre>
 * Internal statistics:
 *
 * num_allocs_failed_limit: 0.
 * num_arena_births: 4.
 * num_arena_deaths: 0.
 * num_vsnodes_births: 2.
 * num_vsnodes_deaths: 0.
 * num_space_committed: 5.
 * num_space_uncommitted: 0.
 * num_chunks_returned_to_freelist: 0.
 * num_chunks_taken_from_freelist: 5.
 * num_chunk_merges: 0.
 * num_chunk_splits: 2.
 * num_chunks_enlarged: 0.
 * num_purges: 0.
 * num_inconsistent_stats: 0.
 * </pre>
 * 
 * @author <a href="mailto:mmillson@redhat.com">Mike Millson</a>
 * 
 */
public class StatisticsEvent implements LogEvent {

    /**
     * Regular expression defining the logging.
     */
    private static final String REGEX = "^(" + StatisticsEvent.REGEX_HEADER + "|num_)" + ".*$";

    /**
     * Regular expression for the header.
     */
    private static final String REGEX_HEADER = "Internal statistics:";

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
    public StatisticsEvent(String logEntry) {
        this.logEntry = logEntry;
    ***REMOVED***

    public String getLogEntry() {
        return logEntry;
    ***REMOVED***

    public String getName() {
        return JdkUtil.LogEventType.STATISTICS.toString();
    ***REMOVED***
***REMOVED***
