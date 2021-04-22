package cn.itbat.generator.utils;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class FTPClientUtil {
    private FTPClient ftpClient = null;
    private final String server;
    private final int port;
    private final String userName;
    private final String userPassword;

    public static void main(String[] args) {
        FTPClientUtil f = new FTPClientUtil("119.28.188.141", 21, "sub", "ydr6SAhc5DNHMf5f");
        try {
            if (f.open()) {
                // 远程路径为相对路径
                boolean put = f.put("D:\\App\\fping-msys2.0\\sub-hs.txt", "sub-hs.txt", "");
                System.out.println("文件上传：" + put);
                f.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public FTPClientUtil(String server, int port, String userName, String userPassword) {
        this.server = server;
        this.port = port;
        this.userName = userName;
        this.userPassword = userPassword;
    }

    /**
     * 链接到服务器
     *
     * @return 成功/失败
     */
    public boolean open() {
        if (ftpClient != null && ftpClient.isConnected()) {
            return true;
        }
        try {
            ftpClient = new FTPClient();
            // 连接
            ftpClient.connect(this.server, this.port);
            ftpClient.login(this.userName, this.userPassword);
            // 检测连接是否成功
            int reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                this.close();
                System.exit(1);
            }
            // 设置上传模式binally or ascii
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            return true;
        } catch (Exception ex) {
            // 关闭
            this.close();
            return false;
        }

    }

    private boolean cd(String dir) throws IOException {
        return ftpClient.changeWorkingDirectory(dir);
    }

    /**
     * 获取目录下所有的文件名称
     *
     * @param filePath 文件目录
     * @return FTPFile[]
     */
    public FTPFile[] getFileList(String filePath) throws IOException {
        return ftpClient.listFiles(filePath);
    }

    /**
     * 上传文件到FTP服务器
     *
     * @param localDirectoryAndFileName 本地文件目录和文件名
     * @param ftpFileName               上传后的文件名
     * @param ftpDirectory              FTP目录如:/path1/pathb2/,如果目录不存在回自动创建目录
     */
    public boolean put(String localDirectoryAndFileName, String ftpFileName, String ftpDirectory) {
        if (!ftpClient.isConnected()) {
            return false;
        }
        boolean flag = false;
        if (ftpClient != null) {
            File srcFile = new File(localDirectoryAndFileName);
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(srcFile);
                ftpClient.setBufferSize(1024);
                ftpClient.setControlEncoding("UTF-8");
                // 设置文件类型（二进制）
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                // 上传
                flag = ftpClient.storeFile(new String(ftpFileName.getBytes(), StandardCharsets.UTF_8), fis);
            } catch (Exception e) {
                e.printStackTrace();
                this.close();
                return false;
            } finally {
                this.close();
            }
        }
        return flag;
    }

    /**
     * 删除FTP目录
     *
     * @param ftpDirectory 远程文件
     */
    public boolean deleteDirectory(String ftpDirectory) {
        return ftpClient.isConnected();
    }

    /**
     * 关闭链接
     */
    public void close() {
        try {
            if (ftpClient != null && ftpClient.isConnected()) {
                ftpClient.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public FTPClient getFtpClient() {
        return ftpClient;
    }

    public void setFtpClient(FTPClient ftpClient) {

        this.ftpClient = ftpClient;
    }
}