package com.kekecha.xiantu.mapper;

import com.kekecha.xiantu.domain.CameraPlatform;

import java.util.List;

public interface CameraMapper {
    List<CameraPlatform> selectAll(String filter);
    CameraPlatform selectById(int id);
    int insert(CameraPlatform cameraPlatform);
    int update(CameraPlatform cameraPlatform);
    int delete(int id);

    int getRefSite(String indexCode);
}
