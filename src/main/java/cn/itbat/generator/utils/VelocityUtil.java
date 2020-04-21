package cn.itbat.generator.utils;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


/**
 * Velocity工具类
 *
 * @author huahui.wu
 * @date 2020年04月14日 16:55:02
 */
public class VelocityUtil {

    /**
     * 根据模板生成文件
     *
     * @param vmFilePath     模板路径
     * @param outputFilePath 输出文件路径
     * @param context        内容
     */
    public static void generate(String vmFilePath, String outputFilePath, VelocityContext context) {
        VelocityEngine velocityEngine = new VelocityEngine();
        velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        velocityEngine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        velocityEngine.init();
        // 获取模板文件
        Template template = velocityEngine.getTemplate(vmFilePath);
        try {
            File outputFile = new File(outputFilePath);
            if (!outputFile.getParentFile().exists()) {
                outputFile.getParentFile().mkdirs();
            }
            FileWriter writer = new FileWriter(outputFile);
            template.merge(context, writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
