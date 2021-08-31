package io.renren.modules.rna.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.rna.entity.RnaSearchRecordEntity;

import java.util.List;
import java.util.Map;

/**
 * 搜索记录表
 *
 * @author hy
 * @email sunlightcs@gmail.com
 * @date 2020-10-13 00:03:15
 */
public interface RnaSearchRecordService extends IService<RnaSearchRecordEntity> {

    List<Map<String, Object>> queryByMap(QueryWrapper genWrapper);

    PageUtils queryPage(Map<String, Object> params);

    Map<String, List<String>> quickSearchTags();
}

