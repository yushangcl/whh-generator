package cn.itbat.generator.enums;

import lombok.Getter;

/**
 * 操作方法
 *
 * @author huahui.wu
 * @date 2020年04月17日 14:27:22
 */
@Getter
public enum ServiceMethodEnum {

    /**
     * 列表查询
     */
    LIST("list", "列表查询"),


    /**
     * 详情查询
     */
    GET_INFO("getInfo", "详情查询"),

    /**
     * 导出
     */
    EXPORT("export", "导出"),


    /**
     * 保存/编辑
     */
    SAVE("save", "保存/编辑"),


    /**
     * 删除
     */
    DELETED("deleted", "删除"),



    ;
    private String code;

    private String value;

    private ServiceMethodEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public static ServiceMethodEnum parse(String code) {
        for (ServiceMethodEnum item : values()) {
            if (item.code.equals(code)) {
                return item;
            }
        }
        return null;
    }

    public static String getStatusByCode(String code) {
        for (ServiceMethodEnum item : values()) {
            if (code.contains(item.code)) {
                return item.value;
            }
        }
        return null;
    }
}
