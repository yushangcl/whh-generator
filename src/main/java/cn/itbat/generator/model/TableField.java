package cn.itbat.generator.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author huahui.wu
 * @date 2020年04月15日 15:06:16
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TableField {
    /**
     * 字段名
     */
    private String filed;

    /**
     * Java属性
     */
    private String property;

    /**
     * 字段类型
     */
    private String type;

    /**
     * 注释
     */
    private String comment;

    /**
     * 字段名 首字母大写
     */
    private String upFiled;

}
