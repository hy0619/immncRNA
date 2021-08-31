package io.renren.modules.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.modules.web.dao.RnaWebNavDao;
import io.renren.modules.web.entity.RnaWebNavEntity;
import io.renren.modules.web.service.RnaWebNavService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @className: RnaWebNavServiceImpl
 * @description: web导航sercie
 * @author: hy
 * @date: 2020-10-4
 **/
@Service
public class RnaWebNavServiceImpl extends ServiceImpl<RnaWebNavDao, RnaWebNavEntity> implements RnaWebNavService {
    @Override
    public List<RnaWebNavEntity> getNavTreeList() {
        List<RnaWebNavEntity> navs = this.list();
        return generateNavsTree(navs);
    }

    /**
     * 构造网站导航tree
     * @param allNavs
     */
    private List<RnaWebNavEntity> generateNavsTree(List<RnaWebNavEntity> allNavs){
        //一级导航
        List<RnaWebNavEntity> topNavList = new ArrayList<>();
        for(RnaWebNavEntity nav : allNavs){
            if(nav.getParentId() != null && nav.getParentId().equals(0L)){
                topNavList.add(nav);
            }
            for(RnaWebNavEntity childNav : allNavs){
                List<RnaWebNavEntity> children = new ArrayList<>();
                if(childNav.getParentId()!=null && childNav.getParentId().equals(nav.getNavId())){
                    children.add(childNav);
                }
                if(!CollectionUtils.isEmpty(children)) nav.setChildren(children);
            }
        }

        return topNavList;
    }
}
