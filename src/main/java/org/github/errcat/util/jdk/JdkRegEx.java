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

/**
 * Regular expression utility methods and constants for OpenJDK and Oracle JDK.
 * 
 * @author <a href="mailto:mmillson@redhat.com">Mike Millson</a>
 * 
 */
public class JdkRegEx {

    /**
     * A 32-bit memory address.
     */
    public static final String ADDRESS32 = "((0x)?[0-9a-f]{8***REMOVED***)";

    /**
     * A 64-bit memory address.
     */
    public static final String ADDRESS64 = "((0x)?[0-9a-f]{16***REMOVED***)";

    /**
     * A 32-bit or 64-bit memory address.
     */
    public static final String ADDRESS = "(" + ADDRESS32 + "|" + ADDRESS64 + ")";

    /**
     * Memory map area.
     * 
     * For example: [vsyscall]
     */
    public static final String AREA = "(\\[(stack|vdso|vsyscall)\\])";

    /**
     * Blank line.
     */
    public static final String BLANK_LINE = "^\\s+$";

    /**
     * <p>
     * Regular expression for valid JDK build date/time in MMM d yyyy HH:mm:ss format (see <code>SimpleDateFormat</code>
     * for date and time pattern definitions).
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
     * Byte units identifier.
     */
    public static final String BYTES = "bB";

    /**
     * Major ID and minor ID of the device where the file is located.
     * 
     * Reference: https://www.kernel.org/doc/Documentation/admin-guide/devices.txt
     * 
     * For example:
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
     */
    public static final String DEVICE_IDS = "([0-9a-f]{2,3***REMOVED***:[0-9a-f]{2***REMOVED***)";

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
     * Gigabyte units identifier.
     */
    public static final String GIGABYTES = "gG";

    /**
     * File inode number
     * 
     * For example: 134326056
     */
    public static final String INODE = "([0-9]{1,10***REMOVED***)";

    /**
     * Regular expression for java.nio.ByteBuffer class.
     */
    public static final String JAVA_NIO_BYTEBUFFER = "java[\\.\\/]nio[\\.\\/]ByteBuffer";

    /**
     * JBoss jar used for <code>Application</code> identification.
     * 
     * For example:
     * 
     * 7fb99ed0d000-7fb99ed15000 r--s 0006e000 f9:00 792511 /path/to/jboss-eap-7.2/jboss-modules.jar
     */
    public static final String JBOSS_JAR = "^.+jboss-modules\\.jar$";

    /**
     * Kilobyte units identifier.
     */
    public static final String KILOBYTES = "kK";

    /**
     * Megabyte units identifier.
     */
    public static final String MEGABYTES = "mM";

    /**
     * Memory region
     * 
     * For example: 7f95caa94000-7f95caaa3000
     */
    public static final String MEMORY_REGION = "([0-9a-f]{8,16***REMOVED***-[0-9a-f]{8,16***REMOVED***)";

    /**
     * Units for JVM options that take a byte number.
     * 
     * For example: -Xss128k -Xmx2048m -Xms2G -XX:ThreadStackSize=256
     */
    public static final String OPTION_SIZE_BYTES = "((\\d{1,***REMOVED***)(b|B|k|K|m|M|g|G)?)";

    /**
     * Permission
     * 
     * For example: r--s
     */
    public static final String PERMISION = "([rwxps\\-]{4***REMOVED***)";

    /**
     * A 32-bit or 64-bit register.
     */
    public static final String REGISTER = "(CR2|CSGSFS|ctr|EFLAGS|EAX|EBP|EBX|ECX|EDI|EDX|EIP|ERR|ESI|ESP|lr |pc |RAX|"
            + "RBP|RBX|RCX|RDI|RDX|RIP|RSI|RSP|[Rr]" + "\\d{1,2***REMOVED***[ ]{0,1***REMOVED***)=(" + ADDRESS32 + "|" + ADDRESS64 + ")";

    /**
     * <p>
     * Regular expression for a JDK release string.
     * </p>
     * 
     * For example:
     * 
     * <pre>
     * 1.8.0_131-b11
     * 11.0.9+11-LTS
     * 12.0.1+12
     * </pre>
     */
    public static final String RELEASE_STRING = "(((1|9|10|11|12|13|14|15)\\.(0|6|7|8))\\.\\d.+)";

    /**
     * Red Hat OpenJDK 11 rpm version directory.
     * 
     * For example:
     * 
     * java-11-openjdk-11.0.7.10-4.el7_8.x86_64
     * 
     * java-11-openjdk-11.0.9.11-2.el8_3.x86_64
     */
    public static final String RH_RPM_OPENJDK11_DIR = "(java\\-11\\-openjdk\\-11\\.0\\.\\d\\.\\d{1,2***REMOVED***-\\d\\.el[78]"
            + "_\\d\\.x86_64)";

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
     * Red Hat OpenJDK 8 rpm version directory.
     * 
     * For example:
     * 
     * java-11-openjdk-11.0.9.11-0.el8_2.x86_64
     * 
     * java-1.8.0-openjdk-1.8.0.131-11.b12.el7.x86_64
     * 
     * java-1.8.0-openjdk-1.8.0.262.b10-0.el6_10.x86_64
     * 
     * java-1.8.0-openjdk-1.8.0.265.b01-1.el7_9.ppc64le
     */
    public static final String RH_RPM_OPENJDK8_DIR = "(java\\-1\\.8\\.0\\-openjdk\\-1\\.8\\.0\\..+\\.el[678]"
            + "(_\\d{1,2***REMOVED***)?\\.(ppc64le|x86_64))";

    /**
     * Red Hat OpenJDK 8 rpm file path.
     * 
     * For example:
     * 
     * /usr/lib/jvm/java-1.8.0-openjdk-1.8.0.262.b10-0.el6_10.x86_64/jre/lib/amd64/server/libjvm.so
     * 
     * /usr/lib/jvm/java-1.8.0-openjdk-1.8.0.131-11.b12.el7.x86_64/jre/lib/amd64/server/libjvm.so
     * 
     * /usr/lib/jvm/java-1.8.0-openjdk-1.8.0.265.b01-1.el7_9.ppc64le/jre/lib/ppc64le/server/libjvm.so
     */
    public static final String RH_RPM_OPENJDK8_LIBJVM_PATH = "\\/usr\\/lib\\/jvm\\/" + JdkRegEx.RH_RPM_OPENJDK8_DIR
            + "\\/jre\\/lib\\/(amd64|ppc64le)\\/server\\/libjvm\\.so";
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
***REMOVED***
