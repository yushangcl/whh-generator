package cn.itbat.generator.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 枚举字段
 *
 * @author huahui.wu
 * @date 2020年11月19日 09:38:28
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnumsField {

    private String key;

    private String value;

    private String desc;
}
