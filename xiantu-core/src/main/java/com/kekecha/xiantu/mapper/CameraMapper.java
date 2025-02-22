package com.kekecha.xiantu.mapper;

import com.kekecha.xiantu.domain.Camera;

import java.util.List;

public interface CameraMapper {
    List<Camera> selectAll(String filter);
    Camera selectByName(String name);
    List<Camera> selectByRef(String refer);
    int insert(Camera camera);
    int update(Camera camera);
    int delete(String name);
}
