package com.kekecha.xiantu.service.impl;

import com.kekecha.xiantu.domain.Camera;
import com.kekecha.xiantu.mapper.CameraMapper;
import com.kekecha.xiantu.service.ICameraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CameraServiceImpl implements ICameraService {
    @Autowired
    CameraMapper cameraMapper;

    public List<Camera> selectAll(String filter)
    {
        return cameraMapper.selectAll(filter);
    }

    public Camera selectByName(String name) {
        return cameraMapper.selectByName(name);
    }

    public int insert(Camera camera)
    {
        return cameraMapper.insert(camera);
    }

    public int update(Camera camera)
    {
        return cameraMapper.update(camera);
    }

    public int delete(String name)
    {
        return cameraMapper.delete(name);
    }
}
