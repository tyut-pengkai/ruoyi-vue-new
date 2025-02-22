package com.kekecha.xiantu.service;

import com.kekecha.xiantu.domain.Site;

import java.util.List;

public interface ISiteService {
    List<Site> selectSite(int type, String city, String filter);
    Site selectOne(int type, String name);

    int insertSite(Site site);
    int updateSite(Site site);
    int deleteSite(String name);
}
