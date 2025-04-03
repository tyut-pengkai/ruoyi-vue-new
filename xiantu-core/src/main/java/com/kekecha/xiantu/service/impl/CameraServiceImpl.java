package com.kekecha.xiantu.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.util.DateUtils;
import com.kekecha.xiantu.domain.CameraPlatform;
import com.kekecha.xiantu.domain.CameraInstance;
import com.kekecha.xiantu.domain.CameraToSite;
import com.kekecha.xiantu.mapper.CameraMapper;
import com.kekecha.xiantu.mapper.SiteMapper;
import com.kekecha.xiantu.service.ICameraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hikvision.artemis.sdk.ArtemisHttpUtil;
import com.hikvision.artemis.sdk.Client;
import com.hikvision.artemis.sdk.Request;
import com.hikvision.artemis.sdk.Response;
import com.hikvision.artemis.sdk.config.ArtemisConfig;
import com.hikvision.artemis.sdk.constant.Constants;
import com.hikvision.artemis.sdk.enums.Method;
import com.hikvision.artemis.sdk.constant.HttpSchema;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class CameraServiceImpl implements ICameraService {
    @Autowired
    CameraMapper cameraMapper;
    @Autowired
    SiteMapper siteMapper;

    private static final String GET_CAMERAS_STATUS_URL = "/artemis/api/nms/v1/online/camera/get";

    private static final String GET_PREVIEW_URL = "/artemis/api/video/v2/cameras/previewURLs";

    private static final String GET_PLAY_BACK_URL = "/artemis/api/video/v2/cameras/playbackURLs";

    private static final String CONTROL_URL = "/artemis/api/video/v1/ptzs/controlling";

    private static final String GET_CAMERAS_URL = "/artemis/api/resource/v1/cameras";

    private static final String GET_TOKEN_URL = "/artemis/api/v1/oauth/token";

    private static final String MONITOR_VIDEO_TOKEN = "MONITOR_VIDEO_TOKEN";

    public List<CameraPlatform> selectAll(String filter)
    {
        return cameraMapper.selectAll(filter);
    }

    public CameraPlatform selectById(int id) {
        return cameraMapper.selectById(id);
    }

    public int insert(CameraPlatform cameraPlatform)
    {
        return cameraMapper.insert(cameraPlatform);
    }

    public int update(CameraPlatform cameraPlatform)
    {
        return cameraMapper.update(cameraPlatform);
    }

    public int delete(int id)
    {
        return cameraMapper.delete(id);
    }

    public int linkCameraToSite(String indexCode, int siteId, String name, String platform, int platformId)
    {
        return siteMapper.refCameraToSite(indexCode, siteId, name, platform, platformId);
    }

    public int clearCameraLink(String indexCode)
    {
        return siteMapper.derefCameraToSite(indexCode);
    }

    public int getCameraLink(String indexCode)
    {
        return cameraMapper.getRefSite(indexCode);
    }

    public List<CameraInstance> getPlatformCameraInstances(CameraPlatform cameraPlatform)
    {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("pageNo", "1");
        paramMap.put("pageSize", "1000");

        String body = JSONObject.toJSONString(paramMap);
        int netProtocol = cameraPlatform.getNetProtocol();

        Map<String, String> path = new HashMap<String, String>();
        if (netProtocol == 0) {
            path.put(HttpSchema.HTTP, GET_CAMERAS_URL);
        } else {
            path.put(HttpSchema.HTTPS, GET_CAMERAS_URL);
        }

        String result = null;
        try {
            result = doPostStringArtemis(path, body, null, null, "application/json",
                    cameraPlatform.getPlatformUrl(), cameraPlatform.getAppKey(), cameraPlatform.getAppSecret());
            JSONObject camerasJson = JSONObject.parseObject(result);
            JSONArray cameraList = camerasJson.getJSONObject("data").getJSONArray("list");
            return cameraList.toList(CameraInstance.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getHikvisionPreviewURL(CameraPlatform cameraPlatform, String cameraIndexCode) {
        /* 当前写死，用户不需要配置 */
        String expand = "transcode=1&videotype=h264";
        String protocol = "hls";
        String streamType = "0";

        String host = cameraPlatform.getPlatformUrl();
        String appKey = cameraPlatform.getAppKey();
        String appSecret = cameraPlatform.getAppSecret();

        Map<String, String> path = new HashMap<String, String>(2) {
            {
                put(HttpSchema.HTTPS, GET_PREVIEW_URL);
            }
        };
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("cameraIndexCode", cameraIndexCode);
        jsonBody.put("protocol", protocol);
        jsonBody.put("streamType", streamType);
        jsonBody.put("transmode", "1");
        jsonBody.put("expand", expand);
        jsonBody.put("streamform", "ps");
        String body = jsonBody.toString();
        String result = doPostStringArtemis(path, body, null, null, "application/json", host, appKey, appSecret);
        JSONObject jsonResult = JSONObject.parseObject(result);
        if ("0".equals(jsonResult.get("code").toString())) {
            JSONObject data = jsonResult.getJSONObject("data");
            return data.get("url").toString();
        } else {
            if (jsonResult.get("msg").toString().contains("No media online")) {
                //{"code":"0x01b01302","msg":"No media online. [\"No media online. {groupIndexCode=default, groupName=mls.mediaGroup.name.default, groupType=null}," +
                //        "relatedAbility:[\\\"openApi\\\",\\\"transCode\\\",\\\"openApi\\\",\\\"notRtsp\\\"],netzoneCode:1\"]","data":null}
                throw new RuntimeException("摄像头离线");
            } else {
                throw new RuntimeException(result);
            }
        }
    }

    @Override
    public String getHikvisionPlaybackURL(
            CameraPlatform cameraPlatform, String cameraIndexCode, String beginTime, String endTime) {

        Date begin = DateUtils.parseDate(beginTime);
        Date end = DateUtils.parseDate(endTime);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        String beginDateStr = sdf.format(begin) + "+08:00"; // 手动添加时区
        String endDateStr = sdf.format(end) + "+08:00"; // 手动添加时区

        String host = cameraPlatform.getPlatformUrl();
        String appKey = cameraPlatform.getAppKey();
        String appSecret = cameraPlatform.getAppSecret();

        Map<String, String> path = new HashMap<String, String>(2) {
            {
                put(HttpSchema.HTTPS, GET_PLAY_BACK_URL);
            }
        };

        String protocol = "hls";

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("cameraIndexCode", cameraIndexCode);
        jsonBody.put("recordLocation", 0);
        jsonBody.put("protocol", protocol);
        jsonBody.put("transmode", 1);
        jsonBody.put("beginTime", beginDateStr);
        jsonBody.put("endTime", endDateStr);
        jsonBody.put("streamform", "ps");
        jsonBody.put("lockType", 0);
        String body = jsonBody.toString();
        String result = doPostStringArtemis(path, body, null, null, "application/json", host, appKey, appSecret);

        //{"code":"0","msg":"success","data":{"uuid":"","url":null,"list":null}}
        JSONObject jsonResult = JSONObject.parseObject(result);
        if ("0".equals(jsonResult.get("code").toString())) {
            JSONObject data = jsonResult.getJSONObject("data");
            JSONObject jsonUrl = data.getJSONObject("url");
            if (jsonUrl != null) {
                return jsonUrl.toString();
            } else {
                return null;
            }
        } else {
            throw new RuntimeException(result);
        }
    }

    @Override
    public Boolean hikvisionControlling(
            CameraPlatform cameraPlatform, String cameraIndexCode, Integer action, String command, Integer speed) {
        if (speed == null) {
            speed = 10;
        }

        String host = cameraPlatform.getPlatformUrl();
        String appKey = cameraPlatform.getAppKey();
        String appSecret = cameraPlatform.getAppSecret();

        Map<String, String> path = new HashMap<String, String>(2) {
            {
                put(HttpSchema.HTTPS, CONTROL_URL);
            }
        };
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("cameraIndexCode", cameraIndexCode);
        jsonBody.put("action", action);
        jsonBody.put("command", command);
        jsonBody.put("speed", speed);
        String body = jsonBody.toString();
        String result = doPostStringArtemis(path, body, null, null, "application/json", host, appKey, appSecret);

        JSONObject jsonResult = JSONObject.parseObject(result);
        if ("0".equals(jsonResult.get("code").toString())) {
            return true;
        } else {
            throw new RuntimeException(result);
        }
    }

    public static String doPostStringArtemis(Map<String, String> path, String body, Map<String, String> querys, String accept, String contentType, String host, String appKey, String appSecret) {
        String httpSchema = (String) path.keySet().toArray()[0];
        if (httpSchema != null && !httpSchema.isEmpty()) {
            String responseStr = null;
            try {
                Map<String, String> headers = new HashMap<>();
                if (accept != null && !accept.isEmpty()) {
                    headers.put("Accept", accept);
                } else {
                    headers.put("Accept", "*/*");
                }
                if (contentType != null && !contentType.isEmpty()) {
                    headers.put("Content-Type", contentType);
                } else {
                    headers.put("Content-Type", "application/text;charset=UTF-8");
                }
                Request request = new Request(Method.POST_STRING, httpSchema + host,
                        (String) path.get(httpSchema), appKey, appSecret, Constants.DEFAULT_TIMEOUT * 10);
                request.setHeaders(headers);
                request.setQuerys(querys);
                request.setStringBody(body);
                Response response = Client.execute(request);
                responseStr = getResponseResult(response);
            } catch (Exception var10) {
                throw new RuntimeException(var10);
            }
            return responseStr;
        } else {
            throw new RuntimeException("http和https参数错误httpSchema: " + httpSchema);
        }
    }

    private static String getResponseResult(Response response) {
        return response.getBody();
    }
}
