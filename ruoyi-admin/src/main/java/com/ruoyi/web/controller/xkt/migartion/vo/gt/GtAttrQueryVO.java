package com.ruoyi.web.controller.xkt.migartion.vo.gt;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

/**
 * @author liangyq
 * @date 2025-05-11 23:46
 */
@Data
public class GtAttrQueryVO {

   @NotBlank(message = "urlPrefix不能为空!")
   private String urlPrefix;
   private String accept;
   private String acceptEncoding;
   private String acceptLanguage;
   private String connection;
   private String cookie;
   private String host;
   private String refererPrefix;
   private String requestedWith;

}
