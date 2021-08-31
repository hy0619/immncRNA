package io.renren.modules.web.controller;

import cn.hutool.json.JSONUtil;
import io.renren.common.utils.ConfigConstant;
import io.renren.common.utils.R;
import io.renren.modules.sys.entity.SysConfigEntity;
import io.renren.modules.sys.service.SysConfigService;
import io.renren.modules.web.entity.RnaWebConfigEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @className: RnaWebConfigControler
 * @description: web站点配置类
 * @author: hy
 * @date: 2020-10-3
 **/
@RestController
@RequestMapping("web/config")
@Api("网站基础配置信息")
public class RnaWebConfigControler {

    @Autowired
    private SysConfigService sysConfigService;

    private final static String KEY = ConfigConstant.WEB_STORAGE_CONFIG_KEY;

    /**
     * 获取网站配置
     * @return
     */
    @RequestMapping("get")
    @ApiOperation("获取网站配置")
    public R getWebConfig(){
        RnaWebConfigEntity rnaWebConfigEntity =  sysConfigService.getConfigObject(KEY , RnaWebConfigEntity.class );
        return R.ok().put("config" , rnaWebConfigEntity);
    }


    @RequestMapping("save")
    @ApiOperation("网站配置保存")
    public R save(@RequestBody RnaWebConfigEntity config){
        SysConfigEntity sysConfigEntity = new SysConfigEntity();
        sysConfigEntity.setParamKey(KEY);
        sysConfigEntity.setParamValue(JSONUtil.toJsonStr(config));
        sysConfigEntity.setRemark("网站基础配置");
        sysConfigService.saveWebConfig(sysConfigEntity);
        return R.ok("保存成功");
    }


}
