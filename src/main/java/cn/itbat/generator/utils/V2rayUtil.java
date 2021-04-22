package cn.itbat.generator.utils;

import org.apache.commons.net.ftp.FTPFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class V2rayUtil {
    private static final String VMESS = "vmess://";
    private static final String LOCATION = "hs";
    private static final String PATH = "D:\\App\\fping-msys2.0\\";

    public static void main(String[] args) {
        // 删除文件
        deletedFile();
        // 执行脚本
        exec();
        // 写入订阅信息
        writeFile(generator(readFile()));
        // 上传文件
        uploadFile();
    }


    private static void exec() {
        String cmd = "cmd /c start auto.bat ";
        try {
            Process process = Runtime.getRuntime().exec(cmd);
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        // 等待结束
        waiteFinish();
    }

    private static void waiteFinish() {
        File file = new File("速度排名-auto.txt");
        while (!file.exists()) {
            try {
                Thread.sleep(10000);
                System.out.println("未检测到文件：速度排名-auto.txt，继续等待10s");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("已检测到文件：速度排名-auto.txt");
    }

    private static void deletedFile() {
        File file = new File("速度排名-auto.txt");
        boolean delete = file.delete();
        System.out.println("删除文件：速度排名-auto.txt " + delete);
    }

    private static List<String> readFile() {
        File file = new File("速度排名-auto.txt");
        List<String> tempString = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String str = null;
            while ((str = br.readLine()) != null) {
                tempString.add(str);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tempString;
    }

    private static void writeFile(String content) {
        File file = new File("sub-hs.txt");
        try {
            if (!file.exists()) {
                boolean newFile = file.createNewFile();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(content.getBytes());
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String generator(List<String> ips) {
        String awsV21 = VMESS + Base64.getEncoder().encodeToString((awsV1Cf().replace("{name}", "aws-v1-cf-" + LOCATION + "-1").replace("{ip}", ips.get(1))).getBytes(StandardCharsets.UTF_8)) + "\n";
        String awsV22 = VMESS + Base64.getEncoder().encodeToString((awsV1Cf().replace("{name}", "aws-v1-cf-" + LOCATION + "-2").replace("{ip}", ips.get(2))).getBytes(StandardCharsets.UTF_8)) + "\n";
        String awsV23 = VMESS + Base64.getEncoder().encodeToString((awsV1Cf().replace("{name}", "aws-v1-cf-" + LOCATION + "-3").replace("{ip}", ips.get(3))).getBytes(StandardCharsets.UTF_8)) + "\n";
//        String ibmV3 = VMESS + Base64.getEncoder().encodeToString((ibmV3().replace("{name}", "ibm-v3-" + LOCATION).replace("{ip}", ips.get(0))).getBytes(StandardCharsets.UTF_8)) + "\n";
//        String ibmV4 = VMESS + Base64.getEncoder().encodeToString((ibmV4().replace("{name}", "ibm-v4-" + LOCATION).replace("{ip}", ips.get(0))).getBytes(StandardCharsets.UTF_8)) + "\n";
//        String awsV1 = VMESS + Base64.getEncoder().encodeToString((awsV1()).getBytes(StandardCharsets.UTF_8)) + "\n";
//        String awsV2 = VMESS + Base64.getEncoder().encodeToString((awsV2()).getBytes(StandardCharsets.UTF_8)) + "\n";
        String awsV1Cf = VMESS + Base64.getEncoder().encodeToString((awsV1Cf().replace("{name}", "aws-v1-cf-" + LOCATION).replace("{ip}", ips.get(0))).getBytes(StandardCharsets.UTF_8)) + "\n";
        String awsV2Cf = VMESS + Base64.getEncoder().encodeToString((awsV2Cf().replace("{name}", "aws-v2-cf-" + LOCATION).replace("{ip}", ips.get(0))).getBytes(StandardCharsets.UTF_8)) + "\n";
        return Base64.getEncoder().encodeToString((awsV21 + awsV22 + awsV23
//                + ibmV3
//                + ibmV4
                + awsV1Cf
                + awsV2Cf
//                + awsV1
//                + awsV2
        ).getBytes(StandardCharsets.UTF_8));
    }

    private static String addPrefix(String str) {
        return VMESS + str;
    }

    private static void uploadFile() {
        FTPClientUtil f = new FTPClientUtil("119.28.188.141", 21, "sub", "ydr6SAhc5DNHMf5f");
        try {
            if (f.open()) {
                // 远程路径为相对路径
                FTPFile[] fileList = f.getFileList("");
                for (FTPFile ftpFile : fileList) {
                    System.out.println(ftpFile.getName());
                }
                boolean put = f.put("sub-" + LOCATION + ".txt", "sub-" + LOCATION + ".txt", "");
                System.out.println("文件上传：" + put);
                f.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String awsV1() {
        return "{\n" +
                "\"v\": \"2\",\n" +
                "\"ps\": \"aws-1\",\n" +
                "\"add\": \"aws-1.itbat.cn\",\n" +
                "\"port\": \"443\",\n" +
                "\"id\": \"1a434649-5491-4653-ad1b-e960860d95e6\",\n" +
                "\"aid\": \"233\",\n" +
                "\"net\": \"ws\",\n" +
                "\"type\": \"none\",\n" +
                "\"host\": \"aws-1.itbat.cn\",\n" +
                "\"path\": \"/testintery\",\n" +
                "\"tls\": \"tls\"\n" +
                "}";
    }

    private static String awsV2() {
        return "{\n" +
                "\"v\": \"2\",\n" +
                "\"ps\": \"aws-2\",\n" +
                "\"add\": \"aws-2.itbat.cn\",\n" +
                "\"port\": \"443\",\n" +
                "\"id\": \"305793e1-ff9e-46ed-9d5d-8b1614dad2d6\",\n" +
                "\"aid\": \"0\",\n" +
                "\"net\": \"ws\",\n" +
                "\"type\": \"none\",\n" +
                "\"host\": \"aws-2.itbat.cn\",\n" +
                "\"path\": \"/detail\",\n" +
                "\"tls\": \"tls\"\n" +
                "}";
    }

    private static String awsV1Cf() {
        return "{ \n" +
                "  \"v\": \"2\", \n" +
                "  \"ps\": \"{name}\", \n" +
                "  \"add\": \"{ip}\", \n" +
                "  \"port\": \"443\", \n" +
                "  \"id\": \"1a434649-5491-4653-ad1b-e960860d95e6\", \n" +
                "  \"aid\": \"233\", \n" +
                "  \"net\": \"ws\", \n" +
                "  \"type\": \"none\", \n" +
                "  \"host\": \"aws-1.gdd.workers.dev\", \n" +
                "  \"path\": \"/testintery\", \n" +
                "  \"tls\": \"tls\" \n" +
                "}";
    }

    private static String awsV2Cf() {
        return "{ \n" +
                "  \"v\": \"2\", \n" +
                "  \"ps\": \"{name}\", \n" +
                "  \"add\": \"{ip}\", \n" +
                "  \"port\": \"443\", \n" +
                "  \"id\": \"305793e1-ff9e-46ed-9d5d-8b1614dad2d6\", \n" +
                "  \"aid\": \"0\", \n" +
                "  \"net\": \"ws\", \n" +
                "  \"type\": \"none\", \n" +
                "  \"host\": \"aws-2.gdd.workers.dev\", \n" +
                "  \"path\": \"/detail\", \n" +
                "  \"tls\": \"tls\" \n" +
                "}";
    }
}
