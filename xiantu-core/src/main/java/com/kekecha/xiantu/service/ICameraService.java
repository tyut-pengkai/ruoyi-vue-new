package com.kekecha.xiantu.service;

import com.kekecha.xiantu.domain.Camera;

import java.util.List;

public interface ICameraService {
    List<Camera> selectAll(String filter);
    Camera selectByName(String name);
    int insert(Camera camera);
    int update(Camera camera);
    int delete(String name);
}
