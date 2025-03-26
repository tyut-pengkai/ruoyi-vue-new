package com.ruoyi.web.controller.common;

import com.aliyun.oss.common.auth.Credentials;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.framework.config.properties.OSSProperties;
import com.ruoyi.framework.oss.OSSClientWrapper;
import com.ruoyi.web.controller.common.vo.STSCredentialsVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 通用请求处理
 *
 * @author ruoyi
 */
@Api(tags = "通用")
@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {
    @Autowired
    private OSSClientWrapper ossClient;
    @Autowired
    private OSSProperties ossProperties;

    @ApiOperation("获取OSS临时访问凭证")
    @GetMapping("/oss/getCredentials")
    public R<STSCredentialsVO> getCredentials() {
        STSCredentialsVO vo = new STSCredentialsVO();
        Credentials credentials = ossClient.createStsCredentials();
        vo.setAccessKeyId(credentials.getAccessKeyId());
        vo.setAccessKeySecret(credentials.getSecretAccessKey());
        vo.setSecurityToken(credentials.getSecurityToken());
        vo.setBucketName(ossProperties.getBucketName());
        vo.setRegionId(ossProperties.getRegionId());
        vo.setEndPoint(ossProperties.getEndPoint());
        vo.setExpiredDuration(ossProperties.getExpiredDuration());
        vo.setHttpsFlag(ossProperties.isHttps());
        return R.ok(vo);
    }

}
