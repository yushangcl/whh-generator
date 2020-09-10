package cn.itbat.generator.utils;

import cn.itbat.generator.plugin.EasyGenerator;

import java.io.File;
import java.io.IOException;

/**
 * @author zhaoliwe
 */
public class ScsUtil {



    public static void afterHandler(EasyGenerator easyGenerator) {
        try {
            java.awt.Desktop.getDesktop().open(new File(easyGenerator.getPrefixPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
