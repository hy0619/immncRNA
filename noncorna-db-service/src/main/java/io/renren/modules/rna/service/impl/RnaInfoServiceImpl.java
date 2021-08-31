package io.renren.modules.rna.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.holder.RequestHolder;
import io.renren.common.utils.IPUtils;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.modules.rna.dao.RnaInfoDao;
import io.renren.modules.rna.entity.RnaInfoEntity;
import io.renren.modules.rna.entity.RnaSearchRecordEntity;
import io.renren.modules.rna.service.RnaInfoService;
import io.renren.modules.rna.service.RnaSearchRecordService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;


@Service("rnaInfoService")
public class RnaInfoServiceImpl extends ServiceImpl<RnaInfoDao, RnaInfoEntity> implements RnaInfoService {
    @Autowired
    private RnaSearchRecordService rnaSearchRecordService;
    @Override
    public QueryWrapper getManegerCommonWrapper(Map<String, Object> params){
        QueryWrapper<RnaInfoEntity> rnaInfoEntityQueryWrapper = new QueryWrapper<>();

        String cancer = (String) params.get("cancer");
        String gen = (String) params.get("gen");
        String immuneCell = (String) params.get("immuneCell");
        String immunePathway = (String) params.get("immunePathway");
        rnaInfoEntityQueryWrapper
                .eq(params.get("status")!=null ,"status" , params.get("status"))

                .and(StringUtils.isNotBlank(cancer), wrapper->wrapper.like("cancer_type" , cancer)
                                .or().like("cancer_type_ad" ,  cancer))

                .and(StringUtils.isNotBlank(gen), wrapper->wrapper.like("gene_id" , gen)
                .or().like("gene_symbol" ,  gen))

                .like(StringUtils.isNotBlank(immuneCell) , "immune_cell" ,immuneCell)

                .like(StringUtils.isNotBlank(immunePathway) , "immune_pathway" , immunePathway);

        RnaSearchRecordEntity rnaSearchRecordEntity = new RnaSearchRecordEntity()
                .setCancer(cancer).setGen(gen).setImmuneCell(immuneCell).setImmunePathway(immunePathway)
                .setCt(new Date()).setIp(IPUtils.getIpAddr(RequestHolder.getRequest()));

        rnaSearchRecordService.save(rnaSearchRecordEntity);

        return rnaInfoEntityQueryWrapper;

    }


    @Override
    public PageUtils listByWebQuickSearch(Map<String, Object> params){
        QueryWrapper<RnaInfoEntity> rnaInfoEntityQueryWrapper = new QueryWrapper<>();

        String keyWords = (String) params.get("keyWords");
        if(StringUtils.isBlank(keyWords)) return new PageUtils(this.page(new Query<RnaInfoEntity>().getPage(params)));
        rnaInfoEntityQueryWrapper
                .eq(params.get("status")!=null ,"status" , params.get("status"))
                .and(StringUtils.isNotBlank(keyWords), wrapper->wrapper.like("cancer_type" , keyWords)
                        .or().like("cancer_type_ad" ,  keyWords)
                        .or().like("immune_cell" ,  keyWords)
                        .or().like("immune_pathway" ,  keyWords)
                        .or().like("gene_id" ,  keyWords)
                        .or().like("gene_symbol" ,  keyWords));

        IPage<RnaInfoEntity> page = this.page(
                new Query<RnaInfoEntity>().getPage(params),
                rnaInfoEntityQueryWrapper
        );

        if(!CollectionUtils.isEmpty(page.getRecords())){
            RnaInfoEntity searchOne = page.getRecords().get(0);
            RnaSearchRecordEntity rnaSearchRecordEntity = handerSearchedData(searchOne , keyWords);
            rnaSearchRecordService.save(rnaSearchRecordEntity);
        }


        return new PageUtils(page);

    }


    private RnaSearchRecordEntity handerSearchedData(RnaInfoEntity searchOne , String keyWords){
        RnaSearchRecordEntity rnaSearchRecordEntity = new RnaSearchRecordEntity()
                .setCt(new Date()).setIp(IPUtils.getIpAddr(RequestHolder.getRequest()));
        if(StringUtils.isNotBlank(searchOne.getGeneId()) && searchOne.getGeneId().contains(keyWords) ){
            rnaSearchRecordEntity.setGen(keyWords);
        }
        if(StringUtils.isNotBlank(searchOne.getGeneSymbol()) && searchOne.getGeneSymbol().contains(keyWords) ){
            rnaSearchRecordEntity.setGen(keyWords);
        }

        if(StringUtils.isNotBlank(searchOne.getCancerType()) && searchOne.getCancerType().contains(keyWords) ){
            rnaSearchRecordEntity.setCancer(keyWords);
        }
        if(StringUtils.isNotBlank(searchOne.getCancerTypeAd()) && searchOne.getCancerTypeAd().contains(keyWords) ){
            rnaSearchRecordEntity.setCancer(keyWords);
        }
        if(StringUtils.isNotBlank(searchOne.getImmuneCell()) && searchOne.getImmuneCell().contains(keyWords) ){
            rnaSearchRecordEntity.setImmuneCell(keyWords);
        }
        if(StringUtils.isNotBlank(searchOne.getImmunePathway()) && searchOne.getImmunePathway().contains(keyWords) ){
            rnaSearchRecordEntity.setImmunePathway(keyWords);
        }
        return rnaSearchRecordEntity;

    }



    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<RnaInfoEntity> rnaInfoEntityQueryWrapper = this.getManegerCommonWrapper(params);
        IPage<RnaInfoEntity> page = this.page(
                new Query<RnaInfoEntity>().getPage(params),
                rnaInfoEntityQueryWrapper
        );
        return new PageUtils(page);
    }

    @Override
    public List<Map<String, Object>> queryByMap(Wrapper wrapper){
       return this.getBaseMapper().selectMaps(wrapper);
    }

}