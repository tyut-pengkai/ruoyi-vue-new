package com.ruoyi.framework.notice.fs.entity;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

/**
 * @author liangyuqi
 * @date 2021/7/14 15:20
 */
@Data
public class FeiShuAtField extends FeiShuMsg.BaseField {
    private String tag = "at";
    @JSONField(name = "user_id")
    private String userId;

    public static FeiShuAtField createAtAll() {
        return createAt("all");
    }

    public static FeiShuAtField createAt(String userId) {
        FeiShuAtField feiShuAtField = new FeiShuAtField();
        feiShuAtField.setUserId(userId);
        return feiShuAtField;
    }
}
