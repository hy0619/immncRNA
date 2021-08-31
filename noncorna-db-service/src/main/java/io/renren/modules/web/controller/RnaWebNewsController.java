package io.renren.modules.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.annotation.SysLog;
import io.renren.common.exception.RRException;
import io.renren.common.utils.Constant;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.modules.sys.controller.AbstractController;
import io.renren.modules.web.entity.RnaWebNewsEntity;
import io.renren.modules.web.service.RnaWebNewsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @className: RnaWebNavController
 * @description: 网站新闻controller
 * @author: hy
 * @date: 2020-10-4
 **/
@RestController
@RequestMapping("web/news")
@Api("网站新闻信息")
public class RnaWebNewsController extends AbstractController {
    @Autowired
    RnaWebNewsService rnaWebNewsService;


    @SysLog("保存新闻")
    @PostMapping("/save")
    @RequiresPermissions("web:news:save")
    @ApiOperation("保存新闻")
    public R save(@RequestBody  @Valid RnaWebNewsEntity rnaWebNewsEntity){
        rnaWebNewsService.save(rnaWebNewsEntity);
        return R.ok("操作成功");
    }


    /**
     * 修改
     */
    @SysLog("修改新闻")
    @PostMapping("/update")
    @RequiresPermissions("web:news:update")
    @ApiOperation("修改新闻")
    public R update(@RequestBody RnaWebNewsEntity rnaWebNewsEntity){
        rnaWebNewsService.updateById(rnaWebNewsEntity);
        return R.ok();
    }


    /**
     * 删除
     * @param newsIds
     * @return
     */
    @SysLog("删除news")
    @PostMapping("/delete")
    @RequiresPermissions("web:news:delete")
    @ApiOperation("删除news")
    public R delete(@RequestBody Long[] newsIds){
        rnaWebNewsService.removeByIds(Arrays.asList(newsIds));
        return R.ok("删除成功");
    }


    /**
     * 查看新闻详情
     * @param newsId
     * @return
     */
    @GetMapping("/info/{newsId}")
    @RequiresPermissions("web:news:info")
    @ApiOperation("查看新闻详情")
    public R info(@PathVariable("newsId") Long newsId){
        RnaWebNewsEntity one = rnaWebNewsService.getById(newsId);
        return R.ok().put("news", one);
    }

    /**
     * 查看新闻详情并跟新访问量
     * @param newsId
     * @return
     */
    @GetMapping("/info4UdpVisitNum/{newsId}")
    @ApiOperation("查看新闻详情并跟新访问量")
    public R info4UdpVisitNum(@PathVariable("newsId") Long newsId){
        RnaWebNewsEntity one = rnaWebNewsService.getById(newsId);
        if(null != one){
            Long visitNum = one.getVisitNum();
            one.setVisitNum(visitNum == null ? 1 : visitNum +1);
            rnaWebNewsService.updateById(one); //增加浏览量
        }
        return R.ok().put("news", one);
    }


    /**
     * 分页查询
     * @param params
     * @return
     */
    @GetMapping("/list")
    @RequiresPermissions("web:news:list")
    @ApiOperation("新闻列表分页")
    public R list(@RequestParam Map<String, Object> params){
        //只有超级管理员，才能查看所有管理员列表
        if(getUserId() != Constant.SUPER_ADMIN){
            params.put("createUserId", getUserId());
        }
        PageUtils page = rnaWebNewsService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 发布
     * @param newsId
     * @return
     */
    @PostMapping("/publish/{newsId}")
    @RequiresPermissions("web:news:publish")
    @ApiOperation("发布新闻")
    public R publishNews(@PathVariable Long newsId){
        RnaWebNewsEntity one = rnaWebNewsService.getById(newsId);
        if(one == null || one.getStatus().equals(1)) throw new RRException("数据不存在或已经发布");
        one.setStatus(1);
        rnaWebNewsService.updateById(one);
        return R.ok();
    }

    /**
     * 撤销
     * @param newsId
     * @return
     */
    @PostMapping("/revoke/{newsId}")
    @RequiresPermissions("web:news:revoke")
    @ApiOperation("撤销新闻")
    public R revokeNews(@PathVariable Long newsId){
        RnaWebNewsEntity one = rnaWebNewsService.getById(newsId);
        if(one == null) throw new RRException("数据不存在");
        if(one.getStatus().equals(1)){
            one.setStatus(2); //撤回
            rnaWebNewsService.updateById(one);
            return R.ok();
        }else {
            return R.error("新闻未发布,不可撤回");
        }
    }


    /**
     * 获取最新新闻列表
     * @param topSize
     * @return
     */
    @GetMapping("/topNews")
    @ApiOperation("获取最新新闻列表")
    public R listTopNews(Integer topSize){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.orderByDesc("ct");
        queryWrapper.last("limit 0," + topSize);
        List list = rnaWebNewsService.list(queryWrapper);
        return R.ok().put("topNews" , list);
    }





}
