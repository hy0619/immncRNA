package io.renren.modules.rna.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.rna.entity.RnaInfoEntity;

import java.util.List;
import java.util.Map;

/**
 * rna信息
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2020-10-11 14:16:17
 */
public interface RnaInfoService extends IService<RnaInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
    QueryWrapper getManegerCommonWrapper(Map<String, Object> params);
    PageUtils listByWebQuickSearch(Map<String, Object> params);
    List<Map<String, Object>> queryByMap(Wrapper wrapper);
}

