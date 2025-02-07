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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;

import org.github.joa.domain.Arch;
import org.github.joa.domain.GarbageCollector;
import org.github.joa.domain.Os;
import org.github.krashpad.service.Manager;
import org.github.krashpad.util.Constants;
import org.github.krashpad.util.Constants.Device;
import org.github.krashpad.util.Constants.OsVendor;
import org.github.krashpad.util.Constants.OsVersion;
import org.github.krashpad.util.KrashUtil;
import org.github.krashpad.util.jdk.Analysis;
import org.github.krashpad.util.jdk.JdkUtil;
import org.github.krashpad.util.jdk.JdkUtil.Application;
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
        VmArguments event = new VmArguments(logLine);
        FatalErrorLog fel = new FatalErrorLog();
        fel.getVmArguments().add(event);
        assertEquals(Application.AMQ_CLI, fel.getApplication(), "AMQ CLI application not identified.");
    }

    @Test
    void testAmqRun() {
        String logLine = "java_command: org.apache.activemq.artemis.boot.Artemis run";
        assertTrue(JdkUtil.identifyEventType(logLine, null) == JdkUtil.LogEventType.VM_ARGUMENTS,
                JdkUtil.LogEventType.VM_ARGUMENTS.toString() + " not identified.");
        VmArguments event = new VmArguments(logLine);
        FatalErrorLog fel = new FatalErrorLog();
        fel.getVmArguments().add(event);
        assertFalse(Application.AMQ_CLI == fel.getApplication(), "AMQ CLI application incorrectly identified.");
    }

    @Test
    void testAmqStart() {
        String logLine = "java_command: org.apache.activemq.artemis.boot.Artemis start";
        assertTrue(JdkUtil.identifyEventType(logLine, null) == JdkUtil.LogEventType.VM_ARGUMENTS,
                JdkUtil.LogEventType.VM_ARGUMENTS.toString() + " not identified.");
        VmArguments event = new VmArguments(logLine);
        FatalErrorLog fel = new FatalErrorLog();
        fel.getVmArguments().add(event);
        assertFalse(Application.AMQ_CLI == fel.getApplication(), "AMQ CLI application incorrectly identified.");
    }

    @Test
    void testAnonHugePages() {
        FatalErrorLog fel = new FatalErrorLog();
        String meminfo = "AnonHugePages:   5965824 kB";
        Meminfo meminfoEvent = new Meminfo(meminfo);
        fel.getMeminfos().add(meminfoEvent);
        long anonHugePages = JdkUtil.convertSize(5965824, 'K', 'B');
        assertEquals(anonHugePages, fel.getAnonHugePages(), "AnonHugePages not correct.");
    }

    @Test
    void testArchSparc() {
        File testFile = new File(Constants.TEST_DATA_DIR + "dataset65.txt");
        Manager manager = new Manager();
        FatalErrorLog fel = manager.parse(testFile);
        assertEquals(0, fel.getUnidentifiedLogLines().size(), "Unidentified log lines.");
        assertEquals(Arch.SPARC, fel.getArch(), "Arch not correct.");
        // No vm_info, so not possible to determine vendor
        assertEquals(JavaVendor.NOT_RED_HAT, fel.getJavaVendor(), "Java vendor not correct.");
        assertFalse(fel.hasAnalysis(Analysis.WARN_UNIDENTIFIED_LOG_LINE.getKey()),
                Analysis.WARN_UNIDENTIFIED_LOG_LINE + " analysis incorrectly identified.");
        assertTrue(fel.hasAnalysis(Analysis.INFO_VM_OPERATION_BULK_REVOKE_BIAS.getKey()),
                Analysis.INFO_VM_OPERATION_BULK_REVOKE_BIAS + " analysis not identified.");
        assertEquals(1, fel.getNativeLibraries().size(), "Native library count not correct.");
        assertEquals(0, fel.getNativeLibrariesUnknown().size(), "Native library unknown count not correct.");
    }

    @Test
    void testAws() {
        File testFile = new File(Constants.TEST_DATA_DIR + "dataset34.txt");
        Manager manager = new Manager();
        FatalErrorLog fel = manager.parse(testFile);
        assertEquals(0, fel.getUnidentifiedLogLines().size(), "Unidentified log lines.");
        assertEquals(JavaSpecification.JDK8, fel.getJavaSpecification(), "Java specification not correct.");
        assertEquals("java-1.8.0-openjdk-1.8.0.272.b10-1.el7_9.x86_64", fel.getRpmDirectory(),
                "Rpm directory not correct.");
        assertTrue(fel.isRhRpmInstall(), "RH rpm install not identified.");
        assertEquals(Device.AWS_BLOCK_STORAGE, fel.getStorageDevice(), "Storage device not correct.");
        assertTrue(fel.hasAnalysis(Analysis.INFO_RH_BUILD_RPM_INSTALL.getKey()),
                Analysis.INFO_RH_BUILD_RPM_INSTALL + " analysis not identified.");
        assertTrue(fel.hasAnalysis(Analysis.ERROR_BUFFERBLOB_FLUSH_ICACHE_STUB.getKey()),
                Analysis.ERROR_BUFFERBLOB_FLUSH_ICACHE_STUB + " analysis not identified.");
        assertEquals(1, fel.getNativeLibraries().size(), "Native library count not correct.");
        assertEquals(0, fel.getNativeLibrariesUnknown().size(), "Native library unknown count not correct.");
    }

    @Test
    void testAzul() {
        FatalErrorLog fel = new FatalErrorLog();
        String headerLine = "#   http://www.azulsystems.com/support/";
        Header headerEvent = new Header(headerLine);
        fel.getHeaders().add(headerEvent);
        assertEquals(JavaVendor.AZUL, fel.getJavaVendor(), "Java vendor not correct.");
        fel.doAnalysis();
        assertFalse(fel.hasAnalysis(Analysis.INFO_RH_BUILD_POSSIBLE.getKey()),
                Analysis.INFO_RH_BUILD_POSSIBLE + " analysis incorrectly identified.");
        assertFalse(fel.hasAnalysis(Analysis.INFO_OPTS_NONE.getKey()),
                Analysis.INFO_OPTS_NONE + " analysis incorrectly identified.");
        assertTrue(fel.hasAnalysis(Analysis.INFO_OPTS_UNKNOWN.getKey()),
                Analysis.INFO_OPTS_UNKNOWN + " analysis not identified.");
    }

    @Test
    void testCodeCacheSizeSegmented() {
        FatalErrorLog fel = new FatalErrorLog();
        String jvmArgs = "jvm_args: -XX:+SegmentedCodeCache -XX:NonNMethodCodeHeapSize=5825164 "
                + "-XX:NonProfiledCodeHeapSize=122916538 -XX:ProfiledCodeHeapSize=122916538";
        VmArguments vmArgumentsEvent = new VmArguments(jvmArgs);
        fel.getVmArguments().add(vmArgumentsEvent);
        fel.doAnalysis();
        long codeCacheSize = JdkUtil.convertSize(240, 'M', 'B');
        assertEquals(codeCacheSize, fel.getCodeCacheSize(), "Code cache size not correct.");
    }

    @Test
    void testCollectorNoData() {
        FatalErrorLog fel = new FatalErrorLog();
        fel.doAnalysis();
        assertEquals(1, fel.getGarbageCollectors().size(), "Garbage collector count not correct.");
        assertTrue(fel.getGarbageCollectors().contains(GarbageCollector.UNKNOWN), "Garbage collector not correct.");
    }

    @Test
    void testCollectorTruncatedLog() {
        FatalErrorLog fel = new FatalErrorLog();
        String headerLine1 = "# JRE version: OpenJDK Runtime Environment (8.0_322-b06) (build 1.8.0_322-b06)";
        Header headerEvent1 = new Header(headerLine1);
        fel.getHeaders().add(headerEvent1);
        String headerLine2 = "# Java VM: OpenJDK 64-Bit Server VM (25.322-b06 mixed mode linux-amd64 compressed oops)";
        Header headerEvent2 = new Header(headerLine2);
        fel.getHeaders().add(headerEvent2);
        fel.doAnalysis();
        assertEquals(1, fel.getGarbageCollectors().size(), "Garbage collector count not correct.");
        assertTrue(fel.getGarbageCollectors().contains(GarbageCollector.UNKNOWN), "Garbage collector not correct.");
    }

    @Test
    void testCompiledFrameWithPercent() {
        FatalErrorLog fel = new FatalErrorLog();
        String stack = "J 7241% C2 com.example.MyClass.match(Ljava/util/List;Ljava/util/List;Ljava/util/Comparator;"
                + "Ljava/util/Comparator;Ljava/util/Comparator;Lcom/example/IMatch;Ljava/lang/Object;)V (534 bytes) "
                + "@ 0x00002b7c5142e69c [0x00002b7c5142e340+0x35c]";
        Stack stackEvent = new Stack(stack);
        fel.getStacks().add(stackEvent);
        fel.doAnalysis();
        assertTrue(fel.hasAnalysis(Analysis.ERROR_COMPILED_JAVA_CODE.getKey()),
                Analysis.ERROR_COMPILED_JAVA_CODE + " analysis not identified.");
    }

    @Test
    void testCompressedOopMode() {
        FatalErrorLog fel = new FatalErrorLog();
        // BIT32
        String heapAddress = "heap address: 0x00000000c0000000, size: 1024 MB, Compressed Oops mode: 32-bit";
        HeapAddress heapAddressEvent = new HeapAddress(heapAddress);
        fel.setHeapAddress(heapAddressEvent);
        assertEquals(CompressedOopMode.BIT32, fel.getCompressedOopMode(), "Compressed oop mode not correct.");
        assertEquals(3221225472L, fel.getHeapStartingAddress(), "Heap starting address not correct.");
        assertEquals(1024L * 1024 * 1024, fel.getHeapMaxSize(), "Heap size not correct.");
        // ZERO
        heapAddress = "heap address: 0x00000003c0000000, size: 16384 MB, Compressed Oops mode: "
                + "Zero based, Oop shift amount: 3";
        heapAddressEvent = new HeapAddress(heapAddress);
        fel.setHeapAddress(heapAddressEvent);
        assertEquals(CompressedOopMode.ZERO, fel.getCompressedOopMode(), "Compressed oop mode not correct.");
        // NON_ZERO
        heapAddress = "heap address: 0x00000005a9c00000, size: 8548 MB, Compressed Oops mode: "
                + "Non-zero based:0x00000005a9bff000, Oop shift amount: 3";
        heapAddressEvent = new HeapAddress(heapAddress);
        fel.setHeapAddress(heapAddressEvent);
        assertEquals(CompressedOopMode.NON_ZERO, fel.getCompressedOopMode(), "Compressed oop mode not correct.");
        assertEquals(8548L * 1024 * 1024, fel.getHeapMaxSize(), "Heap max size not correct.");
    }

    @Test
    void testCpuCapilityAvx2() {
        FatalErrorLog fel = new FatalErrorLog();
        String cpu = "CPU:total 8 (2 cores per cpu, 1 threads per core) family 6 model 63 stepping 0, cmov, cx8, "
                + "fxsr, mmx, sse, sse2, sse3, ssse3, sse4.1, sse4.2, popcnt, avx, avx2, aes, erms, tsc, tscinvbit";
        CpuInfo cpuInfoEvent = new CpuInfo(cpu);
        fel.getCpuInfos().add(cpuInfoEvent);
        assertTrue(fel.hasCpuCapability("avx2"), "CPU avx2 capability not identified.");
        // test when at end
        fel.getCpuInfos().clear();
        cpu = "CPU:total 8 (2 cores per cpu, 1 threads per core) family 6 model 63 stepping 0, cmov, cx8, "
                + "fxsr, mmx, sse, sse2, sse3, ssse3, sse4.1, sse4.2, popcnt, avx, avx2";
        cpuInfoEvent = new CpuInfo(cpu);
        fel.getCpuInfos().add(cpuInfoEvent);
        assertTrue(fel.hasCpuCapability("avx2"), "CPU avx2 capability not identified.");
    }

    @Test
    void testCrashShutdown() {
        FatalErrorLog fel = new FatalErrorLog();
        String logLine = "Event: 5353.018 Executing VM operation: Exit";
        Event event = new Event(logLine);
        fel.getEvents().add(event);
        fel.doAnalysis();
        assertTrue(fel.hasAnalysis(Analysis.INFO_SHUTDOWN.getKey()),
                Analysis.INFO_SHUTDOWN + " analysis not identified.");
    }

    @Test
    void testCurrentThread() {
        File testFile = new File(Constants.TEST_DATA_DIR + "dataset20.txt");
        Manager manager = new Manager();
        FatalErrorLog fel = manager.parse(testFile);
        assertEquals(0, fel.getUnidentifiedLogLines().size(), "Unidentified log lines.");
        assertEquals(
                "JavaThread \"ajp-/hostname:8109-16\" daemon [_thread_in_native, id=112672, "
                        + "stack(0x00007f11e11a2000,0x00007f11e12a3000)]",
                fel.getCurrentThreadName(), "Current thread not correct.");
    }

    @Test
    void testDatesJdk11() {
        FatalErrorLog fel = new FatalErrorLog();
        String logLine = "Time: Sun Nov 14 14:25:00 2021 UTC elapsed time: 5697278.196357 seconds (65d 22h 34m 38s)";
        TimeElapsedTime event = new TimeElapsedTime(logLine);
        fel.setTimeElapsedTime(event);
        assertEquals("Sun Nov 14 14:25:00 2021 UTC", fel.getCrashTimeString(), "Crash time not correct.");
        assertEquals("65d 22h 34m 38s", fel.getElapsedTime(), "Elapsed time not correct.");
    }

    @Test
    void testDatesJdk8() {
        FatalErrorLog fel = new FatalErrorLog();
        Time timeEvent = new Time("time: Tue Nov 23 09:21:06 2021");
        fel.setTime(timeEvent);
        Timezone timezoneEvent = new Timezone("timezone: CET");
        fel.setTimezone(timezoneEvent);
        ElapsedTime elapsedTimeEvent = new ElapsedTime("elapsed time: 644647 seconds (7d 11h 4m 7s)");
        fel.setElapsedTime(elapsedTimeEvent);
        assertEquals("Tue Nov 23 09:21:06 2021 CET", fel.getCrashTimeString(), "Crash time not correct.");
        assertEquals("7d 11h 4m 7s", fel.getElapsedTime(), "Elapsed time not correct.");
    }

    @Test
    void testDebugSymbolsNoVmCodeInStack() {
        File testFile = new File(Constants.TEST_DATA_DIR + "dataset5.txt");
        Manager manager = new Manager();
        FatalErrorLog fel = manager.parse(testFile);
        assertEquals(0, fel.getUnidentifiedLogLines().size(), "Unidentified log lines.");
        assertFalse(fel.hasAnalysis(Analysis.WARN_UNIDENTIFIED_LOG_LINE.getKey()),
                Analysis.WARN_UNIDENTIFIED_LOG_LINE + " analysis incorrectly identified.");
        assertFalse(fel.isRhel(), "OS incorrectly identified as RHEL.");
        assertFalse(fel.hasAnalysis(Analysis.WARN_DEBUG_SYMBOLS.getKey()),
                Analysis.WARN_DEBUG_SYMBOLS + " analysis incorrectly identified.");
        assertFalse(fel.hasAnalysis(Analysis.INFO_RH_BUILD_RPM_INSTALL.getKey()),
                Analysis.INFO_RH_BUILD_RPM_INSTALL + " analysis incorrectly identified.");
        // It's a CentOS build, not a RH build.
        assertFalse(fel.hasAnalysis(Analysis.INFO_RH_BUILD_CENTOS.getKey()),
                Analysis.INFO_RH_BUILD_CENTOS + " analysis incorrectly identified.");
        assertFalse(fel.hasAnalysis(Analysis.INFO_STACK_NO_VM_CODE.getKey()),
                Analysis.INFO_STACK_NO_VM_CODE + " analysis incorrectly identified.");
    }

    @Test
    void testDirectMemory() {
        File testFile = new File(Constants.TEST_DATA_DIR + "dataset50.txt");
        Manager manager = new Manager();
        FatalErrorLog fel = manager.parse(testFile);
        assertEquals(0, fel.getUnidentifiedLogLines().size(), "Unidentified log lines.");
        long heapInitial = JdkUtil.convertSize(96, 'G', 'B');
        assertEquals(heapInitial, fel.getHeapInitialSize(), "Heap initial size not correct.");
        long heapMax = JdkUtil.convertSize(96, 'G', 'B');
        assertEquals(heapMax, fel.getHeapMaxSize(), "Heap max size not correct.");
        long heapAllocation = JdkUtil.convertSize(96, 'G', 'B');
        assertEquals(heapAllocation, fel.getHeapAllocation(), "Heap allocation not correct.");
        long heapUsed = JdkUtil.convertSize(78973, 'M', 'B');
        assertEquals(heapUsed, fel.getHeapUsed(), "Heap used not correct.");
        long metaspaceMax = JdkUtil.convertSize(182272, 'K', 'B');
        assertEquals(metaspaceMax, fel.getMetaspaceMaxSize(), "Metaspace max size not correct.");
        long metaspaceAllocation = JdkUtil.convertSize(180428, 'K', 'B');
        assertEquals(metaspaceAllocation, fel.getMetaspaceAllocation(), "Metaspace allocation not correct.");
        long metaspaceUsed = JdkUtil.convertSize(176392, 'K', 'B');
        assertEquals(metaspaceUsed, fel.getMetaspaceUsed(), "Metaspace used not correct.");
        long directMemoryMax = JdkUtil.convertSize(8, 'G', 'B');
        assertEquals(directMemoryMax, fel.getDirectMemoryMaxSize(), "Direct Memory max not correct.");
        assertEquals(1024, fel.getThreadStackSize(), "Thread stack size not correct.");
        assertEquals(25, fel.getJavaThreadCount(), "Thread count not correct.");
        long threadMemory = JdkUtil.convertSize(1024 * 25, 'K', 'B');
        assertEquals(threadMemory, fel.getThreadStackMemory(), "Thread memory not correct.");
        long codeCacheSize = JdkUtil.convertSize(420, 'M', 'B');
        assertEquals(codeCacheSize, fel.getCodeCacheSize(), "Code cache size not correct.");
        assertEquals(heapInitial + metaspaceMax + directMemoryMax + threadMemory + codeCacheSize,
                fel.getJvmMemoryInitial(), "Jvm memory max not correct.");
        assertEquals(heapMax + metaspaceMax + directMemoryMax + threadMemory + codeCacheSize, fel.getJvmMemoryMax(),
                "Jvm memory max not correct.");
        assertEquals(1, fel.getNativeLibraries().size(), "Native library count not correct.");
        assertEquals(0, fel.getNativeLibrariesUnknown().size(), "Native library unknown count not correct.");
    }

    @Test
    void testDynamicLibraryMappings() {
        File testFile = new File(Constants.TEST_DATA_DIR + "dataset26.txt");
        Manager manager = new Manager();
        FatalErrorLog fel = manager.parse(testFile);
        assertEquals(1314, fel.getDynamicLibrariesMappingCount(), "Mappings count not correct.");
        assertEquals(874, fel.getDynamicLibraries().size(), "Dynamic library count not correct.");
    }

    @Test
    void testG1() {
        File testFile = new File(Constants.TEST_DATA_DIR + "dataset32.txt");
        Manager manager = new Manager();
        FatalErrorLog fel = manager.parse(testFile);
        assertEquals(0, fel.getUnidentifiedLogLines().size(), "Unidentified log lines.");
        assertFalse(fel.hasAnalysis(Analysis.WARN_UNIDENTIFIED_LOG_LINE.getKey()),
                Analysis.WARN_UNIDENTIFIED_LOG_LINE + " analysis incorrectly identified.");
        assertEquals("Tue May  5 18:32:04 2020 CEST", fel.getCrashTimeString(), "Crash time not correct.");
        assertEquals("0d 0h 15m 56s", fel.getElapsedTime(), "Elapsed time not correct.");
        long physicalMemory = JdkUtil.convertSize(32780544, 'K', 'B');
        assertEquals(physicalMemory, fel.getJvmMemTotal(), "Physical memory not correct.");
        long physicalMemoryFree = JdkUtil.convertSize(2698868, 'K', 'B');
        assertEquals(physicalMemoryFree, fel.getJvmMemFree(), "Physical memory free not correct.");
        long swap = JdkUtil.convertSize(8191996, 'K', 'B');
        assertEquals(swap, fel.getJvmSwap(), "Swap not correct.");
        long swapFree = JdkUtil.convertSize(8190972, 'K', 'B');
        assertEquals(swapFree, fel.getJvmSwapFree(), "Swap free not correct.");
        long heapInitial = JdkUtil.convertSize(1073741824, 'B', 'B');
        assertEquals(heapInitial, fel.getHeapInitialSize(), "Heap initial size not correct.");
        long heapMax = JdkUtil.convertSize(12884901888L, 'B', 'B');
        assertEquals(heapMax, fel.getHeapMaxSize(), "Heap max size not correct.");
        long heapAllocation = JdkUtil.convertSize(1933312, 'K', 'B');
        assertEquals(heapAllocation, fel.getHeapAllocation(), "Heap allocation not correct.");
        long heapUsed = JdkUtil.convertSize(1030565, 'K', 'B');
        assertEquals(heapUsed, fel.getHeapUsed(), "Heap used not correct.");
        long metaspaceMax = JdkUtil.convertSize(1189888, 'K', 'B');
        assertEquals(metaspaceMax, fel.getMetaspaceMaxSize(), "Metaspace max size not correct.");
        long metaspaceAllocation = JdkUtil.convertSize(159168, 'K', 'B');
        assertEquals(metaspaceAllocation, fel.getMetaspaceAllocation(), "Metaspace allocation not correct.");
        long metaspaceUsed = JdkUtil.convertSize(147896, 'K', 'B');
        assertEquals(metaspaceUsed, fel.getMetaspaceUsed(), "Metaspace used not correct.");
        assertEquals(8, fel.getCpusLogical(), "CPU cores not correct.");
        assertEquals(5 * 1024, fel.getThreadStackSize(), "Thread stack size not correct.");
        assertEquals(805306368L, fel.getCompressedClassSpaceSize(), "CompressedClassSpaceSize not correct.");
        assertEquals(1073741824L, fel.getDirectMemoryMaxSize(), "MaxDirectMemorySize not correct.");
        assertEquals(255838, fel.getThreadsMaxLimit(), "threads-max not correct.");
        assertEquals(32768, fel.getPidMaxLimit(), "pid_max not correct.");
        assertEquals(65530, fel.getMaxMapCountLimit(), "max_map_count not correct.");
        assertTrue(fel.hasAnalysis(Analysis.INFO_VM_OPERATION_THREAD_DUMP.getKey()),
                Analysis.INFO_VM_OPERATION_THREAD_DUMP + " analysis not identified.");
        assertEquals(1, fel.getNativeLibraries().size(), "Native library count not correct.");
        assertEquals(0, fel.getNativeLibrariesUnknown().size(), "Native library unknown count not correct.");
    }

    @Test
    void testGarbageCollectionHydration() {
        File testFile = new File(Constants.TEST_DATA_DIR + "dataset26.txt");
        Manager manager = new Manager();
        FatalErrorLog fel = manager.parse(testFile);
        assertEquals(0, fel.getUnidentifiedLogLines().size(), "Unidentified log lines.");
        assertEquals(24, fel.getGarbageCollections().size(), "Garbage collection count not correct.");
        assertEquals(1905, fel.getGarbageCollections().get(0).getTimestampStartGc(),
                "First GC begin timestamp not correct.");
        assertEquals(6669456,
                fel.getGarbageCollections().get(fel.getGarbageCollections().size() - 1).getTimestampEndGc(),
                "Last GC after timestamp not correct.");
        assertEquals(317, fel.getGarbageCollectionDurationMax(), "GC duration max not correct.");
        assertEquals(1599, fel.getGarbageCollectionDurationTotal(), "GC duration total not correct.");
        assertEquals(100, fel.getGarbageCollectionThroughput(), "GC throughput not correct.");
    }

    @Test
    void testGetRhReleaseFromBuildString() {
        FatalErrorLog fel = new FatalErrorLog();
        String headerLine1 = "# JRE version: OpenJDK Runtime Environment (8.0_191-b12) (build 1.8.0_191-b12)";
        Header headerEvent1 = new Header(headerLine1);
        fel.getHeaders().add(headerEvent1);
        String headerLine2 = "# Java VM: OpenJDK 64-Bit Server VM (25.191-bl2 mixed mode linux-amdé4 compressed oops)";
        Header headerEvent2 = new Header(headerLine2);
        fel.getHeaders().add(headerEvent2);
        assertEquals("1.8.0_191-b12", fel.getJdkReleaseString(), "JDK release string not correct.");
        fel.doAnalysis();
        Release release = fel.getFirstRelease(fel.getJdkReleaseString());
        assertNotNull(release, "Red Hat release not identified.");
        assertEquals(KrashUtil.getDate("Oct 09 2018 00:00:00"), release.getBuildDate(), "Build date not correct.");
        assertTrue(fel.hasAnalysis(Analysis.INFO_JDK_ANCIENT.getKey()),
                Analysis.INFO_JDK_ANCIENT + " analysis not identified.");
        assertTrue(
                fel.getAnalysisLiteral(Analysis.INFO_JDK_ANCIENT.getKey())
                        .matches("^The JDK is very old \\(\\d{1,}\\.\\d years\\)\\..+$"),
                Analysis.INFO_JDK_ANCIENT + " not correct.");
    }

    @Test
    void testGraal() {
        FatalErrorLog fel = new FatalErrorLog();
        String vmInfo = "vm_info: OpenJDK 64-Bit Server VM (11.0.15+10-jvmci-22.1-b06) for linux-amd64 JRE "
                + "(11.0.15+10-jvmci-22.1-b06), built on Apr 20 2022 15:31:40 by \"buildslave\" with gcc 7.3.0";
        VmInfo vmInfoEvent = new VmInfo(vmInfo);
        fel.setVmInfo(vmInfoEvent);
        assertFalse(fel.isRhBuildString(), "RH build string identified.");
    }

    @Test
    void testHaveDebuggingSymbols() {
        FatalErrorLog fel = new FatalErrorLog();
        String headerLine = "# V  [libjvm.so+0xa333a6]  ShenandoahUpdateRefsClosure::do_oop(oopDesc**)+0x26";
        Header he = new Header(headerLine);
        fel.getHeaders().add(he);
        assertTrue(fel.haveJdkDebugSymbols(), "Debugging symbols not identified.");
    }

    @Test
    void testHeaderArch() {
        FatalErrorLog fel = new FatalErrorLog();
        String headerLine = "# Java VM: Java HotSpot(TM) 64-Bit Server VM (24.85-b06 mixed mode linux-amd64 compressed "
                + "oops)";
        Header he = new Header(headerLine);
        fel.getHeaders().add(he);
        assertEquals(Arch.X86_64, fel.getArch(), "Arch not correct.");
    }

    @Test
    void testHeaderJdkVersion() {
        FatalErrorLog fel = new FatalErrorLog();
        String headerLine = "# JRE version:  (7.0_85-b15) (build )";
        Header he = new Header(headerLine);
        fel.getHeaders().add(he);
        assertEquals(JavaSpecification.JDK7, fel.getJavaSpecification(), "Java specification not correct.");
        assertEquals("1.7.0_85-b15", fel.getJdkReleaseString(), "Java release not correct.");
    }

    @Test
    void testHeaderNotRhVersion() {
        FatalErrorLog fel = new FatalErrorLog();
        String headerLine1 = "# JRE version: Java(TM) SE Runtime Environment (8.0_301-b09) (build 1.8.0_301-b09)";
        Header he1 = new Header(headerLine1);
        fel.getHeaders().add(he1);
        String headerLine2 = "# Java VM: Java HotSpot(TM) 64-Bit Server VM (25.301-b09 mixed mode linux-amd64 "
                + "compressed oops)";
        Header he2 = new Header(headerLine2);
        fel.getHeaders().add(he2);
        assertEquals(Os.LINUX, fel.getOs(), "OS type not correct.");
        assertEquals("1.8.0_301-b09", fel.getJdkReleaseString(), "Java release not correct.");
        assertEquals(JavaVendor.NOT_RED_HAT, fel.getJavaVendor(), "Java vendor not correct.");
    }

    @Test
    void testHeaderOsVersion() {
        FatalErrorLog fel = new FatalErrorLog();
        String headerLine = "# Java VM: Java HotSpot(TM) 64-Bit Server VM (24.85-b06 mixed mode linux-amd64 compressed "
                + "oops)";
        Header he = new Header(headerLine);
        fel.getHeaders().add(he);
        assertEquals(OsVersion.UNIDENTIFIED, fel.getOsVersion(), "OS version not correct.");
        assertEquals(Os.LINUX, fel.getOs(), "OS type not correct.");
    }

    @Test
    void testHeapAllocation() {
        FatalErrorLog fel = new FatalErrorLog();
        String event1 = "Heap:";
        Heap heapEvent1 = new Heap(event1);
        fel.getHeaps().add(heapEvent1);
        String event2 = " 3456M max, 3456M soft max, 3200M committed, 2325M used";
        Heap heapEvent2 = new Heap(event2);
        fel.getHeaps().add(heapEvent2);
        assertEquals(3200L * 1024 * 1024, fel.getHeapAllocation(), "Heap allocation not correct.");
    }

    @Test
    void testHeapMaxGlobalFlag() {
        File testFile = new File(Constants.TEST_DATA_DIR + "dataset41.txt");
        Manager manager = new Manager();
        FatalErrorLog fel = manager.parse(testFile);
        assertEquals(0, fel.getUnidentifiedLogLines().size(), "Unidentified log lines.");
        long heapInitial = JdkUtil.convertSize(32, 'G', 'B');
        assertEquals(heapInitial, fel.getHeapInitialSize(), "Heap initial size not correct.");
        long heapMax = JdkUtil.convertSize(33554432, 'K', 'B');
        assertEquals(heapMax, fel.getHeapMaxSize(), "Heap max size not correct.");
        long codeCacheSize = JdkUtil.convertSize(251658240, 'B', 'B');
        assertEquals(codeCacheSize, fel.getCodeCacheSize(), "Code cache size not correct.");
        long jvmMemoryMax = JdkUtil.convertSize(33554432 + 245760, 'K', 'B');
        assertEquals(jvmMemoryMax, fel.getJvmMemoryMax(), "Jvm memory max not correct.");
        assertEquals(1, fel.getNativeLibraries().size(), "Native library count not correct.");
        assertEquals(0, fel.getNativeLibrariesUnknown().size(), "Native library unknown count not correct.");
    }

    @Test
    void testHugepagesize() {
        FatalErrorLog fel = new FatalErrorLog();
        String meminfo = "Hugepagesize:       2048 kB";
        Meminfo meminfoEvent = new Meminfo(meminfo);
        fel.getMeminfos().add(meminfoEvent);
        long hugepagesize = JdkUtil.convertSize(2048, 'K', 'B');
        assertEquals(hugepagesize, fel.getHugepagesize(), "Hugepagesize not correct.");
    }

    @Test
    void testHugepagesize0() {
        FatalErrorLog fel = new FatalErrorLog();
        String meminfo = "Hugepagesize:       0 kB";
        Meminfo meminfoEvent = new Meminfo(meminfo);
        fel.getMeminfos().add(meminfoEvent);
        assertEquals(0, fel.getHugepagesize(), "Hugepagesize not correct.");
    }

    @Test
    void testHugePagesPoolSize0Rhel7() {
        FatalErrorLog fel = new FatalErrorLog();
        String meminfo1 = "Hugepagesize:       2048 kB";
        Meminfo meminfoEvent1 = new Meminfo(meminfo1);
        fel.getMeminfos().add(meminfoEvent1);
        String meminfo2 = "HugePages_Total:   0";
        Meminfo meminfoEvent2 = new Meminfo(meminfo2);
        fel.getMeminfos().add(meminfoEvent2);
        assertEquals(0, fel.getExplicitHugePagesPoolSize(), "Explicit Huge Pages Pool Size not correct.");
    }

    @Test
    void testHugePagesPoolSizeRhel7() {
        FatalErrorLog fel = new FatalErrorLog();
        String meminfo1 = "Hugepagesize:       2048 kB";
        Meminfo meminfoEvent1 = new Meminfo(meminfo1);
        fel.getMeminfos().add(meminfoEvent1);
        String meminfo2 = "HugePages_Total:   696418";
        Meminfo meminfoEvent2 = new Meminfo(meminfo2);
        fel.getMeminfos().add(meminfoEvent2);
        assertEquals(696418L * 2048L * 1024L, fel.getExplicitHugePagesPoolSize(),
                "Explicit Huge Pages Pool Size not correct.");
    }

    @Test
    void testHugePagesTotal() {
        FatalErrorLog fel = new FatalErrorLog();
        String meminfo = "HugePages_Total:   696418";
        Meminfo meminfoEvent = new Meminfo(meminfo);
        fel.getMeminfos().add(meminfoEvent);
        assertEquals(696418L, fel.getHugePagesTotal(), "HugePages_Total not correct.");
    }

    @Test
    void testHugetlb() {
        FatalErrorLog fel = new FatalErrorLog();
        String meminfo = "Hugetlb:         4194304 kB";
        Meminfo meminfoEvent = new Meminfo(meminfo);
        fel.getMeminfos().add(meminfoEvent);
        long hugetlb = JdkUtil.convertSize(4194304, 'K', 'B');
        assertEquals(hugetlb, fel.getHugetlb(), "Hugetlb not correct.");
    }

    @Test
    void testInternalError() {
        File testFile = new File(Constants.TEST_DATA_DIR + "dataset3.txt");
        Manager manager = new Manager();
        FatalErrorLog fel = manager.parse(testFile);
        assertEquals(0, fel.getUnidentifiedLogLines().size(), "Unidentified log lines.");
        String causedBy = "#  Internal Error (ciEnv.hpp:172), pid=6570, tid=0x00007fe3d7dfd700"
                + Constants.LINE_SEPARATOR + "#  Error: ShouldNotReachHere()";
        assertEquals(causedBy, fel.getError(), "Caused by incorrect.");
        assertTrue(fel.haveJdkDebugSymbols(), "Debugging symbols incorrectly identified.");
    }

    @Test
    void testJavaThreadCount() {
        File testFile = new File(Constants.TEST_DATA_DIR + "dataset26.txt");
        Manager manager = new Manager();
        FatalErrorLog fel = manager.parse(testFile);
        assertEquals(0, fel.getUnidentifiedLogLines().size(), "Unidentified log lines.");
        assertEquals(2, fel.getJavaThreadCount(), "Java thread count not correct.");
        assertEquals(168, fel.getNativeLibraries().size(), "Native library count not correct.");
        assertEquals(7, fel.getNativeLibrariesUnknown().size(), "Native library unknown count not correct.");
    }

    @Test
    void testJBoss() {
        String logLine = "java_command: /path/to/jboss-modules.jar -Djboss.home.dir=/path/to/standalone";
        assertTrue(JdkUtil.identifyEventType(logLine, null) == JdkUtil.LogEventType.VM_ARGUMENTS,
                JdkUtil.LogEventType.VM_ARGUMENTS.toString() + " not identified.");
        VmArguments event = new VmArguments(logLine);
        FatalErrorLog fel = new FatalErrorLog();
        fel.getVmArguments().add(event);
        assertEquals(Application.WILDFLY, fel.getApplication(), "JBoss application not identified.");
    }

    @Test
    void testJdk21Lts() {
        FatalErrorLog fel = new FatalErrorLog();
        String vmInfo = "vm_info: OpenJDK 64-Bit Server VM (21.0.1+12-LTS) for linux-amd64 JRE (21.0.1+12-LTS), built "
                + "on 2023-10-30T00:33:46Z by \"mockbuild\" with gcc 10.2.1 20210130 (Red Hat 10.2.1-11)";
        VmInfo vmInfoEvent = new VmInfo(vmInfo);
        fel.setVmInfo(vmInfoEvent);
        assertEquals(JavaSpecification.JDK21, fel.getJavaSpecification(), "Java specification not correct.");
        assertTrue(fel.isJdkLts(), "LTS release not identified.");
    }

    @Test
    void testJdk8NotRhVersion() {
        FatalErrorLog fel = new FatalErrorLog();
        String os = "OS: Windows 10.0 , 64 bit Build 17763 (10.0.17763.2028)";
        OsInfo osEvent = new OsInfo(os);
        fel.getOsInfos().add(osEvent);
        String vmInfo = "vm_info: Java HotSpot(TM) 64-Bit Server VM (25.271-b09) for windows-amd64 JRE "
                + "(1.8.0_271-b09), built on Sep 16 2020 19:14:59 by \"\" with MS VC++ 15.9 (VS2017)";
        VmInfo vmInfoEvent = new VmInfo(vmInfo);
        fel.setVmInfo(vmInfoEvent);
        fel.doAnalysis();
        assertFalse(fel.hasAnalysis(Analysis.INFO_RH_BUILD_POSSIBLE.getKey()),
                Analysis.INFO_RH_BUILD_POSSIBLE + " analysis incorrectly identified.");
    }

    @Test
    void testJdk8Rhel7GenericOs() {
        FatalErrorLog fel = new FatalErrorLog();
        String os = "OS: Linux";
        OsInfo osEvent = new OsInfo(os);
        fel.getOsInfos().add(osEvent);
        String uname = "uname:Linux 3.10.0-514.6.1.el7.x86_64 #1 SMP Sat Dec 10 11:15:38 EST 2016 x86_6";
        Uname unameEvent = new Uname(uname);
        fel.setUname(unameEvent);
        assertEquals("Linux", fel.getOsString(), "OS string not correct.");
        assertEquals(OsVersion.RHEL7, fel.getOsVersion(), "OS version not correct.");
    }

    @Test
    void testJdkRedHatBuildUnknown() {
        File testFile = new File(Constants.TEST_DATA_DIR + "dataset6.txt");
        Manager manager = new Manager();
        FatalErrorLog fel = manager.parse(testFile);
        assertEquals(0, fel.getUnidentifiedLogLines().size(), "Unidentified log lines.");
        assertTrue(fel.isRhel(), "OS not identified as RHEL.");
        assertTrue(fel.isRhRpmInstall(), "Red Hat rpm install not identified.");
        assertEquals(OsVersion.RHEL6, fel.getOsVersion(), "OS version not correct.");
        assertTrue(fel.hasAnalysis(Analysis.WARN_RHEL6.getKey()), Analysis.WARN_RHEL6 + " analysis not identified.");
        assertEquals(1, fel.getNativeLibraries().size(), "Native library count not correct.");
        assertEquals(0, fel.getNativeLibrariesUnknown().size(), "Native library unknown count not correct.");
    }

    @Test
    void testJeusCommandLine() {
        String logLine = "java_command: jeus.server.ServerBootstrapper -domain mydomain -u myuser -server myserver";
        assertTrue(JdkUtil.identifyEventType(logLine, null) == JdkUtil.LogEventType.VM_ARGUMENTS,
                JdkUtil.LogEventType.VM_ARGUMENTS.toString() + " not identified.");
        VmArguments event = new VmArguments(logLine);
        FatalErrorLog fel = new FatalErrorLog();
        fel.getVmArguments().add(event);
        assertEquals(Application.JEUS, fel.getApplication(), "JEUS application not identified.");
    }

    @Test
    void testJeusDynamicLibrary() {
        DynamicLibrary priorLogEvent = new DynamicLibrary(null);
        String dynamicLibrary = "7ff41c726000-7ff41c764000 r--s 004dd000 fd:02 9611207                    "
                + "/path/to/jeus.jar";
        assertTrue(JdkUtil.identifyEventType(dynamicLibrary, priorLogEvent) == JdkUtil.LogEventType.DYNAMIC_LIBRARY,
                JdkUtil.LogEventType.DYNAMIC_LIBRARY.toString() + " not identified.");
        DynamicLibrary event = new DynamicLibrary(dynamicLibrary);
        FatalErrorLog fel = new FatalErrorLog();
        fel.getDynamicLibraries().add(event);
        assertEquals(Application.JEUS, fel.getApplication(), "JEUS application not identified.");
    }

    @Test
    void testJeusThreadDomainAdminServer() {
        String thread = "  0x00007f4168025000 JavaThread \"jeus.server.admin.DomainAdminServer\" [_thread_blocked, "
                + "id=75, stack(0x00007f41715c9000,0x00007f41716ca000)]";
        assertTrue(JdkUtil.identifyEventType(thread, null) == JdkUtil.LogEventType.THREAD,
                JdkUtil.LogEventType.THREAD.toString() + " not identified.");
        Thread event = new Thread(thread);
        FatalErrorLog fel = new FatalErrorLog();
        fel.getThreads().add(event);
        assertEquals(Application.JEUS, fel.getApplication(), "JEUS application not identified.");
    }

    @Test
    void testJeusThreadServer() {
        String thread = "  0x00007ff434057000 JavaThread \"jeus.server.Server\" [_thread_blocked, id=2663909, "
                + "stack(0x00007ff43b657000,0x00007ff43b757000)]";
        assertTrue(JdkUtil.identifyEventType(thread, null) == JdkUtil.LogEventType.THREAD,
                JdkUtil.LogEventType.THREAD.toString() + " not identified.");
        Thread event = new Thread(thread);
        FatalErrorLog fel = new FatalErrorLog();
        fel.getThreads().add(event);
        assertEquals(Application.JEUS, fel.getApplication(), "JEUS application not identified.");
    }

    @Test
    void testJlinkCustomerJreFromRpm() {
        FatalErrorLog fel = new FatalErrorLog();
        String os = "OS:Red Hat Enterprise Linux Server release 7.7 (Maipo)";
        OsInfo osEvent = new OsInfo(os);
        fel.getOsInfos().add(osEvent);
        String vmInfo = "vm_info: OpenJDK 64-Bit Server VM (11.0.9.1+1-LTS) for linux-amd64 JRE (11.0.9.1+1-LTS), "
                + "built on Nov 12 2020 18:10:11 by \"mockbuild\" with gcc 4.8.5 20150623 (Red Hat 4.8.5-44";
        VmInfo vmInfoEvent = new VmInfo(vmInfo);
        fel.setVmInfo(vmInfoEvent);
        fel.doAnalysis();
        assertEquals(Os.LINUX, fel.getOs(), "OS not correct.");
        assertEquals(OsVersion.RHEL7, fel.getOsVersion(), "OS version not correct.");
        assertTrue(fel.isRhRpm(), "Red Hat rpm not identified.");
        assertFalse(fel.hasAnalysis(Analysis.INFO_RH_BUILD_RPM_INSTALL.getKey()),
                Analysis.INFO_RH_BUILD_RPM_INSTALL + " analysis incorrectly identified.");
        assertFalse(fel.hasAnalysis(Analysis.INFO_RH_BUILD_CENTOS.getKey()),
                Analysis.INFO_RH_BUILD_CENTOS + " analysis incorrectly identified.");
        assertTrue(fel.hasAnalysis(Analysis.INFO_RH_BUILD_RPM_BASED.getKey()),
                Analysis.INFO_RH_BUILD_RPM_BASED + " analysis not identified.");
    }

    @Test
    void testJvmArgsFromCommandLine() {
        FatalErrorLog fel = new FatalErrorLog();
        String jvmArgs = "-Xmx2048m -Xmx12G -Xms1G";
        String javaCommand = "";
        String logLine = "Command Line: " + jvmArgs + " " + javaCommand;
        CommandLine commandLineEvent = new CommandLine(logLine);
        fel.setCommandLine(commandLineEvent);
        assertEquals(jvmArgs, fel.getJvmArgs(), "JVM options not correct.");
    }

    @Test
    void testJvmUser() {
        File testFile = new File(Constants.TEST_DATA_DIR + "dataset26.txt");
        Manager manager = new Manager();
        FatalErrorLog fel = manager.parse(testFile);
        assertEquals(0, fel.getUnidentifiedLogLines().size(), "Unidentified log lines.");
        assertEquals("user123", fel.getJvmUser(), "JVM user not correct.");
    }

    @Test
    void testJvmUserWithDotAndAtSign() {
        FatalErrorLog fel = new FatalErrorLog();
        String hsperfdata = "7f23f0838000-7f23f0840000 rw-s 00000000 fd:05 3430                       "
                + "/tmp/hsperfdata_first.last@location/12345";
        DynamicLibrary dynamicLibraryEvent = new DynamicLibrary(hsperfdata);
        fel.getDynamicLibraries().add(dynamicLibraryEvent);
        assertEquals("first.last@location", fel.getJvmUser(), "JVM user not correct.");
    }

    @Test
    void testJvmUserWithUnderscore() {
        FatalErrorLog fel = new FatalErrorLog();
        String hsperfdata = "7ff0f61d2000-7ff0f61da000 rw-s 00000000 fd:01 33563495                   "
                + "/tmp/hsperfdata_jb_admin/92333";
        DynamicLibrary dynamicLibraryEvent = new DynamicLibrary(hsperfdata);
        fel.getDynamicLibraries().add(dynamicLibraryEvent);
        assertEquals("jb_admin", fel.getJvmUser(), "JVM user not correct.");
    }

    @Test
    void testJvmVersionFromRpmPath() {
        FatalErrorLog fel = new FatalErrorLog();
        String os = "OS:Red Hat Enterprise Linux Server release 7.9 (Maipo)";
        OsInfo osEvent = new OsInfo(os);
        fel.getOsInfos().add(osEvent);
        String dynamicLibrary = "7efc12c7c000-7efc12ca7000 rw-p 00e4c000 fd:98 12596136                   "
                + "/usr/lib/jvm/java-1.8.0-openjdk-1.8.0.312.b07-1.el7_9.x86_64/jre/lib/amd64/server/libjvm.so";
        DynamicLibrary dynamicLibraryEvent = new DynamicLibrary(dynamicLibrary);
        fel.getDynamicLibraries().add(dynamicLibraryEvent);
        String uname = "uname:Linux 3.10.0-957.27.2.el7.x86_64 #1 SMP Tue Jul 9 16:53:14 UTC 2019 x86_64";
        Uname unameEvent = new Uname(uname);
        fel.setUname(unameEvent);
        fel.doAnalysis();
        assertEquals(OsVersion.RHEL7, fel.getOsVersion(), "OS version not correct.");
        assertEquals(Arch.X86_64, fel.getArch(), "Arch not correct.");
        assertEquals(JavaSpecification.JDK8, fel.getJavaSpecification(), "Java specification not correct.");
        assertEquals("java-1.8.0-openjdk-1.8.0.312.b07-1.el7_9.x86_64", fel.getRpmDirectory(),
                "RPM directory not correct.");
        assertEquals(JavaSpecification.JDK8, fel.getJavaSpecification(), "Java specification not correct.");
        assertEquals(Constants.PROPERTY_UNKNOWN, fel.getCurrentThreadName(), "Current thread not correct.");
        assertTrue(fel.hasAnalysis(Analysis.INFO_RH_BUILD_POSSIBLE.getKey()),
                Analysis.INFO_RH_BUILD_POSSIBLE + " analysis not identified.");
    }

    @Test
    void testKafka() {
        String logLine = "java_command: kafka.Kafka /path/to/my.properties";
        assertTrue(JdkUtil.identifyEventType(logLine, null) == JdkUtil.LogEventType.VM_ARGUMENTS,
                JdkUtil.LogEventType.VM_ARGUMENTS.toString() + " not identified.");
        VmArguments event = new VmArguments(logLine);
        FatalErrorLog fel = new FatalErrorLog();
        fel.getVmArguments().add(event);
        assertEquals(Application.KAFKA, fel.getApplication(), Application.KAFKA + " application not identified.");
    }

    @Test
    void testLogicalCpus() {
        File testFile = new File(Constants.TEST_DATA_DIR + "dataset53.txt");
        Manager manager = new Manager();
        FatalErrorLog fel = manager.parse(testFile);
        assertEquals(0, fel.getUnidentifiedLogLines().size(), "Unidentified log lines.");
        assertEquals(200, fel.getCpusLogical(), "CPU cores not correct.");
    }

    @Test
    void testMaxjitcodesize() {
        File testFile = new File(Constants.TEST_DATA_DIR + "dataset37.txt");
        Manager manager = new Manager();
        FatalErrorLog fel = manager.parse(testFile);
        assertEquals(0, fel.getUnidentifiedLogLines().size(), "Unidentified log lines.");
        long codeCacheSize = JdkUtil.convertSize(1024, 'm', 'B');
        assertEquals(codeCacheSize, fel.getCodeCacheSize(), "Code cache size not correct.");
        assertEquals(1, fel.getNativeLibraries().size(), "Native library count not correct.");
        assertEquals(0, fel.getNativeLibrariesUnknown().size(), "Native library unknown count not correct.");
    }

    @Test
    void testMeminfo() {
        File testFile = new File(Constants.TEST_DATA_DIR + "dataset38.txt");
        Manager manager = new Manager();
        FatalErrorLog fel = manager.parse(testFile);
        assertEquals(0, fel.getUnidentifiedLogLines().size(), "Unidentified log lines.");
        long physicalMemory = JdkUtil.convertSize(1584737884, 'K', 'B');
        assertEquals(physicalMemory, fel.getOsMemTotal(), "System physical memory not correct.");
        long physicalMemoryFree = JdkUtil.convertSize(136528040, 'K', 'B');
        assertEquals(physicalMemoryFree, fel.getOsMemFree(), "System physical memory free not correct.");
        long swap = JdkUtil.convertSize(33554428, 'K', 'B');
        assertEquals(swap, fel.getOsSwap(), "System swap not correct.");
        long swapFree = JdkUtil.convertSize(33554428, 'K', 'B');
        assertEquals(swapFree, fel.getOsSwapFree(), "System swap free not correct.");
        long heapInitial = JdkUtil.convertSize(220000, 'M', 'B');
        assertEquals(heapInitial, fel.getHeapInitialSize(), "Heap initial size not correct.");
        long heapMax = JdkUtil.convertSize(220000, 'M', 'B');
        assertEquals(heapMax, fel.getHeapMaxSize(), "Heap max size not correct.");
        long heapAllocation = JdkUtil.convertSize(225041472, 'K', 'B');
        assertEquals(heapAllocation, fel.getHeapAllocation(), "Heap allocation not correct.");
        long heapUsed = JdkUtil.convertSize(1908416, 'K', 'B');
        assertEquals(heapUsed, fel.getHeapUsed(), "Heap used not correct.");
        long metaspaceMax = JdkUtil.convertSize(43008, 'K', 'B');
        assertEquals(metaspaceMax, fel.getMetaspaceMaxSize(), "Metaspace max size not correct.");
        long metaspaceAllocation = JdkUtil.convertSize(41268, 'K', 'B');
        assertEquals(metaspaceAllocation, fel.getMetaspaceAllocation(), "Metaspace allocation not correct.");
        long metaspaceUsed = JdkUtil.convertSize(40246, 'K', 'B');
        assertEquals(metaspaceUsed, fel.getMetaspaceUsed(), "Metaspace used not correct.");
        assertEquals(256, fel.getThreadStackSize(), "Thread stack size not correct.");
        assertEquals(1, fel.getNativeLibraries().size(), "Native library count not correct.");
        assertEquals(0, fel.getNativeLibrariesUnknown().size(), "Native library unknown count not correct.");
    }

    @Test
    void testMemory() {
        File testFile = new File(Constants.TEST_DATA_DIR + "dataset26.txt");
        Manager manager = new Manager();
        FatalErrorLog fel = manager.parse(testFile);
        assertEquals(0, fel.getUnidentifiedLogLines().size(), "Unidentified log lines.");
        long physicalMemory = JdkUtil.convertSize(16058700, 'K', 'B');
        assertEquals(physicalMemory, fel.getJvmMemTotal(), "Physical memory not correct.");
        long physicalMemoryFree = JdkUtil.convertSize(1456096, 'K', 'B');
        assertEquals(physicalMemoryFree, fel.getJvmMemFree(), "Physical memory free not correct.");
        long swap = JdkUtil.convertSize(8097788, 'K', 'B');
        assertEquals(swap, fel.getJvmSwap(), "Swap not correct.");
        long swapFree = JdkUtil.convertSize(7612768, 'K', 'B');
        assertEquals(swapFree, fel.getJvmSwapFree(), "Swap free not correct.");
        long heapInitial = JdkUtil.convertSize(512, 'M', 'B');
        assertEquals(heapInitial, fel.getHeapInitialSize(), "Heap initial size not correct.");
        long heapMax = JdkUtil.convertSize(1024, 'M', 'B');
        assertEquals(heapMax, fel.getHeapMaxSize(), "Heap max size not correct.");
        long heapAllocation = JdkUtil.convertSize(244736 + 699392, 'K', 'B');
        assertEquals(heapAllocation, fel.getHeapAllocation(), "Heap allocation not correct.");
        long heapUsed = JdkUtil.convertSize(103751 + 91187, 'K', 'B');
        assertEquals(heapUsed, fel.getHeapUsed(), "Heap used not correct.");
        long metaspaceMax = JdkUtil.convertSize(1183744, 'K', 'B');
        assertEquals(metaspaceMax, fel.getMetaspaceMaxSize(), "Metaspace max size not correct.");
        long metaspaceAllocation = JdkUtil.convertSize(155992, 'K', 'B');
        assertEquals(metaspaceAllocation, fel.getMetaspaceAllocation(), "Metaspace allocation not correct.");
        long metaspaceUsed = JdkUtil.convertSize(139716, 'K', 'B');
        assertEquals(metaspaceUsed, fel.getMetaspaceUsed(), "Metaspace used not correct.");
        assertEquals(16, fel.getCpusLogical(), "CPU cores not correct.");
        long commitLimit = JdkUtil.convertSize(16127136, 'K', 'B');
        assertEquals(commitLimit, fel.getOsCommitLimit(), "CommitLimit not correct.");
        long committedAs = JdkUtil.convertSize(28976296, 'K', 'B');
        assertEquals(committedAs, fel.getOsCommittedAs(), "Committed_AS not correct.");
    }

    @Test
    void testMemoryWindows() {
        File testFile = new File(Constants.TEST_DATA_DIR + "dataset80.txt");
        Manager manager = new Manager();
        FatalErrorLog fel = manager.parse(testFile);
        assertEquals(0, fel.getUnidentifiedLogLines().size(), "Unidentified log lines.");
        long physicalMemory = JdkUtil.convertSize(16383, 'M', 'B');
        assertEquals(physicalMemory, fel.getJvmMemTotal(), "Physical memory not correct.");
        long physicalMemoryFree = JdkUtil.convertSize(203, 'M', 'B');
        assertEquals(physicalMemoryFree, fel.getJvmMemFree(), "Physical memory free not correct.");
        long memBalloonedNow = JdkUtil.convertSize(0, 'K', 'B');
        assertEquals(memBalloonedNow, fel.getMemBalloonedNow(), "Memory ballooned now not correct.");
        assertEquals(1, fel.getNativeLibraries().size(), "Native library count not correct.");
        assertEquals(0, fel.getNativeLibrariesUnknown().size(), "Native library unknown count not correct.");
    }

    @Test
    void testNoCompressedOops() {
        FatalErrorLog fel = new FatalErrorLog();
        String headerLine = "# Java VM: OpenJDK 64-Bit Server VM (25.302-b08 mixed mode linux-amd64 )";
        Header he = new Header(headerLine);
        fel.getHeaders().add(he);
        assertEquals(CompressedOopMode.NONE, fel.getCompressedOopMode(), "Compressed oop mode not correct.");
    }

    @Test
    void testNoDebuggingSymbols() {
        FatalErrorLog fel = new FatalErrorLog();
        String headerLine = "# V  [libjvm.so+0xa41ea4]";
        Header he = new Header(headerLine);
        fel.getHeaders().add(he);
        assertFalse(fel.haveJdkDebugSymbols(), "Debugging symbols incorrectly identified.");
    }

    @Test
    void testNoJvmOptions() {
        FatalErrorLog fel = new FatalErrorLog();
        String logLine = "jvm_args: -D[Standalone]";
        VmArguments jvmArgs = new VmArguments(logLine);
        fel.getVmArguments().add(jvmArgs);
        fel.doAnalysis();
        assertFalse(fel.hasAnalysis(Analysis.INFO_OPTS_UNKNOWN.getKey()),
                Analysis.INFO_OPTS_UNKNOWN + " analysis incorrectly identified.");
        assertTrue(fel.hasAnalysis(Analysis.INFO_OPTS_NONE.getKey()),
                Analysis.INFO_OPTS_NONE + " analysis not identified.");
    }

    @Test
    void testOracleEnterpriseLinuxJdk8u392() {
        FatalErrorLog fel = new FatalErrorLog();
        String os = "OS: Oracle Linux Server release 7.9";
        OsInfo osEvent = new OsInfo(os);
        fel.getOsInfos().add(osEvent);
        String vmInfo = "vm_info: OpenJDK 64-Bit Server VM (25.392-b08) for linux-amd64 JRE (1.8.0_392-b08), built on "
                + "Oct 18 2023 15:26:17 by \"mockbuild\" with gcc 4.8.5 20150623 (Red Hat 4.8.5-44.0.3)";
        VmInfo vmInfoEvent = new VmInfo(vmInfo);
        fel.setVmInfo(vmInfoEvent);
        assertEquals(JavaVendor.ORACLE, fel.getJavaVendor(), "Java vendor not correct.");
    }

    @Test
    void testOs2Line() {
        FatalErrorLog fel = new FatalErrorLog();
        String os1 = "OS:";
        OsInfo osEvent1 = new OsInfo(os1);
        fel.getOsInfos().add(osEvent1);
        String os2 = " Windows 10 , 64 bit Build 19041 (10.0.19041.3155)";
        OsInfo osEvent2 = new OsInfo(os2);
        fel.getOsInfos().add(osEvent2);
        assertEquals("Windows 10 , 64 bit Build 19041 (10.0.19041.3155)", fel.getOsString(), "OS string not correct.");
        assertEquals(OsVersion.WINDOWS10, fel.getOsVersion(), "OS version not correct.");
    }

    @Test
    void testOsJustLinux() {
        FatalErrorLog fel = new FatalErrorLog();
        String os = "OS:Linux";
        OsInfo osEvent = new OsInfo(os);
        fel.getOsInfos().add(osEvent);
        assertEquals(Os.LINUX, fel.getOs(), "OS not correct.");
        assertEquals(OsVersion.UNIDENTIFIED, fel.getOsVersion(), "OS version not correct.");
    }

    @Test
    void testOsRhel() {
        FatalErrorLog fel = new FatalErrorLog();
        String os = "OS:Red Hat Enterprise Linux Server release 7.8 (Maipo)";
        OsInfo osEvent = new OsInfo(os);
        fel.getOsInfos().add(osEvent);
        assertEquals(Os.LINUX, fel.getOs(), "OS not correct.");
        assertEquals(OsVersion.RHEL7, fel.getOsVersion(), "OS version not correct.");
    }

    @Test
    void testOutOfMemoryError() {
        File testFile = new File(Constants.TEST_DATA_DIR + "dataset21.txt");
        Manager manager = new Manager();
        FatalErrorLog fel = manager.parse(testFile);
        assertEquals(0, fel.getUnidentifiedLogLines().size(), "Unidentified log lines.");
        StringBuilder error = new StringBuilder();
        error.append("# There is insufficient memory for the Java Runtime Environment to continue.");
        error.append(Constants.LINE_SEPARATOR);
        error.append("# Native memory allocation (mmap) failed to map 754974720 bytes for committing reserved memory.");
        error.append(Constants.LINE_SEPARATOR);
        error.append("#  Out of Memory Error (os_linux.cpp:2749), pid=25305, tid=0x00007f5ab28b7700");
        assertEquals(error.toString(), fel.getError());
    }

    @Test
    void testRhBuildStringMockbuild() {
        FatalErrorLog fel = new FatalErrorLog();
        String vmInfo = "";
        VmInfo vmInfoEvent = new VmInfo(vmInfo);
        fel.setVmInfo(vmInfoEvent);
        assertFalse(fel.isRhBuildString(), "RH build string identified.");
    }

    @Test
    void testRhel7Jdk11() {
        File testFile = new File(Constants.TEST_DATA_DIR + "dataset8.txt");
        Manager manager = new Manager();
        FatalErrorLog fel = manager.parse(testFile);
        assertEquals(0, fel.getUnidentifiedLogLines().size(), "Unidentified log lines.");
        assertTrue(fel.isRhel(), "OS not identified as RHEL.");
        assertFalse(fel.hasAnalysis(Analysis.WARN_DEBUG_SYMBOLS.getKey()),
                Analysis.WARN_DEBUG_SYMBOLS + " analysis incorrectly identified.");
        assertTrue(fel.hasAnalysis(Analysis.INFO_RH_BUILD_RPM_INSTALL.getKey()),
                Analysis.INFO_RH_BUILD_RPM_INSTALL + " analysis not identified.");
        assertEquals("11.0.7+10-LTS", fel.getJdkReleaseString(), "JDK release not correct.");
        assertEquals(JavaVendor.RED_HAT, fel.getJavaVendor(), "Java vendor not correct.");
        assertEquals(1, fel.getNativeLibraries().size(), "Native library count not correct.");
        assertEquals(0, fel.getNativeLibrariesUnknown().size(), "Native library unknown count not correct.");
    }

    @Test
    void testRhel7Jdk8() {
        File testFile = new File(Constants.TEST_DATA_DIR + "dataset10.txt");
        Manager manager = new Manager();
        FatalErrorLog fel = manager.parse(testFile);
        assertEquals(0, fel.getUnidentifiedLogLines().size(), "Unidentified log lines.");
        assertTrue(fel.isRhel(), "OS not identified as RHEL.");
        assertFalse(fel.hasAnalysis(Analysis.WARN_DEBUG_SYMBOLS.getKey()),
                Analysis.WARN_DEBUG_SYMBOLS + " analysis incorrectly identified.");
        assertTrue(fel.hasAnalysis(Analysis.INFO_RH_BUILD_RPM_INSTALL.getKey()),
                Analysis.INFO_RH_BUILD_RPM_INSTALL + " analysis not identified.");
        assertEquals("1.8.0_131-b12", fel.getJdkReleaseString(), "JDK release not correct.");
        assertEquals(JavaVendor.RED_HAT, fel.getJavaVendor(), "Java vendor not correct.");
        assertEquals(1, fel.getNativeLibraries().size(), "Native library count not correct.");
        assertEquals(0, fel.getNativeLibrariesUnknown().size(), "Native library unknown count not correct.");
    }

    @Test
    void testRhel8Jdk11() {
        File testFile = new File(Constants.TEST_DATA_DIR + "dataset9.txt");
        Manager manager = new Manager();
        FatalErrorLog fel = manager.parse(testFile);
        assertEquals(0, fel.getUnidentifiedLogLines().size(), "Unidentified log lines.");
        assertTrue(fel.isRhel(), "OS not identified as RHEL.");
        assertFalse(fel.hasAnalysis(Analysis.WARN_DEBUG_SYMBOLS.getKey()),
                Analysis.WARN_DEBUG_SYMBOLS + " analysis incorrectly identified.");
        assertTrue(fel.hasAnalysis(Analysis.INFO_RH_BUILD_RPM_INSTALL.getKey()),
                Analysis.INFO_RH_BUILD_RPM_INSTALL + " analysis not identified.");
        assertEquals("11.0.8+10-LTS", fel.getJdkReleaseString(), "JDK release not correct.");
        assertEquals(JavaVendor.RED_HAT, fel.getJavaVendor(), "Java vendor not correct.");
        assertEquals(1, fel.getNativeLibraries().size(), "Native library count not correct.");
        assertEquals(0, fel.getNativeLibrariesUnknown().size(), "Native library unknown count not correct.");
    }

    @Test
    void testRhel8Jdk17() {
        File testFile = new File(Constants.TEST_DATA_DIR + "dataset70.txt");
        Manager manager = new Manager();
        FatalErrorLog fel = manager.parse(testFile);
        assertEquals(0, fel.getUnidentifiedLogLines().size(), "Unidentified log lines.");
        assertEquals("Red Hat Enterprise Linux release 8.5 (Ootpa)", fel.getOsString(), "OS string not correct.");
        assertEquals(OsVendor.REDHAT, fel.getOsVendor(), "OS vendor not correct.");
        assertEquals(OsVersion.RHEL8, fel.getOsVersion(), "OS version not correct.");
        assertTrue(fel.isJdkLts(), "LTS release not identified.");
        assertTrue(fel.isRhRpm(), "Red Hat rpm not identified.");
        assertTrue(fel.isRhRpmInstall(), "Red Hat rpm install not identified.");
        assertTrue(fel.isRhel(), "RHEL not identified.");
        assertFalse(fel.isWindows(), "Windows incorrectly identified.");
        assertEquals(254790, fel.getThreadsMaxLimit(), "threads-max not correct.");
        assertEquals(4194304, fel.getPidMaxLimit(), "pid_max not correct.");
        assertEquals(65530, fel.getMaxMapCountLimit(), "max_map_count not correct.");
        assertFalse(fel.hasAnalysis(Analysis.WARN_UNIDENTIFIED_LOG_LINE.getKey()),
                Analysis.WARN_UNIDENTIFIED_LOG_LINE + " analysis incorrectly identified.");
        assertEquals(1, fel.getNativeLibraries().size(), "Native library count not correct.");
        assertEquals(0, fel.getNativeLibrariesUnknown().size(), "Native library unknown count not correct.");
    }

    @Test
    void testRhel8Ppc64le() {
        File testFile = new File(Constants.TEST_DATA_DIR + "dataset52.txt");
        Manager manager = new Manager();
        FatalErrorLog fel = manager.parse(testFile);
        assertEquals(0, fel.getUnidentifiedLogLines().size(), "Unidentified log lines.");
        assertEquals(Arch.PPC64LE, fel.getArch(), "Arch not correct.");
        assertEquals(JavaVendor.RED_HAT, fel.getJavaVendor(), "Java vendor not correct.");
        assertEquals(Application.TOMCAT, fel.getApplication(), "Application not correct.");
        assertEquals(1, fel.getNativeLibraries().size(), "Native library count not correct.");
        assertEquals(0, fel.getNativeLibrariesUnknown().size(), "Native library unknown count not correct.");
    }

    @Test
    void testRhel8RhBuildJdk17u8() {
        FatalErrorLog fel = new FatalErrorLog();
        String os = "OS:Red Hat Enterprise Linux release 8.8 (Ootpa)";
        OsInfo osEvent = new OsInfo(os);
        fel.getOsInfos().add(osEvent);
        String dynamicLibrary = "7ff4c137c000-7ff4c2616000 r-xp 00000000 fd:01 67479981                   "
                + "/usr/lib/jvm/java-17-openjdk-17.0.8.0.7-2.el8.x86_64/lib/server/libjvm.so";
        DynamicLibrary dynamicLibraryEvent = new DynamicLibrary(dynamicLibrary);
        fel.getDynamicLibraries().add(dynamicLibraryEvent);
        String vmInfo = "vm_info: OpenJDK 64-Bit Server VM (17.0.8+7-LTS) for linux-amd64 JRE (17.0.8+7-LTS), built on "
                + "Jul 14 2023 17:37:12 by \"mockbuild\" with gcc 8.5.0 20210514 (Red Hat 8.5.0-18)";
        VmInfo vmInfoEvent = new VmInfo(vmInfo);
        fel.setVmInfo(vmInfoEvent);
        fel.doAnalysis();
        assertEquals("java-17-openjdk-17.0.8.0.7-2.el8.x86_64", fel.getRpmDirectory(), "Rpm directory not correct.");
        assertTrue(fel.isRhRpmInstall(), "RH rpm install not identified.");
        assertTrue(fel.hasAnalysis(Analysis.INFO_RH_BUILD_RPM_INSTALL.getKey()),
                Analysis.INFO_RH_BUILD_RPM_INSTALL + " analysis not identified.");
    }

    @Test
    void testRhel8RhBuildJdk21() {
        FatalErrorLog fel = new FatalErrorLog();
        String os1 = "OS:";
        OsInfo osEvent1 = new OsInfo(os1);
        fel.getOsInfos().add(osEvent1);
        String os2 = "Red Hat Enterprise Linux release 8.9 (Ootpa)";
        OsInfo osEvent2 = new OsInfo(os2);
        fel.getOsInfos().add(osEvent2);
        String dynamicLibrary = "7fc74b1ef000-7fc74b487000 r--p 00000000 fd:01 100949751                  "
                + "/usr/lib/jvm/java-21-openjdk-21.0.1.0.12-2.el8.x86_64/lib/server/libjvm.so";
        DynamicLibrary dynamicLibraryEvent = new DynamicLibrary(dynamicLibrary);
        fel.getDynamicLibraries().add(dynamicLibraryEvent);
        String vmInfo = "vm_info: OpenJDK 64-Bit Server VM (21.0.1+12-LTS) for linux-amd64 JRE (21.0.1+12-LTS), built "
                + "on 2023-10-30T00:33:46Z by \"mockbuild\" with gcc 10.2.1 20210130 (Red Hat 10.2.1-11)";
        VmInfo vmInfoEvent = new VmInfo(vmInfo);
        fel.setVmInfo(vmInfoEvent);
        fel.doAnalysis();
        assertEquals("java-21-openjdk-21.0.1.0.12-2.el8.x86_64", fel.getRpmDirectory(), "Rpm directory not correct.");
        assertTrue(fel.isRhRpmInstall(), "RH rpm install not identified.");
        assertTrue(fel.hasAnalysis(Analysis.INFO_RH_BUILD_RPM_INSTALL.getKey()),
                Analysis.INFO_RH_BUILD_RPM_INSTALL + " analysis not identified.");
    }

    @Test
    void testRhel9Os() {
        FatalErrorLog fel = new FatalErrorLog();
        String os = "OS:Red Hat Enterprise Linux release 9.0 (Plow)";
        OsInfo osEvent = new OsInfo(os);
        fel.getOsInfos().add(osEvent);
        assertEquals(OsVersion.RHEL9, fel.getOsVersion(), "OS version not correct.");
    }

    @Test
    void testRhel9RhBuildJdk11() {
        FatalErrorLog fel = new FatalErrorLog();
        String os = "OS:Red Hat Enterprise Linux release 9.0 (Plow)";
        OsInfo osEvent = new OsInfo(os);
        fel.getOsInfos().add(osEvent);
        String dynamicLibrary = "7f76913c5000-7f7691649000 r--p 00000000 fd:01 1706624                    "
                + "/usr/lib/jvm/java-11-openjdk-11.0.17.0.8-2.el9_0.x86_64/lib/server/libjvm.so";
        DynamicLibrary dynamicLibraryEvent = new DynamicLibrary(dynamicLibrary);
        fel.getDynamicLibraries().add(dynamicLibraryEvent);
        String vmInfo = "vm_info: OpenJDK 64-Bit Server VM (11.0.17+8-LTS) for linux-amd64 JRE (11.0.17+8-LTS), built "
                + "on Oct 15 2022 00:00:00 by \"mockbuild\" with gcc 11.2.1 20220127 (Red Hat 11.2.1-9)";
        VmInfo vmInfoEvent = new VmInfo(vmInfo);
        fel.setVmInfo(vmInfoEvent);
        fel.doAnalysis();
        assertEquals("java-11-openjdk-11.0.17.0.8-2.el9_0.x86_64", fel.getRpmDirectory(), "Rpm directory not correct.");
        assertTrue(fel.isRhRpmInstall(), "RH rpm install not identified.");
        assertTrue(fel.hasAnalysis(Analysis.INFO_RH_BUILD_RPM_INSTALL.getKey()),
                Analysis.INFO_RH_BUILD_RPM_INSTALL + " analysis not identified.");
    }

    @Test
    void testRhelJdkNotRedHatBuild() {
        File testFile = new File(Constants.TEST_DATA_DIR + "dataset4.txt");
        Manager manager = new Manager();
        FatalErrorLog fel = manager.parse(testFile);
        assertEquals(0, fel.getUnidentifiedLogLines().size(), "Unidentified log lines.");
        assertTrue(fel.hasAnalysis(Analysis.INFO_RH_BUILD_NOT.getKey()),
                Analysis.INFO_RH_BUILD_NOT + " analysis not identified.");
        assertEquals("Thu May  7 17:24:12 2020 UTC", fel.getCrashTimeString(), "Time of crash not correct.");
        assertEquals("1d 7h 30m 19s", fel.getElapsedTime(), "JVM run time not correct.");
        assertEquals(1, fel.getNativeLibraries().size(), "Native library count not correct.");
        assertEquals(0, fel.getNativeLibrariesUnknown().size(), "Native library unknown count not correct.");
    }

    @Test
    void testRhReleaseStringTruncatedLogfile() {
        File testFile = new File(Constants.TEST_DATA_DIR + "dataset77.txt");
        Manager manager = new Manager();
        FatalErrorLog fel = manager.parse(testFile);
        assertEquals(0, fel.getUnidentifiedLogLines().size(), "Unidentified log lines.");
        assertFalse(fel.hasAnalysis(Analysis.INFO_RH_BUILD_NOT.getKey()),
                Analysis.INFO_RH_BUILD_NOT + " analysis incorrectly identified.");
    }

    @Test
    void testRhRpmInstall() {
        FatalErrorLog fel = new FatalErrorLog();
        String os = "OS:Red Hat Enterprise Linux Server release 7.9 (Maipo)";
        OsInfo osEvent = new OsInfo(os);
        fel.getOsInfos().add(osEvent);
        String dynamicLibrary = "7f7eec43f000-7f7eed1dc000 r-xp 00000000 fd:00 37590                      "
                + "/usr/lib/jvm/java-1.8.0-openjdk-1.8.0.342.b07-1.el7_9.x86_64/jre/lib/amd64/server/libjvm.so";
        DynamicLibrary dynamicLibraryEvent = new DynamicLibrary(dynamicLibrary);
        fel.getDynamicLibraries().add(dynamicLibraryEvent);
        String vmInfo = "vm_info: OpenJDK 64-Bit Server VM (25.342-b07) for linux-amd64 JRE (1.8.0_342-b07), built on "
                + "Jul 18 2022 23:53:30 by \"mockbuild\" with gcc 4.8.5 20150623 (Red Hat 4.8.5-44)";
        VmInfo vmInfoEvent = new VmInfo(vmInfo);
        fel.setVmInfo(vmInfoEvent);
        fel.doAnalysis();
        assertEquals("java-1.8.0-openjdk-1.8.0.342.b07-1.el7_9.x86_64", fel.getRpmDirectory(),
                "Rpm directory not correct.");
        assertTrue(fel.isRhRpmInstall(), "RH rpm install not identified.");
    }

    @Test
    void testRhShenandoahNotExperimental() {
        FatalErrorLog fel = new FatalErrorLog();
        String os = "OS:Red Hat Enterprise Linux Server release 7.7 (Maipo)";
        OsInfo osEvent = new OsInfo(os);
        fel.getOsInfos().add(osEvent);
        String vmInfo = "vm_info: OpenJDK 64-Bit Server VM (11.0.9.1+1-LTS) for linux-amd64 JRE (11.0.9.1+1-LTS), "
                + "built on Nov 12 2020 18:10:11 by \"mockbuild\" with gcc 4.8.5 20150623 (Red Hat 4.8.5-44";
        VmInfo vmInfoEvent = new VmInfo(vmInfo);
        fel.setVmInfo(vmInfoEvent);
        String jvmArgs = "jvm_args: -XX:+UnlockExperimentalVMOptions -XX:+UseShenandoahGC";
        VmArguments vmArgumentsEvent = new VmArguments(jvmArgs);
        fel.getVmArguments().add(vmArgumentsEvent);
        fel.doAnalysis();
        assertTrue(fel.hasAnalysis(Analysis.INFO_RH_BUILD_RPM_BASED.getKey()),
                Analysis.INFO_RH_BUILD_RPM_BASED + " analysis not identified.");
        assertTrue(fel.hasAnalysis(Analysis.INFO_RH_OPT_EXPERIMENTAL_SHENANDOAH.getKey()),
                Analysis.INFO_RH_OPT_EXPERIMENTAL_SHENANDOAH + " analysis not identified.");
    }

    @Test
    void testRhSso() {
        String logLine = "  0x00005587a9039000 JavaThread \"Brute Force Protector\" [_thread_blocked, id=6073, "
                + "stack(0x00007f5abb897000,0x00007f5abb998000)]";
        assertTrue(JdkUtil.identifyEventType(logLine, null) == JdkUtil.LogEventType.THREAD,
                JdkUtil.LogEventType.THREAD.toString() + " not identified.");
        Thread event = new Thread(logLine);
        FatalErrorLog fel = new FatalErrorLog();
        fel.getThreads().add(event);
        assertEquals(Application.RHSSO, fel.getApplication(), "RHSSO application not identified.");
    }

    @Test
    void testRhWindowsReleaseWith2BuildDateTimes() {
        File testFile = new File(Constants.TEST_DATA_DIR + "dataset25.txt");
        Manager manager = new Manager();
        FatalErrorLog fel = manager.parse(testFile);
        assertEquals(0, fel.getUnidentifiedLogLines().size(), "Unidentified log lines.");
        assertTrue(fel.isWindows(), "OS not identified as Windows.");
        assertEquals(Arch.X86_64, fel.getArch(), "Arch not correct.");
        assertTrue(fel.hasAnalysis(Analysis.INFO_RH_BUILD_WINDOWS_ZIP.getKey()),
                Analysis.INFO_RH_BUILD_WINDOWS_ZIP + " analysis not identified.");
    }

    @Test
    void testShenandoah() {
        File testFile = new File(Constants.TEST_DATA_DIR + "dataset31.txt");
        Manager manager = new Manager();
        FatalErrorLog fel = manager.parse(testFile);
        assertEquals(0, fel.getUnidentifiedLogLines().size(), "Unidentified log lines.");
        long physicalMemory = JdkUtil.convertSize(8388608, 'K', 'B');
        assertEquals(physicalMemory, fel.getJvmMemTotal(), "Physical memory not correct.");
        long physicalMemoryFree = JdkUtil.convertSize(1334692, 'K', 'B');
        assertEquals(physicalMemoryFree, fel.getJvmMemFree(), "Physical memory free not correct.");
        long swap = JdkUtil.convertSize(0, 'K', 'B');
        assertEquals(swap, fel.getJvmSwap(), "Swap not correct.");
        long swapFree = JdkUtil.convertSize(0, 'K', 'B');
        assertEquals(swapFree, fel.getJvmSwapFree(), "Swap free not correct.");
        long heapInitial = JdkUtil.convertSize(4014, 'M', 'B');
        assertEquals(heapInitial, fel.getHeapInitialSize(), "Heap initial size not correct.");
        long heapMax = JdkUtil.convertSize(5734, 'M', 'B');
        assertEquals(heapMax, fel.getHeapMaxSize(), "Heap max size not correct.");
        long heapAllocation = JdkUtil.convertSize(5734, 'M', 'B');
        assertEquals(heapAllocation, fel.getHeapAllocation(), "Heap allocation not correct.");
        long heapUsed = JdkUtil.convertSize(3795, 'M', 'B');
        assertEquals(heapUsed, fel.getHeapUsed(), "Heap used not correct.");
        long metaspaceMax = JdkUtil.convertSize(512, 'M', 'B');
        assertEquals(metaspaceMax, fel.getMetaspaceMaxSize(), "Metaspace max size not correct.");
        long metaspaceAllocation = JdkUtil.convertSize(277632, 'K', 'B');
        assertEquals(metaspaceAllocation, fel.getMetaspaceAllocation(), "Metaspace allocation not correct.");
        long metaspaceUsed = JdkUtil.convertSize(257753, 'K', 'B');
        assertEquals(metaspaceUsed, fel.getMetaspaceUsed(), "Metaspace used not correct.");
        assertEquals(Application.JBOSS_EAP7, fel.getApplication(), "Application not correct.");
        assertEquals(16, fel.getCpusLogical(), "CPU cores not correct.");
        assertEquals("at safepoint (normal execution)", fel.getLiteral(), "State not correct.");
        assertTrue(fel.hasAnalysis(Analysis.INFO_SWAP_DISABLED.getKey()),
                Analysis.INFO_SWAP_DISABLED + " analysis not identified.");
        assertTrue(fel.hasAnalysis(Analysis.ERROR_JDK8_SHENANDOAH_ROOT_UPDATER.getKey()),
                Analysis.ERROR_JDK8_SHENANDOAH_ROOT_UPDATER + " analysis not identified.");
        assertEquals(1, fel.getNativeLibraries().size(), "Native library count not correct.");
        assertEquals(0, fel.getNativeLibrariesUnknown().size(), "Native library unknown count not correct.");
    }

    @Test
    void testSigSegvCompiledJavaCode() {
        File testFile = new File(Constants.TEST_DATA_DIR + "dataset1.txt");
        Manager manager = new Manager();
        FatalErrorLog fel = manager.parse(testFile);
        assertEquals(0, fel.getUnidentifiedLogLines().size(), "Unidentified log lines.");
        StringBuffer causedBy = new StringBuffer("#  SIGSEGV (0xb) at pc=0x00007fcd2af94e64, pid=23171, tid=23172");
        causedBy.append(Constants.LINE_SEPARATOR);
        causedBy.append("# C  [libcairo.so.2+0x66e64]  cairo_region_num_rectangles+0x4");
        assertEquals(causedBy.toString(), fel.getError(), "Caused by incorrect.");
    }

    @Test
    void testSigSegvNativeCode() {
        File testFile = new File(Constants.TEST_DATA_DIR + "dataset2.txt");
        Manager manager = new Manager();
        FatalErrorLog fel = manager.parse(testFile);
        assertEquals(0, fel.getUnidentifiedLogLines().size(), "Unidentified log lines.");
        StringBuffer causedBy = new StringBuffer(
                "#  SIGSEGV (0xb) at pc=0x0000000000000000, pid=44768, tid=0x00007f368f18d700");
        causedBy.append(Constants.LINE_SEPARATOR);
        causedBy.append("# C  0x0000000000000000");
        assertEquals(causedBy.toString(), fel.getError(), "Caused by incorrect.");
    }

    @Test
    void testSolaris() {
        File testFile = new File(Constants.TEST_DATA_DIR + "dataset7.txt");
        Manager manager = new Manager();
        FatalErrorLog fel = manager.parse(testFile);
        assertEquals(0, fel.getUnidentifiedLogLines().size(), "Unidentified log lines.");
        assertEquals(Arch.SPARC, fel.getArch(), "Arch not correct.");
        assertEquals("1.8.0_251-b08", fel.getJdkReleaseString(), "JDK release not correct.");
        // No vm_info, so not possible to determine vendor
        assertEquals(JavaVendor.NOT_RED_HAT, fel.getJavaVendor(), "Java vendor not correct.");
        assertFalse(fel.hasAnalysis(Analysis.WARN_UNIDENTIFIED_LOG_LINE.getKey()),
                Analysis.WARN_UNIDENTIFIED_LOG_LINE + " analysis incorrectly identified.");
    }

    @Test
    void testSpringBoot() {
        String logLine = "java_command: org.springframework.boot.loader.WarLauncher";
        assertTrue(JdkUtil.identifyEventType(logLine, null) == JdkUtil.LogEventType.VM_ARGUMENTS,
                JdkUtil.LogEventType.VM_ARGUMENTS.toString() + " not identified.");
        VmArguments event = new VmArguments(logLine);
        FatalErrorLog fel = new FatalErrorLog();
        fel.getVmArguments().add(event);
        assertEquals(Application.SPRING_BOOT, fel.getApplication(),
                Application.SPRING_BOOT + " application not identified.");
    }

    @Test
    void testTemurin() {
        FatalErrorLog fel = new FatalErrorLog();
        String headerLine = "#   https://github.com/adoptium/adoptium-support/issues";
        Header headerEvent = new Header(headerLine);
        fel.getHeaders().add(headerEvent);
        String vmInfo = "vm_info: OpenJDK 64-Bit Server VM (11.0.19+7) for linux-amd64 JRE (11.0.19+7), built on "
                + "Apr 18 2023 22:12:43 by \"\" with gcc 7.5.0";
        VmInfo vmInfoEvent = new VmInfo(vmInfo);
        fel.setVmInfo(vmInfoEvent);
        assertEquals(JavaVendor.ADOPTIUM, fel.getJavaVendor(), "Java vendor not correct.");
    }

    @Test
    void testTenuredGeneration() {
        File testFile = new File(Constants.TEST_DATA_DIR + "dataset40.txt");
        Manager manager = new Manager();
        FatalErrorLog fel = manager.parse(testFile);
        assertEquals(0, fel.getUnidentifiedLogLines().size(), "Unidentified log lines.");
        long heapInitial = JdkUtil.convertSize(3172, 'M', 'B');
        assertEquals(heapInitial, fel.getHeapInitialSize(), "Heap initial size not correct.");
        long heapMax = JdkUtil.convertSize(3172, 'M', 'B');
        assertEquals(heapMax, fel.getHeapMaxSize(), "Heap max size not correct.");
        long heapAllocation = JdkUtil.convertSize(947392 + 2165440, 'K', 'B');
        assertEquals(heapAllocation, fel.getHeapAllocation(), "Heap allocation not correct.");
        long heapUsed = JdkUtil.convertSize(396580 + 937560, 'K', 'B');
        assertEquals(heapUsed, fel.getHeapUsed(), "Heap used not correct.");
        long metaspaceMax = JdkUtil.convertSize(1275904, 'K', 'B');
        assertEquals(metaspaceMax, fel.getMetaspaceMaxSize(), "Metaspace max size not correct.");
        long metaspaceAllocation = JdkUtil.convertSize(262244, 'K', 'B');
        assertEquals(metaspaceAllocation, fel.getMetaspaceAllocation(), "Metaspace allocation not correct.");
        long metaspaceUsed = JdkUtil.convertSize(243180, 'K', 'B');
        assertEquals(metaspaceUsed, fel.getMetaspaceUsed(), "Metaspace used not correct.");
        assertEquals(512, fel.getThreadStackSize(), "Thread stack size not correct.");
        assertEquals(1, fel.getNativeLibraries().size(), "Native library count not correct.");
        assertEquals(0, fel.getNativeLibrariesUnknown().size(), "Native library unknown count not correct.");
    }

    @Test
    void testThreadDumpJvmti() {
        FatalErrorLog fel = new FatalErrorLog();
        String vmOperation = "VM_Operation (0x00007efff5d6d830): GetThreadListStackTraces, mode: safepoint, requested "
                + "by thread 0x000055b2423e2800";
        VmOperation vmOperationEvent = new VmOperation(vmOperation);
        fel.setVmOperation(vmOperationEvent);
        fel.doAnalysis();
        assertTrue(fel.hasAnalysis(Analysis.WARN_VM_OPERATION_THREAD_DUMP_JVMTI.getKey()),
                Analysis.WARN_VM_OPERATION_THREAD_DUMP_JVMTI + " analysis not identified.");
    }

    @Test
    void testThreadStackSize() {
        FatalErrorLog fel = new FatalErrorLog();
        String jvmArgs = "jvm_args: -Xmx37400M -Xms37400M -XX:ThreadStackSize=256";
        VmArguments event = new VmArguments(jvmArgs);
        fel.getVmArguments().add(event);
        fel.doAnalysis();
        assertEquals(256, fel.getThreadStackSize(), "Thread stack size not correct.");
        fel.getVmArguments().clear();
        jvmArgs = "jvm_args: -Xmx37400M -Xms37400M -XX:ThreadStackSize=256k";
        event = new VmArguments(jvmArgs);
        fel.getVmArguments().add(event);
        fel.doAnalysis();
        assertEquals(256 * 1024, fel.getThreadStackSize(), "Thread stack size not correct.");
        fel.getVmArguments().clear();
        jvmArgs = "jvm_args: -Xmx37400M -Xms37400M -Xss256";
        event = new VmArguments(jvmArgs);
        fel.getVmArguments().add(event);
        fel.doAnalysis();
        assertEquals(0, fel.getThreadStackSize(), "Thread stack size not correct.");
    }

    @Test
    void testTruncated() {
        FatalErrorLog fel = new FatalErrorLog();
        assertTrue(fel.isTruncated(), "Truncated log not identified.");
    }

    @Test
    void testTruncatedElapsedTimeEvent() {
        FatalErrorLog fel = new FatalErrorLog();
        fel.setElapsedTime(new ElapsedTime(""));
        assertFalse(fel.isTruncated(), "Truncated log incorrectly identified.");
    }

    @Test
    void testTruncatedEndEvent() {
        FatalErrorLog fel = new FatalErrorLog();
        fel.setEnd(new End(""));
        assertFalse(fel.isTruncated(), "Truncated log incorrectly identified.");
    }

    @Test
    void testUbi9OnRhel7() {
        FatalErrorLog fel = new FatalErrorLog();
        String os = "OS:Red Hat Enterprise Linux release 9.0 (Plow)";
        OsInfo osEvent = new OsInfo(os);
        fel.getOsInfos().add(osEvent);
        String uname = "uname:Linux 3.10.0-870.el7.CSB.input.5.x86_64 #1 SMP Mon Apr 16 16:59:47 UTC 2018 x86_64";
        Uname unameEvent = new Uname(uname);
        fel.setUname(unameEvent);
        assertEquals(OsVersion.RHEL9, fel.getOsVersion(), "OS version not correct.");
    }

    @Test
    void testUnameSplitAcross2Lines() {
        File testFile = new File(Constants.TEST_DATA_DIR + "dataset64.txt");
        Manager manager = new Manager();
        FatalErrorLog fel = manager.parse(testFile);
        assertEquals(0, fel.getUnidentifiedLogLines().size(), "Unidentified log lines.");
        assertEquals("SunOS 5.11 11.4.32.88.3 sun4v  (T2 libthread)", fel.getUname().getUname(), "uname not correct.");
    }

    @Test
    void testUptime() {
        FatalErrorLog fel = new FatalErrorLog();
        TimeElapsedTime event = new TimeElapsedTime(
                "Time: Tue May  5 18:32:04 2020 CEST elapsed time: 956 seconds (0d 0h 15m 56s)");
        fel.setTimeElapsedTime(event);
        assertEquals(956000L, fel.getUptime(), "Uptime not correct.");
    }

    @Test
    void testUsername() {
        File testFile = new File(Constants.TEST_DATA_DIR + "dataset40.txt");
        Manager manager = new Manager();
        FatalErrorLog fel = manager.parse(testFile);
        assertEquals(0, fel.getUnidentifiedLogLines().size(), "Unidentified log lines.");
        assertEquals("username", fel.getUsername(), "USERNAME not correct.");
    }

    @Test
    void testUsernameWithDotAtSign() {
        FatalErrorLog fel = new FatalErrorLog();
        String username = "USERNAME=first.last@location";
        EnvironmentVariable environmentVariablesEvent = new EnvironmentVariable(username);
        fel.getEnvironmentVariables().add(environmentVariablesEvent);
        assertEquals("first.last@location", fel.getUsername(), "USERNAME not correct.");
    }

    @Test
    void testUsernameWithUnderscore() {
        FatalErrorLog fel = new FatalErrorLog();
        String username = "USERNAME=jb_admin";
        EnvironmentVariable environmentVariablesEvent = new EnvironmentVariable(username);
        fel.getEnvironmentVariables().add(environmentVariablesEvent);
        assertEquals("jb_admin", fel.getUsername(), "USERNAME not correct.");
    }

    @Test
    void testVendorAdoptium() {
        FatalErrorLog fel = new FatalErrorLog();
        String vmInfo = "vm_info: OpenJDK 64-Bit Server VM (17.0.4+8) for linux-amd64 JRE (17.0.4+8), built on Jul 19 "
                + "2022 00:00:00 by \"temurin\" with gcc 10.3.0";
        VmInfo vmInfoEvent = new VmInfo(vmInfo);
        fel.setVmInfo(vmInfoEvent);
        assertEquals(JavaVendor.ADOPTIUM, fel.getJavaVendor(), "JDK vendor not correct.");
    }

    @Test
    void testVendorAdoptOpenJdkHeader() {
        FatalErrorLog fel = new FatalErrorLog();
        String header = "# JRE version: OpenJDK Runtime Environment AdoptOpenJDK (11.0.9+11) (build 11.0.9+11)";
        Header headerEvent = new Header(header);
        fel.getHeaders().add(headerEvent);
        assertEquals(JavaVendor.ADOPTOPENJDK, fel.getJavaVendor(), "JDK vendor not correct.");
    }

    @Test
    void testVendorAdoptOpenJdkVmInfo() {
        FatalErrorLog fel = new FatalErrorLog();
        String vmInfo = "vm_info: OpenJDK 64-Bit Server VM (25.265-b01) for linux-amd64 JRE (1.8.0_265-b01), "
                + "built on Jul 28 2020 15:17:23 by \"jenkins\" with gcc 4.8.2 20140120 (Red Hat 4.8.2-15)";
        VmInfo vmInfoEvent = new VmInfo(vmInfo);
        fel.setVmInfo(vmInfoEvent);
        assertEquals(JavaVendor.ADOPTOPENJDK, fel.getJavaVendor(), "JDK vendor not correct.");
    }

    @Test
    void testVendorAzul() {
        FatalErrorLog fel = new FatalErrorLog();
        String vmInfo = "vm_info: OpenJDK 64-Bit Server VM (25.252-b14) for linux-amd64 JRE "
                + "(Zulu 8.46.0.52-SA-linux64) (1.8.0_252-b14), built on Apr 22 2020 07:39:02 by \"zulu_re\" with gcc "
                + "4.4.7 20120313";
        VmInfo vmInfoEvent = new VmInfo(vmInfo);
        fel.setVmInfo(vmInfoEvent);
        assertEquals(JavaVendor.AZUL, fel.getJavaVendor(), "JDK vendor not correct.");
    }

    @Test
    void testVendorMicrosoft() {
        FatalErrorLog fel = new FatalErrorLog();
        String vmInfo = "vm_info: OpenJDK 64-Bit Server VM (11.0.10+9) for linux-amd64 JRE (11.0.10+9), "
                + "built on Jan 22 2021 19:24:16 by \"vsts\" with gcc 7.3.0";
        VmInfo vmInfoEvent = new VmInfo(vmInfo);
        fel.setVmInfo(vmInfoEvent);
        assertEquals(JavaVendor.MICROSOFT, fel.getJavaVendor(), "JDK vendor not correct.");
    }

    @Test
    void testVendorOracle() {
        FatalErrorLog fel = new FatalErrorLog();
        String vmInfo = "vm_info: Java HotSpot(TM) 64-Bit Server VM (25.25-b02) for windows-amd64 JRE (1.8.0_25-b18), "
                + "built on Oct  7 2014 14:25:37 by \"java_re\" with MS VC++ 10.0 (VS2010)";
        VmInfo vmInfoEvent = new VmInfo(vmInfo);
        fel.setVmInfo(vmInfoEvent);
        assertEquals(JavaVendor.ORACLE, fel.getJavaVendor(), "JDK vendor not correct.");
    }

    @Test
    void testVendorUnknown() {
        FatalErrorLog fel = new FatalErrorLog();
        String vmInfo = "vm_info: OpenJDK 64-Bit Server VM (25.242-b08) for linux-amd64 JRE (1.8.0_242-b08), built on "
                + "Jan 17 2020 09:36:23 by \"bob\" with gcc 4.4.7 20120313 (Red Hat 4.4.7-23";
        VmInfo vmInfoEvent = new VmInfo(vmInfo);
        fel.setVmInfo(vmInfoEvent);
        assertEquals(JavaVendor.UNIDENTIFIED, fel.getJavaVendor(), "JDK vendor not correct.");
    }

    @Test
    void testWindows() {
        File testFile = new File(Constants.TEST_DATA_DIR + "dataset49.txt");
        Manager manager = new Manager();
        FatalErrorLog fel = manager.parse(testFile);
        assertEquals(0, fel.getUnidentifiedLogLines().size(), "Unidentified log lines.");
        long physicalMemory = JdkUtil.convertSize(16776740, 'K', 'B');
        assertEquals(physicalMemory, fel.getOsMemTotal(), "System physical memory not correct.");
        long physicalMemoryFree = JdkUtil.convertSize(674168, 'K', 'B');
        assertEquals(physicalMemoryFree, fel.getOsMemFree(), "System physical memory free not correct.");
        long swap = JdkUtil.convertSize(20970784, 'K', 'B');
        assertEquals(swap, fel.getOsSwap(), "System swap not correct.");
        long swapFree = JdkUtil.convertSize(5252, 'K', 'B');
        assertEquals(swapFree, fel.getOsSwapFree(), "System swap free not correct.");
        assertEquals(1, fel.getNativeLibraries().size(), "Native library count not correct.");
        assertEquals(0, fel.getNativeLibrariesUnknown().size(), "Native library unknown count not correct.");
    }

    @Test
    void testWindowsJdk11u15Releases() {
        FatalErrorLog felJdk11u15_1 = new FatalErrorLog();
        FatalErrorLog felJdk11u15_2 = new FatalErrorLog();
        // 2 release w/ same build string
        String vmInfo15_1 = "vm_info: OpenJDK 64-Bit Server VM (11.0.15+9-LTS) for windows-amd64 JRE (11.0.15+9-LTS), "
                + "built on Apr 17 2022 13:56:34 by \"\" with unknown MS VC++:1916";
        String vmInfo15_2 = "vm_info: OpenJDK 64-Bit Server VM (11.0.15+9-LTS) for windows-amd64 JRE (11.0.15+9-LTS), "
                + "built on Apr 27 2022 19:12:18 by \"\" with unknown MS VC++:1916";
        VmInfo vmInfoEvent15_1 = new VmInfo(vmInfo15_1);
        VmInfo vmInfoEvent15_2 = new VmInfo(vmInfo15_2);
        felJdk11u15_1.setVmInfo(vmInfoEvent15_1);
        felJdk11u15_2.setVmInfo(vmInfoEvent15_2);
        String os = "OS: Windows Server 2012 , 64 Bit Build 9200 (6.2.9200.16384)";
        OsInfo osEvent = new OsInfo(os);
        felJdk11u15_1.getOsInfos().add(osEvent);
        felJdk11u15_2.getOsInfos().add(osEvent);
        assertEquals("11.0.15+9-LTS-1", felJdk11u15_1.getJdkReleaseString(), "JDK release not correct.");
        assertEquals("11.0.15+9-LTS-2", felJdk11u15_2.getJdkReleaseString(), "JDK release not correct.");
        assertEquals(KrashUtil.getDate("Apr 17 2022 13:56:34"), felJdk11u15_1.getJdkBuildDate(),
                "Build date not correct.");
        assertEquals(KrashUtil.getDate("Apr 27 2022 19:12:18"), felJdk11u15_2.getJdkBuildDate(),
                "Build date not correct.");
        assertTrue(felJdk11u15_1.isRhBuildDate(), "Red Hat build date incorrectly identified.");
        assertTrue(felJdk11u15_2.isRhBuildDate(), "Red Hat build date incorrectly identified.");
    }

    @Test
    void testWindowsJdk17u3Releases() {
        FatalErrorLog felJdk17u3_1 = new FatalErrorLog();
        FatalErrorLog felJdk17u3_2 = new FatalErrorLog();
        // 2 release w/ same build string
        String vmInfo15_1 = "vm_info: OpenJDK 64-Bit Server VM (17.0.3+6-LTS) for windows-amd64 JRE (17.0.3+6-LTS), "
                + "built on Apr 17 2022 12:11:44 by \"\" with MS VC++ 16.10 / 16.11 (VS2019)";
        String vmInfo15_2 = "vm_info: OpenJDK 64-Bit Server VM (17.0.3+6-LTS) for windows-amd64 JRE (17.0.3+6-LTS), "
                + "built on Apr 27 2022 11:51:42 by \"\" with MS VC++ 16.10 / 16.11 (VS2019)";
        VmInfo vmInfoEvent15_1 = new VmInfo(vmInfo15_1);
        VmInfo vmInfoEvent15_2 = new VmInfo(vmInfo15_2);
        felJdk17u3_1.setVmInfo(vmInfoEvent15_1);
        felJdk17u3_2.setVmInfo(vmInfoEvent15_2);
        String os = "OS: Windows Server 2012 , 64 Bit Build 9200 (6.2.9200.16384)";
        OsInfo osEvent = new OsInfo(os);
        felJdk17u3_1.getOsInfos().add(osEvent);
        felJdk17u3_2.getOsInfos().add(osEvent);
        assertEquals("17.0.3+6-LTS-1", felJdk17u3_1.getJdkReleaseString(), "JDK release not correct.");
        assertEquals("17.0.3+6-LTS-2", felJdk17u3_2.getJdkReleaseString(), "JDK release not correct.");
        assertEquals(KrashUtil.getDate("Apr 17 2022 12:11:44"), felJdk17u3_1.getJdkBuildDate(),
                "Build date not correct.");
        assertEquals(KrashUtil.getDate("Apr 27 2022 11:51:42"), felJdk17u3_2.getJdkBuildDate(),
                "Build date not correct.");
        assertTrue(felJdk17u3_1.isRhBuildDate(), "Red Hat build date incorrectly identified.");
        assertTrue(felJdk17u3_2.isRhBuildDate(), "Red Hat build date incorrectly identified.");
    }

    @Test
    void testWindowsJdk8u332Releases() {
        FatalErrorLog felJdk8u332_1 = new FatalErrorLog();
        FatalErrorLog felJdk8u332_2 = new FatalErrorLog();
        // 2 release w/ same build string
        String vmInfo332_1 = "vm_info: OpenJDK 64-Bit Server VM (25.332-b09) for windows-amd64 JRE (1.8.0_332-b09), "
                + "built on Apr 19 2022 13:36:53 by \"build\" with MS VC++ 10.0 (VS2010)";
        String vmInfo332_2 = "vm_info: OpenJDK 64-Bit Server VM (25.332-b09) for windows-amd64 JRE (1.8.0_332-b09), "
                + "built on Apr 27 2022 21:29:19 by \"build\" with MS VC++ 10.0 (VS2010)";
        VmInfo vmInfoEventJdk8u332_1 = new VmInfo(vmInfo332_1);
        VmInfo vmInfoEventJdk8u332_2 = new VmInfo(vmInfo332_2);
        felJdk8u332_1.setVmInfo(vmInfoEventJdk8u332_1);
        felJdk8u332_2.setVmInfo(vmInfoEventJdk8u332_2);
        String os = "OS: Windows Server 2012 , 64 Bit Build 9200 (6.2.9200.16384)";
        OsInfo osEvent = new OsInfo(os);
        felJdk8u332_1.getOsInfos().add(osEvent);
        felJdk8u332_2.getOsInfos().add(osEvent);
        assertEquals("1.8.0_332-b09-1", felJdk8u332_1.getJdkReleaseString(), "JDK release not correct.");
        assertEquals("1.8.0_332-b09-2", felJdk8u332_2.getJdkReleaseString(), "JDK release not correct.");
        assertEquals(KrashUtil.getDate("Apr 19 2022 13:36:53"), felJdk8u332_1.getJdkBuildDate(),
                "Build date not correct.");
        assertEquals(KrashUtil.getDate("Apr 27 2022 21:29:19"), felJdk8u332_2.getJdkBuildDate(),
                "Build date not correct.");
        assertTrue(felJdk8u332_1.isRhBuildDate(), "Red Hat build date incorrectly identified.");
        assertTrue(felJdk8u332_2.isRhBuildDate(), "Red Hat build date incorrectly identified.");
    }

    @Test
    void testWindowsOracleJdk8() {
        File testFile = new File(Constants.TEST_DATA_DIR + "dataset12.txt");
        Manager manager = new Manager();
        FatalErrorLog fel = manager.parse(testFile);
        assertEquals(0, fel.getUnidentifiedLogLines().size(), "Unidentified log lines.");
        assertTrue(fel.isWindows(), "OS not identified as Windows.");
        assertTrue(fel.hasAnalysis(Analysis.WARN_DEBUG_SYMBOLS.getKey()),
                Analysis.WARN_DEBUG_SYMBOLS + " analysis not identified.");
        assertFalse(fel.hasAnalysis(Analysis.INFO_RH_BUILD_WINDOWS_ZIP.getKey()),
                Analysis.INFO_RH_BUILD_WINDOWS_ZIP + " analysis incorrectly identified.");
        assertEquals(Arch.X86_64, fel.getArch(), "Arch not correct.");
        assertEquals("1.8.0_25-b18", fel.getJdkReleaseString(), "JDK release not correct.");
        assertEquals(JavaVendor.ORACLE, fel.getJavaVendor(), "Java vendor not correct.");
        assertEquals(1, fel.getNativeLibraries().size(), "Native library count not correct.");
        assertEquals(0, fel.getNativeLibrariesUnknown().size(), "Native library unknown count not correct.");
    }

    @Test
    void testWindowsPageFile() {
        FatalErrorLog fel = new FatalErrorLog();
        Memory memory1 = new Memory("Memory: 4k page, system-wide physical 114687M (1028M free)");
        Memory memory2 = new Memory("TotalPageFile size 229376M (AvailPageFile size 6785M)");
        fel.getMemories().add(memory1);
        fel.getMemories().add(memory2);
        long physicalMemory = JdkUtil.convertSize(114687, 'M', 'B');
        assertEquals(physicalMemory, fel.getOsMemTotal(), "System physical memory not correct.");
        long physicalMemoryFree = JdkUtil.convertSize(1028, 'M', 'B');
        assertEquals(physicalMemoryFree, fel.getOsMemFree(), "System physical memory free not correct.");
        long swap = JdkUtil.convertSize(229376, 'M', 'B');
        assertEquals(swap, fel.getOsSwap(), "System swap not correct.");
        long swapFree = JdkUtil.convertSize(6785, 'M', 'B');
        assertEquals(swapFree, fel.getOsSwapFree(), "System swap free not correct.");
    }

    @Test
    void testWindowsRedHatJdk11() {
        File testFile = new File(Constants.TEST_DATA_DIR + "dataset13.txt");
        Manager manager = new Manager();
        FatalErrorLog fel = manager.parse(testFile);
        assertEquals(0, fel.getUnidentifiedLogLines().size(), "Unidentified log lines.");
        assertTrue(fel.isWindows(), "OS not identified as Windows.");
        assertTrue(fel.hasAnalysis(Analysis.WARN_DEBUG_SYMBOLS.getKey()),
                Analysis.WARN_DEBUG_SYMBOLS + " analysis not identified.");
        assertTrue(fel.hasAnalysis(Analysis.INFO_RH_BUILD_WINDOWS_ZIP.getKey()),
                Analysis.INFO_RH_BUILD_WINDOWS_ZIP + " analysis not identified.");
        assertEquals(Arch.X86_64, fel.getArch(), "Arch not correct.");
        assertEquals("11.0.7+10-LTS", fel.getJdkReleaseString(), "JDK release not correct.");
        assertEquals(JavaVendor.RED_HAT, fel.getJavaVendor(), "Java vendor not correct.");
        assertEquals(1, fel.getNativeLibraries().size(), "Native library count not correct.");
        assertEquals(0, fel.getNativeLibrariesUnknown().size(), "Native library unknown count not correct.");
    }

    @Test
    void testZ() {
        FatalErrorLog fel = new FatalErrorLog();
        String heap = " ZHeap           used 39991M, capacity 47104M, max capacity 47104M";
        Heap heapEvent = new Heap(heap);
        fel.getHeaps().add(heapEvent);
        long heapUsed = JdkUtil.convertSize(39991, 'M', 'B');
        assertEquals(heapUsed, fel.getHeapUsed(), "Heap used not correct.");
        long heapAllocation = JdkUtil.convertSize(47104, 'M', 'B');
        assertEquals(heapAllocation, fel.getHeapAllocation(), "Heap allocation not correct.");
        long heapMax = JdkUtil.convertSize(47104, 'M', 'B');
        assertEquals(heapMax, fel.getHeapMaxSize(), "Heap max size not correct.");
    }
}