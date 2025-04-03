package com.kekecha.xiantu.mapper;

import com.kekecha.xiantu.domain.CameraToSite;
import com.kekecha.xiantu.domain.Site;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;

public interface SiteMapper {
    List<Site> selectAll(@Param("city") String city, @Param("filter") String filter);
    List<Site> selectNet(@Param("city") String city, @Param("filter") String filter);
    List<Site> selectParking(@Param("city") String city, @Param("filter") String filter);

    Site selectOne(@Param("type") int type, @Param("name") String name);
    int insertSite(Site site);
    int updateSite(Site site);
    int deleteSite(int id);

    int refCameraToSite(
            @Param("indexCode")String indexCode, @Param("siteId") int siteId,
            @Param("name") String name, @Param("platform") String platform, @Param("platformId") int platformId);
    int derefCameraToSite(String indexCode);

    int clearSiteCamera(@Param("siteId") int siteId);
    List<CameraToSite> selectRef(@Param("siteId") int siteId);
}
