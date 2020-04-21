package cn.itbat.generator.utils;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: wanzi
 * Date: 12-3-22
 * Time: 下午1:52
 * To change this template use File | Settings | File Templates.
 */
public class OSTypeUtil {

    private static int OS_TYPE = 0;

    private static String WIN_PATH_SEPARATOR = "\\";
    private static String UNIX_PATH_SEPARATOR = "/";

    static {
        initOSType();
    }

    public static void initOSType() {
        try {
            String osName = System.getProperty("os.name");
            if (osName == null) {
                throw new IOException("os.name not found");
            }
            osName = osName.toLowerCase();
            // match
            if (osName.indexOf("windows") != -1) {
                OS_TYPE = OSType.WINDOWS.getCode();
            } else if (osName.indexOf("linux") != -1 ||
                    osName.indexOf("sun os") != -1 ||
                    osName.indexOf("sunos") != -1 ||
                    osName.indexOf("solaris") != -1 ||
                    osName.indexOf("mpe/ix") != -1 ||
                    osName.indexOf("freebsd") != -1 ||
                    osName.indexOf("irix") != -1 ||
                    osName.indexOf("digital unix") != -1 ||
                    osName.indexOf("unix") != -1 ||
                    osName.indexOf("mac os x") != -1) {
                OS_TYPE = OSType.UNIX.getCode();
            } else if (osName.indexOf("hp-ux") != -1 ||
                    osName.indexOf("aix") != -1) {
                OS_TYPE = OSType.POSIX_UNIX.getCode();
            } else {
                OS_TYPE = OSType.OTHER.getCode();
            }

        } catch (Exception ex) {

        }
    }

    public static String getSeparator() {
        if (OS_TYPE == OSType.WINDOWS.getCode())
            return WIN_PATH_SEPARATOR;
        else
            return UNIX_PATH_SEPARATOR;
    }
}
