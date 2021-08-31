package io.renren.modules.web.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @className: RnaWebNewsEntity
 * @description: 新闻类
 * @author: hy
 * @date: 2020-10-9
 **/
@Data
@TableName("rna_web_news")
public class RnaWebNewsEntity {

    @TableId
    private Long newsId;
    /**
     * 标题
     */
    private String title;
    /**
     * 新闻内容
     */
    private String content ;
    /**
     * 链接
     */
    private String linkUrl;
    /**
     * 浏览量
     */
    private Long visitNum;

    /**
     * 创建用户
     */
    private Long createUserId;

    /**
     *  状态 ： 0 待发布  1 已发布 2 已撤回
     */
    private Integer status;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date ct;

}
