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
public class TestElapsedTimeEvent extends TestCase {

    public void testIdentity() {
        String logLine = "elapsed time: 855185 seconds (9d 21h 33m 5s)";
        Assert.assertTrue(JdkUtil.LogEventType.ELAPSED_TIME.toString() + " not identified.",
                JdkUtil.identifyEventType(logLine) == JdkUtil.LogEventType.ELAPSED_TIME);
    ***REMOVED***

    public void testParseLogLine() {
        String logLine = "elapsed time: 855185 seconds (9d 21h 33m 5s)";
        Assert.assertTrue(JdkUtil.LogEventType.ELAPSED_TIME.toString() + " not parsed.",
                JdkUtil.parseLogLine(logLine) instanceof ElapsedTimeEvent);
    ***REMOVED***

    public void testTimezone() {
        String logLine = "elapsed time: 855185 seconds (9d 21h 33m 5s)";
        ElapsedTimeEvent event = new ElapsedTimeEvent(logLine);
        Assert.assertEquals("Elapsed time not correct.", "9d 21h 33m 5s", event.getElapsedTime());
    ***REMOVED***
***REMOVED***
