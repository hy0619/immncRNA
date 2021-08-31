/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package io.renren.modules.web.service;


import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.web.entity.RnaWebNewsEntity;

import java.util.Map;


/**
 * 新闻管理
 *
 * @author Mark sunlightcs@gmail.com
 */
public interface RnaWebNewsService extends IService<RnaWebNewsEntity> {


    PageUtils queryPage(Map<String, Object> params);
}
