package io.renren.modules.web.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

/**
 * @className: RnaWebNavEntity
 * @description: 网站首页导航
 * @author: hy
 * @date: 2020-10-4
 **/
@Data
@TableName("rna_web_nav")
public class RnaWebNavEntity implements Serializable {
    /**
     * id
     */
    @TableId
    private Long navId;
    /**
     * 父id
     */
    private Long parentId;
    /**
     * 名称
     */
    @NotBlank(message = "名称不能为空")
    private String name;
    /**
     * url
     */
    private String url ;
    /**
     * 类型 ： 0 目录 1 菜单
     */
    private Integer type ;
    /**
     * 排序
     */
    private Integer orderNum;

    /**
     * 子菜单
     */
    @TableField(exist = false)
    private List<RnaWebNavEntity> children;

    /**
     * ztree属性
     */
    @TableField(exist=false)
    private Boolean open;

    private String icon;

    private String hidden;
}
