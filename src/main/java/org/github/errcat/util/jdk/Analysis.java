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

import org.github.errcat.util.Constants;
import org.github.errcat.util.ErrUtil;

/**
 * Analysis constants.
 * 
 * @author <a href="mailto:mmillson@redhat.com">Mike Millson</a>
 * 
 */
public enum Analysis {

    /**
     * Property key for a crash in BufferBlob::flush_icache_stub.
     */
    ERROR_BUFFERBLOB_FLUSH_ICACHE_STUB("error.bufferblob.flush_icache_stub"),

    /**
     * Property key for a crash due to multiple threads access DirectByteBuffer at the same time.
     */
    ERROR_DIRECT_BYTE_BUFFER_CONTENTION("error.direct.byte.buffer.contention"),

    /**
     * Property key for heap + metaspace &gt; physical memory.
     */
    ERROR_HEAP_PLUS_METASPACE_GT_PHYSICAL_MEMORY("error.heap.plus.metaspace.gt.physical.memory"),

    /**
     * Property key for a crash on RHEL7 where a power8 rpm is deployed on power9. Power8 support is through the
     * rhel-7-for-power-le-rpms repo (least release 7.9). Power9 support is through the rhel-7-for-power-9-rpms repo
     * (last release 7.6).
     */
    ERROR_JDK8_RHEL7_POWER8_RPM_ON_POWER9("error.jdk8.rhel7.power8.rpm.on.power9"),

    /**
     * Property key for ShenandoahConcurrentMark::mark_loop_work.
     */
    ERROR_JDK8_SHENANDOAH_MARK_LOOP_WORK("error.jdk8.shenandoah.mark.loop.work"),

    /**
     * Property key for ShenadoahRootUpdater bug fixed in OpenJDK8 u282.
     */
    ERROR_JDK8_SHENANDOAH_ROOT_UPDATER("error.jdk8.shenandoah.root.updater"),

    /**
     * Property key for a crash in JDK8 when a file is attempting to be modified while Java has it open.
     */
    ERROR_JDK8_ZIPFILE_CONTENTION("error.jdk8.zipfile.contention"),

    /**
     * Property key for a crash in JNA code.
     */
    ERROR_JNA("error.jna"),

    /**
     * Property key for a crash in JNA code on a Red Hat JDK.
     */
    ERROR_JNA_RH("error.jna.rh"),

    /**
     * Property key for a crash in org.apache.activemq.artemis.nativo.jlibaio.LibaioContext.done() method.
     */
    ERROR_LIBAIO_CONTEXT_DONE("error.libaio.context.done"),

    /**
     * Property key for Out of Memory Error due to CompressedOops blocking the growth of the native heap.
     */
    ERROR_OOME_COMPRESSED_OOPS("error.oome.compressed.oops"),

    /**
     * Property key for insufficient physical memory due to an external process.
     */
    ERROR_OOME_EXTERNAL("error.oome.external"),

    /**
     * Property key for OutOfMemoryError Java heap space.
     */
    ERROR_OOME_JAVA_HEAP("error.oome.java.heap"),

    /**
     * Property key for insufficient physical memory due to the JVM process.
     */
    ERROR_OOME_JVM("error.oome.jvm"),

    /**
     * Property key for not enough physical memory for the JVM to start.
     */
    ERROR_OOME_STARTUP("error.oome.startup"),

    /**
     * Property key for error calling pthread_getcpuclockid
     */
    ERROR_PTHREAD_GETCPUCLOCKID("error.pthread.getcpuclockid"),

    /**
     * Property key for error for remote debugging enabled.
     */
    ERROR_REMOTE_DEBUGGING_ENABLED("error.remote.debugging.enabled"),

    /**
     * Property key for stack free space greater than stack size.
     */
    ERROR_STACK_FREESPACE_GT_STACK_SIZE("error.stack.freespace.gt.stack.size"),

    /**
     * Property key for StackOverflowError.
     */
    ERROR_STACKOVERFLOW("error.stackoverflow"),

    /**
     * Property key for crash in ~BufferBlob::StubRoutines.
     */
    ERROR_STUBROUTINES("error.stubroutines"),

    /**
     * Property key for AdoptOpenJDK build of OpenJDK.
     */
    INFO_ADOPTOPENJDK_POSSIBLE("info.adoptopenjdk.possible"),

    /**
     * Property key for a JDK that is more than 1 year older than the latest release.
     */
    INFO_JDK_ANCIENT("info.jdk.ancient"),

    /**
     * Property key for crash on JVM startup.
     */
    INFO_JVM_STARTUP_FAILS("info.jvm.startup.fails"),

    /**
     * Property key for Red Hat build of OpenJDK on CentOS.
     */
    INFO_RH_BUILD_CENTOS("info.rh.build.centos"),

    /**
     * Property key for Red Hat build of OpenJDK Linux zip install.
     */
    INFO_RH_BUILD_LINUX_ZIP("info.rh.build.linux.zip"),

    /**
     * Property key for a JDK that is not a RH build.
     */
    INFO_RH_BUILD_NOT("info.rh.build.not"),

    /**
     * Property key for a JDK that is possibly a RH build.
     */
    INFO_RH_BUILD_POSSIBLE("info.rh.build.possible"),

    /**
     * Property key for Red Hat build of OpenJDK rpm install.
     */
    INFO_RH_BUILD_RPM("info.rh.build.rpm"),

    /**
     * Property key for Red Hat build of OpenJDK Windows zip install.
     */
    INFO_RH_BUILD_WINDOWS_ZIP("info.rh.build.windows.zip"),

    /**
     * Property key for BUS_ADDRERR.
     */
    INFO_SIGCODE_BUS_ADDERR("info.sigcode.bus.adrerr"),

    /**
     * Property key for BUS_ADDR crash on linux.
     */
    INFO_SIGCODE_BUS_ADDERR_LINUX("info.sigcode.bus.adrerr.linux"),

    /**
     * Property key for BUS_ADRALN.
     */
    INFO_SIGCODE_BUS_ADRALN("info.sigcode.bus.adraln"),

    /**
     * Property key for BUS_OBJERR.
     */
    INFO_SIGCODE_BUS_OBJERR("info.sigcode.bus.objerr"),

    /**
     * Property key for ILL_ILLOPN
     */
    INFO_SIGCODE_ILL_ILLOPN("info.sigcode.ill.illopn"),

    /**
     * Property key for SEGV_ACCERR.
     */
    INFO_SIGCODE_SEGV_ACCERR("info.sigcode.segv.accerr"),

    /**
     * Property key for SEGV_MAPERR.
     */
    INFO_SIGCODE_SEGV_MAPERR("info.sigcode.segv.maperr"),

    /**
     * Property key for SI_KERNEL crash.
     */
    INFO_SIGCODE_SI_KERNEL("info.sigcode.si.kernel"),

    /**
     * Property key for SI_USER crash.
     */
    INFO_SIGCODE_SI_USER("info.sigcode.si.user"),

    /**
     * Property key for ACCESS_VIOLATION crash.
     */
    INFO_SIGNO_EXCEPTION_ACCESS_VIOLATION("info.signo.exception.access.violation"),

    /**
     * Property key for SIGBUS crash.
     */
    INFO_SIGNO_SIGBUS("info.signo.sigbus"),

    /**
     * Property key for SIGILL crash.
     */
    INFO_SIGNO_SIGILL("info.signo.sigill"),

    /**
     * Property key for SIGSEGV crash.
     */
    INFO_SIGNO_SIGSEGV("info.signo.sigsegv"),

    /**
     * Property key for the stack not containing any VM code.
     */
    INFO_STACK_NO_VM_CODE("info.stack.no.vm.code"),

    /**
     * Property key for swapping disabled.
     */
    INFO_SWAP_DISABLED("info.swap.disabled"),

    /**
     * Property key for swapping.
     */
    INFO_SWAPPING("info.swapping"),

    /**
     * Property key for a small thread stack size (&lt; 128k).
     */
    INFO_THREAD_STACK_SIZE_SMALL("warn.thread.stack.size.small"),

    /**
     * Property key for no evidence the JDK debug symbols are installed.
     */
    WARN_DEBUG_SYMBOLS("warn.jdk.debug.symbols"),

    /**
     * Property key for a JDK that is not the latest JDK release.
     */
    WARN_JDK_NOT_LATEST("warn.jdk.not.latest"),

    /**
     * Property key for a JDK that is not a Long Term Support (LTS) version.
     */
    WARN_JDK_NOT_LTS("warn.jdk.not.lts"),

    /**
     * Property key for a JDK that is deployed on RHEL6.
     */
    WARN_RHEL6("warn.rhel6"),

    /**
     * Property key for a tiny thread stack size (&lt; 1k).
     */
    WARN_THREAD_STACK_SIZE_TINY("warn.thread.stack.size.tiny"),

    /**
     * Property key for unidentified line(s) needing reporting.
     */
    WARN_UNIDENTIFIED_LOG_LINE_REPORT("warn.unidentified.log.line.report");

    private String key;

    private Analysis(final String key) {
        this.key = key;
    ***REMOVED***

    /**
     * @return Analysis property file key.
     */
    public String getKey() {
        return key;
    ***REMOVED***

    /**
     * @return Analysis property file value.
     */
    public String getValue() {
        return ErrUtil.getPropertyValue(Constants.ANALYSIS_PROPERTY_FILE, key);
    ***REMOVED***

    @Override
    public String toString() {
        return this.getKey();
    ***REMOVED***
***REMOVED***
