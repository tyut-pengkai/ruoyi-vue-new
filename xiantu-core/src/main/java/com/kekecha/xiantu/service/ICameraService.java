package com.kekecha.xiantu.service;

import com.kekecha.xiantu.domain.CameraInstance;
import com.kekecha.xiantu.domain.CameraPlatform;

import java.util.List;

public interface ICameraService {
    List<CameraPlatform> selectAll(String filter);
    CameraPlatform selectByName(String name);
    List<CameraPlatform> selectByRef(String refer);
    int insert(CameraPlatform cameraPlatform);
    int update(CameraPlatform cameraPlatform);
    int delete(String name);

    int linkCameraToSite(String indexCode, int siteId);
    int clearCameraLink(String indexCode);

    List<CameraInstance> getPlatformCameraInstances(CameraPlatform cameraPlatform);
    String getHikvisionPreviewURL(CameraPlatform cameraPlatform, String cameraIndexCode);
    String getHikvisionPlaybackURL(
            CameraPlatform cameraPlatform, String cameraIndexCode, String beginTime, String endTime);
    Boolean hikvisionControlling(
            CameraPlatform cameraPlatform, String cameraIndexCode, Integer action, String command, Integer speed);
}
