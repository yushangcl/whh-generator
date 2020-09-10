package cn.itbat.generator.plugin;

import cn.itbat.generator.enums.DbColumnTypeEnum;
import cn.itbat.generator.model.TableField;
import cn.itbat.generator.model.TableInfo;
import cn.itbat.generator.utils.DateUtil;
import cn.itbat.generator.utils.OSTypeUtil;
import cn.itbat.generator.utils.ScsUtil;
import cn.itbat.generator.utils.VelocityUtil;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.VelocityContext;

import java.io.IOException;
import java.sql.*;
import java.util.Date;
import java.util.*;

/**
 * @author huahui.wu
 * @date 2020年04月14日 18:31:14
 */
public class EasyGenerator {

    @Getter
    private String prefixPath;
    private String apiPath;
    private String webPath;
    private String xmlPath;
    private String beanPath;
    private String voPath;
    private String mapperPath;
    private String servicePath;
    private String serviceImplPath;
    private String controllerPath;
    private String criteriaPath;
    private String enumsPath;


    private String base_package;

    private Connection conn = null;
    private String tbName;

    private TableInfo tableInfo;
    private VelocityContext context;
    private String[] methods;


    public EasyGenerator() {
        try {
            init();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void init() throws ClassNotFoundException, SQLException {
        Properties generatorProps;
        generatorProps = new Properties();
        try {
            generatorProps.load(EasyGenerator.class.getResourceAsStream("/env.properties"));
        } catch (IOException e) {
            System.err.println("读取配置文件错误！");
        }
        String model = generatorProps.getProperty("model");
        base_package = generatorProps.getProperty("package");
        String basePackage = generatorProps.getProperty("base_package").replace("{model}", model);
        prefixPath = generatorProps.getProperty("prefix_path");
        apiPath = prefixPath + generatorProps.getProperty("api_path").replace("{model}", model);
        webPath = prefixPath + generatorProps.getProperty("web_path").replace("{model}", model);
        xmlPath = webPath + OSTypeUtil.getSeparator() + "src" + OSTypeUtil.getSeparator() + "main" + OSTypeUtil.getSeparator() + "resources" + OSTypeUtil.getSeparator() + "mapper";
        webPath = webPath + OSTypeUtil.getSeparator() + "src" + OSTypeUtil.getSeparator() + "main" + OSTypeUtil.getSeparator() + "java" + OSTypeUtil.getSeparator() + basePackage.replace(".", OSTypeUtil.getSeparator());
        apiPath = apiPath + OSTypeUtil.getSeparator() + "src" + OSTypeUtil.getSeparator() + "main" + OSTypeUtil.getSeparator() + "java" + OSTypeUtil.getSeparator() + basePackage.replace(".", OSTypeUtil.getSeparator());

        beanPath = apiPath + OSTypeUtil.getSeparator() + "base" + OSTypeUtil.getSeparator() + "model";
        enumsPath = apiPath + OSTypeUtil.getSeparator() + "base" + OSTypeUtil.getSeparator() + "enums";
        voPath = apiPath + OSTypeUtil.getSeparator() + "base" + OSTypeUtil.getSeparator() + "vo";
        criteriaPath = apiPath + OSTypeUtil.getSeparator() + "base" + OSTypeUtil.getSeparator() + "criteria";
        mapperPath = webPath + OSTypeUtil.getSeparator() + "mapper";
        servicePath = webPath + OSTypeUtil.getSeparator() + "service";
        serviceImplPath = servicePath + OSTypeUtil.getSeparator() + "impl";
        controllerPath = webPath + OSTypeUtil.getSeparator() + "controller";

        String driverName = generatorProps.getProperty("driverName");
        String user = generatorProps.getProperty("user");
        String password = generatorProps.getProperty("password");
        String url = generatorProps.getProperty("url").replace("{model}", model);
        tbName = generatorProps.getProperty("table_name");
        Class.forName(driverName);
        conn = DriverManager.getConnection(url, user, password);

        String authorName = generatorProps.getProperty("authorName");
        String version = generatorProps.getProperty("version");

        // 初始化 VelocityContext 信息
        context = new VelocityContext();
        context.put("base_package", base_package);
        context.put("commonPackage", base_package + ".common");
        context.put("beanPackage", basePackage + ".base.model");
        context.put("voPackage", basePackage + ".base.vo");
        context.put("criteriaPackage", basePackage + ".base.criteria");
        context.put("mapperPackage", basePackage + ".mapper");
        context.put("servicePackage", basePackage + ".service");
        context.put("controllerPackage", basePackage + ".controller");
        context.put("apiPackage", basePackage + ".api");
        context.put("enumPackage", basePackage + ".base.enums");
        context.put("model", model);
        context.put("version", version);
        context.put("authorName", authorName);
        context.put("date", DateUtil.format(new Date(), DateUtil.YYMMDD_HHmmSS));

        String methodStr = generatorProps.getProperty("method");
        if (StringUtils.isNotBlank(methodStr)) {
            methods = methodStr.split(";");
            context.put("methods", methods);
        }

    }

    /**
     * 转换实体名称
     *
     * @param table 表名
     */
    private String processTableName(String table) {
        StringBuilder sb = new StringBuilder(table.length());
        String tableNew = table.toLowerCase();
        String[] tables = tableNew.split("_");
        String temp;
        for (String table1 : tables) {
            temp = table1.trim();
            sb.append(temp.substring(0, 1).toUpperCase()).append(temp.substring(1));
        }
        return sb.toString();
    }

    /**
     * 首字母转小写
     *
     * @return String
     */
    private String shotFirst(String str) {
        char[] chars = new char[1];
        chars[0] = str.charAt(0);
        String temp = new String(chars);
        if (chars[0] >= 'A' && chars[0] <= 'Z') {
            str = str.replaceFirst(temp, temp.toLowerCase());
        }
        return str;
    }

    /**
     * 字段类型转换
     *
     * @param type 表类型
     * @return java类型
     */
    private String processType(String type) {
        return DbColumnTypeEnum.getStatusByCode(type);
    }

    /**
     * 驼峰命名
     *
     * @param field 字段
     * @return String
     */
    private String processField(String field) {
        StringBuilder sb = new StringBuilder(field.length());
        String[] fields = field.split("_");
        String temp;
        sb.append(fields[0]);
        for (int i = 1; i < fields.length; i++) {
            temp = fields[i].trim();
            sb.append(temp.substring(0, 1).toUpperCase()).append(temp.substring(1));
        }
        return sb.toString();
    }

    /**
     * 首字母大写
     *
     * @param field 字段
     * @return String
     */
    private String processFieldUp(String field) {
        field = this.processField(field);
        char[] charArray = field.toCharArray();
        charArray[0] -= 32;
        return String.valueOf(charArray);
    }


    /**
     * 生成实体类
     */
    private void buildEntityBean() {
        context.put("tableInfo", tableInfo);
        VelocityUtil.generate("template/Entity.vm", beanPath + "\\" + tableInfo.getBeanName() + ".java", context);
    }

    /**
     * 构建VO文件
     */
    private void buildVo() {
        context.put("tableInfo", tableInfo);
        VelocityUtil.generate("template/EntityVo.vm", voPath + "\\" + tableInfo.getBeanName() + "Vo.java", context);
    }

    private void buildCriteria() {
        context.put("tableInfo", tableInfo);
        VelocityUtil.generate("template/Criteria.vm", criteriaPath + "\\" + tableInfo.getBeanName() + "Criteria.java", context);
    }

    /**
     * 构建Mapper文件
     */
    private void buildMapper() {
        context.put("tableInfo", tableInfo);
        VelocityUtil.generate("template/Mapper.vm", mapperPath + "\\" + tableInfo.getBeanName() + "Mapper.java", context);
    }

    /**
     * 构建实体类映射XML文件
     */
    private void buildMapperXml() {
        context.put("tableInfo", tableInfo);
        VelocityUtil.generate("template/Mapper.xml.vm", xmlPath + "\\" + tableInfo.getBeanName() + "Mapper.xml", context);
    }

    /**
     * 构建Service文件
     */
    private void buildService() {
        context.put("tableInfo", tableInfo);
        VelocityUtil.generate("template/service.vm", servicePath + "\\" + tableInfo.getBeanName() + "Service.java", context);
    }

    /**
     * 构建ServiceImpl文件
     */
    private void buildServiceImpl() {
        context.put("tableInfo", tableInfo);
        // 是否 分布式锁
        context.put("lock", true);
        VelocityUtil.generate("template/serviceImpl.vm", serviceImplPath + "\\" + tableInfo.getBeanName() + "ServiceImpl.java", context);
    }

    /**
     * 构建Controller文件
     */
    private void buildController() {
        if (tableInfo.getBeanName().contains("Detail")) {
            return;
        }
        context.put("tableInfo", tableInfo);
        VelocityUtil.generate("template/Controller.vm", controllerPath + "\\" + tableInfo.getBeanName() + "Controller.java", context);
    }

    /**
     * 构建API文件
     */
    private void buildApi() {
        context.put("tableInfo", tableInfo);
        VelocityUtil.generate("template/api.vm", apiPath + "\\api\\" + tableInfo.getBeanName() + "Api.java", context);
    }

    // 是否生成枚举


    /**
     * 构建Enums文件
     */
    private void buildEnum() {
        context.put("tableInfo", tableInfo);
        for (TableField tableField : tableInfo.getTableFields()) {
            if (tableField.getFiled().equals("status")) {
                context.put("enum", "Status");
                VelocityUtil.generate("template/Enum.vm", enumsPath + "\\" + tableInfo.getBeanName() + "StatusEnum.java", context);
            }
            if (tableField.getFiled().equals("type")) {
                context.put("enum", "Type");
                VelocityUtil.generate("template/Enum.vm", enumsPath + "\\" + tableInfo.getBeanName() + "TypeEnum.java", context);
            }
        }
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

    /**
     * 获取所有的数据库表注释
     *
     * @return 表注释
     */
    private Map<String, String> getTableComment() {
        Map<String, String> maps = new HashMap<String, String>();
        try {
            PreparedStatement pstate = conn.prepareStatement("show table status");
            ResultSet results = pstate.executeQuery();
            while (results.next()) {
                String tableName = results.getString("NAME");
                String comment = results.getString("COMMENT");
                maps.put(tableName, comment);
            }
        } catch (Exception e) {
            System.out.println("=============erro: " + e + "=======================");
        }

        return maps;
    }


    private void generate(String tableName) {
        String prefix = "show full fields from ";
        PreparedStatement pstate;
        Map<String, String> tableComments = getTableComment();
        for (String table : getTables()) {
            // 多表
            if ("".equals(tableName) || !table.equals(tableName)) {
                continue;
            }
            // 明细问题处理
            if (tableName.contains("detail")) {
                context.put("methods", null);
            } else {
                context.put("methods", methods);
            }
            tableInfo = new TableInfo();
            List<TableField> tableFields = new ArrayList<>();
            try {
                pstate = conn.prepareStatement(prefix + table);
                ResultSet results = pstate.executeQuery();
                while (results.next()) {
                    tableFields.add(TableField.builder()
                            .filed(results.getString("FIELD"))
                            .property(processField(results.getString("FIELD")))
                            .upFiled(processFieldUp(results.getString("FIELD")))
                            .type(processType(results.getString("TYPE")))
                            .comment(results.getString("COMMENT")).build());
                }
            } catch (Exception e) {
                System.out.println("=============error: " + e + "==================");
            }
            // 设置Table的属性
            tableInfo.setTableName(table);
            tableInfo.setTableComment(tableComments.get(table));
            tableInfo.setBeanName(processTableName(table));
            tableInfo.setShortBeanName(this.shotFirst(tableInfo.getBeanName()));
            tableInfo.setTableFields(tableFields);
            StringBuilder fileds = new StringBuilder();
            for (TableField tableField : tableFields) {
                fileds.append("`").append(tableField.getFiled()).append("`").append(",");
            }
            tableInfo.setFileds(fileds.toString().substring(0, fileds.toString().length() - 1));
        }
    }

    public enum TypeEnum {
        /**
         * all
         */
        all,
        /**
         * criteria
         */
        criteria,
        /**
         * bean
         */
        bean,
        /**
         * mapper
         */
        mapper,
        /**
         * dao
         */
        dao,
        /**
         * service
         */
        service,
        /**
         * controller
         */
        controller,
        /**
         * api
         */
        api,
        /**
         * update
         */
        update;
    }

    public void execute(TypeEnum typeEnum) {
        String[] strings = tbName.split(";");
        for (String tableName : strings) {
            generate(tableName);
            try {
                switch (typeEnum.ordinal()) {
                    case 0:
                        //all
                        buildCriteria();
                        buildEntityBean();
                        buildEnum();
                        buildVo();
                        buildMapper();
                        buildMapperXml();
                        buildService();
                        buildServiceImpl();
                        buildController();
//                        buildApi();
                        break;
                    case 1:
                        //criteria
                        buildCriteria();
                        break;
                    case 2:
                        //bean
                        buildEntityBean();
                        break;
                    case 3:
                        //mapper
                        buildMapperXml();
                        break;
                    case 4:
                        //dao
                        break;
                    case 5:
                        //service
                        buildService();
                        buildServiceImpl();
                        break;
                    case 6:
                        //controller
                        buildController();
                        break;
                    case 7:
                        //api
                        buildApi();
                        break;
                    case 8:
                        //update
                        buildCriteria();
                        buildEntityBean();
//                        buildMapper();
                        buildMapperXml();
                        break;
                    default:
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("==================" + tableInfo.getBeanName() + " error:" + e + "==================");
            }
            System.out.println("===============" + tableInfo.getBeanName() + " success !!!!===============");
        }
//        ScsUtil.afterHandler(this);
        try {
            conn.close();
        } catch (Exception e) {
            System.out.println("===============error: " + e + "===============");
        }
    }

}
