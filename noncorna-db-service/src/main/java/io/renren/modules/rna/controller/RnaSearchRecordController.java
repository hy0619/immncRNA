package io.renren.modules.rna.controller;

import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.modules.rna.entity.RnaSearchRecordEntity;
import io.renren.modules.rna.service.RnaSearchRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;



/**
 * 搜索记录表
 *
 * @author hy
 * @email sunlightcs@gmail.com
 * @date 2020-10-13 09:28:26
 */
@RestController
@RequestMapping("rna/rnasearchrecord")
@Api("rna搜索日志")
public class RnaSearchRecordController {
    @Autowired
    private RnaSearchRecordService rnaSearchRecordService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("rna:rnasearchrecord:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = rnaSearchRecordService.queryPage(params);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{searchId}")
    @RequiresPermissions("rna:rnasearchrecord:info")
    public R info(@PathVariable("searchId") Long searchId){
		RnaSearchRecordEntity rnaSearchRecord = rnaSearchRecordService.getById(searchId);

        return R.ok().put("rnaSearchRecord", rnaSearchRecord);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("rna:rnasearchrecord:save")
    public R save(@RequestBody RnaSearchRecordEntity rnaSearchRecord){
		rnaSearchRecordService.save(rnaSearchRecord);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("rna:rnasearchrecord:update")
    public R update(@RequestBody RnaSearchRecordEntity rnaSearchRecord){
		rnaSearchRecordService.updateById(rnaSearchRecord);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("rna:rnasearchrecord:delete")
    public R delete(@RequestBody Long[] searchIds){
		rnaSearchRecordService.removeByIds(Arrays.asList(searchIds));

        return R.ok();
    }


    @GetMapping("/web/quickSearchTags")
    @ApiOperation("快速搜索标签")
    public R quickSearchTags(){
        Map<String, List<String>> tags = rnaSearchRecordService.quickSearchTags();
        Integer tagsNum = 0;
        for(List<String> tagList : tags.values()){
            if(!CollectionUtils.isEmpty(tagList))
                tagsNum += tagList.size();
        }
        return R.ok().put("tags" , tags).put("tagNum" , tagsNum);

    }

}
