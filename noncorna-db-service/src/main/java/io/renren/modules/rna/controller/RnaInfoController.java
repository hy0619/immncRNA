package io.renren.modules.rna.controller;

import cn.afterturn.easypoi.csv.CsvExportUtil;
import cn.afterturn.easypoi.csv.entity.CsvExportParams;
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
import io.renren.common.utils.StringUtil;
import io.renren.modules.rna.entity.RnaCategoryEntity;
import io.renren.modules.rna.entity.RnaInfoCategoryZTree;
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
import java.io.IOException;
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
        List tree = new LinkedList<>();
        String pid = "-1";

      if(category.equals("gen")){
            List<RnaInfoCategoryZTree> genTypeNode = genColumnNode("gene_type", "gen" , pid);
            tree.addAll(genTypeNode);

          /*List<RnaInfoCategoryZTree> genTypeAdNode = genColumnNode("gene_type_ad", "gen" , pid);
          tree.addAll(genTypeAdNode);*/

        }else if(category.equals("cancer")) {
            List<RnaInfoCategoryZTree> cancerTypeNode = genColumnNode("cancer_type" , "cancer" , pid);
            List<RnaInfoCategoryZTree> cancerTypeAdNode = genColumnNode("cancer_type_ad" , "cancer" , pid);
            tree.addAll(cancerTypeNode);
            tree.addAll(cancerTypeAdNode);

        } /*else if(category.equals("immunePathway")) {
            List<RnaInfoCategoryZTree> immunePathwayNode =
                    genColumnNode("immune_pathway" , "immunePathway" , pid);
            tree.addAll(immunePathwayNode);
        }*/else if(category.equals("immuneCell")) {
            List<RnaInfoCategoryZTree> immuneCellNode = genColumnNode("immune_cell"
                    , "immuneCell" , pid);
            tree.addAll(immuneCellNode);
        }else if(category.equals("suvival")){
          List<RnaInfoCategoryZTree> tissueOriginNode = genColumnNode("suvival"
                  , "tissueOrigin" , pid);
          tree.addAll(tissueOriginNode);

        }else if(category.equals("pubTime")){
          List<RnaInfoCategoryZTree> pubTimeNode = genColumnNode("pub_time"
                  , "pubTime" , pid);
          tree.addAll(pubTimeNode);

        }else if(category.equals("tissueOrigin")){
          List<RnaInfoCategoryZTree> tissueOriginNode = genColumnNode("tissue_origin"
                  , "tissueOrigin" , pid);
          tree.addAll(tissueOriginNode);
        }

        return R.ok().put("tree" , tree);
    }


    private  List<RnaInfoCategoryZTree> genColumnNode(String columnName  , String category , String pid){
        List<RnaInfoCategoryZTree> resList = new LinkedList<>();
        String id = UUID.randomUUID().toString();
        String str = StringUtil.replaceUnderlineAndfirstToUpper(columnName, "_", " ");
        String name = StringUtil.firstCharacterToUpper(str);
        RnaInfoCategoryZTree node = new RnaInfoCategoryZTree()
                .setId(id)
                .setPid(pid)
                .setCategory(category)
                .setName(name);
        resList.add(node);
        QueryWrapper<RnaInfoEntity> wrapper =
                new QueryWrapper<RnaInfoEntity>()
                        .orderByDesc(columnName)
                        .isNotNull(columnName)
                        .eq("status" , 0).select("distinct " + columnName);
        List<RnaInfoEntity> list = rnaInfoService.list(wrapper);
        if(!CollectionUtils.isEmpty(list)){
            for(RnaInfoEntity rnaInfoEntity : list){
                RnaInfoCategoryZTree rnaInfoCateGoryTree = new RnaInfoCategoryZTree();
                String labLower = (String) ReflectUtil.getFieldValue(rnaInfoEntity,
                        CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, columnName));

                rnaInfoCateGoryTree.setId(rnaInfoEntity.getDataId() + "")
                        .setPid(id)
                        .setCategory(category)
                        .setName(labLower);
                resList.add(rnaInfoCateGoryTree);
            }
        }
        return resList;
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
    @GetMapping("/web/getMaxAndMinPubTime")
    public R getMaxAndMinPubTime(){
        QueryWrapper queryWrapper =
                new QueryWrapper<RnaCategoryEntity>()
                        .select( "max(pub_time) as pubTime " )
                        .eq("status" , 0 )
                        .ne("pub_time" , "")
                ;

        QueryWrapper queryWrapper2 =
                new QueryWrapper<RnaCategoryEntity>()
                        .select( "min(pub_time)  as pubTime" )
                        .eq("status" , 0 )
                        .ne("pub_time" , "")
                ;
        RnaInfoEntity maxPubTimeEntity = rnaInfoService.getOne(queryWrapper);

        RnaInfoEntity minPubTimeEntity = rnaInfoService.getOne(queryWrapper2);


        R ok = R.ok();
        ok.put("max" , null == maxPubTimeEntity ? 2021 : Integer.valueOf(maxPubTimeEntity.getPubTime()) );

        ok.put("min" , null == minPubTimeEntity ? 2020 : Integer.valueOf(minPubTimeEntity.getPubTime()) );
        return ok;
    }

    private List<Integer> getNumByPubTime(String column , Integer startYear , Integer endYear){
        QueryWrapper queryWrapper =
                new QueryWrapper<RnaCategoryEntity>()
                        .select( "pub_time  as pubTime, count(distinct "+column+ ") as dataNum" )
                        .eq("status" , 0 )
                        .ge("pub_time" , startYear)
                        .le("pub_time" , endYear)
                        .groupBy("pub_time")
                        .orderByDesc("pub_time")
                ;

        List<Map<String, Object>> list = rnaInfoService.listMaps(queryWrapper);

        Map<Integer , Integer> rMap = new HashMap<>();


        List<Integer> resList = new ArrayList();

        for(Map<String, Object> dataMap : list){
            rMap.put(Integer.valueOf("" + dataMap.get("pubTime")), Integer.valueOf("" +dataMap.get("dataNum")));
        }

        for(int i = startYear;  i<endYear ; i++ ){
            if(rMap.get(i)!=null){
                resList.add(rMap.get(i));
            }else{
                resList.add(0);
            }
        }

        return resList;
    }


    @GetMapping("/web/getCateGroyNumGroupByPubTime")
    public R getCateGroyNumGroupByPubTime(@RequestParam Map<String, Object> map){

        Integer startYear = Integer.valueOf((String) map.get("startYear")) ;
        Integer endYear = Integer.valueOf((String)map.get("endYear"));


        List<Integer> geneIdList = this.getNumByPubTime("gene_id", startYear, endYear);
        List<Integer> tissueOriginList = this.getNumByPubTime("tissue_origin" , startYear , endYear);
        List<Integer> cancerTypeList = this.getNumByPubTime("cancer_type" , startYear , endYear);
        List<Integer> suvivalList = this.getNumByPubTime("suvival" , startYear , endYear);
        List<Integer> geneTypeList = this.getNumByPubTime("gene_type" , startYear , endYear);

        R ok = R.ok();

        ok.put("geneIdList" , geneIdList);
        ok.put("tissueOriginList" , tissueOriginList);
        ok.put("cancerTypeList" , cancerTypeList);
        ok.put("geneTypeList" , geneTypeList);
        ok.put("suvivalList" , suvivalList);


        return ok;

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

    @GetMapping("/web/downloadByCategory4cvs")
    public void downloadByCategory4cvs(@RequestParam Map<String, Object> map ,HttpServletResponse response) throws IOException {
        String column = (String) map.get("column");
        String value = (String) map.get("value");
        //Map<String , Object> params = new HashMap<>(2);
        QueryWrapper<RnaInfoEntity> queryWrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty(column) && !StringUtils.isEmpty(value)){
            queryWrapper.like(column , value);
        }
        List list = rnaInfoService.list(queryWrapper);
        CsvExportParams exportParams = new CsvExportParams("utf-8");
        String fileName = "attachment;filename=download.cvs";
        response.setHeader("Content-Disposition", new String(fileName.getBytes(), "utf-8"));
        CsvExportUtil.exportCsv(exportParams , RnaInfoEntity.class , list, response.getOutputStream());

    }


}
