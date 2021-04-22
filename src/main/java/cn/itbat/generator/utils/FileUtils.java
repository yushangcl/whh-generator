package cn.itbat.generator.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.regex.Pattern;

/**
 * @author huahui.wu
 * @date 2020年09月03日 09:31:29
 */
public class FileUtils {

    public static void main(String[] args) {
//        changeFileName("E:\\OneDrive - 懒人科技研究院\\电影\\单部电影");
//        changeFileName("E:\\OneDrive - 懒人科技研究院\\电影\\漫威电影");
//        changeFileName("E:\\OneDrive - 懒人科技研究院\\电影\\系列电影");
        deletedFile("E:\\Test");
    }

    /**
     * 通过文件路径，修改该路径下所有文件的名字
     *
     * @param path 文件夹路径
     * @date 2019/8/8 14:52
     */
    public static void changeFileName(String path) {
        File file = new File(path);
        if (!file.exists()) {
            System.out.println("该路径不存在");
            return;
        }
        File[] files = file.listFiles();
        if (null == files || files.length == 0) {
            System.out.println("文件夹是空的!");
            return;
        }
        for (File file2 : files) {
            if (file2.isDirectory()) {
                String dirName = getDirName(file2.getAbsolutePath());
                if (!StringUtils.isEmpty(dirName)) {
                    boolean b = file2.renameTo(new File(dirName));
                }
                changeFileName(dirName);
            } else {
                String filePath = file2.getAbsolutePath();
                File oriFile = new File(filePath);
                String fileName = filePath.substring(0, filePath.lastIndexOf("\\")) + "\\" + getFileName(oriFile.getName()) + filePath.substring(filePath.lastIndexOf("."));
                boolean b = oriFile.renameTo(new File(fileName));
                System.out.println("文件 " + fileName + " 重命名 ：" + b);
            }
        }
    }

    private static String getDirName(String filePath) {
        String startPath = filePath.substring(0, filePath.lastIndexOf("\\"));
        String[] split = filePath.split("\\\\");
        String p = "^[1-9]\\d*|0$";
        String[] strings = split[split.length - 1].split("\\.");
        if (strings.length > 0) {
            if (Pattern.matches(p, strings[0])) {
                return startPath + "\\" + strings[1];
            }
            if (strings[0].endsWith("1")) {
                return startPath + "\\" + strings[0].substring(0, strings[0].length() - 1);
            }
            return startPath + "\\" + strings[0];
        }
        return null;
    }

    private static String getFileName(String fileName) {
        String p = "^[1-9]\\d*|0$";
        String[] strings = fileName.split("\\.");
        if (strings.length > 0) {
            if (Pattern.matches(p, strings[0])) {
                return strings[1];
            }
            if (strings[0].endsWith("1")) {
                return strings[0].substring(0, strings[0].length() - 1);
            }
            return strings[0];
        }
        return null;
    }

    private static void deletedFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            System.out.println("该路径不存在");
            return;
        }
        File[] files = file.listFiles();
        if (null == files || files.length == 0) {
            System.out.println("文件夹是空的!");
            return;
        }
        for (File file2 : files) {
            if (file2.isDirectory()) {
                deletedFile(file2.getAbsolutePath());
            } else {
                String filePath = file2.getAbsolutePath();
                File oriFile = new File(filePath);
                if (".url".equals(filePath.substring(filePath.lastIndexOf("."))) || ".txt".equals(filePath.substring(filePath.lastIndexOf(".")))) {
                    boolean delete = oriFile.delete();
                    System.out.println("文件：" + oriFile.getName() + "删除：" + delete);
                }

            }
        }

    }
}


