/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package io.renren.modules.web.service;


import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.modules.web.entity.RnaWebNavEntity;

import java.util.List;


/**
 * 菜单管理
 *
 * @author Mark sunlightcs@gmail.com
 */
public interface RnaWebNavService extends IService<RnaWebNavEntity> {


    List<RnaWebNavEntity> getNavTreeList();


}
