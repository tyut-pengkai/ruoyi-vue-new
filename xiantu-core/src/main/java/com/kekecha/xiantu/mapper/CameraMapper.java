package com.kekecha.xiantu.mapper;

import com.kekecha.xiantu.domain.CameraPlatform;

import java.util.List;

public interface CameraMapper {
    List<CameraPlatform> selectAll(String filter);
    CameraPlatform selectByName(String name);
    List<CameraPlatform> selectByRef(String refer);
    int insert(CameraPlatform cameraPlatform);
    int update(CameraPlatform cameraPlatform);
    int delete(String name);
}
