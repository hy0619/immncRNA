package io.renren.modules.rna.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @className: RnaInfoCateGoryTree
 * @description: TODO 类描述
 * @author: hy
 * @date: 2020-10-20
 **/
@Data
@Accessors(chain = true)
public class RnaInfoCateGoryTree {

    private String id ;
    private String label;
    private String category;
    private List<RnaInfoCateGoryTree> children;


}
