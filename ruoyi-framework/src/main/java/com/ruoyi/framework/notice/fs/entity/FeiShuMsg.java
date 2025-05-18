package com.ruoyi.framework.notice.fs.entity;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 飞书消息
 *
 * {
 *    "open_id":"ou_5ad573a6411d72b8305fda3a9c15c70e",
 *    "root_id":"om_40eb06e7b84dc71c03e009ad3c754195",
 *    "chat_id":"oc_5ad11d72b830411d72b836c20",
 *    "user_id": "92e39a99",
 *    "email":"fanlv@gmail.com",
 *    "msg_type":"post",
 *    "content":{
 *         "post":{
 *          	"zh_cn":{}, // option
 *             "ja_jp":{}, // option
 *          	"en_us":{} // option
 *         }
 *    }
 * }
 *
 * @author liangyuqi
 * @date 2021/7/14 10:52
 */
@Data
public class FeiShuMsg {
    @JSONField(name = "open_id")
    private String openId;
    @JSONField(name = "root_id")
    private String rootId;
    @JSONField(name = "chat_id")
    private String chatId;
    @JSONField(name = "user_id")
    private String userId;
    private String email;
    @JSONField(name = "msg_type")
    private String msgType;
    private Content content;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Content {
        private Post post;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Post {
        @JSONField(name = "zh_cn")
        private ZhCn zhCn;
    }

    @Data
    public static class ZhCn {
        private String title;
        private List<List<BaseField>> content;

    }

    @Data
    public static abstract class BaseField {
        private String tag;
    }
}
