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
package org.github.krashpad.util.jdk;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.github.joa.domain.Arch;
import org.github.joa.util.Constants;
import org.github.krashpad.domain.BlankLine;
import org.github.krashpad.domain.LogEvent;
import org.github.krashpad.domain.UnknownEvent;
import org.github.krashpad.domain.jdk.ActiveLocale;
import org.github.krashpad.domain.jdk.BitsEvent;
import org.github.krashpad.domain.jdk.CardTable;
import org.github.krashpad.domain.jdk.CdsArchive;
import org.github.krashpad.domain.jdk.ClassesLoadedEvent;
import org.github.krashpad.domain.jdk.ClassesRedefinedEvent;
import org.github.krashpad.domain.jdk.ClassesUnloadedEvent;
import org.github.krashpad.domain.jdk.CodeCache;
import org.github.krashpad.domain.jdk.CommandLine;
import org.github.krashpad.domain.jdk.CompilationEvent;
import org.github.krashpad.domain.jdk.CompressedClassSpace;
import org.github.krashpad.domain.jdk.ConstantPool;
import org.github.krashpad.domain.jdk.ContainerInfo;
import org.github.krashpad.domain.jdk.CpuInfo;
import org.github.krashpad.domain.jdk.CurrentCompileTask;
import org.github.krashpad.domain.jdk.CurrentThread;
import org.github.krashpad.domain.jdk.DeoptimizationEvent;
import org.github.krashpad.domain.jdk.DllOperationEvent;
import org.github.krashpad.domain.jdk.DynamicLibrary;
import org.github.krashpad.domain.jdk.ElapsedTime;
import org.github.krashpad.domain.jdk.End;
import org.github.krashpad.domain.jdk.EnvironmentVariable;
import org.github.krashpad.domain.jdk.Event;
import org.github.krashpad.domain.jdk.ExceptionCounts;
import org.github.krashpad.domain.jdk.FatalErrorLog;
import org.github.krashpad.domain.jdk.GcHeapHistoryEvent;
import org.github.krashpad.domain.jdk.GcPreciousLog;
import org.github.krashpad.domain.jdk.GlobalFlag;
import org.github.krashpad.domain.jdk.Header;
import org.github.krashpad.domain.jdk.Heading;
import org.github.krashpad.domain.jdk.Heap;
import org.github.krashpad.domain.jdk.HeapAddress;
import org.github.krashpad.domain.jdk.HeapRegions;
import org.github.krashpad.domain.jdk.Host;
import org.github.krashpad.domain.jdk.Instructions;
import org.github.krashpad.domain.jdk.InternalExceptionEvent;
import org.github.krashpad.domain.jdk.InternalStatistic;
import org.github.krashpad.domain.jdk.LdPreloadFile;
import org.github.krashpad.domain.jdk.Libc;
import org.github.krashpad.domain.jdk.LoadAverage;
import org.github.krashpad.domain.jdk.Logging;
import org.github.krashpad.domain.jdk.MachCode;
import org.github.krashpad.domain.jdk.MaxMapCount;
import org.github.krashpad.domain.jdk.Meminfo;
import org.github.krashpad.domain.jdk.Memory;
import org.github.krashpad.domain.jdk.Metaspace;
import org.github.krashpad.domain.jdk.NarrowKlass;
import org.github.krashpad.domain.jdk.NativeDecoderState;
import org.github.krashpad.domain.jdk.NativeMemoryTracking;
import org.github.krashpad.domain.jdk.NumberEvent;
import org.github.krashpad.domain.jdk.OsInfo;
import org.github.krashpad.domain.jdk.OsUptime;
import org.github.krashpad.domain.jdk.PeriodicNativeTrim;
import org.github.krashpad.domain.jdk.PidMax;
import org.github.krashpad.domain.jdk.PollingPage;
import org.github.krashpad.domain.jdk.ProcessMemory;
import org.github.krashpad.domain.jdk.Register;
import org.github.krashpad.domain.jdk.RegisterToMemoryMapping;
import org.github.krashpad.domain.jdk.Release;
import org.github.krashpad.domain.jdk.Rlimit;
import org.github.krashpad.domain.jdk.SigInfo;
import org.github.krashpad.domain.jdk.SignalHandlers;
import org.github.krashpad.domain.jdk.Stack;
import org.github.krashpad.domain.jdk.StackSlotToMemoryMapping;
import org.github.krashpad.domain.jdk.Thread;
import org.github.krashpad.domain.jdk.ThreadsActiveCompile;
import org.github.krashpad.domain.jdk.ThreadsClassSmrInfo;
import org.github.krashpad.domain.jdk.ThreadsMax;
import org.github.krashpad.domain.jdk.Time;
import org.github.krashpad.domain.jdk.TimeElapsedTime;
import org.github.krashpad.domain.jdk.Timeout;
import org.github.krashpad.domain.jdk.Timezone;
import org.github.krashpad.domain.jdk.TopOfStack;
import org.github.krashpad.domain.jdk.TransparentHugepageDefrag;
import org.github.krashpad.domain.jdk.TransparentHugepageEnabled;
import org.github.krashpad.domain.jdk.Uid;
import org.github.krashpad.domain.jdk.Umask;
import org.github.krashpad.domain.jdk.Uname;
import org.github.krashpad.domain.jdk.VirtualizationInfo;
import org.github.krashpad.domain.jdk.VmArguments;
import org.github.krashpad.domain.jdk.VmInfo;
import org.github.krashpad.domain.jdk.VmMutex;
import org.github.krashpad.domain.jdk.VmOperation;
import org.github.krashpad.domain.jdk.VmOperationEvent;
import org.github.krashpad.domain.jdk.VmState;
import org.github.krashpad.domain.jdk.ZgcGlobals;
import org.github.krashpad.domain.jdk.ZgcMetadataBits;
import org.github.krashpad.domain.jdk.ZgcPageTable;
import org.github.krashpad.domain.jdk.ZgcPhaseSwitchEvent;

/**
 * <p>
 * Utility methods and constants.
 * </p>
 * 
 * @author <a href="mailto:mmillson@redhat.com">Mike Millson</a>
 * 
 */
public class JdkUtil {

    /**
     * Defined Java applications.
     */
    public enum Application {
        //
        AMQ,
        //
        AMQ_CLI,
        //
        CASSANDRA,
        //
        JBOSS_EAP6,
        //
        JBOSS_EAP7,
        //
        JBOSS_VERSION,
        // Java Enterprise User Solution, Korean web application server developed by TmaxSoft
        JEUS,
        //
        KAFKA,
        // Red Hat Certificate System, Red Hat Enterprise Linux (RHEL) Identity Management (IdM), or upstream Dogtag
        // Certificate System
        PKI_TOMCAT,
        //
        RHSSO,
        //
        SPRING_BOOT,
        //
        TOMCAT,
        //
        TOMCAT_SHUTDOWN,
        //
        UNKNOWN,
        /**
         * Upstream WildFly or an unidentified derivative (e.g. EAP, RHSSO, etc.).
         */
        WILDFLY
    }

    /**
     * Defined collector families.
     */
    public enum CollectorFamily {
        CMS, G1, PARALLEL, SERIAL, SHENANDOAH, UNKNOWN, Z
    }

    /**
     * Compressed ordinary object pointer (oop) modes used by the JVM to encode 64-bit addresses as 32-bit pointers to
     * save heap space for heaps &lt; 32G.
     * 
     * The JVM checks first checks if Heap + HeapBaseMinAddress can fit in the first 4G address space. If yes, 32-bit
     * (BIT32) mode is used.
     * 
     * If Heap + HeapBaseMinAddress cannot fit in the first 4G address space, the JVM checks if it can fit in the first
     * 32G address space. If yes zero-based (ZERO) mode is used.
     * 
     * If Heap + HeapBaseMinAddress cannot fit in the first 32G address space, and Heap &lt; 32G, non-zero mode
     * (NON-ZERO) is used.
     * 
     * BIT32: Heap mapped into the first 4G of virtual memory. Lower 32 bits of 64 bit address are used for the pointer
     * reference because higher order bits are all 0 (encoding/decoding not necessary).
     * 
     * NONE: 64-bit pointers are used (e.g. Heap &gt;= 32G).
     * 
     * NON_ZERO: Same as ZERO except Heap is mapped above the first 32G of virtual memory. Significant bits are stored
     * in the lowest bits, and encoding/decoding is done by bit shifting and added the heap base address.
     * 
     * ZERO: Heap mapped into the first 32G of virtual memory. Significant bits are stored in the lowest bits, and
     * encoding/decoding is done by bit shifting.
     */
    public enum CompressedOopMode {
        //
        BIT32, NON_ZERO, NONE, UNKNOWN, ZERO
    }

    /**
     * Defined Java specifications.
     */
    public enum JavaSpecification {
        JDK10, JDK11, JDK12, JDK13, JDK14, JDK15, JDK16, JDK17, JDK18, JDK19, JDK20, JDK21, JDK6, JDK7, JDK8, JDK9,
        //
        UNKNOWN
    }

    /**
     * Defined Java vendors.
     */
    public enum JavaVendor {
        ADOPTIUM, ADOPTOPENJDK, AZUL, MICROSOFT, NOT_RED_HAT, ORACLE, RED_HAT, UNIDENTIFIED
    }

    /**
     * Defined logging events.
     */
    public enum LogEventType {
        //
        ACTIVE_LOCALE, BITS, BLANK_LINE, CARD_TABLE, CDS_ARCHIVE, CLASSES_LOADED_EVENT, CLASSES_REDEFINED_EVENT,
        //
        CLASSES_UNLOADED_EVENT, CODE_CACHE, COMMAND_LINE, COMPILATION_EVENT, COMPRESSED_CLASS_SPACE, CONSTANT_POOL,
        //
        CONTAINER_INFO, CPU, CPU_INFO, CURRENT_COMPILE_TASK, CURRENT_THREAD, DEOPTIMIZATION_EVENT, DLL_OPERATION_EVENT,
        //
        DYNAMIC_LIBRARY, ELAPSED_TIME, END, ENVIRONMENT_VARIABLES, EVENT, EXCEPTION_COUNTS, GC_HEAP_HISTORY_EVENT,
        //
        GC_PRECIOUS_LOG, GLOBAL_FLAGS, HEADER, HEADING, HEAP, HEAP_ADDRESS, HEAP_REGIONS, HOST, INSTRUCTIONS, INTEGER,
        //
        INTERNAL_EXCEPTION_EVENT, INTERNAL_STATISTICS, LD_PRELOAD_FILE, LIBC, LOAD_AVERAGE, LOGGING, MACH_CODE,
        //
        MAX_MAP_COUNT, MEMINFO, MEMORY, METASPACE, NARROW_KLASS, NATIVE_DECODER_STATE, NATIVE_MEMORY_TRACKING, NUMBER,
        //
        OS_INFO, OS_UPTIME, PERIODIC_NATIVE_TRIM, PID_MAX, POLLING_PAGE, PROCESS_MEMORY, REGISTER,
        //
        REGISTER_TO_MEMORY_MAPPING, RLIMIT, SIGINFO, SIGNAL_HANDLERS, STACK, STACK_SLOT_TO_MEMORY_MAPPING, THREAD,
        //
        THREADS_ACTIVE_COMPILE, THREADS_CLASS_SMR_INFO, THREADS_MAX, TIME, TIME_ELAPSED_TIME, TIMEOUT, TIMEZONE,
        //
        TOP_OF_STACK, TRANSPARENT_HUGEPAGE_DEFRAG, TRANSPARENT_HUGEPAGE_ENABLED, UID, UMASK, UNAME, UNKNOWN,
        //
        VIRTUALIZATION_INFO, VM_ARGUMENTS, VM_INFO, VM_MUTEX, VM_OPERATION, VM_OPERATION_EVENT, VM_STATE, ZGC_GLOBALS,
        //
        ZGC_METADATA_BITS, ZGC_PAGE_TABLE, ZGC_PHASE_SWITCH_EVENT
    }

    /**
     * Signal codes.
     * 
     * BUS_ADRALN: The memory address that has an invalid address alignment for the CPU.
     * 
     * BUS_ADRERR: The memory address does not exist. Two causes: (1) Internal JVM corruption. (2) A memory mapped
     * (mmap) file has been deleted or truncated (e.g. a threading issue where 2 threads access a file at the same
     * time).
     * 
     * BUS_OBJERR: Hardware issue.
     * 
     * FPE_INTDIV: Floating point error (division by 0, modulo by 0, integer overflow).
     * 
     * SEGV_ACCERR: The access is not allowed due to permissions. For example: (1) Attempting to write to read-only
     * memory. (2) Attempting to write to protected (OS) memory. (3) Attempting to access an array at an index greater
     * than the array size (out of bounds).
     * 
     * SEGV_MAPERR: The memory address is not in the application address space. For example: (1) Dereferencing a null
     * pointer. (2) A corrupted pointer. (3) Stack overflow.
     * 
     * SI_KERNEL: Sent by the kernel.
     * 
     * SI_USER: A process or thread in the current process is raising the signal by calling kill.
     */
    public enum SignalCode {
        //
        BUS_ADRALN, BUS_ADRERR, BUS_OBJERR, FPE_INTDIV, ILL_ILLOPN, SEGV_ACCERR, SEGV_MAPERR, SI_KERNEL, SI_USER,
        //
        UNKNOWN
    }

    /**
     * Signal numbers.
     * 
     * EXCEPTION_STACK_OVERFLOW: Stack overflow.
     * 
     * SIGBUS: Invalid memory address.
     * 
     * SIGFPE: Floating point error (division by 0, modulo by 0, integer overflow).
     * 
     * SIGILL: Illegal instruction at the processor.
     * 
     * SIGSEGV, EXCEPTION_ACCESS_VIOLATION: Segmentation fault. Accessing valid memory in an invalid way.
     */
    public enum SignalNumber {
        //
        EXCEPTION_ACCESS_VIOLATION, EXCEPTION_STACK_OVERFLOW, SIGBUS, SIGFPE, SIGILL, SIGSEGV, UNKNOWN
    }

    /**
     * @param size
     *            The size in fromUnits.
     * @param fromUnits
     *            Current units.
     * @param toUnits
     *            Conversion units.
     * @return The size in toUnits.
     */
    public static long convertSize(final long size, char fromUnits, char toUnits) {
        if (fromUnits == toUnits) {
            return size;
        } else {
            if (!"bBkKmMgG".matches("^.*" + Character.toString(toUnits) + ".*$")) {
                throw new AssertionError("Unexpected toUnits value: " + toUnits);
            }
            BigDecimal newSize = new BigDecimal(size);
            switch (fromUnits) {
            case 'b':
            case 'B':
                if (toUnits == 'k' || toUnits == 'K') {
                    newSize = newSize.divide(Constants.KILOBYTE);
                } else if (toUnits == 'm' || toUnits == 'M') {
                    newSize = newSize.divide(Constants.MEGABYTE);
                } else if (toUnits == 'g' || toUnits == 'G') {
                    newSize = newSize.divide(Constants.GIGABYTE);
                }
                break;
            case 'k':
            case 'K':
                if (toUnits == 'b' || toUnits == 'B') {
                    newSize = newSize.multiply(Constants.KILOBYTE);
                } else if (toUnits == 'm' || toUnits == 'M') {
                    newSize = newSize.divide(Constants.KILOBYTE);
                } else if (toUnits == 'g' || toUnits == 'G') {
                    newSize = newSize.divide(Constants.MEGABYTE);
                }
                break;
            case 'm':
            case 'M':
                if (toUnits == 'b' || toUnits == 'B') {
                    newSize = newSize.multiply(Constants.MEGABYTE);
                } else if (toUnits == 'k' || toUnits == 'K') {
                    newSize = newSize.multiply(Constants.KILOBYTE);
                } else if (toUnits == 'g' || toUnits == 'G') {
                    newSize = newSize.divide(Constants.MEGABYTE);
                }
                break;
            case 'g':
            case 'G':
                if (toUnits == 'b' || toUnits == 'B') {
                    newSize = newSize.multiply(Constants.GIGABYTE);
                } else if (toUnits == 'k' || toUnits == 'K') {
                    newSize = newSize.multiply(Constants.MEGABYTE);
                } else if (toUnits == 'm' || toUnits == 'M') {
                    newSize = newSize.multiply(Constants.KILOBYTE);
                }
                break;
            default:
                throw new AssertionError("Unexpected fromUnits value: " + fromUnits);
            }
            newSize = newSize.setScale(0, RoundingMode.HALF_EVEN);
            return newSize.longValue();
        }
    }

    /**
     * Get the bytes of a JVM option that specifies a byte value. For example, the bytes for <code>128k</code> is 128 x
     * 1024 = 131,072.
     * 
     * @param optionValue
     *            The JVM option value.
     * @return The JVM option value in bytes, or <code>Long.MIN_VALUE</code> if the option does not exist
     */
    public static final long getByteOptionBytes(final String optionValue) {
        long bytes = Long.MIN_VALUE;
        if (optionValue != null) {
            char fromUnits;
            long value;
            Pattern pattern = Pattern.compile(JdkRegEx.OPTION_SIZE_BYTES);
            Matcher matcher = pattern.matcher(optionValue);
            if (matcher.find()) {
                value = Long.parseLong(matcher.group(2));
                if (matcher.group(3) != null) {
                    fromUnits = matcher.group(3).charAt(0);
                } else {
                    fromUnits = 'B';
                }
                char toUnits = 'B';
                if (fromUnits == toUnits) {
                    bytes = value;
                } else {
                    bytes = JdkUtil.convertSize(value, fromUnits, toUnits);
                }
            }
        }
        return bytes;
    }

    /**
     * Get the value of a JVM option that specifies a byte value. For example, the value for <code>-Xss128k</code> is
     * 128k. The value for <code>-XX:PermSize=128M</code> is 128M.
     * 
     * @param option
     *            The JVM option.
     * @return The JVM option value, or null if the option does not exist.
     */
    public static final String getByteOptionValue(final String option) {
        String value = null;
        if (option != null) {
            String regex = "^-[a-zA-Z:.]+={0,1}(" + JdkRegEx.OPTION_SIZE_BYTES + ")$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(option);
            if (matcher.find()) {
                value = matcher.group(1);
            }
        }
        return value;
    }

    /**
     * @param jdkReleaseString
     *            The JDK release string. For example: "11.0.15+10-LTS".
     * @param releases
     *            The JDK releases for a given OS and distribution method (e.g. zip, rpm).
     * @return The first release that matches the build string, or null if none found.
     */
    public static final Release getFirstReleaseFromReleases(String jdkReleaseString,
            HashMap<String, Release> releases) {
        Release firstRelease = null;
        Iterator<Entry<String, Release>> i = releases.entrySet().iterator();
        while (i.hasNext()) {
            Entry<String, Release> entry = i.next();
            Release release = entry.getValue();
            if (release.getVersion().equals(jdkReleaseString)) {
                firstRelease = release;
                break;
            }
        }
        return firstRelease;
    }

    /**
     * @param version
     *            The JDK version.
     * @return The Java specification as a release number that can be used for comparing release order.
     */
    public static final int getJavaSpecificationNumber(JavaSpecification version) {
        int javaSpecificationNumber = Integer.MIN_VALUE;
        if (version != JavaSpecification.UNKNOWN) {
            javaSpecificationNumber = Integer.parseInt(version.toString().substring(3));
        }
        return javaSpecificationNumber;
    }

    /**
     * @param jdk11ReleaseString
     *            The JDK11 release string (e.g. 11.0.9+11-LTS).
     * @return The JDK11 update number (e.g. 9).
     */
    public static final int getJdk11UpdateNumber(String jdk11ReleaseString) {
        int jdk11UpdateNumber = Integer.MIN_VALUE;
        String regEx = "11.0.(\\d{1,}).+";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(jdk11ReleaseString);
        if (matcher.find()) {
            jdk11UpdateNumber = Integer.parseInt(matcher.group(1));
        }
        return jdk11UpdateNumber;
    }

    /**
     * @param jdk17ReleaseString
     *            The JDK17 release string (e.g. 17.0.4+8-LTS).
     * @return The JDK update number (e.g. 4).
     */
    public static final int getJdk17UpdateNumber(String jdk17ReleaseString) {
        int jdk17UpdateNumber = Integer.MIN_VALUE;
        String regEx = "17.0.(\\d{1,}).+";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(jdk17ReleaseString);
        if (matcher.find()) {
            jdk17UpdateNumber = Integer.parseInt(matcher.group(1));
        }
        return jdk17UpdateNumber;
    }

    /**
     * @param jdk8ReleaseString
     *            The JDK8 release string (e.g. 1.8.0_222-b10).
     * @return The JDK8 update number (e.g. 222).
     */
    public static final int getJdk8UpdateNumber(String jdk8ReleaseString) {
        int jdk8UpdateNumber = Integer.MIN_VALUE;
        String regEx = "(1.)?8.0_(\\d{1,}).+";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(jdk8ReleaseString);
        if (matcher.find()) {
            jdk8UpdateNumber = Integer.parseInt(matcher.group(2));
        }
        return jdk8UpdateNumber;
    }

    /**
     * @param fatalErrorLog
     *            The fatal error log.
     * @return The known release date for the JDK build that produced the fatal error log.
     */
    public static final Date getJdkReleaseDate(FatalErrorLog fatalErrorLog) {
        Date date = null;
        if (fatalErrorLog != null) {
            Release release = null;
            HashMap<String, Release> releases = getJdkReleases(fatalErrorLog);
            if (releases != null && !releases.isEmpty()) {
                if (fatalErrorLog.isRhRpmInstall()) {
                    release = releases.get(fatalErrorLog.getRpmDirectory());
                } else if (fatalErrorLog.isRhLinuxZipInstall() || fatalErrorLog.isRhWindowsZipInstall()) {
                    release = releases.get(fatalErrorLog.getJdkReleaseString());
                }
            } else {
                // Approximate release
                release = fatalErrorLog.getFirstRelease(fatalErrorLog.getJdkReleaseString());
            }
            if (release != null) {
                date = release.getBuildDate();
            }
        }
        return date;
    }

    /**
     * @param fatalErrorLog
     *            The fatal error log.
     * @return The release number for the JDK that produced the fatal error log.
     */
    public static final int getJdkReleaseNumber(FatalErrorLog fatalErrorLog) {
        int number = 0;
        if (fatalErrorLog != null) {
            HashMap<String, Release> releases = getJdkReleases(fatalErrorLog);
            if (releases != null && !releases.isEmpty()) {
                Release release = null;
                if (fatalErrorLog.isRhRpmInstall()) {
                    release = releases.get(fatalErrorLog.getRpmDirectory());
                } else if (fatalErrorLog.isRhLinuxZipInstall() || fatalErrorLog.isRhWindowsZipInstall()) {
                    release = releases.get(fatalErrorLog.getJdkReleaseString());
                }
                if (release != null) {
                    number = release.getNumber();
                }
            }
        }
        return number;
    }

    /**
     * @param fatalErrorLog
     *            The fatal error log.
     * @return The JDK releases for the JDK that produced the fatal error log.
     */
    public static final HashMap<String, Release> getJdkReleases(FatalErrorLog fatalErrorLog) {
        HashMap<String, Release> releases = null;
        if (fatalErrorLog.getJavaVendor().equals(JavaVendor.RED_HAT)) {
            if (fatalErrorLog.isRhel()) {
                if (fatalErrorLog.isRhRpmInstall()) {
                    switch (fatalErrorLog.getOsVersion()) {
                    case RHEL6:
                        if (fatalErrorLog.getJavaSpecification() == JavaSpecification.JDK8) {
                            releases = Jdk8.RHEL6_X86_64_RPMS;
                        }
                        break;
                    case RHEL7:
                        if (fatalErrorLog.getJavaSpecification() == JavaSpecification.JDK8) {
                            if (fatalErrorLog.getArch() == Arch.X86_64) {
                                releases = Jdk8.RHEL7_X86_64_RPMS;
                            } else if (fatalErrorLog.getArch() == Arch.PPC64) {
                                releases = Jdk8.RHEL7_PPC64_RPMS;
                            } else if (fatalErrorLog.getArch() == Arch.PPC64LE) {
                                releases = Jdk8.RHEL7_PPC64LE_RPMS;
                            }
                        } else if (fatalErrorLog.getJavaSpecification() == JavaSpecification.JDK11) {
                            releases = Jdk11.RHEL7_X86_64_RPMS;
                        }
                        break;
                    case RHEL8:
                        if (fatalErrorLog.getJavaSpecification() == JavaSpecification.JDK8) {
                            releases = Jdk8.RHEL8_X86_64_RPMS;
                        } else if (fatalErrorLog.getJavaSpecification() == JavaSpecification.JDK11) {
                            releases = Jdk11.RHEL8_X86_64_RPMS;
                        } else if (fatalErrorLog.getJavaSpecification() == JavaSpecification.JDK17) {
                            releases = Jdk17.RHEL8_X86_64_RPMS;
                        } else if (fatalErrorLog.getJavaSpecification() == JavaSpecification.JDK21) {
                            releases = Jdk21.RHEL8_X86_64_RPMS;
                        }
                        break;
                    case RHEL9:
                        if (fatalErrorLog.getJavaSpecification() == JavaSpecification.JDK8) {
                            releases = Jdk8.RHEL9_X86_64_RPMS;
                        } else if (fatalErrorLog.getJavaSpecification() == JavaSpecification.JDK11) {
                            releases = Jdk11.RHEL9_X86_64_RPMS;
                        } else if (fatalErrorLog.getJavaSpecification() == JavaSpecification.JDK17) {
                            releases = Jdk17.RHEL9_X86_64_RPMS;
                        } else if (fatalErrorLog.getJavaSpecification() == JavaSpecification.JDK21) {
                            releases = Jdk21.RHEL9_X86_64_RPMS;
                        }
                        break;
                    default:
                    }
                } else if (fatalErrorLog.isRhLinuxZipInstall()) {
                    switch (fatalErrorLog.getJavaSpecification()) {
                    case JDK8:
                        releases = Jdk8.RHEL_ZIPS;
                        break;
                    case JDK11:
                        releases = Jdk11.RHEL_ZIPS;
                        break;
                    case JDK17:
                        releases = Jdk17.RHEL_ZIPS;
                        break;
                    case JDK21:
                        releases = Jdk21.RHEL_ZIPS;
                        break;
                    case UNKNOWN:
                    default:
                    }
                }
            } else if (fatalErrorLog.isRhWindowsZipInstall()) {
                switch (fatalErrorLog.getJavaSpecification()) {
                case JDK8:
                    releases = Jdk8.WINDOWS_ZIPS;
                    break;
                case JDK11:
                    releases = Jdk11.WINDOWS_ZIPS;
                    break;
                case JDK17:
                    releases = Jdk17.WINDOWS_ZIPS;
                    break;
                case JDK21:
                    releases = Jdk21.WINDOWS_ZIPS;
                    break;
                case UNKNOWN:
                default:
                }
            }
        }
        return releases;

    }

    /**
     * @param fatalErrorLog
     *            The fatal error log.
     * @return Latest JDK release date for the JDK that produced the fatal error log.
     */
    public static final Date getLatestJdkReleaseDate(FatalErrorLog fatalErrorLog) {
        Date date = null;
        HashMap<String, Release> releases = getJdkReleases(fatalErrorLog);
        if (releases != null && releases.get("LATEST") != null) {
            date = releases.get("LATEST").getBuildDate();
        }
        return date;
    }

    /**
     * @param fatalErrorLog
     *            The fatal error log.
     * @return Latest JDK release number for the JDK that produced the fatal error log.
     */
    public static final int getLatestJdkReleaseNumber(FatalErrorLog fatalErrorLog) {
        int number = 0;
        HashMap<String, Release> releases = getJdkReleases(fatalErrorLog);
        if (releases != null && !releases.isEmpty()) {
            Release latest = releases.get("LATEST");
            number = latest.getNumber();
        }
        return number;
    }

    /**
     * @param fatalErrorLog
     *            The fatal error log.
     * @return Latest JDK release string for the JDK that produced the fatal error log.
     */
    public static final String getLatestJdkReleaseString(FatalErrorLog fatalErrorLog) {
        String release = null;
        HashMap<String, Release> releases = getJdkReleases(fatalErrorLog);
        if (releases != null && !releases.isEmpty()) {
            release = releases.get("LATEST").getVersion();
        }
        return release;
    }

    /**
     * Get the value of a JVM option that specifies a number value.
     * 
     * For example:
     * <ul>
     * <li>The value for <code>-XX:MaxTenuringThreshold=9</code> is 9.</li>
     * <li>The value for <code>-Dsun.rmi.dgc.client.gcInterval=3600000</code> is 3600000.</li>
     * </ul>
     * 
     * @param option
     *            The JVM option or system property.
     * @return The JVM option or system property value, or <code>Integer.MIN_VALUE</code> if the option does not exist.
     */
    public static final long getNumberOptionValue(final String option) {
        long value = Long.MIN_VALUE;
        if (option != null) {
            String regex = "^.+=(\\d{1,19})$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(option);
            if (matcher.find()) {
                value = Long.parseLong(matcher.group(1));
            }
        }
        return value;
    }

    /**
     * Identify the log line fatal error log event.
     * 
     * @param logLine
     *            The log entry.
     * @param priorEvent
     *            The previous log line event.
     * @return The <code>LogEventType</code> of the log entry.
     */
    public static final LogEventType identifyEventType(String logLine, LogEvent priorEvent) {
        LogEventType logEventType = LogEventType.UNKNOWN;
        if (ActiveLocale.match(logLine)
                && (logLine.matches(ActiveLocale._REGEX_HEADER) || priorEvent instanceof ActiveLocale)) {
            logEventType = LogEventType.ACTIVE_LOCALE;
        } else if (BitsEvent.match(logLine)) {
            logEventType = LogEventType.BITS;
        } else if (BlankLine.match(logLine)) {
            logEventType = LogEventType.BLANK_LINE;
        } else if (CardTable.match(logLine)) {
            logEventType = LogEventType.CARD_TABLE;
        } else if (CdsArchive.match(logLine)) {
            logEventType = LogEventType.CDS_ARCHIVE;
        } else if (ClassesLoadedEvent.match(logLine)
                && (logLine.matches(ClassesLoadedEvent._REGEX_HEADER) || priorEvent instanceof ClassesLoadedEvent)) {
            logEventType = LogEventType.CLASSES_LOADED_EVENT;
        } else if (ClassesRedefinedEvent.match(logLine) && (logLine.matches(ClassesRedefinedEvent._REGEX_HEADER)
                || priorEvent instanceof ClassesRedefinedEvent)) {
            logEventType = LogEventType.CLASSES_REDEFINED_EVENT;
        } else if (ClassesUnloadedEvent.match(logLine) && (logLine.matches(ClassesUnloadedEvent._REGEX_HEADER)
                || priorEvent instanceof ClassesUnloadedEvent)) {
            logEventType = LogEventType.CLASSES_UNLOADED_EVENT;
        } else if (CodeCache.match(logLine)) {
            logEventType = LogEventType.CODE_CACHE;
        } else if (CommandLine.match(logLine)) {
            logEventType = LogEventType.COMMAND_LINE;
        } else if (CompilationEvent.match(logLine)
                && (logLine.matches(CompilationEvent._REGEX_HEADER) || priorEvent instanceof CompilationEvent)) {
            logEventType = LogEventType.COMPILATION_EVENT;
        } else if (CompressedClassSpace.match(logLine)) {
            logEventType = LogEventType.COMPRESSED_CLASS_SPACE;
        } else if (ConstantPool.match(logLine)) {
            logEventType = LogEventType.CONSTANT_POOL;
        } else if (ContainerInfo.match(logLine)) {
            logEventType = LogEventType.CONTAINER_INFO;
        } else if (CpuInfo.match(logLine)
                && (logLine.matches(CpuInfo._REGEX_HEADER) || priorEvent instanceof CpuInfo)) {
            logEventType = LogEventType.CPU_INFO;
        } else if (CurrentCompileTask.match(logLine)) {
            logEventType = LogEventType.CURRENT_COMPILE_TASK;
        } else if (CurrentThread.match(logLine)) {
            logEventType = LogEventType.CURRENT_THREAD;
        } else if (DeoptimizationEvent.match(logLine)
                && (logLine.matches(DeoptimizationEvent._REGEX_HEADER) || priorEvent instanceof DeoptimizationEvent)) {
            logEventType = LogEventType.DEOPTIMIZATION_EVENT;
        } else if (DllOperationEvent.match(logLine)
                && (logLine.matches(DllOperationEvent._REGEX_HEADER) || priorEvent instanceof DllOperationEvent)) {
            logEventType = LogEventType.DLL_OPERATION_EVENT;
        } else if (DynamicLibrary.match(logLine)
                && (logLine.matches(DynamicLibrary._REGEX_HEADER) || priorEvent instanceof DynamicLibrary)) {
            logEventType = LogEventType.DYNAMIC_LIBRARY;
        } else if (ElapsedTime.match(logLine)) {
            logEventType = LogEventType.ELAPSED_TIME;
        } else if (End.match(logLine)) {
            logEventType = LogEventType.END;
        } else if (EnvironmentVariable.match(logLine)
                && (logLine.matches(EnvironmentVariable._REGEX_HEADER) || priorEvent instanceof EnvironmentVariable)) {
            logEventType = LogEventType.ENVIRONMENT_VARIABLES;
        } else if (Event.match(logLine) && (logLine.matches(Event._REGEX_HEADER) || priorEvent instanceof Event)) {
            logEventType = LogEventType.EVENT;
        } else if (ExceptionCounts.match(logLine)) {
            logEventType = LogEventType.EXCEPTION_COUNTS;
        } else if (GcHeapHistoryEvent.match(logLine)
                && (logLine.matches(GcHeapHistoryEvent._REGEX_HEADER) || priorEvent instanceof GcHeapHistoryEvent)) {
            logEventType = LogEventType.GC_HEAP_HISTORY_EVENT;
        } else if (GcPreciousLog.match(logLine)) {
            logEventType = LogEventType.GC_PRECIOUS_LOG;
        } else if (GlobalFlag.match(logLine)) {
            logEventType = LogEventType.GLOBAL_FLAGS;
        } else if (Header.match(logLine)
                && (priorEvent == null || priorEvent instanceof UnknownEvent || priorEvent instanceof Header)) {
            logEventType = LogEventType.HEADER;
        } else if (Heading.match(logLine)) {
            logEventType = LogEventType.HEADING;
        } else if (Heap.match(logLine) && (logLine.matches(Heap._REGEX_HEADER) || priorEvent instanceof Heap)) {
            logEventType = LogEventType.HEAP;
        } else if (HeapAddress.match(logLine)) {
            logEventType = LogEventType.HEAP_ADDRESS;
        } else if (HeapRegions.match(logLine)) {
            logEventType = LogEventType.HEAP_REGIONS;
        } else if (Host.match(logLine)) {
            logEventType = LogEventType.HOST;
        } else if (Instructions.match(logLine)) {
            logEventType = LogEventType.INSTRUCTIONS;
        } else if (InternalExceptionEvent.match(logLine) && (logLine.matches(InternalExceptionEvent._REGEX_HEADER)
                || priorEvent instanceof InternalExceptionEvent)) {
            logEventType = LogEventType.INTERNAL_EXCEPTION_EVENT;
        } else if (InternalStatistic.match(logLine)
                && (logLine.matches(InternalStatistic._REGEX_HEADER) || priorEvent instanceof InternalStatistic)) {
            logEventType = LogEventType.INTERNAL_STATISTICS;
        } else if (LdPreloadFile.match(logLine)
                && (logLine.matches(LdPreloadFile._REGEX_HEADER) || priorEvent instanceof LdPreloadFile)) {
            logEventType = LogEventType.LD_PRELOAD_FILE;
        } else if (Libc.match(logLine)) {
            logEventType = LogEventType.LIBC;
        } else if (LoadAverage.match(logLine)) {
            logEventType = LogEventType.LOAD_AVERAGE;
        } else if (Logging.match(logLine)) {
            logEventType = LogEventType.LOGGING;
        } else if (MachCode.match(logLine)
                && (logLine.matches(MachCode._REGEX_HEADER) || priorEvent instanceof MachCode)) {
            logEventType = LogEventType.MACH_CODE;
        } else if (MaxMapCount.match(logLine)) {
            logEventType = LogEventType.MAX_MAP_COUNT;
        } else if (Meminfo.match(logLine)
                && (logLine.matches(Meminfo._REGEX_HEADER) || priorEvent instanceof Meminfo)) {
            logEventType = LogEventType.MEMINFO;
        } else if (Memory.match(logLine) && (logLine.matches(Memory._REGEX_HEADER) || priorEvent instanceof Memory)) {
            logEventType = LogEventType.MEMORY;
        } else if (Metaspace.match(logLine)) {
            logEventType = LogEventType.METASPACE;
        } else if (NarrowKlass.match(logLine)) {
            logEventType = LogEventType.NARROW_KLASS;
        } else if (NativeDecoderState.match(logLine)) {
            logEventType = LogEventType.NATIVE_DECODER_STATE;
        } else if (NativeMemoryTracking.match(logLine) && (logLine.matches(NativeMemoryTracking._REGEX_HEADER)
                || priorEvent instanceof NativeMemoryTracking)) {
            logEventType = LogEventType.NATIVE_MEMORY_TRACKING;
        } else if (NumberEvent.match(logLine)) {
            logEventType = LogEventType.NUMBER;
        } else if (OsInfo.match(logLine)) {
            logEventType = LogEventType.OS_INFO;
        } else if (OsUptime.match(logLine)) {
            logEventType = LogEventType.OS_UPTIME;
        } else if (PeriodicNativeTrim.match(logLine)) {
            logEventType = LogEventType.PERIODIC_NATIVE_TRIM;
        } else if (PidMax.match(logLine)) {
            logEventType = LogEventType.PID_MAX;
        } else if (PollingPage.match(logLine)) {
            logEventType = LogEventType.POLLING_PAGE;
        } else if (ProcessMemory.match(logLine)) {
            logEventType = LogEventType.PROCESS_MEMORY;
        } else if (Register.match(logLine)) {
            logEventType = LogEventType.REGISTER;
        } else if (RegisterToMemoryMapping.match(logLine) && (logLine.matches(RegisterToMemoryMapping._REGEX_HEADER)
                || priorEvent instanceof RegisterToMemoryMapping)) {
            logEventType = LogEventType.REGISTER_TO_MEMORY_MAPPING;
        } else if (Rlimit.match(logLine)) {
            logEventType = LogEventType.RLIMIT;
        } else if (SigInfo.match(logLine)) {
            logEventType = LogEventType.SIGINFO;
        } else if (SignalHandlers.match(logLine)) {
            logEventType = LogEventType.SIGNAL_HANDLERS;
        } else if (Stack.match(logLine)) {
            logEventType = LogEventType.STACK;
        } else if (StackSlotToMemoryMapping.match(logLine) && (logLine.matches(StackSlotToMemoryMapping._REGEX_HEADER)
                || priorEvent instanceof StackSlotToMemoryMapping)) {
            logEventType = LogEventType.STACK_SLOT_TO_MEMORY_MAPPING;
        } else if (Thread.match(logLine)) {
            logEventType = LogEventType.THREAD;
        } else if (ThreadsActiveCompile.match(logLine)) {
            logEventType = LogEventType.THREADS_ACTIVE_COMPILE;
        } else if (ThreadsClassSmrInfo.match(logLine)
                && (logLine.matches(ThreadsClassSmrInfo._REGEX_HEADER) || priorEvent instanceof ThreadsClassSmrInfo)) {
            logEventType = LogEventType.THREADS_CLASS_SMR_INFO;
        } else if (ThreadsMax.match(logLine)) {
            logEventType = LogEventType.THREADS_MAX;
        } else if (Time.match(logLine)) {
            logEventType = LogEventType.TIME;
        } else if (Timeout.match(logLine)) {
            logEventType = LogEventType.TIMEOUT;
        } else if (TimeElapsedTime.match(logLine)) {
            logEventType = LogEventType.TIME_ELAPSED_TIME;
        } else if (Timezone.match(logLine)) {
            logEventType = LogEventType.TIMEZONE;
        } else if (TopOfStack.match(logLine)) {
            logEventType = LogEventType.TOP_OF_STACK;
        } else if (TransparentHugepageDefrag.match(logLine)) {
            logEventType = LogEventType.TRANSPARENT_HUGEPAGE_DEFRAG;
        } else if (TransparentHugepageEnabled.match(logLine)) {
            logEventType = LogEventType.TRANSPARENT_HUGEPAGE_ENABLED;
        } else if (Uid.match(logLine)) {
            logEventType = LogEventType.UID;
        } else if (Umask.match(logLine)) {
            logEventType = LogEventType.UMASK;
        } else if (Uname.match(logLine)) {
            logEventType = LogEventType.UNAME;
        } else if (VmArguments.match(logLine)) {
            logEventType = LogEventType.VM_ARGUMENTS;
        } else if (VmInfo.match(logLine)) {
            logEventType = LogEventType.VM_INFO;
        } else if (VmMutex.match(logLine)) {
            logEventType = LogEventType.VM_MUTEX;
        } else if (VmOperation.match(logLine)) {
            logEventType = LogEventType.VM_OPERATION;
        } else if (VmOperationEvent.match(logLine)
                && (logLine.matches(VmOperationEvent._REGEX_HEADER) || priorEvent instanceof VmOperationEvent)) {
            logEventType = LogEventType.VM_OPERATION_EVENT;
        } else if (VmState.match(logLine)) {
            logEventType = LogEventType.VM_STATE;
        } else if (VirtualizationInfo.match(logLine)
                && (logLine.matches(VirtualizationInfo._REGEX_HEADER) || priorEvent instanceof VirtualizationInfo)) {
            logEventType = LogEventType.VIRTUALIZATION_INFO;
        } else if (ZgcGlobals.match(logLine)) {
            logEventType = LogEventType.ZGC_GLOBALS;
        } else if (ZgcMetadataBits.match(logLine)) {
            logEventType = LogEventType.ZGC_METADATA_BITS;
        } else if (ZgcGlobals.match(logLine)) {
            logEventType = LogEventType.ZGC_GLOBALS;
        } else if (ZgcMetadataBits.match(logLine)) {
            logEventType = LogEventType.ZGC_METADATA_BITS;
        } else if (ZgcPageTable.match(logLine)) {
            logEventType = LogEventType.ZGC_PAGE_TABLE;
        } else if (ZgcPhaseSwitchEvent.match(logLine)) {
            logEventType = LogEventType.ZGC_PHASE_SWITCH_EVENT;
        }
        return logEventType;
    }

    /**
     * Determine if a build date is a known date/time or an estimate. Estimate have 0 for hh:mm:ss.
     * 
     * The following build date/time is a known date/time:
     * 
     * Apr 19 2022 00:14:41
     * 
     * The following build date/time is an estimate:
     * 
     * Apr 19 2022 00:00:00
     * 
     * @param buildDate
     *            The JDK build date/time, or an estimate if unknown.
     * 
     * @return true if the JDK build date is known, false otherwise.
     */
    public static final boolean isBuildDateKnown(Date buildDate) {
        boolean isBuildDateKnown = false;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(buildDate);
        if (!(calendar.get(Calendar.HOUR) == 0 && calendar.get(Calendar.MINUTE) == 0
                && calendar.get(Calendar.SECOND) == 0)) {
            isBuildDateKnown = true;
        }
        return isBuildDateKnown;
    }

    /**
     * @param fatalErrorLog
     *            The fatal error log.
     * @return true if the JDK that produced the fatal error log is the latest release, false otherwise.
     */
    public static final boolean isLatestJdkRelease(FatalErrorLog fatalErrorLog) {
        boolean isLatestRelease = true;
        HashMap<String, Release> releases = getJdkReleases(fatalErrorLog);
        if (releases != null && !releases.isEmpty()) {
            Release latest = releases.get("LATEST");
            if (latest != null) {
                if (latest.getVersion() != null && !latest.getVersion().equals(fatalErrorLog.getJdkReleaseString())) {
                    isLatestRelease = false;
                } else if (latest.getBuildDate() != null
                        && !latest.getBuildDate().equals(fatalErrorLog.getJdkReleaseDate())) {
                    // There is a newer release with the same release string
                    isLatestRelease = false;
                }
            }
        }
        return isLatestRelease;
    }

    /**
     * Determine if a JVM option is explicitly disabled. For example, <code>-XX:-TraceClassUnloading</code> is disabled.
     * 
     * @param option
     *            The JVM option.
     * @return True if the JVM option is disabled, false otherwise.
     */
    public static final boolean isOptionDisabled(final String option) {
        boolean disabled = false;
        if (option != null) {
            disabled = option.matches("^-XX:-.+$");
        }
        return disabled;
    }

    /**
     * Determine if a JVM option is explicitly enabled. For example, <code>-XX:+TraceClassUnloading</code> is enabled.
     * 
     * @param option
     *            The JVM option.
     * @return True if the JVM option is enabled, false otherwise.
     */
    public static final boolean isOptionEnabled(final String option) {
        boolean enabled = false;
        if (option != null) {
            enabled = option.matches("^-XX:\\+.+$");
        }
        return enabled;
    }

    /**
     * @param jdkReleaseString
     *            The JDK release string. For example: "11.0.15+10-LTS".
     * @param releases
     *            The JDK releases for a given OS and distribution method (e.g. zip, rpm).
     * @return True if the release string exists in the releases, false otherwise.
     */
    public static final boolean isReleaseStringInReleases(String jdkReleaseString, HashMap<String, Release> releases) {
        boolean containsJdkReleaseString = false;
        Iterator<Entry<String, Release>> i = releases.entrySet().iterator();
        while (i.hasNext()) {
            Entry<String, Release> entry = i.next();
            Release release = entry.getValue();
            if (release.getVersion().equals(jdkReleaseString)) {
                containsJdkReleaseString = true;
                break;
            }
        }
        return containsJdkReleaseString;
    }

    /**
     * Create <code>LogEvent</code> from VM log line.
     * 
     * @param logLine
     *            The log line as it appears in the VM log.
     * @param priorEvent
     *            The previous log line event.
     * @return The <code>LogEvent</code> corresponding to the log line.
     */
    public static final LogEvent parseLogLine(String logLine, LogEvent priorEvent) {
        LogEventType eventType = identifyEventType(logLine, priorEvent);
        LogEvent event = null;
        switch (eventType) {
        case ACTIVE_LOCALE:
            event = new ActiveLocale(logLine);
            break;
        case BITS:
            event = new BitsEvent(logLine);
            break;
        case BLANK_LINE:
            event = new BlankLine(logLine);
            break;
        case CARD_TABLE:
            event = new CardTable(logLine);
            break;
        case CDS_ARCHIVE:
            event = new CdsArchive(logLine);
            break;
        case CLASSES_LOADED_EVENT:
            event = new ClassesLoadedEvent(logLine);
            break;
        case CLASSES_REDEFINED_EVENT:
            event = new ClassesRedefinedEvent(logLine);
            break;
        case CLASSES_UNLOADED_EVENT:
            event = new ClassesUnloadedEvent(logLine);
            break;
        case CODE_CACHE:
            event = new CodeCache(logLine);
            break;
        case COMMAND_LINE:
            event = new CommandLine(logLine);
            break;
        case COMPRESSED_CLASS_SPACE:
            event = new CompressedClassSpace(logLine);
            break;
        case COMPILATION_EVENT:
            event = new CompilationEvent(logLine);
            break;
        case CONSTANT_POOL:
            event = new ConstantPool(logLine);
            break;
        case CONTAINER_INFO:
            event = new ContainerInfo(logLine);
            break;
        case CPU_INFO:
            event = new CpuInfo(logLine);
            break;
        case CURRENT_COMPILE_TASK:
            event = new CurrentCompileTask(logLine);
            break;
        case CURRENT_THREAD:
            event = new CurrentThread(logLine);
            break;
        case DEOPTIMIZATION_EVENT:
            event = new DeoptimizationEvent(logLine);
            break;
        case DLL_OPERATION_EVENT:
            event = new DllOperationEvent(logLine);
            break;
        case DYNAMIC_LIBRARY:
            event = new DynamicLibrary(logLine);
            break;
        case ELAPSED_TIME:
            event = new ElapsedTime(logLine);
            break;
        case END:
            event = new End(logLine);
            break;
        case EVENT:
            event = new Event(logLine);
            break;
        case ENVIRONMENT_VARIABLES:
            event = new EnvironmentVariable(logLine);
            break;
        case EXCEPTION_COUNTS:
            event = new ExceptionCounts(logLine);
            break;
        case GC_HEAP_HISTORY_EVENT:
            event = new GcHeapHistoryEvent(logLine);
            break;
        case GC_PRECIOUS_LOG:
            event = new GcPreciousLog(logLine);
            break;
        case GLOBAL_FLAGS:
            event = new GlobalFlag(logLine);
            break;
        case HEADER:
            event = new Header(logLine);
            break;
        case HEADING:
            event = new Heading(logLine);
            break;
        case HEAP:
            event = new Heap(logLine);
            break;
        case HEAP_ADDRESS:
            event = new HeapAddress(logLine);
            break;
        case HEAP_REGIONS:
            event = new HeapRegions(logLine);
            break;
        case HOST:
            event = new Host(logLine);
            break;
        case INSTRUCTIONS:
            event = new Instructions(logLine);
            break;
        case INTERNAL_EXCEPTION_EVENT:
            event = new InternalExceptionEvent(logLine);
            break;
        case INTERNAL_STATISTICS:
            event = new InternalStatistic(logLine);
            break;
        case LD_PRELOAD_FILE:
            event = new LdPreloadFile(logLine);
            break;
        case LIBC:
            event = new Libc(logLine);
            break;
        case LOAD_AVERAGE:
            event = new LoadAverage(logLine);
            break;
        case LOGGING:
            event = new Logging(logLine);
            break;
        case MACH_CODE:
            event = new MachCode(logLine);
            break;
        case MAX_MAP_COUNT:
            event = new MaxMapCount(logLine);
            break;
        case MEMINFO:
            event = new Meminfo(logLine);
            break;
        case MEMORY:
            event = new Memory(logLine);
            break;
        case METASPACE:
            event = new Metaspace(logLine);
            break;
        case NARROW_KLASS:
            event = new NarrowKlass(logLine);
            break;
        case NATIVE_DECODER_STATE:
            event = new NativeDecoderState(logLine);
            break;
        case NATIVE_MEMORY_TRACKING:
            event = new NativeMemoryTracking(logLine);
            break;
        case NUMBER:
            event = new NumberEvent(logLine);
            break;
        case OS_INFO:
            event = new OsInfo(logLine);
            break;
        case OS_UPTIME:
            event = new OsUptime(logLine);
            break;
        case PERIODIC_NATIVE_TRIM:
            event = new PeriodicNativeTrim(logLine);
            break;
        case PID_MAX:
            event = new PidMax(logLine);
            break;
        case POLLING_PAGE:
            event = new PollingPage(logLine);
            break;
        case PROCESS_MEMORY:
            event = new ProcessMemory(logLine);
            break;
        case REGISTER:
            event = new Register(logLine);
            break;
        case REGISTER_TO_MEMORY_MAPPING:
            event = new RegisterToMemoryMapping(logLine);
            break;
        case RLIMIT:
            event = new Rlimit(logLine);
            break;
        case SIGINFO:
            event = new SigInfo(logLine);
            break;
        case SIGNAL_HANDLERS:
            event = new SignalHandlers(logLine);
            break;
        case STACK:
            event = new Stack(logLine);
            break;
        case STACK_SLOT_TO_MEMORY_MAPPING:
            event = new StackSlotToMemoryMapping(logLine);
            break;
        case THREAD:
            event = new Thread(logLine);
            break;
        case THREADS_ACTIVE_COMPILE:
            event = new ThreadsActiveCompile(logLine);
            break;
        case THREADS_CLASS_SMR_INFO:
            event = new ThreadsClassSmrInfo(logLine);
            break;
        case THREADS_MAX:
            event = new ThreadsMax(logLine);
            break;
        case TIME:
            event = new Time(logLine);
            break;
        case TIMEOUT:
            event = new Timeout(logLine);
            break;
        case TIME_ELAPSED_TIME:
            event = new TimeElapsedTime(logLine);
            break;
        case TIMEZONE:
            event = new Timezone(logLine);
            break;
        case TOP_OF_STACK:
            event = new TopOfStack(logLine);
            break;
        case TRANSPARENT_HUGEPAGE_DEFRAG:
            event = new TransparentHugepageDefrag(logLine);
            break;
        case TRANSPARENT_HUGEPAGE_ENABLED:
            event = new TransparentHugepageEnabled(logLine);
            break;
        case UID:
            event = new Uid(logLine);
            break;
        case UMASK:
            event = new Umask(logLine);
            break;
        case UNAME:
            event = new Uname(logLine);
            break;
        case UNKNOWN:
            event = new UnknownEvent(logLine);
            break;
        case VM_ARGUMENTS:
            event = new VmArguments(logLine);
            break;
        case VM_OPERATION_EVENT:
            event = new VmOperationEvent(logLine);
            break;
        case VM_INFO:
            event = new VmInfo(logLine);
            break;
        case VM_MUTEX:
            event = new VmMutex(logLine);
            break;
        case VM_OPERATION:
            event = new VmOperation(logLine);
            break;
        case VM_STATE:
            event = new VmState(logLine);
            break;
        case VIRTUALIZATION_INFO:
            event = new VirtualizationInfo(logLine);
            break;
        case ZGC_GLOBALS:
            event = new ZgcGlobals(logLine);
            break;
        case ZGC_METADATA_BITS:
            event = new ZgcMetadataBits(logLine);
            break;
        case ZGC_PAGE_TABLE:
            event = new ZgcPageTable(logLine);
            break;
        case ZGC_PHASE_SWITCH_EVENT:
            event = new ZgcPhaseSwitchEvent(logLine);
            break;
        default:
            throw new AssertionError("Unexpected event type value: " + eventType);
        }
        return event;
    }

    /**
     * Make default constructor private so the class cannot be instantiated.
     */
    private JdkUtil() {

    }
}
