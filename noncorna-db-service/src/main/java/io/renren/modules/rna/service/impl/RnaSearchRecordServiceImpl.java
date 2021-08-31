package io.renren.modules.rna.service.impl;

import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.CaseFormat;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.modules.rna.dao.RnaSearchRecordDao;
import io.renren.modules.rna.entity.RnaInfoEntity;
import io.renren.modules.rna.entity.RnaSearchRecordEntity;
import io.renren.modules.rna.service.RnaInfoService;
import io.renren.modules.rna.service.RnaSearchRecordService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;


@Service("rnaSearchRecordService")
public class RnaSearchRecordServiceImpl extends ServiceImpl<RnaSearchRecordDao, RnaSearchRecordEntity> implements RnaSearchRecordService {

    @Autowired
    RnaInfoService rnaInfoService;

    @Override
    public List<Map<String, Object>> queryByMap(QueryWrapper genWrapper) {
        return this.getBaseMapper().selectMaps(genWrapper);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {

        String ip = (String)params.get("ip");
        String key =(String)params.get("key");
        QueryWrapper<RnaSearchRecordEntity> queryWrapper = new QueryWrapper();
        queryWrapper.like(StringUtils.isNotBlank(ip) , "ip" , ip);
        queryWrapper.and(StringUtils.isNotBlank(key), wrapper->wrapper.like("cancer" , key)
                .or().like("gen"  , key)
                .or().like("immune_cell"  , key)
                .or().like("immune_pathway"  , key));
        IPage<RnaSearchRecordEntity> page = this.page(
                new Query<RnaSearchRecordEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    public static final String SEARCH_DEFAULT_VAL = "##";
    @Override
    public Map<String, List<String>> quickSearchTags() {
        List<RnaInfoEntity> genIdList = this.listTags("gen" , "gene_id" );
        List<RnaInfoEntity> genSymbolList = this.listTags("gen" ,"gene_symbol");
        List<RnaInfoEntity> cancerTypeList = this.listTags("cancer" , "cancer_type" );
        List<RnaInfoEntity> cancerTypeAdList = this.listTags("cancer" , "cancer_type_ad" );
        List<RnaInfoEntity> immuneCellList = this.listTags("immune_cell" , "immune_cell");
        List<RnaInfoEntity> immunePathwayList = this.listTags("immune_pathway" , "immune_pathway");

        List<String> genIdNameList = genIdList.parallelStream()
                .map(gen->gen.getGeneId()).collect(Collectors.toList());

        List<String> genSymbolNameList = genSymbolList.parallelStream()
                .map(gen->gen.getGeneSymbol()).collect(Collectors.toList());


        List<String> cancerTypeNameList = cancerTypeList.parallelStream()
                .map(cancer->cancer.getCancerType()).collect(Collectors.toList());
        List<String> cancerTypeAdNameList =cancerTypeAdList.parallelStream()
                .map(cancer->cancer.getCancerTypeAd()).collect(Collectors.toList());

        List<String> cellList = immuneCellList.parallelStream()
                .map(immuneCell->immuneCell.getImmuneCell()).collect(Collectors.toList());

        List<String> pathwayList = immunePathwayList.parallelStream()
                .map(pathWay->pathWay.getImmunePathway()).collect(Collectors.toList());



        Map<String ,List<String>> searchTagsMap = new HashMap<String ,List<String>>(){
            {
                put("geneId" , genIdNameList);
                put("genSymbol" , genSymbolNameList);
                put("cancerType" , cancerTypeNameList);
                put("cancerTypeAd" , cancerTypeAdNameList);
                put("immuneCell" , cellList);
                put("immunePathway" , pathwayList);
            }
        };

        return searchTagsMap;
    }



    public List<RnaInfoEntity> listTags(String searchColumn , String... infoColumns){
        QueryWrapper<RnaSearchRecordEntity> searchWrapper =
                new QueryWrapper<RnaSearchRecordEntity>().select(searchColumn,"count(*) as cnt")
                        .ne(searchColumn , SEARCH_DEFAULT_VAL).groupBy(searchColumn)
                        .orderByDesc("cnt" ).last("limit  40");
        List<RnaSearchRecordEntity> list = this.list(searchWrapper);
        if(CollectionUtils.isEmpty(list)) return new ArrayList<>();
        QueryWrapper<RnaInfoEntity> rnaInfoEntityQueryWrapper = new QueryWrapper<>();
        rnaInfoEntityQueryWrapper.eq("status" , 0 ).select("distinct " + Arrays.stream(infoColumns).collect(Collectors.joining(",")));

            rnaInfoEntityQueryWrapper.and(wrapper->{
                for(RnaSearchRecordEntity searchRecordEntity : list) {
                    for (int i = 0; i < infoColumns.length; i++) {
                        Object searchVal = ReflectUtil.getFieldValue(searchRecordEntity,
                                CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, searchColumn));
                            wrapper.or().like( infoColumns[i], searchVal);

                    }
                }

            });
        rnaInfoEntityQueryWrapper.last(" limit 4");

        return rnaInfoService.list(rnaInfoEntityQueryWrapper);
    }

}