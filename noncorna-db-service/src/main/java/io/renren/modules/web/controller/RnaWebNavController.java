package io.renren.modules.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.annotation.SysLog;
import io.renren.common.utils.R;
import io.renren.modules.web.entity.RnaWebNavEntity;
import io.renren.modules.web.service.RnaWebNavService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @className: RnaWebNavController
 * @description: 网站导航controller
 * @author: hy
 * @date: 2020-10-4
 **/
@RestController
@RequestMapping("web/nav")
@Api("网站导航信息")
public class RnaWebNavController {
    @Autowired
    RnaWebNavService rnaWebNavService;
    @GetMapping("/list")
    @ApiOperation("网站导航-list")
    public R list(){
        List<RnaWebNavEntity> treeList = rnaWebNavService.getNavTreeList();
        return R.ok().put("navs" , treeList);
    }


    @SysLog("保存网站菜单")
    @PostMapping("/save")
    @RequiresPermissions("web:nav:save")
    public R save(@RequestBody  @Valid RnaWebNavEntity rnaWebNavEntity){
        if(rnaWebNavEntity.getNavId()==null)
            rnaWebNavService.save(rnaWebNavEntity);
        else
            rnaWebNavService.updateById(rnaWebNavEntity);
        return R.ok("操作成功");
    }


    /**
     * 修改
     */
    @SysLog("修改导航")
    @PostMapping("/update")
    @RequiresPermissions("web:nav:update")
    public R update(@RequestBody RnaWebNavEntity rnaWebNavEntity){
        rnaWebNavService.updateById(rnaWebNavEntity);
        return R.ok();
    }



    @GetMapping("/select")
    @RequiresPermissions("web:nav:select")
    public R select(){
        //查询列表数据
        List<RnaWebNavEntity> navTreeList = rnaWebNavService.getNavTreeList();
        //添加顶级菜单
        RnaWebNavEntity root = new RnaWebNavEntity();
        root.setNavId(0L);
        root.setName("一级菜单");
        root.setParentId(-1L);
        root.setOpen(true);
        navTreeList.add(root);
        return R.ok().put("menuList", navTreeList);
    }

    @SysLog("删除菜单")
    @PostMapping("/delete/{navId}")
    @RequiresPermissions("web:nav:delete")
    public R delete(@PathVariable("navId") Long navId){

        QueryWrapper<RnaWebNavEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id" , navId);
        Integer count = rnaWebNavService.count(queryWrapper);
        if(count > 0){
            return R.error("请先删除子菜单或按钮");
        }
        rnaWebNavService.removeById(navId);
        return R.ok();
    }



    @GetMapping("/info/{navId}")
    @RequiresPermissions("web:nav:info")
    public R info(@PathVariable("navId") Long navId){
        RnaWebNavEntity one = rnaWebNavService.getById(navId);
        return R.ok().put("nav", one);
    }




}
