package io.renren.modules.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.utils.R;
import io.renren.modules.rna.entity.RnaInfoEntity;
import io.renren.modules.rna.service.RnaInfoService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @className: RnaWebStatisticsController
 * @description: 网站统计信息
 * @author: hy
 * @date: 2020-10-18
 **/
@RestController
@RequestMapping("web/statistics")
@Api("网站统计信息")
public class RnaWebStatisticsController {
    @Autowired
    RnaInfoService rnaInfoService;


    @GetMapping("/baseInfo")
    public R baseInfo(){
        QueryWrapper<RnaInfoEntity> queryWrapper
                = new QueryWrapper<>();
        queryWrapper
                .eq("status" , 0 );
        Integer dataCnt = rnaInfoService.count(queryWrapper);


        QueryWrapper<RnaInfoEntity> cancerTypeWrapper
                = new QueryWrapper<>();
        cancerTypeWrapper
                .eq("status" , 0 ).select("distinct(cancer_type)");
       Integer cancerTypeCnt = rnaInfoService.count(cancerTypeWrapper);


        QueryWrapper<RnaInfoEntity> immuneCellWrapper
                = new QueryWrapper<>();
        cancerTypeWrapper.select("distinct(immune_cell)")
                .eq("status" , 0 );
        Integer immuneCellCnt = rnaInfoService.count(immuneCellWrapper);


        QueryWrapper<RnaInfoEntity> genWrapper
                = new QueryWrapper<>();
        cancerTypeWrapper.select("distinct(gene_id)")
                .eq("status" , 0 );
        Integer genCnt = rnaInfoService.count(genWrapper);
      return  R.ok().put("immuneCellCnt" , immuneCellCnt)
                    .put("dataCnt" , dataCnt)
                    .put("genCnt" , genCnt)
                    .put("cancerTypeCnt" , cancerTypeCnt);
    }

}
