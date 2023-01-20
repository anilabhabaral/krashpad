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

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.github.krashpad.util.jdk.JdkUtil;
import org.junit.jupiter.api.Test;

/**
 * @author <a href="mailto:mmillson@redhat.com">Mike Millson</a>
 * 
 */
class TestThreadsClassSmrInfoEvent {

    @Test
    void testAdress1() {
        ThreadsClassSmrInfoEvent priorEvent = new ThreadsClassSmrInfoEvent(null);
        String logLine = "0x00007f66f00a8800";
        assertTrue(JdkUtil.identifyEventType(logLine, priorEvent) == JdkUtil.LogEventType.THREADS_CLASS_SMR_INFO,
                JdkUtil.LogEventType.THREADS_CLASS_SMR_INFO.toString() + " not identified.");
    ***REMOVED***

    @Test
    void testAdress2() {
        ThreadsClassSmrInfoEvent priorEvent = new ThreadsClassSmrInfoEvent(null);
        String logLine = "0x00007fff5d5c6000, 0x0000000001b2a000";
        assertTrue(JdkUtil.identifyEventType(logLine, priorEvent) == JdkUtil.LogEventType.THREADS_CLASS_SMR_INFO,
                JdkUtil.LogEventType.THREADS_CLASS_SMR_INFO.toString() + " not identified.");
    ***REMOVED***

    @Test
    void testAdress3() {
        ThreadsClassSmrInfoEvent priorEvent = new ThreadsClassSmrInfoEvent(null);
        String logLine = "0x00007fdbb0001800, 0x00007fdb0c001800, 0x00007fdbf00b1800";
        assertTrue(JdkUtil.identifyEventType(logLine, priorEvent) == JdkUtil.LogEventType.THREADS_CLASS_SMR_INFO,
                JdkUtil.LogEventType.THREADS_CLASS_SMR_INFO.toString() + " not identified.");
    ***REMOVED***

    @Test
    void testAdress4() {
        ThreadsClassSmrInfoEvent priorEvent = new ThreadsClassSmrInfoEvent(null);
        String logLine = "0x00007ffff0017800, 0x00007ffff0450000, 0x00007ffff0452000, 0x00007ffff0460000,";
        assertTrue(JdkUtil.identifyEventType(logLine, priorEvent) == JdkUtil.LogEventType.THREADS_CLASS_SMR_INFO,
                JdkUtil.LogEventType.THREADS_CLASS_SMR_INFO.toString() + " not identified.");
    ***REMOVED***

    @Test
    void testAdress4NoEndComma() {
        ThreadsClassSmrInfoEvent priorEvent = new ThreadsClassSmrInfoEvent(null);
        String logLine = "0x00007f99b80117c0, 0x00007f98f8024750, 0x00007f995001c480, 0x00007f99b0003e20";
        assertTrue(JdkUtil.identifyEventType(logLine, priorEvent) == JdkUtil.LogEventType.THREADS_CLASS_SMR_INFO,
                JdkUtil.LogEventType.THREADS_CLASS_SMR_INFO.toString() + " not identified.");
    ***REMOVED***

    @Test
    void testClosingBrace() {
        ThreadsClassSmrInfoEvent priorEvent = new ThreadsClassSmrInfoEvent(null);
        String logLine = "***REMOVED***";
        assertTrue(JdkUtil.identifyEventType(logLine, priorEvent) == JdkUtil.LogEventType.THREADS_CLASS_SMR_INFO,
                JdkUtil.LogEventType.THREADS_CLASS_SMR_INFO.toString() + " not identified.");
    ***REMOVED***

    @Test
    void testHeader() {
        ThreadsClassSmrInfoEvent priorEvent = new ThreadsClassSmrInfoEvent(null);
        String logLine = "Threads class SMR info:";
        assertTrue(JdkUtil.identifyEventType(logLine, priorEvent) == JdkUtil.LogEventType.THREADS_CLASS_SMR_INFO,
                JdkUtil.LogEventType.THREADS_CLASS_SMR_INFO.toString() + " not identified.");
    ***REMOVED***

    @Test
    void testIdentity() {
        ThreadsClassSmrInfoEvent priorEvent = new ThreadsClassSmrInfoEvent(null);
        String logLine = "_java_thread_list=0x00000000020a0100, length=58, elements={";
        assertTrue(JdkUtil.identifyEventType(logLine, priorEvent) == JdkUtil.LogEventType.THREADS_CLASS_SMR_INFO,
                JdkUtil.LogEventType.THREADS_CLASS_SMR_INFO.toString() + " not identified.");
    ***REMOVED***

    @Test
    void testParseLogLine() {
        ThreadsClassSmrInfoEvent priorEvent = new ThreadsClassSmrInfoEvent(null);
        String logLine = "_java_thread_list=0x00000000020a0100, length=58, elements={";
        assertTrue(JdkUtil.parseLogLine(logLine, priorEvent) instanceof ThreadsClassSmrInfoEvent,
                JdkUtil.LogEventType.THREADS_CLASS_SMR_INFO.toString() + " not parsed.");
    ***REMOVED***
***REMOVED***