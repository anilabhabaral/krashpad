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

import static java.math.RoundingMode.HALF_EVEN;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.github.joa.JvmOptions;
import org.github.joa.domain.Arch;
import org.github.joa.domain.Bit;
import org.github.joa.domain.BuiltBy;
import org.github.joa.domain.GarbageCollector;
import org.github.joa.domain.JvmContext;
import org.github.joa.domain.Os;
import org.github.krashpad.util.Constants.CpuArch;
import org.github.krashpad.util.Constants.Device;
import org.github.krashpad.util.Constants.OsVendor;
import org.github.krashpad.util.Constants.OsVersion;
import org.github.krashpad.util.ErrUtil;
import org.github.krashpad.util.jdk.Analysis;
import org.github.krashpad.util.jdk.JdkMath;
import org.github.krashpad.util.jdk.JdkRegEx;
import org.github.krashpad.util.jdk.JdkUtil;
import org.github.krashpad.util.jdk.JdkUtil.Application;
import org.github.krashpad.util.jdk.JdkUtil.CompressedOopMode;
import org.github.krashpad.util.jdk.JdkUtil.JavaSpecification;
import org.github.krashpad.util.jdk.JdkUtil.JavaVendor;
import org.github.krashpad.util.jdk.JdkUtil.SignalCode;
import org.github.krashpad.util.jdk.JdkUtil.SignalNumber;

/**
 * Fatal error log data.
 * 
 * @author <a href="mailto:mmillson@redhat.com">Mike Millson</a>
 * 
 */
public class FatalErrorLog {

    /**
     * Analysis.
     */
    private List<Analysis> analysis;

    /**
     * Classes unloaded information.
     */
    private List<ClassesUnloadedEvent> classesUnloadedEvents;

    /**
     * Command line information.
     */
    private CommandLine commandLine;

    /**
     * Compilation event information.
     */
    private List<CompilationEvent> compilationEvents;

    /**
     * Compressed class space information
     */
    private CompressedClassSpace compressedClassSpaceEvent;

    /**
     * Container information.
     */
    private List<ContainerInfo> containerInfos;

    /**
     * CPU information.
     */
    private List<CpuInfo> cpuInfos;

    /**
     * Current compile task information.
     */
    private List<CurrentCompileTask> currentCompileTasks;

    /**
     * Current thread information.
     */
    private CurrentThread currentThread;

    /**
     * Deoptimization event information.
     */
    private List<DeoptimizationEvent> deoptimizationEvents;

    /**
     * Dll operation information.
     */
    private List<DllOperationEvent> dllOperationEvents;

    /**
     * Dynamic library information.
     */
    private List<DynamicLibrary> dynamicLibraries;

    /**
     * JVM run duration information in JDK8.
     */
    private ElapsedTime elapsedTime;

    /**
     * Environment variables information.
     */
    private List<EnvironmentVariable> environmentVariables;

    /**
     * Vm event information.
     */
    private List<Event> events;

    /**
     * Exception counts information.
     */
    private List<ExceptionCounts> exceptionCounts;

    /**
     * GC heap history information.
     */
    private List<GcHeapHistoryEvent> gcHeapHistoryEvents;

    /**
     * GC precious log information.
     */
    private List<GcPreciousLog> gcPreciousLogs;

    /**
     * Global flag information.
     */
    private List<GlobalFlag> globalFlags;

    /**
     * Header.
     */
    private List<Header> headers;

    /**
     * Heap address information
     */
    private HeapAddress heapAddress;

    /**
     * Heap information.
     */
    private List<Heap> heaps;

    /**
     * Host information.
     */
    private Host host;

    /**
     * Exceptions information.
     */
    private List<InternalExceptionEvent> internalExceptionEvents;

    /**
     * Statistics information.
     */
    private List<InternalStatistic> internalStatistics;

    /**
     * JVMOptions object.
     */
    private JvmOptions jvmOptions;

    /**
     * Preloaded library information.
     */
    private List<LdPreloadFile> ldPreloadFiles;

    /**
     * max_map_count information.
     */
    private MaxMapCount maxMapCount;

    /**
     * Memory information.
     */
    private List<Meminfo> meminfos;

    /**
     * Memory information.
     */
    private List<Memory> memories;

    /**
     * Narrow klass information
     */
    private NarrowKlass narrowKlass;

    /**
     * Native memory tracking information.
     */
    private List<NativeMemoryTracking> nativeMemoryTrackings;

    /**
     * OS information.
     */
    private List<OsInfo> osInfos;

    /**
     * pid_max information.
     */
    private PidMax pidMax;

    /**
     * Register to memory mapping information.
     */
    private List<RegisterToMemoryMapping> registerToMemoryMappings;

    /**
     * rlimit information.
     */
    private Rlimit rlimit;

    /**
     * Signal information.
     */
    private SigInfo sigInfo;

    /**
     * Stack information.
     */
    private List<Stack> stacks;

    /**
     * Stack slot to memory mapping information.
     */
    private List<StackSlotToMemoryMapping> stackSlotToMemoryMappings;

    /**
     * Thread information.
     */
    private List<Thread> threads;

    /**
     * threads-max information.
     */
    private ThreadsMax threadsMax;

    /**
     * JVM crash time information.
     */
    private Time time;

    /**
     * Combined time + elapsed time information.
     */
    private TimeElapsedTime timeElapsedTime;

    /**
     * JVM crash time timezone information in JDK8.
     */
    private Timezone timezone;

    /**
     * uname information.
     */
    private Uname uname;

    /**
     * Log lines that do not match any existing logging patterns.
     */
    private List<String> unidentifiedLogLines;

    /**
     * VMware information.
     */
    private List<VirtualizationInfo> virtualizationInfos;

    /**
     * VM arguments information.
     */
    private List<VmArguments> vmArguments;

    /**
     * JVM environment information.
     */
    private VmInfo vmInfo;

    /**
     * VM operation information.
     */
    private VmOperation vmOperation;

    /**
     * VM state information.
     */
    private VmState vmState;

    /*
     * Default constructor.
     */
    public FatalErrorLog() {
        analysis = new ArrayList<Analysis>();
        classesUnloadedEvents = new ArrayList<ClassesUnloadedEvent>();
        compilationEvents = new ArrayList<CompilationEvent>();
        containerInfos = new ArrayList<ContainerInfo>();
        cpuInfos = new ArrayList<CpuInfo>();
        currentCompileTasks = new ArrayList<CurrentCompileTask>();
        deoptimizationEvents = new ArrayList<DeoptimizationEvent>();
        dllOperationEvents = new ArrayList<DllOperationEvent>();
        dynamicLibraries = new ArrayList<DynamicLibrary>();
        environmentVariables = new ArrayList<EnvironmentVariable>();
        events = new ArrayList<Event>();
        exceptionCounts = new ArrayList<ExceptionCounts>();
        gcHeapHistoryEvents = new ArrayList<GcHeapHistoryEvent>();
        gcPreciousLogs = new ArrayList<GcPreciousLog>();
        globalFlags = new ArrayList<GlobalFlag>();
        headers = new ArrayList<Header>();
        heaps = new ArrayList<Heap>();
        internalExceptionEvents = new ArrayList<InternalExceptionEvent>();
        internalStatistics = new ArrayList<InternalStatistic>();
        ldPreloadFiles = new ArrayList<LdPreloadFile>();
        meminfos = new ArrayList<Meminfo>();
        memories = new ArrayList<Memory>();
        nativeMemoryTrackings = new ArrayList<NativeMemoryTracking>();
        osInfos = new ArrayList<OsInfo>();
        registerToMemoryMappings = new ArrayList<RegisterToMemoryMapping>();
        stacks = new ArrayList<Stack>();
        stackSlotToMemoryMappings = new ArrayList<StackSlotToMemoryMapping>();
        threads = new ArrayList<Thread>();
        unidentifiedLogLines = new ArrayList<String>();
        virtualizationInfos = new ArrayList<VirtualizationInfo>();
        vmArguments = new ArrayList<VmArguments>();
    ***REMOVED***

    /**
     * Convenience method to add <code>Analysis</code>.
     * 
     * @param key
     *            The <code>Analysis</code> to dd.
     */
    public void addAnalysis(Analysis key) {
        analysis.add(key);
    ***REMOVED***

    /**
     * Convenience method to add <code>org.github.joa.util.Analysis</code>.
     * 
     * @param key
     *            The <code>org.github.joa.util.Analysis</code> to add.
     */
    public void addAnalysis(org.github.joa.util.Analysis key) {
        if (jvmOptions == null) {
            // Create JvmOptions for analysis
            JvmContext context = new JvmContext(null);
            JvmOptions options = new JvmOptions(context);
            jvmOptions = options;
        ***REMOVED***
        jvmOptions.addAnalysis(key);
    ***REMOVED***

    /**
     * Do analysis.
     */
    public void doAnalysis() {
        String opts = getJvmArgs();
        if (opts != null) {
            JvmContext context = new JvmContext(opts, getJavaVersionMajor(), getJavaVersionMinor());
            context.setGarbageCollectors(getGarbageCollectorsFromHeapEvents());
            context.setContainer(isContainer());
            context.setOs(getOs());
            context.setBit(getBit());
            context.setContainer(isContainer());
            context.setMemory(getOsMemTotal());
            jvmOptions = new JvmOptions(context);
            jvmOptions.doAnalysis();
        ***REMOVED*** else {
            analysis.add(0, Analysis.INFO_OPTS_NONE);
        ***REMOVED***
        // Unidentified logging lines
        if (JdkUtil.getJavaSpecificationNumber(getJavaSpecification()) >= 8 && !getUnidentifiedLogLines().isEmpty()) {
            analysis.add(0, Analysis.WARN_UNIDENTIFIED_LOG_LINE);
        ***REMOVED***
        // Crashes related to Oracle JDBC OCI (native) driver
        if (getStackFrameTop() != null && getStackFrameTop().matches("^C  \\[libocijdbc.+$")) {
            analysis.add(Analysis.ERROR_ORACLE_JDBC_OCI_DRIVER);
        ***REMOVED***
        if (getEventTimestamp("^Event: (\\d{1,***REMOVED***\\.\\d{3***REMOVED***) Loaded shared library .+libocijdbc.+.(dll|so)$") > 0
                && getUptime() > 0 && getUptime() - getEventTimestamp(
                        "^Event: (\\d{1,***REMOVED***\\.\\d{3***REMOVED***) Loaded shared library .+libocijdbc.+.(dll|so)$") <= 1000) {
            analysis.add(Analysis.ERROR_ORACLE_JDBC_OCI_LOADING);
        ***REMOVED***
        if (getStackFrame(2) != null && getStackFrame(2).matches("^C  \\[libocijdbc.+$")) {
            analysis.add(Analysis.WARN_ORACLE_JDBC_OCI_CONNECION);
        ***REMOVED***
        // Check Oracle JDBC driver / JDK compatibility
        List<String> nativeLibrariesUnknown = getNativeLibrariesUnknown();
        if (!nativeLibrariesUnknown.isEmpty()) {
            Iterator<String> iterator = nativeLibrariesUnknown.iterator();
            Pattern pattern = Pattern.compile(JdkRegEx.ORACLE_JDBC_OCI_DRIVER_PATH);
            Matcher matcher;
            while (iterator.hasNext()) {
                String nativeLibraryPath = iterator.next();
                matcher = pattern.matcher(nativeLibraryPath);
                if (matcher.find()) {
                    if (!analysis.contains(Analysis.INFO_ORACLE_JDBC_OCI)
                            && !analysis.contains(Analysis.ERROR_ORACLE_JDBC_OCI_DRIVER)
                            && !analysis.contains(Analysis.ERROR_ORACLE_JDBC_OCI_LOADING)
                            && !analysis.contains(Analysis.WARN_ORACLE_JDBC_OCI_CONNECION)) {
                        analysis.add(Analysis.INFO_ORACLE_JDBC_OCI);
                    ***REMOVED***
                    String versionRegEx = "^.*[/\\\\]oracle[/\\\\]product[/\\\\](\\d{1,***REMOVED***)\\.\\d{1,***REMOVED***\\.\\d{1,***REMOVED***"
                            + "(\\.\\d{1,***REMOVED***)?[/\\\\].*$";
                    Pattern pattern2 = Pattern.compile(versionRegEx);
                    Matcher matcher2 = pattern2.matcher(nativeLibraryPath);
                    if (matcher2.find()) {
                        Integer oracleDatabaseVersion = Integer.parseInt(matcher2.group(1));
                        if (JdkUtil.getJavaSpecificationNumber(getJavaSpecification()) > 11
                                && oracleDatabaseVersion < 21) {
                            analysis.add(Analysis.ERROR_ORACLE_JDBC_JDK_INCOMPATIBLE);
                            break;
                        ***REMOVED***
                    ***REMOVED***
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        // Check PostgreSQL JDBC driver / JDK8 compatibility
        List<String> jars = getJars();
        if (!jars.isEmpty()) {
            String postgresqlJdbcDriverPath = null;
            Iterator<String> iterator = jars.iterator();
            while (iterator.hasNext()) {
                String jar = iterator.next();
                if (jar.matches(JdkRegEx.POSTGRESQL_JDBC_DRIVER_PATH)) {
                    postgresqlJdbcDriverPath = jar;
                    break;
                ***REMOVED***
            ***REMOVED***
            if (postgresqlJdbcDriverPath != null) {
                Pattern pattern = Pattern.compile(JdkRegEx.POSTGRESQL_JDBC_DRIVER_PATH);
                Matcher matcher = pattern.matcher(postgresqlJdbcDriverPath);
                if (matcher.find()) {
                    Integer minorVersion = Integer.parseInt(matcher.group(3));
                    if (JdkUtil.getJavaSpecificationNumber(getJavaSpecification()) == 8 && minorVersion < 5) {
                        analysis.add(Analysis.ERROR_POSTGRESQL_JDBC_JDK8_INCOMPATIBLE);
                    ***REMOVED***
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        // Check for ancient fatal error log
        if (ErrUtil.dayDiff(getCrashDate(), new Date()) > 30) {
            analysis.add(Analysis.WARN_FATAL_ERROR_LOG_ANCIENT);
        ***REMOVED***
        // Check for ancient JDK
        if (getJdkReleaseDate() != null && ErrUtil.dayDiff(getJdkReleaseDate(), new Date()) > 365) {
            analysis.add(Analysis.INFO_JDK_ANCIENT);
        ***REMOVED***
        // Check for unknown JDK version
        if (getJavaSpecification() == JavaSpecification.UNKNOWN) {
            analysis.add(Analysis.ERROR_JDK_VERSION_UNKNOWN);
        ***REMOVED***
        // Check for unsupported JDK version
        if (getJavaSpecification() == JavaSpecification.JDK6 || getJavaSpecification() == JavaSpecification.JDK7) {
            analysis.add(Analysis.ERROR_JDK_VERSION_UNSUPPORTED);
        ***REMOVED***
        // Check for JVM failing to start
        if (isCrashOnStartup()) {
            analysis.add(Analysis.INFO_JVM_STARTUP_FAILS);
        ***REMOVED***
        // Check if JDK debugging symbols are installed
        if ((haveVmFrameInStack() || haveVmFrameInHeader()) && !haveJdkDebugSymbols()) {
            analysis.add(Analysis.WARN_DEBUG_SYMBOLS);
        ***REMOVED***
        // Check if latest JDK release
        if (!JdkUtil.isLatestJdkRelease(this)) {
            analysis.add(0, Analysis.WARN_JDK_NOT_LATEST);
        ***REMOVED***
        // Identify vendor/build
        if (isRhBuildOpenJdk()) {
            if (getOs() == Os.LINUX) {
                if (getOsVendor() == OsVendor.CENTOS) {
                    // CentOs redistributes RH build of OpenJDK
                    analysis.add(0, Analysis.INFO_RH_BUILD_CENTOS);
                ***REMOVED*** else if (getRpmDirectory() != null) {
                    analysis.add(0, Analysis.INFO_RH_BUILD_RPM_INSTALL);
                    if (getCpuArch() == CpuArch.POWER9 && getJavaSpecification() == JavaSpecification.JDK8
                            && getOsString().matches(".+7\\.(7|8|9).+")) {
                        // power8 JDK8 deployed on power9 on RHEL 7
                        analysis.add(Analysis.ERROR_JDK8_RHEL7_POWER8_RPM_ON_POWER9);
                    ***REMOVED***
                ***REMOVED*** else if (isRhRpm()) {
                    analysis.add(0, Analysis.INFO_RH_BUILD_RPM_BASED);
                ***REMOVED*** else {
                    analysis.add(0, Analysis.INFO_RH_BUILD_LINUX_ZIP);
                ***REMOVED***
                // Check for RHEL6
                if (getOsVersion() == OsVersion.RHEL6) {
                    analysis.add(Analysis.WARN_RHEL6);
                ***REMOVED***
                // Check for RHEL7 Power9
                if (getOsVersion() == OsVersion.RHEL7 && (getArch() == Arch.PPC64 || getArch() == Arch.PPC64LE)) {
                    analysis.add(Analysis.WARN_RHEL7_POWER9);
                ***REMOVED***
                // Check for unnecessary use of -XX:+UnlockExperimentalVMOptions with Shenandoah on RH build.
                if (getJvmOptions() != null) {
                    if (JdkUtil.isOptionEnabled(getJvmOptions().getUseShenandoahGc())
                            && JdkUtil.isOptionEnabled(getJvmOptions().getUnlockExperimentalVmOptions())) {
                        analysis.add(Analysis.INFO_RH_OPT_EXPERIMENTAL_SHENANDOAH);
                    ***REMOVED***
                ***REMOVED***
            ***REMOVED*** else if (isWindows()) {
                analysis.add(0, Analysis.INFO_RH_BUILD_WINDOWS_ZIP);
            ***REMOVED***
        ***REMOVED*** else {
            if ((vmInfo == null || isRhBuildString()) && isRhVersion() && (isRhBuildDate() || isRhBuildDateUnknown())) {
                analysis.add(Analysis.INFO_RH_BUILD_POSSIBLE);
            ***REMOVED*** else if (isAdoptOpenJdkBuildString()) {
                analysis.add(Analysis.INFO_ADOPTOPENJDK_POSSIBLE);
            ***REMOVED*** else if (vmInfo != null || getJavaVendor() == JavaVendor.NOT_RED_HAT) {
                analysis.add(0, Analysis.INFO_RH_BUILD_NOT);
            ***REMOVED***
        ***REMOVED***
        // Check if there is vm code in the stack
        if (haveFramesInStack() && !haveVmCodeInStack()) {
            analysis.add(Analysis.INFO_STACK_NO_VM_CODE);
        ***REMOVED***
        if (getJavaSpecification() != JavaSpecification.UNKNOWN && !isJdkLts()) {
            analysis.add(Analysis.WARN_JDK_NOT_LTS);
        ***REMOVED***
        // JNA
        if (isJnaCrash()) {
            if (getStackFrameTop() != null && getStackFrameTop().matches("^C  .+ffi_prep_closure_loc.+$")) {
                analysis.add(Analysis.ERROR_JNA_FFI_PREP_CLOSURE_LOC);
            ***REMOVED*** else if (getJavaVendor() == JavaVendor.RED_HAT) {
                analysis.add(Analysis.ERROR_JNA_RH);
            ***REMOVED*** else {
                analysis.add(Analysis.ERROR_JNA);
            ***REMOVED***
        ***REMOVED*** else {
            Iterator<DynamicLibrary> iterator = dynamicLibraries.iterator();
            while (iterator.hasNext()) {
                DynamicLibrary event = iterator.next();
                if (event.getFilePath() != null && event.getFilePath().matches("^.+[\\\\/](jna|JNA).+$")) {
                    analysis.add(Analysis.INFO_JNA);
                    break;
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        // Check for JDK8 ZipFile contention
        if (JdkUtil.getJavaSpecificationNumber(getJavaSpecification()) >= 6
                && JdkUtil.getJavaSpecificationNumber(getJavaSpecification()) <= 8) {
            if ((getStackFrameTopCompiledJavaCode() != null && getStackFrameTopCompiledJavaCode()
                    .matches("^.+java\\.util\\.zip\\.ZipFile\\.(getEntry|open).+$"))
                    || (getStackFrameTop() != null
                            && getStackFrameTop().matches("^C[ ]{1,***REMOVED***\\[libzip\\.so.*\\][ ]{1,***REMOVED***readCEN.*$"))) {
                analysis.add(Analysis.ERROR_JDK8_ZIPFILE_CONTENTION);
            ***REMOVED***
        ***REMOVED***
        // Check for JDK8 Deflator contention
        if (getJavaSpecification() == JavaSpecification.JDK8 && getStackFrameTopCompiledJavaCode() != null
                && getStackFrameTopCompiledJavaCode().matches("^.+java\\.util\\.zip\\.Deflater\\.deflateBytes.+$")) {
            analysis.add(Analysis.ERROR_JDK8_DEFLATER_CONTENTION);
        ***REMOVED***
        // Check for unsynchronized access to DirectByteBuffer
        String regexStubRoutines = "^v  ~(BufferBlob::)?StubRoutines.*$";
        if (getStackFrameTop() != null && getStackFrameTop().matches(regexStubRoutines)) {
            if (isInStack(JdkRegEx.JAVA_NIO_BYTEBUFFER)) {
                analysis.add(Analysis.ERROR_DIRECT_BYTE_BUFFER_CONTENTION);
            ***REMOVED*** else if (isInStack("(com[\\./]itextpdf[\\./](text[\\./])?io|"
                    + "com[\\./]itextpdf[\\./]text[\\./]pdf[\\./]RandomAccessFileOrArray)")) {
                analysis.add(Analysis.ERROR_ITEXT_IO);
            ***REMOVED*** else {
                analysis.add(Analysis.ERROR_STUBROUTINES);
            ***REMOVED***
        ***REMOVED***
        // Check for insufficient physical memory
        if (getHeapMaxSize() > 0 && getMetaspaceMaxSize() > 0 && getJvmMemTotal() > 0
                && (getHeapMaxSize() + getMetaspaceMaxSize()) > getJvmMemTotal()) {
            if (getOsSwap() == 0 || getJvmSwap() == 0) {
                analysis.add(Analysis.WARN_HEAP_PLUS_METASPACE_GT_PHYSICAL_MEMORY_NOSWAP);
            ***REMOVED*** else {
                analysis.add(Analysis.WARN_HEAP_PLUS_METASPACE_GT_PHYSICAL_MEMORY_SWAP);
            ***REMOVED***
        ***REMOVED***
        // CrashOnOutOfMemoryError
        if (jvmOptions != null && JdkUtil.isOptionEnabled(jvmOptions.getCrashOnOutOfMemoryError())
                && isError("OutOfMemory encountered: Java heap space")) {
            analysis.add(Analysis.ERROR_CRASH_ON_OOME_HEAP);
        ***REMOVED***
        // OOME
        if (isMemoryAllocationFail()) {
            if (isCrashOnStartup()) {
                if (getApplication() == Application.TOMCAT_SHUTDOWN) {
                    analysis.add(Analysis.ERROR_OOME_TOMCAT_SHUTDOWN);
                ***REMOVED*** else if (getApplication() == Application.JBOSS_VERSION) {
                    analysis.add(Analysis.ERROR_OOME_JBOSS_VERSION);
                ***REMOVED*** else if (getApplication() == Application.AMQ_CLI) {
                    analysis.add(Analysis.ERROR_OOME_AMQ_CLI);
                ***REMOVED*** else if (getCommitLimit() >= 0 && getCommittedAs() >= 0) {
                    long allocation = Long.MIN_VALUE;
                    allocation = getMemoryAllocation();
                    if (allocation < 0) {
                        // Use JVM estimated initial process size
                        allocation = getJvmMemoryInitial();
                    ***REMOVED***
                    if (allocation > 0 && allocation > (getCommitLimit() - getCommittedAs()) && !isOvercommitted()) {
                        // Strong evidence for vm.overcommit_memory=2, but possible resource limit
                        analysis.add(Analysis.ERROR_OOME_OVERCOMMIT_LIMIT_STARTUP);
                        if (jvmOptions == null
                                || (jvmOptions.getInitialHeapSize() != null && jvmOptions.getMaxHeapSize() != null
                                        && (JdkUtil.getByteOptionBytes(JdkUtil.getByteOptionValue(
                                                jvmOptions.getInitialHeapSize())) == JdkUtil.getByteOptionBytes(
                                                        JdkUtil.getByteOptionValue(jvmOptions.getMaxHeapSize()))))) {
                            analysis.add(Analysis.INFO_OOME_STARTUP_HEAP_MIN_EQUAL_MAX);
                        ***REMOVED***
                    ***REMOVED*** else {
                        // Resource limit
                        analysis.add(Analysis.ERROR_OOME_LIMIT_STARTUP);
                    ***REMOVED***
                ***REMOVED*** else if (getJvmMemoryInitial() >= 0 && (getJvmMemFree() >= 0 || getOsMemAvailable() >= 0)
                        && getJvmSwapFree() >= 0) {
                    if (getJvmMemoryInitial() > (Math.max(getJvmMemFree(), getOsMemAvailable()) + getJvmSwapFree())) {
                        // Insufficient physical memory for JVM estimated initial process size
                        if (JdkMath.calcPercent(getJvmMemoryInitial(), getOsMemTotal()) < 50) {
                            analysis.add(Analysis.ERROR_OOME_EXTERNAL_STARTUP);
                        ***REMOVED*** else {
                            analysis.add(Analysis.ERROR_OOME_JVM_STARTUP);
                        ***REMOVED***
                        if (jvmOptions == null
                                || (jvmOptions.getInitialHeapSize() != null && jvmOptions.getMaxHeapSize() != null
                                        && (JdkUtil.getByteOptionBytes(JdkUtil.getByteOptionValue(
                                                jvmOptions.getInitialHeapSize())) == JdkUtil.getByteOptionBytes(
                                                        JdkUtil.getByteOptionValue(jvmOptions.getMaxHeapSize()))))) {
                            analysis.add(Analysis.INFO_OOME_STARTUP_HEAP_MIN_EQUAL_MAX);
                        ***REMOVED***
                    ***REMOVED*** else {
                        // Resource limit
                        analysis.add(Analysis.ERROR_OOME_LIMIT_STARTUP);
                    ***REMOVED***
                ***REMOVED***
                // Don't double report the JVM failing to start
                analysis.remove(Analysis.INFO_JVM_STARTUP_FAILS);
            ***REMOVED*** else {
                // Crash after startup
                if (getThreadStackMemory() > 0 && getJvmMemTotal() > 0
                        && JdkMath.calcPercent(getThreadStackMemory(), getJvmMemTotal()) > 50) {
                    // thread leak
                    int executorPoolThreadCount = getJavaThreadCount(JdkRegEx.WILDFLY_EXECUTOR_POOL_THREAD);
                    if (executorPoolThreadCount > 0 && getJavaThreadCount() > 0
                            && JdkMath.calcPercent(executorPoolThreadCount, getJavaThreadCount()) > 50) {
                        analysis.add(Analysis.ERROR_OOME_THREAD_LEAK_EAP_EXECUTOR_POOL);
                    ***REMOVED*** else {
                        analysis.add(Analysis.ERROR_OOME_THREAD_LEAK);
                    ***REMOVED***
                ***REMOVED*** else if (getMemoryAllocation() >= 0 && getCommitLimit() >= 0 && getCommittedAs() >= 0
                        && getMemoryAllocation() > (getCommitLimit() - getCommittedAs())
                        && getCommitLimit() >= getCommittedAs()) {
                    // Allocation > available commit limit and CommitLimit >= Committed_AS
                    analysis.add(Analysis.ERROR_OOME_OVERCOMMIT_LIMIT);
                ***REMOVED*** else if (getMemoryAllocation() >= 0 && (getJvmMemFree() >= 0 || getJvmSwapFree() >= 0)
                        && getMemoryAllocation() >= (getJvmMemFree() + getJvmSwapFree())) {
                    // Allocation > available physical memory
                    if (getJvmMemoryMax() > 0 && getJvmMemTotal() > 0) {
                        if (JdkMath.calcPercent(getJvmMemoryMax(), getJvmMemTotal()) >= 95) {
                            analysis.add(Analysis.ERROR_OOME_JVM);
                        ***REMOVED*** else {
                            if (getCommitCharge() > 0
                                    && JdkMath.calcPercent(getCommitCharge(), getJvmMemTotal()) < 95) {
                                // Windows has a good process size approximation
                                if (getMemBalloonedNow() > 0) {
                                    analysis.add(Analysis.ERROR_OOME_EXTERNAL_OR_HYPERVISOR);
                                ***REMOVED*** else {
                                    analysis.add(Analysis.ERROR_OOME_EXTERNAL);
                                ***REMOVED***
                            ***REMOVED*** else {
                                analysis.add(Analysis.ERROR_OOME_NATIVE_OR_EXTERNAL);
                            ***REMOVED***
                        ***REMOVED***
                    ***REMOVED***
                ***REMOVED*** else if ((getJvmMemFree() >= 0 && getJvmMemTotal() > 0
                        && JdkMath.calcPercent(getJvmMemFree(), getJvmMemTotal()) >= 50)
                        || (getJvmMemoryMax() >= 0 && getJvmMemTotal() > 0
                                && JdkMath.calcPercent(getJvmMemoryMax(), getJvmMemTotal()) < 50)) {
                    // Likely a limit when JVM memory < 1/2 total memory
                    if (isInHeader("Java Heap may be blocking the growth of the native heap")
                            || isInHeader("compressed oops")) {
                        analysis.add(Analysis.ERROR_OOME_LIMIT_OOPS);
                    ***REMOVED*** else {
                        analysis.add(Analysis.ERROR_OOME_LIMIT);
                    ***REMOVED***
                ***REMOVED*** else {
                    if (!(isTruncated() || isInHeader("Java Heap may be blocking the growth of the native heap")
                            || isInHeader("compressed oops"))) {
                        analysis.add(Analysis.ERROR_OOME);
                    ***REMOVED*** else {
                        analysis.add(Analysis.ERROR_OOME_OOPS);
                    ***REMOVED***
                ***REMOVED***
            ***REMOVED***
            // G1 collector is not good when memory is tight
            if (getGarbageCollectors().contains(GarbageCollector.G1)) {
                analysis.add(Analysis.WARN_OOM_G1);
            ***REMOVED***
        ***REMOVED***
        // swap
        if (getJvmSwap() > 0) {
            // Check for excessive swap usage
            int swapUsedPercent = 100 - JdkMath.calcPercent(getJvmSwapFree(), getJvmSwap());
            if (swapUsedPercent > 5 && swapUsedPercent < 20) {
                analysis.add(Analysis.INFO_SWAPPING);
            ***REMOVED*** else if (swapUsedPercent >= 20) {
                analysis.add(Analysis.WARN_SWAPPING);
            ***REMOVED***
        ***REMOVED***
        // Check for swap disabled
        if (getJvmSwap() == 0) {
            analysis.add(Analysis.INFO_SWAP_DISABLED);
            // Check if collector is appropriate for no-swap (e.g. container) use cases
            if (getGarbageCollectors().contains(GarbageCollector.G1) && getJvmSwap() == 0) {
                analysis.add(Analysis.WARN_SWAP_DISABLED_G1);
            ***REMOVED*** else if (getGarbageCollectors().contains(GarbageCollector.CMS) && getJvmSwap() == 0) {
                analysis.add(Analysis.WARN_SWAP_DISABLED_CMS);
            ***REMOVED***
        ***REMOVED***
        // libjvm.so/jvm.dll
        if (getJavaSpecification() == JavaSpecification.JDK8 && JdkUtil.getJdk8UpdateNumber(getJdkReleaseString()) > 0
                && JdkUtil.getJdk8UpdateNumber(getJdkReleaseString()) < 282 && getStackFrameTop() != null
                && getStackFrameTop()
                        .matches("^V  \\[(libjvm\\.so|jvm\\.dll).+\\]  ShenandoahUpdateRefsClosure::do_oop.+$")) {
            analysis.add(Analysis.ERROR_JDK8_SHENANDOAH_ROOT_UPDATER);
        ***REMOVED*** else if (getJavaSpecification() == JavaSpecification.JDK8
                && JdkUtil.getJdk8UpdateNumber(getJdkReleaseString()) > 0
                && JdkUtil.getJdk8UpdateNumber(getJdkReleaseString()) < 312 && getStackFrameTop() != null
                && getStackFrameTop()
                        .matches("^V  \\[(libjvm\\.so|jvm\\.dll).+\\]  MetadataOnStackMark::~MetadataOnStackMark.+$")) {
            analysis.add(Analysis.ERROR_JDK8_SHENANDOAH_METADATA_ON_STACK_MARK);
        ***REMOVED*** else if (getCurrentThreadName() != null && getCurrentThreadName().matches("^.+CompilerThread\\d{1,***REMOVED***.+$")) {
            analysis.add(Analysis.ERROR_COMPILER_THREAD);
        ***REMOVED*** else if (getStackFrameTop() != null
                && getStackFrameTop().matches("^V  \\[(libjvm\\.so|jvm\\.dll).+\\]  (ModuleEntry::purge_reads|"
                        + "ModuleEntryTable::purge_all_module_reads).+$")) {
            analysis.add(Analysis.ERROR_MODULE_ENTRY_PURGE_READS);
        ***REMOVED*** else if (getJavaSpecification() == JavaSpecification.JDK8
                && JdkUtil.getJdk8UpdateNumber(getJdkReleaseString()) >= 262
                && JdkUtil.getJdk8UpdateNumber(getJdkReleaseString()) < 282 && getStackFrameTop() != null
                && getStackFrameTop().matches("^V.+JfrEventClassTransformer::on_klass_creation.+$")) {
            analysis.add(Analysis.ERROR_JDK8_JFR_CLASS_TRANSFORMED);
        ***REMOVED*** else if (getStackFrameTop() != null
                && !isError("There is insufficient memory for the Java Runtime Environment to continue")
                && !isError("***REMOVED***  fatal error: OutOfMemory encountered: Java heap space")) {
            // Other libjvm.so/jvm.dll analysis
            if (getStackFrameTop().matches("^V  \\[libjvm\\.so.+\\](.+)?$")) {
                analysis.add(Analysis.ERROR_LIBJVM_SO);
            ***REMOVED*** else if (getStackFrameTop().matches("^V  \\[jvm\\.dll.+\\](.+)?$")) {
                analysis.add(Analysis.ERROR_JVM_DLL);
            ***REMOVED***
        ***REMOVED***
        // Signal numbers
        switch (getSignalNumber()) {
        case EXCEPTION_ACCESS_VIOLATION:
            analysis.add(Analysis.INFO_SIGNO_EXCEPTION_ACCESS_VIOLATION);
            break;
        case SIGBUS:
            analysis.add(Analysis.INFO_SIGNO_SIGBUS);
            break;
        case SIGFPE:
            analysis.add(Analysis.INFO_SIGNO_SIGFPE);
            break;
        case SIGILL:
            analysis.add(Analysis.INFO_SIGNO_SIGILL);
            break;
        case SIGSEGV:
            analysis.add(Analysis.INFO_SIGNO_SIGSEGV);
            break;
        case UNKNOWN:
        default:
            break;
        ***REMOVED***
        // Signal codes
        switch (getSignalCode()) {
        case BUS_ADRALN:
            analysis.add(Analysis.INFO_SIGCODE_BUS_ADRALN);
            break;
        case BUS_ADRERR:
            if (getOs() == Os.LINUX) {
                analysis.add(Analysis.INFO_SIGCODE_BUS_ADDERR_LINUX);
            ***REMOVED*** else {
                analysis.add(Analysis.INFO_SIGCODE_BUS_ADDERR);
            ***REMOVED***
            break;
        case BUS_OBJERR:
            analysis.add(Analysis.INFO_SIGCODE_BUS_OBJERR);
            break;
        case FPE_INTDIV:
            analysis.add(Analysis.INFO_SIGCODE_FPE_INTDIV);
            break;
        case ILL_ILLOPN:
            analysis.add(Analysis.INFO_SIGCODE_ILL_ILLOPN);
            break;
        case SEGV_ACCERR:
            analysis.add(Analysis.INFO_SIGCODE_SEGV_ACCERR);
            break;
        case SEGV_MAPERR:
            analysis.add(Analysis.INFO_SIGCODE_SEGV_MAPERR);
            break;
        case SI_KERNEL:
            analysis.add(Analysis.INFO_SIGCODE_SI_KERNEL);
            break;
        case SI_USER:
            analysis.add(Analysis.INFO_SIGCODE_SI_USER);
            break;
        case UNKNOWN:
        default:
            break;
        ***REMOVED***
        // Floating point error
        if (getSignalNumber().equals(SignalNumber.SIGFPE) || getSignalCode().equals(SignalCode.FPE_INTDIV)) {
            analysis.add(Analysis.ERROR_FPE);
        ***REMOVED***
        // pthread_getcpuclockid
        if (getStackFrameTop() != null
                && getStackFrameTop().matches("^C  \\[libpthread\\.so.+\\]  pthread_getcpuclockid.+$")) {
            analysis.add(Analysis.ERROR_PTHREAD_GETCPUCLOCKID);
        ***REMOVED***
        // BufferBlob::flush_icache_stub
        if (getStackFrameTop() != null && getStackFrameTop().matches("^v  ~BufferBlob::flush_icache_stub+$")) {
            analysis.add(Analysis.ERROR_BUFFERBLOB_FLUSH_ICACHE_STUB);
        ***REMOVED***
        // StackOverflowError
        if (haveStackOverFlowError()) {
            analysis.add(Analysis.ERROR_STACKOVERFLOW);
        ***REMOVED*** else {
            if (getThreadStackFreeSpace() > getThreadStackSize()) {
                // Applies only to ThreadStackSize (not CompilerThreadStackSize, VMThreadStackSize, MarkStackSize, the
                // JLI_Launch method in main.c that starts the JVM, or C code).
                if (currentThread != null && !(currentThread.isCompilerThread() || currentThread.isVmThread())
                        && stacks.size() > 0 && !isInStack("JLI_Launch")) {
                    analysis.add(Analysis.INFO_STACK_FREESPACE_GT_STACK_SIZE);
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        // LinkageError
        if (haveLinkageError()) {
            analysis.add(Analysis.ERROR_LINKAGE);
        ***REMOVED***
        // Thread stack size
        long threadStackMaxSize = getThreadStackSize();
        if ((jvmOptions == null || !hasAnalysis(org.github.joa.util.Analysis.WARN_THREAD_STACK_SIZE_TINY.getKey()))
                && threadStackMaxSize < 1) {
            addAnalysis(org.github.joa.util.Analysis.WARN_THREAD_STACK_SIZE_TINY);
        ***REMOVED*** else if ((jvmOptions == null
                || !hasAnalysis(org.github.joa.util.Analysis.WARN_THREAD_STACK_SIZE_SMALL.getKey()))
                && threadStackMaxSize < 128) {
            addAnalysis(org.github.joa.util.Analysis.WARN_THREAD_STACK_SIZE_SMALL);
        ***REMOVED***
        // OutOfMemoryError other than "Metaspace" or "Compressed class space" caught and thrown
        if (haveOomeThrownJavaHeap()) {
            analysis.add(Analysis.ERROR_OOME_THROWN_JAVA_HEAP);
        ***REMOVED***
        // "OutOfMemoryError: Metaspace" caught and thrown
        if (haveOomeThrownMetaspace()) {
            analysis.add(Analysis.ERROR_OOME_THROWN_METASPACE);
        ***REMOVED***
        // "OutOfMemoryError: Compressed class space" caught and thrown
        if (haveOomeThrownCompressedClassSpace()) {
            analysis.add(Analysis.ERROR_OOME_THROWN_COMP_CLASS_SPACE);
        ***REMOVED***
        // Stubroutines
        if ((getStackFrameTop() != null && getStackFrameTop().matches("^v  ~BufferBlob::StubRoutines.*"))
                || isError("v  ~BufferBlob::StubRoutines")) {
            analysis.add(Analysis.ERROR_STUBROUTINES);
        ***REMOVED***
        // ShenandoahConcurrentMark::mark_loop_work
        if ((getStackFrameTop() != null
                && getStackFrameTop().matches("^.+ShenandoahConcurrentMark::mark_loop_work.+"))) {
            analysis.add(Analysis.ERROR_JDK8_SHENANDOAH_MARK_LOOP_WORK);
        ***REMOVED***
        // org.apache.activemq.artemis.nativo.jlibaio.LibaioContext.done()
        if ((getStackFrameTop() != null && getStackFrameTop()
                .matches("^.+ org.apache.activemq.artemis.nativo.jlibaio.LibaioContext.done().+"))) {
            analysis.add(Analysis.ERROR_LIBAIO_CONTEXT_DONE);
        ***REMOVED***
        // container
        if (!getContainerInfos().isEmpty() && getJvmSwap() == 0) {
            analysis.add(Analysis.INFO_CGROUP);
        ***REMOVED***
        if (getJvmMemTotal() > 0 && getOsMemTotal() > 0 && getJvmMemTotal() != getOsMemTotal()) {
            analysis.add(Analysis.INFO_MEMORY_JVM_NE_SYSTEM);
            if (haveCgroupMemoryLimit()) {
                analysis.add(Analysis.INFO_CGROUP_MEMORY_LIMIT);
            ***REMOVED***
        ***REMOVED***
        // truncated fatal error log
        if (isTruncated()) {
            analysis.add(Analysis.INFO_TRUNCATED);
        ***REMOVED***
        // Storage analysis
        if (getOs() == Os.LINUX && !dynamicLibraries.isEmpty()) {
            switch (getStorageDevice()) {
            case AWS_BLOCK_STORAGE:
                analysis.add(Analysis.INFO_STORAGE_AWS);
                break;
            case NFS:
                analysis.add(Analysis.INFO_STORAGE_NFS);
                break;
            case UNIDENTIFIED:
                analysis.add(Analysis.INFO_STORAGE_UNKNOWN);
                break;
            default:
                break;
            ***REMOVED***
        ***REMOVED***
        // Check for explicit gc disabled on EAP7
        if (getApplication() == Application.JBOSS_EAP7 && jvmOptions != null
                && JdkUtil.isOptionEnabled(jvmOptions.getDisableExplicitGc())) {
            // Don't double report
            if (hasAnalysis(org.github.joa.util.Analysis.WARN_EXPLICIT_GC_DISABLED.getKey())) {
                jvmOptions.removeAnalysis(org.github.joa.util.Analysis.WARN_EXPLICIT_GC_DISABLED);
            ***REMOVED***
            analysis.add(Analysis.ERROR_EXPLICIT_GC_DISABLED_EAP7);
        ***REMOVED***
        // Check for CMS incremental mode with > 2 cpu
        if (getCpusLogical() > 2 && jvmOptions != null && !JdkUtil.isOptionDisabled(jvmOptions.getUseConcMarkSweepGc())
                && JdkUtil.isOptionEnabled(jvmOptions.getCmsIncrementalMode())) {
            analysis.add(Analysis.WARN_CMS_INCREMENTAL_MODE);
        ***REMOVED***
        // Check for crash caused trying to dereference a null pointer.
        if (sigInfo != null && sigInfo.getSignalAddress() != null) {
            if (sigInfo.getSignalAddress().matches(JdkRegEx.POINTER_NULL)) {
                analysis.add(Analysis.ERROR_POINTER_NULL);
            ***REMOVED*** else if (sigInfo.getSignalAddress().matches(JdkRegEx.POINTER_INVALID)) {
                analysis.add(Analysis.ERROR_POINTER_INVALID);
            ***REMOVED***
        ***REMOVED***
        if (getStackFrameTop() != null && getStackFrameTop()
                .matches("J \\d{1,***REMOVED*** C2 java\\.lang\\.String\\.compareTo\\(Ljava/lang/Object;\\)I")) {
            if (getCpuInfos().size() > 0) {

            ***REMOVED***
        ***REMOVED***
        // Crashes in Java compiled code
        if (getStackFrameTop() != null && getStackFrameTop().matches("^J \\d{1,***REMOVED***%{0,1***REMOVED*** C[12].+$")) {
            if (getStackFrameTop().matches("^.+java\\.lang\\.String\\.compareTo\\(Ljava\\/lang/Object;\\)I.+$")
                    && hasCpuCapability("avx2") && (getJvmOptions() == null || !(getJvmOptions().getUseAvx() != null
                            && getJvmOptions().getUseAvx().equals("-XX:UseAVX=0")))) {
                analysis.add(Analysis.ERROR_AVX2_STRING_COMPARE_TO);
            ***REMOVED*** else {
                analysis.add(Analysis.ERROR_COMPILED_JAVA_CODE);
                if (hasCpuCapability("avx2") && (getJvmOptions() == null || (getJvmOptions().getUseAvx() != null
                        && !getJvmOptions().getUseAvx().equals("-XX:UseAVX=0")))) {
                    analysis.add(Analysis.INFO_COMPILED_JAVA_CODE_AVX2);
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        // Check for possible JFFI usage
        if (!dynamicLibraries.isEmpty()) {
            Iterator<DynamicLibrary> iterator = dynamicLibraries.iterator();
            while (iterator.hasNext()) {
                DynamicLibrary event = iterator.next();
                if (event.getFilePath() != null && event.getFilePath().matches("^.+[\\\\/](jffi|JFFI).+$")) {
                    analysis.add(Analysis.INFO_JFFI);
                    break;
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        // Check for JVM crash due to temporary font file being removed from java.io.tmpdir.
        if (getStackFrameTopJava() != null
                && getStackFrameTopJava().matches("^.+sun\\.font\\.FreetypeFontScaler\\.getGlyphImageNative.+$")) {
            analysis.add(Analysis.ERROR_FREETYPE_FONT_SCALER_GET_GLYPH_IMAGE_NATIVE);
        ***REMOVED***
        // Check for JDK8 Deflator contention
        if ((getArch().equals(Arch.PPC64) || getArch().equals(Arch.PPC64LE)) && getStackFrameTop() != null
                && getStackFrameTop().matches("^V.+JavaThread::pd_get_top_frame_for_profiling.+$")) {
            analysis.add(Analysis.ERROR_JFR_PD_GET_TOP_FRAME);
        ***REMOVED***

        // Cannot get library information
        if (!dynamicLibraries.isEmpty()) {
            Iterator<DynamicLibrary> iterator = dynamicLibraries.iterator();
            while (iterator.hasNext()) {
                DynamicLibrary event = iterator.next();
                if (event.getLogEntry().matches("^Can not get library information for pid = \\d{1,***REMOVED***$")) {
                    analysis.add(Analysis.ERROR_CANNOT_GET_LIBRARY_INFORMATION);
                    break;
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        // Check for G1ParScanThreadState::copy_to_survivor_space
        if (getStackFrameTop() != null
                && getStackFrameTop().matches("^V.+G1ParScanThreadState::copy_to_survivor_space.+$")
                && (getJavaVersionMajor() == 8 || (getJavaVersionMajor() == 11 && getJavaVersionMinor() < 10))) {
            analysis.add(Analysis.ERROR_G1_PAR_SCAN_THREAD_STATE_COPY_TO_SURVIVOR_SPACE);
            // Don't double report
            analysis.remove(Analysis.ERROR_LIBJVM_SO);
        ***REMOVED***
        // Check for PSPromotionManager::copy_to_survivor_space
        if (getStackFrameTop() != null
                && getStackFrameTop().matches("^V.+PSPromotionManager::copy_to_survivor_space.+$")) {
            analysis.add(Analysis.ERROR_PS_PROMOTION_MANAGER_COPY_TO_SURVIVOR_SPACE);
            // Don't double report
            analysis.remove(Analysis.ERROR_LIBJVM_SO);
        ***REMOVED***
        // Check if JVM user ne USERNAME
        if (getJvmUser() != null && getUsername() != null && !getJvmUser().equals(getUsername())) {
            analysis.add(Analysis.INFO_JVM_USER_NE_USERNAME);
        ***REMOVED***
        // Check for many threads
        if (getJavaThreadCount() > 1000) {
            if (getJavaThreadCount() > 5000) {
                analysis.add(Analysis.WARN_THREADS_MANY);
            ***REMOVED*** else {
                analysis.add(Analysis.INFO_THREADS_MANY);
            ***REMOVED***
        ***REMOVED***
        // Check environments
        if (isVMWareEnvironment()) {
            analysis.add(Analysis.INFO_VMWARE);
        ***REMOVED*** else if (isHyperVEnvironment()) {
            analysis.add(Analysis.INFO_HYPERV);
        ***REMOVED***
        // Check for mmap resources in deleted state
        if (!getDynamicLibraries().isEmpty() && getMmapDeletedCount() > 0) {
            analysis.add(Analysis.WARN_MMAP_DELETED);
        ***REMOVED***
        // Check for RHEL/JDK rpm version mismatch
        if (isRhRpmInstall() && getRhelVersion() != null && getJdkRhelVersion() != null) {
            if ((getJdkRhelVersion().indexOf('.') != -1 && !getRhelVersion().matches(getJdkRhelVersion()))
                    || (getJdkRhelVersion().indexOf('.') == -1
                            && !getRhelVersion().startsWith((getJdkRhelVersion())))) {
                analysis.add(0, Analysis.ERROR_RHEL_JDK_RPM_MISMATCH);
                if (analysis.contains(Analysis.WARN_JDK_NOT_LATEST)) {
                    analysis.remove(Analysis.WARN_JDK_NOT_LATEST);
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        // Crash in HashMap
        if (getStackFrameTop() != null && getStackFrameTop().matches("^J.+java\\.util\\.HashMap.+$")) {
            analysis.add(Analysis.ERROR_HASHMAP);
        ***REMOVED***
        // Specific CompilerThread crashes
        if (getCurrentThreadName() != null && getCurrentThreadName().matches("^.+C2 CompilerThread\\d{1,***REMOVED***.+$")) {
            if (isInHeader("guarantee\\(n != NULL\\) failed: No Node.") && isInStack("IdealLoopTree::beautify_loops")) {
                analysis.add(Analysis.ERROR_COMPILER_THREAD_C2_BEAUTIFY_LOOPS);
                // Don't double report
                analysis.remove(Analysis.ERROR_COMPILER_THREAD);
            ***REMOVED*** else if (getStackFrameTop() != null
                    && getStackFrameTop().matches("^.*MinINode::Ideal\\(PhaseGVN\\*, bool\\).*$")
                    && (getJavaSpecification() == JavaSpecification.JDK8
                            && JdkUtil.getJdk8UpdateNumber(getJdkReleaseString()) > 0
                            && JdkUtil.getJdk8UpdateNumber(getJdkReleaseString()) < 275
                            || getJavaSpecification() == JavaSpecification.JDK11
                                    && JdkUtil.getJdk8UpdateNumber(getJdkReleaseString()) > 0
                                    && JdkUtil.getJdk8UpdateNumber(getJdkReleaseString()) < 9)) {
                analysis.add(Analysis.ERROR_COMPILER_THREAD_C2_MININODE_IDEAL);
                // Don't double report
                analysis.remove(Analysis.ERROR_COMPILER_THREAD);
            ***REMOVED*** else if (!getCurrentCompileTasks().isEmpty()) {
                Iterator<CurrentCompileTask> iterator = getCurrentCompileTasks().iterator();
                while (iterator.hasNext()) {
                    CurrentCompileTask event = iterator.next();
                    if (event.getLogEntry() != null && event.getLogEntry()
                            .matches("^.+sun\\.security\\.ssl\\.SSLEngineInputRecord::decodeInputRecord.+")) {
                        if ((getJavaSpecification() == JavaSpecification.JDK11
                                && JdkUtil.getJdk11UpdateNumber(getJdkReleaseString()) == 16)
                                || (getJavaSpecification() == JavaSpecification.JDK17
                                        && JdkUtil.getJdk17UpdateNumber(getJdkReleaseString()) == 4)) {
                            analysis.add(Analysis.ERROR_OOME_COMPILER_THREAD_C2_SSL_DECODE);
                            // Don't double report
                            analysis.remove(Analysis.ERROR_COMPILER_THREAD);
                            analysis.remove(Analysis.ERROR_OOME_NATIVE_OR_EXTERNAL);
                            analysis.remove(Analysis.ERROR_OOME_JVM);
                        ***REMOVED***
                        break;
                    ***REMOVED***
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        // Crash during shutdown
        if (!getEvents().isEmpty()) {
            Event lastEventEvent = getEvents().get(getEvents().size() - 1);
            if (lastEventEvent.getLogEntry().matches("^.+Executing VM operation: Exit$")) {
                analysis.add(Analysis.INFO_SHUTDOWN);
            ***REMOVED***
        ***REMOVED***
        // iText
        if (isInStack("com\\.itextpdf\\.text")) {
            analysis.add(0, Analysis.WARN_ITEXT);
        ***REMOVED*** else if (!dynamicLibraries.isEmpty()) {
            Iterator<DynamicLibrary> iterator = dynamicLibraries.iterator();
            while (iterator.hasNext()) {
                DynamicLibrary event = iterator.next();
                if (event.getLogEntry() != null && event.getLogEntry().matches("^.+itext.*\\.jar$")) {
                    analysis.add(Analysis.INFO_ITEXT);
                    break;
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        // Lucene
        if (isInStack("org\\.apache\\.lucene\\.")) {
            analysis.add(0, Analysis.WARN_LUCENE);
        ***REMOVED***
        // IBM Toolkit
        if (!dynamicLibraries.isEmpty()) {
            Iterator<DynamicLibrary> iterator = dynamicLibraries.iterator();
            while (iterator.hasNext()) {
                DynamicLibrary event = iterator.next();
                if (event.getLogEntry() != null && event.getLogEntry().matches("^.+jt400\\.jar$")) {
                    analysis.add(Analysis.INFO_IBM_TOOLKIT);
                    break;
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        // ERROR_JDK8_LIBC_CFREE
        if (getJavaSpecification() == JavaSpecification.JDK8
                && JdkUtil.getJdk8UpdateNumber(getJdkReleaseString()) < 262) {
            if (!headers.isEmpty()) {
                Iterator<Header> iterator = headers.iterator();
                while (iterator.hasNext()) {
                    Header event = iterator.next();
                    if (event.isProblematicFrame() && event.getLogEntry().matches("^.+libc.+cfree\\+0x1c$")
                            && getJvmOptions().getUseGcLogFileRotation() != null
                            && getCurrentThreadName().matches("^ConcurrentGCThread .+$")) {
                        analysis.add(Analysis.ERROR_JDK8_LIBC_CFREE);
                        break;
                    ***REMOVED***
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        // VM operations
        if (vmOperation != null) {
            if (vmOperation.getVmOperation().equals("BulkRevokeBias")) {
                analysis.add(Analysis.INFO_VM_OPERATION_BULK_REVOKE_BIAS);
            ***REMOVED*** else if (vmOperation.getVmOperation().equals("CGC_Operation")) {
                analysis.add(Analysis.INFO_VM_OPERATION_CONCURRENT_GC);
            ***REMOVED*** else if (vmOperation.getVmOperation().equals("GetThreadListStackTraces")) {
                analysis.add(Analysis.WARN_VM_OPERATION_THREAD_DUMP_JVMTI);
            ***REMOVED*** else if (vmOperation.getVmOperation().equals("HeapDumper")) {
                analysis.add(Analysis.INFO_VM_OPERATION_HEAP_DUMP);
            ***REMOVED*** else if (vmOperation.getVmOperation().equals("PrintThreads")) {
                analysis.add(Analysis.INFO_VM_OPERATION_THREAD_DUMP);
            ***REMOVED***
        ***REMOVED***
        // DBCP2
        String orgApacheCommonsDbcp2 = "org[\\.\\/]apache[\\.\\/]commons[\\.\\/]dbcp2[\\.\\/]";
        if (isInStack(orgApacheCommonsDbcp2)) {
            analysis.add(Analysis.INFO_DBCP2);
        ***REMOVED***
        // PostgreSQL connection
        String postgreSqlConnection = "org[\\.\\/]postgresql[\\.\\/]Driver[\\.\\/]connect\\(";
        if (isInStack(postgreSqlConnection)) {
            analysis.add(Analysis.INFO_POSTGRESQL_CONNECTION);
        ***REMOVED***
        // ld.so.preload
        if (!getLdPreloadFiles().isEmpty()) {
            analysis.add(Analysis.INFO_LD_SO_PRELOAD);
        ***REMOVED***
        // Unknown native libraries
        if (!getNativeLibrariesUnknown().isEmpty()) {
            analysis.add(Analysis.INFO_NATIVE_LIBRARIES_UNKNOWN);
        ***REMOVED***
        // Dynatrace
        if (getStackFrameTop() != null
                && getStackFrameTop().matches("^.*" + JdkRegEx.NATIVE_LIBRARY_DYNATRACE + ".*$")) {
            // Crash in Dynatrace
            analysis.add(Analysis.ERROR_DYNATRACE);
        ***REMOVED*** else if (isInStack(JdkRegEx.NATIVE_LIBRARY_DYNATRACE)) {
            // Dynatrace in stack
            analysis.add(0, Analysis.WARN_DYNATRACE);
        ***REMOVED*** else if (!getNativeLibrariesUnknown().isEmpty()) {
            // Dynatrace detected
            Iterator<String> iterator = getNativeLibrariesUnknown().iterator();
            Pattern pattern = Pattern.compile(JdkRegEx.FILE);
            Matcher matcher;
            while (iterator.hasNext()) {
                String nativeLibraryPath = iterator.next();
                matcher = pattern.matcher(nativeLibraryPath);
                if (matcher.find()) {
                    String nativeLibrary = matcher.group(3);
                    if (nativeLibrary.matches(JdkRegEx.NATIVE_LIBRARY_DYNATRACE)) {
                        analysis.add(Analysis.INFO_DYNATRACE);
                        break;
                    ***REMOVED***
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        // Wily/DX APM
        if ((getStackFrameTop() != null && getStackFrameTop().matches("^.*" + JdkRegEx.NATIVE_LIBRARY_WILY + ".*$"))
                || (getStackFrameTopJava() != null
                        && getStackFrameTopJava().matches("^.+ com\\.wily\\.introscope\\..+"))) {
            // Crash in Wily
            analysis.add(Analysis.ERROR_WILY);
        ***REMOVED*** else if (isInStack(" com\\.wily\\.introscope\\.")) {
            // Wily in stack
            analysis.add(0, Analysis.WARN_WILY);
        ***REMOVED*** else if (!getNativeLibrariesUnknown().isEmpty()) {
            // Wily detected
            Iterator<String> iterator = getNativeLibrariesUnknown().iterator();
            Pattern pattern = Pattern.compile(JdkRegEx.FILE);
            Matcher matcher;
            while (iterator.hasNext()) {
                String nativeLibraryPath = iterator.next();
                matcher = pattern.matcher(nativeLibraryPath);
                if (matcher.find()) {
                    String nativeLibrary = matcher.group(3);
                    if (nativeLibrary.matches(JdkRegEx.NATIVE_LIBRARY_WILY)) {
                        analysis.add(Analysis.INFO_WILY);
                        break;
                    ***REMOVED***
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        // AppDynamics
        if (getJvmOptions() != null && getJvmOptions().getJavaagent() != null) {
            Iterator<String> iterator = getJvmOptions().getJavaagent().iterator();
            while (iterator.hasNext()) {
                String javaagent = iterator.next();
                if (javaagent.matches(JdkRegEx.JAVAAGENT_APP_DYNAMICS)) {
                    analysis.add(Analysis.INFO_APP_DYNAMICS_DETECTED);
                    break;
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        if (!analysis.contains(Analysis.INFO_APP_DYNAMICS_DETECTED) && compilationEvents != null) {
            Iterator<CompilationEvent> iterator = compilationEvents.iterator();
            while (iterator.hasNext()) {
                CompilationEvent event = iterator.next();
                if (event.getLogEntry().matches("^.*" + JdkRegEx.PACKAGE_APP_DYNAMICS + ".*$")) {
                    analysis.add(Analysis.INFO_APP_DYNAMICS_DETECTED);
                    break;
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        if (!analysis.contains(Analysis.INFO_APP_DYNAMICS_DETECTED) && getJvmOptions() != null
                && getJvmOptions().getJavaagent() != null) {
            Iterator<String> iterator = getJvmOptions().getJavaagent().iterator();
            while (iterator.hasNext()) {
                String javaagent = iterator.next();
                if (javaagent.endsWith(JdkRegEx.JAR_APP_DYNAMICS)) {
                    analysis.add(Analysis.INFO_APP_DYNAMICS_POSSIBLE);
                    break;
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        // RHEL9 + JDK8
        if (getOsVersion() == OsVersion.RHEL9 && getJavaSpecification() == JavaSpecification.JDK8) {
            analysis.add(Analysis.INFO_RHEL9_JDK8);
        ***REMOVED***
        // Crash in 3rd party or unknown library
        if (getNativeLibraryInCrash() != null) {
            if (!getNativeLibrariesUnknown().isEmpty()) {
                Iterator<String> iterator = getNativeLibrariesUnknown().iterator();
                while (iterator.hasNext()) {
                    String unknownNativeLibary = iterator.next();
                    if (unknownNativeLibary.contains(getNativeLibraryInCrash())) {
                        analysis.add(Analysis.ERROR_CRASH_NATIVE_LIBRARY_UNKNOWN);
                        break;
                    ***REMOVED***
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        // pki_tomcat
        if (getApplication() == Application.PKI_TOMCAT) {
            analysis.add(Analysis.INFO_PKI_TOMCAT);
        ***REMOVED***
        // JSS
        if ((getStackFrameTop() != null && getStackFrameTop().matches("^.*" + JdkRegEx.NATIVE_LIBRARY_JSS + ".*$"))
                || (getStackFrameTopJava() != null && getStackFrameTopJava().matches("^.+ org\\.mozilla\\.jss\\..+"))) {
            // Crash in JSS
            analysis.add(Analysis.ERROR_JSS);
        ***REMOVED*** else if (isInStack(" org\\.mozilla\\.jss\\.")) {
            // JSS in stack
            analysis.add(0, Analysis.WARN_JSS);
        ***REMOVED*** else if (!getNativeLibraries().isEmpty()) {
            // JSS detected
            Iterator<String> iterator = getNativeLibraries().iterator();
            Pattern pattern = Pattern.compile(JdkRegEx.FILE);
            Matcher matcher;
            while (iterator.hasNext()) {
                String nativeLibraryPath = iterator.next();
                matcher = pattern.matcher(nativeLibraryPath);
                if (matcher.find()) {
                    String nativeLibrary = matcher.group(3);
                    if (nativeLibrary.matches(JdkRegEx.NATIVE_LIBRARY_JSS)) {
                        analysis.add(Analysis.INFO_JSS);
                        break;
                    ***REMOVED***
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        // Microsoft SQL Server native driver
        if (!getNativeLibrariesUnknown().isEmpty()) {
            Iterator<String> iterator = getNativeLibrariesUnknown().iterator();
            Pattern pattern = Pattern.compile(JdkRegEx.FILE);
            Matcher matcher;
            while (iterator.hasNext()) {
                String nativeLibraryPath = iterator.next();
                matcher = pattern.matcher(nativeLibraryPath);
                if (matcher.find()) {
                    String nativeLibrary = matcher.group(3);
                    if (nativeLibrary.matches(JdkRegEx.NATIVE_LIBRARY_MICROSOFT_SQL_SERVER)) {
                        analysis.add(Analysis.INFO_MICROSOFT_SQL_SERVER_NATIVE);
                        break;
                    ***REMOVED***
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        // Detective work when debug symbols are missing but many clues exist
        if ((analysis.contains(Analysis.ERROR_JVM_DLL) || analysis.contains(Analysis.ERROR_LIBJVM_SO))
                && analysis.contains(Analysis.WARN_DEBUG_SYMBOLS) && (analysis.contains(Analysis.ERROR_POINTER_NULL)
                        || analysis.contains(Analysis.ERROR_POINTER_INVALID))) {
            if (analysis.contains(Analysis.INFO_APP_DYNAMICS_DETECTED)
                    || analysis.contains(Analysis.INFO_APP_DYNAMICS_POSSIBLE)
                            && analysis.contains(Analysis.INFO_VM_OPERATION_CONCURRENT_GC)) {
                if (getJavaSpecification() == JavaSpecification.JDK11
                        && JdkUtil.getJdk11UpdateNumber(getJdkReleaseString()) <= 12) {
                    analysis.add(Analysis.ERROR_MODULE_ENTRY_PURGE_READS_POSSIBLE);
                    analysis.remove(Analysis.ERROR_JVM_DLL);
                    analysis.remove(Analysis.ERROR_LIBJVM_SO);
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        // JBoss native library detection
        if (!getNativeLibrariesJBoss().isEmpty()) {
            analysis.add(Analysis.INFO_NATIVE_LIBRARIES_JBOSS);
        ***REMOVED***
    ***REMOVED***

    /**
     * @return Analysis as a <code>List</code> of String arrays with 2 elements, the first the key, the second the
     *         display literal.
     */
    public List<String[]> getAnalysis() {
        List<String[]> a = new ArrayList<String[]>();
        Iterator<Analysis> itFelAnalysis = analysis.iterator();
        while (itFelAnalysis.hasNext()) {
            Analysis item = itFelAnalysis.next();
            if (item.getKey().equals(Analysis.ERROR_CRASH_NATIVE_LIBRARY_UNKNOWN.toString())) {
                StringBuffer s = new StringBuffer(item.getValue());
                s.append(getNativeLibraryInCrash());
                s.append(".");
                a.add(new String[] { item.getKey(), s.toString() ***REMOVED***);
            ***REMOVED*** else if (item.getKey().equals(Analysis.ERROR_CRASH_ON_OOME_HEAP.toString())) {
                StringBuffer s = new StringBuffer(item.getValue());
                s.append(" Check the following location for a heap dump: ");
                s.append(getJvmOptions().getHeapDumpPath());
                s.append(".");
                a.add(new String[] { item.getKey(), s.toString() ***REMOVED***);
            ***REMOVED*** else if (item.getKey().equals(Analysis.INFO_JDK_ANCIENT.toString())) {
                StringBuffer s = new StringBuffer(item.getValue());
                String replace = ">1 yr";
                int position = s.toString().lastIndexOf(replace);
                StringBuffer with = new StringBuffer();
                BigDecimal years = new BigDecimal(ErrUtil.dayDiff(getJdkReleaseDate(), new Date()));
                years = years.divide(new BigDecimal(365), 1, HALF_EVEN);
                with.append(years.toString());
                with.append(" years");
                s.replace(position, position + replace.length(), with.toString());
                a.add(new String[] { item.getKey(), s.toString() ***REMOVED***);
            ***REMOVED*** else if (item.getKey().equals(Analysis.INFO_NATIVE_LIBRARIES_JBOSS.toString())) {
                StringBuffer s = new StringBuffer(item.getValue());
                Iterator<String> iterator = getNativeLibrariesJBoss().iterator();
                boolean punctuate = false;
                while (iterator.hasNext()) {
                    String library = iterator.next();
                    if (punctuate) {
                        s.append(", ");
                    ***REMOVED***
                    s.append(library);
                    punctuate = true;
                ***REMOVED***
                s.append(".");
                a.add(new String[] { item.getKey(), s.toString() ***REMOVED***);
            ***REMOVED*** else if (item.getKey().equals(Analysis.INFO_NATIVE_LIBRARIES_UNKNOWN.toString())) {
                StringBuffer s = new StringBuffer(item.getValue());
                Iterator<String> iterator = getNativeLibrariesUnknown().iterator();
                boolean punctuate = false;
                while (iterator.hasNext()) {
                    String library = iterator.next();
                    if (punctuate) {
                        s.append(", ");
                    ***REMOVED***
                    s.append(library);
                    punctuate = true;
                ***REMOVED***
                s.append(".");
                a.add(new String[] { item.getKey(), s.toString() ***REMOVED***);
            ***REMOVED*** else if (item.getKey().equals(Analysis.WARN_JDK_NOT_LATEST.toString())) {
                StringBuffer s = new StringBuffer(item.getValue());
                s.append(JdkUtil.getLatestJdkReleaseString(this));
                // Add latest release info
                int releaseDayDiff = ErrUtil.dayDiff(JdkUtil.getJdkReleaseDate(this),
                        JdkUtil.getLatestJdkReleaseDate(this));
                int releaseNumberDiff = JdkUtil.getLatestJdkReleaseNumber(this) - JdkUtil.getJdkReleaseNumber(this);
                if (releaseDayDiff > 0 && releaseNumberDiff > 0) {
                    s.append(" (newer by ");
                    s.append("" + releaseNumberDiff);
                    s.append(" version");
                    if (releaseNumberDiff > 1) {
                        s.append("s");
                    ***REMOVED***
                    s.append(" and ");
                    s.append("" + releaseDayDiff);
                    s.append(" day");
                    if (releaseDayDiff > 1) {
                        s.append("s");
                    ***REMOVED***
                    s.append(")");
                ***REMOVED***
                s.append(".");
                a.add(new String[] { item.getKey(), s.toString() ***REMOVED***);
            ***REMOVED*** else {
                a.add(new String[] { item.getKey(), item.getValue() ***REMOVED***);
            ***REMOVED***
        ***REMOVED***
        if (jvmOptions != null) {
            Iterator<String[]> itJvmOptionsAnalysis = jvmOptions.getAnalysis().iterator();
            while (itJvmOptionsAnalysis.hasNext()) {
                String[] item = itJvmOptionsAnalysis.next();
                if (item[0].equals(org.github.joa.util.Analysis.INFO_INSTRUMENTATION.toString())) {
                    StringBuffer s = new StringBuffer(item[1]);
                    Iterator<String> iterator = getJvmOptions().getJavaagent().iterator();
                    while (iterator.hasNext()) {
                        String option = iterator.next();
                        s.append(" ");
                        s.append(option);
                    ***REMOVED***
                    s.append(".");
                    a.add(new String[] { item[0], s.toString() ***REMOVED***);
                ***REMOVED*** else if (item[0].equals(org.github.joa.util.Analysis.INFO_NATIVE_AGENT.toString())) {
                    StringBuffer s = new StringBuffer(item[1]);
                    if (!getJvmOptions().getAgentlib().isEmpty()) {
                        Iterator<String> iterator = getJvmOptions().getAgentlib().iterator();
                        while (iterator.hasNext()) {
                            String option = iterator.next();
                            s.append(" ");
                            s.append(option);
                        ***REMOVED***
                    ***REMOVED***
                    if (!getJvmOptions().getAgentpath().isEmpty()) {
                        Iterator<String> iterator = getJvmOptions().getAgentpath().iterator();
                        while (iterator.hasNext()) {
                            String option = iterator.next();
                            s.append(" ");
                            s.append(option);
                        ***REMOVED***
                    ***REMOVED***
                    s.append(".");
                    a.add(new String[] { item[0], s.toString() ***REMOVED***);
                ***REMOVED*** else if (item[0].equals(org.github.joa.util.Analysis.INFO_OPTS_UNDEFINED.toString())) {
                    StringBuffer s = new StringBuffer(item[1]);
                    s.append(" Please submit an issue so we can investigate: "
                            + "https://github.com/mgm3746/krashpad/issues. "
                            + "If attaching a fatal error log, be sure to review it and remove any sensitive "
                            + "information.");
                    a.add(new String[] { item[0], s.toString() ***REMOVED***);
                ***REMOVED*** else {
                    a.add(item);
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        return a;
    ***REMOVED***

    /**
     * Convenience method to get the <code>Analysis</code> literal.
     * 
     * @param key
     *            The analysis identifier.
     * @return The analysisis display literal, or null if it does not exist.
     */
    public String getAnalysisLiteral(String key) {
        String literal = null;
        Iterator<String[]> i = getAnalysis().iterator();
        while (i.hasNext()) {
            String[] item = i.next();
            if (item[0].equals(key)) {
                literal = item[1];
                break;
            ***REMOVED***
        ***REMOVED***
        return literal;
    ***REMOVED***

    /**
     * @return The application running on the JDK
     */
    public Application getApplication() {
        Application application = Application.UNKNOWN;
        // Check libraries
        if (!dynamicLibraries.isEmpty()) {
            Iterator<DynamicLibrary> iterator = dynamicLibraries.iterator();
            while (iterator.hasNext()) {
                DynamicLibrary event = iterator.next();
                if (event.getLogEntry().matches(JdkRegEx.JAR_JBOSS_EAP6)) {
                    application = Application.JBOSS_EAP6;
                    break;
                ***REMOVED*** else if (event.getLogEntry().matches(JdkRegEx.JAR_JBOSS_EAP7)) {
                    application = Application.JBOSS_EAP7;
                    break;
                ***REMOVED*** else if (event.getLogEntry().matches(JdkRegEx.JAR_JEUS)) {
                    application = Application.JEUS;
                    break;
                ***REMOVED*** else if (event.getLogEntry().matches(JdkRegEx.JAR_TOMCAT)) {
                    application = Application.TOMCAT;
                    // Continue checking for known applications built on top of tomcat
                ***REMOVED*** else if (event.getLogEntry().matches(JdkRegEx.JAR_PKI_TOMCAT)) {
                    application = Application.PKI_TOMCAT;
                    break;
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        // Check threads
        if (application == Application.UNKNOWN) {
            if (!threads.isEmpty()) {
                Iterator<Thread> iterator = threads.iterator();
                while (iterator.hasNext()) {
                    Thread event = iterator.next();
                    if (event.getLogEntry() != null && event.getLogEntry().matches(JdkRegEx.THREAD_JEUS)) {
                        application = Application.JEUS;
                        break;
                    ***REMOVED*** else if (event.getLogEntry() != null && event.getLogEntry().matches(JdkRegEx.THREAD_PKI_TOMCAT)) {
                        application = Application.PKI_TOMCAT;
                        break;
                    ***REMOVED*** else if (event.getLogEntry() != null && event.getLogEntry().matches(JdkRegEx.THREAD_RHSSO)) {
                        application = Application.RHSSO;
                        break;
                    ***REMOVED***
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        // Check java_command
        if (application == Application.UNKNOWN) {
            String javaCommand = getJavaCommand();
            if (javaCommand != null) {
                if (javaCommand.matches(JdkRegEx.ARTEMIS_COMMAND)) {
                    application = Application.AMQ;
                ***REMOVED*** else if (javaCommand.matches(JdkRegEx.COMMAND_ARTEMIS_CLI)) {
                    application = Application.AMQ_CLI;
                ***REMOVED*** else if (javaCommand.matches(JdkRegEx.COMMAND_CASSANDRA)) {
                    application = Application.CASSANDRA;
                ***REMOVED*** else if (javaCommand.matches(JdkRegEx.COMMAND_JBOSS_VERSION)) {
                    application = Application.JBOSS_VERSION;
                ***REMOVED*** else if (javaCommand.matches(JdkRegEx.COMMAND_JEUS)) {
                    application = Application.JEUS;
                ***REMOVED*** else if (javaCommand.matches(JdkRegEx.COMMAND_KAFKA)) {
                    application = Application.KAFKA;
                ***REMOVED*** else if (javaCommand.matches(JdkRegEx.COMMAND_SPRING_BOOT)) {
                    application = Application.SPRING_BOOT;
                ***REMOVED*** else if (javaCommand.matches(JdkRegEx.COMMAND_TOMCAT_START)) {
                    application = Application.TOMCAT;
                ***REMOVED*** else if (javaCommand.matches(JdkRegEx.COMMAND_TOMCAT_STOP)) {
                    application = Application.TOMCAT_SHUTDOWN;
                ***REMOVED*** else if (javaCommand.matches(JdkRegEx.JAR_WILDFLY)) {
                    application = Application.WILDFLY;
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        // Check JVM arguments
        if (application == Application.UNKNOWN) {
            if (getJvmArgs() != null && getJvmArgs().matches("^.*-Dcatalina.base=/var/lib/pki/pki-tomcat.*$")) {
                application = Application.PKI_TOMCAT;
            ***REMOVED***
        ***REMOVED***
        return application;
    ***REMOVED***

    /**
     * @return <code>Arch</code>
     */
    public Arch getArch() {
        Arch arch = Arch.UNKNOWN;
        if (uname != null) {
            arch = uname.getArch();
        ***REMOVED*** else if (vmInfo != null) {
            arch = vmInfo.getArch();
        ***REMOVED*** else if (!headers.isEmpty()) {
            // Check header
            Iterator<Header> iterator = headers.iterator();
            while (iterator.hasNext()) {
                Header he = iterator.next();
                if (he.isJavaVm()) {
                    if (he.getLogEntry().matches("^.+ppc64.+$")) {
                        arch = Arch.PPC64;
                    ***REMOVED*** else if (he.getLogEntry().matches("^.+ppc64le.+$")) {
                        arch = Arch.PPC64LE;
                    ***REMOVED*** else if (he.getLogEntry().matches("^.+solaris-sparc.+$")) {
                        arch = Arch.SPARC;
                    ***REMOVED*** else if (he.getLogEntry().matches("^.+amd64.+$")) {
                        arch = Arch.X86_64;
                    ***REMOVED***
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        return arch;
    ***REMOVED***

    /**
     * On Windows, the maximum amount of memory the current process can commit.
     * 
     * @return The maximum amount of memory the current process can commit, in
     *         <code>org.github.joa.util.Constants.PRECISION_REPORTING</code> units.
     */
    public long getAvailPageFile() {
        long availPageFile = Long.MIN_VALUE;
        if (!memories.isEmpty()) {
            String regexTotalPageFile = "TotalPageFile size \\d{1,***REMOVED***M \\(AvailPageFile size (\\d{1,***REMOVED***)M\\)";
            Pattern pattern = Pattern.compile(regexTotalPageFile);
            Iterator<Memory> iterator = memories.iterator();
            while (iterator.hasNext()) {
                Memory event = iterator.next();
                Matcher matcher = pattern.matcher(event.getLogEntry());
                if (matcher.find()) {
                    availPageFile = JdkUtil.convertSize(Long.parseLong(matcher.group(1)), 'M',
                            org.github.joa.util.Constants.UNITS);
                    break;
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        return availPageFile;
    ***REMOVED***

    /**
     * @return <code>Bit</code>
     */
    public Bit getBit() {
        Bit bit = Bit.UNKNOWN;
        if (getArch() == Arch.X86) {
            bit = Bit.BIT32;
        ***REMOVED***
        return bit;
    ***REMOVED***

    public List<ClassesUnloadedEvent> getClassesUnloadedEvents() {
        return classesUnloadedEvents;
    ***REMOVED***

    /**
     * @return The max code cache size in <code>org.github.joa.util.Constants.PRECISION_REPORTING</code> units.
     */
    public long getCodeCacheSize() {
        // Default is 420m
        long reservedCodeCacheSize = JdkUtil.convertSize(420, 'M', org.github.joa.util.Constants.UNITS);
        // 1st check [Global flags]
        if (!globalFlags.isEmpty()) {
            Iterator<GlobalFlag> iterator = globalFlags.iterator();
            while (iterator.hasNext()) {
                GlobalFlag event = iterator.next();
                String regExReservedCodeCacheSize = "^.+uintx ReservedCodeCacheSize[ ]{1,***REMOVED***= (\\d{1,***REMOVED***).+$";
                Pattern pattern = Pattern.compile(regExReservedCodeCacheSize);
                Matcher matcher = pattern.matcher(event.getLogEntry());
                if (matcher.find()) {
                    reservedCodeCacheSize = JdkUtil.convertSize(Long.parseLong(matcher.group(1)), 'B',
                            org.github.joa.util.Constants.UNITS);
                    break;
                ***REMOVED***
            ***REMOVED***
        ***REMOVED*** else if (jvmOptions != null
                && (jvmOptions.getReservedCodeCacheSize() != null || jvmOptions.getMaxjitcodesize() != null)) {
            char fromUnits;
            long value;
            Pattern pattern = Pattern.compile(JdkRegEx.OPTION_SIZE_BYTES);
            Matcher matcher;
            if (jvmOptions.getReservedCodeCacheSize() != null) {
                matcher = pattern.matcher(jvmOptions.getReservedCodeCacheSize());
            ***REMOVED*** else {
                matcher = pattern.matcher(jvmOptions.getMaxjitcodesize());
            ***REMOVED***
            if (matcher.find()) {
                value = Long.parseLong(matcher.group(2));
                if (matcher.group(3) != null) {
                    fromUnits = matcher.group(3).charAt(0);
                ***REMOVED*** else {
                    fromUnits = 'B';
                ***REMOVED***
                reservedCodeCacheSize = JdkUtil.convertSize(value, fromUnits, org.github.joa.util.Constants.UNITS);
            ***REMOVED***
        ***REMOVED***
        return reservedCodeCacheSize;
    ***REMOVED***

    public CommandLine getCommandLine() {
        return commandLine;
    ***REMOVED***

    /**
     * On Windows, the memory the process has asked for that cannot be shared with other processes. A good approximation
     * of the process size.
     * 
     * Can be physical memory, memory paged to disk, or memory in the standby page list (no longer in use, but not yet
     * paged to disk).
     * 
     * Excludes memory mapped files (shared DLLs), but does not necessarily exclude the memory allocated by those files.
     * 
     * @return the memory the process has asked for that cannot be shared with other processes, in
     *         <code>org.github.joa.util.Constants.PRECISION_REPORTING</code> units.
     */
    public long getCommitCharge() {
        long commitCharge = Long.MIN_VALUE;
        if (!memories.isEmpty()) {
            String regexTotalPageFile = "current process commit charge \\(\"private bytes\"\\): (\\d{1,***REMOVED***)M, peak: "
                    + "\\d{1,***REMOVED***M";
            Pattern pattern = Pattern.compile(regexTotalPageFile);
            Iterator<Memory> iterator = memories.iterator();
            while (iterator.hasNext()) {
                Memory event = iterator.next();
                Matcher matcher = pattern.matcher(event.getLogEntry());
                if (matcher.find()) {
                    commitCharge = JdkUtil.convertSize(Long.parseLong(matcher.group(1)), 'M',
                            org.github.joa.util.Constants.UNITS);
                    break;
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        return commitCharge;
    ***REMOVED***

    /**
     * The total amount of memory that can be allocated by the system, based on the overcommit ratio (vm.
     * overcommit_ratio).
     * 
     * Formula: ([total RAM pages] - [total huge TLB pages]) * overcommit_ratio / 100 + [total swap pages]
     * 
     * This limit is only adhered to if strict overcommit accounting is enabled (mode 2 in vm.overcommit_memory)
     * 
     * @return The total amount of memory currently available to be allocated by the system in
     *         <code>org.github.joa.util.Constants.PRECISION_REPORTING</code> units.
     */
    public long getCommitLimit() {
        long commitLimit = Long.MIN_VALUE;
        if (!meminfos.isEmpty()) {
            String regexMemTotal = "CommitLimit:[ ]{0,***REMOVED***(\\d{1,***REMOVED***) kB";
            Pattern pattern = Pattern.compile(regexMemTotal);
            Iterator<Meminfo> iterator = meminfos.iterator();
            while (iterator.hasNext()) {
                Meminfo event = iterator.next();
                Matcher matcher = pattern.matcher(event.getLogEntry());
                if (matcher.find()) {
                    commitLimit = JdkUtil.convertSize(Long.parseLong(matcher.group(1)), 'K',
                            org.github.joa.util.Constants.UNITS);
                    break;
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        return commitLimit;
    ***REMOVED***

    /**
     * The amount of userspace virtual memory currently allocated on the system.
     * 
     * The committed memory is a sum of all of the memory which has been allocated by processes, even if it has not been
     * "used" by them yet.
     * 
     * When vm.overcommit_memory=2, the cumulative VSS of all userspace processes is limited to overcomit_ratio percent
     * of ram + swap.
     * 
     * RHEL5: allocatable memory=(swap size + (RAM size * overcommit ratio / 100))
     * 
     * RHEL6/7/8: allocatable memory=(swap size + ((RAM size - huge tlb size) * overcommit ratio / 100))
     * 
     * @return The amount of userspace virtual memory currently allocated on the system in
     *         <code>org.github.joa.util.Constants.PRECISION_REPORTING</code> units.
     */
    public long getCommittedAs() {
        long committedAs = Long.MIN_VALUE;
        if (!meminfos.isEmpty()) {
            String regexMemTotal = "Committed_AS:[ ]{0,***REMOVED***(\\d{1,***REMOVED***) kB";
            Pattern pattern = Pattern.compile(regexMemTotal);
            Iterator<Meminfo> iterator = meminfos.iterator();
            while (iterator.hasNext()) {
                Meminfo event = iterator.next();
                Matcher matcher = pattern.matcher(event.getLogEntry());
                if (matcher.find()) {
                    committedAs = JdkUtil.convertSize(Long.parseLong(matcher.group(1)), 'K',
                            org.github.joa.util.Constants.UNITS);
                    break;
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        return committedAs;
    ***REMOVED***

    public List<CompilationEvent> getCompilationEvents() {
        return compilationEvents;
    ***REMOVED***

    public CompressedClassSpace getCompressedClassSpaceEvent() {
        return compressedClassSpaceEvent;
    ***REMOVED***

    /**
     * @return The max compressed class size reserved in <code>org.github.joa.util.Constants.PRECISION_REPORTING</code>
     *         units.
     */
    public long getCompressedClassSpaceSize() {
        // 1) Determine if compressed pointers are being used.
        boolean usingCompressedPointers = true;

        // 2) Default is to use compressed pointers based on heap size
        BigDecimal thirtyTwoGigabytes = new BigDecimal("32").multiply(org.github.joa.util.Constants.GIGABYTE);
        long heapMaxSize = getHeapMaxSize();
        if (heapMaxSize >= thirtyTwoGigabytes.longValue()) {
            usingCompressedPointers = false;
        ***REMOVED***

        // 3) Check if the default behavior is being overridden
        if (jvmOptions != null) {
            if (JdkUtil.isOptionDisabled(jvmOptions.getUseCompressedOops())) {
                usingCompressedPointers = false;
            ***REMOVED*** else {
                if (JdkUtil.isOptionDisabled(jvmOptions.getUseCompressedClassPointers())) {
                    usingCompressedPointers = false;
                ***REMOVED*** else {
                    usingCompressedPointers = true;
                ***REMOVED***
            ***REMOVED***
        ***REMOVED*** else if (!globalFlags.isEmpty()) {
            Iterator<GlobalFlag> iterator = globalFlags.iterator();
            boolean useCompressedOops = true;
            boolean useCompressedClassPointers = true;
            while (iterator.hasNext()) {
                GlobalFlag event = iterator.next();
                String regExCompressedOopsDisabled = "^.+bool UseCompressedOops[ ]{1,***REMOVED***= false.+$";
                String regExCompressedClassPointersDisabled = "^.+bool UseCompressedClassPointers[ ]{1,***REMOVED***= false.+$";
                if (event.getLogEntry().matches(regExCompressedOopsDisabled)) {
                    useCompressedOops = false;
                ***REMOVED*** else if (event.getLogEntry().matches(regExCompressedClassPointersDisabled)) {
                    useCompressedClassPointers = false;
                ***REMOVED***
            ***REMOVED***
            if (!useCompressedOops) {
                usingCompressedPointers = false;
            ***REMOVED*** else {
                if (!useCompressedClassPointers) {
                    usingCompressedPointers = false;
                ***REMOVED*** else {
                    usingCompressedPointers = true;
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***

        long compressedClassSpaceSize = 0;

        if (usingCompressedPointers) {
            // Default is 1g
            compressedClassSpaceSize = JdkUtil.convertSize(1, 'G', org.github.joa.util.Constants.UNITS);
            // 1st check [Global flags]
            if (!globalFlags.isEmpty()) {
                Iterator<GlobalFlag> iterator = globalFlags.iterator();
                while (iterator.hasNext()) {
                    GlobalFlag event = iterator.next();
                    String regExCompressedClassSpaceSize = "^.+uintx CompressedClassSpaceSize[ ]{1,***REMOVED***= (\\d{1,***REMOVED***).+$";
                    Pattern pattern = Pattern.compile(regExCompressedClassSpaceSize);
                    Matcher matcher = pattern.matcher(event.getLogEntry());
                    if (matcher.find()) {
                        compressedClassSpaceSize = JdkUtil.convertSize(Long.parseLong(matcher.group(1)), 'B',
                                org.github.joa.util.Constants.UNITS);
                        break;
                    ***REMOVED***
                ***REMOVED***
            ***REMOVED*** else if (jvmOptions != null) {
                if (JdkUtil.isOptionDisabled(jvmOptions.getUseCompressedOops())
                        || JdkUtil.isOptionDisabled(jvmOptions.getUseCompressedClassPointers())) {
                    compressedClassSpaceSize = 0;
                ***REMOVED*** else if (jvmOptions.getCompressedClassSpaceSize() != null) {
                    char fromUnits;
                    long value;
                    Pattern pattern = Pattern.compile(JdkRegEx.OPTION_SIZE_BYTES);
                    Matcher matcher = pattern.matcher(jvmOptions.getCompressedClassSpaceSize());
                    if (matcher.find()) {
                        value = Long.parseLong(matcher.group(2));
                        if (matcher.group(3) != null) {
                            fromUnits = matcher.group(3).charAt(0);
                        ***REMOVED*** else {
                            fromUnits = 'B';
                        ***REMOVED***
                        compressedClassSpaceSize = JdkUtil.convertSize(value, fromUnits,
                                org.github.joa.util.Constants.UNITS);
                    ***REMOVED***
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        return compressedClassSpaceSize;
    ***REMOVED***

    /**
     * @return The JVM compressed oop mode.
     */
    public CompressedOopMode getCompressedOopMode() {
        CompressedOopMode compressedOopMode = CompressedOopMode.UNKNOWN;
        if (!headers.isEmpty()) {
            Iterator<Header> iterator = headers.iterator();
            while (iterator.hasNext()) {
                Header event = iterator.next();
                if (event.isJavaVm()) {
                    if (!event.getLogEntry().matches(".*compressed oops.*")) {
                        compressedOopMode = CompressedOopMode.NONE;
                    ***REMOVED***
                    break;
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        if (compressedOopMode != CompressedOopMode.NONE && heapAddress != null) {
            compressedOopMode = heapAddress.getCompressedOopMode();
        ***REMOVED***
        return compressedOopMode;
    ***REMOVED***

    public List<ContainerInfo> getContainerInfos() {
        return containerInfos;
    ***REMOVED***

    /**
     * @return The CPU architecture.
     */
    public CpuArch getCpuArch() {
        CpuArch cpuArch = CpuArch.UNIDENTIFIED;
        if (!cpuInfos.isEmpty()) {
            Iterator<CpuInfo> iterator = cpuInfos.iterator();
            while (iterator.hasNext()) {
                CpuInfo event = iterator.next();
                if (event.getLogEntry().matches("^.+POWER9.+$")) {
                    cpuArch = CpuArch.POWER9;
                    break;
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        return cpuArch;
    ***REMOVED***

    public List<CpuInfo> getCpuInfos() {
        return cpuInfos;
    ***REMOVED***

    /**
     * @return The number of logical cpus (cpus x cpu cores x hyperthreading).
     */
    public int getCpusLogical() {
        int cpuLogical = Integer.MIN_VALUE;
        if (!cpuInfos.isEmpty()) {
            Iterator<CpuInfo> iterator = cpuInfos.iterator();
            while (iterator.hasNext()) {
                CpuInfo event = iterator.next();
                if (event.isCpuHeader()) {
                    Pattern pattern = Pattern.compile(CpuInfo._REGEX_HEADER);
                    Matcher matcher = pattern.matcher(event.getLogEntry());
                    if (matcher.find() && matcher.group(1) != null) {
                        int cpus = Integer.parseInt(matcher.group(1));
                        BigDecimal calc = new BigDecimal(cpus);
                        if (matcher.group(5) != null) {
                            int cores = Integer.parseInt(matcher.group(5));
                            calc = calc.multiply(new BigDecimal(cores));
                        ***REMOVED***
                        if (matcher.group(6) != null) {
                            int threads = Integer.parseInt(matcher.group(6));
                            calc = calc.multiply(new BigDecimal(threads));
                        ***REMOVED***
                        cpuLogical = calc.intValue();
                    ***REMOVED***
                    break;
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        return cpuLogical;
    ***REMOVED***

    /**
     * The crash <code>Date</code> from a given crash date/time string (e.g. Tue Mar 1 09:13:16 2022 UTC).
     * 
     * Note: Timezone is ignored.
     * 
     * @return The <code>Date</code> of the crash or null if unknown.
     */
    public Date getCrashDate() {
        Date crashDate = null;
        if (getCrashTimeString() != null && getCrashTimeString().length() > 0) {
            String MMM = null;
            String d = null;
            String yyyy = null;
            String HH = null;
            String mm = null;
            String ss = null;
            Pattern pattern = Pattern.compile(JdkRegEx.CRASH_DATE_TIME);
            Matcher matcher = pattern.matcher(getCrashTimeString());
            if (matcher.find()) {
                MMM = matcher.group(2);
                d = matcher.group(3);
                HH = matcher.group(4);
                mm = matcher.group(5);
                ss = matcher.group(6);
                yyyy = matcher.group(7);
            ***REMOVED***
            crashDate = ErrUtil.getDate(MMM, d, yyyy, HH, mm, ss);
        ***REMOVED***
        return crashDate;
    ***REMOVED***

    /**
     * The crash date/time string.
     * 
     * For example:
     * 
     * Tue Mar 1 09:13:16 2022 UTC
     * 
     * @return The date/time string of the crash.
     */
    public String getCrashTimeString() {
        StringBuilder crashTime = new StringBuilder();
        if (time != null) {
            crashTime.append(time.getTimeString());
            if (timezone != null) {
                crashTime.append(" ");
                crashTime.append(timezone.getTimezone());
            ***REMOVED***
        ***REMOVED*** else if (timeElapsedTime != null) {
            crashTime.append(timeElapsedTime.getTimeString());
        ***REMOVED***
        return crashTime.toString();
    ***REMOVED***

    public List<CurrentCompileTask> getCurrentCompileTasks() {
        return currentCompileTasks;
    ***REMOVED***

    public String getCurrentThreadName() {
        String currentThreadName = null;
        if (currentThread != null) {
            currentThreadName = currentThread.getThreadName();
        ***REMOVED*** else {
            currentThreadName = org.github.krashpad.util.Constants.PROPERTY_UNKNOWN;
        ***REMOVED***
        return currentThreadName;
    ***REMOVED***

    public List<DeoptimizationEvent> getDeoptimizationEvents() {
        return deoptimizationEvents;
    ***REMOVED***

    /**
     * @return The max direct memory size reserved in <code>org.github.joa.util.Constants.PRECISION_REPORTING</code>
     *         units.
     */
    public long getDirectMemoryMaxSize() {
        long directMemorySize = 0;
        // 1st check [Global flags]
        if (!globalFlags.isEmpty()) {
            Iterator<GlobalFlag> iterator = globalFlags.iterator();
            while (iterator.hasNext()) {
                GlobalFlag event = iterator.next();
                String regExMaxDirectMemorySize = "^.+uintx MaxDirectMemorySize[ ]{1,***REMOVED***= (\\d{1,***REMOVED***).+$";
                Pattern pattern = Pattern.compile(regExMaxDirectMemorySize);
                Matcher matcher = pattern.matcher(event.getLogEntry());
                if (matcher.find()) {
                    directMemorySize = JdkUtil.convertSize(Long.parseLong(matcher.group(1)), 'B',
                            org.github.joa.util.Constants.UNITS);
                    break;
                ***REMOVED***
            ***REMOVED***
        ***REMOVED*** else if (jvmOptions != null && jvmOptions.getMaxDirectMemorySize() != null) {
            char fromUnits;
            long value;
            Pattern pattern = Pattern.compile(JdkRegEx.OPTION_SIZE_BYTES);
            Matcher matcher = pattern.matcher(jvmOptions.getMaxDirectMemorySize());
            if (matcher.find()) {
                value = Long.parseLong(matcher.group(2));
                if (matcher.group(3) != null) {
                    fromUnits = matcher.group(3).charAt(0);
                ***REMOVED*** else {
                    fromUnits = 'B';
                ***REMOVED***
                directMemorySize = JdkUtil.convertSize(value, fromUnits, org.github.joa.util.Constants.UNITS);
            ***REMOVED***
        ***REMOVED***
        return directMemorySize;
    ***REMOVED***

    public List<DllOperationEvent> getDllOperationEvents() {
        return dllOperationEvents;
    ***REMOVED***

    public List<DynamicLibrary> getDynamicLibraries() {
        return dynamicLibraries;
    ***REMOVED***

    /**
     * @return The duration of the JVM run in seconds.
     */
    public String getElapsedTime() {
        String elapsedTimeLiteral = null;
        if (elapsedTime != null) {
            elapsedTimeLiteral = elapsedTime.getLiteral();
        ***REMOVED*** else if (timeElapsedTime != null) {
            elapsedTimeLiteral = timeElapsedTime.getLiteral();
        ***REMOVED***
        return elapsedTimeLiteral;
    ***REMOVED***

    public List<EnvironmentVariable> getEnvironmentVariables() {
        return environmentVariables;
    ***REMOVED***

    /**
     * @return A <code>String</code> describing the cause of the crash.
     */
    public String getError() {
        StringBuilder causedBy = new StringBuilder();
        if (!headers.isEmpty()) {
            Iterator<Header> iterator = headers.iterator();
            while (iterator.hasNext()) {
                Header he = iterator.next();
                if (he.isSignalNumber() || he.isInternalError() || he.isError() || he.isFailed() || he.isInsufficient()
                        || he.isInvalid() || he.isOutOf() || he.isProblematicFrame()) {
                    if (causedBy.length() > 0) {
                        causedBy.append(org.github.krashpad.util.Constants.LINE_SEPARATOR);
                    ***REMOVED***
                    causedBy.append(he.getLogEntry());
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        return causedBy.toString();
    ***REMOVED***

    public List<Event> getEvents() {
        return events;
    ***REMOVED***

    /**
     * @param regex
     *            The event regex.
     * @return the timestamp of the first <code>EventEvent</code> that matches the regex in milliseconds, or
     *         Long.MIN_VALUE if no event matches.
     */
    public long getEventTimestamp(String regex) {
        long timestamp = Long.MIN_VALUE;
        if (!events.isEmpty()) {
            Pattern pattern = Pattern.compile(regex);
            Iterator<Event> iterator = events.iterator();
            while (iterator.hasNext()) {
                Event event = iterator.next();
                Matcher matcher = pattern.matcher(event.getLogEntry());
                if (matcher.find()) {
                    BigDecimal millis = new BigDecimal(matcher.group(1)).movePointRight(3);
                    timestamp = millis.longValue();
                    break;
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        return timestamp;
    ***REMOVED***

    public List<ExceptionCounts> getExceptionCounts() {
        return exceptionCounts;
    ***REMOVED***

    /**
     * @param releaseString
     *            The JDK release string (e.g. 17.0.4.1+1-LTS).
     * @return The first release that matches a Red Hat build string, or null if none found. Used to get a an
     *         approximate release (e.g. to determine approximate JDK age).
     */
    public Release getFirstRelease(String releaseString) {
        Release firstRelease = null;
        if (firstRelease == null) {
            firstRelease = JdkUtil.getFirstReleaseFromReleases(releaseString, JdkUtil.JDK8_RHEL6_X86_64_RPMS);
        ***REMOVED***
        if (firstRelease == null) {
            firstRelease = JdkUtil.getFirstReleaseFromReleases(releaseString, JdkUtil.JDK8_RHEL7_X86_64_RPMS);
        ***REMOVED***
        if (firstRelease == null) {
            firstRelease = JdkUtil.getFirstReleaseFromReleases(releaseString, JdkUtil.JDK8_RHEL8_X86_64_RPMS);
        ***REMOVED***
        if (firstRelease == null) {
            firstRelease = JdkUtil.getFirstReleaseFromReleases(releaseString, JdkUtil.JDK8_RHEL9_X86_64_RPMS);
        ***REMOVED***
        if (firstRelease == null) {
            firstRelease = JdkUtil.getFirstReleaseFromReleases(releaseString, JdkUtil.JDK8_RHEL_ZIPS);
        ***REMOVED***
        if (firstRelease == null) {
            firstRelease = JdkUtil.getFirstReleaseFromReleases(releaseString, JdkUtil.JDK8_WINDOWS_ZIPS);
        ***REMOVED***
        if (firstRelease == null) {
            firstRelease = JdkUtil.getFirstReleaseFromReleases(releaseString, JdkUtil.JDK11_RHEL7_X86_64_RPMS);
        ***REMOVED***
        if (firstRelease == null) {
            firstRelease = JdkUtil.getFirstReleaseFromReleases(releaseString, JdkUtil.JDK11_RHEL8_X86_64_RPMS);
        ***REMOVED***
        if (firstRelease == null) {
            firstRelease = JdkUtil.getFirstReleaseFromReleases(releaseString, JdkUtil.JDK11_RHEL9_X86_64_RPMS);
        ***REMOVED***
        if (firstRelease == null) {
            firstRelease = JdkUtil.getFirstReleaseFromReleases(releaseString, JdkUtil.JDK11_RHEL_ZIPS);
        ***REMOVED***
        if (firstRelease == null) {
            firstRelease = JdkUtil.getFirstReleaseFromReleases(releaseString, JdkUtil.JDK11_WINDOWS_ZIPS);
        ***REMOVED***
        if (firstRelease == null) {
            firstRelease = JdkUtil.getFirstReleaseFromReleases(releaseString, JdkUtil.JDK17_RHEL8_X86_64_RPMS);
        ***REMOVED***
        if (firstRelease == null) {
            firstRelease = JdkUtil.getFirstReleaseFromReleases(releaseString, JdkUtil.JDK17_RHEL9_X86_64_RPMS);
        ***REMOVED***
        if (firstRelease == null) {
            firstRelease = JdkUtil.getFirstReleaseFromReleases(releaseString, JdkUtil.JDK17_RHEL_ZIPS);
        ***REMOVED***
        if (firstRelease == null) {
            firstRelease = JdkUtil.getFirstReleaseFromReleases(releaseString, JdkUtil.JDK17_WINDOWS_ZIPS);
        ***REMOVED***
        return firstRelease;
    ***REMOVED***

    /**
     * @return The list of garbage collectors identified from events, JVM options, or JDK defaults.
     */
    public List<GarbageCollector> getGarbageCollectors() {
        if (getGarbageCollectorsFromHeapEvents().size() > 0) {
            return getGarbageCollectorsFromHeapEvents();
        ***REMOVED*** else if (jvmOptions != null) {
            return jvmOptions.getGarbageCollectors();
        ***REMOVED*** else {
            ArrayList<GarbageCollector> garbageCollectors = new ArrayList<GarbageCollector>();
            garbageCollectors.add(GarbageCollector.UNKNOWN);
            return garbageCollectors;
        ***REMOVED***
    ***REMOVED***

    /**
     * @return The list of garbage collectors as determined by inspecting the <code>HeapEvent</code>s.
     */
    public List<GarbageCollector> getGarbageCollectorsFromHeapEvents() {
        List<GarbageCollector> garbageCollectors = new ArrayList<GarbageCollector>();
        if (!heaps.isEmpty()) {
            Iterator<Heap> iterator = heaps.iterator();
            while (iterator.hasNext()) {
                Heap event = iterator.next();
                if (event.getLogEntry().matches("^[ ]{0,***REMOVED***Shenandoah.+$")
                        && !garbageCollectors.contains(GarbageCollector.SHENANDOAH)) {
                    garbageCollectors.add(GarbageCollector.SHENANDOAH);
                    break;
                ***REMOVED*** else if (event.getLogEntry().matches("^[ ]{0,***REMOVED***garbage-first.+$")
                        && !garbageCollectors.contains(GarbageCollector.G1)) {
                    garbageCollectors.add(GarbageCollector.G1);
                    break;
                ***REMOVED*** else if (event.getLogEntry().matches("^[ ]{0,***REMOVED***PSYoungGen.+$")
                        && !garbageCollectors.contains(GarbageCollector.PARALLEL_SCAVENGE)) {
                    garbageCollectors.add(GarbageCollector.PARALLEL_SCAVENGE);
                ***REMOVED*** else if (event.getLogEntry().matches("^[ ]{0,***REMOVED***ParOldGen.+$")
                        && !garbageCollectors.contains(GarbageCollector.PARALLEL_OLD)) {
                    garbageCollectors.add(GarbageCollector.PARALLEL_OLD);
                ***REMOVED*** else if (event.getLogEntry().matches("^[ ]{0,***REMOVED***par new.+$")
                        && !garbageCollectors.contains(GarbageCollector.PAR_NEW)) {
                    garbageCollectors.add(GarbageCollector.PAR_NEW);
                ***REMOVED*** else if (event.getLogEntry().matches("^[ ]{0,***REMOVED***concurrent mark-sweep.+$")
                        && !garbageCollectors.contains(GarbageCollector.CMS)) {
                    garbageCollectors.add(GarbageCollector.CMS);
                ***REMOVED*** else if (event.getLogEntry().matches("^[ ]{0,***REMOVED***ZHeap.+$")
                        && !garbageCollectors.contains(GarbageCollector.ZGC)) {
                    garbageCollectors.add(GarbageCollector.ZGC);
                ***REMOVED*** else if (event.getLogEntry().matches("^[ ]{0,***REMOVED***def new.+$")
                        && !garbageCollectors.contains(GarbageCollector.SERIAL_NEW)) {
                    garbageCollectors.add(GarbageCollector.SERIAL_NEW);
                ***REMOVED*** else if ((event.getLogEntry().matches("^[ ]{0,***REMOVED***PSOldGen.+$")
                        || event.getLogEntry().matches("^[ ]{0,***REMOVED***tenured.+$"))
                        && !garbageCollectors.contains(GarbageCollector.SERIAL_OLD)) {
                    garbageCollectors.add(GarbageCollector.SERIAL_OLD);
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        return garbageCollectors;
    ***REMOVED***

    public List<GcHeapHistoryEvent> getGcHeapHistoryEvents() {
        return gcHeapHistoryEvents;
    ***REMOVED***

    public List<GcPreciousLog> getGcPreciousLogs() {
        return gcPreciousLogs;
    ***REMOVED***

    public List<GlobalFlag> getGlobalFlags() {
        return globalFlags;
    ***REMOVED***

    public List<Header> getHeaders() {
        return headers;
    ***REMOVED***

    public HeapAddress getHeapAddress() {
        return heapAddress;
    ***REMOVED***

    /**
     * @return The total heap allocation in <code>org.github.joa.util.Constants.PRECISION_REPORTING</code> units.
     */
    public long getHeapAllocation() {
        long heapAllocation = Long.MIN_VALUE;
        if (!heaps.isEmpty()) {
            heapAllocation = 0;
            Iterator<Heap> iterator = heaps.iterator();
            char fromUnits;
            long value;
            Pattern pattern = null;
            Matcher matcher = null;
            while (iterator.hasNext()) {
                Heap event = iterator.next();
                if (event.isYoungGen()) {
                    pattern = Pattern.compile(JdkRegEx.YOUNG_GEN_SIZE);
                    matcher = pattern.matcher(event.getLogEntry());
                    if (matcher.find()) {
                        value = Long.parseLong(matcher.group(3));
                        if (matcher.group(5) != null) {
                            fromUnits = matcher.group(5).charAt(0);
                        ***REMOVED*** else {
                            fromUnits = 'B';
                        ***REMOVED***
                        heapAllocation += JdkUtil.convertSize(value, fromUnits, org.github.joa.util.Constants.UNITS);
                    ***REMOVED***
                ***REMOVED*** else if (event.isOldGen()) {
                    pattern = Pattern.compile(JdkRegEx.OLD_GEN_SIZE);
                    matcher = pattern.matcher(event.getLogEntry());
                    if (matcher.find()) {
                        value = Long.parseLong(matcher.group(2));
                        if (matcher.group(4) != null) {
                            fromUnits = matcher.group(4).charAt(0);
                        ***REMOVED*** else {
                            fromUnits = 'B';
                        ***REMOVED***
                        heapAllocation += JdkUtil.convertSize(value, fromUnits, org.github.joa.util.Constants.UNITS);
                    ***REMOVED***
                ***REMOVED*** else if (event.isShenandoah()) {
                    pattern = Pattern.compile(JdkRegEx.SHENANDOAH_SIZE);
                    matcher = pattern.matcher(event.getLogEntry());
                    if (matcher.find()) {
                        value = Long.parseLong(matcher.group(9));
                        if (matcher.group(11) != null) {
                            fromUnits = matcher.group(11).charAt(0);
                        ***REMOVED*** else {
                            fromUnits = 'B';
                        ***REMOVED***
                        heapAllocation += JdkUtil.convertSize(value, fromUnits, org.github.joa.util.Constants.UNITS);
                    ***REMOVED***
                ***REMOVED*** else if (event.isG1()) {
                    pattern = Pattern.compile(JdkRegEx.G1_SIZE);
                    matcher = pattern.matcher(event.getLogEntry());
                    if (matcher.find()) {
                        value = Long.parseLong(matcher.group(1));
                        if (matcher.group(3) != null) {
                            fromUnits = matcher.group(3).charAt(0);
                        ***REMOVED*** else {
                            fromUnits = 'B';
                        ***REMOVED***
                        heapAllocation += JdkUtil.convertSize(value, fromUnits, org.github.joa.util.Constants.UNITS);
                    ***REMOVED***
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        return heapAllocation;
    ***REMOVED***

    /**
     * @return The heap initial size reserved in <code>org.github.joa.util.Constants.PRECISION_REPORTING</code> units.
     */
    public long getHeapInitialSize() {
        long heapInitialSize = Long.MIN_VALUE;
        // 1st check [Global flags]
        if (!globalFlags.isEmpty()) {
            Iterator<GlobalFlag> iterator = globalFlags.iterator();
            while (iterator.hasNext()) {
                GlobalFlag event = iterator.next();
                String regExInitialHeapSize = "^.+size_t InitialHeapSize[ ]{1,***REMOVED***= (\\d{1,***REMOVED***).+$";
                Pattern pattern = Pattern.compile(regExInitialHeapSize);
                Matcher matcher = pattern.matcher(event.getLogEntry());
                if (matcher.find()) {
                    heapInitialSize = JdkUtil.convertSize(Long.parseLong(matcher.group(1)), 'B',
                            org.github.joa.util.Constants.UNITS);
                    break;
                ***REMOVED***
            ***REMOVED***
        ***REMOVED*** else if (jvmOptions != null && jvmOptions.getInitialHeapSize() != null) {
            // Get from jvm_args
            char fromUnits;
            long value;
            Pattern pattern = Pattern.compile(JdkRegEx.OPTION_SIZE_BYTES);
            Matcher matcher = pattern.matcher(jvmOptions.getInitialHeapSize());
            if (matcher.find()) {
                value = Long.parseLong(matcher.group(2));
                if (matcher.group(3) != null) {
                    fromUnits = matcher.group(3).charAt(0);
                ***REMOVED*** else {
                    fromUnits = 'B';
                ***REMOVED***
                heapInitialSize = JdkUtil.convertSize(value, fromUnits, org.github.joa.util.Constants.UNITS);
            ***REMOVED***
        ***REMOVED*** else if (heapAddress != null) {
            heapInitialSize = heapAddress.getSize();
        ***REMOVED*** else if (getOsMemTotal() > 0) {
            // Use JVM default = 1/64 system memory
            BigDecimal systemPhysicalMemory = new BigDecimal(getOsMemTotal());
            systemPhysicalMemory = systemPhysicalMemory.divide(new BigDecimal(64));
            systemPhysicalMemory = systemPhysicalMemory.setScale(0, RoundingMode.HALF_EVEN);
            heapInitialSize = systemPhysicalMemory.longValue();
        ***REMOVED*** else if (getHeapAllocation() > 0) {
            // Use allocation
            heapInitialSize = getHeapAllocation();
        ***REMOVED***
        return heapInitialSize;
    ***REMOVED***

    /**
     * @return The heap max size reserved in <code>org.github.joa.util.Constants.PRECISION_REPORTING</code> units.
     */
    public long getHeapMaxSize() {
        long heapMaxSize = Long.MIN_VALUE;
        // 1st check [Global flags]
        if (!globalFlags.isEmpty()) {
            Iterator<GlobalFlag> iterator = globalFlags.iterator();
            while (iterator.hasNext()) {
                GlobalFlag event = iterator.next();
                String regExMaxHeapSize = "^.+size_t MaxHeapSize[ ]{1,***REMOVED***= (\\d{1,***REMOVED***).+$";
                Pattern pattern = Pattern.compile(regExMaxHeapSize);
                Matcher matcher = pattern.matcher(event.getLogEntry());
                if (matcher.find()) {
                    heapMaxSize = JdkUtil.convertSize(Long.parseLong(matcher.group(1)), 'B',
                            org.github.joa.util.Constants.UNITS);
                    break;
                ***REMOVED***
            ***REMOVED***
        ***REMOVED*** else if (jvmOptions != null && jvmOptions.getMaxHeapSize() != null) {
            // Get from jvm_args
            char fromUnits;
            long value;
            Pattern pattern = Pattern.compile(JdkRegEx.OPTION_SIZE_BYTES);
            Matcher matcher = pattern.matcher(jvmOptions.getMaxHeapSize());
            if (matcher.find()) {
                value = Long.parseLong(matcher.group(2));
                if (matcher.group(3) != null) {
                    fromUnits = matcher.group(3).charAt(0);
                ***REMOVED*** else {
                    fromUnits = 'B';
                ***REMOVED***
                heapMaxSize = JdkUtil.convertSize(value, fromUnits, org.github.joa.util.Constants.UNITS);
            ***REMOVED***
        ***REMOVED*** else if (heapAddress != null) {
            heapMaxSize = heapAddress.getSize();
        ***REMOVED*** else if (getOsMemTotal() > 0) {
            // Use JVM default = 1/4 system memory
            BigDecimal systemPhysicalMemory = new BigDecimal(getOsMemTotal());
            systemPhysicalMemory = systemPhysicalMemory.divide(new BigDecimal(4));
            systemPhysicalMemory = systemPhysicalMemory.setScale(0, RoundingMode.HALF_EVEN);
            heapMaxSize = systemPhysicalMemory.longValue();
        ***REMOVED*** else if (getHeapAllocation() > 0) {
            // Use allocation
            heapMaxSize = getHeapAllocation();
        ***REMOVED***
        return heapMaxSize;
    ***REMOVED***

    public List<Heap> getHeaps() {
        return heaps;
    ***REMOVED***

    /**
     * @return The heap starting address in <code>org.github.joa.util.Constants.PRECISION_REPORTING</code> units.
     */
    public long getHeapStartingAddress() {
        long heapStartingAddress = Long.MIN_VALUE;
        if (heapAddress != null) {
            heapStartingAddress = JdkUtil.convertSize(heapAddress.getStartingAddress(), 'B',
                    org.github.joa.util.Constants.UNITS);
        ***REMOVED***
        return heapStartingAddress;
    ***REMOVED***

    /**
     * @return The total heap used in <code>org.github.joa.util.Constants.PRECISION_REPORTING</code> units.
     */
    public long getHeapUsed() {
        long heapUsed = Long.MIN_VALUE;
        if (!heaps.isEmpty()) {
            Iterator<Heap> iterator = heaps.iterator();
            char fromUnits;
            long value;
            Pattern pattern = null;
            Matcher matcher = null;
            while (iterator.hasNext()) {
                Heap event = iterator.next();
                if (event.isYoungGen()) {
                    pattern = Pattern.compile(JdkRegEx.YOUNG_GEN_SIZE);
                    matcher = pattern.matcher(event.getLogEntry());
                    if (matcher.find()) {
                        value = Long.parseLong(matcher.group(6));
                        if (matcher.group(8) != null) {
                            fromUnits = matcher.group(8).charAt(0);
                        ***REMOVED*** else {
                            fromUnits = 'B';
                        ***REMOVED***
                        if (heapUsed == Long.MIN_VALUE) {
                            heapUsed = JdkUtil.convertSize(value, fromUnits, org.github.joa.util.Constants.UNITS);
                        ***REMOVED*** else {
                            heapUsed += JdkUtil.convertSize(value, fromUnits, org.github.joa.util.Constants.UNITS);
                        ***REMOVED***
                    ***REMOVED***
                ***REMOVED*** else if (event.isOldGen()) {
                    pattern = Pattern.compile(JdkRegEx.OLD_GEN_SIZE);
                    matcher = pattern.matcher(event.getLogEntry());
                    if (matcher.find()) {
                        value = Long.parseLong(matcher.group(5));
                        if (matcher.group(7) != null) {
                            fromUnits = matcher.group(7).charAt(0);
                        ***REMOVED*** else {
                            fromUnits = 'B';
                        ***REMOVED***
                        if (heapUsed == Long.MIN_VALUE) {
                            heapUsed = JdkUtil.convertSize(value, fromUnits, org.github.joa.util.Constants.UNITS);
                        ***REMOVED*** else {
                            heapUsed += JdkUtil.convertSize(value, fromUnits, org.github.joa.util.Constants.UNITS);
                        ***REMOVED***
                    ***REMOVED***
                ***REMOVED*** else if (event.isShenandoah()) {
                    pattern = Pattern.compile(JdkRegEx.SHENANDOAH_SIZE);
                    matcher = pattern.matcher(event.getLogEntry());
                    if (matcher.find()) {
                        value = Long.parseLong(matcher.group(12));
                        if (matcher.group(14) != null) {
                            fromUnits = matcher.group(14).charAt(0);
                        ***REMOVED*** else {
                            fromUnits = 'B';
                        ***REMOVED***
                        if (heapUsed == Long.MIN_VALUE) {
                            heapUsed = JdkUtil.convertSize(value, fromUnits, org.github.joa.util.Constants.UNITS);
                        ***REMOVED*** else {
                            heapUsed += JdkUtil.convertSize(value, fromUnits, org.github.joa.util.Constants.UNITS);
                        ***REMOVED***
                    ***REMOVED***
                ***REMOVED*** else if (event.isG1()) {
                    pattern = Pattern.compile(JdkRegEx.G1);
                    matcher = pattern.matcher(event.getLogEntry());
                    if (matcher.find()) {
                        value = Long.parseLong(matcher.group(5));
                        if (matcher.group(7) != null) {
                            fromUnits = matcher.group(7).charAt(0);
                        ***REMOVED*** else {
                            fromUnits = 'B';
                        ***REMOVED***
                        if (heapUsed == Long.MIN_VALUE) {
                            heapUsed = JdkUtil.convertSize(value, fromUnits, org.github.joa.util.Constants.UNITS);
                        ***REMOVED*** else {
                            heapUsed += JdkUtil.convertSize(value, fromUnits, org.github.joa.util.Constants.UNITS);
                        ***REMOVED***
                    ***REMOVED***
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        return heapUsed;
    ***REMOVED***

    public Host getHost() {
        return host;
    ***REMOVED***

    public List<InternalExceptionEvent> getInternalExceptionEvents() {
        return internalExceptionEvents;
    ***REMOVED***

    public List<InternalStatistic> getInternalStatistics() {
        return internalStatistics;
    ***REMOVED***

    /**
     * @return Jar list (unique entries).
     */
    public List<String> getJars() {
        List<String> jars = new ArrayList<String>();
        if (!dynamicLibraries.isEmpty()) {
            Iterator<DynamicLibrary> iterator = dynamicLibraries.iterator();
            while (iterator.hasNext()) {
                DynamicLibrary event = iterator.next();
                if (event.isJar()) {
                    if (!jars.contains(event.getFilePath())) {
                        jars.add(event.getFilePath());
                    ***REMOVED***
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        return jars;
    ***REMOVED***

    /**
     * @return The Java command used to start the JVM, or null if none exists.
     */
    public String getJavaCommand() {
        String javaCommand = null;
        if (!vmArguments.isEmpty()) {
            Iterator<VmArguments> iterator = vmArguments.iterator();
            while (iterator.hasNext()) {
                VmArguments event = iterator.next();
                if (event.isJavaCommand()) {
                    javaCommand = event.getValue();
                    break;
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        return javaCommand;
    ***REMOVED***

    /**
     * @return <code>JavaSpecificiation</code>
     */
    public JavaSpecification getJavaSpecification() {
        JavaSpecification version = JavaSpecification.UNKNOWN;
        if (vmInfo != null) {
            version = vmInfo.getJavaSpecification();
        ***REMOVED***
        // Get from header
        if (version == JavaSpecification.UNKNOWN && !headers.isEmpty()) {
            Iterator<Header> iterator = headers.iterator();
            while (iterator.hasNext()) {
                Header he = iterator.next();
                String regEx = "\\((\\d{1,2***REMOVED***)\\..+\\)";
                Pattern pattern = Pattern.compile(regEx);
                if (he.isJreVersion()) {
                    Matcher matcher = pattern.matcher(he.getLogEntry());
                    if (matcher.find()) {
                        switch (Integer.parseInt(matcher.group(1))) {
                        case 6:
                            version = JavaSpecification.JDK6;
                            break;
                        case 7:
                            version = JavaSpecification.JDK7;
                            break;
                        case 8:
                            version = JavaSpecification.JDK8;
                            break;
                        case 11:
                            version = JavaSpecification.JDK11;
                            break;
                        case 17:
                            version = JavaSpecification.JDK17;
                            break;
                        default:
                            break;
                        ***REMOVED***
                    ***REMOVED***
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        // Check dynamic library (rpm)
        if (version == JavaSpecification.UNKNOWN && !dynamicLibraries.isEmpty()) {
            if (getRpmDirectory() != null) {
                String regEx = "^java-.+-openjdk-(1.8.0|11|17).+-.+$";
                Pattern pattern = Pattern.compile(regEx);
                Matcher matcher = pattern.matcher(getRpmDirectory());
                if (matcher.find()) {
                    if (matcher.group(1).equals("1.8.0")) {
                        version = JavaSpecification.JDK8;
                    ***REMOVED*** else if (matcher.group(1).equals("11")) {
                        version = JavaSpecification.JDK11;
                    ***REMOVED*** else if (matcher.group(1).equals("17")) {
                        version = JavaSpecification.JDK17;
                    ***REMOVED***
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        return version;
    ***REMOVED***

    /**
     * @return The number of Java threads running when the JVM crashed.
     */
    public int getJavaThreadCount() {
        int javaThreadCount = 0;
        if (!threads.isEmpty()) {
            Iterator<Thread> iterator = threads.iterator();
            while (iterator.hasNext()) {
                Thread event = iterator.next();
                if (event.getLogEntry().matches("^***REMOVED***$")) {
                    break;
                ***REMOVED*** else if (!event.getLogEntry().matches("^Java Threads: \\( => current thread \\)$")) {
                    javaThreadCount++;
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        return javaThreadCount;
    ***REMOVED***

    /**
     * @param regex
     *            The thread name regex.
     * @return The number of threads matching the pattern regex.
     */
    public int getJavaThreadCount(String regex) {
        int threadCount = 0;
        if (!threads.isEmpty()) {
            Iterator<Thread> iterator = threads.iterator();
            while (iterator.hasNext()) {
                Thread event = iterator.next();
                if (event.getLogEntry().matches("^.*" + regex + ".*$")) {
                    threadCount++;
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        return threadCount;
    ***REMOVED***

    /**
     * @return <code>JavaVendor</code>
     */
    public JavaVendor getJavaVendor() {
        JavaVendor vendor = JavaVendor.UNIDENTIFIED;
        if (isRhBuildOpenJdk()) {
            vendor = JavaVendor.RED_HAT;
        ***REMOVED*** else {
            if (vmInfo != null) {
                switch (vmInfo.getBuiltBy()) {
                case JAVA_RE:
                case MACH5ONE:
                    vendor = JavaVendor.ORACLE;
                    break;
                case JENKINS:
                    vendor = JavaVendor.ADOPTOPENJDK;
                    break;
                case MOCKBUILD:
                    // Some other OpenJDK
                    vendor = JavaVendor.UNIDENTIFIED;
                    break;
                case VSTS:
                    vendor = JavaVendor.MICROSOFT;
                    break;
                case TEMURIN:
                    vendor = JavaVendor.ADOPTIUM;
                    break;
                case TESTER:
                case ZULU_RE:
                    vendor = JavaVendor.AZUL;
                    break;
                case BUILD:
                case EMPTY:
                case UNKNOWN:
                default:
                    break;
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        if (vendor == JavaVendor.UNIDENTIFIED && !headers.isEmpty()) {
            // Check header
            Iterator<Header> iterator = headers.iterator();
            while (iterator.hasNext()) {
                Header he = iterator.next();
                if (he.isJreVersion()) {
                    if (he.getLogEntry().matches("^.+AdoptOpenJDK.+$")) {
                        vendor = JavaVendor.ADOPTOPENJDK;
                    ***REMOVED*** else if (getOs() != Os.UNIDENTIFIED && !isRhVersion()) {
                        vendor = JavaVendor.NOT_RED_HAT;
                    ***REMOVED***
                    break;
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        return vendor;
    ***REMOVED***

    /**
     * @return The Java major version, or Integer.MIN_VALUE if unknown.
     */
    public int getJavaVersionMajor() {
        return JdkUtil.getJavaSpecificationNumber(getJavaSpecification());
    ***REMOVED***

    /**
     * @return The Java minor version number, or Integer.MIN_VALUE if unknown.
     */
    public int getJavaVersionMinor() {
        int javaVersionMinor = Integer.MIN_VALUE;
        switch (getJavaSpecification()) {
        case JDK8:
            javaVersionMinor = JdkUtil.getJdk8UpdateNumber(getJdkReleaseString());
            break;
        case JDK11:
            javaVersionMinor = JdkUtil.getJdk11UpdateNumber(getJdkReleaseString());
            break;
        case JDK17:
            javaVersionMinor = JdkUtil.getJdk17UpdateNumber(getJdkReleaseString());
            break;
        case JDK6:
        case JDK7:
        case UNKNOWN:
        default:
            break;
        ***REMOVED***
        return javaVersionMinor;
    ***REMOVED***

    /**
     * @return The JDK build date/time in <code>VmInfo</code>.
     */
    public Date getJdkBuildDate() {
        Date date = null;
        if (vmInfo != null) {
            date = vmInfo.getBuildDate();
        ***REMOVED***
        return date;
    ***REMOVED***

    /**
     * @return The JDK actual or estimated release date.
     */
    public Date getJdkReleaseDate() {
        Date date = getJdkBuildDate();
        if (date == null) {
            date = JdkUtil.getJdkReleaseDate(this);
        ***REMOVED***
        return date;
    ***REMOVED***

    /**
     * @return JDK release string, or UNKNOWN if it cannot be determined.
     */
    public String getJdkReleaseString() {
        String jdkReleaseString = null;
        if (vmInfo != null) {
            jdkReleaseString = vmInfo.getJdkReleaseString();
            // TODO: Better solution than this hack to account for 2 windows builds based on the same upstream tag?
            if (vmInfo.getOs() == Os.WINDOWS) {
                if (vmInfo.getJavaSpecification() == JavaSpecification.JDK8
                        && jdkReleaseString.equals("1.8.0_332-b09")) {
                    if (vmInfo.getBuildDate().equals(ErrUtil.getDate("Apr 19 2022 13:36:53"))) {
                        jdkReleaseString = "1.8.0_332-b09-1";
                    ***REMOVED*** else if (vmInfo.getBuildDate().equals(ErrUtil.getDate("Apr 27 2022 21:29:19"))) {
                        jdkReleaseString = "1.8.0_332-b09-2";
                    ***REMOVED***
                ***REMOVED*** else if (vmInfo.getJavaSpecification() == JavaSpecification.JDK11
                        && jdkReleaseString.equals("11.0.15+9-LTS")) {
                    if (vmInfo.getBuildDate().equals(ErrUtil.getDate("Apr 17 2022 13:56:34"))) {
                        jdkReleaseString = "11.0.15+9-LTS-1";
                    ***REMOVED*** else if (vmInfo.getBuildDate().equals(ErrUtil.getDate("Apr 27 2022 19:12:18"))) {
                        jdkReleaseString = "11.0.15+9-LTS-2";
                    ***REMOVED***
                ***REMOVED*** else if (vmInfo.getJavaSpecification() == JavaSpecification.JDK17
                        && jdkReleaseString.equals("17.0.3+6-LTS")) {
                    if (vmInfo.getBuildDate().equals(ErrUtil.getDate("Apr 17 2022 12:11:44"))) {
                        jdkReleaseString = "17.0.3+6-LTS-1";
                    ***REMOVED*** else if (vmInfo.getBuildDate().equals(ErrUtil.getDate("Apr 27 2022 11:51:42"))) {
                        jdkReleaseString = "17.0.3+6-LTS-2";
                    ***REMOVED***
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        if (jdkReleaseString == null && !headers.isEmpty()) {
            // Check header
            Iterator<Header> iterator = headers.iterator();
            while (iterator.hasNext()) {
                Header he = iterator.next();
                if (he.isJreVersion()) {
                    String regEx = "^.+\\(" + JdkRegEx.VERSION_STRING + "\\)( \\(build " + JdkRegEx.BUILD_STRING
                            + "\\))?.*$";
                    Pattern pattern = Pattern.compile(regEx);
                    Matcher matcher = pattern.matcher(he.getLogEntry());
                    if (matcher.find()) {
                        if (matcher.group(4) != null) {
                            jdkReleaseString = matcher.group(4);
                        ***REMOVED*** else if (matcher.group(1) != null) {
                            // Add leading "1."
                            if (matcher.group(1).matches("^[678].+$")) {
                                jdkReleaseString = "1." + matcher.group(1);
                            ***REMOVED*** else {
                                jdkReleaseString = matcher.group(1);
                            ***REMOVED***
                        ***REMOVED***
                    ***REMOVED***
                    break;
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        if (jdkReleaseString == null && !dynamicLibraries.isEmpty()) {
            // Check dynamic libraries (rpm)
            if (getRpmDirectory() != null) {
                Iterator<Entry<String, Release>> iterator;
                if (getJavaSpecification() == JavaSpecification.JDK8) {
                    switch (getOsVersion()) {
                    case CENTOS6:
                    case RHEL6:
                        iterator = JdkUtil.JDK8_RHEL6_X86_64_RPMS.entrySet().iterator();
                        while (iterator.hasNext()) {
                            Entry<String, Release> entry = iterator.next();
                            if (entry.getKey().equals(getRpmDirectory())) {
                                Release release = entry.getValue();
                                jdkReleaseString = release.getVersion();
                            ***REMOVED***
                        ***REMOVED***
                        break;
                    case CENTOS7:
                    case RHEL7:
                        if (getArch() == Arch.X86_64) {
                            iterator = JdkUtil.JDK8_RHEL7_X86_64_RPMS.entrySet().iterator();
                            while (iterator.hasNext()) {
                                Entry<String, Release> entry = iterator.next();
                                if (entry.getKey().equals(getRpmDirectory())) {
                                    Release release = entry.getValue();
                                    jdkReleaseString = release.getVersion();
                                ***REMOVED***
                            ***REMOVED***
                        ***REMOVED*** else if (getArch() == Arch.PPC64) {
                            iterator = JdkUtil.JDK8_RHEL7_PPC64_RPMS.entrySet().iterator();
                            while (iterator.hasNext()) {
                                Entry<String, Release> entry = iterator.next();
                                if (entry.getKey().equals(getRpmDirectory())) {
                                    Release release = entry.getValue();
                                    jdkReleaseString = release.getVersion();
                                ***REMOVED***
                            ***REMOVED***
                        ***REMOVED*** else if (getArch() == Arch.PPC64LE) {
                            iterator = JdkUtil.JDK8_RHEL7_PPC64LE_RPMS.entrySet().iterator();
                            while (iterator.hasNext()) {
                                Entry<String, Release> entry = iterator.next();
                                if (entry.getKey().equals(getRpmDirectory())) {
                                    Release release = entry.getValue();
                                    jdkReleaseString = release.getVersion();
                                ***REMOVED***
                            ***REMOVED***
                        ***REMOVED***
                        break;
                    case CENTOS8:
                    case RHEL8:
                        if (getArch() == Arch.X86_64) {
                            iterator = JdkUtil.JDK8_RHEL8_X86_64_RPMS.entrySet().iterator();
                            while (iterator.hasNext()) {
                                Entry<String, Release> entry = iterator.next();
                                if (entry.getKey().equals(getRpmDirectory())) {
                                    Release release = entry.getValue();
                                    jdkReleaseString = release.getVersion();
                                ***REMOVED***
                            ***REMOVED***
                        ***REMOVED*** else if (getArch() == Arch.PPC64LE) {
                            iterator = JdkUtil.JDK8_RHEL8_PPC64LE_RPMS.entrySet().iterator();
                            while (iterator.hasNext()) {
                                Entry<String, Release> entry = iterator.next();
                                if (entry.getKey().equals(getRpmDirectory())) {
                                    Release release = entry.getValue();
                                    jdkReleaseString = release.getVersion();
                                ***REMOVED***
                            ***REMOVED***
                        ***REMOVED***
                        break;
                    case CENTOS9:
                    case RHEL9:
                        iterator = JdkUtil.JDK8_RHEL9_X86_64_RPMS.entrySet().iterator();
                        while (iterator.hasNext()) {
                            Entry<String, Release> entry = iterator.next();
                            if (entry.getKey().equals(getRpmDirectory())) {
                                Release release = entry.getValue();
                                jdkReleaseString = release.getVersion();
                            ***REMOVED***
                        ***REMOVED***
                        break;
                    case UNIDENTIFIED:
                    default:
                        break;
                    ***REMOVED***
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        if (jdkReleaseString == null) {
            return org.github.krashpad.util.Constants.PROPERTY_UNKNOWN;
        ***REMOVED*** else {
            return jdkReleaseString;
        ***REMOVED***
    ***REMOVED***

    /**
     * @return The JDK rpm RHEL version, or null if unknown.
     */
    public String getJdkRhelVersion() {
        String jdkRhelVersion = null;
        String rpmDirectory = getRpmDirectory();
        if (rpmDirectory != null) {
            Pattern pattern = Pattern.compile(JdkRegEx.RH_RPM_DIR);
            Matcher matcher = pattern.matcher(rpmDirectory);
            if (matcher.find()) {
                if (matcher.group(2) != null) {
                    // JDK8
                    jdkRhelVersion = matcher.group(3);
                    if (matcher.group(5) != null) {
                        jdkRhelVersion = jdkRhelVersion + "." + matcher.group(5);
                    ***REMOVED***
                ***REMOVED*** else if (matcher.group(8) != null) {
                    // JDK11
                    jdkRhelVersion = matcher.group(10);
                    if (matcher.group(12) != null) {
                        jdkRhelVersion = jdkRhelVersion + "." + matcher.group(12);
                    ***REMOVED***
                ***REMOVED*** else if (matcher.group(13) != null) {
                    // JDK17
                    jdkRhelVersion = matcher.group(15);
                    if (matcher.group(16) != null) {
                        jdkRhelVersion = jdkRhelVersion + "." + matcher.group(16);
                    ***REMOVED***
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        return jdkRhelVersion;
    ***REMOVED***

    /**
     * @return The JVM options, or null if none exist.
     */
    public String getJvmArgs() {
        String jvmArgs = null;
        if (!vmArguments.isEmpty()) {
            Iterator<VmArguments> iterator = vmArguments.iterator();
            while (iterator.hasNext()) {
                VmArguments event = iterator.next();
                if (event.isJvmArgs()) {
                    jvmArgs = event.getValue();
                    break;
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        return jvmArgs;
    ***REMOVED***

    /**
     * Free memory as reported by the JVM <code>MemoryEvent</code>.
     * 
     * Note that free memory does not include Buffers or Cached memory, which can be reclaimed at any time. Therefore,
     * low free memory does not necessarily indicate swapping or out of memory is imminent.
     * 
     * @return The total free physical memory as reported by the JVM in
     *         <code>org.github.joa.util.Constants.PRECISION_REPORTING</code> units.
     */
    public long getJvmMemFree() {
        long physicalMemoryFree = Long.MIN_VALUE;
        if (!memories.isEmpty()) {
            Iterator<Memory> iterator = memories.iterator();
            while (iterator.hasNext()) {
                Memory event = iterator.next();
                if (event.isHeader()) {
                    Matcher matcher = Memory.PATTERN.matcher(event.getLogEntry());
                    if (matcher.find()) {
                        physicalMemoryFree = JdkUtil.convertSize(Long.parseLong(matcher.group(7)),
                                matcher.group(9).charAt(0), org.github.joa.util.Constants.UNITS);
                    ***REMOVED***
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        return physicalMemoryFree;
    ***REMOVED***

    /**
     * @return Estimated JVM initial memory in <code>org.github.joa.util.Constants.PRECISION_REPORTING</code> units.
     */
    public long getJvmMemoryInitial() {
        long jvmMemoryInitial = Long.MIN_VALUE;
        if (getHeapMaxSize() > 0) {
            jvmMemoryInitial = getHeapInitialSize();
        ***REMOVED***
        if (getMetaspaceMaxSize() > 0) {
            if (jvmMemoryInitial > 0) {
                jvmMemoryInitial += getMetaspaceMaxSize();
            ***REMOVED*** else {
                jvmMemoryInitial = getMetaspaceMaxSize();
            ***REMOVED***
        ***REMOVED***
        // Thread stack space
        if (getThreadStackMemory() > 0) {
            if (jvmMemoryInitial > 0) {
                jvmMemoryInitial += getThreadStackMemory();
            ***REMOVED*** else {
                jvmMemoryInitial = getThreadStackMemory();
            ***REMOVED***
        ***REMOVED***
        // code cache
        if (jvmMemoryInitial > 0) {
            jvmMemoryInitial += getCodeCacheSize();
        ***REMOVED*** else {
            jvmMemoryInitial = getCodeCacheSize();
        ***REMOVED***
        // Direct memory
        if (jvmMemoryInitial > 0) {
            jvmMemoryInitial += getDirectMemoryMaxSize();
        ***REMOVED*** else {
            jvmMemoryInitial = getDirectMemoryMaxSize();
        ***REMOVED***
        return jvmMemoryInitial;
    ***REMOVED***

    /**
     * @return Estimated JVM maximum memory in <code>org.github.joa.util.Constants.PRECISION_REPORTING</code> units.
     */
    public long getJvmMemoryMax() {
        long jvmMemoryMax = Long.MIN_VALUE;
        if (getHeapMaxSize() > 0) {
            jvmMemoryMax = getHeapMaxSize();
        ***REMOVED***
        if (getMetaspaceMaxSize() > 0) {
            if (jvmMemoryMax > 0) {
                jvmMemoryMax += getMetaspaceMaxSize();
            ***REMOVED*** else {
                jvmMemoryMax = getMetaspaceMaxSize();
            ***REMOVED***
        ***REMOVED***
        // Thread stack space
        if (getThreadStackMemory() > 0) {
            if (jvmMemoryMax > 0) {
                jvmMemoryMax += getThreadStackMemory();
            ***REMOVED*** else {
                jvmMemoryMax = getThreadStackMemory();
            ***REMOVED***
        ***REMOVED***
        // code cache
        if (jvmMemoryMax > 0) {
            jvmMemoryMax += getCodeCacheSize();
        ***REMOVED*** else {
            jvmMemoryMax = getCodeCacheSize();
        ***REMOVED***
        // Direct memory
        if (jvmMemoryMax > 0) {
            jvmMemoryMax += getDirectMemoryMaxSize();
        ***REMOVED*** else {
            jvmMemoryMax = getDirectMemoryMaxSize();
        ***REMOVED***
        return jvmMemoryMax;
    ***REMOVED***

    /**
     * @return The total physical memory reported by the JVM in
     *         <code>org.github.joa.util.Constants.PRECISION_REPORTING</code> units.
     */
    public long getJvmMemTotal() {
        long physicalMemory = Long.MIN_VALUE;
        if (!memories.isEmpty()) {
            Iterator<Memory> iterator = memories.iterator();
            while (iterator.hasNext()) {
                Memory event = iterator.next();
                if (event.isHeader()) {
                    Matcher matcher = Memory.PATTERN.matcher(event.getLogEntry());
                    if (matcher.find()) {
                        physicalMemory = JdkUtil.convertSize(Long.parseLong(matcher.group(4)),
                                matcher.group(6).charAt(0), org.github.joa.util.Constants.UNITS);
                    ***REMOVED***
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        return physicalMemory;
    ***REMOVED***

    public JvmOptions getJvmOptions() {
        return jvmOptions;
    ***REMOVED***

    /**
     * @return The total available swap as reported by the JVM in
     *         <code>org.github.joa.util.Constants.PRECISION_REPORTING</code> units.
     */
    public long getJvmSwap() {
        long swap = Long.MIN_VALUE;
        if (!memories.isEmpty()) {
            Iterator<Memory> iterator = memories.iterator();
            while (iterator.hasNext()) {
                Memory event = iterator.next();
                if (event.isHeader()) {
                    Matcher matcher = Memory.PATTERN.matcher(event.getLogEntry());
                    if (matcher.find()) {
                        if (matcher.group(11) != null && matcher.group(13) != null) {
                            swap = JdkUtil.convertSize(Long.parseLong(matcher.group(11)), matcher.group(13).charAt(0),
                                    org.github.joa.util.Constants.UNITS);
                        ***REMOVED***
                    ***REMOVED***
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        return swap;
    ***REMOVED***

    /**
     * @return The total free swap as reported by the JVM in
     *         <code>org.github.joa.util.Constants.PRECISION_REPORTING</code> units.
     */
    public long getJvmSwapFree() {
        long swapFree = Long.MIN_VALUE;
        if (!memories.isEmpty()) {
            Iterator<Memory> iterator = memories.iterator();
            while (iterator.hasNext()) {
                Memory event = iterator.next();
                if (event.isHeader()) {
                    Matcher matcher = Memory.PATTERN.matcher(event.getLogEntry());
                    if (matcher.find()) {
                        if (matcher.group(14) != null && matcher.group(16) != null) {
                            swapFree = JdkUtil.convertSize(Long.parseLong(matcher.group(14)),
                                    matcher.group(16).charAt(0), org.github.joa.util.Constants.UNITS);
                        ***REMOVED***
                    ***REMOVED***
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        return swapFree;
    ***REMOVED***

    /**
     * Parse JVM user from hsperfdata string. For example, the following user is jb_admin:
     * 
     * 7ff0f61d2000-7ff0f61da000 rw-s 00000000 fd:01 33563495 /tmp/hsperfdata_jb_admin/92333
     * 
     * @return The user the JVM process is running under.
     */
    public String getJvmUser() {
        String jvmUser = null;
        if (!dynamicLibraries.isEmpty()) {
            String regExHsPerfData = System.getProperty("file.separator") + "hsperfdata_([^"
                    + System.getProperty("file.separator") + "]+)";
            Pattern pattern = Pattern.compile(regExHsPerfData);
            Iterator<DynamicLibrary> iterator = dynamicLibraries.iterator();
            while (iterator.hasNext()) {
                DynamicLibrary event = iterator.next();
                if (event.getFilePath() != null) {
                    Matcher matcher = pattern.matcher(event.getFilePath());
                    if (matcher.find()) {
                        jvmUser = matcher.group(1);
                        break;
                    ***REMOVED***
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        return jvmUser;
    ***REMOVED***

    public List<LdPreloadFile> getLdPreloadFiles() {
        return ldPreloadFiles;
    ***REMOVED***

    /**
     * @return The VM state for display purposes.
     */
    public String getLiteral() {
        String vmStateState = null;
        if (vmState != null) {
            vmStateState = vmState.getState();
        ***REMOVED***
        return vmStateState;
    ***REMOVED***

    public MaxMapCount getMaxMapCount() {
        return maxMapCount;
    ***REMOVED***

    /**
     * @return max_map_count, or a negative value if undefined.
     */
    public Long getMaxMapCountLimit() {
        Long maxMapCountLimit = Long.MIN_VALUE;
        if (maxMapCount != null) {
            maxMapCountLimit = maxMapCount.getLimit();
        ***REMOVED***
        return maxMapCountLimit;
    ***REMOVED***

    /**
     * Memory ballooned now. For example:
     * 
     * vSphere resource information available now:
     * 
     * guest.mem.ballooned = 2048
     * 
     * @return The total amount of ballooned memory in <code>org.github.joa.util.Constants.PRECISION_REPORTING</code>
     *         units.
     */
    public long getMemBalloonedNow() {
        long memBallooned = Long.MIN_VALUE;
        if (!virtualizationInfos.isEmpty()) {
            String regexGuestMemBallooned = "guest.mem.ballooned = (\\d{1,***REMOVED***)";
            Pattern pattern = Pattern.compile(regexGuestMemBallooned);
            Iterator<VirtualizationInfo> iterator = virtualizationInfos.iterator();
            // Get information now, not at startup
            boolean now = false;
            while (iterator.hasNext()) {
                VirtualizationInfo event = iterator.next();
                Matcher matcher = pattern.matcher(event.getLogEntry());
                if (now && matcher.find()) {
                    memBallooned = JdkUtil.convertSize(Long.parseLong(matcher.group(1)), 'K',
                            org.github.joa.util.Constants.UNITS);
                    break;
                ***REMOVED*** else if (event.getLogEntry().equals("vSphere resource information available now:")) {
                    now = true;
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        return memBallooned;
    ***REMOVED***

    public List<Meminfo> getMeminfos() {
        return meminfos;
    ***REMOVED***

    public List<Memory> getMemories() {
        return memories;
    ***REMOVED***

    /**
     * @return the memory allocation (in bytes) causing the crash, or a negative number if the crash is not related to a
     *         memory allocation.
     */
    public long getMemoryAllocation() {
        long memoryAllocation = Long.MIN_VALUE;
        if (!headers.isEmpty()) {
            Iterator<Header> iterator = headers.iterator();
            String regex = "^.+failed to (allocate|map) (\\d{1,***REMOVED***) bytes.+$";
            while (iterator.hasNext()) {
                Header he = iterator.next();
                if (he.getLogEntry().matches(regex)) {
                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(he.getLogEntry());
                    if (matcher.find()) {
                        memoryAllocation = JdkUtil.convertSize(Long.parseLong(matcher.group(2)), 'B',
                                org.github.joa.util.Constants.UNITS);
                        break;
                    ***REMOVED***
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        return memoryAllocation;
    ***REMOVED***

    /**
     * @return The total metaspace allocation in <code>org.github.joa.util.Constants.PRECISION_REPORTING</code> units.
     */
    public long getMetaspaceAllocation() {
        long metaspaceAllocation = Long.MIN_VALUE;
        if (!heaps.isEmpty()) {
            Iterator<Heap> iterator = heaps.iterator();
            char fromUnits;
            long value;
            Pattern pattern = null;
            Matcher matcher = null;
            while (iterator.hasNext()) {
                Heap event = iterator.next();
                if (event.isMetaspace()) {
                    pattern = Pattern.compile(JdkRegEx.METASPACE_SIZE);
                    matcher = pattern.matcher(event.getLogEntry());
                    if (matcher.find()) {
                        value = Long.parseLong(matcher.group(8));
                        if (matcher.group(10) != null) {
                            fromUnits = matcher.group(10).charAt(0);
                        ***REMOVED*** else {
                            fromUnits = 'B';
                        ***REMOVED***
                        metaspaceAllocation = JdkUtil.convertSize(value, fromUnits,
                                org.github.joa.util.Constants.UNITS);
                        break;
                    ***REMOVED***
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        return metaspaceAllocation;
    ***REMOVED***

    /**
     * @return The metaspace max size reserved in <code>org.github.joa.util.Constants.PRECISION_REPORTING</code> units.
     */
    /**
     * @return The Metaspace maximum size.
     */
    public long getMetaspaceMaxSize() {
        long metaspaceMaxSize = Long.MIN_VALUE;
        if (jvmOptions != null && jvmOptions.getMaxMetaspaceSize() != null) {
            char fromUnits;
            long value;
            Pattern pattern = Pattern.compile(JdkRegEx.OPTION_SIZE_BYTES);
            Matcher matcher = pattern.matcher(jvmOptions.getMaxMetaspaceSize());
            if (matcher.find()) {
                value = Long.parseLong(matcher.group(2));
                if (matcher.group(3) != null) {
                    fromUnits = matcher.group(3).charAt(0);
                ***REMOVED*** else {
                    fromUnits = 'B';
                ***REMOVED***
                metaspaceMaxSize = JdkUtil.convertSize(value, fromUnits, org.github.joa.util.Constants.UNITS);
            ***REMOVED***
        ***REMOVED***
        // If max metaspace size not set (recommended), get from <code>HeapEvent</code>
        if (metaspaceMaxSize == Long.MIN_VALUE) {
            if (!heaps.isEmpty()) {
                Iterator<Heap> iterator = heaps.iterator();
                char fromUnits;
                long value;
                Pattern pattern = null;
                Matcher matcher = null;
                while (iterator.hasNext()) {
                    Heap event = iterator.next();
                    if (event.isMetaspace()) {
                        pattern = Pattern.compile(JdkRegEx.METASPACE_SIZE);
                        matcher = pattern.matcher(event.getLogEntry());
                        if (matcher.find()) {
                            value = Long.parseLong(matcher.group(11));
                            if (matcher.group(13) != null) {
                                fromUnits = matcher.group(13).charAt(0);
                            ***REMOVED*** else {
                                fromUnits = 'B';
                            ***REMOVED***
                            metaspaceMaxSize = JdkUtil.convertSize(value, fromUnits,
                                    org.github.joa.util.Constants.UNITS);
                            break;
                        ***REMOVED***
                    ***REMOVED***
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        return metaspaceMaxSize;
    ***REMOVED***

    /**
     * @return The total metaspace used in <code>org.github.joa.util.Constants.PRECISION_REPORTING</code> units.
     */
    public long getMetaspaceUsed() {
        long metaspaceUsed = Long.MIN_VALUE;
        if (!heaps.isEmpty()) {
            Iterator<Heap> iterator = heaps.iterator();
            char fromUnits;
            long value;
            Pattern pattern = null;
            Matcher matcher = null;
            while (iterator.hasNext()) {
                Heap event = iterator.next();
                if (event.isMetaspace()) {
                    pattern = Pattern.compile(JdkRegEx.METASPACE_SIZE);
                    matcher = pattern.matcher(event.getLogEntry());
                    if (matcher.find()) {
                        value = Long.parseLong(matcher.group(1));
                        if (matcher.group(3) != null) {
                            fromUnits = matcher.group(3).charAt(0);
                        ***REMOVED*** else {
                            fromUnits = 'B';
                        ***REMOVED***
                        metaspaceUsed = JdkUtil.convertSize(value, fromUnits, org.github.joa.util.Constants.UNITS);
                        break;
                    ***REMOVED***
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        return metaspaceUsed;
    ***REMOVED***

    /**
     * @return The number of mmap resources in a deleted state.
     */
    public int getMmapDeletedCount() {
        int mmapDeletedCount = 0;
        if (!dynamicLibraries.isEmpty()) {
            String regExMmapDeleted = "^.+ \\(deleted\\)$";
            Iterator<DynamicLibrary> iterator = dynamicLibraries.iterator();
            while (iterator.hasNext()) {
                DynamicLibrary event = iterator.next();
                if (event.getLogEntry().matches(regExMmapDeleted)) {
                    mmapDeletedCount++;
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        return mmapDeletedCount;
    ***REMOVED***

    public NarrowKlass getNarrowKlass() {
        return narrowKlass;
    ***REMOVED***

    /**
     * @return Native libraries list (unique entries).
     */
    public List<String> getNativeLibraries() {
        List<String> nativeLibraries = new ArrayList<String>();
        if (!dynamicLibraries.isEmpty()) {
            Iterator<DynamicLibrary> iterator = dynamicLibraries.iterator();
            while (iterator.hasNext()) {
                DynamicLibrary event = iterator.next();
                if (event.isNativeLibrary()) {
                    if (!nativeLibraries.contains(event.getFilePath())) {
                        nativeLibraries.add(event.getFilePath());
                    ***REMOVED***
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        return nativeLibraries;
    ***REMOVED***

    /**
     * @return JBoss native libraries.
     */
    public List<String> getNativeLibrariesJBoss() {
        List<String> jbossNativeLibraries = new ArrayList<String>();
        List<String> nativeLibraries = getNativeLibraries();
        if (!nativeLibraries.isEmpty()) {
            Iterator<String> iterator = nativeLibraries.iterator();
            Pattern pattern = Pattern.compile(JdkRegEx.FILE);
            Matcher matcher;
            while (iterator.hasNext()) {
                String nativeLibraryPath = iterator.next();
                matcher = pattern.matcher(nativeLibraryPath);
                if (matcher.find()) {
                    String nativeLibrary = matcher.group(3);
                    if (ErrUtil.NATIVE_LIBRARIES_JBOSS.contains(nativeLibrary)) {
                        jbossNativeLibraries.add(nativeLibraryPath);
                    ***REMOVED***
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        return jbossNativeLibraries;
    ***REMOVED***

    /**
     * @return Unknown native libraries (not OS, not Java).
     */
    public List<String> getNativeLibrariesUnknown() {
        List<String> unidentifiedNativeLibraries = new ArrayList<String>();
        List<String> nativeLibraries = getNativeLibraries();
        if (!nativeLibraries.isEmpty()) {
            Iterator<String> iterator = nativeLibraries.iterator();
            Pattern pattern = Pattern.compile(JdkRegEx.FILE);
            Matcher matcher;
            while (iterator.hasNext()) {
                String nativeLibraryPath = iterator.next();
                matcher = pattern.matcher(nativeLibraryPath);
                if (matcher.find()) {
                    String nativeLibrary = matcher.group(3);
                    if (!ErrUtil.NATIVE_LIBRARIES_JBOSS.contains(nativeLibrary)
                            && !ErrUtil.NATIVE_LIBRARIES_LINUX.contains(nativeLibrary)
                            && !ErrUtil.NATIVE_LIBRARIES_LINUX_JAVA.contains(nativeLibrary)
                            && !ErrUtil.NATIVE_LIBRARIES_ORACLE.contains(nativeLibrary)
                            && !ErrUtil.NATIVE_LIBRARIES_WINDOWS.contains(nativeLibrary)
                            && !ErrUtil.NATIVE_LIBRARIES_WINDOWS_JAVA.contains(nativeLibrary)) {
                        unidentifiedNativeLibraries.add(nativeLibraryPath);
                    ***REMOVED***
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        return unidentifiedNativeLibraries;
    ***REMOVED***

    /**
     * @return The native library the crash is happening in, or null if the crash does not happen in a native library.
     */
    public String getNativeLibraryInCrash() {
        String nativeLibraryInCrash = null;
        if (getStackFrameTop() != null) {
            Pattern pattern = Pattern.compile("^C[ ]{1,2***REMOVED***\\[(.+\\.(so|dll)).*\\]$");
            Matcher matcher = pattern.matcher(getStackFrameTop());
            if (matcher.find()) {
                nativeLibraryInCrash = matcher.group(1);
            ***REMOVED***
        ***REMOVED***
        return nativeLibraryInCrash;
    ***REMOVED***

    public List<NativeMemoryTracking> getNativeMemoryTrackings() {
        return nativeMemoryTrackings;
    ***REMOVED***

    /**
     * @param stackFrame
     *            A stack frame in the stack.
     * @return The next stack frame, or null if the stack frame passed in does not exist or is at the bottom of the
     *         stack.
     */
    public String getNextStackFrame(String stackFrame) {
        String nextStackFrame = null;
        int stackIndex = 0;
        Iterator<Stack> iterator = stacks.iterator();
        while (iterator.hasNext()) {
            Stack event = iterator.next();
            if (event.getLogEntry().matches("^" + stackFrame + "$") && stackIndex < stacks.size()) {
                nextStackFrame = stacks.get(stackIndex + 1).getLogEntry();
                break;
            ***REMOVED***
            stackIndex++;
        ***REMOVED***
        return nextStackFrame;
    ***REMOVED***

    /**
     * @return <code>Os</code>
     */
    public Os getOs() {
        Os osType = Os.UNIDENTIFIED;
        String osString = getOsString();
        if (osString != null) {
            if (osString.matches(".*Linux.*")) {
                osType = Os.LINUX;
            ***REMOVED*** else if (osString.matches("^Windows.+$")) {
                osType = Os.WINDOWS;
            ***REMOVED*** else if (osString.matches(".+Solaris.+")) {
                osType = Os.SOLARIS;
            ***REMOVED***
        ***REMOVED*** else if (!headers.isEmpty()) {
            // Check header
            Iterator<Header> iterator = headers.iterator();
            while (iterator.hasNext()) {
                Header he = iterator.next();
                if (he.isJavaVm()) {
                    osType = he.getOsType();
                    break;
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        return osType;
    ***REMOVED***

    public List<OsInfo> getOsInfos() {
        return osInfos;
    ***REMOVED***

    /**
     * Available memory is an estimate of how much physical memory is available without swapping. It does not include
     * the memory used by the JVM process, as it is before the JVM process exits and its memory is freed.
     * 
     * @return The total available physical memory as reported by the OS in
     *         <code>org.github.joa.util.Constants.PRECISION_REPORTING</code> units.
     */
    public long getOsMemAvailable() {
        long memAvailable = Long.MIN_VALUE;
        if (!meminfos.isEmpty()) {
            String regexMemTotal = "MemAvailable:[ ]{0,***REMOVED***(\\d{1,***REMOVED***) kB";
            Pattern pattern = Pattern.compile(regexMemTotal);
            Iterator<Meminfo> iterator = meminfos.iterator();
            while (iterator.hasNext()) {
                Meminfo event = iterator.next();
                Matcher matcher = pattern.matcher(event.getLogEntry());
                if (matcher.find()) {
                    memAvailable = JdkUtil.convertSize(Long.parseLong(matcher.group(1)), 'K',
                            org.github.joa.util.Constants.UNITS);
                    break;
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        return memAvailable;
    ***REMOVED***

    /**
     * Free memory as reported by the OS. Free memory does not include Buffers or Cached memory, which can be reclaimed
     * at any time. Therefore, low free memory does not necessarily indicate swapping or out of memory is imminent.
     * 
     * @return The total free physical memory as reported by the OS in
     *         <code>org.github.joa.util.Constants.PRECISION_REPORTING</code> units.
     */
    public long getOsMemFree() {
        long memFree = Long.MIN_VALUE;
        if (!meminfos.isEmpty()) {
            String regexMemTotal = "MemFree:[ ]{0,***REMOVED***(\\d{1,***REMOVED***) kB";
            Pattern pattern = Pattern.compile(regexMemTotal);
            Iterator<Meminfo> iterator = meminfos.iterator();
            while (iterator.hasNext()) {
                Meminfo event = iterator.next();
                Matcher matcher = pattern.matcher(event.getLogEntry());
                if (matcher.find()) {
                    memFree = JdkUtil.convertSize(Long.parseLong(matcher.group(1)), 'K',
                            org.github.joa.util.Constants.UNITS);
                    break;
                ***REMOVED***
            ***REMOVED***
        ***REMOVED*** else if (!memories.isEmpty()) {
            Iterator<Memory> iterator = memories.iterator();
            while (iterator.hasNext()) {
                Memory event = iterator.next();
                if (event.isHeader()) {
                    Pattern pattern = Pattern.compile(Memory._REGEX_HEADER);
                    Matcher matcher = pattern.matcher(event.getLogEntry());
                    if (matcher.find()) {
                        memFree = JdkUtil.convertSize(Long.parseLong(matcher.group(6)), matcher.group(8).charAt(0),
                                org.github.joa.util.Constants.UNITS);
                    ***REMOVED***
                    break;
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        return memFree;
    ***REMOVED***

    /**
     * @return The total available physical memory reported by the OS in
     *         <code>org.github.joa.util.Constants.PRECISION_REPORTING</code> units.
     */
    public long getOsMemTotal() {
        long memTotal = Long.MIN_VALUE;
        if (!meminfos.isEmpty()) {
            String regexMemTotal = "MemTotal:[ ]{0,***REMOVED***(\\d{1,***REMOVED***) kB";
            Pattern pattern = Pattern.compile(regexMemTotal);
            Iterator<Meminfo> iterator = meminfos.iterator();
            while (iterator.hasNext()) {
                Meminfo event = iterator.next();
                Matcher matcher = pattern.matcher(event.getLogEntry());
                if (matcher.find()) {
                    memTotal = JdkUtil.convertSize(Long.parseLong(matcher.group(1)), 'K',
                            org.github.joa.util.Constants.UNITS);
                    break;
                ***REMOVED***
            ***REMOVED***
        ***REMOVED*** else if (!memories.isEmpty()) {
            Iterator<Memory> iterator = memories.iterator();
            while (iterator.hasNext()) {
                Memory event = iterator.next();
                if (event.isHeader()) {
                    Pattern pattern = Pattern.compile(Memory._REGEX_HEADER);
                    Matcher matcher = pattern.matcher(event.getLogEntry());
                    if (matcher.find()) {
                        memTotal = JdkUtil.convertSize(Long.parseLong(matcher.group(3)), matcher.group(5).charAt(0),
                                org.github.joa.util.Constants.UNITS);
                    ***REMOVED***
                    break;
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        return memTotal;
    ***REMOVED***

    /**
     * The OS string. For example:
     * 
     * Red Hat Enterprise Linux Server release 7.7 (Maipo)
     * 
     * @return OS string, of null if it doesn't exist.
     */
    public String getOsString() {
        String osString = null;
        if (!osInfos.isEmpty()) {
            Iterator<OsInfo> iterator = osInfos.iterator();
            while (iterator.hasNext()) {
                OsInfo event = iterator.next();
                if (event.isHeader()) {
                    Matcher matcher = OsInfo.PATTERN.matcher(event.getLogEntry());
                    if (matcher.find()) {
                        if (matcher.group(2) != null) {
                            osString = matcher.group(4).trim();
                        ***REMOVED*** else {
                            // OS is on separate line
                            event = iterator.next();
                            osString = event.getLogEntry();
                        ***REMOVED***
                    ***REMOVED***
                    break;
                ***REMOVED***
            ***REMOVED***
        ***REMOVED*** else if (host != null && host.getOsString() != null) {
            osString = host.getOsString();
        ***REMOVED***
        return osString;
    ***REMOVED***

    /**
     * @return The total available swap as reported by the JVM in
     *         <code>org.github.joa.util.Constants.PRECISION_REPORTING</code> units.
     */
    public long getOsSwap() {
        long swap = Long.MIN_VALUE;
        if (!meminfos.isEmpty()) {
            String regexMemTotal = "SwapTotal:[ ]{0,***REMOVED***(\\d{1,***REMOVED***) kB";
            Pattern pattern = Pattern.compile(regexMemTotal);
            Iterator<Meminfo> iterator = meminfos.iterator();
            while (iterator.hasNext()) {
                Meminfo event = iterator.next();
                Matcher matcher = pattern.matcher(event.getLogEntry());
                if (matcher.find()) {
                    swap = JdkUtil.convertSize(Long.parseLong(matcher.group(1)), 'K',
                            org.github.joa.util.Constants.UNITS);
                    break;
                ***REMOVED***
            ***REMOVED***
        ***REMOVED*** else if (!memories.isEmpty()) {
            Iterator<Memory> iterator = memories.iterator();
            while (iterator.hasNext()) {
                Memory event = iterator.next();
                if (event.isHeader()) {
                    Pattern pattern = Pattern.compile(Memory._REGEX_HEADER);
                    Matcher matcher = pattern.matcher(event.getLogEntry());
                    if (matcher.find() && matcher.group(9) != null) {
                        swap = JdkUtil.convertSize(Long.parseLong(matcher.group(10)), matcher.group(12).charAt(0),
                                org.github.joa.util.Constants.UNITS);
                    ***REMOVED***
                    break;
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        return swap;
    ***REMOVED***

    /**
     * @return The total free swap as reported by the JVM in
     *         <code>org.github.joa.util.Constants.PRECISION_REPORTING</code> units.
     */
    public long getOsSwapFree() {
        long swapFree = Long.MIN_VALUE;
        if (!meminfos.isEmpty()) {
            String regexMemTotal = "SwapFree:[ ]{0,***REMOVED***(\\d{1,***REMOVED***) kB";
            Pattern pattern = Pattern.compile(regexMemTotal);
            Iterator<Meminfo> iterator = meminfos.iterator();
            while (iterator.hasNext()) {
                Meminfo event = iterator.next();
                Matcher matcher = pattern.matcher(event.getLogEntry());
                if (matcher.find()) {
                    swapFree = JdkUtil.convertSize(Long.parseLong(matcher.group(1)), 'K',
                            org.github.joa.util.Constants.UNITS);
                    break;
                ***REMOVED***
            ***REMOVED***
        ***REMOVED*** else if (!memories.isEmpty()) {
            Iterator<Memory> iterator = memories.iterator();
            while (iterator.hasNext()) {
                Memory event = iterator.next();
                if (event.isHeader()) {
                    Pattern pattern = Pattern.compile(Memory._REGEX_HEADER);
                    Matcher matcher = pattern.matcher(event.getLogEntry());
                    if (matcher.find() && matcher.group(9) != null) {
                        swapFree = JdkUtil.convertSize(Long.parseLong(matcher.group(13)), matcher.group(15).charAt(0),
                                org.github.joa.util.Constants.UNITS);
                    ***REMOVED***
                    break;
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        return swapFree;
    ***REMOVED***

    /**
     * @return <code>OsVendor</code>
     */
    public OsVendor getOsVendor() {
        OsVendor osVendor = OsVendor.UNIDENTIFIED;
        if (!osInfos.isEmpty()) {
            Iterator<OsInfo> iterator = osInfos.iterator();
            while (iterator.hasNext()) {
                OsInfo event = iterator.next();
                if (event.isHeader()) {
                    if (event.getLogEntry().matches("^OS:$")) {
                        // OS string on next line
                        event = iterator.next();
                    ***REMOVED***
                    if (event.getLogEntry().matches("^.*Red Hat.+$")) {
                        osVendor = OsVendor.REDHAT;
                    ***REMOVED*** else if (event.getLogEntry().matches(".*Windows.+$")) {
                        osVendor = OsVendor.MICROSOFT;
                    ***REMOVED*** else if (event.getLogEntry().matches("^.+Oracle.+$")) {
                        osVendor = OsVendor.ORACLE;
                    ***REMOVED*** else if (event.getLogEntry().matches("^.*CentOS.+$")) {
                        osVendor = OsVendor.CENTOS;
                    ***REMOVED***
                    break;
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***

        return osVendor;
    ***REMOVED***

    /**
     * @return <code>OsVersion</code>
     */
    public OsVersion getOsVersion() {
        OsVersion osVersion = OsVersion.UNIDENTIFIED;
        if (!osInfos.isEmpty()) {
            Iterator<OsInfo> iterator = osInfos.iterator();
            while (iterator.hasNext()) {
                OsInfo event = iterator.next();
                if (event.isHeader()) {
                    if (event.getLogEntry().matches("^OS:$")) {
                        // OS on next line
                        event = iterator.next();
                    ***REMOVED***
                    if (event.getLogEntry().matches("^.*Red Hat Enterprise Linux (Server|Workstation) release 6.+$")) {
                        osVersion = OsVersion.RHEL6;
                    ***REMOVED*** else if (event.getLogEntry()
                            .matches("^.*Red Hat Enterprise Linux (Server|Workstation) release 7.+$")) {
                        osVersion = OsVersion.RHEL7;
                    ***REMOVED*** else if (event.getLogEntry().matches("^.*Red Hat Enterprise Linux release 8.+$")) {
                        osVersion = OsVersion.RHEL8;
                    ***REMOVED*** else if (event.getLogEntry().matches("^.*Red Hat Enterprise Linux release 9.+$")) {
                        osVersion = OsVersion.RHEL9;
                    ***REMOVED*** else if (event.getLogEntry().matches("^.*CentOS Linux release 6.+$")) {
                        osVersion = OsVersion.CENTOS6;
                    ***REMOVED*** else if (event.getLogEntry().matches("^.*CentOS Linux release 7.+$")) {
                        osVersion = OsVersion.CENTOS7;
                    ***REMOVED*** else if (event.getLogEntry().matches("^.*CentOS Linux release 8.+$")) {
                        osVersion = OsVersion.CENTOS8;
                    ***REMOVED***
                    break;
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        if (osVersion == OsVersion.UNIDENTIFIED && uname != null) {
            osVersion = uname.getOsVersion();
        ***REMOVED***
        return osVersion;
    ***REMOVED***

    public PidMax getPidMax() {
        return pidMax;
    ***REMOVED***

    /**
     * @return pid_max, or a negative value if undefined.
     */
    public Long getPidMaxLimit() {
        Long pidMaxLimit = Long.MIN_VALUE;
        if (pidMax != null) {
            pidMaxLimit = pidMax.getLimit();
        ***REMOVED***
        return pidMaxLimit;
    ***REMOVED***

    public List<RegisterToMemoryMapping> getRegisterToMemoryMappings() {
        return registerToMemoryMappings;
    ***REMOVED***

    /**
     * @return The RHEL version, or null if unknown.
     */
    public String getRhelVersion() {
        String rhelVersion = null;
        String osString = getOsString();
        if (osString != null) {
            String regex = "^.*Red Hat Enterprise Linux (Server )?release (\\d\\.\\d{1,2***REMOVED***).*$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(osString);
            if (matcher.find()) {
                rhelVersion = matcher.group(2);
            ***REMOVED***
        ***REMOVED***
        return rhelVersion;
    ***REMOVED***

    public Rlimit getRlimit() {
        return rlimit;
    ***REMOVED***

    /**
     * @return The rpm directory name, or null if not an rpm install.
     *
     *         For example:
     * 
     *         java-1.8.0-openjdk-1.8.0.262.b10-0.el6_10.x86_64
     * 
     *         java-11-openjdk-11.0.7.10-4.el7_8.x86_64
     * 
     *         java-17-openjdk-17.0.4.1.1-2.el9_0.x86_64
     */
    public String getRpmDirectory() {
        String rpmDirectory = null;
        if (getOs() == Os.LINUX) {
            if (!dynamicLibraries.isEmpty()) {
                Iterator<DynamicLibrary> iterator = dynamicLibraries.iterator();
                while (iterator.hasNext()) {
                    DynamicLibrary event = iterator.next();
                    if (event.getFilePath() != null) {
                        Pattern pattern = null;
                        Matcher matcher = null;
                        if (event.getFilePath().matches(JdkRegEx.RH_RPM_OPENJDK8_LIBJVM_PATH)) {
                            pattern = Pattern.compile(JdkRegEx.RH_RPM_OPENJDK8_LIBJVM_PATH);
                            matcher = pattern.matcher(event.getFilePath());
                            if (matcher.find()) {
                                rpmDirectory = matcher.group(1);
                            ***REMOVED***
                            break;
                        ***REMOVED*** else if (event.getFilePath().matches(JdkRegEx.RH_RPM_OPENJDK11_LIBJVM_PATH)) {
                            pattern = Pattern.compile(JdkRegEx.RH_RPM_OPENJDK11_LIBJVM_PATH);
                            matcher = pattern.matcher(event.getFilePath());
                            if (matcher.find()) {
                                rpmDirectory = matcher.group(1);
                            ***REMOVED***
                            break;
                        ***REMOVED*** else if (event.getFilePath().matches(JdkRegEx.RH_RPM_OPENJDK17_LIBJVM_PATH)) {
                            pattern = Pattern.compile(JdkRegEx.RH_RPM_OPENJDK17_LIBJVM_PATH);
                            matcher = pattern.matcher(event.getFilePath());
                            if (matcher.find()) {
                                rpmDirectory = matcher.group(1);
                            ***REMOVED***
                            break;
                        ***REMOVED***
                    ***REMOVED***
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        return rpmDirectory;
    ***REMOVED***

    public SigInfo getSigInfo() {
        return sigInfo;
    ***REMOVED***

    /**
     * @return Signal code.
     */
    public SignalCode getSignalCode() {
        SignalCode signalCode = SignalCode.UNKNOWN;
        if (sigInfo != null) {
            signalCode = sigInfo.getSignalCode();
        ***REMOVED***
        return signalCode;
    ***REMOVED***

    /**
     * @return Signal number.
     */
    public SignalNumber getSignalNumber() {
        SignalNumber signalNumber = SignalNumber.UNKNOWN;
        if (sigInfo != null) {
            signalNumber = sigInfo.getSignalNumber();
        ***REMOVED***
        return signalNumber;
    ***REMOVED***

    /**
     * @param i
     *            The stack frame index (1 = top).
     * @return The stack frame at the specified position.
     */
    public String getStackFrame(int i) {
        String stackFrame = null;
        int stackIndex = 1;
        Iterator<Stack> iterator = stacks.iterator();
        while (iterator.hasNext()) {
            Stack event = iterator.next();
            if (!event.getLogEntry().matches("^(Stack|(Java|Native) frames):.+$")) {
                if (stackIndex == i) {
                    stackFrame = event.getLogEntry();
                    break;
                ***REMOVED***
                stackIndex++;
            ***REMOVED***
        ***REMOVED***
        return stackFrame;
    ***REMOVED***

    /**
     * @return The top stack frame, or null if none exists.
     */
    public String getStackFrameTop() {
        String stackFrameTop = null;
        Iterator<Stack> iteratorStack = stacks.iterator();
        while (iteratorStack.hasNext()) {
            Stack event = iteratorStack.next();
            if (event.getLogEntry().matches("^(A|C|j|J|v|V)[ ]{1,2***REMOVED***.+$")) {
                stackFrameTop = event.getLogEntry();
                break;
            ***REMOVED***
        ***REMOVED***
        if (stackFrameTop == null && !headers.isEmpty()) {
            Iterator<Header> iteratorHeader = headers.iterator();
            while (iteratorHeader.hasNext()) {
                Header he = iteratorHeader.next();
                if (he.isProblematicFrame()) {
                    stackFrameTop = he.getLogEntry().substring(2, he.getLogEntry().length());
                    break;
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        return stackFrameTop;
    ***REMOVED***

    /**
     * @return The top Compile Java Code (J) stack frame, or null if none exists.
     */
    public String getStackFrameTopCompiledJavaCode() {
        String stackFrameTopCompiledJavaCode = null;
        Iterator<Stack> iterator = stacks.iterator();
        while (iterator.hasNext()) {
            Stack event = iterator.next();
            if (event.getLogEntry().matches("^J[ ]{1,2***REMOVED***.+$")) {
                stackFrameTopCompiledJavaCode = event.getLogEntry();
                break;
            ***REMOVED***
        ***REMOVED***
        return stackFrameTopCompiledJavaCode;
    ***REMOVED***

    /**
     * @return The top Java stack frame (J=compiled Java code, j=interpreted), or null if none exists.
     */
    public String getStackFrameTopJava() {
        String stackFrameTopJava = null;
        Iterator<Stack> iterator = stacks.iterator();
        while (iterator.hasNext()) {
            Stack event = iterator.next();
            if (event.getLogEntry().matches("^[jJ][ ]{1,2***REMOVED***.+$")) {
                stackFrameTopJava = event.getLogEntry();
                break;
            ***REMOVED***
        ***REMOVED***
        return stackFrameTopJava;
    ***REMOVED***

    public List<Stack> getStacks() {
        return stacks;
    ***REMOVED***

    public List<StackSlotToMemoryMapping> getStackSlotToMemoryMappings() {
        return stackSlotToMemoryMappings;
    ***REMOVED***

    /**
     * @return The storage device where the JDK is installed.
     */
    public Device getStorageDevice() {
        Device device = Device.UNIDENTIFIED;
        if (getOs() == Os.LINUX && !dynamicLibraries.isEmpty()) {
            Iterator<DynamicLibrary> iterator = dynamicLibraries.iterator();
            while (iterator.hasNext()) {
                DynamicLibrary event = iterator.next();
                if (event.getFilePath() != null && event.getFilePath().matches("^.+libjvm\\.so$")) {
                    device = event.getDevice();
                    break;
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        return device;
    ***REMOVED***

    public List<Thread> getThreads() {
        return threads;
    ***REMOVED***

    public ThreadsMax getThreadsMax() {
        return threadsMax;
    ***REMOVED***

    /**
     * @return threads-max, or a negative value if undefined.
     */
    public Long getThreadsMaxLimit() {
        Long threadsMaxLimit = Long.MIN_VALUE;
        if (threadsMax != null) {
            threadsMaxLimit = threadsMax.getLimit();
        ***REMOVED***
        return threadsMaxLimit;
    ***REMOVED***

    /**
     * Applies only to ThreadStackSize (not CompilerThreadStackSize, VMThreadStackSize, MarkStackSize, or // the
     * JLI_Launch method in main.c that starts the JVM).
     * 
     * @return The stack free space in <code>org.github.joa.util.Constants.PRECISION_REPORTING</code> units.
     */
    public long getThreadStackFreeSpace() {
        long stackFreeSpace = Long.MIN_VALUE;
        if (!stacks.isEmpty()) {
            Iterator<Stack> iterator = stacks.iterator();
            while (iterator.hasNext()) {
                Stack event = iterator.next();
                if (event.isHeader()) {
                    stackFreeSpace = event.getStackFreeSpace();
                    break;
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        return stackFreeSpace;
    ***REMOVED***

    /**
     * @return The thread memory in <code>org.github.joa.util.Constants.PRECISION_REPORTING</code> units.
     */
    public long getThreadStackMemory() {
        long threadStackMemory = Long.MIN_VALUE;
        if (getJavaThreadCount() > 0) {
            BigDecimal memoryPerThread = new BigDecimal(getThreadStackSize());
            BigDecimal threads = new BigDecimal(getJavaThreadCount());
            threadStackMemory = memoryPerThread.multiply(threads).longValue();
            threadStackMemory = JdkUtil.convertSize(threadStackMemory, 'K', org.github.joa.util.Constants.UNITS);
        ***REMOVED***
        return threadStackMemory;
    ***REMOVED***

    /**
     * Thread stack size for threads other than VMThread (VMThreadStackSize) and CompilerThread
     * (CompilerThreadStackSize).
     * 
     * @return The stack size reserved (kilobytes).
     */
    public long getThreadStackSize() {
        long stackSize;
        switch (getArch()) {
        case PPC64:
        case PPC64LE:
            stackSize = 2048;
            break;
        case SPARC:
        case I86PC:
        case X86:
        case X86_64:
        case UNKNOWN:
        default:
            stackSize = 1024;
            break;
        ***REMOVED***
        // 1st check [Global flags]
        if (!globalFlags.isEmpty()) {
            Iterator<GlobalFlag> iterator = globalFlags.iterator();
            while (iterator.hasNext()) {
                GlobalFlag event = iterator.next();
                String regExThreadStackSize = "^.+intx ThreadStackSize[ ]{1,***REMOVED***= (\\d{1,***REMOVED***).+$";
                Pattern pattern = Pattern.compile(regExThreadStackSize);
                Matcher matcher = pattern.matcher(event.getLogEntry());
                if (matcher.find()) {
                    stackSize = Long.parseLong(matcher.group(1));
                    break;
                ***REMOVED***
            ***REMOVED***
        ***REMOVED*** else if (jvmOptions != null && jvmOptions.getThreadStackSize() != null) {
            char fromUnits;
            long value;
            Pattern pattern = Pattern.compile("^-(X)?(ss|X:ThreadStackSize=)" + JdkRegEx.OPTION_SIZE_BYTES + "$");
            Matcher matcher = pattern.matcher(jvmOptions.getThreadStackSize());
            if (matcher.find()) {
                value = Long.parseLong(matcher.group(4));
                if (matcher.group(2) != null && matcher.group(2).equals("X:ThreadStackSize=")) {
                    // value is in kilobytes, multiply by 1024
                    value = JdkUtil.convertSize(value, 'K', 'B');
                ***REMOVED***
                if (matcher.group(5) != null) {
                    fromUnits = matcher.group(5).charAt(0);
                ***REMOVED*** else {
                    fromUnits = 'B';
                ***REMOVED***
                stackSize = JdkUtil.convertSize(value, fromUnits, 'K');
            ***REMOVED***
        ***REMOVED***
        return stackSize;

    ***REMOVED***

    public TimeElapsedTime getTimeElapsedTime() {
        return timeElapsedTime;
    ***REMOVED***

    public Uname getUname() {
        return uname;
    ***REMOVED***

    public List<String> getUnidentifiedLogLines() {
        return unidentifiedLogLines;
    ***REMOVED***

    /**
     * @return The duration of the JVM run in milliseconds.
     */
    public Long getUptime() {
        long uptime = Long.MIN_VALUE;
        if (timeElapsedTime != null && timeElapsedTime.getUptime() > 0) {
            uptime = timeElapsedTime.getUptime();
        ***REMOVED*** else if (elapsedTime != null && elapsedTime.getUptime() > 0) {
            uptime = elapsedTime.getUptime();
        ***REMOVED***
        return uptime;
    ***REMOVED***

    /**
     * @return The USERNAME environment variable.
     */
    public String getUsername() {
        String username = null;
        if (!environmentVariables.isEmpty()) {
            String regExUsername = "^USERNAME=(.+)$";
            Pattern pattern = Pattern.compile(regExUsername);
            Iterator<EnvironmentVariable> iterator = environmentVariables.iterator();
            while (iterator.hasNext()) {
                EnvironmentVariable event = iterator.next();
                Matcher matcher = pattern.matcher(event.getLogEntry());
                if (matcher.find()) {
                    username = matcher.group(1);
                    break;
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        return username;
    ***REMOVED***

    public List<VirtualizationInfo> getVirtualizationInfos() {
        return virtualizationInfos;
    ***REMOVED***

    public List<VmArguments> getVmArguments() {
        return vmArguments;
    ***REMOVED***

    public VmOperation getVmOperation() {
        return vmOperation;
    ***REMOVED***

    public VmState getVmState() {
        return vmState;
    ***REMOVED***

    /**
     * @param key
     *            The analysis to check.
     * @return True if the {@link org.github.joa.util.Analysis***REMOVED*** exists, false otherwise.
     */
    public boolean hasAnalysis(String key) {
        boolean hasAnalysis = false;
        List<String[]> analysis = getAnalysis();
        if (!analysis.isEmpty()) {
            Iterator<String[]> i = analysis.iterator();
            while (i.hasNext()) {
                String[] a = i.next();
                if (a[0].equals(key)) {
                    hasAnalysis = true;
                    break;
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        return hasAnalysis;
    ***REMOVED***

    /**
     * @param capability
     *            The cpu capability as a regex.
     * @return True if the CPU has the specified capability (specified as a regex), false otherwise.
     */
    public boolean hasCpuCapability(String capability) {
        boolean hasCpuCapability = false;
        if (!cpuInfos.isEmpty()) {
            Iterator<CpuInfo> iterator = cpuInfos.iterator();
            while (iterator.hasNext()) {
                CpuInfo event = iterator.next();
                if (event.isCpuHeader()) {
                    hasCpuCapability = event.getLogEntry().matches("^.*( " + capability + ",.+| " + capability + ")$");
                    break;
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        return hasCpuCapability;
    ***REMOVED***

    /**
     * @return true if there is a cgroup memory limit, false otherwise.
     */
    public boolean haveCgroupMemoryLimit() {
        boolean isCgroupMemoryLimit = false;
        if (!containerInfos.isEmpty()) {
            Iterator<ContainerInfo> iterator = containerInfos.iterator();
            while (iterator.hasNext()) {
                ContainerInfo event = iterator.next();
                if (event.getLogEntry().matches("^memory_limit_in_bytes: \\d{1,***REMOVED***$")) {
                    isCgroupMemoryLimit = true;
                    break;
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        return isCgroupMemoryLimit;
    ***REMOVED***

    /**
     * @return true if the stack contains frames, false otherwise.
     */
    public boolean haveFramesInStack() {
        boolean haveFramesInStack = false;
        if (!stacks.isEmpty()) {
            Iterator<Stack> iterator = stacks.iterator();
            while (iterator.hasNext()) {
                Stack event = iterator.next();
                if (event.isFrame()) {
                    haveFramesInStack = true;
                    break;
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        return haveFramesInStack;
    ***REMOVED***

    /**
     * @return true if JDK debug symbols are installed, false otherwise.
     */
    public boolean haveJdkDebugSymbols() {
        boolean haveJdkDebugSymbols = false;
        if (!headers.isEmpty()) {
            Iterator<Header> iterator1 = headers.iterator();
            while (iterator1.hasNext()) {
                Header he = iterator1.next();
                if (he.isProblematicFrame()) {
                    haveJdkDebugSymbols = he.getLogEntry().matches("^***REMOVED*** V  \\[.+\\].+$");
                    break;
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        if (!haveJdkDebugSymbols) {
            if (!stacks.isEmpty()) {
                Iterator<Stack> iterator2 = stacks.iterator();
                while (iterator2.hasNext() && !haveJdkDebugSymbols) {
                    Stack se = iterator2.next();
                    if (se.isVmFrame()) {
                        haveJdkDebugSymbols = se.getLogEntry().matches("^V  \\[.+\\].+$")
                                && !se.getLogEntry().matches("^V  \\[.+\\]  JVM_DoPrivileged.+$");
                    ***REMOVED***
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        return haveJdkDebugSymbols;
    ***REMOVED***

    /**
     * @return true if there were LinkageErrors before the crash, false otherwise.
     */
    public boolean haveLinkageError() {
        boolean haveLinkageError = false;
        if (!exceptionCounts.isEmpty()) {
            Iterator<ExceptionCounts> iteratorExceptionCounts = exceptionCounts.iterator();
            while (iteratorExceptionCounts.hasNext()) {
                ExceptionCounts exceptionCountsEvent = iteratorExceptionCounts.next();
                if (!exceptionCountsEvent.isHeader()
                        && exceptionCountsEvent.getLogEntry().matches("^LinkageErrors=\\d{1,***REMOVED***$")) {
                    haveLinkageError = true;
                    break;
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        return haveLinkageError;
    ***REMOVED***

    /**
     * @return true if there were "OutOfMemoryError: Compressed class space" or caught and thrown before the crash,
     *         false otherwise.
     */
    public boolean haveOomeThrownCompressedClassSpace() {
        boolean haveOomeThrownCompressedClassSpace = false;
        if (!exceptionCounts.isEmpty()) {
            Iterator<ExceptionCounts> iteratorExceptionCounts = exceptionCounts.iterator();
            while (iteratorExceptionCounts.hasNext()) {
                ExceptionCounts exceptionCountsEvent = iteratorExceptionCounts.next();
                if (!exceptionCountsEvent.isHeader() && exceptionCountsEvent.getLogEntry()
                        .matches("^OutOfMemoryError class_metaspace_errors=\\d{1,***REMOVED***$")) {
                    haveOomeThrownCompressedClassSpace = true;
                    break;
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        return haveOomeThrownCompressedClassSpace;
    ***REMOVED***

    /**
     * @return true if there were OutOfMemoryError other than "Metaspace" or "Compressed class space" caught and thrown
     *         before the crash, false otherwise.
     */
    public boolean haveOomeThrownJavaHeap() {
        boolean haveOomeThrownJavaHeap = false;
        if (!exceptionCounts.isEmpty()) {
            Iterator<ExceptionCounts> iteratorExceptionCounts = exceptionCounts.iterator();
            while (iteratorExceptionCounts.hasNext()) {
                ExceptionCounts exceptionCountsEvent = iteratorExceptionCounts.next();
                if (!exceptionCountsEvent.isHeader()
                        && exceptionCountsEvent.getLogEntry().matches("^OutOfMemoryError java_heap_errors=\\d{1,***REMOVED***$")) {
                    haveOomeThrownJavaHeap = true;
                    break;
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        return haveOomeThrownJavaHeap;
    ***REMOVED***

    /**
     * @return true if there were "OutOfMemoryError: Metaspace" or caught and thrown before the crash, false otherwise.
     */
    public boolean haveOomeThrownMetaspace() {
        boolean haveOomeThrownMetaspace = false;
        if (!exceptionCounts.isEmpty()) {
            Iterator<ExceptionCounts> iteratorExceptionCounts = exceptionCounts.iterator();
            while (iteratorExceptionCounts.hasNext()) {
                ExceptionCounts exceptionCountsEvent = iteratorExceptionCounts.next();
                if (!exceptionCountsEvent.isHeader()
                        && exceptionCountsEvent.getLogEntry().matches("^OutOfMemoryError metaspace_errors=\\d{1,***REMOVED***$")) {
                    haveOomeThrownMetaspace = true;
                    break;
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        return haveOomeThrownMetaspace;
    ***REMOVED***

    /**
     * @return true if there were StackOverflowErrors before the crash, false otherwise.
     */
    public boolean haveStackOverFlowError() {
        boolean haveStackOverFlowError = false;
        if (!exceptionCounts.isEmpty()) {
            Iterator<ExceptionCounts> iteratorExceptionCounts = exceptionCounts.iterator();
            while (iteratorExceptionCounts.hasNext()) {
                ExceptionCounts exceptionCountsEvent = iteratorExceptionCounts.next();
                if (!exceptionCountsEvent.isHeader()
                        && exceptionCountsEvent.getLogEntry().matches("^StackOverflowErrors=\\d{1,***REMOVED***$")) {
                    haveStackOverFlowError = true;
                    break;
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        return haveStackOverFlowError;
    ***REMOVED***

    /**
     * @return true if the stack contains JDK VM frame code, false otherwise.
     */
    public boolean haveVmCodeInStack() {
        boolean haveVmCodeInStack = false;
        //
        if (stacks.size() > 2) {
            Iterator<Stack> iterator = stacks.iterator();
            while (iterator.hasNext()) {
                Stack event = iterator.next();
                if (event.isVmFrame() || event.isVmGeneratedCodeFrame()) {
                    haveVmCodeInStack = true;
                    break;
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        return haveVmCodeInStack;
    ***REMOVED***

    /**
     * @return true if the header contains JDK VM frame code, false otherwise.
     */
    public boolean haveVmFrameInHeader() {
        boolean haveVmFrameInHeader = false;
        if (!headers.isEmpty()) {
            Iterator<Header> iterator = headers.iterator();
            while (iterator.hasNext()) {
                Header event = iterator.next();
                if (event.isVmFrame()) {
                    haveVmFrameInHeader = true;
                    break;
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        return haveVmFrameInHeader;
    ***REMOVED***

    /**
     * @return true if the stack contains JDK VM frame, false otherwise.
     */
    public boolean haveVmFrameInStack() {
        boolean haveVmFrameInStack = false;
        if (!stacks.isEmpty()) {
            Iterator<Stack> iterator = stacks.iterator();
            while (iterator.hasNext()) {
                Stack event = iterator.next();
                if (event.isVmFrame()) {
                    haveVmFrameInStack = true;
                    break;
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        return haveVmFrameInStack;
    ***REMOVED***

    /**
     * @return true if the stack contains JDK VM generated code frame, false otherwise.
     */
    public boolean haveVmGeneratedCodeFrameInStack() {
        boolean haveVmGeneratedCodeFrameInStack = false;
        if (!stacks.isEmpty()) {
            Iterator<Stack> iterator = stacks.iterator();
            while (iterator.hasNext()) {
                Stack event = iterator.next();
                if (event.isVmGeneratedCodeFrame()) {
                    haveVmGeneratedCodeFrameInStack = true;
                    break;
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        return haveVmGeneratedCodeFrameInStack;
    ***REMOVED***

    /**
     * AdoptOpenJDK has the same release versions as the RH build of OpenJDK but have a different build date/time and
     * builder string ("jenkins").
     * 
     * @return true if the fatal error log was created by a JDK build string used by AdoptOpenJDK, false otherwise.
     */
    public boolean isAdoptOpenJdkBuildString() {
        return vmInfo != null && (vmInfo.getBuiltBy() == BuiltBy.JENKINS);
    ***REMOVED***

    /**
     * @return true if there is evidence the crash happens in a container environment, false otherwise.
     */
    public boolean isContainer() {
        boolean isContainer = false;
        if (!containerInfos.isEmpty() && (getOsSwap() == 0 || getJvmSwap() == 0)) {
            isContainer = true;
        ***REMOVED***
        return isContainer;
    ***REMOVED***

    /**
     * @return true if the crash happens when the JVM starts, false otherwise.
     */
    public boolean isCrashOnStartup() {
        boolean isCrashOnStartup = false;
        if (getElapsedTime() != null && getElapsedTime().matches("0d 0h 0m 0s")) {
            isCrashOnStartup = true;
        ***REMOVED***
        return isCrashOnStartup;
    ***REMOVED***

    /**
     * @param errorRegEx
     *            The error regex to search for.
     * @return True if the crash error contains the string, false otherwise.
     */
    public boolean isError(String errorRegEx) {
        boolean isError = false;
        Pattern pattern = Pattern.compile(errorRegEx);
        Matcher matcher = pattern.matcher(getError());
        if (matcher.find()) {
            isError = true;
        ***REMOVED***
        return isError;
    ***REMOVED***

    /**
     * @return true if HyperV environment, false otherwise.
     */
    public boolean isHyperVEnvironment() {
        boolean isVMWareEnvironment = false;
        if (!containerInfos.isEmpty()) {
            Iterator<ContainerInfo> iterator = containerInfos.iterator();
            while (iterator.hasNext()) {
                ContainerInfo event = iterator.next();
                if (event.getLogEntry().matches("^HyperV virtualization detected$")) {
                    isVMWareEnvironment = true;
                    break;
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        return isVMWareEnvironment;
    ***REMOVED***

    /**
     * @param regEx
     *            A regular expression.
     * @return true if the regex is in the header, false otherwise.
     */
    public boolean isInHeader(String regEx) {
        boolean isInHeader = false;
        if (!headers.isEmpty()) {
            Iterator<Header> iterator = headers.iterator();
            while (iterator.hasNext()) {
                Header event = iterator.next();
                if (event.getLogEntry().matches("^.*" + regEx + ".*$")) {
                    isInHeader = true;
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        return isInHeader;
    ***REMOVED***

    /**
     * @param classRegEx
     *            A class name as a regular expression.
     * @return true if the class is in the stack, false otherwise.
     */
    public boolean isInStack(String classRegEx) {
        boolean isInStack = false;
        if (!stacks.isEmpty()) {
            Iterator<Stack> iterator = stacks.iterator();
            while (iterator.hasNext()) {
                Stack event = iterator.next();
                if (event.getLogEntry().matches("^.+" + classRegEx + ".+$")) {
                    isInStack = true;
                    break;
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        if (!isInStack) {
            if (!stackSlotToMemoryMappings.isEmpty()) {
                Iterator<StackSlotToMemoryMapping> iterator = stackSlotToMemoryMappings.iterator();
                while (iterator.hasNext()) {
                    StackSlotToMemoryMapping event = iterator.next();
                    if (event.getLogEntry().matches("^.+" + classRegEx + ".+$")) {
                        isInStack = true;
                        break;
                    ***REMOVED***
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        if (!isInStack) {
            if (!registerToMemoryMappings.isEmpty()) {
                Iterator<RegisterToMemoryMapping> iterator = registerToMemoryMappings.iterator();
                while (iterator.hasNext()) {
                    RegisterToMemoryMapping event = iterator.next();
                    if (event.getLogEntry().matches("^.+" + classRegEx + ".+$")) {
                        isInStack = true;
                        break;
                    ***REMOVED***
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        return isInStack;
    ***REMOVED***

    /**
     * @return true if the fatal error log was created by a JDK that is a Long Term Support (LTS) version, false
     *         otherwise.
     */
    public boolean isJdkLts() {
        boolean isJdkLts = false;
        switch (getJavaSpecification()) {
        case JDK6:
        case JDK7:
        case JDK8:
        case JDK11:
        case JDK17:
            isJdkLts = true;
            break;
        case JDK9:
        case JDK10:
        case JDK12:
        case JDK13:
        case JDK14:
        case JDK15:
        case JDK16:
        case UNKNOWN:
        default:
            break;
        ***REMOVED***
        return isJdkLts;
    ***REMOVED***

    /**
     * @return true if the crash happens in JNA code, false otherwise.
     */
    public boolean isJnaCrash() {
        boolean isJnaCrash = false;
        if (getStacks() != null && getStacks().size() >= 2) {
            String stackFrame2 = getStackFrame(2);
            if (stackFrame2 != null && stackFrame2.matches("^[CjJ].+com[\\._]sun[\\._]jna[\\._].+$")) {
                isJnaCrash = true;
            ***REMOVED***
        ***REMOVED***
        return isJnaCrash;
    ***REMOVED***

    /**
     * @return true if the crash is due to a memory allocation failing, false otherwise.
     */
    public boolean isMemoryAllocationFail() {
        boolean isMemoryAllocationFail = false;
        if (isError("There is insufficient memory for the Java Runtime Environment to continue.")) {
            isMemoryAllocationFail = true;
        ***REMOVED***
        return isMemoryAllocationFail;
    ***REMOVED***

    /**
     * @return true if memory is overcommitted, false otherwise.
     */
    private boolean isOvercommitted() {
        boolean isOvercommitted = false;
        if (getCommitLimit() > 0 && getCommittedAs() > 0 && (getCommittedAs() > getCommitLimit())) {
            isOvercommitted = true;
        ***REMOVED***
        return isOvercommitted;
    ***REMOVED***

    /**
     * @return true if the JDK build date/time is a Red Hat build date/time, false otherwise.
     */
    public boolean isRhBuildDate() {
        boolean isRhBuildDate = false;
        if (getJdkBuildDate() != null) {
            String releaseString = getJdkReleaseString();
            String rpmDirectory = getRpmDirectory();
            if (getOs() == Os.LINUX) {
                switch (getJavaSpecification()) {
                case JDK8:
                    isRhBuildDate = (JdkUtil.JDK8_RHEL_ZIPS.containsKey(releaseString)
                            && JdkUtil.isBuildDateKnown(JdkUtil.JDK8_RHEL_ZIPS.get(releaseString).getBuildDate())
                            && getJdkBuildDate()
                                    .compareTo(JdkUtil.JDK8_RHEL_ZIPS.get(releaseString).getBuildDate()) == 0)
                            || (JdkUtil.JDK8_RHEL6_X86_64_RPMS.containsKey(rpmDirectory)
                                    && JdkUtil.isBuildDateKnown(
                                            JdkUtil.JDK8_RHEL6_X86_64_RPMS.get(rpmDirectory).getBuildDate())
                                    && getJdkBuildDate().compareTo(
                                            JdkUtil.JDK8_RHEL6_X86_64_RPMS.get(rpmDirectory).getBuildDate()) == 0)
                            || (JdkUtil.JDK8_RHEL7_X86_64_RPMS.containsKey(rpmDirectory)
                                    && JdkUtil.isBuildDateKnown(
                                            JdkUtil.JDK8_RHEL7_X86_64_RPMS.get(rpmDirectory).getBuildDate())
                                    && getJdkBuildDate().compareTo(
                                            JdkUtil.JDK8_RHEL7_X86_64_RPMS.get(rpmDirectory).getBuildDate()) == 0)
                            || (JdkUtil.JDK8_RHEL7_PPC64_RPMS.containsKey(rpmDirectory)
                                    && JdkUtil.isBuildDateKnown(
                                            JdkUtil.JDK8_RHEL7_PPC64_RPMS.get(rpmDirectory).getBuildDate())
                                    && getJdkBuildDate().compareTo(
                                            JdkUtil.JDK8_RHEL7_PPC64_RPMS.get(rpmDirectory).getBuildDate()) == 0)
                            || (JdkUtil.JDK8_RHEL7_PPC64LE_RPMS.containsKey(rpmDirectory)
                                    && JdkUtil.isBuildDateKnown(
                                            JdkUtil.JDK8_RHEL7_PPC64LE_RPMS.get(rpmDirectory).getBuildDate())
                                    && getJdkBuildDate().compareTo(
                                            JdkUtil.JDK8_RHEL7_PPC64LE_RPMS.get(rpmDirectory).getBuildDate()) == 0)
                            || (JdkUtil.JDK8_RHEL8_X86_64_RPMS.containsKey(rpmDirectory)
                                    && JdkUtil.isBuildDateKnown(
                                            JdkUtil.JDK8_RHEL8_X86_64_RPMS.get(rpmDirectory).getBuildDate())
                                    && getJdkBuildDate().compareTo(
                                            JdkUtil.JDK8_RHEL8_X86_64_RPMS.get(rpmDirectory).getBuildDate()) == 0)
                            || (JdkUtil.JDK8_RHEL9_X86_64_RPMS.containsKey(rpmDirectory)
                                    && JdkUtil.isBuildDateKnown(
                                            JdkUtil.JDK8_RHEL9_X86_64_RPMS.get(rpmDirectory).getBuildDate())
                                    && getJdkBuildDate().compareTo(
                                            JdkUtil.JDK8_RHEL9_X86_64_RPMS.get(rpmDirectory).getBuildDate()) == 0);
                    break;
                case JDK11:
                    isRhBuildDate = (JdkUtil.JDK11_RHEL_ZIPS.containsKey(releaseString)
                            && JdkUtil.isBuildDateKnown(JdkUtil.JDK11_RHEL_ZIPS.get(releaseString).getBuildDate())
                            && getJdkBuildDate()
                                    .compareTo(JdkUtil.JDK11_RHEL_ZIPS.get(releaseString).getBuildDate()) == 0)
                            || (JdkUtil.JDK11_RHEL7_X86_64_RPMS.containsKey(rpmDirectory)
                                    && JdkUtil.isBuildDateKnown(
                                            JdkUtil.JDK11_RHEL7_X86_64_RPMS.get(rpmDirectory).getBuildDate())
                                    && getJdkBuildDate().compareTo(
                                            JdkUtil.JDK11_RHEL7_X86_64_RPMS.get(rpmDirectory).getBuildDate()) == 0)
                            || (JdkUtil.JDK11_RHEL8_X86_64_RPMS.containsKey(rpmDirectory)
                                    && JdkUtil.isBuildDateKnown(
                                            JdkUtil.JDK11_RHEL8_X86_64_RPMS.get(rpmDirectory).getBuildDate())
                                    && getJdkBuildDate().compareTo(
                                            JdkUtil.JDK11_RHEL8_X86_64_RPMS.get(rpmDirectory).getBuildDate()) == 0)
                            || (JdkUtil.JDK11_RHEL9_X86_64_RPMS.containsKey(rpmDirectory)
                                    && JdkUtil.isBuildDateKnown(
                                            JdkUtil.JDK11_RHEL9_X86_64_RPMS.get(rpmDirectory).getBuildDate())
                                    && getJdkBuildDate().compareTo(
                                            JdkUtil.JDK11_RHEL9_X86_64_RPMS.get(rpmDirectory).getBuildDate()) == 0);
                    break;
                case JDK17:
                    isRhBuildDate = (JdkUtil.JDK17_RHEL_ZIPS.containsKey(releaseString)
                            && JdkUtil.isBuildDateKnown(JdkUtil.JDK17_RHEL_ZIPS.get(releaseString).getBuildDate())
                            && getJdkBuildDate()
                                    .compareTo(JdkUtil.JDK17_RHEL_ZIPS.get(releaseString).getBuildDate()) == 0)
                            || (JdkUtil.JDK17_RHEL8_X86_64_RPMS.containsKey(rpmDirectory)
                                    && JdkUtil.isBuildDateKnown(
                                            JdkUtil.JDK17_RHEL8_X86_64_RPMS.get(rpmDirectory).getBuildDate())
                                    && getJdkBuildDate().compareTo(
                                            JdkUtil.JDK17_RHEL8_X86_64_RPMS.get(rpmDirectory).getBuildDate()) == 0)
                            || (JdkUtil.JDK17_RHEL9_X86_64_RPMS.containsKey(rpmDirectory)
                                    && JdkUtil.isBuildDateKnown(
                                            JdkUtil.JDK17_RHEL9_X86_64_RPMS.get(rpmDirectory).getBuildDate())
                                    && getJdkBuildDate().compareTo(
                                            JdkUtil.JDK17_RHEL9_X86_64_RPMS.get(rpmDirectory).getBuildDate()) == 0);
                    break;
                case JDK6:
                case JDK7:
                case UNKNOWN:
                default:
                    break;
                ***REMOVED***
            ***REMOVED*** else if (getOs() == Os.WINDOWS) {
                switch (getJavaSpecification()) {
                case JDK8:
                    isRhBuildDate = JdkUtil.JDK8_WINDOWS_ZIPS.containsKey(releaseString)
                            && JdkUtil.isBuildDateKnown(JdkUtil.JDK8_WINDOWS_ZIPS.get(releaseString).getBuildDate())
                            && getJdkBuildDate()
                                    .compareTo(JdkUtil.JDK8_WINDOWS_ZIPS.get(releaseString).getBuildDate()) == 0;
                    break;
                case JDK11:
                    isRhBuildDate = JdkUtil.JDK11_WINDOWS_ZIPS.containsKey(releaseString)
                            && JdkUtil.isBuildDateKnown(JdkUtil.JDK11_WINDOWS_ZIPS.get(releaseString).getBuildDate())
                            && getJdkBuildDate()
                                    .compareTo(JdkUtil.JDK11_WINDOWS_ZIPS.get(releaseString).getBuildDate()) == 0;
                    break;
                case JDK17:
                    isRhBuildDate = JdkUtil.JDK17_WINDOWS_ZIPS.containsKey(releaseString)
                            && JdkUtil.isBuildDateKnown(JdkUtil.JDK17_WINDOWS_ZIPS.get(releaseString).getBuildDate())
                            && getJdkBuildDate()
                                    .compareTo(JdkUtil.JDK17_WINDOWS_ZIPS.get(releaseString).getBuildDate()) == 0;
                    break;
                case JDK6:
                case JDK7:
                case UNKNOWN:
                default:
                    break;
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        return isRhBuildDate;
    ***REMOVED***

    /**
     * @return true if the Red Hat build date/time for the JDK version is unknown (ends in "00:00:00"), false otherwise.
     */
    public boolean isRhBuildDateUnknown() {
        boolean isRhBuildDateUnknown = false;
        if (getJdkBuildDate() != null) {
            String releaseString = getJdkReleaseString();
            String rpmDirectory = getRpmDirectory();
            if (getOs() == Os.LINUX) {
                switch (getJavaSpecification()) {
                case JDK8:
                    isRhBuildDateUnknown = (JdkUtil.JDK8_RHEL_ZIPS.containsKey(releaseString)
                            && !JdkUtil.isBuildDateKnown(JdkUtil.JDK8_RHEL_ZIPS.get(releaseString).getBuildDate()))
                            || (JdkUtil.JDK8_RHEL6_X86_64_RPMS.containsKey(rpmDirectory) && !JdkUtil
                                    .isBuildDateKnown(JdkUtil.JDK8_RHEL6_X86_64_RPMS.get(rpmDirectory).getBuildDate()))
                            || (JdkUtil.JDK8_RHEL7_X86_64_RPMS.containsKey(rpmDirectory) && !JdkUtil
                                    .isBuildDateKnown(JdkUtil.JDK8_RHEL7_X86_64_RPMS.get(rpmDirectory).getBuildDate()))
                            || (JdkUtil.JDK8_RHEL7_PPC64_RPMS.containsKey(rpmDirectory) && !JdkUtil
                                    .isBuildDateKnown(JdkUtil.JDK8_RHEL7_PPC64_RPMS.get(rpmDirectory).getBuildDate()))
                            || (JdkUtil.JDK8_RHEL7_PPC64LE_RPMS.containsKey(rpmDirectory) && !JdkUtil
                                    .isBuildDateKnown(JdkUtil.JDK8_RHEL7_PPC64LE_RPMS.get(rpmDirectory).getBuildDate()))
                            || (JdkUtil.JDK8_RHEL8_X86_64_RPMS.containsKey(rpmDirectory) && !JdkUtil
                                    .isBuildDateKnown(JdkUtil.JDK8_RHEL8_X86_64_RPMS.get(rpmDirectory).getBuildDate()))
                            || (JdkUtil.JDK8_RHEL9_X86_64_RPMS.containsKey(rpmDirectory) && !JdkUtil
                                    .isBuildDateKnown(JdkUtil.JDK8_RHEL9_X86_64_RPMS.get(rpmDirectory).getBuildDate()));
                    break;
                case JDK11:
                    isRhBuildDateUnknown = (JdkUtil.JDK11_RHEL_ZIPS.containsKey(releaseString)
                            && !JdkUtil.isBuildDateKnown(JdkUtil.JDK11_RHEL_ZIPS.get(releaseString).getBuildDate()))
                            || (JdkUtil.JDK11_RHEL7_X86_64_RPMS.containsKey(rpmDirectory) && !JdkUtil
                                    .isBuildDateKnown(JdkUtil.JDK11_RHEL7_X86_64_RPMS.get(rpmDirectory).getBuildDate()))
                            || (JdkUtil.JDK11_RHEL8_X86_64_RPMS.containsKey(rpmDirectory) && !JdkUtil
                                    .isBuildDateKnown(JdkUtil.JDK11_RHEL8_X86_64_RPMS.get(rpmDirectory).getBuildDate()))
                            || (JdkUtil.JDK11_RHEL9_X86_64_RPMS.containsKey(rpmDirectory) && !JdkUtil.isBuildDateKnown(
                                    JdkUtil.JDK11_RHEL9_X86_64_RPMS.get(rpmDirectory).getBuildDate()));
                    break;
                case JDK17:
                    isRhBuildDateUnknown = (JdkUtil.JDK17_RHEL_ZIPS.containsKey(releaseString)
                            && !JdkUtil.isBuildDateKnown(JdkUtil.JDK17_RHEL_ZIPS.get(releaseString).getBuildDate()))
                            || (JdkUtil.JDK17_RHEL8_X86_64_RPMS.containsKey(rpmDirectory) && !JdkUtil
                                    .isBuildDateKnown(JdkUtil.JDK17_RHEL8_X86_64_RPMS.get(rpmDirectory).getBuildDate()))
                            || (JdkUtil.JDK17_RHEL9_X86_64_RPMS.containsKey(rpmDirectory) && !JdkUtil.isBuildDateKnown(
                                    JdkUtil.JDK17_RHEL9_X86_64_RPMS.get(rpmDirectory).getBuildDate()));
                    break;
                case JDK6:
                case JDK7:
                case UNKNOWN:
                default:
                    break;
                ***REMOVED***
            ***REMOVED*** else if (getOs() == Os.WINDOWS) {
                switch (getJavaSpecification()) {
                case JDK8:
                    isRhBuildDateUnknown = JdkUtil.JDK8_WINDOWS_ZIPS.containsKey(releaseString)
                            && !JdkUtil.isBuildDateKnown(JdkUtil.JDK8_WINDOWS_ZIPS.get(releaseString).getBuildDate());
                    break;
                case JDK11:
                    isRhBuildDateUnknown = JdkUtil.JDK11_WINDOWS_ZIPS.containsKey(releaseString)
                            && !JdkUtil.isBuildDateKnown(JdkUtil.JDK11_WINDOWS_ZIPS.get(releaseString).getBuildDate());
                    break;
                case JDK17:
                    isRhBuildDateUnknown = JdkUtil.JDK17_WINDOWS_ZIPS.containsKey(releaseString)
                            && !JdkUtil.isBuildDateKnown(JdkUtil.JDK17_WINDOWS_ZIPS.get(releaseString).getBuildDate());
                    break;
                case JDK6:
                case JDK7:
                case UNKNOWN:
                default:
                    break;
                ***REMOVED***
            ***REMOVED***
        ***REMOVED*** else {
            isRhBuildDateUnknown = true;
        ***REMOVED***
        return isRhBuildDateUnknown;
    ***REMOVED***

    /**
     * @return true if the fatal error log was created by a RH build of OpenJDK, false otherwise.
     */
    public boolean isRhBuildOpenJdk() {
        return isRhRpmInstall() || isRhLinuxZipInstall() || isRhWindowsZipInstall() || isRhRpm();
    ***REMOVED***

    /**
     * @return true if the fatal error log was created by a JDK build string used by Red Hat, false otherwise.
     */
    public boolean isRhBuildString() {
        return vmInfo != null && (vmInfo.getBuiltBy() == BuiltBy.BUILD || vmInfo.getBuiltBy() == BuiltBy.EMPTY
                || vmInfo.getBuiltBy() == BuiltBy.MOCKBUILD);
    ***REMOVED***

    /**
     * @return true if the fatal error log was created on RHEL, false otherwise.
     */
    public boolean isRhel() {
        boolean isRhel = false;
        if (!osInfos.isEmpty()) {
            Iterator<OsInfo> iterator = osInfos.iterator();
            while (iterator.hasNext()) {
                OsInfo event = iterator.next();
                if (event.isHeader()) {
                    if (event.getLogEntry().matches("^OS:$")) {
                        // OS string on next line
                        event = iterator.next();
                    ***REMOVED***
                    isRhel = event.getLogEntry().matches("^.*Red Hat Enterprise Linux.+$");
                    break;
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        return isRhel;
    ***REMOVED***

    /**
     * @return true if the JDK that produced the fatal error log is a Red Hat build of OpenJDK RHEL tarball, false
     *         otherwise.
     */
    public boolean isRhLinuxZipInstall() {
        boolean isRhLinuxZipInstall = false;
        if (getOs() == Os.LINUX && getArch() == Arch.X86_64) {
            switch (getJavaSpecification()) {
            case JDK8:
                isRhLinuxZipInstall = JdkUtil.JDK8_RHEL_ZIPS.containsKey(getJdkReleaseString())
                        && getJdkBuildDate() != null && getJdkBuildDate()
                                .compareTo(JdkUtil.JDK8_RHEL_ZIPS.get(getJdkReleaseString()).getBuildDate()) == 0;
                break;
            case JDK11:
                isRhLinuxZipInstall = JdkUtil.JDK11_RHEL_ZIPS.containsKey(getJdkReleaseString())
                        && getJdkBuildDate() != null && getJdkBuildDate() != null && getJdkBuildDate()
                                .compareTo(JdkUtil.JDK11_RHEL_ZIPS.get(getJdkReleaseString()).getBuildDate()) == 0;
                break;
            case JDK17:
                isRhLinuxZipInstall = JdkUtil.JDK17_RHEL_ZIPS.containsKey(getJdkReleaseString())
                        && getJdkBuildDate() != null && getJdkBuildDate() != null && getJdkBuildDate()
                                .compareTo(JdkUtil.JDK17_RHEL_ZIPS.get(getJdkReleaseString()).getBuildDate()) == 0;
                break;
            case JDK6:
            case JDK7:
            case UNKNOWN:
            default:
                break;
            ***REMOVED***
        ***REMOVED***
        return isRhLinuxZipInstall;
    ***REMOVED***

    /**
     * Check if the JDK that produced the fatal error log is a derivative of a Red Hat build of OpenJDK rpm (e.g.
     * created with jlink). The version and build date will match, but it will be installed in a different location than
     * an rpm install.
     * 
     * @return true if the JDK that produced the fatal error log is a Red Hat build of OpenJDK rpm, false otherwise.
     */
    public boolean isRhRpm() {
        boolean isRhelRpm = false;
        String jdkReleaseString = getJdkReleaseString();
        Iterator<Entry<String, Release>> iterator;
        Date jdkBuildDate = getJdkBuildDate();
        if (getJavaSpecification() == JavaSpecification.JDK8) {
            switch (getOsVersion()) {
            case CENTOS6:
            case RHEL6:
                iterator = JdkUtil.JDK8_RHEL6_X86_64_RPMS.entrySet().iterator();
                while (iterator.hasNext()) {
                    Entry<String, Release> entry = iterator.next();
                    Release release = entry.getValue();
                    if (release.getVersion().equals(jdkReleaseString) && release.getBuildDate() != null
                            && release.getBuildDate().compareTo(jdkBuildDate) == 0) {
                        isRhelRpm = true;
                        break;
                    ***REMOVED***
                ***REMOVED***
                break;
            case CENTOS7:
            case RHEL7:
                if (getArch() == Arch.X86_64) {
                    iterator = JdkUtil.JDK8_RHEL7_X86_64_RPMS.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Entry<String, Release> entry = iterator.next();
                        Release release = entry.getValue();
                        if (release.getVersion().equals(jdkReleaseString) && jdkBuildDate != null
                                && release.getBuildDate() != null
                                && release.getBuildDate().compareTo(jdkBuildDate) == 0) {
                            isRhelRpm = true;
                            break;
                        ***REMOVED***
                    ***REMOVED***
                ***REMOVED*** else if (getArch() == Arch.PPC64) {
                    iterator = JdkUtil.JDK8_RHEL7_PPC64_RPMS.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Entry<String, Release> entry = iterator.next();
                        Release release = entry.getValue();
                        if (release.getVersion().equals(jdkReleaseString) && release.getBuildDate() != null
                                && release.getBuildDate().compareTo(jdkBuildDate) == 0) {
                            isRhelRpm = true;
                            break;
                        ***REMOVED***
                    ***REMOVED***
                ***REMOVED*** else if (getArch() == Arch.PPC64LE) {
                    iterator = JdkUtil.JDK8_RHEL7_PPC64LE_RPMS.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Entry<String, Release> entry = iterator.next();
                        Release release = entry.getValue();
                        if (release.getVersion().equals(jdkReleaseString) && release.getBuildDate() != null
                                && release.getBuildDate().compareTo(jdkBuildDate) == 0) {
                            isRhelRpm = true;
                            break;
                        ***REMOVED***
                    ***REMOVED***
                ***REMOVED***
                break;
            case CENTOS8:
            case RHEL8:
                if (getArch() == Arch.X86_64) {
                    iterator = JdkUtil.JDK8_RHEL8_X86_64_RPMS.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Entry<String, Release> entry = iterator.next();
                        Release release = entry.getValue();
                        if (release.getVersion().equals(jdkReleaseString) && release.getBuildDate() != null
                                && release.getBuildDate().compareTo(jdkBuildDate) == 0) {
                            isRhelRpm = true;
                            break;
                        ***REMOVED***
                    ***REMOVED***
                ***REMOVED*** else if (getArch() == Arch.PPC64LE) {
                    iterator = JdkUtil.JDK8_RHEL8_PPC64LE_RPMS.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Entry<String, Release> entry = iterator.next();
                        Release release = entry.getValue();
                        if (release.getVersion().equals(jdkReleaseString) && release.getBuildDate() != null
                                && release.getBuildDate().compareTo(jdkBuildDate) == 0) {
                            isRhelRpm = true;
                            break;
                        ***REMOVED***
                    ***REMOVED***
                ***REMOVED***
                break;
            case CENTOS9:
            case RHEL9:
                iterator = JdkUtil.JDK8_RHEL9_X86_64_RPMS.entrySet().iterator();
                while (iterator.hasNext()) {
                    Entry<String, Release> entry = iterator.next();
                    Release release = entry.getValue();
                    if (release.getVersion().equals(jdkReleaseString) && release.getBuildDate() != null
                            && release.getBuildDate().compareTo(jdkBuildDate) == 0) {
                        isRhelRpm = true;
                        break;
                    ***REMOVED***
                ***REMOVED***
                break;
            case UNIDENTIFIED:
            default:
                break;
            ***REMOVED***
        ***REMOVED*** else if (getJavaSpecification() == JavaSpecification.JDK11) {
            switch (getOsVersion()) {
            case CENTOS7:
            case RHEL7:
                iterator = JdkUtil.JDK11_RHEL7_X86_64_RPMS.entrySet().iterator();
                while (iterator.hasNext()) {
                    Entry<String, Release> entry = iterator.next();
                    Release release = entry.getValue();
                    if (release.getVersion().equals(jdkReleaseString) && release.getBuildDate() != null
                            && release.getBuildDate().compareTo(jdkBuildDate) == 0) {
                        isRhelRpm = true;
                        break;
                    ***REMOVED***
                ***REMOVED***
                break;
            case CENTOS8:
            case RHEL8:
                iterator = JdkUtil.JDK11_RHEL8_X86_64_RPMS.entrySet().iterator();
                while (iterator.hasNext()) {
                    Entry<String, Release> entry = iterator.next();
                    Release release = entry.getValue();
                    if (release.getVersion().equals(jdkReleaseString) && release.getBuildDate() != null
                            && release.getBuildDate().compareTo(jdkBuildDate) == 0) {
                        isRhelRpm = true;
                        break;
                    ***REMOVED***
                ***REMOVED***
                break;
            case RHEL9:
                iterator = JdkUtil.JDK11_RHEL9_X86_64_RPMS.entrySet().iterator();
                while (iterator.hasNext()) {
                    Entry<String, Release> entry = iterator.next();
                    Release release = entry.getValue();
                    if (release.getVersion().equals(jdkReleaseString) && release.getBuildDate() != null
                            && release.getBuildDate().compareTo(jdkBuildDate) == 0) {
                        isRhelRpm = true;
                        break;
                    ***REMOVED***
                ***REMOVED***
                break;
            case CENTOS6:
            case RHEL6:
            case UNIDENTIFIED:
            default:
                break;
            ***REMOVED***
        ***REMOVED*** else if (getJavaSpecification() == JavaSpecification.JDK17) {
            switch (getOsVersion()) {
            case CENTOS8:
            case RHEL8:
                iterator = JdkUtil.JDK17_RHEL8_X86_64_RPMS.entrySet().iterator();
                while (iterator.hasNext()) {
                    Entry<String, Release> entry = iterator.next();
                    Release release = entry.getValue();
                    if (release.getVersion().equals(jdkReleaseString) && release.getBuildDate() != null
                            && release.getBuildDate().compareTo(jdkBuildDate) == 0) {
                        isRhelRpm = true;
                        break;
                    ***REMOVED***
                ***REMOVED***
                break;
            case RHEL9:
                iterator = JdkUtil.JDK17_RHEL9_X86_64_RPMS.entrySet().iterator();
                while (iterator.hasNext()) {
                    Entry<String, Release> entry = iterator.next();
                    Release release = entry.getValue();
                    if (release.getVersion().equals(jdkReleaseString) && release.getBuildDate() != null
                            && release.getBuildDate().compareTo(jdkBuildDate) == 0) {
                        isRhelRpm = true;
                        break;
                    ***REMOVED***
                ***REMOVED***
                break;
            case CENTOS6:
            case RHEL6:
            case CENTOS7:
            case RHEL7:
            case UNIDENTIFIED:
            default:
                break;
            ***REMOVED***
        ***REMOVED***
        return isRhelRpm;
    ***REMOVED***

    /**
     * @return true if the JDK that produced the fatal error log is a Red Hat build of OpenJDK rpm install, false
     *         otherwise.
     */
    public boolean isRhRpmInstall() {
        boolean isRhelRpmInstall = false;
        String rpmDirectory = getRpmDirectory();
        if (rpmDirectory != null) {
            if (getJavaSpecification() == JavaSpecification.JDK8) {
                switch (getOsVersion()) {
                case CENTOS6:
                case RHEL6:
                    isRhelRpmInstall = JdkUtil.JDK8_RHEL6_X86_64_RPMS.containsKey(rpmDirectory)
                            && getJdkBuildDate() != null && getJdkBuildDate()
                                    .compareTo(JdkUtil.JDK8_RHEL6_X86_64_RPMS.get(rpmDirectory).getBuildDate()) == 0;
                    break;
                case CENTOS7:
                case RHEL7:
                    if (getArch() == Arch.X86_64) {
                        isRhelRpmInstall = JdkUtil.JDK8_RHEL7_X86_64_RPMS.containsKey(rpmDirectory)
                                && getJdkBuildDate() != null && getJdkBuildDate().compareTo(
                                        JdkUtil.JDK8_RHEL7_X86_64_RPMS.get(rpmDirectory).getBuildDate()) == 0;
                    ***REMOVED*** else if (getArch() == Arch.PPC64) {
                        isRhelRpmInstall = JdkUtil.JDK8_RHEL7_PPC64_RPMS.containsKey(rpmDirectory)
                                && getJdkBuildDate() != null && getJdkBuildDate()
                                        .compareTo(JdkUtil.JDK8_RHEL7_PPC64_RPMS.get(rpmDirectory).getBuildDate()) == 0;
                    ***REMOVED*** else if (getArch() == Arch.PPC64LE) {
                        isRhelRpmInstall = JdkUtil.JDK8_RHEL7_PPC64LE_RPMS.containsKey(rpmDirectory)
                                && getJdkBuildDate() != null && getJdkBuildDate().compareTo(
                                        JdkUtil.JDK8_RHEL7_PPC64LE_RPMS.get(rpmDirectory).getBuildDate()) == 0;
                    ***REMOVED***
                    break;
                case CENTOS8:
                case RHEL8:
                    if (getArch() == Arch.X86_64) {
                        isRhelRpmInstall = JdkUtil.JDK8_RHEL8_X86_64_RPMS.containsKey(rpmDirectory)
                                && getJdkBuildDate() != null && getJdkBuildDate().compareTo(
                                        JdkUtil.JDK8_RHEL8_X86_64_RPMS.get(rpmDirectory).getBuildDate()) == 0;
                    ***REMOVED*** else if (getArch() == Arch.PPC64LE) {
                        isRhelRpmInstall = JdkUtil.JDK8_RHEL8_PPC64LE_RPMS.containsKey(rpmDirectory)
                                && getJdkBuildDate() != null && getJdkBuildDate().compareTo(
                                        JdkUtil.JDK8_RHEL8_PPC64LE_RPMS.get(rpmDirectory).getBuildDate()) == 0;
                    ***REMOVED***
                    break;
                case CENTOS9:
                case RHEL9:
                    isRhelRpmInstall = JdkUtil.JDK8_RHEL9_X86_64_RPMS.containsKey(rpmDirectory)
                            && getJdkBuildDate() != null && getJdkBuildDate()
                                    .compareTo(JdkUtil.JDK8_RHEL9_X86_64_RPMS.get(rpmDirectory).getBuildDate()) == 0;
                    break;
                case UNIDENTIFIED:
                default:
                    break;
                ***REMOVED***
            ***REMOVED*** else if (getJavaSpecification() == JavaSpecification.JDK11) {
                switch (getOsVersion()) {
                case CENTOS7:
                case RHEL7:
                    isRhelRpmInstall = JdkUtil.JDK11_RHEL7_X86_64_RPMS.containsKey(rpmDirectory)
                            && getJdkBuildDate() != null && getJdkBuildDate()
                                    .compareTo(JdkUtil.JDK11_RHEL7_X86_64_RPMS.get(rpmDirectory).getBuildDate()) == 0;
                    break;
                case CENTOS8:
                case RHEL8:
                    isRhelRpmInstall = JdkUtil.JDK11_RHEL8_X86_64_RPMS.containsKey(rpmDirectory)
                            && getJdkBuildDate() != null && getJdkBuildDate()
                                    .compareTo(JdkUtil.JDK11_RHEL8_X86_64_RPMS.get(rpmDirectory).getBuildDate()) == 0;
                    break;
                case RHEL9:
                    isRhelRpmInstall = JdkUtil.JDK11_RHEL9_X86_64_RPMS.containsKey(rpmDirectory)
                            && getJdkBuildDate() != null && getJdkBuildDate()
                                    .compareTo(JdkUtil.JDK11_RHEL9_X86_64_RPMS.get(rpmDirectory).getBuildDate()) == 0;
                    break;
                case CENTOS6:
                case RHEL6:
                case UNIDENTIFIED:
                default:
                    break;
                ***REMOVED***
            ***REMOVED*** else if (getJavaSpecification() == JavaSpecification.JDK17) {
                switch (getOsVersion()) {
                case CENTOS8:
                case RHEL8:
                    isRhelRpmInstall = JdkUtil.JDK17_RHEL8_X86_64_RPMS.containsKey(rpmDirectory)
                            && getJdkBuildDate() != null && getJdkBuildDate()
                                    .compareTo(JdkUtil.JDK17_RHEL8_X86_64_RPMS.get(rpmDirectory).getBuildDate()) == 0;
                    break;
                case RHEL9:
                    isRhelRpmInstall = JdkUtil.JDK17_RHEL9_X86_64_RPMS.containsKey(rpmDirectory)
                            && getJdkBuildDate() != null && getJdkBuildDate()
                                    .compareTo(JdkUtil.JDK17_RHEL9_X86_64_RPMS.get(rpmDirectory).getBuildDate()) == 0;
                    break;
                case CENTOS6:
                case RHEL6:
                case CENTOS7:
                case RHEL7:
                case UNIDENTIFIED:
                default:
                    break;
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        return isRhelRpmInstall;
    ***REMOVED***

    /**
     * @return true if the version matches a Red Hat build of OpenJDK, false otherwise.
     */
    public boolean isRhVersion() {
        boolean isRhVersion = false;
        if (getOs() == Os.LINUX) {
            switch (getJavaSpecification()) {
            case JDK8:
                isRhVersion = JdkUtil.JDK8_RHEL_ZIPS.containsKey(getJdkReleaseString())
                        || JdkUtil.isReleaseStringInReleases(getJdkReleaseString(), JdkUtil.JDK8_RHEL6_X86_64_RPMS)
                        || JdkUtil.isReleaseStringInReleases(getJdkReleaseString(), JdkUtil.JDK8_RHEL7_X86_64_RPMS)
                        || JdkUtil.isReleaseStringInReleases(getJdkReleaseString(), JdkUtil.JDK8_RHEL8_X86_64_RPMS)
                        || JdkUtil.isReleaseStringInReleases(getJdkReleaseString(), JdkUtil.JDK8_RHEL9_X86_64_RPMS);
                break;
            case JDK11:
                isRhVersion = JdkUtil.JDK11_RHEL_ZIPS.containsKey(getJdkReleaseString())
                        || JdkUtil.isReleaseStringInReleases(getJdkReleaseString(), JdkUtil.JDK11_RHEL7_X86_64_RPMS)
                        || JdkUtil.isReleaseStringInReleases(getJdkReleaseString(), JdkUtil.JDK11_RHEL8_X86_64_RPMS)
                        || JdkUtil.isReleaseStringInReleases(getJdkReleaseString(), JdkUtil.JDK11_RHEL9_X86_64_RPMS);
                break;
            case JDK17:
                isRhVersion = JdkUtil.JDK17_RHEL_ZIPS.containsKey(getJdkReleaseString())
                        || JdkUtil.isReleaseStringInReleases(getJdkReleaseString(), JdkUtil.JDK17_RHEL8_X86_64_RPMS)
                        || JdkUtil.isReleaseStringInReleases(getJdkReleaseString(), JdkUtil.JDK17_RHEL9_X86_64_RPMS);
                break;
            case JDK6:
            case JDK7:
            case UNKNOWN:
            default:
                break;
            ***REMOVED***
        ***REMOVED*** else if (getOs() == Os.WINDOWS) {
            switch (getJavaSpecification()) {
            case JDK8:
                isRhVersion = JdkUtil.JDK8_WINDOWS_ZIPS.containsKey(getJdkReleaseString());
                break;
            case JDK11:
                isRhVersion = JdkUtil.JDK11_WINDOWS_ZIPS.containsKey(getJdkReleaseString());
                break;
            case JDK17:
                isRhVersion = JdkUtil.JDK17_WINDOWS_ZIPS.containsKey(getJdkReleaseString());
                break;
            case JDK6:
            case JDK7:
            case UNKNOWN:
            default:
                break;
            ***REMOVED***
        ***REMOVED***
        return isRhVersion;
    ***REMOVED***

    /**
     * @return true if the JDK that produced the fatal error log is a Red Hat build of OpenJDK Windows zip install,
     *         false otherwise.
     */
    public boolean isRhWindowsZipInstall() {
        boolean isRhWindowsZipInstall = false;
        if (isWindows() && getArch() == Arch.X86_64) {
            switch (getJavaSpecification()) {
            case JDK8:
                isRhWindowsZipInstall = JdkUtil.JDK8_WINDOWS_ZIPS.containsKey(getJdkReleaseString())
                        && getJdkBuildDate() != null && getJdkBuildDate()
                                .compareTo(JdkUtil.JDK8_WINDOWS_ZIPS.get(getJdkReleaseString()).getBuildDate()) == 0;
                break;
            case JDK11:
                isRhWindowsZipInstall = JdkUtil.JDK11_WINDOWS_ZIPS.containsKey(getJdkReleaseString())
                        && getJdkBuildDate() != null && getJdkBuildDate()
                                .compareTo(JdkUtil.JDK11_WINDOWS_ZIPS.get(getJdkReleaseString()).getBuildDate()) == 0;
                break;
            case JDK17:
                isRhWindowsZipInstall = JdkUtil.JDK17_WINDOWS_ZIPS.containsKey(getJdkReleaseString())
                        && getJdkBuildDate() != null && getJdkBuildDate()
                                .compareTo(JdkUtil.JDK17_WINDOWS_ZIPS.get(getJdkReleaseString()).getBuildDate()) == 0;
                break;
            case JDK6:
            case JDK7:
            case UNKNOWN:
            default:
                break;
            ***REMOVED***
        ***REMOVED***
        return isRhWindowsZipInstall;
    ***REMOVED***

    /**
     * @return true if the JDK that produced the fatal error log is a Red Hat build of OpenJDK rpm install, false
     *         otherwise.
     */
    public boolean isRpmInstall() {
        boolean isRhelRpmInstall = false;
        String rpmDirectory = getRpmDirectory();
        if (rpmDirectory != null) {
            if (getJavaSpecification() == JavaSpecification.JDK8) {
                switch (getOsVersion()) {
                case CENTOS6:
                case RHEL6:
                    isRhelRpmInstall = JdkUtil.JDK8_RHEL6_X86_64_RPMS.containsKey(rpmDirectory)
                            && getJdkBuildDate() != null && getJdkBuildDate()
                                    .compareTo(JdkUtil.JDK8_RHEL6_X86_64_RPMS.get(rpmDirectory).getBuildDate()) == 0;
                    break;
                case CENTOS7:
                case RHEL7:
                    if (getArch() == Arch.X86_64) {
                        isRhelRpmInstall = JdkUtil.JDK8_RHEL7_X86_64_RPMS.containsKey(rpmDirectory)
                                && getJdkBuildDate() != null && getJdkBuildDate().compareTo(
                                        JdkUtil.JDK8_RHEL7_X86_64_RPMS.get(rpmDirectory).getBuildDate()) == 0;
                    ***REMOVED*** else if (getArch() == Arch.PPC64) {
                        isRhelRpmInstall = JdkUtil.JDK8_RHEL7_PPC64_RPMS.containsKey(rpmDirectory)
                                && getJdkBuildDate() != null && getJdkBuildDate()
                                        .compareTo(JdkUtil.JDK8_RHEL7_PPC64_RPMS.get(rpmDirectory).getBuildDate()) == 0;
                    ***REMOVED*** else if (getArch() == Arch.PPC64LE) {
                        isRhelRpmInstall = JdkUtil.JDK8_RHEL7_PPC64LE_RPMS.containsKey(rpmDirectory)
                                && getJdkBuildDate() != null && getJdkBuildDate().compareTo(
                                        JdkUtil.JDK8_RHEL7_PPC64LE_RPMS.get(rpmDirectory).getBuildDate()) == 0;
                    ***REMOVED***
                    break;
                case CENTOS8:
                case RHEL8:
                    if (getArch() == Arch.X86_64) {
                        isRhelRpmInstall = JdkUtil.JDK8_RHEL8_X86_64_RPMS.containsKey(rpmDirectory)
                                && getJdkBuildDate() != null && getJdkBuildDate().compareTo(
                                        JdkUtil.JDK8_RHEL8_X86_64_RPMS.get(rpmDirectory).getBuildDate()) == 0;
                    ***REMOVED*** else if (getArch() == Arch.PPC64LE) {
                        isRhelRpmInstall = JdkUtil.JDK8_RHEL8_PPC64LE_RPMS.containsKey(rpmDirectory)
                                && getJdkBuildDate() != null && getJdkBuildDate().compareTo(
                                        JdkUtil.JDK8_RHEL8_PPC64LE_RPMS.get(rpmDirectory).getBuildDate()) == 0;
                    ***REMOVED***
                    break;
                case CENTOS9:
                case RHEL9:
                    isRhelRpmInstall = JdkUtil.JDK8_RHEL9_X86_64_RPMS.containsKey(rpmDirectory)
                            && getJdkBuildDate() != null && getJdkBuildDate()
                                    .compareTo(JdkUtil.JDK8_RHEL9_X86_64_RPMS.get(rpmDirectory).getBuildDate()) == 0;
                    break;
                case UNIDENTIFIED:
                default:
                    break;
                ***REMOVED***
            ***REMOVED*** else if (getJavaSpecification() == JavaSpecification.JDK11) {
                switch (getOsVersion()) {
                case CENTOS7:
                case RHEL7:
                    isRhelRpmInstall = JdkUtil.JDK11_RHEL7_X86_64_RPMS.containsKey(rpmDirectory)
                            && getJdkBuildDate() != null && getJdkBuildDate()
                                    .compareTo(JdkUtil.JDK11_RHEL7_X86_64_RPMS.get(rpmDirectory).getBuildDate()) == 0;
                    break;
                case CENTOS8:
                case RHEL8:
                    isRhelRpmInstall = JdkUtil.JDK11_RHEL8_X86_64_RPMS.containsKey(rpmDirectory)
                            && getJdkBuildDate() != null && getJdkBuildDate()
                                    .compareTo(JdkUtil.JDK11_RHEL8_X86_64_RPMS.get(rpmDirectory).getBuildDate()) == 0;
                    break;
                case RHEL9:
                    isRhelRpmInstall = JdkUtil.JDK11_RHEL9_X86_64_RPMS.containsKey(rpmDirectory)
                            && getJdkBuildDate() != null && getJdkBuildDate()
                                    .compareTo(JdkUtil.JDK11_RHEL9_X86_64_RPMS.get(rpmDirectory).getBuildDate()) == 0;
                    break;
                case CENTOS6:
                case RHEL6:
                case UNIDENTIFIED:
                default:
                    break;
                ***REMOVED***
            ***REMOVED*** else if (getJavaSpecification() == JavaSpecification.JDK17) {
                switch (getOsVersion()) {
                case CENTOS8:
                case RHEL8:
                    isRhelRpmInstall = JdkUtil.JDK17_RHEL8_X86_64_RPMS.containsKey(rpmDirectory)
                            && getJdkBuildDate() != null && getJdkBuildDate()
                                    .compareTo(JdkUtil.JDK17_RHEL8_X86_64_RPMS.get(rpmDirectory).getBuildDate()) == 0;
                    break;
                case RHEL9:
                    isRhelRpmInstall = JdkUtil.JDK17_RHEL9_X86_64_RPMS.containsKey(rpmDirectory)
                            && getJdkBuildDate() != null && getJdkBuildDate()
                                    .compareTo(JdkUtil.JDK17_RHEL9_X86_64_RPMS.get(rpmDirectory).getBuildDate()) == 0;
                    break;
                case CENTOS6:
                case RHEL6:
                case CENTOS7:
                case RHEL7:
                case UNIDENTIFIED:
                default:
                    break;
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        return isRhelRpmInstall;
    ***REMOVED***

    /**
     * @return true if the fatal error is truncated, false otherwise.
     */
    public boolean isTruncated() {
        boolean isTruncated = false;
        if (vmInfo == null) {
            isTruncated = true;
        ***REMOVED***
        return isTruncated;
    ***REMOVED***

    /**
     * @return true if VMWare environment, false otherwise.
     */
    public boolean isVMWareEnvironment() {
        boolean isVMWareEnvironment = false;
        if (!virtualizationInfos.isEmpty()) {
            Iterator<VirtualizationInfo> iterator = virtualizationInfos.iterator();
            while (iterator.hasNext()) {
                VirtualizationInfo event = iterator.next();
                if (event.getLogEntry().matches("^VMWare virtualization detected$")) {
                    isVMWareEnvironment = true;
                    break;
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        return isVMWareEnvironment;
    ***REMOVED***

    /**
     * @return true if the fatal error log was created on Windows, false otherwise.
     */
    public boolean isWindows() {
        boolean isWindows = false;
        if (!osInfos.isEmpty()) {
            Iterator<OsInfo> iterator = osInfos.iterator();
            while (iterator.hasNext()) {
                OsInfo event = iterator.next();
                if (event.isHeader()) {
                    if (event.getLogEntry().matches("^OS:$")) {
                        // OS string on next line
                        event = iterator.next();
                    ***REMOVED***
                    isWindows = event.getLogEntry().matches("^OS: Windows.+$");
                    break;
                ***REMOVED***
            ***REMOVED***
        ***REMOVED***
        return isWindows;
    ***REMOVED***

    /**
     * Convenience method to remove <code>Analysis</code>.
     * 
     * @param key
     *            The <code>Analysis</code> to check.
     */
    public void removeAnalysis(Analysis key) {
        analysis.remove(key);
    ***REMOVED***

    public void setCommandLine(CommandLine commandLine) {
        this.commandLine = commandLine;
    ***REMOVED***

    public void setCompressedClassSpaceEvent(CompressedClassSpace compressedClassSpaceEvent) {
        this.compressedClassSpaceEvent = compressedClassSpaceEvent;
    ***REMOVED***

    public void setCurrentThread(CurrentThread currentThread) {
        this.currentThread = currentThread;
    ***REMOVED***

    public void setElapsedTime(ElapsedTime elapsedTime) {
        this.elapsedTime = elapsedTime;
    ***REMOVED***

    public void setHeapAddress(HeapAddress heapAddress) {
        this.heapAddress = heapAddress;
    ***REMOVED***

    public void setHost(Host host) {
        this.host = host;
    ***REMOVED***

    public void setMaxMapCount(MaxMapCount maxMapCount) {
        this.maxMapCount = maxMapCount;
    ***REMOVED***

    public void setNarrowKlass(NarrowKlass narrowKlass) {
        this.narrowKlass = narrowKlass;
    ***REMOVED***

    public void setPidMax(PidMax pidMax) {
        this.pidMax = pidMax;
    ***REMOVED***

    public void setRlimit(Rlimit rlimit) {
        this.rlimit = rlimit;
    ***REMOVED***

    public void setSigInfo(SigInfo sigInfo) {
        this.sigInfo = sigInfo;
    ***REMOVED***

    public void setThreadsMax(ThreadsMax threadsMax) {
        this.threadsMax = threadsMax;
    ***REMOVED***

    public void setTime(Time time) {
        this.time = time;
    ***REMOVED***

    public void setTimeElapsedTime(TimeElapsedTime timeElapsedTime) {
        this.timeElapsedTime = timeElapsedTime;
    ***REMOVED***

    public void setTimezone(Timezone timezone) {
        this.timezone = timezone;
    ***REMOVED***

    public void setUname(Uname uname) {
        this.uname = uname;
    ***REMOVED***

    public void setUnidentifiedLogLines(List<String> unidentifiedLogLines) {
        this.unidentifiedLogLines = unidentifiedLogLines;
    ***REMOVED***

    public void setVmInfo(VmInfo vmInfo) {
        this.vmInfo = vmInfo;
    ***REMOVED***

    public void setVmOperation(VmOperation vmOperation) {
        this.vmOperation = vmOperation;
    ***REMOVED***

    public void setVmState(VmState vmState) {
        this.vmState = vmState;
    ***REMOVED***
***REMOVED***