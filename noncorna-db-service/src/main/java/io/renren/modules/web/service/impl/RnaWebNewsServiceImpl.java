package io.renren.modules.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.modules.web.dao.RnaWebNewsDao;
import io.renren.modules.web.entity.RnaWebNewsEntity;
import io.renren.modules.web.service.RnaWebNewsService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @className: RnaWebNewsServiceImpl
 * @description: web新闻sercie
 * @author: hy
 * @date: 2020-10-4
 **/
@Service
public class RnaWebNewsServiceImpl extends ServiceImpl<RnaWebNewsDao, RnaWebNewsEntity> implements RnaWebNewsService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String title = (String)params.get("title");
        Long createUserId = (Long)params.get("createUserId");

        IPage<RnaWebNewsEntity> page = this.page(
                new Query<RnaWebNewsEntity>().getPage(params),
                new QueryWrapper<RnaWebNewsEntity>()
                        .like(StringUtils.isNotBlank(title),"title", title)
                        .eq(createUserId != null,"create_user_id", createUserId)
        );

        return new PageUtils(page);
    }
}
