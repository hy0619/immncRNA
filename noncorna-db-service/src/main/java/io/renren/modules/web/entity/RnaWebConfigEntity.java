package io.renren.modules.web.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * @className: RnaWebConfigEntity
 * @description: 网站配置实体
 * @author: hy
 * @date: 2020-10-3
 **/
@Data
public class RnaWebConfigEntity implements Serializable {

    @TableId
    private Long id;
    /**
     * 网站logo
     */
    private String logo;
    /**
     * 网站简介
     */
    private String introduce;
    /**
     * 网站首页详细描述
     */
    private String description;
    /**
     * 网站底部文案
     */
    private String footerContent;
    /**
     * 网站帮助内容
     */
    private String helpContent;
}
