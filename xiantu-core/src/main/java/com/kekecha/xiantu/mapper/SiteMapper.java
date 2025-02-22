package com.kekecha.xiantu.mapper;

import com.kekecha.xiantu.domain.Site;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SiteMapper {
    List<Site> selectAll(@Param("city") String city, @Param("filter") String filter);
    List<Site> selectNet(@Param("city") String city, @Param("filter") String filter);
    List<Site> selectParking(@Param("city") String city, @Param("filter") String filter);

    Site selectOne(@Param("type") int type, @Param("name") String name);
    int insertSite(Site site);
    int updateSite(Site site);
    int deleteSite(String name);
}
