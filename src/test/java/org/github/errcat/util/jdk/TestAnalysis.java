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
package org.github.errcat.util.jdk;

import java.io.File;

import org.github.errcat.domain.jdk.FatalErrorLog;
import org.github.errcat.service.Manager;
import org.github.errcat.util.Constants;
import org.github.errcat.util.ErrUtil;
import org.github.errcat.util.jdk.JdkUtil.JavaSpecification;
import org.junit.Assert;

import junit.framework.TestCase;

/**
 * @author <a href="mailto:mmillson@redhat.com">Mike Millson</a>
 * 
 */
public class TestAnalysis extends TestCase {

    public void testLatestRelease() {
        File testFile = new File(Constants.TEST_DATA_DIR + "dataset14.txt");
        Manager manager = new Manager();
        FatalErrorLog fel = manager.parse(testFile);
        Assert.assertTrue(Analysis.WARN_JDK_NOT_LATEST + " analysis not identified.",
                fel.getAnalysis().contains(Analysis.WARN_JDK_NOT_LATEST));
        Assert.assertEquals("Release days diff not correct.", 100,
                ErrUtil.dayDiff(JdkUtil.getJdkReleaseDate(fel), JdkUtil.getLatestJdkReleaseDate(fel)));
        Assert.assertEquals("Release ***REMOVED*** diff not correct.", 1,
                JdkUtil.getLatestJdkReleaseNumber(fel) - JdkUtil.getJdkReleaseNumber(fel));
    ***REMOVED***

    public void testRpmPpc64le() {
        File testFile = new File(Constants.TEST_DATA_DIR + "dataset15.txt");
        Manager manager = new Manager();
        FatalErrorLog fel = manager.parse(testFile);
        Assert.assertTrue(Analysis.INFO_RH_BUILD_RPM + " analysis not identified.",
                fel.getAnalysis().contains(Analysis.INFO_RH_BUILD_RPM));
        Assert.assertTrue(Analysis.WARN_JDK_NOT_LATEST + " analysis not identified.",
                fel.getAnalysis().contains(Analysis.WARN_JDK_NOT_LATEST));
    ***REMOVED***

    public void testNotLts() {
        File testFile = new File(Constants.TEST_DATA_DIR + "dataset16.txt");
        Manager manager = new Manager();
        FatalErrorLog fel = manager.parse(testFile);
        Assert.assertEquals("Release ***REMOVED*** diff not correct.", JavaSpecification.JDK12, fel.getJavaSpecification());
        Assert.assertTrue(Analysis.WARN_JDK_NOT_LTS + " analysis not identified.",
                fel.getAnalysis().contains(Analysis.WARN_JDK_NOT_LTS));
    ***REMOVED***

    public void testAdoptOpenJdk() {
        File testFile = new File(Constants.TEST_DATA_DIR + "dataset17.txt");
        Manager manager = new Manager();
        FatalErrorLog fel = manager.parse(testFile);
        Assert.assertFalse(Analysis.INFO_RH_BUILD_LINUX_ZIP + " analysis incorrectly identified.",
                fel.getAnalysis().contains(Analysis.INFO_RH_BUILD_LINUX_ZIP));
        Assert.assertTrue(Analysis.INFO_ADOPTOPENJDK_POSSIBLE + " analysis not identified.",
                fel.getAnalysis().contains(Analysis.INFO_ADOPTOPENJDK_POSSIBLE));
    ***REMOVED***

    public void testJdkDebugSymbolsMissing() {
        File testFile = new File(Constants.TEST_DATA_DIR + "dataset18.txt");
        Manager manager = new Manager();
        FatalErrorLog fel = manager.parse(testFile);
        Assert.assertFalse(Analysis.INFO_RH_BUILD_LINUX_ZIP + " analysis incorrectly identified.",
                fel.getAnalysis().contains(Analysis.INFO_RH_BUILD_LINUX_ZIP));
        Assert.assertTrue(Analysis.ERROR_DEBUGGING_SYMBOLS + " analysis not identified.",
                fel.getAnalysis().contains(Analysis.ERROR_DEBUGGING_SYMBOLS));
        Assert.assertTrue(Analysis.INFO_JDK_ANCIENT + " analysis not identified.",
                fel.getAnalysis().contains(Analysis.INFO_JDK_ANCIENT));
    ***REMOVED***
***REMOVED***
