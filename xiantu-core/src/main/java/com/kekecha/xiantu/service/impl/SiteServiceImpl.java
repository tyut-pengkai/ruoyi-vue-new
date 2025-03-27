package com.kekecha.xiantu.service.impl;

import com.kekecha.xiantu.domain.CameraToSite;
import com.kekecha.xiantu.domain.Site;
import com.kekecha.xiantu.mapper.SiteMapper;
import com.kekecha.xiantu.service.ISiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SiteServiceImpl implements ISiteService {
    @Autowired
    SiteMapper siteMapper;

    public List<Site> selectSite(int type, String city, String filter)
    {
        if (type == 1) {
            return siteMapper.selectNet(city, filter);
        } else if (type == 2) {
            return siteMapper.selectParking(city, filter);
        } else {
            return new ArrayList<>();
        }
    }

    public Site selectOne(int type, String name)
    {
        return siteMapper.selectOne(type, name);
    }

    public int insertSite(Site site)
    {
        return siteMapper.insertSite(site);
    }

    public int updateSite(Site site)
    {
        return siteMapper.updateSite(site);
    }

    public int deleteSite(int siteId)
    {
        siteMapper.clearSiteCamera(siteId);
        return siteMapper.deleteSite(siteId);
    }

    public List<CameraToSite> getSiteCamera(int id)
    {
        return siteMapper.selectRef(id);
    }
}
