/**********************************************************************************************************************
 * krashpad                                                                                                           *
 *                                                                                                                    *
 * Copyright (c) 2020-2022 Mike Millson                                                                               *
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

/**
 * Regular expression utility methods and constants for OpenJDK and Oracle JDK.
 * 
 * @author <a href="mailto:mmillson@redhat.com">Mike Millson</a>
 * 
 */
public class JdkRegEx {

    /**
     * A 32-bit or 64-bit memory address.
     */
    public static final String ADDRESS = "(" + JdkRegEx.ADDRESS32 + "|" + JdkRegEx.ADDRESS64 + ")";

    /**
     * A 32-bit memory address.
     */
    public static final String ADDRESS32 = "((0x)?[0-9a-f]{8***REMOVED***)";

    /**
     * A 64-bit memory address.
     */
    public static final String ADDRESS64 = "((0x)?[0-9a-f]{16***REMOVED***)";

    /**
     * Memory map area.
     * 
     * For example: [vsyscall]
     */
    public static final String AREA = "(\\[(stack|vdso|vsyscall)\\])";

    /**
     * ActiveMQ CLI main class used for <code>Application</code> identification.
     * 
     * For example:
     * 
     * <pre>
     * org.apache.activemq.artemis.boot.Artemis queue stat --url tcp://mydomain:12345 --user myuser 
     * --password mypassword --maxRows 1234
     * 
     * org.apache.activemq.artemis.boot.Artemis queue purge --name ExpiryQueue --url tcp://mydomain:12345 
     * --user myuser --password mypassword
     * </pre>
     */
    public static final String ARTEMIS_CLI_COMMAND = "^.*org\\.apache\\.activemq\\.artemis\\.boot\\.Artemis (?!run).*$";

    /**
     * ActiveMQ main class used for <code>Application</code> identification.
     * 
     * For example:
     * 
     * <pre>
     * org.apache.activemq.artemis.boot.Artemis run
     * </pre>
     */
    public static final String ARTEMIS_COMMAND = "^.*org\\.apache\\.activemq\\.artemis\\.boot\\.Artemis run.*$";

    /**
     * Blank line.
     */
    public static final String BLANK_LINE = "^\\s+$";

    /**
     * <p>
     * Regular expression for JDK build date/time in MMM d yyyy HH:mm:ss format (see <code>SimpleDateFormat</code> for
     * date and time pattern definitions).
     * </p>
     * 
     * For example:
     * 
     * <pre>
     * Oct  6 2018 06:46:09
     * </pre>
     */
    public static final String BUILD_DATE_TIME = "([a-zA-Z]{3***REMOVED***)[ ]{1,2***REMOVED***(\\d{1,2***REMOVED***) (\\d{4***REMOVED***) (\\d{2***REMOVED***):(\\d{2***REMOVED***):(\\d{2***REMOVED***)";

    /**
     * <p>
     * Regular expression for a JDK release string.
     * </p>
     * 
     * For example:
     * 
     * <pre>
     * 1.7.0_85-b15
     * 1.8.0_131-b11
     * 11.0.9+11-LTS
     * 12.0.1+12
     * </pre>
     */
    public static final String BUILD_STRING = "((1.6.0|1.7.0|1.8.0|9|10|11|12|13|14|15|16|17)[^\\)]{1,***REMOVED***)";

    /**
     * Byte units identifier.
     */
    public static final String BYTES = "bB";

    /**
     * <p>
     * Regular expression for crash date/time in MMM d HH:mm:ss yyyy format (see <code>SimpleDateFormat</code> for date
     * and time pattern definitions).
     * </p>
     * 
     * For example:
     * 
     * <p>
     * 1) Without timezone:
     * </p>
     *
     * <pre>
     * Tue Aug 18 14:10:59 2020
     * </pre>
     * 
     * <p>
     * 2) With timezone:
     * </p>
     *
     * <pre>
     * Tue Mar  1 09:13:16 2022 UTC
     * </pre>
     */
    public static final String CRASH_DATE_TIME = "([a-zA-Z]{3***REMOVED***) ([a-zA-Z]{3***REMOVED***)[ ]{1,2***REMOVED***(\\d{1,2***REMOVED***) "
            + "(\\d{2***REMOVED***):(\\d{2***REMOVED***):(\\d{2***REMOVED***) (\\d{4***REMOVED***).*";

    /**
     * Major ID and minor ID of the device where the file is located in hexadecimal.
     * 
     * To determine the device, convert the major id to decimal and cross reference lsblk output.
     * 
     * For example:
     * 
     * 7f6d941a5000-7f6d95338000 r-xp 00000000 fd:01 34113771
     * /usr/lib/jvm/java-11-openjdk-11.0.11.0.9-0.el8_3.x86_64/lib/server/libjvm.so
     * 
     * fd = 253, and 253:1 maps to fixed disk "/":
     * 
     * <code>
     * $ lsblk
     * NAME                                          MAJ:MIN RM   SIZE RO TYPE  MOUNTPOINT
     * nvme0n1                                       259:0    0   477G  0 disk  
     * ├─nvme0n1p1                                   259:1    0   600M  0 part  /boot/efi
     * ├─nvme0n1p2                                   259:2    0     1G  0 part  /boot
     * └─nvme0n1p3                                   259:3    0 475.4G  0 part  
     *   └─luks-43efaec5-acef-4c6a-b96e-3fa67b5a0a15 253:0    0 475.3G  0 crypt 
     *     ├─rhel-root                               253:1    0    50G  0 lvm   /
     *     ├─rhel-swap                               253:2    0  15.7G  0 lvm   [SWAP]
     *     └─rhel-home                               253:3    0 409.7G  0 lvm   /home
     *</code>
     * 
     * <p>
     * 1) Fixed disk:
     * </p>
     * 
     * <pre>
     * fd:0d
     * </pre>
     * 
     * <p>
     * 2) AWS block storage:
     * </p>
     * 
     * <pre>
     * 103:03
     * </pre>
     * 
     * <p>
     * 3) NFS:
     * </p>
     * 
     * <pre>
     * 00:38
     * </pre>
     */
    public static final String DEVICE_IDS = "([0-9a-f]{2,3***REMOVED***:[0-9a-f]{2,4***REMOVED***)";

    /**
     * Memory map file path.
     * 
     * For example: /usr/lib64/libaio.so.1.0.1
     */
    public static final String FILE = "(.*/)*(.+)";

    /**
     * File offset
     * 
     * For example: 00016000
     */
    public static final String FILE_OFFSET = "([0-9a-f]{8***REMOVED***)";

    /**
     * Regular expression for G1 gc data.
     * 
     * <pre>
     *  garbage-first heap   total 33554432K, used 22395212K [0x00007f56fc000000, 0x00007f5efc000000)
     *   region size 16384K, 182 young (2981888K), 19 survivors (311296K)
     * </pre>
     */
    public static final String G1 = "(" + JdkRegEx.G1_SIZE + "|  region size.+)";

    /**
     * Regular expression for G1 heap size data.
     * 
     * <pre>
     *  garbage-first heap   total 33554432K, used 22395212K [0x00007f56fc000000, 0x00007f5efc000000)
     * </pre>
     */
    public static final String G1_SIZE = " garbage-first heap   total " + JdkRegEx.SIZE + ", used " + JdkRegEx.SIZE
            + ".+";

    /**
     * Gigabyte units identifier.
     */
    public static final String GIGABYTES = "gG";

    /**
     * File inode number
     * 
     * For example: 134326056
     */
    public static final String INODE = "([0-9]{1,***REMOVED***)";

    /**
     * Regular expression for java.nio.ByteBuffer class.
     */
    public static final String JAVA_NIO_BYTEBUFFER = "java[\\.\\/]nio[\\.\\/]ByteBuffer";

    /**
     * JBoss EAP6 jar used for <code>Application</code> identification.
     * 
     * For example:
     * 
     * 7fb99ed0d000-7fb99ed15000 r--s 0006e000 f9:00 792511 /path/to/jbossweb-7.5.7.Final-redhat-1.jar
     */
    public static final String JBOSS_EAP6_JAR = "^.+jbossweb.+\\.jar$";

    /**
     * JBoss EAP6 jar used for <code>Application</code> identification.
     * 
     * For example:
     * 
     * 7fb99ed0d000-7fb99ed15000 r--s 0006e000 f9:00 792511 /path/to/undertow-core-2.0.22.Final-redhat-00001.jar
     */
    public static final String JBOSS_EAP7_JAR = "^.+undertow-core.+\\.jar$";

    /**
     * JBoss version check string used for <code>Application</code> identification.
     * 
     * For example:
     * 
     * <pre>
     * org.jboss.as.standalone -Djboss.home.dir=C:\path\to -version
     * </pre>
     */
    public static final String JBOSS_VERSION_COMMAND = "^.+org\\.jboss\\.as\\.standalone .*(-[vV]|[-]{1,2***REMOVED***version)$";

    /**
     * Kafka main class used for <code>Application</code> identification.
     * 
     * For example:
     * 
     * <pre>
     * kafka.Kafka / path / to / my.properties
     * </pre>
     */
    public static final String KAFKA_COMMAND = "^.*kafka\\.Kafka.*$";

    /**
     * Kilobyte units identifier.
     */
    public static final String KILOBYTES = "kK";

    /**
     * Megabyte units identifier.
     */
    public static final String MEGABYTES = "mM";

    /**
     * Object memory mapping.
     * 
     * For example:
     * 
     * <pre>
     * org.xnio.nio.WorkerThread {0x0000000800c89d28***REMOVED***
     * </pre>
     */
    public static final String MEMORY_MAPPING = "^[a-zA-z\\.]+ \\{" + ADDRESS + "\\***REMOVED***$";

    /**
     * Memory region
     * 
     * For example: 7f95caa94000-7f95caaa3000
     */
    public static final String MEMORY_REGION = "([0-9a-f]{8,16***REMOVED***-[0-9a-f]{8,16***REMOVED***)";

    /**
     * Regular expression for a metaspace event.
     * 
     * <pre>
     * Metaspace used 19510K, capacity 21116K, committed 21248K, reserved 1069056K
     *  class space    used 32477K, capacity 37071K, committed 40576K, reserved 1048576K
     * </pre>
     */
    public static final String METASPACE = "(" + JdkRegEx.METASPACE_SIZE + "|  class space.+)";

    /**
     * Regular expression for a metaspace event.
     * 
     * <pre>
     * Metaspace used 19510K, capacity 21116K, committed 21248K, reserved 1069056K
     * </pre>
     */
    public static final String METASPACE_SIZE = " Metaspace[ ]{1,7***REMOVED***used " + JdkRegEx.SIZE + ", (capacity "
            + JdkRegEx.SIZE + ", )?committed " + JdkRegEx.SIZE + ", reserved " + JdkRegEx.SIZE;

    /**
     * A null pointer address.
     */
    public static final String NULL_POINTER = "0x([0]{8***REMOVED***|[0]{16***REMOVED***)";

    /**
     * Regular expression for a old generation gc data.
     * 
     * 1) <code>GarbageCollection.PARALLEL_OLD</code>:
     * 
     * <pre>
     * ParOldGen       total 341504K, used 94378K [0x00000005cd600000, 0x00000005e2380000, 0x0000000719d00000)
     *  object space 341504K, 27% used [0x00000005cd600000,0x00000005d322aa70,0x00000005e2380000)
     * </pre>
     * 
     * 2) <code>GarbageCollection.SERIAL_OLD</code> when in combination with
     * <code>GarbageCollection.PARALLEL_SCAVENGE</code>:
     * 
     * ParOldGen total 699392K, used 91187K [0x00000000c0000000, 0x00000000eab00000, 0x00000000eab00000)
     * 
     * 3) <code>GarbageCollection.SERIAL_OLD</code> when in combination with <code>GarbageCollection.SERIAL</code>:
     * 
     * <pre>
     *   tenured generation   total 2165440K, used 937560K [0x000000073bd50000, 0x00000007c0000000, 0x00000007c0000000)
     *     the space 2165440K,  43% used [0x000000073bd50000, 0x00000007750e6118, 0x00000007750e6200, 
     *     0x00000007c0000000)
     * </pre>
     * 
     * 4) <code>GarbageCollection.CMS</code>:
     * 
     * concurrent mark-sweep generation total 21676032K, used 6923299K [0x0000000295000000, 0x00000007c0000000,
     * 0x00000007c0000000)
     */
    public static final String OLD_GEN = "(" + JdkRegEx.OLD_GEN_SIZE + "|  (object| the) space.+)";

    /**
     * Regular expression for a old generation heap size data.
     * 
     * 1) <code>GarbageCollection.PARALLEL_OLD</code>:
     * 
     * <pre>
     * ParOldGen       total 341504K, used 94378K [0x00000005cd600000, 0x00000005e2380000, 0x0000000719d00000)
     *  object space 341504K, 27% used [0x00000005cd600000,0x00000005d322aa70,0x00000005e2380000)
     * </pre>
     * 
     * 2) <code>GarbageCollection.SERIAL_OLD</code> when in combination with
     * <code>GarbageCollection.PARALLEL_SCAVENGE</code>:
     * 
     * ParOldGen total 699392K, used 91187K [0x00000000c0000000, 0x00000000eab00000, 0x00000000eab00000)
     * 
     * 3) <code>GarbageCollection.SERIAL_OLD</code> when in combination with <code>GarbageCollection.SERIAL</code>:
     * 
     * <pre>
     *   tenured generation   total 2165440K, used 937560K [0x000000073bd50000, 0x00000007c0000000, 0x00000007c0000000)
     * </pre>
     * 
     * 4) <code>GarbageCollection.CMS</code>:
     * 
     * <pre>
     * concurrent mark-sweep generation total 21676032K, used 6923299K [0x0000000295000000, 0x00000007c0000000, 
     * 0x00000007c0000000)
     * </pre>
     */
    public static final String OLD_GEN_SIZE = " (concurrent mark-sweep generation|PSOldGen|ParOldGen|"
            + "tenured generation)[ ]{1,7***REMOVED***total " + JdkRegEx.SIZE + ", used " + JdkRegEx.SIZE + ".+";

    /**
     * Units for JVM options that take a byte number.
     * 
     * For example: -Xss128k -Xmx2048m -Xms2G -XX:ThreadStackSize=256
     */
    public static final String OPTION_SIZE_BYTES = "((\\d{1,***REMOVED***)(b|B|k|K|m|M|g|G)?)";

    /**
     * Percent rounded to whole number.
     * 
     * For example: 54%
     */
    public static final String PERCENT = "\\d{1,3***REMOVED***%";

    /**
     * Permission
     * 
     * For example: r--s
     */
    public static final String PERMISION = "([rwxps\\-]{4***REMOVED***)";

    /**
     * A 32-bit or 64-bit register.
     */
    public static final String REGISTER = "(CR2|CSGSFS|ctr|EAX|EBP|EBX|ECX|EDI|EDX|EFLAGS|EIP|ERR|ESI|ESP|lr |pc |RAX|"
            + "RBP|RBX|RCX|RDI|RDX|RIP|RSI|RSP|[Rr]" + "\\d{1,2***REMOVED***[ ]{0,1***REMOVED***)=(" + ADDRESS32 + "|" + ADDRESS64 + ")";

    /**
     * Red Hat OpenJDK rpm directory.
     * 
     * For example:
     * 
     * java-11-openjdk-11.0.9.11-0.el8_2.x86_64
     * 
     * java-1.8.0-openjdk-1.8.0.265.b01-1.el7_9.ppc64le
     */
    public static final String RH_RPM_DIR = "(" + JdkRegEx.RH_RPM_OPENJDK8_DIR + "|" + JdkRegEx.RH_RPM_OPENJDK11_DIR
            + "|" + JdkRegEx.RH_RPM_OPENJDK17_DIR + ")";

    /**
     * Red Hat OpenJDK 17 rpm directory.
     * 
     * For example:
     * 
     * java-11-openjdk-11.0.7.10-4.el7_8.x86_64
     * 
     * java-11-openjdk-11.0.9.11-2.el8_3.x86_64
     * 
     * java-11-openjdk-11.0.10.0.9-0.el7_9.x86_64
     * 
     * java-11-openjdk-11.0.10.0.9-8.el8.x86_64
     */
    public static final String RH_RPM_OPENJDK11_DIR = "(java\\-11\\-openjdk\\-11\\.0\\.\\d{1,2***REMOVED***\\.\\d{1,2***REMOVED***"
            + "(\\.\\d{1,2***REMOVED***)?-\\d\\.el([78])(_(\\d{1,2***REMOVED***))?\\.x86_64)";

    /**
     * Red Hat OpenJDK 11 rpm libjvm.so file path.
     * 
     * For example:
     * 
     * /usr/lib/jvm/java-11-openjdk-11.0.7.10-4.el7_8.x86_64/lib/server/libjvm.so
     */
    public static final String RH_RPM_OPENJDK11_LIBJVM_PATH = "^\\/usr\\/lib\\/jvm\\/" + JdkRegEx.RH_RPM_OPENJDK11_DIR
            + "\\/lib\\/server\\/libjvm\\.so$";

    /**
     * Red Hat OpenJDK 17 rpm directory.
     * 
     * For example:
     * 
     * java-17-openjdk-17.0.1.0.12-2.el8_5.x86_64
     */
    public static final String RH_RPM_OPENJDK17_DIR = "(java\\-17\\-openjdk\\-17\\.0\\.\\d{1,2***REMOVED***\\.\\d{1,2***REMOVED***"
            + "(\\.\\d{1,2***REMOVED***)?-\\d\\.el([8])_(\\d{1,2***REMOVED***)\\.x86_64)";

    /**
     * Red Hat OpenJDK 17 rpm libjvm.so file path.
     * 
     * For example:
     * 
     * /usr/lib/jvm/java-17-openjdk-17.0.1.0.12-2.el8_5.x86_64/lib/server/libjvm.so
     */
    public static final String RH_RPM_OPENJDK17_LIBJVM_PATH = "^\\/usr\\/lib\\/jvm\\/" + JdkRegEx.RH_RPM_OPENJDK17_DIR
            + "\\/lib\\/server\\/libjvm\\.so$";

    /**
     * Red Hat OpenJDK 8 rpm directory.
     * 
     * For example:
     * 
     * java-1.8.0-openjdk-1.8.0.131-11.b12.el7.x86_64
     * 
     * java-1.8.0-openjdk-1.8.0.262.b10-0.el6_10.x86_64
     * 
     * java-1.8.0-openjdk-1.8.0.265.b01-1.el7_9.ppc64le
     */
    public static final String RH_RPM_OPENJDK8_DIR = "(java\\-1\\.8\\.0\\-openjdk\\-1\\.8\\.0\\..+\\.el([678])"
            + "(_(\\d{1,2***REMOVED***))?\\.(ppc64(le)?|x86_64))";

    /**
     * Red Hat OpenJDK 8 rpm file path.
     * 
     * For example:
     * 
     * /usr/lib/jvm/java-1.8.0-openjdk-1.8.0.262.b10-0.el6_10.x86_64/jre/lib/amd64/server/libjvm.so
     * 
     * /usr/lib/jvm/java-1.8.0-openjdk-1.8.0.131-11.b12.el7.x86_64/jre/lib/amd64/server/libjvm.so
     * 
     * /usr/lib/jvm/java-1.8.0-openjdk-1.8.0.342.b07-1.el7_9.x86_64/jre/lib/amd64/server/libjvm.so
     * 
     * /usr/lib/jvm/java-1.8.0-openjdk-1.8.0.282.b08-1.el7_9.ppc64/jre/lib/ppc64/server/libjvm.so
     * 
     * /usr/lib/jvm/java-1.8.0-openjdk-1.8.0.265.b01-1.el7_9.ppc64le/jre/lib/ppc64le/server/libjvm.so
     */
    public static final String RH_RPM_OPENJDK8_LIBJVM_PATH = "\\/usr\\/lib\\/jvm\\/" + JdkRegEx.RH_RPM_OPENJDK8_DIR
            + "\\/jre\\/lib\\/(amd64|ppc64(le)?)\\/server\\/libjvm\\.so";
    /**
     * RHSSO thread used for <code>Application</code> identification.
     * 
     * For example:
     * 
     * 0x00005587a9039000 JavaThread "Brute Force Protector" [_thread_blocked, id=6073,
     * stack(0x00007f5abb897000,0x00007f5abb998000)]
     */
    public static final String RHSSO_THREAD = "^.+\"Brute Force Protector\".+$";

    /**
     * Regular expression for Shenandoah gc data.
     * 
     * <pre>
     * Shenandoah Heap
     *  5734M total, 5734M committed, 3099M used
     *  2867 x 2048K regions
     * Status: marking, not cancelled
     * Reserved region:
     *  - [0x000000067a200000, 0x00000007e0800000)
     * Collection set:
     *  - map (vanilla): 0x00007f2e5435b3d1
     *  - map (biased):  0x00007f2e54358000
     * </pre>
     * 
     * <pre>
     * 3456M max, 3456M soft max, 3200M committed, 2325M used
     * </pre>
     */
    public static final String SHENANDOAH = "(Shenandoah Heap|" + JdkRegEx.SHENANDOAH_SIZE + "| \\d{1,5***REMOVED*** x "
            + JdkRegEx.SIZE + " regions|Status:.+|Reserved region:| - \\[.+|Collection set:| - map.+)";

    /**
     * Regular expression for Shenandoah heap size data.
     * 
     * <pre>
     *  5734M total, 5734M committed, 3099M used
     * </pre>
     * 
     * <pre>
     * 3456M max, 3456M soft max, 3200M committed, 2325M used
     * </pre>
     */
    public static final String SHENANDOAH_SIZE = " " + JdkRegEx.SIZE + " (total|max)(, " + JdkRegEx.SIZE
            + " soft max)?, " + JdkRegEx.SIZE + " committed, " + JdkRegEx.SIZE + " used";

    /**
     * The size of memory in bytes (B), kilobytes (K), megabytes (M), or gigabytes (G) to a whole number or to one
     * decimal place.
     * 
     * See with G1 collector <code>-XX:+PrintGCDetails</code>.
     * 
     * With the G1 collector units are not consistent line to line or even within a single logging line.
     * 
     * Whole number examples: 2128K, 30M, 30G
     * 
     * Decimal examples: 0.0B, 8192.0K, 28.0M, 30.0G
     * 
     * With comma: 306,0M
     */
    public static final String SIZE = "(\\d{1,10***REMOVED***([\\.,]\\d)?)([" + BYTES + KILOBYTES + MEGABYTES + GIGABYTES + "])";

    /**
     * The size of memory in kilobytes (KB), megabytes (MB), or gigabytes (GB) to two decimal places. For example:
     * 
     * 3.00 KB, 395.36 MB, 1.00 GB
     */
    public static final String SIZE2 = "(\\d{1,***REMOVED***([\\.,]\\d{2***REMOVED***)?) (KB|MB|GB)";

    /**
     * Timestamp. Milliseconds since JVM started.
     * 
     * For example: 487.020
     */
    public static final String TIMESTAMP = "(\\d{0,12***REMOVED***[\\.\\,]\\d{3***REMOVED***)";

    /**
     * Tomcat jar used for <code>Application</code> identification.
     * 
     * For example:
     * 
     * 7f0c4b92c000-7f0c4b93e000 r--s 00183000 fd:04 51406344 /path/to/WEB-INF/lib/catalina.jar
     */
    public static final String TOMCAT_JAR = "^.+catalina\\.jar$";

    /**
     * Tomcat start main class used for <code>Application</code> identification.
     * 
     * For example:
     * 
     * org.apache.catalina.startup.Bootstrap start
     */
    public static final String TOMCAT_START_COMMAND = "^.*org\\.apache\\.catalina\\.startup\\.Bootstrap start$";

    /**
     * Tomcat stop main class used for <code>Application</code> identification.
     * 
     * For example:
     * 
     * org.apache.catalina.startup.Bootstrap stop stop
     */
    public static final String TOMCAT_STOP_COMMAND = "^.*org\\.apache\\.catalina\\.startup\\.Bootstrap stop.*$";

    /**
     * <p>
     * Regular expression for a JDK version string. Can be used for sorting purposes.
     * </p>
     * 
     * For example, given the following header:
     * 
     * <pre>
     * 7.0_85-b15
     * 8.0_131-b11
     * 11.0.9+11-LTS
     * 12.0.1+12
     * </pre>
     */
    public static final String VERSION_STRING = "((6.0|7.0|8.0|9|10|11|12|13|14|15|16|17)[^\\)]{1,***REMOVED***)";

    /**
     * JBoss jar used for <code>Application</code> identification.
     * 
     * For example:
     * 
     * 7fb99ed0d000-7fb99ed15000 r--s 0006e000 f9:00 792511 /path/to/jboss-eap-7.2/jboss-modules.jar
     */
    public static final String WILDFLY_JAR = "^.+jboss-modules\\.jar.*$";

    /**
     * Regular expression for a young generation gc data.
     * 
     * 1) <code>GarbageCollection.PARALLEL_SCAVENGE</code>:
     * 
     * PSYoungGen total 153088K, used 116252K [0x00000000eab00000, 0x00000000f5580000, 0x0000000100000000)
     * 
     * 2) <code>GarbageCollection.SERIAL</code>:
     * 
     * def new generation total 629440K, used 511995K [0x00000006c0000000, 0x00000006eaaf0000, 0x0000000715550000)
     * 
     * 3) <code>GarbageCollection.PAR_NEW</code>:
     * 
     * par new generation total 766784K, used 37193K [0x0000000261000000, 0x0000000295000000, 0x0000000295000000)
     */
    public static final String YOUNG_GEN = "(" + JdkRegEx.YOUNG_GEN_SIZE + "|  (eden|from|to).+)";

    /**
     * Regular expression for a young generation heap size data.
     * 
     * 1) <code>GarbageCollection.PARALLEL_SCAVENGE</code>:
     * 
     * PSYoungGen total 153088K, used 116252K [0x00000000eab00000, 0x00000000f5580000, 0x0000000100000000)
     * 
     * 2) <code>GarbageCollection.SERIAL</code>:
     * 
     * def new generation total 629440K, used 511995K [0x00000006c0000000, 0x00000006eaaf0000, 0x0000000715550000)
     * 
     * 3) <code>GarbageCollection.PAR_NEW</code>:
     * 
     * par new generation total 766784K, used 37193K [0x0000000261000000, 0x0000000295000000, 0x0000000295000000)
     */
    public static final String YOUNG_GEN_SIZE = " ((def|par) new generation|PSYoungGen)[ ]{1,6***REMOVED***total " + JdkRegEx.SIZE
            + ", used " + JdkRegEx.SIZE + ".+";
***REMOVED***
