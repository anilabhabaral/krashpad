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

import org.github.krashpad.util.jdk.JdkUtil;
import org.junit.Assert;

import junit.framework.TestCase;

/**
 * @author <a href="mailto:mmillson@redhat.com">Mike Millson</a>
 * 
 */
public class TestUmaskEvent extends TestCase {

    public void testIdentity() {
        String logLine = "umask: 0022 (----w--w-)";
        Assert.assertTrue(JdkUtil.LogEventType.UMASK.toString() + " not identified.",
                JdkUtil.identifyEventType(logLine, null) == JdkUtil.LogEventType.UMASK);
    ***REMOVED***

    public void testParseLogLine() {
        String logLine = "umask: 0022 (----w--w-)";
        Assert.assertTrue(JdkUtil.LogEventType.UMASK.toString() + " not parsed.",
                JdkUtil.parseLogLine(logLine, null) instanceof UmaskEvent);
    ***REMOVED***
***REMOVED***