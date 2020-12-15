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

import org.github.errcat.util.jdk.JdkUtil;
import org.junit.Assert;

import junit.framework.TestCase;

/**
 * @author <a href="mailto:mmillson@redhat.com">Mike Millson</a>
 * 
 */
public class TestMemoryEvent extends TestCase {

    public void testIdentity() {
        String logLine = "Memory: 4k page, physical 16058700k(1456096k free), swap 8097788k(7612768k free)";
        Assert.assertTrue(JdkUtil.LogEventType.MEMORY.toString() + " not identified.",
                JdkUtil.identifyEventType(logLine) == JdkUtil.LogEventType.MEMORY);
    ***REMOVED***

    public void testParseLogLine() {
        String logLine = "Memory: 4k page, physical 16058700k(1456096k free), swap 8097788k(7612768k free)";
        Assert.assertTrue(JdkUtil.LogEventType.MEMORY.toString() + " not parsed.",
                JdkUtil.parseLogLine(logLine) instanceof MemoryEvent);
    ***REMOVED***

    public void testMemory() {
        String logLine = "Memory: 4k page, physical 16058700k(1456096k free), swap 8097788k(7612768k free)";
        Assert.assertTrue(JdkUtil.LogEventType.MEMORY.toString() + " not parsed.",
                JdkUtil.parseLogLine(logLine) instanceof MemoryEvent);
        MemoryEvent event = new MemoryEvent(logLine);
        Assert.assertEquals("Physical memory not correct.", 16058700, event.getPhysicalMemory());
        Assert.assertEquals("Physical memory free not correct.", 1456096, event.getPhysicalMemoryFree());
        Assert.assertEquals("Swap not correct.", 8097788, event.getSwap());
        Assert.assertEquals("Swap free not correct.", 7612768, event.getSwapFree());
    ***REMOVED***

    public void testMemoryWindows() {
        String logLine = "Memory: 4k page, system-wide physical 16383M (5994M free)";
        Assert.assertTrue(JdkUtil.LogEventType.MEMORY.toString() + " not parsed.",
                JdkUtil.parseLogLine(logLine) instanceof MemoryEvent);
        MemoryEvent event = new MemoryEvent(logLine);
        Assert.assertEquals("Physical memory not correct.", 16383L * 1024, event.getPhysicalMemory());
        Assert.assertEquals("Physical memory free not correct.", 5994 * 1024, event.getPhysicalMemoryFree());
        Assert.assertEquals("Swap not correct.", 0, event.getSwap());
        Assert.assertEquals("Swap free not correct.", 0, event.getSwapFree());
    ***REMOVED***

    public void testMemory9Digits() {
        String logLine = "Memory: 4k page, physical 263868708k(8753840k free), swap 2097148k(36k free)";
        Assert.assertTrue(JdkUtil.LogEventType.MEMORY.toString() + " not parsed.",
                JdkUtil.parseLogLine(logLine) instanceof MemoryEvent);
        MemoryEvent event = new MemoryEvent(logLine);
        Assert.assertEquals("Physical memory not correct.", 263868708, event.getPhysicalMemory());
        Assert.assertEquals("Physical memory free not correct.", 8753840, event.getPhysicalMemoryFree());
        Assert.assertEquals("Swap not correct.", 2097148, event.getSwap());
        Assert.assertEquals("Swap free not correct.", 36, event.getSwapFree());
    ***REMOVED***
***REMOVED***
