package com.ruoyi.web.controller.xkt;

import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author liangyq
 * @date 2025-07-29
 */
@Api(tags = "OSS回调接口")
@RestController
@RequestMapping("/rest/v1/oss-callback")
public class OSSCallbackController extends XktBaseController {

    @RequestMapping("/upload")
    public R callback(HttpServletRequest servletRequest) {
        String filename = servletRequest.getParameter("filename");
        String size = servletRequest.getParameter("size");
        String mimeType = servletRequest.getParameter("mimeType");
        String height = servletRequest.getParameter("imageInfo.height");
        String width = servletRequest.getParameter("imageInfo.width");
        logger.info("OSS回调: {}, {}, {}, {}, {}", filename, size, mimeType, height, width);
        return success();
    }
}
