/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package io.renren.modules.web.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.modules.web.entity.RnaWebNewsEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 网站新闻管理
 *
 * @author Mark sunlightcs@gmail.com
 */
@Mapper
public interface RnaWebNewsDao extends BaseMapper<RnaWebNewsEntity> {
	

}
