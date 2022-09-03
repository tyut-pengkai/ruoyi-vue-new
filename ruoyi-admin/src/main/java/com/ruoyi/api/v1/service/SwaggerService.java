package com.ruoyi.api.v1.service;

import com.ruoyi.api.v1.constants.ApiDefine;
import com.ruoyi.api.v1.constants.Constants;
import com.ruoyi.api.v1.domain.*;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.SysWebsite;
import com.ruoyi.system.service.ISysWebsiteService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class SwaggerService {

    @Value("${token.header}")
    private String tokenKey;
    @Value("${swagger.pathMapping}")
    private String pathMapping;
    @Resource
    private RuoYiConfig config;
    @Resource
    private ISysWebsiteService sysWebsiteService;

    public SwaggerVo getSwaggerInfo(HttpServletRequest request) {
        SwaggerVo<String, Object> swagger = new SwaggerVo<>();
        swagger.put("swagger", "2.0");
        Map<String, Object> infoObj = new HashMap<>();
        infoObj.put("description", "开放Api接口文档");
        infoObj.put("version", config.getVersion());
        infoObj.put("title", "开放API接口文档");
        Map<String, Object> contactObj = new HashMap<>();
        contactObj.put("name", config.getName());
        contactObj.put("url", config.getUrl());
        contactObj.put("email", config.getEmail());
        infoObj.put("contact", contactObj);
        swagger.put("info", infoObj);
//        swagger.put("host", request.getServerName() + ("80".equals(String.valueOf(request.getServerPort())) ? "" : ":" + request.getServerPort()));
        swagger.put("host", getHost());
        swagger.put("basePath", pathMapping);
        List<Map<String, Object>> tagsList = new ArrayList<>();
        for (String tag : Constants.API_TAG_ALL) {
            Map<String, Object> tagsObj = new HashMap<>();
            tagsObj.put("name", tag);
            tagsObj.put("description", tag);
            tagsList.add(tagsObj);
        }
        swagger.put("tags", tagsList);
        Map<String, Object> pathsObj = new TreeMap<>();
        Map<String, Object> definitionsObj = new HashMap<>();

        Map<String, Object> definitionObj = new HashMap<>();
        definitionObj.put("type", "object");
        Map<String, Object> resultPropertiesObj = new LinkedHashMap<>();
        Map<String, Object> codeObj = new HashMap<>();
        codeObj.put("type", "integer");
        codeObj.put("format", "int64");
        codeObj.put("description", "执行成功返回200，否则返回错误码");
        resultPropertiesObj.put("code", codeObj);
        Map<String, Object> dataObj = new HashMap<>();
        dataObj.put("type", "string");
        dataObj.put("description", "执行结果，可能为字符串或json对象");
        resultPropertiesObj.put("data", dataObj);
        Map<String, Object> msgObj = new HashMap<>();
        msgObj.put("type", "string");
        msgObj.put("description", "结果说明");
        resultPropertiesObj.put("msg", msgObj);
        Map<String, Object> signObj = new HashMap<>();
        signObj.put("type", "string");
        signObj.put("description", "数据签名");
        resultPropertiesObj.put("sign", signObj);
        Map<String, Object> timestampObj = new HashMap<>();
        timestampObj.put("type", "string");
        timestampObj.put("description", "结果生成时间");
        resultPropertiesObj.put("timestamp", timestampObj);

        definitionObj.put("properties", resultPropertiesObj);
        definitionObj.put("title", "AjaxResult");

        for (Map.Entry<String, Api> apiDefine : ApiDefine.apiMap.entrySet()) {
            Api api = apiDefine.getValue();
            Map<String, Object> pathObj = new HashMap<>();
            Map<String, Object> postObj = new HashMap<>();
            postObj.put("tags", api.getTags());
            postObj.put("summary", api.getName());
            postObj.put("description", api.getDescription());
            postObj.put("operationId", api.getApi() + "UsingPOST");
            postObj.put("consumes", new String[]{"application/json"});
            postObj.put("produces", new String[]{"application/json;charset=UTF-8"});
            List<Map<String, Object>> parametersList = new ArrayList<>();
            if (api.isCheckToken()) {
                Map<String, Object> parametersTokenObj = new HashMap<>();
                parametersTokenObj.put("in", "header");
                parametersTokenObj.put("name", tokenKey);
                parametersTokenObj.put("description", "token标记");
                parametersTokenObj.put("required", true);
                parametersTokenObj.put("type", "string");
                parametersList.add(parametersTokenObj);
            }
            Map<String, Object> parametersAppidObj = new HashMap<>();
            parametersAppidObj.put("in", "path");
            parametersAppidObj.put("name", "appkey");
            parametersAppidObj.put("description", "AppKey");
            parametersAppidObj.put("required", true);
            parametersAppidObj.put("type", "string");
            parametersList.add(parametersAppidObj);

            Map<String, Object> responsesObj = new HashMap<>();
            Map<String, Object> response200Obj = new HashMap<>();
            response200Obj.put("description", "请求完成");
            Map<String, Object> response200SchemaObj = new HashMap<>();
            String ajaxResultVoName = StringUtils.capitalize(api.getApi()) + "AjaxResultVo";
            response200SchemaObj.put("$ref", "#/definitions/" + ajaxResultVoName);

            Map<String, Object> resultPropertiesObjTemp = new LinkedHashMap<>(resultPropertiesObj);
            Map<String, Object> definitionObjTemp = new HashMap<>(definitionObj);
            definitionObjTemp.put("title", ajaxResultVoName);
            definitionObjTemp.put("properties", resultPropertiesObjTemp);

            if (api.isCheckToken()) {
                Map<String, Object> vstrObj = new HashMap<>();
                vstrObj.put("type", "string");
                vstrObj.put("description", "用作标记或验证的冗余数据，与输入保持一致");
                resultPropertiesObjTemp.put("vstr", vstrObj);
            }

            // 定义api返回的data
            if (api.getResp() != null) {
                Map<String, Object> dataDetailObj = new HashMap<>(dataObj);
                recursion(definitionsObj, api.getResp().getDataType(), api.getResp().getDescription(), api.getResp().getItems(), ajaxResultVoName + "Data", dataDetailObj);
//                if (api.getResp().getDataType() == Resp.DataType.object || api.getResp().getDataType() == Resp.DataType.array) {
//                    if(api.getResp().getDataType() == Resp.DataType.object) {
//                        dataDetailObj.remove("type");
//                    } else {
//                        dataDetailObj.put("type", api.getResp().getDataType().name());
//                    }
//                    dataDetailObj.put("$ref", "#/definitions/" + ajaxResultVoName + "Data");
//                    dataDetailObj.put("originalRef", ajaxResultVoName + "Data");
//                    if (api.getResp().getItems() != null) {
//                        Map<String, Object> defineObj = new HashMap<>();
//                        Map<String, Object> propertiesObj = new LinkedHashMap<>();
//                        for (RespItem item : api.getResp().getItems()) {
//                            Map<String, Object> itemObj = new HashMap<>();
//                            itemObj.put("type", item.getDataType().name());
//                            itemObj.put("description", item.getDescription());
//                            propertiesObj.put(item.getName(), itemObj);
//                        }
//                        defineObj.put("type", "object");
//                        defineObj.put("properties", propertiesObj);
//                        defineObj.put("title", ajaxResultVoName + "Data");
//                        definitionsObj.put(ajaxResultVoName + "Data", defineObj);
//                    }
//                } else {
//                    dataDetailObj.put("type", api.getResp().getDataType().name());
//                    dataDetailObj.put("description", api.getResp().getDescription());
//                }
                resultPropertiesObjTemp.put("data", dataDetailObj);
            }
            definitionsObj.put(ajaxResultVoName, definitionObjTemp);

            response200Obj.put("schema", response200SchemaObj);
            responsesObj.put("200", response200Obj);
            postObj.put("responses", responsesObj);
            postObj.put("deprecated", false);
            pathObj.put("post", postObj);
            if (api.isCheckToken()) {
//                pathsObj.put("/api/v1/{appkey}/auth?" + api.getApi(), pathObj);
                pathsObj.put("/api/v1/{appkey}?" + api.getApi(), pathObj);
            } else {
                if (api.getApi().equals("calcSign")) {
                    pathsObj.put("/api/v1/devTool/{appkey}/calcSign", pathObj);
                } else {
//                    pathsObj.put("/api/v1/{appkey}/noAuth?" + api.getApi(), pathObj);
                    pathsObj.put("/api/v1/{appkey}?" + api.getApi(), pathObj);
                }

            }
            postObj.put("parameters", parametersList);
            Map<String, Object> propertiesObj = new LinkedHashMap<>();
            List<String> requiredList = new ArrayList<>();
            if (api.isCheckToken()) {
                for (Param param : ApiDefine.publicParamsAuth) {
                    Map<String, Object> paramObj = new HashMap<>();
                    paramObj.put("type", "string");
                    paramObj.put("description", "[公共]" + param.getDescription().replace("${api}", api.getApi()));
                    propertiesObj.put(param.getName(), paramObj);
                    if (param.isNecessary()) {
                        requiredList.add(param.getName());
                    }
                }
            } else {
                for (Param param : ApiDefine.publicParamsNoAuth) {
                    Map<String, Object> paramObj = new HashMap<>();
                    paramObj.put("type", "string");
                    paramObj.put("description", "[公共]" + param.getDescription().replace("${api}", api.getApi()));
                    propertiesObj.put(param.getName(), paramObj);
                    if (param.isNecessary()) {
                        requiredList.add(param.getName());
                    }
                }
            }
            for (Param param : api.getParams()) {
                Map<String, Object> paramObj = new HashMap<>();
                paramObj.put("type", "string");
                paramObj.put("description", "[私有]" + param.getDescription());
                propertiesObj.put(param.getName(), paramObj);
                if (param.isNecessary()) {
                    requiredList.add(param.getName());
                }
            }
//            if (api.isCheckToken() || api.getParams().size() > 0) {
//            if (!"calcSign".equals(api.getApi())) {
            Map<String, Object> parametersObj = new HashMap<>();
            parametersObj.put("in", "body");
            parametersObj.put("name", "params");
            parametersObj.put("description", "请求参数");
            parametersObj.put("required", true);
            parametersList.add(parametersObj);
            Map<String, Object> schemaObj = new HashMap<>();
            String defineVoName = StringUtils.capitalize(api.getApi()) + "ParamsVo";
            schemaObj.put("$ref", "#/definitions/" + defineVoName);
            parametersObj.put("schema", schemaObj);

            Map<String, Object> defineObj = new HashMap<>();
            defineObj.put("type", "object");
            defineObj.put("required", requiredList.toArray());
            defineObj.put("properties", propertiesObj);
            defineObj.put("title", defineVoName);
            definitionsObj.put(defineVoName, defineObj);
//            }
//            }
        }
        swagger.put("paths", pathsObj);
//        Map<String, Object> definitionObj = new HashMap<>();
//        definitionObj.put("type", "object");
//        Map<String, Object> resultPropertiesObj = new LinkedHashMap<>();
//        Map<String, Object> codeObj = new HashMap<>();
//        codeObj.put("type", "integer");
//        codeObj.put("format", "int64");
//        codeObj.put("description", "执行成功返回200，否则返回错误码");
//        resultPropertiesObj.put("code", codeObj);
//        Map<String, Object> dataObj = new HashMap<>();
//        dataObj.put("type", "string");
//        dataObj.put("description", "执行结果，可能为字符串或json对象");
//        resultPropertiesObj.put("data", dataObj);
//        Map<String, Object> msgObj = new HashMap<>();
//        msgObj.put("type", "string");
//        msgObj.put("description", "结果说明");
//        resultPropertiesObj.put("msg", msgObj);
//        Map<String, Object> timestampObj = new HashMap<>();
//        timestampObj.put("type", "string");
//        timestampObj.put("description", "结果生成时间");
//        resultPropertiesObj.put("timestamp", timestampObj);
//
//        definitionObj.put("properties", resultPropertiesObj);
//        definitionObj.put("title", "AjaxResult");
//        definitionsObj.put("AjaxResultNoAuth", definitionObj);
//
//        Map<String, Object> definitionObjAuth = new HashMap<>(definitionObj);
//        Map<String, Object> resultPropertiesObjAuth = new LinkedHashMap<>(resultPropertiesObj);
//        Map<String, Object> vstrObj = new HashMap<>();
//        vstrObj.put("type", "string");
//        vstrObj.put("description", "用作标记或验证的冗余数据，与输入保持一致");
//        resultPropertiesObjAuth.put("vstr", vstrObj);
//        definitionObjAuth.put("properties", resultPropertiesObjAuth);
//        definitionsObj.put("AjaxResultAuth", definitionObjAuth);

        swagger.put("definitions", definitionsObj);
        return swagger;
    }

    private String getHost() {
        HttpServletRequest request = ServletUtils.getRequest();
        String port = "80".equals(String.valueOf(request.getServerPort())) ? "" : ":" + request.getServerPort();
        port = pathMapping.contains("dev") ? "" : port;
        String url = "";
        SysWebsite website = sysWebsiteService.getById(1);
        if (website != null && StringUtils.isNotBlank(website.getDomain())) {
            url = website.getDomain();
        } else {
            url = request.getScheme() + "://" + request.getServerName() + port;
        }
        if (url.endsWith("/")) {
            url = url.substring(0, url.length() - 1);
        }
        return url;
    }

    private void recursion(Map<String, Object> definitionsObj, Resp.DataType dataType, String desc, RespItem[] items, String voName, Map<String, Object> obj) {
        if (dataType == Resp.DataType.object || dataType == Resp.DataType.array) {
            if (dataType == Resp.DataType.object) {
                obj.remove("type");
            } else {
                obj.put("type", dataType.name());
            }
            obj.put("$ref", "#/definitions/" + voName);
            obj.put("originalRef", voName);
            obj.put("description", desc);
            if (items != null && items.length > 0) {
                Map<String, Object> defineObj = new HashMap<>();
                Map<String, Object> propertiesObj = new LinkedHashMap<>();
                for (RespItem item : items) {
                    Map<String, Object> itemObj = new HashMap<>();
                    itemObj.put("type", item.getDataType().name());
                    itemObj.put("description", item.getDescription());
                    recursion(definitionsObj, item.getDataType(), item.getDescription(), item.getItems(), voName + StringUtils.capitalize(item.getName()), itemObj);
                    propertiesObj.put(item.getName(), itemObj);
                }
                defineObj.put("type", "object");
                defineObj.put("properties", propertiesObj);
                defineObj.put("title", voName);
                definitionsObj.put(voName, defineObj);
            }
        } else {
            obj.put("type", dataType.name());
            obj.put("description", desc);
        }
    }

}
