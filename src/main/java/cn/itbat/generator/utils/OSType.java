package cn.itbat.generator.utils;



/**
 * Created by IntelliJ IDEA.
 * User: xj
 * Date: 14-4-1
 * Time: 上午10:49
 */
public enum OSType {

    WINDOWS(1, "windows"),
    UNIX(2, "unix"),
    POSIX_UNIX(3, "posix_unix"),
    OTHER(4, "other");


    private int code = 0;

    private String desc = "";

    OSType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

}
