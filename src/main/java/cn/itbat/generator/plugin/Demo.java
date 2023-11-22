package cn.itbat.generator.plugin;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author huahui.wu
 * @date 2022年06月30日 10:38:30
 */
public class Demo {

    private Connection conn = null;
    private String tbName;

    private void init() throws ClassNotFoundException, SQLException {
        Properties generatorProps;
        generatorProps = new Properties();
        try {
            generatorProps.load(EasyGenerator.class.getResourceAsStream("/env-sat.properties"));
        } catch (IOException e) {
            System.err.println("读取配置文件错误！");
        }

        String model = generatorProps.getProperty("model");
        String driverName = generatorProps.getProperty("driverName");
        String user = generatorProps.getProperty("user");
        String password = generatorProps.getProperty("password");
        String url = generatorProps.getProperty("url").replace("{model}", model);
        tbName = generatorProps.getProperty("table_name");
        Class.forName(driverName);
        conn = DriverManager.getConnection(url, user, password);

    }

    /**
     * 获取所有的表
     *
     * @return 表名称
     */
    private List<String> getTables() {
        List<String> tables = new ArrayList<>();
        try {
            PreparedStatement pstate = conn.prepareStatement("show tables");
            ResultSet results = pstate.executeQuery();
            while (results.next()) {
                String tableName = results.getString(1);
                tables.add(tableName);
            }
        } catch (Exception e) {
            System.out.println("  ================ error :" + e + "===================");
        }
        return tables;
    }

    private void updateType(String sql) {
        try {
            PreparedStatement pstate = conn.prepareStatement(sql);
            ResultSet results = pstate.executeQuery();

        } catch (Exception e) {
            System.out.println("  ================ error :" + e + "===================");
        }
    }

    private void generate(String tableName) {
        String prefix = "show full fields from ";
        PreparedStatement pstate;
        for (String table : getTables()) {
            System.out.println("\n");
            System.out.println("表：" + table);
            // 多表
//            if ("".equals(tableName) || !table.equals(tableName)) {
//                continue;
//            }
            String sql = "ALTER TABLE `wms`.`%s`\n" +
                    "MODIFY COLUMN `%s` decimal(32, %d) NULL DEFAULT NULL COMMENT '%s';";
            try {
                pstate = conn.prepareStatement(prefix + table);
                ResultSet results = pstate.executeQuery();
                while (results.next()) {
                    String type = results.getString("TYPE");
                    if (!type.contains("decimal") || !type.contains(",3")) {
                        continue;
                    }
                    String field = results.getString("FIELD");
                    String comment = results.getString("COMMENT");
                    System.out.println("\n字段：" + field + ", 备注：" + comment + ", 类型：" + type);
                    String format = String.format(sql, table, field, 4, comment);
                    System.out.println("\n执行sql ：");
                    System.out.println(format);
                    updateType(sql);
                }
            } catch (Exception e) {
                System.out.println("=============error: " + e + "==================");
            }

        }
    }

    public void exec() {
        try {
            init();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        generate(tbName);
    }
}
