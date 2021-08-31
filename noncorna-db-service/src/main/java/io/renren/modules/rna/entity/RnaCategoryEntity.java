package io.renren.modules.rna.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @className: TypeEntity
 * @description: TODO 类描述
 * @author: hy
 * @date: 2020-10-21
 **/
@Data
@Accessors(chain = true)
public class RnaCategoryEntity {
    /**
     * 分类 ： cancer type  || GenType
     */
    private String category;
    private Integer dataNum;
}
