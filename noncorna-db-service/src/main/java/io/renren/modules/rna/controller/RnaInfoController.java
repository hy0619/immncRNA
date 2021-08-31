package io.renren.modules.rna.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.handler.inter.IReadHandler;
import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.base.CaseFormat;
import io.renren.common.exception.RRException;
import io.renren.common.utils.ExcelUtils;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.modules.rna.entity.RnaCategoryEntity;
import io.renren.modules.rna.entity.RnaInfoCateGoryTree;
import io.renren.modules.rna.entity.RnaInfoEntity;
import io.renren.modules.rna.service.RnaInfoService;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.*;


/**
 * rna信息
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2020-10-11 14:16:17
 */
@RestController
@RequestMapping("rna/rnainfo")
public class RnaInfoController {
    @Autowired
    private RnaInfoService rnaInfoService;

    /**
     * 列表
     */
    @RequestMapping("/web/list")
    //@RequiresPermissions("rna:rnainfo:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = rnaInfoService.queryPage(params);
        return R.ok().put("page", page);
    }


    @RequestMapping("/web/search")
    public R listByWebQuickSearch(@RequestParam Map<String, Object> params){
        PageUtils page = rnaInfoService.listByWebQuickSearch(params);
        return R.ok().put("page", page);

    }

    /**
     * 下载rna数据模板
     * @param response
     */
    @GetMapping("/downloadRnaTemplate")
    @RequiresPermissions("rna:rnainfo:download")
    public void downloadRnaTemplate(HttpServletResponse response){
        String title = "LmmncRNA BASIC INFO IMPORT TEMPLATE";
        String sheetName = "LmmncRNA BASIC INFO IMPORT TEMPLATE";
        ExportParams exportParams = new ExportParams(title, sheetName);
        exportParams.setType(ExcelType.XSSF);
        exportParams.setCreateHeadRows(true);
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, RnaInfoEntity.class, new ArrayList<>(1));
       if(null != workbook){
           ExcelUtils.downLoadExcel("LmmncRNA" , response , workbook);
       }
    }

    /**
     * 下载rna 有效数据
     * @param response
     */
    @GetMapping("/downloadRnaData")
    public void downloadRnaData(HttpServletResponse response , @RequestParam Map<String, Object> params){
        List list = rnaInfoService.list(rnaInfoService.getManegerCommonWrapper(params));
        ExcelUtils.exportExcel(list , "LmmncRNA DATA"  ,
                "LmmncRNA DATA" , RnaInfoEntity.class ,
                "LmmncRNA DATA" , true, response);
    }

    /**
     * 导入Rna 数据
     * @param file
     * @return
     */
    @PostMapping("/importRnaData")
    @RequiresPermissions("rna:rnainfo:import")
    public R importRnaData(MultipartFile file ) throws Exception {
        Integer titleRows = 1; // 表格标题行数,默认0
        Integer headerRows = 1 ; //表头行数,默认1

        ImportParams importParams = new ImportParams();
        importParams.setTitleRows(titleRows);
        importParams.setHeadRows(headerRows);
        List<RnaInfoEntity> rnaInfoEntities = new LinkedList<>();
                ExcelImportUtil.importExcelBySax(file.getInputStream(), RnaInfoEntity.class, importParams, new IReadHandler() {
                    @Override
                    public void handler(Object o) {
                        rnaInfoEntities.add((RnaInfoEntity) o);
                    }

                    @Override
                    public void doAfterAll() {
                        if(CollectionUtils.isEmpty(rnaInfoEntities)) throw new RRException("无数据,请导入数据");
                        rnaInfoService.saveBatch(rnaInfoEntities , 2000);

                    }
                });

        return R.ok();

    }


    /**
     * 信息
     */
    @RequestMapping("/info/{dataId}")
    @RequiresPermissions("rna:rnainfo:info")
    public R info(@PathVariable("dataId") Long dataId){
		RnaInfoEntity rnaInfo = rnaInfoService.getById(dataId);

        return R.ok().put("rnaInfo", rnaInfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("rna:rnainfo:save")
    public R save(@RequestBody RnaInfoEntity rnaInfo){
		rnaInfoService.save(rnaInfo);

        return R.ok();
    }

    @RequestMapping("/web/submit")
    //@RequiresPermissions("rna:rnainfo:save")
    public R submit(@RequestBody RnaInfoEntity rnaInfo){
        rnaInfoService.save(rnaInfo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("rna:rnainfo:update")
    public R update(@RequestBody RnaInfoEntity rnaInfo){
		rnaInfoService.updateById(rnaInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("rna:rnainfo:delete")
    public R delete(@RequestBody Long[] dataIds){
		rnaInfoService.removeByIds(Arrays.asList(dataIds));

        return R.ok();
    }

    @GetMapping("/web/infoTree/{category}")
    public R getTreeByCatagory(@PathVariable String category ){
        List<RnaInfoCateGoryTree> children = new ArrayList<>();
        if(category.equals("gen")){
            RnaInfoCateGoryTree genIdNode = genColumnNode("gene_id" , "gen");
            RnaInfoCateGoryTree geneSymbolNode = genColumnNode("gene_symbol" , "gen");
            children.add(genIdNode);
            children.add(geneSymbolNode);
        }else if(category.equals("cancer")) {
            RnaInfoCateGoryTree cancerTypeNode = genColumnNode("cancer_type" , "cancer");
            RnaInfoCateGoryTree cancerTypeAdNode = genColumnNode("cancer_type_ad" , "cancer");
            children.add(cancerTypeNode);
            children.add(cancerTypeAdNode);

        } else if(category.equals("immunePathway")) {
            RnaInfoCateGoryTree immunePathwayNode = genColumnNode("immune_pathway" , "immunePathway");
            children.add(immunePathwayNode);
        }else if(category.equals("immuneCell")) {
            RnaInfoCateGoryTree immuneCellNode = genColumnNode("immune_cell" , "immuneCell");
            children.add(immuneCellNode);
        }
        if(CollectionUtils.isEmpty(children)) children = null;
        return R.ok().put("tree" , children);
    }


    private  RnaInfoCateGoryTree genColumnNode(String columnName  , String category){
        RnaInfoCateGoryTree node = new RnaInfoCateGoryTree()
                .setId(UUID.randomUUID().toString())
                .setCategory(category)
                .setLabel(columnName.toUpperCase());
        QueryWrapper<RnaInfoEntity> wrapper =
                new QueryWrapper<RnaInfoEntity>().eq("status" , 0).select("distinct " + columnName);
        List<RnaInfoEntity> list = rnaInfoService.list(wrapper);
        List<RnaInfoCateGoryTree> children = new ArrayList<>();
        if(!CollectionUtils.isEmpty(list)){
            for(RnaInfoEntity rnaInfoEntity : list){
                RnaInfoCateGoryTree rnaInfoCateGoryTree = new RnaInfoCateGoryTree();
                String labLower = (String)ReflectUtil.getFieldValue(rnaInfoEntity,
                        CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, columnName));

                rnaInfoCateGoryTree.setId(UUID.randomUUID().toString()).setCategory(category)
                        .setLabel(StringUtils.isEmpty(labLower)? "" : labLower.toUpperCase())
                        .setChildren(null);


                children.add(rnaInfoCateGoryTree);
            }
        }
        if(!CollectionUtils.isEmpty(children)) node.setChildren(children);
        return node;
    }


    @GetMapping("/web/getCateGroyNum")
    public R getCateGroyNum(@RequestParam Map<String, Object> map){
        String column = (String) map.get("column");
        String value = (String) map.get("value");

        QueryWrapper queryWrapper =
                new QueryWrapper<RnaCategoryEntity>()
                        .select(column + " as category, count(*) as dataNum" )
                        .like(!StringUtils.isEmpty(value) , column , value)
                        .eq("status" , 0 )
                        .groupBy(column);


        return R.ok().put("list" ,rnaInfoService.listMaps(queryWrapper));


    }


    @GetMapping("/web/downloadByCategory")
    public void downloadByCategory(@RequestParam Map<String, Object> map ,HttpServletResponse response){
        String column = (String) map.get("column");
        String value = (String) map.get("value");
        //Map<String , Object> params = new HashMap<>(2);
        QueryWrapper<RnaInfoEntity> queryWrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty(column) && !StringUtils.isEmpty(value)){
            queryWrapper.like(column , value);
        }
        List list = rnaInfoService.list(queryWrapper);
        ExcelUtils.exportExcel(list , "LmmRNA DATA"  ,
                "LmmRNA DATA" , RnaInfoEntity.class ,
                "LmmRNA DATA" , true, response);

    }
}
