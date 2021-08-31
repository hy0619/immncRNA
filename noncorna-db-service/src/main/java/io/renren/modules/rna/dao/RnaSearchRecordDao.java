package io.renren.modules.rna.dao;

import io.renren.modules.rna.entity.RnaSearchRecordEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 搜索记录表
 * 
 * @author hy
 * @email sunlightcs@gmail.com
 * @date 2020-10-13 00:03:15
 */
@Mapper
public interface RnaSearchRecordDao extends BaseMapper<RnaSearchRecordEntity> {
	
}
