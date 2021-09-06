package io.renren.modules.rna.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.utils.R;
import io.renren.modules.rna.service.RnaInfoService;
import io.renren.modules.rna.service.RnaSearchRecordService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @className: RnaStatisticController
 * @description: 统计分析
 * @author: hy
 * @date: 2020-10-28
 **/
@RestController
@RequestMapping("/web/statistic")
@Api("rna统计")
public class RnaStatisticController {

    @Autowired
    RnaInfoService rnaInfoService;
    @Autowired
    RnaSearchRecordService rnaSearchRecordService;


    @GetMapping("getCntByCol/{col}")
    public R getCntByCol(@PathVariable String col) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("status", 0);
        queryWrapper.ne(col , "");
        queryWrapper.isNotNull(col);
        queryWrapper.select(col, "count(*) as cnt")

                .groupBy(col);
        return R.ok().put("res", rnaInfoService.queryByMap(queryWrapper));

    }

    @GetMapping("searchCnt")
    public R searchCnt() {
        List<String> dateList = new ArrayList<>(7);
        for (int i = 6; i >= 0; i--) {
            dateList.add(getPastDate(i));
        }
        Map<String, List<Integer>> map = new HashMap<>(4);
        map.put("gen", cntListByCol("gen", dateList));
        map.put("cancer", cntListByCol("cancer", dateList));
        map.put("immune_cell", cntListByCol("immune_cell", dateList));
        map.put("immune_pathway", cntListByCol("immune_pathway", dateList));
        return R.ok().put("searchCnt", map).put("date", dateList);

    }


    private List<Integer> cntListByCol(String searchCol, List<String> dateList) {
        //查询gen的数据
        QueryWrapper genWrapper = new QueryWrapper();
        genWrapper.select("count(*) as cnt , DATE_FORMAT(ct , '%Y-%m-%d') as ct");
        genWrapper.ge("DATE_FORMAT(ct , '%Y-%m-%d')", dateList.get(0));
        genWrapper.le("DATE_FORMAT(ct , '%Y-%m-%d')", dateList.get(dateList.size()-1));
        genWrapper.ne(searchCol, "##");
        genWrapper.groupBy("DATE_FORMAT(ct , '%Y-%m-%d')");
        List<Map<String, Object>> genSerList = rnaSearchRecordService.queryByMap(genWrapper);
        List<Integer> cntList = new ArrayList<>(7);
        for (String date : dateList) {
            cntList.add(getCnt(date , genSerList));
        }
        return cntList;
    }




    private Integer getCnt(String date , List<Map<String, Object>> genSerList){
        for(Map<String, Object> map : genSerList){
            if(date.equals(map.get("ct"))){
                return Integer.valueOf(String.valueOf(map.get("cnt")));
            }
        }

        return 0;

    }


    private String getPastDate(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String result = format.format(today);
        return result;


    }

}
