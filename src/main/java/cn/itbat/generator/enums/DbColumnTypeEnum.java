package cn.itbat.generator.enums;


import lombok.Getter;

/**
 * @author huahui.wu
 * @date 2020年04月15日 14:24:35
 */
@Getter
public enum DbColumnTypeEnum {
    /**
     * TYPE_CHAR
     */
    TYPE_CHAR("char", "String"),

    /**
     * TYPE_BIGINT
     */
    TYPE_BIGINT("bigint", "Long"),

    /**
     * TYPE_INT
     */
    TYPE_INT("int", "Integer"),

    /**
     * TYPE_DATE
     */
    TYPE_DATE("date", "Date"),

    /**
     * TYPE_TEXT
     */
    TYPE_TEXT("text", "String"),

    /**
     * TYPE_TIMESTAMP
     */
    TYPE_TIMESTAMP("timestamp", "Date"),

    /**
     * TYPE_BIT
     */
    TYPE_BIT("bit", "Boolean"),

    /**
     * TYPE_DECIMAL
     */
    TYPE_DECIMAL("decimal", "BigDecimal"),

    /**
     * TYPE_BLOB
     */
    TYPE_BLOB("blob", "byte[]"),

    /**
     * TYPE_DOUBLE
     */
    TYPE_DOUBLE("double", "Double"),

    /**
     * TYPE_FLOAT
     */
    TYPE_FLOAT("float", "Float"),
    ;

    private String code;

    private String value;

    private DbColumnTypeEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return this.code;
    }

    public String getValue() {
        return this.value;
    }

    public static DbColumnTypeEnum parse(String code) {
        for (DbColumnTypeEnum item : values()) {
            if (item.code.equals(code)) {
                return item;
            }
        }
        return null;
    }

    public static String getStatusByCode(String code) {
        for (DbColumnTypeEnum item : values()) {
            if (code.contains(item.code)) {
                return item.value;
            }
        }
        return null;
    }
}
