package cn.itbat.generator.utils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author huahui.wu
 * @date 2021年01月07日 09:21:51
 */
public class PingUtils {
    /**
     * 通过域名获取这个域名所在的IP
     *
     * @param hostName 域名
     * @param timeout  超时
     */
    public static String getIPByHostName(String hostName, int timeout) {
        try {
            InetAddress address = InetAddress.getByName(hostName);
            try {
                if (address.isReachable(timeout)) {
                    return address.getHostAddress();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println((getIPByHostName("frp.itbat.cn", 100)));
    }
}
