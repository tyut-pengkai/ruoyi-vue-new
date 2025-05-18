package com.ruoyi.framework.notice.fs.entity;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

/**
 * @author liangyuqi
 * @date 2021/7/14 15:20
 */
@Data
public class FeiShuTextField extends FeiShuMsg.BaseField {
    @JSONField(name = "un_escape")
    private boolean unEscape = true;
    private String tag = "text";
    private String text;

    public static FeiShuTextField createText(String text) {
        FeiShuTextField feiShuTextField = new FeiShuTextField();
        feiShuTextField.setText(text);
        return feiShuTextField;
    }
}
