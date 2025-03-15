package com.kekecha.xiantu.controller;

import com.kekecha.xiantu.service.IContentService;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/content")
public class ContentController extends BaseController {
    @Autowired
    private IContentService contentService;

    @Anonymous
    @GetMapping("")
    public AjaxResult getWebContent(@RequestParam(name="type") String type) {
        if (!(type == null || type.isEmpty())) {
            AjaxResult ajaxResult = AjaxResult.success("查询成功");
            ajaxResult.put(type, contentService.getContentByType(type));
            return ajaxResult;
        }
        AjaxResult ajaxResult = AjaxResult.success("查询成功");

        /* 查询QR CODE */
        ajaxResult.put("QRCode", contentService.getContentByType("QRCode"));
        /* 查询公司信息 */
        ajaxResult.put("companyInfo", contentService.getContentByType("companyInfo"));
        /* 查询联系电话 */
        ajaxResult.put("companyPhone", contentService.getContentByType("companyPhone"));
        return ajaxResult;
    }

    @Anonymous
    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody Map<String, Object> jsonMap) {
        try {

            String type = jsonMap.get("type").toString();
            String content = jsonMap.get("content").toString();

            if (contentService.saveToFile(type, content) == 0) {
                return AjaxResult.success("设置成功");
            } else {
                return AjaxResult.error("设置失败，请刷新重试");
            }
        } catch (Exception e) {
            return AjaxResult.error("发生错误，请刷新重试");
        }
    }
}
