package io.renren.modules.rna.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Hero
 * @description todo
 * @create 2021-09-03
 **/
@Data
@Accessors(chain = true)
public class RnaInfoCategoryZTree {

    private String id ;
    private String pid ;
    private String name ;
    private String category;
    private Boolean open = Boolean.FALSE;
}
