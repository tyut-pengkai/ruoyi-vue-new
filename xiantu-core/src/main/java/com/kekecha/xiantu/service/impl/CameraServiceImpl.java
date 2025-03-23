package com.kekecha.xiantu.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.kekecha.xiantu.domain.Camera;
import com.kekecha.xiantu.mapper.CameraMapper;
import com.kekecha.xiantu.service.ICameraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import com.hikvision.artemis.sdk.ArtemisHttpUtil;
//import com.hikvision.artemis.sdk.Client;
//import com.hikvision.artemis.sdk.Request;
//import com.hikvision.artemis.sdk.Response;
//import com.hikvision.artemis.sdk.config.ArtemisConfig;
//import com.hikvision.artemis.sdk.constant.Constants;
//import com.hikvision.artemis.sdk.enums.Method;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CameraServiceImpl implements ICameraService {
    @Autowired
    CameraMapper cameraMapper;

    private static final String GET_PREVIEW_URL = "/artemis/api/video/v2/cameras/previewURLs";

    public List<Camera> selectAll(String filter)
    {
        return cameraMapper.selectAll(filter);
    }

    public Camera selectByName(String name) {
        return cameraMapper.selectByName(name);
    }
    public List<Camera> selectByRef(String refer) {
        return cameraMapper.selectByRef(refer);
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

//    public String getPriviewURL(Camera camera)
//    {
//        String platformUrl = null;
//        String appKey = null;
//        String appSecret = null;
//        String netProtocol = null;
//        String streamProtocol = null;
//
//        appKey = camera.getAppKey();
//        appSecret = camera.getAppSecret();
//        platformUrl = camera.getPlatformUrl();
//        netProtocol = camera.getNetProtocol();
//        streamProtocol = camera.getStreamProtocol();
//        String expand = "";
//
//        if ("ws".equals(streamProtocol)) {
//            expand = "transcode=0";
//        } else {
//            expand = "transcode=1&videotype=h264";
//        }
//
//        String path = GET_PREVIEW_URL;
//
//        JSONObject jsonBody = new JSONObject();
//        jsonBody.put("cameraIndexCode", indexCode);
//        jsonBody.put("protocol", netProtocol);
//        jsonBody.put("streamType", streamProtocol);
//        jsonBody.put("transmode", "1");
//        jsonBody.put("expand", expand);
//        jsonBody.put("streamform", "ps");
//        String body = jsonBody.toJSONString();
//        String result = doPostStringArtemis(path, body, null, null, "application/json", host, appKey, appSecret);
//        JSONObject jsonResult = JSONObject.parseObject(result);
//        if ("0".equals(jsonResult.get("code"))) {
//            JSONObject jsonData = JSONObject.parseObject(jsonResult.get("data").toString());
//            return jsonData.get("url").toString();
//        } else {
//            throw new RuntimeException(result);
//        }
//    }
//
//    private static String doPostStringArtemis(
//            String path, String body, Map<String, String> querys,
//            String accept, String contentType, String host, String appKey, String appSecret) {
//        if (path != null && !path.isEmpty()) {
//            String responseStr = null;
//            try {
//                Map<String, String> headers = new HashMap();
//                if (accept != null && !accept.isEmpty()) {
//                    headers.put("Accept", accept);
//                } else {
//                    headers.put("Accept", "*/*");
//                }
//
//                if (contentType != null && !contentType.isEmpty()) {
//                    headers.put("Content-Type", contentType);
//                } else {
//                    headers.put("Content-Type", "application/text;charset=UTF-8");
//                }
//
//                Request request = new Request(Method.POST_STRING, path + host, path, appKey, appSecret, 1000);
//                request.setHeaders(headers);
//                request.setQuerys(querys);
//                request.setStringBody(body);
//                Response response = Client.execute(request);
//                responseStr = getResponseResult(response);
//            } catch (Exception var10) {
//            }
//            return responseStr;
//        } else {
//            throw new RuntimeException("http和https参数错误httpSchema: " + httpSchema);
//        }
//    }
}
