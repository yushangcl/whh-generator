package cn.itbat.generator.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author huahui.wu
 * @date 2020年04月15日 16:33:39
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class  TableInfo implements Serializable {

    private String tableName;

    private String tableComment;

    private String beanName;

    private String shortBeanName;

    private String mapperName;

    private String xmlName;

    private String serviceName;

    private String serviceImplName;

    private String controllerName;

    private String fileds;

    private List<TableField> tableFields;

}
