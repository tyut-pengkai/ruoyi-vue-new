package com.ruoyi.framework.notice.fs;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.framework.notice.AbstractNotice;
import com.ruoyi.framework.notice.fs.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author liangyq
 * @date 2025-05-18 17:14
 */
@Slf4j
@Component
public class FsNotice extends AbstractNotice {

    public static final String FEI_SHU_ROBOT_TOKEN_CACHE_PREFIX = "FS_ROBOT_TOKEN_";
    public static final String FEI_SHU_ROBOT_TOKEN_REQUEST_URL = "https://open.feishu.cn/open-apis/auth/v3/tenant_access_token/internal/";
    public static final String FEI_SHU_ROBOT_SEND_MSG_URL = "https://open.feishu.cn/open-apis/message/v4/send/";

    @Value("${fs.robot.appId:cli_a8a3175e84f8500b}")
    private String defaultAppId;
    @Value("${fs.robot.appSecret:AZTpyNiW4hpLRXcDhrdjJcr8Z404gVqf}")
    private String defaultAppSecret;
    @Value("${fs.robot.defaultChatId:oc_34e207df88addd15d83ec274fda95ef6}")
    private String defaultChatId;
    @Value("${fs.robot.monitorSwitch}")
    private Boolean monitorSwitch;
    @Value("${fs.robot.monitorChartId}")
    private String monitorChartId;
    @Value("${fs.robot.monitorEnv}")
    private String env;

    /**
     * 发送消息给默认会话
     *
     * @param title
     * @param content
     */
    public void sendMsg2DefaultChat(String title, String content) {
        if (Boolean.TRUE.equals(monitorSwitch)) {
            ThreadUtil.execAsync(() -> sendMsg(CharSequenceUtil.format("[{}]{}", env, title), content,
                    new String[]{defaultChatId}));
        }
    }

    /**
     * 发送异常信息给监控群
     *
     * @param e
     */
    public <T extends Exception> void sendException2MonitorChat(T e) {
        sendException2MonitorChat(null, e);
    }

    /**
     * 发送异常信息给监控群
     *
     * @param e
     */
    public <T extends Exception> void sendException2MonitorChat(String uri, T e) {
        if (Boolean.TRUE.equals(monitorSwitch)) {
            ThreadUtil.execAsync(() -> {
                try {
                    //内容
                    FeiShuMsg.ZhCn zhCn = new FeiShuMsg.ZhCn();
                    List<List<FeiShuMsg.BaseField>> contentFields = new ArrayList<>(5);
                    contentFields.add(Collections.singletonList(FeiShuTextField
                            .createText(CharSequenceUtil.format("环境：{}", env))));
                    if (uri != null) {
                        contentFields.add(Collections.singletonList(FeiShuTextField
                                .createText(CharSequenceUtil.format("地址：{}", uri))));
                    }
                    contentFields.add(Collections.singletonList(FeiShuTextField
                            .createText(CharSequenceUtil.format("时间：{}", DateUtil.now()))));
                    contentFields.add(Collections.singletonList(FeiShuTextField
                            .createText(CharSequenceUtil.format("异常：{}", e.getClass().getName()))));
                    contentFields.add(Collections.singletonList(FeiShuTextField
                            .createText(CharSequenceUtil.format("错误信息：{}", StrUtil.truncateUtf8(e.getMessage(), 512)))));
                    zhCn.setContent(contentFields);
                    //消息体
                    FeiShuMsg feiShuMsg = new FeiShuMsg();
                    feiShuMsg.setChatId(monitorChartId);
                    feiShuMsg.setMsgType("post");
                    feiShuMsg.setContent(FeiShuMsg.Content.builder().post(FeiShuMsg.Post.builder().zhCn(zhCn).build()).build());
                    //发送
                    httpPost(FEI_SHU_ROBOT_SEND_MSG_URL, createDefaultHeader(), JSON.toJSONString(feiShuMsg));
                } catch (Exception ex) {
                    log.error("[FS]发送消息异常", ex);
                }
            });
        }
    }


    /**
     * 发送消息
     *
     * @param title
     * @param content
     * @param chartIds
     * @param params
     */
    private void sendMsg(String title, String content, String[] chartIds, Object... params) {
        try {
            List<FeiShuMsg> msgs = createFeiShuMsg(title, content, chartIds, params);
            for (FeiShuMsg msg : msgs) {
                String response = httpPost(FEI_SHU_ROBOT_SEND_MSG_URL, createDefaultHeader(), JSON.toJSONString(msg));
                log.info("[FS]发送消息: msg={}, response={}", msg, response);
            }
        } catch (Exception e) {
            log.error("[FS]发送消息异常", e);
        }
    }

    /**
     * 创建飞书消息
     *
     * @param title
     * @param content
     * @param chartIds
     * @param params
     * @return
     */
    private List<FeiShuMsg> createFeiShuMsg(String title, String content, String[] chartIds, Object... params) {
        List<FeiShuMsg> msgs = new ArrayList<>(chartIds.length);
        for (String chartId : chartIds) {
            //标题和内容
            FeiShuMsg.ZhCn zhCn = new FeiShuMsg.ZhCn();
            zhCn.setTitle(title);

            String textContent = ArrayUtil.isNotEmpty(params) ? StrUtil.indexedFormat(content, params) : content;
            List<List<FeiShuMsg.BaseField>> contentField = Collections.singletonList(Arrays
                    .asList(FeiShuTextField.createText(textContent), FeiShuAtField.createAtAll()));
            zhCn.setContent(contentField);
            //消息体
            FeiShuMsg feiShuMsg = new FeiShuMsg();
            feiShuMsg.setChatId(chartId);
            feiShuMsg.setMsgType("post");
            feiShuMsg.setContent(FeiShuMsg.Content.builder().post(FeiShuMsg.Post.builder().zhCn(zhCn).build()).build());
            msgs.add(feiShuMsg);
        }
        return msgs;
    }


    /**
     * 飞书请求头
     *
     * @return
     */
    private Map<String, String> createDefaultHeader() {
        return createHeaderWithAuth(getToken(defaultAppId, defaultAppSecret));
    }

    /**
     * 创建带有认证信息的请求头
     *
     * @param token 飞书token
     * @return
     */
    private Map<String, String> createHeaderWithAuth(String token) {
        Map<String, String> headers = new HashMap<>(2);
        headers.put("Content-Type", "application/json; charset=utf-8");
        if (StrUtil.isNotEmpty(token)) {
            //值格式："Bearer access_token"
            headers.put("Authorization", "Bearer ".concat(token));
        }
        return headers;
    }

    /**
     * 获取token
     *
     * @param appId     应用id
     * @param appSecret 应用秘钥
     * @return token
     */
    private String getToken(String appId, String appSecret) {
        String token = redisCache.getCacheObject(FEI_SHU_ROBOT_TOKEN_CACHE_PREFIX.concat(appId));
        if (StrUtil.isEmpty(token)) {
            synchronized (this) {
                token = redisCache.getCacheObject(FEI_SHU_ROBOT_TOKEN_CACHE_PREFIX.concat(appId));
                if (StrUtil.isEmpty(token)) {
                    token = refreshToken(appId, appSecret);
                }
            }
        }
        return token;
    }

    /**
     * 从飞书服务器获取token，并刷新缓存
     *
     * @param appId     应用id
     * @param appSecret 秘钥
     * @return
     */
    private String refreshToken(String appId, String appSecret) {
        String response = httpPost(FEI_SHU_ROBOT_TOKEN_REQUEST_URL, null,
                JSON.toJSONString(FeiShuTokenReq.builder().appId(appId).appSecret(appSecret).build()));
        FeiShuTokenResp respToken = StrUtil.isEmpty(response) ?
                null : JSON.parseObject(response, FeiShuTokenResp.class);
        if (Objects.isNull(respToken) || StrUtil.isEmpty(respToken.getTenantAccessToken())) {
            throw new ServiceException("[FS]TOKEN获取失败");
        }
        String token = respToken.getTenantAccessToken();
        //Token 有效期为 2 小时，在此期间调用该接口 token 不会改变。
        //当 token 有效期小于 30 分的时候，再次请求获取 token 的时候，会生成一个新的 token，与此同时老的 token 依然有效。
        int expire = Integer.parseInt(respToken.getExpire()) - 30 * 60;
        redisCache.setCacheObject(FEI_SHU_ROBOT_TOKEN_CACHE_PREFIX.concat(appId), token, expire > 0 ? expire : 1,
                TimeUnit.SECONDS);
        log.info("[FS]TOKEN缓存刷新成功appId={}，expire={}", appId, expire);
        return token;
    }
}
