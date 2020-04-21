package cn.itbat.generator.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 方法
 * @author huahui.wu
 * @date 2020年04月17日 15:16:01
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceMethod {

    private String list;
    private String listInfo;
    private String export;
    private String save;
    private String deleted;
}
