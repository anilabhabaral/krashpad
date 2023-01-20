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

import org.github.krashpad.domain.LogEvent;
import org.github.krashpad.util.jdk.JdkRegEx;
import org.github.krashpad.util.jdk.JdkUtil;

/**
 * <p>
 * GC_HEAP_HISTORY
 * </p>
 * 
 * <p>
 * GC heap history information.
 * </p>
 * 
 * <h2>Example Logging</h2>
 * 
 * <pre>
 * GC Heap History (48 events):
 * Event: 1.905 GC heap before
 * {Heap before GC invocations=1 (full 0):
 *  PSYoungGen      total 153088K, used 116252K [0x00000000eab00000, 0x00000000f5580000, 0x0000000100000000)
 *   eden space 131584K, 88% used [0x00000000eab00000,0x00000000f1c87328,0x00000000f2b80000)
 *   from space 21504K, 0% used [0x00000000f4080000,0x00000000f4080000,0x00000000f5580000)
 *   to   space 21504K, 0% used [0x00000000f2b80000,0x00000000f2b80000,0x00000000f4080000)
 *  ParOldGen       total 349696K, used 0K [0x00000000c0000000, 0x00000000d5580000, 0x00000000eab00000)
 *   object space 349696K, 0% used [0x00000000c0000000,0x00000000c0000000,0x00000000d5580000)
 *  Metaspace       used 19510K, capacity 21116K, committed 21248K, reserved 1069056K
 *   class space    used 1971K, capacity 2479K, committed 2560K, reserved 1048576K
 * Event: 1.941 GC heap after
 * Heap after GC invocations=1 (full 0):
 *  PSYoungGen      total 153088K, used 21493K [0x00000000eab00000, 0x00000000fd600000, 0x0000000100000000)
 *   eden space 131584K, 0% used [0x00000000eab00000,0x00000000eab00000,0x00000000f2b80000)
 *   from space 21504K, 99% used [0x00000000f2b80000,0x00000000f407d4c0,0x00000000f4080000)
 *   to   space 21504K, 0% used [0x00000000fc100000,0x00000000fc100000,0x00000000fd600000)
 *  ParOldGen       total 349696K, used 6340K [0x00000000c0000000, 0x00000000d5580000, 0x00000000eab00000)
 *   object space 349696K, 1% used [0x00000000c0000000,0x00000000c06311f0,0x00000000d5580000)
 *  Metaspace       used 19510K, capacity 21116K, committed 21248K, reserved 1069056K
 *   class space    used 1971K, capacity 2479K, committed 2560K, reserved 1048576K
 * ***REMOVED***
 * </pre>
 * 
 * @author <a href="mailto:mmillson@redhat.com">Mike Millson</a>
 * 
 */
public class GcHeapHistoryEvent implements LogEvent {

    /**
     * Regular expression defining the logging.
     */
    private static final String REGEX = "^(" + GcHeapHistoryEvent.REGEX_HEADER + "|" + JdkRegEx.YOUNG_GEN + "|"
            + JdkRegEx.OLD_GEN + "|" + JdkRegEx.SHENANDOAH + "|" + JdkRegEx.G1 + "|" + JdkRegEx.METASPACE
            + "|[\\{]{0,1***REMOVED***Heap (after|before) GC.+|Event: " + JdkRegEx.TIMESTAMP + " GC heap (after|before)|\\***REMOVED***)$";
    /**
     * Regular expression for the header.
     */
    public static final String REGEX_HEADER = "GC Heap History \\(\\d{1,***REMOVED*** events\\):";

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
    public GcHeapHistoryEvent(String logEntry) {
        this.logEntry = logEntry;
    ***REMOVED***

    public String getLogEntry() {
        return logEntry;
    ***REMOVED***

    public String getName() {
        return JdkUtil.LogEventType.HEAP.toString();
    ***REMOVED***

    /**
     * @return true if the log line contains G1 heap information, false otherwise.
     */
    public boolean isG1() {
        return logEntry.matches(JdkRegEx.G1_SIZE);
    ***REMOVED***

    /**
     * @return true if the log line contains metaspace information, false otherwise.
     */
    public boolean isMetaspace() {
        return logEntry.matches(JdkRegEx.METASPACE_SIZE);
    ***REMOVED***

    /**
     * @return true if the log line contains old generation heap information, false otherwise.
     */
    public boolean isOldGen() {
        return logEntry.matches(JdkRegEx.OLD_GEN_SIZE);
    ***REMOVED***

    /**
     * @return true if the log line contains Shenandoah heap information, false otherwise.
     */
    public boolean isShenandoah() {
        return logEntry.matches(JdkRegEx.SHENANDOAH_SIZE);
    ***REMOVED***

    /**
     * @return true if the log line contains young generation heap information, false otherwise.
     */
    public boolean isYoungGen() {
        return logEntry.matches(JdkRegEx.YOUNG_GEN_SIZE);
    ***REMOVED***
***REMOVED***
