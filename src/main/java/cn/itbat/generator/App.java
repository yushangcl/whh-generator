package cn.itbat.generator;

import cn.itbat.generator.plugin.Demo;
import cn.itbat.generator.plugin.EasyGenerator;

/**
 * 代码生成工具
 *
 * @author huahui.wu
 * @date 2020年04月14日 16:55:02
 */
public class App {
    public static void main(String[] args) {
        EasyGenerator easyGenerator = new EasyGenerator();
//        easyGenerator.execute(EasyGenerator.TypeEnum.service);
//        easyGenerator.execute(EasyGenerator.TypeEnum.update);
                easyGenerator.execute(EasyGenerator.TypeEnum.all);
//        easyGenerator.execute(EasyGenerator.TypeEnum.controller);

//        Demo demo = new Demo();
//        demo.exec();
    }
}