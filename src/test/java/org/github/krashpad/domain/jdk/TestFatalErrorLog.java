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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;

import org.github.krashpad.service.Manager;
import org.github.krashpad.util.Constants;
import org.github.krashpad.util.Constants.Device;
import org.github.krashpad.util.Constants.OsType;
import org.github.krashpad.util.Constants.OsVendor;
import org.github.krashpad.util.Constants.OsVersion;
import org.github.krashpad.util.jdk.Analysis;
import org.github.krashpad.util.jdk.JdkUtil;
import org.github.krashpad.util.jdk.JdkUtil.Application;
import org.github.krashpad.util.jdk.JdkUtil.Arch;
import org.github.krashpad.util.jdk.JdkUtil.CompressedOopMode;
import org.github.krashpad.util.jdk.JdkUtil.JavaSpecification;
import org.github.krashpad.util.jdk.JdkUtil.JavaVendor;
import org.junit.jupiter.api.Test;

/**
 * @author <a href="mailto:mmillson@redhat.com">Mike Millson</a>
 * 
 */
class TestFatalErrorLog {

    @Test
    void testAmqCli() {
        String logLine = "java_command: org.apache.activemq.artemis.boot.Artemis queue stat --url tcp://domain:12345 "
                + "--user myuser --password mypassword --maxRows 1234";
        assertTrue(JdkUtil.identifyEventType(logLine, null) == JdkUtil.LogEventType.VM_ARGUMENTS,
                JdkUtil.LogEventType.VM_ARGUMENTS.toString() + " not identified.");
        VmArgumentsEvent event = new VmArgumentsEvent(logLine);
        FatalErrorLog fel = new FatalErrorLog();
        fel.getVmArgumentsEvents().add(event);
        assertEquals(Application.AMQ_CLI, fel.getApplication(), "AMQ CLI application not identified.");
    ***REMOVED***

    @Test
    void testArchSparc() {
        File testFile = new File(Constants.TEST_DATA_DIR + "dataset65.txt");
        Manager manager = new Manager();
        FatalErrorLog fel = manager.parse(testFile);
        assertEquals(Arch.SPARC, fel.getArch(), "Arch not correct.");
        // No vm_info, so not possible to determine vendor
        assertEquals(JavaVendor.UNKNOWN, fel.getJavaVendor(), "Java vendor not correct.");
        assertFalse(fel.getAnalysis().contains(Analysis.WARN_UNIDENTIFIED_LOG_LINE),
                Analysis.WARN_UNIDENTIFIED_LOG_LINE + " analysis incorrectly identified.");
    ***REMOVED***

    @Test
    void testAws() {
        File testFile = new File(Constants.TEST_DATA_DIR + "dataset34.txt");
        Manager manager = new Manager();
        FatalErrorLog fel = manager.parse(testFile);
        assertEquals(JavaSpecification.JDK8, fel.getJavaSpecification(), "Java specification not correct.");
        assertEquals("java-1.8.0-openjdk-1.8.0.272.b10-1.el7_9.x86_64", fel.getRpmDirectory(),
                "Rpm directory not correct.");
        assertTrue(fel.isRhRpmInstall(), "RH rpm install not identified.");
        assertEquals(Device.AWS_BLOCK_STORAGE, fel.getStorageDevice(), "Storage device not correct.");
        assertTrue(fel.getAnalysis().contains(Analysis.INFO_RH_BUILD_RPM_INSTALL),
                Analysis.INFO_RH_BUILD_RPM_INSTALL + " analysis not identified.");
        assertTrue(fel.getAnalysis().contains(Analysis.ERROR_BUFFERBLOB_FLUSH_ICACHE_STUB),
                Analysis.ERROR_BUFFERBLOB_FLUSH_ICACHE_STUB + " analysis not identified.");
    ***REMOVED***

    @Test
    void testCompressedOopMode() {
        FatalErrorLog fel = new FatalErrorLog();
        // BIT32
        String heapAddress = "heap address: 0x00000000c0000000, size: 1024 MB, Compressed Oops mode: 32-bit";
        HeapAddressEvent heapAddressEvent = new HeapAddressEvent(heapAddress);
        fel.setHeapAddressEvent(heapAddressEvent);
        assertEquals(CompressedOopMode.BIT32, fel.getCompressedOopMode(), "Compressed oop mode not correct.");
        // ZERO
        heapAddress = "heap address: 0x00000003c0000000, size: 16384 MB, Compressed Oops mode: "
                + "Zero based, Oop shift amount: 3";
        heapAddressEvent = new HeapAddressEvent(heapAddress);
        fel.setHeapAddressEvent(heapAddressEvent);
        assertEquals(CompressedOopMode.ZERO, fel.getCompressedOopMode(), "Compressed oop mode not correct.");
        // NON_ZERO
        heapAddress = "heap address: 0x00000005a9c00000, size: 8548 MB, Compressed Oops mode: "
                + "Non-zero based:0x00000005a9bff000, Oop shift amount: 3";
        heapAddressEvent = new HeapAddressEvent(heapAddress);
        fel.setHeapAddressEvent(heapAddressEvent);
        assertEquals(CompressedOopMode.NON_ZERO, fel.getCompressedOopMode(), "Compressed oop mode not correct.");
        assertEquals(8548, fel.getHeapMaxSize(), "Heap max size not correct.");
    ***REMOVED***

    @Test
    void testCurrentThread() {
        File testFile = new File(Constants.TEST_DATA_DIR + "dataset20.txt");
        Manager manager = new Manager();
        FatalErrorLog fel = manager.parse(testFile);
        assertEquals(
                "JavaThread \"ajp-/hostname:8109-16\" daemon [_thread_in_native, id=112672, "
                        + "stack(0x00007f11e11a2000,0x00007f11e12a3000)]",
                fel.getCurrentThread(), "Current thread not correct.");
    ***REMOVED***

    @Test
    void testDatesJdk11() {
        FatalErrorLog fel = new FatalErrorLog();
        String logLine = "Time: Sun Nov 14 14:25:00 2021 UTC elapsed time: 5697278.196357 seconds (65d 22h 34m 38s)";
        TimeElapsedTimeEvent event = new TimeElapsedTimeEvent(logLine);
        fel.setTimeElapsedTimeEvent(event);
        assertEquals("Sun Nov 14 14:25:00 2021 UTC", fel.getCrashTimeString(), "Crash time not correct.");
        assertEquals("65d 22h 34m 38s", fel.getElapsedTime(), "Elapsed time not correct.");
    ***REMOVED***

    @Test
    void testDatesJdk8() {
        FatalErrorLog fel = new FatalErrorLog();
        TimeEvent timeEvent = new TimeEvent("time: Tue Nov 23 09:21:06 2021");
        fel.setTimeEvent(timeEvent);
        TimezoneEvent timezoneEvent = new TimezoneEvent("***REMOVED***");
        fel.setTimezoneEvent(timezoneEvent);
        ElapsedTimeEvent elapsedTimeEvent = new ElapsedTimeEvent("elapsed time: 644647 seconds (7d 11h 4m 7s)");
        fel.setElapsedTimeEvent(elapsedTimeEvent);
        assertEquals("Tue Nov 23 09:21:06 2021 CET", fel.getCrashTimeString(), "Crash time not correct.");
        assertEquals("7d 11h 4m 7s", fel.getElapsedTime(), "Elapsed time not correct.");
    ***REMOVED***

    @Test
    void testDebugSymbolsNoVmCodeInStack() {
        File testFile = new File(Constants.TEST_DATA_DIR + "dataset5.txt");
        Manager manager = new Manager();
        FatalErrorLog fel = manager.parse(testFile);
        assertFalse(fel.getAnalysis().contains(Analysis.WARN_UNIDENTIFIED_LOG_LINE),
                Analysis.WARN_UNIDENTIFIED_LOG_LINE + " analysis incorrectly identified.");
        assertFalse(fel.isRhel(), "OS incorrectly identified as RHEL.");
        assertFalse(fel.getAnalysis().contains(Analysis.WARN_DEBUG_SYMBOLS),
                Analysis.WARN_DEBUG_SYMBOLS + " analysis incorrectly identified.");
        assertFalse(fel.getAnalysis().contains(Analysis.INFO_RH_BUILD_RPM_INSTALL),
                Analysis.INFO_RH_BUILD_RPM_INSTALL + " analysis incorrectly identified.");
        assertTrue(fel.getAnalysis().contains(Analysis.INFO_RH_BUILD_CENTOS),
                Analysis.INFO_RH_BUILD_CENTOS + " analysis not identified.");
        assertFalse(fel.getAnalysis().contains(Analysis.INFO_STACK_NO_VM_CODE),
                Analysis.INFO_STACK_NO_VM_CODE + " analysis incorrectly identified.");
    ***REMOVED***

    @Test
    void testDirectMemory() {
        File testFile = new File(Constants.TEST_DATA_DIR + "dataset50.txt");
        Manager manager = new Manager();
        FatalErrorLog fel = manager.parse(testFile);
        long heapInitial = JdkUtil.convertSize(96, 'G', Constants.PRECISION_REPORTING);
        assertEquals(heapInitial, fel.getHeapInitialSize(), "Heap initial size not correct.");
        long heapMax = JdkUtil.convertSize(96, 'G', Constants.PRECISION_REPORTING);
        assertEquals(heapMax, fel.getHeapMaxSize(), "Heap max size not correct.");
        long heapAllocation = JdkUtil.convertSize(96, 'G', Constants.PRECISION_REPORTING);
        assertEquals(heapAllocation, fel.getHeapAllocation(), "Heap allocation not correct.");
        long heapUsed = JdkUtil.convertSize(78973, 'M', Constants.PRECISION_REPORTING);
        assertEquals(heapUsed, fel.getHeapUsed(), "Heap used not correct.");
        long metaspaceMax = JdkUtil.convertSize(182272, 'K', Constants.PRECISION_REPORTING);
        assertEquals(metaspaceMax, fel.getMetaspaceMaxSize(), "Metaspace max size not correct.");
        long metaspaceAllocation = JdkUtil.convertSize(180428, 'K', Constants.PRECISION_REPORTING);
        assertEquals(metaspaceAllocation, fel.getMetaspaceAllocation(), "Metaspace allocation not correct.");
        long metaspaceUsed = JdkUtil.convertSize(176392, 'K', Constants.PRECISION_REPORTING);
        assertEquals(metaspaceUsed, fel.getMetaspaceUsed(), "Metaspace used not correct.");
        long directMemoryMax = JdkUtil.convertSize(8, 'G', Constants.PRECISION_REPORTING);
        assertEquals(directMemoryMax, fel.getDirectMemoryMaxSize(), "Direct Memory max not correct.");
        assertEquals(1024, fel.getThreadStackSize(), "Thread stack size not correct.");
        assertEquals(720, fel.getJavaThreadCount(), "Thread count not correct.");
        long threadMemory = JdkUtil.convertSize(1024 * 720, 'K', Constants.PRECISION_REPORTING);
        assertEquals(threadMemory, fel.getThreadStackMemory(), "Thread memory not correct.");
        long codeCacheSize = JdkUtil.convertSize(420, 'M', Constants.PRECISION_REPORTING);
        assertEquals(codeCacheSize, fel.getCodeCacheSize(), "Code cache size not correct.");
        assertEquals(heapInitial + metaspaceMax + directMemoryMax + threadMemory + codeCacheSize,
                fel.getJvmMemoryInitial(), "Jvm memory max not correct.");
        assertEquals(heapMax + metaspaceMax + directMemoryMax + threadMemory + codeCacheSize, fel.getJvmMemoryMax(),
                "Jvm memory max not correct.");
    ***REMOVED***

    @Test
    void testG1() {
        File testFile = new File(Constants.TEST_DATA_DIR + "dataset32.txt");
        Manager manager = new Manager();
        FatalErrorLog fel = manager.parse(testFile);
        assertEquals("Tue May  5 18:32:04 2020 CEST", fel.getCrashTimeString(), "Crash time not correct.");
        assertEquals("0d 0h 15m 56s", fel.getElapsedTime(), "Elapsed time not correct.");
        long physicalMemory = JdkUtil.convertSize(32780544, 'K', Constants.PRECISION_REPORTING);
        assertEquals(physicalMemory, fel.getContainerMemTotal(), "Physical memory not correct.");
        long physicalMemoryFree = JdkUtil.convertSize(2698868, 'K', Constants.PRECISION_REPORTING);
        assertEquals(physicalMemoryFree, fel.getContainerMemFree(), "Physical memory free not correct.");
        long swap = JdkUtil.convertSize(8191996, 'K', Constants.PRECISION_REPORTING);
        assertEquals(swap, fel.getContainerSwap(), "Swap not correct.");
        long swapFree = JdkUtil.convertSize(8190972, 'K', Constants.PRECISION_REPORTING);
        assertEquals(swapFree, fel.getContainerSwapFree(), "Swap free not correct.");
        long heapInitial = JdkUtil.convertSize(1073741824, 'B', Constants.PRECISION_REPORTING);
        assertEquals(heapInitial, fel.getHeapInitialSize(), "Heap initial size not correct.");
        long heapMax = JdkUtil.convertSize(12884901888L, 'B', Constants.PRECISION_REPORTING);
        assertEquals(heapMax, fel.getHeapMaxSize(), "Heap max size not correct.");
        long heapAllocation = JdkUtil.convertSize(1933312, 'K', Constants.PRECISION_REPORTING);
        assertEquals(heapAllocation, fel.getHeapAllocation(), "Heap allocation not correct.");
        long heapUsed = JdkUtil.convertSize(1030565, 'K', Constants.PRECISION_REPORTING);
        assertEquals(heapUsed, fel.getHeapUsed(), "Heap used not correct.");
        long metaspaceMax = JdkUtil.convertSize(1189888, 'K', Constants.PRECISION_REPORTING);
        assertEquals(metaspaceMax, fel.getMetaspaceMaxSize(), "Metaspace max size not correct.");
        long metaspaceAllocation = JdkUtil.convertSize(159168, 'K', Constants.PRECISION_REPORTING);
        assertEquals(metaspaceAllocation, fel.getMetaspaceAllocation(), "Metaspace allocation not correct.");
        long metaspaceUsed = JdkUtil.convertSize(147896, 'K', Constants.PRECISION_REPORTING);
        assertEquals(metaspaceUsed, fel.getMetaspaceUsed(), "Metaspace used not correct.");
        assertEquals(8, fel.getCpusLogical(), "CPU cores not correct.");
        assertEquals(5 * 1024, fel.getThreadStackSize(), "Thread stack size not correct.");
        assertEquals(768, fel.getCompressedClassSpaceSize(), "CompressedClassSpaceSize not correct.");
        assertEquals(1024, fel.getDirectMemoryMaxSize(), "MaxDirectMemorySize not correct.");
        assertEquals(255838, fel.getThreadsMax(), "threads-max not correct.");
        assertEquals(32768, fel.getPidMax(), "pid_max not correct.");
        assertEquals(65530, fel.getMaxMapCount(), "max_map_count not correct.");
    ***REMOVED***

    @Test
    void testHaveDebuggingSymbols() {
        FatalErrorLog fel = new FatalErrorLog();
        String headerLine = "***REMOVED*** V  [libjvm.so+0xa333a6]  ShenandoahUpdateRefsClosure::do_oop(oopDesc**)+0x26";
        HeaderEvent he = new HeaderEvent(headerLine);
        fel.getHeaderEvents().add(he);
        assertTrue(fel.haveJdkDebugSymbols(), "Debugging symbols not identified.");
    ***REMOVED***

    @Test
    void testHeaderArch() {
        FatalErrorLog fel = new FatalErrorLog();
        String headerLine = "***REMOVED*** Java VM: Java HotSpot(TM) 64-Bit Server VM (24.85-b06 mixed mode linux-amd64 compressed "
                + "oops)";
        HeaderEvent he = new HeaderEvent(headerLine);
        fel.getHeaderEvents().add(he);
        assertEquals(Arch.X86_64, fel.getArch(), "Arch not correct.");
    ***REMOVED***

    @Test
    void testHeaderJdkVersion() {
        FatalErrorLog fel = new FatalErrorLog();
        String headerLine = "***REMOVED*** JRE version:  (7.0_85-b15) (build )";
        HeaderEvent he = new HeaderEvent(headerLine);
        fel.getHeaderEvents().add(he);
        assertEquals(JavaSpecification.JDK7, fel.getJavaSpecification(), "Java specification not correct.");
        assertEquals("1.7.0_85-b15", fel.getJdkReleaseString(), "Java release not correct.");
    ***REMOVED***

    @Test
    void testHeaderOsVersion() {
        FatalErrorLog fel = new FatalErrorLog();
        String headerLine = "***REMOVED*** Java VM: Java HotSpot(TM) 64-Bit Server VM (24.85-b06 mixed mode linux-amd64 compressed "
                + "oops)";
        HeaderEvent he = new HeaderEvent(headerLine);
        fel.getHeaderEvents().add(he);
        assertEquals(OsVersion.UNKNOWN, fel.getOsVersion(), "OS version not correct.");
        assertEquals(OsType.LINUX, fel.getOsType(), "OS type not correct.");
    ***REMOVED***

    @Test
    void testHeapAllocation() {
        FatalErrorLog fel = new FatalErrorLog();
        String event1 = "***REMOVED***";
        HeapEvent heapEvent1 = new HeapEvent(event1);
        fel.getHeapEvents().add(heapEvent1);
        String event2 = " 3456M max, 3456M soft max, 3200M committed, 2325M used";
        HeapEvent heapEvent2 = new HeapEvent(event2);
        fel.getHeapEvents().add(heapEvent2);
        assertEquals(3200, fel.getHeapAllocation(), "Heap allocation not correct.");
    ***REMOVED***

    @Test
    void testHeapMaxGlobalFlag() {
        File testFile = new File(Constants.TEST_DATA_DIR + "dataset41.txt");
        Manager manager = new Manager();
        FatalErrorLog fel = manager.parse(testFile);
        long heapInitial = JdkUtil.convertSize(32, 'G', Constants.PRECISION_REPORTING);
        assertEquals(heapInitial, fel.getHeapInitialSize(), "Heap initial size not correct.");
        long heapMax = JdkUtil.convertSize(33554432, 'K', Constants.PRECISION_REPORTING);
        assertEquals(heapMax, fel.getHeapMaxSize(), "Heap max size not correct.");
        long codeCacheSize = JdkUtil.convertSize(251658240, 'B', Constants.PRECISION_REPORTING);
        assertEquals(codeCacheSize, fel.getCodeCacheSize(), "Code cache size not correct.");
        long jvmMemoryMax = JdkUtil.convertSize(33554432 + 245760, 'K', Constants.PRECISION_REPORTING);
        assertEquals(jvmMemoryMax, fel.getJvmMemoryMax(), "Jvm memory max not correct.");
    ***REMOVED***

    @Test
    void testInternalError() {
        File testFile = new File(Constants.TEST_DATA_DIR + "dataset3.txt");
        Manager manager = new Manager();
        FatalErrorLog fel = manager.parse(testFile);
        String causedBy = "***REMOVED***  Internal Error (ciEnv.hpp:172), pid=6570, tid=0x00007fe3d7dfd700"
                + Constants.LINE_SEPARATOR + "***REMOVED***  Error: ShouldNotReachHere()";
        assertEquals(causedBy, fel.getError(), "Caused by incorrect.");
        assertTrue(fel.haveJdkDebugSymbols(), "Debugging symbols incorrectly identified.");
    ***REMOVED***

    @Test
    void testJavaThreadCount() {
        File testFile = new File(Constants.TEST_DATA_DIR + "dataset26.txt");
        Manager manager = new Manager();
        FatalErrorLog fel = manager.parse(testFile);
        assertEquals(37, fel.getJavaThreadCount(), "Java thread count not correct.");
    ***REMOVED***

    @Test
    void testJBoss() {
        String logLine = "java_command: /path/to/jboss-modules.jar -Djboss.home.dir=/path/to/standalone";
        assertTrue(JdkUtil.identifyEventType(logLine, null) == JdkUtil.LogEventType.VM_ARGUMENTS,
                JdkUtil.LogEventType.VM_ARGUMENTS.toString() + " not identified.");
        VmArgumentsEvent event = new VmArgumentsEvent(logLine);
        FatalErrorLog fel = new FatalErrorLog();
        fel.getVmArgumentsEvents().add(event);
        assertEquals(Application.WILDFLY, fel.getApplication(), "JBoss application not identified.");
    ***REMOVED***

    @Test
    void testJdk8NotRhVersion() {
        FatalErrorLog fel = new FatalErrorLog();
        String os = "OS: Windows 10.0 , 64 bit Build 17763 (10.0.17763.2028)";
        OsEvent osEvent = new OsEvent(os);
        fel.getOsEvents().add(osEvent);
        String vmInfo = "vm_info: Java HotSpot(TM) 64-Bit Server VM (25.271-b09) for windows-amd64 JRE "
                + "(1.8.0_271-b09), built on Sep 16 2020 19:14:59 by \"\" with MS VC++ 15.9 (VS2017)";
        VmInfoEvent vmInfoEvent = new VmInfoEvent(vmInfo);
        fel.setVmInfoEvent(vmInfoEvent);
        fel.doAnalysis();
        assertFalse(fel.getAnalysis().contains(Analysis.INFO_RH_BUILD_POSSIBLE),
                Analysis.INFO_RH_BUILD_RPM_BASED + " analysis incorrectly identified.");
    ***REMOVED***

    @Test
    void testJdk8Rhel7GenericOs() {
        FatalErrorLog fel = new FatalErrorLog();
        String os = "OS: Linux";
        OsEvent osEvent = new OsEvent(os);
        fel.getOsEvents().add(osEvent);
        String uname = "uname:Linux 3.10.0-514.6.1.el7.x86_64 ***REMOVED***1 SMP Sat Dec 10 11:15:38 EST 2016 x86_6";
        UnameEvent unameEvent = new UnameEvent(uname);
        fel.setUnameEvent(unameEvent);
        assertEquals("Linux", fel.getOsString(), "OS string not correct.");
        assertEquals(OsVersion.RHEL7, fel.getOsVersion(), "OS version not correct.");
    ***REMOVED***

    @Test
    void testJdkRedHatBuildUnknown() {
        File testFile = new File(Constants.TEST_DATA_DIR + "dataset6.txt");
        Manager manager = new Manager();
        FatalErrorLog fel = manager.parse(testFile);
        assertTrue(fel.isRhel(), "OS not identified as RHEL.");
        assertTrue(fel.isRhRpmInstall(), "Red Hat rpm install not identified.");
        assertEquals(OsVersion.RHEL6, fel.getOsVersion(), "OS version not correct.");
        assertTrue(fel.getAnalysis().contains(Analysis.WARN_RHEL6), Analysis.WARN_RHEL6 + " analysis not identified.");
    ***REMOVED***

    @Test
    void testJlinkCustomerJreFromRpm() {
        FatalErrorLog fel = new FatalErrorLog();
        String os = "OS:Red Hat Enterprise Linux Server release 7.7 (Maipo)";
        OsEvent osEvent = new OsEvent(os);
        fel.getOsEvents().add(osEvent);
        String vmInfo = "vm_info: OpenJDK 64-Bit Server VM (11.0.9.1+1-LTS) for linux-amd64 JRE (11.0.9.1+1-LTS), "
                + "built on Nov 12 2020 18:10:11 by \"mockbuild\" with gcc 4.8.5 20150623 (Red Hat 4.8.5-44";
        VmInfoEvent vmInfoEvent = new VmInfoEvent(vmInfo);
        fel.setVmInfoEvent(vmInfoEvent);
        fel.doAnalysis();
        assertEquals(OsType.LINUX, fel.getOsType(), "OS not correct.");
        assertEquals(OsVersion.RHEL7, fel.getOsVersion(), "OS version not correct.");
        assertTrue(fel.isRhRpm(), "Red Hat rpm not identified.");
        assertFalse(fel.getAnalysis().contains(Analysis.INFO_RH_BUILD_RPM_INSTALL),
                Analysis.INFO_RH_BUILD_RPM_INSTALL + " analysis incorrectly identified.");
        assertFalse(fel.getAnalysis().contains(Analysis.INFO_RH_BUILD_CENTOS),
                Analysis.INFO_RH_BUILD_CENTOS + " analysis incorrectly identified.");
        assertTrue(fel.getAnalysis().contains(Analysis.INFO_RH_BUILD_RPM_BASED),
                Analysis.INFO_RH_BUILD_RPM_BASED + " analysis not identified.");
    ***REMOVED***

    @Test
    void testJvmUser() {
        File testFile = new File(Constants.TEST_DATA_DIR + "dataset26.txt");
        Manager manager = new Manager();
        FatalErrorLog fel = manager.parse(testFile);
        assertEquals("user123", fel.getJvmUser(), "JVM user not correct.");
    ***REMOVED***

    @Test
    void testJvmUserWithDotAndAtSign() {
        FatalErrorLog fel = new FatalErrorLog();
        String hsperfdata = "7f23f0838000-7f23f0840000 rw-s 00000000 fd:05 3430                       "
                + "/tmp/hsperfdata_first.last@location/12345";
        DynamicLibraryEvent dynamicLibraryEvent = new DynamicLibraryEvent(hsperfdata);
        fel.getDynamicLibraryEvents().add(dynamicLibraryEvent);
        assertEquals("first.last@location", fel.getJvmUser(), "JVM user not correct.");
    ***REMOVED***

    @Test
    void testJvmUserWithUnderscore() {
        FatalErrorLog fel = new FatalErrorLog();
        String hsperfdata = "7ff0f61d2000-7ff0f61da000 rw-s 00000000 fd:01 33563495                   "
                + "/tmp/hsperfdata_jb_admin/92333";
        DynamicLibraryEvent dynamicLibraryEvent = new DynamicLibraryEvent(hsperfdata);
        fel.getDynamicLibraryEvents().add(dynamicLibraryEvent);
        assertEquals("jb_admin", fel.getJvmUser(), "JVM user not correct.");
    ***REMOVED***

    @Test
    void testKafka() {
        String logLine = "java_command: kafka.Kafka /path/to/my.properties";
        assertTrue(JdkUtil.identifyEventType(logLine, null) == JdkUtil.LogEventType.VM_ARGUMENTS,
                JdkUtil.LogEventType.VM_ARGUMENTS.toString() + " not identified.");
        VmArgumentsEvent event = new VmArgumentsEvent(logLine);
        FatalErrorLog fel = new FatalErrorLog();
        fel.getVmArgumentsEvents().add(event);
        assertEquals(Application.KAFKA, fel.getApplication(), "KAFKA application not identified.");
    ***REMOVED***

    @Test
    void testLogicalCpus() {
        File testFile = new File(Constants.TEST_DATA_DIR + "dataset53.txt");
        Manager manager = new Manager();
        FatalErrorLog fel = manager.parse(testFile);
        assertEquals(200, fel.getCpusLogical(), "CPU cores not correct.");
    ***REMOVED***

    @Test
    void testMaxjitcodesize() {
        File testFile = new File(Constants.TEST_DATA_DIR + "dataset37.txt");
        Manager manager = new Manager();
        FatalErrorLog fel = manager.parse(testFile);
        long codeCacheSize = JdkUtil.convertSize(1024, 'm', Constants.PRECISION_REPORTING);
        assertEquals(codeCacheSize, fel.getCodeCacheSize(), "Code cache size not correct.");
    ***REMOVED***

    @Test
    void testMeminfo() {
        File testFile = new File(Constants.TEST_DATA_DIR + "dataset38.txt");
        Manager manager = new Manager();
        FatalErrorLog fel = manager.parse(testFile);
        long physicalMemory = JdkUtil.convertSize(1584737884, 'K', Constants.PRECISION_REPORTING);
        assertEquals(physicalMemory, fel.getOsMemTotal(), "System physical memory not correct.");
        long physicalMemoryFree = JdkUtil.convertSize(136528040, 'K', Constants.PRECISION_REPORTING);
        assertEquals(physicalMemoryFree, fel.getOsMemFree(), "System physical memory free not correct.");
        long swap = JdkUtil.convertSize(33554428, 'K', Constants.PRECISION_REPORTING);
        assertEquals(swap, fel.getOsSwap(), "System swap not correct.");
        long swapFree = JdkUtil.convertSize(33554428, 'K', Constants.PRECISION_REPORTING);
        assertEquals(swapFree, fel.getOsSwapFree(), "System swap free not correct.");
        long heapInitial = JdkUtil.convertSize(220000, 'M', Constants.PRECISION_REPORTING);
        assertEquals(heapInitial, fel.getHeapInitialSize(), "Heap initial size not correct.");
        long heapMax = JdkUtil.convertSize(220000, 'M', Constants.PRECISION_REPORTING);
        assertEquals(heapMax, fel.getHeapMaxSize(), "Heap max size not correct.");
        long heapAllocation = JdkUtil.convertSize(225041472, 'K', Constants.PRECISION_REPORTING);
        assertEquals(heapAllocation, fel.getHeapAllocation(), "Heap allocation not correct.");
        long heapUsed = JdkUtil.convertSize(1908416, 'K', Constants.PRECISION_REPORTING);
        assertEquals(heapUsed, fel.getHeapUsed(), "Heap used not correct.");
        long metaspaceMax = JdkUtil.convertSize(43008, 'K', Constants.PRECISION_REPORTING);
        assertEquals(metaspaceMax, fel.getMetaspaceMaxSize(), "Metaspace max size not correct.");
        long metaspaceAllocation = JdkUtil.convertSize(41268, 'K', Constants.PRECISION_REPORTING);
        assertEquals(metaspaceAllocation, fel.getMetaspaceAllocation(), "Metaspace allocation not correct.");
        long metaspaceUsed = JdkUtil.convertSize(40246, 'K', Constants.PRECISION_REPORTING);
        assertEquals(metaspaceUsed, fel.getMetaspaceUsed(), "Metaspace used not correct.");
        assertEquals(256, fel.getThreadStackSize(), "Thread stack size not correct.");
    ***REMOVED***

    @Test
    void testMemory() {
        File testFile = new File(Constants.TEST_DATA_DIR + "dataset26.txt");
        Manager manager = new Manager();
        FatalErrorLog fel = manager.parse(testFile);
        long physicalMemory = JdkUtil.convertSize(16058700, 'K', Constants.PRECISION_REPORTING);
        assertEquals(physicalMemory, fel.getContainerMemTotal(), "Physical memory not correct.");
        long physicalMemoryFree = JdkUtil.convertSize(1456096, 'K', Constants.PRECISION_REPORTING);
        assertEquals(physicalMemoryFree, fel.getContainerMemFree(), "Physical memory free not correct.");
        long swap = JdkUtil.convertSize(8097788, 'K', Constants.PRECISION_REPORTING);
        assertEquals(swap, fel.getContainerSwap(), "Swap not correct.");
        long swapFree = JdkUtil.convertSize(7612768, 'K', Constants.PRECISION_REPORTING);
        assertEquals(swapFree, fel.getContainerSwapFree(), "Swap free not correct.");
        long heapInitial = JdkUtil.convertSize(512, 'M', Constants.PRECISION_REPORTING);
        assertEquals(heapInitial, fel.getHeapInitialSize(), "Heap initial size not correct.");
        long heapMax = JdkUtil.convertSize(1024, 'M', Constants.PRECISION_REPORTING);
        assertEquals(heapMax, fel.getHeapMaxSize(), "Heap max size not correct.");
        long heapAllocation = JdkUtil.convertSize(244736 + 699392, 'K', Constants.PRECISION_REPORTING);
        assertEquals(heapAllocation, fel.getHeapAllocation(), "Heap allocation not correct.");
        long heapUsed = JdkUtil.convertSize(103751 + 91187, 'K', Constants.PRECISION_REPORTING);
        assertEquals(heapUsed, fel.getHeapUsed(), "Heap used not correct.");
        long metaspaceMax = JdkUtil.convertSize(1183744, 'K', Constants.PRECISION_REPORTING);
        assertEquals(metaspaceMax, fel.getMetaspaceMaxSize(), "Metaspace max size not correct.");
        long metaspaceAllocation = JdkUtil.convertSize(155992, 'K', Constants.PRECISION_REPORTING);
        assertEquals(metaspaceAllocation, fel.getMetaspaceAllocation(), "Metaspace allocation not correct.");
        long metaspaceUsed = JdkUtil.convertSize(139716, 'K', Constants.PRECISION_REPORTING);
        assertEquals(metaspaceUsed, fel.getMetaspaceUsed(), "Metaspace used not correct.");
        assertEquals(16, fel.getCpusLogical(), "CPU cores not correct.");
        long commitLimit = JdkUtil.convertSize(16127136, 'K', Constants.PRECISION_REPORTING);
        assertEquals(commitLimit, fel.getCommitLimit(), "CommitLimit not correct.");
        long committedAs = JdkUtil.convertSize(28976296, 'K', Constants.PRECISION_REPORTING);
        assertEquals(committedAs, fel.getCommittedAs(), "Committed_AS not correct.");
    ***REMOVED***

    @Test
    void testNoCompressedOops() {
        FatalErrorLog fel = new FatalErrorLog();
        String headerLine = "***REMOVED*** Java VM: OpenJDK 64-Bit Server VM (25.302-b08 mixed mode linux-amd64 )";
        HeaderEvent he = new HeaderEvent(headerLine);
        fel.getHeaderEvents().add(he);
        assertEquals(CompressedOopMode.NONE, fel.getCompressedOopMode(), "Compressed oop mode not correct.");
    ***REMOVED***

    @Test
    void testNoDebuggingSymbols() {
        FatalErrorLog fel = new FatalErrorLog();
        String headerLine = "***REMOVED*** V  [libjvm.so+0xa41ea4]";
        HeaderEvent he = new HeaderEvent(headerLine);
        fel.getHeaderEvents().add(he);
        assertFalse(fel.haveJdkDebugSymbols(), "Debugging symbols incorrectly identified.");
    ***REMOVED***

    @Test
    void testOsJustLinux() {
        FatalErrorLog fel = new FatalErrorLog();
        String os = "OS:Linux";
        OsEvent osEvent = new OsEvent(os);
        fel.getOsEvents().add(osEvent);
        assertEquals(OsType.LINUX, fel.getOsType(), "OS not correct.");
        assertEquals(OsVersion.UNKNOWN, fel.getOsVersion(), "OS version not correct.");
    ***REMOVED***

    @Test
    void testOsRhel() {
        FatalErrorLog fel = new FatalErrorLog();
        String os = "OS:Red Hat Enterprise Linux Server release 7.8 (Maipo)";
        OsEvent osEvent = new OsEvent(os);
        fel.getOsEvents().add(osEvent);
        assertEquals(OsType.LINUX, fel.getOsType(), "OS not correct.");
        assertEquals(OsVersion.RHEL7, fel.getOsVersion(), "OS version not correct.");
    ***REMOVED***

    @Test
    void testOutOfMemoryError() {
        File testFile = new File(Constants.TEST_DATA_DIR + "dataset21.txt");
        Manager manager = new Manager();
        FatalErrorLog fel = manager.parse(testFile);
        StringBuilder error = new StringBuilder();
        error.append("***REMOVED*** There is insufficient memory for the Java Runtime Environment to continue.");
        error.append(Constants.LINE_SEPARATOR);
        error.append("***REMOVED*** Native memory allocation (mmap) failed to map 754974720 bytes for committing reserved memory.");
        error.append(Constants.LINE_SEPARATOR);
        error.append("***REMOVED***  Out of Memory Error (os_linux.cpp:2749), pid=25305, tid=0x00007f5ab28b7700");
        assertEquals(error.toString(), fel.getError());
    ***REMOVED***

    @Test
    void testRel8Jdk17() {
        File testFile = new File(Constants.TEST_DATA_DIR + "dataset70.txt");
        Manager manager = new Manager();
        FatalErrorLog fel = manager.parse(testFile);
        assertEquals("Red Hat Enterprise Linux release 8.5 (Ootpa)", fel.getOsString(), "OS string not correct.");
        assertEquals(OsVendor.REDHAT, fel.getOsVendor(), "OS vendor not correct.");
        assertEquals(OsVersion.RHEL8, fel.getOsVersion(), "OS version not correct.");
        assertTrue(fel.isJdkLts(), "LTS release not identified.");
        assertTrue(fel.isRhRpm(), "Red Hat rpm not identified.");
        assertTrue(fel.isRhRpmInstall(), "Red Hat rpm install not identified.");
        assertTrue(fel.isRhel(), "RHEL not identified.");
        assertFalse(fel.isWindows(), "Windows incorrectly identified.");
        assertEquals(254790, fel.getThreadsMax(), "threads-max not correct.");
        assertEquals(4194304, fel.getPidMax(), "pid_max not correct.");
        assertEquals(65530, fel.getMaxMapCount(), "max_map_count not correct.");
        assertFalse(fel.getAnalysis().contains(Analysis.WARN_UNIDENTIFIED_LOG_LINE),
                Analysis.WARN_UNIDENTIFIED_LOG_LINE + " analysis incorrectly identified.");
    ***REMOVED***

    @Test
    void testRhBuildStringMockbuild() {
        FatalErrorLog fel = new FatalErrorLog();
        String vmInfo = "vm_info: OpenJDK 64-Bit Server VM (25.181-b13) for linux-ppc64 JRE (1.8.0_181-b13), "
                + "built on Jul 16 2018 11:33:43 by \"mockbuild\" with gcc 4.8.5 20150623 (Red Hat 4.8.5-28";
        VmInfoEvent vmInfoEvent = new VmInfoEvent(vmInfo);
        fel.setVmInfoEvent(vmInfoEvent);
        assertTrue(fel.isRhBuildString(), "RH build string identified.");
    ***REMOVED***

    @Test
    void testRhel7Jdk11() {
        File testFile = new File(Constants.TEST_DATA_DIR + "dataset8.txt");
        Manager manager = new Manager();
        FatalErrorLog fel = manager.parse(testFile);
        assertTrue(fel.isRhel(), "OS not identified as RHEL.");
        assertFalse(fel.getAnalysis().contains(Analysis.WARN_DEBUG_SYMBOLS),
                Analysis.WARN_DEBUG_SYMBOLS + " analysis incorrectly identified.");
        assertTrue(fel.getAnalysis().contains(Analysis.INFO_RH_BUILD_RPM_INSTALL),
                Analysis.INFO_RH_BUILD_RPM_INSTALL + " analysis not identified.");
        assertEquals("11.0.7+10-LTS", fel.getJdkReleaseString(), "Jdk release not correct.");
        assertEquals(JavaVendor.RED_HAT, fel.getJavaVendor(), "Java vendor not correct.");
    ***REMOVED***

    @Test
    void testRhel7Jdk8() {
        File testFile = new File(Constants.TEST_DATA_DIR + "dataset10.txt");
        Manager manager = new Manager();
        FatalErrorLog fel = manager.parse(testFile);
        assertTrue(fel.isRhel(), "OS not identified as RHEL.");
        assertFalse(fel.getAnalysis().contains(Analysis.WARN_DEBUG_SYMBOLS),
                Analysis.WARN_DEBUG_SYMBOLS + " analysis incorrectly identified.");
        assertTrue(fel.getAnalysis().contains(Analysis.INFO_RH_BUILD_RPM_INSTALL),
                Analysis.INFO_RH_BUILD_RPM_INSTALL + " analysis not identified.");
        assertEquals("1.8.0_131-b12", fel.getJdkReleaseString(), "Jdk release not correct.");
        assertEquals(JavaVendor.RED_HAT, fel.getJavaVendor(), "Java vendor not correct.");
    ***REMOVED***

    @Test
    void testRhel8Jdk11() {
        File testFile = new File(Constants.TEST_DATA_DIR + "dataset9.txt");
        Manager manager = new Manager();
        FatalErrorLog fel = manager.parse(testFile);
        assertTrue(fel.isRhel(), "OS not identified as RHEL.");
        assertFalse(fel.getAnalysis().contains(Analysis.WARN_DEBUG_SYMBOLS),
                Analysis.WARN_DEBUG_SYMBOLS + " analysis incorrectly identified.");
        assertTrue(fel.getAnalysis().contains(Analysis.INFO_RH_BUILD_RPM_INSTALL),
                Analysis.INFO_RH_BUILD_RPM_INSTALL + " analysis not identified.");
        assertEquals("11.0.8+10-LTS", fel.getJdkReleaseString(), "Jdk release not correct.");
        assertEquals(JavaVendor.RED_HAT, fel.getJavaVendor(), "Java vendor not correct.");
    ***REMOVED***

    @Test
    void testRhel8Ppc64le() {
        File testFile = new File(Constants.TEST_DATA_DIR + "dataset52.txt");
        Manager manager = new Manager();
        FatalErrorLog fel = manager.parse(testFile);
        assertEquals(Arch.PPC64LE, fel.getArch(), "Arch not correct.");
        assertEquals(JavaVendor.RED_HAT, fel.getJavaVendor(), "Java vendor not correct.");
        assertEquals(Application.TOMCAT, fel.getApplication(), "Application not correct.");
    ***REMOVED***

    @Test
    void testRhelJdkNotRedHatBuild() {
        File testFile = new File(Constants.TEST_DATA_DIR + "dataset4.txt");
        Manager manager = new Manager();
        FatalErrorLog fel = manager.parse(testFile);
        assertTrue(fel.getAnalysis().contains(Analysis.INFO_RH_BUILD_NOT),
                Analysis.INFO_RH_BUILD_NOT + " analysis not identified.");
        assertEquals("Thu May  7 17:24:12 2020 UTC", fel.getCrashTimeString(), "Time of crash not correct.");
        assertEquals("1d 7h 30m 19s", fel.getElapsedTime(), "JVM run time not correct.");
    ***REMOVED***

    @Test
    void testRhShenandoahNotExperimental() {
        FatalErrorLog fel = new FatalErrorLog();
        String os = "OS:Red Hat Enterprise Linux Server release 7.7 (Maipo)";
        OsEvent osEvent = new OsEvent(os);
        fel.getOsEvents().add(osEvent);
        String vmInfo = "vm_info: OpenJDK 64-Bit Server VM (11.0.9.1+1-LTS) for linux-amd64 JRE (11.0.9.1+1-LTS), "
                + "built on Nov 12 2020 18:10:11 by \"mockbuild\" with gcc 4.8.5 20150623 (Red Hat 4.8.5-44";
        VmInfoEvent vmInfoEvent = new VmInfoEvent(vmInfo);
        fel.setVmInfoEvent(vmInfoEvent);
        String jvmArgs = "jvm_args: -XX:+UnlockExperimentalVMOptions -XX:+UseShenandoahGC";
        VmArgumentsEvent vmArgumentsEvent = new VmArgumentsEvent(jvmArgs);
        fel.getVmArgumentsEvents().add(vmArgumentsEvent);
        fel.doAnalysis();
        assertTrue(fel.getAnalysis().contains(Analysis.INFO_RH_BUILD_RPM_BASED),
                Analysis.INFO_RH_BUILD_RPM_BASED + " analysis not identified.");
        assertTrue(fel.getAnalysis().contains(Analysis.INFO_RH_OPT_EXPERIMENTAL_SHENANDOAH),
                Analysis.INFO_RH_OPT_EXPERIMENTAL_SHENANDOAH + " analysis not identified.");
    ***REMOVED***

    @Test
    void testRhSso() {
        String logLine = "  0x00005587a9039000 JavaThread \"Brute Force Protector\" [_thread_blocked, id=6073, "
                + "stack(0x00007f5abb897000,0x00007f5abb998000)]";
        assertTrue(JdkUtil.identifyEventType(logLine, null) == JdkUtil.LogEventType.THREAD,
                JdkUtil.LogEventType.THREAD.toString() + " not identified.");
        ThreadEvent event = new ThreadEvent(logLine);
        FatalErrorLog fel = new FatalErrorLog();
        fel.getThreadEvents().add(event);
        assertEquals(Application.RHSSO, fel.getApplication(), "RHSSO application not identified.");
    ***REMOVED***

    @Test
    void testRhWindowsReleaseWith2BuildDateTimes() {
        File testFile = new File(Constants.TEST_DATA_DIR + "dataset25.txt");
        Manager manager = new Manager();
        FatalErrorLog fel = manager.parse(testFile);
        assertTrue(fel.isWindows(), "OS not identified as Windows.");
        assertEquals(Arch.X86_64, fel.getArch(), "Arch not correct.");
        assertTrue(fel.getAnalysis().contains(Analysis.INFO_RH_BUILD_WINDOWS_ZIP),
                Analysis.INFO_RH_BUILD_WINDOWS_ZIP + " analysis not identified.");
    ***REMOVED***

    @Test
    void testShenandoah() {
        File testFile = new File(Constants.TEST_DATA_DIR + "dataset31.txt");
        Manager manager = new Manager();
        FatalErrorLog fel = manager.parse(testFile);
        long physicalMemory = JdkUtil.convertSize(8388608, 'K', Constants.PRECISION_REPORTING);
        assertEquals(physicalMemory, fel.getContainerMemTotal(), "Physical memory not correct.");
        long physicalMemoryFree = JdkUtil.convertSize(1334692, 'K', Constants.PRECISION_REPORTING);
        assertEquals(physicalMemoryFree, fel.getContainerMemFree(), "Physical memory free not correct.");
        long swap = JdkUtil.convertSize(0, 'K', Constants.PRECISION_REPORTING);
        assertEquals(swap, fel.getContainerSwap(), "Swap not correct.");
        long swapFree = JdkUtil.convertSize(0, 'K', Constants.PRECISION_REPORTING);
        assertEquals(swapFree, fel.getContainerSwapFree(), "Swap free not correct.");
        long heapInitial = JdkUtil.convertSize(4014, 'M', Constants.PRECISION_REPORTING);
        assertEquals(heapInitial, fel.getHeapInitialSize(), "Heap initial size not correct.");
        long heapMax = JdkUtil.convertSize(5734, 'M', Constants.PRECISION_REPORTING);
        assertEquals(heapMax, fel.getHeapMaxSize(), "Heap max size not correct.");
        long heapAllocation = JdkUtil.convertSize(5734, 'M', Constants.PRECISION_REPORTING);
        assertEquals(heapAllocation, fel.getHeapAllocation(), "Heap allocation not correct.");
        long heapUsed = JdkUtil.convertSize(3795, 'M', Constants.PRECISION_REPORTING);
        assertEquals(heapUsed, fel.getHeapUsed(), "Heap used not correct.");
        long metaspaceMax = JdkUtil.convertSize(512, 'M', Constants.PRECISION_REPORTING);
        assertEquals(metaspaceMax, fel.getMetaspaceMaxSize(), "Metaspace max size not correct.");
        long metaspaceAllocation = JdkUtil.convertSize(277632, 'K', Constants.PRECISION_REPORTING);
        assertEquals(metaspaceAllocation, fel.getMetaspaceAllocation(), "Metaspace allocation not correct.");
        long metaspaceUsed = JdkUtil.convertSize(257753, 'K', Constants.PRECISION_REPORTING);
        assertEquals(metaspaceUsed, fel.getMetaspaceUsed(), "Metaspace used not correct.");
        assertEquals(Application.JBOSS_EAP7, fel.getApplication(), "Application not correct.");
        assertEquals(16, fel.getCpusLogical(), "CPU cores not correct.");
        assertEquals("at safepoint (normal execution)", fel.getVmState(), "State not correct.");
        assertTrue(fel.getAnalysis().contains(Analysis.INFO_SWAP_DISABLED),
                Analysis.INFO_SWAP_DISABLED + " analysis not identified.");
        assertTrue(fel.getAnalysis().contains(Analysis.ERROR_JDK8_SHENANDOAH_ROOT_UPDATER),
                Analysis.ERROR_JDK8_SHENANDOAH_ROOT_UPDATER + " analysis not identified.");
    ***REMOVED***

    @Test
    void testSigSegvCompiledJavaCode() {
        File testFile = new File(Constants.TEST_DATA_DIR + "dataset1.txt");
        Manager manager = new Manager();
        FatalErrorLog fel = manager.parse(testFile);
        StringBuffer causedBy = new StringBuffer("***REMOVED***  SIGSEGV (0xb) at pc=0x00007fcd2af94e64, pid=23171, tid=23172");
        causedBy.append(Constants.LINE_SEPARATOR);
        causedBy.append("***REMOVED*** C  [libcairo.so.2+0x66e64]  cairo_region_num_rectangles+0x4");
        assertEquals(causedBy.toString(), fel.getError(), "Caused by incorrect.");
    ***REMOVED***

    @Test
    void testSigSegvNativeCode() {
        File testFile = new File(Constants.TEST_DATA_DIR + "dataset2.txt");
        Manager manager = new Manager();
        FatalErrorLog fel = manager.parse(testFile);
        StringBuffer causedBy = new StringBuffer(
                "***REMOVED***  SIGSEGV (0xb) at pc=0x0000000000000000, pid=44768, tid=0x00007f368f18d700");
        causedBy.append(Constants.LINE_SEPARATOR);
        causedBy.append("***REMOVED*** C  0x0000000000000000");
        assertEquals(causedBy.toString(), fel.getError(), "Caused by incorrect.");
    ***REMOVED***

    @Test
    void testSolaris() {
        File testFile = new File(Constants.TEST_DATA_DIR + "dataset7.txt");
        Manager manager = new Manager();
        FatalErrorLog fel = manager.parse(testFile);
        assertEquals(Arch.SPARC, fel.getArch(), "Arch not correct.");
        assertEquals("1.8.0_251-b08", fel.getJdkReleaseString(), "Jdk release not correct.");
        // No vm_info, so not possible to determine vendor
        assertEquals(JavaVendor.UNKNOWN, fel.getJavaVendor(), "Java vendor not correct.");
        assertFalse(fel.getAnalysis().contains(Analysis.WARN_UNIDENTIFIED_LOG_LINE),
                Analysis.WARN_UNIDENTIFIED_LOG_LINE + " analysis incorrectly identified.");
    ***REMOVED***

    @Test
    void testTenuredGeneration() {
        File testFile = new File(Constants.TEST_DATA_DIR + "dataset40.txt");
        Manager manager = new Manager();
        FatalErrorLog fel = manager.parse(testFile);
        long heapInitial = JdkUtil.convertSize(3172, 'M', Constants.PRECISION_REPORTING);
        assertEquals(heapInitial, fel.getHeapInitialSize(), "Heap initial size not correct.");
        long heapMax = JdkUtil.convertSize(3172, 'M', Constants.PRECISION_REPORTING);
        assertEquals(heapMax, fel.getHeapMaxSize(), "Heap max size not correct.");
        long heapAllocation = JdkUtil.convertSize(947392 + 2165440, 'K', Constants.PRECISION_REPORTING);
        assertEquals(heapAllocation, fel.getHeapAllocation(), "Heap allocation not correct.");
        long heapUsed = JdkUtil.convertSize(396580 + 937560, 'K', Constants.PRECISION_REPORTING);
        assertEquals(heapUsed, fel.getHeapUsed(), "Heap used not correct.");
        long metaspaceMax = JdkUtil.convertSize(1275904, 'K', Constants.PRECISION_REPORTING);
        assertEquals(metaspaceMax, fel.getMetaspaceMaxSize(), "Metaspace max size not correct.");
        long metaspaceAllocation = JdkUtil.convertSize(262244, 'K', Constants.PRECISION_REPORTING);
        assertEquals(metaspaceAllocation, fel.getMetaspaceAllocation(), "Metaspace allocation not correct.");
        long metaspaceUsed = JdkUtil.convertSize(243180, 'K', Constants.PRECISION_REPORTING);
        assertEquals(metaspaceUsed, fel.getMetaspaceUsed(), "Metaspace used not correct.");
        assertEquals(512, fel.getThreadStackSize(), "Thread stack size not correct.");
    ***REMOVED***

    @Test
    void testThreadStackSize() {
        FatalErrorLog fel = new FatalErrorLog();
        String jvmArgs = "jvm_args: -Xmx37400M -Xms37400M -XX:ThreadStackSize=256";
        VmArgumentsEvent event = new VmArgumentsEvent(jvmArgs);
        fel.getVmArgumentsEvents().add(event);
        fel.doAnalysis();
        assertEquals(256, fel.getThreadStackSize(), "Thread stack size not correct.");
        fel.getVmArgumentsEvents().clear();
        jvmArgs = "jvm_args: -Xmx37400M -Xms37400M -XX:ThreadStackSize=256k";
        event = new VmArgumentsEvent(jvmArgs);
        fel.getVmArgumentsEvents().add(event);
        fel.doAnalysis();
        assertEquals(256 * 1024, fel.getThreadStackSize(), "Thread stack size not correct.");
        fel.getVmArgumentsEvents().clear();
        jvmArgs = "jvm_args: -Xmx37400M -Xms37400M -Xss256";
        event = new VmArgumentsEvent(jvmArgs);
        fel.getVmArgumentsEvents().add(event);
        fel.doAnalysis();
        assertEquals(0, fel.getThreadStackSize(), "Thread stack size not correct.");
    ***REMOVED***

    @Test
    void testUnameSplitAcross2Lines() {
        File testFile = new File(Constants.TEST_DATA_DIR + "dataset64.txt");
        Manager manager = new Manager();
        FatalErrorLog fel = manager.parse(testFile);
        assertEquals("SunOS 5.11 11.4.32.88.3 sun4v  (T2 libthread)", fel.getUnameEvent().getUname(),
                "uname not correct.");
    ***REMOVED***

    @Test
    void testUsername() {
        File testFile = new File(Constants.TEST_DATA_DIR + "dataset40.txt");
        Manager manager = new Manager();
        FatalErrorLog fel = manager.parse(testFile);
        assertEquals("username", fel.getUsername(), "USERNAME not correct.");
    ***REMOVED***

    @Test
    void testUsernameWithDotAtSign() {
        FatalErrorLog fel = new FatalErrorLog();
        String username = "USERNAME=first.last@location";
        EnvironmentVariablesEvent environmentVariablesEvent = new EnvironmentVariablesEvent(username);
        fel.getEnvironmentVariablesEvents().add(environmentVariablesEvent);
        assertEquals("first.last@location", fel.getUsername(), "USERNAME not correct.");
    ***REMOVED***

    @Test
    void testUsernameWithUnderscore() {
        FatalErrorLog fel = new FatalErrorLog();
        String username = "USERNAME=jb_admin";
        EnvironmentVariablesEvent environmentVariablesEvent = new EnvironmentVariablesEvent(username);
        fel.getEnvironmentVariablesEvents().add(environmentVariablesEvent);
        assertEquals("jb_admin", fel.getUsername(), "USERNAME not correct.");
    ***REMOVED***

    @Test
    void testVendorAdoptOpenJdkHeader() {
        FatalErrorLog fel = new FatalErrorLog();
        String header = "***REMOVED*** JRE version: OpenJDK Runtime Environment AdoptOpenJDK (11.0.9+11) (build 11.0.9+11)";
        HeaderEvent headerEvent = new HeaderEvent(header);
        fel.getHeaderEvents().add(headerEvent);
        assertEquals(JavaVendor.ADOPTOPENJDK, fel.getJavaVendor(), "JDK vendor not correct.");
    ***REMOVED***

    @Test
    void testVendorAdoptOpenJdkVmInfo() {
        FatalErrorLog fel = new FatalErrorLog();
        String vmInfo = "vm_info: OpenJDK 64-Bit Server VM (25.265-b01) for linux-amd64 JRE (1.8.0_265-b01), "
                + "built on Jul 28 2020 15:17:23 by \"jenkins\" with gcc 4.8.2 20140120 (Red Hat 4.8.2-15)";
        VmInfoEvent vmInfoEvent = new VmInfoEvent(vmInfo);
        fel.setVmInfoEvent(vmInfoEvent);
        assertEquals(JavaVendor.ADOPTOPENJDK, fel.getJavaVendor(), "JDK vendor not correct.");
    ***REMOVED***

    @Test
    void testVendorAzul() {
        FatalErrorLog fel = new FatalErrorLog();
        String vmInfo = "vm_info: OpenJDK 64-Bit Server VM (25.252-b14) for linux-amd64 JRE "
                + "(Zulu 8.46.0.52-SA-linux64) (1.8.0_252-b14), built on Apr 22 2020 07:39:02 by \"zulu_re\" with gcc "
                + "4.4.7 20120313";
        VmInfoEvent vmInfoEvent = new VmInfoEvent(vmInfo);
        fel.setVmInfoEvent(vmInfoEvent);
        assertEquals(JavaVendor.AZUL, fel.getJavaVendor(), "JDK vendor not correct.");
    ***REMOVED***

    @Test
    void testVendorMicrosoft() {
        FatalErrorLog fel = new FatalErrorLog();
        String vmInfo = "vm_info: OpenJDK 64-Bit Server VM (11.0.10+9) for linux-amd64 JRE (11.0.10+9), "
                + "built on Jan 22 2021 19:24:16 by \"vsts\" with gcc 7.3.0";
        VmInfoEvent vmInfoEvent = new VmInfoEvent(vmInfo);
        fel.setVmInfoEvent(vmInfoEvent);
        assertEquals(JavaVendor.MICROSOFT, fel.getJavaVendor(), "JDK vendor not correct.");
    ***REMOVED***

    @Test
    void testVendorOracle() {
        FatalErrorLog fel = new FatalErrorLog();
        String vmInfo = "vm_info: Java HotSpot(TM) 64-Bit Server VM (25.25-b02) for windows-amd64 JRE (1.8.0_25-b18), "
                + "built on Oct  7 2014 14:25:37 by \"java_re\" with MS VC++ 10.0 (VS2010)";
        VmInfoEvent vmInfoEvent = new VmInfoEvent(vmInfo);
        fel.setVmInfoEvent(vmInfoEvent);
        assertEquals(JavaVendor.ORACLE, fel.getJavaVendor(), "JDK vendor not correct.");
    ***REMOVED***

    @Test
    void testVendorUnknown() {
        FatalErrorLog fel = new FatalErrorLog();
        String vmInfo = "vm_info: OpenJDK 64-Bit Server VM (25.242-b08) for linux-amd64 JRE (1.8.0_242-b08), built on "
                + "Jan 17 2020 09:36:23 by \"bob\" with gcc 4.4.7 20120313 (Red Hat 4.4.7-23";
        VmInfoEvent vmInfoEvent = new VmInfoEvent(vmInfo);
        fel.setVmInfoEvent(vmInfoEvent);
        assertEquals(JavaVendor.UNKNOWN, fel.getJavaVendor(), "JDK vendor not correct.");
    ***REMOVED***

    @Test
    void testWindows() {
        File testFile = new File(Constants.TEST_DATA_DIR + "dataset49.txt");
        Manager manager = new Manager();
        FatalErrorLog fel = manager.parse(testFile);
        long physicalMemory = JdkUtil.convertSize(16776740, 'K', Constants.PRECISION_REPORTING);
        assertEquals(physicalMemory, fel.getOsMemTotal(), "System physical memory not correct.");
        long physicalMemoryFree = JdkUtil.convertSize(674168, 'K', Constants.PRECISION_REPORTING);
        assertEquals(physicalMemoryFree, fel.getOsMemFree(), "System physical memory free not correct.");
        long swap = JdkUtil.convertSize(20970784, 'K', Constants.PRECISION_REPORTING);
        assertEquals(swap, fel.getOsSwap(), "System swap not correct.");
        long swapFree = JdkUtil.convertSize(5252, 'K', Constants.PRECISION_REPORTING);
        assertEquals(swapFree, fel.getOsSwapFree(), "System swap free not correct.");
    ***REMOVED***

    @Test
    void testWindowsOracleJdk8() {
        File testFile = new File(Constants.TEST_DATA_DIR + "dataset12.txt");
        Manager manager = new Manager();
        FatalErrorLog fel = manager.parse(testFile);
        assertTrue(fel.isWindows(), "OS not identified as Windows.");
        assertTrue(fel.getAnalysis().contains(Analysis.WARN_DEBUG_SYMBOLS),
                Analysis.WARN_DEBUG_SYMBOLS + " analysis not identified.");
        assertFalse(fel.getAnalysis().contains(Analysis.INFO_RH_BUILD_WINDOWS_ZIP),
                Analysis.INFO_RH_BUILD_WINDOWS_ZIP + " analysis incorrectly identified.");
        assertEquals(Arch.X86_64, fel.getArch(), "Arch not correct.");
        assertEquals("1.8.0_25-b18", fel.getJdkReleaseString(), "Jdk release not correct.");
        assertEquals(JavaVendor.ORACLE, fel.getJavaVendor(), "Java vendor not correct.");
    ***REMOVED***

    @Test
    void testWindowsRedHatJdk11() {
        File testFile = new File(Constants.TEST_DATA_DIR + "dataset13.txt");
        Manager manager = new Manager();
        FatalErrorLog fel = manager.parse(testFile);
        assertTrue(fel.isWindows(), "OS not identified as Windows.");
        assertTrue(fel.getAnalysis().contains(Analysis.WARN_DEBUG_SYMBOLS),
                Analysis.WARN_DEBUG_SYMBOLS + " analysis not identified.");
        assertTrue(fel.getAnalysis().contains(Analysis.INFO_RH_BUILD_WINDOWS_ZIP),
                Analysis.INFO_RH_BUILD_WINDOWS_ZIP + " analysis not identified.");
        assertEquals(Arch.X86_64, fel.getArch(), "Arch not correct.");
        assertEquals("11.0.7+10-LTS", fel.getJdkReleaseString(), "Jdk release not correct.");
        assertEquals(JavaVendor.RED_HAT, fel.getJavaVendor(), "Java vendor not correct.");
    ***REMOVED***
***REMOVED***