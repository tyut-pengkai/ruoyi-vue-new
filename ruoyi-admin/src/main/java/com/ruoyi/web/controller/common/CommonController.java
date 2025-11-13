package com.ruoyi.web.controller.common;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.aliyun.oss.common.auth.Credentials;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.framework.config.properties.AppProperties;
import com.ruoyi.framework.config.properties.OSSProperties;
import com.ruoyi.framework.ocr.BusinessLicense;
import com.ruoyi.framework.ocr.IdCard;
import com.ruoyi.framework.ocr.OcrClientWrapper;
import com.ruoyi.framework.oss.OSSClientWrapper;
import com.ruoyi.system.service.ISysHtmlService;
import com.ruoyi.web.controller.common.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.Charset;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 通用请求处理
 *
 * @author ruoyi
 */
@Api(tags = "通用")
@Slf4j
@RestController
@RequestMapping("/rest/v1/common")
public class CommonController {

    @Value("${oss.fileExpireTime:3600}")
    private Long fileExpireTime;//指定过期时间，单位为秒。
    @Value("${oss.callbackUrl}")
    private String callbackUrl;//上传回调URL
    @Autowired
    private AppProperties appProperties;
    @Autowired
    private OSSClientWrapper ossClient;
    @Autowired
    private OSSProperties ossProperties;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private OcrClientWrapper ocrClient;
    @Autowired
    private ISysHtmlService htmlService;

    @ApiOperation("APP版本")
    @GetMapping("/app-version")
    public R<AppVersionVO> getAppVersion() {
        return R.ok(BeanUtil.toBean(appProperties, AppVersionVO.class));
    }

    @ApiOperation("获取OSS临时访问凭证")
    @GetMapping("/oss/getCredentials")
    public R<STSCredentialsVO> getCredentials() {
        Long currentUserId = SecurityUtils.getUserId();
        if (currentUserId == null) {
            return R.fail();
        }
        String cacheStr = redisCache.getCacheObject(CacheConstants.USER_STS_KEY + currentUserId);
        if (StrUtil.isNotBlank(cacheStr)) {
            //有缓存直接返回
            return R.ok(JSONUtil.toBean(cacheStr, STSCredentialsVO.class));
        }
        Credentials credentials = ossClient.createStsCredentials();
        STSCredentialsVO vo = new STSCredentialsVO();
        vo.setAccessKeyId(credentials.getAccessKeyId());
        vo.setAccessKeySecret(credentials.getSecretAccessKey());
        vo.setSecurityToken(credentials.getSecurityToken());
        vo.setBucketName(ossProperties.getBucketName());
        vo.setPublicBucketName(ossProperties.getPublicBucketName());
        vo.setRegionId(ossProperties.getRegionId());
        vo.setEndPoint(ossProperties.getEndPoint());
        vo.setExpiredDuration(ossProperties.getExpiredDuration() - ossProperties.getStsCacheDuration());
        vo.setHttpsFlag(ossProperties.isHttps());
        //缓存
        redisCache.setCacheObject(CacheConstants.USER_STS_KEY + currentUserId, JSONUtil.toJsonStr(vo),
                ossProperties.getStsCacheDuration(), TimeUnit.SECONDS);
        return R.ok(vo);
    }

    @ApiOperation("获取OSS上传签名")
    @PostMapping("/oss/signature4upload")
    public R<OSSUploadSignRespVO> signature4upload(@Validated @RequestBody OSSUploadSignReqVO vo) {
        String accessKeyId = vo.getAccessKeyId();
        String accessKeySecret = vo.getAccessKeySecret();
        String securityToken = vo.getSecurityToken();
        String region = ossProperties.getRegionId();
        String bucket = vo.getBucket();
        String uploadDir = StrUtil.emptyIfNull(vo.getUploadDir());
        try {
            //获取x-oss-credential里的date，当前日期，格式为yyyyMMdd
            ZonedDateTime today = ZonedDateTime.now().withZoneSameInstant(java.time.ZoneOffset.UTC);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            String date = today.format(formatter);

            //获取x-oss-date
            ZonedDateTime now = ZonedDateTime.now().withZoneSameInstant(java.time.ZoneOffset.UTC);
            DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'");
            String x_oss_date = now.format(formatter2);

            // 步骤1：创建policy。
            String x_oss_credential = accessKeyId + "/" + date + "/" + region + "/oss/aliyun_v4_request";

            ObjectMapper mapper = new ObjectMapper();

            Map<String, Object> policy = new HashMap<>(2);
            policy.put("expiration", generateExpiration(fileExpireTime));

            List<Object> conditions = new ArrayList<>(8);

            Map<String, String> bucketCondition = new HashMap<>(1);
            bucketCondition.put("bucket", bucket);
            conditions.add(bucketCondition);

            Map<String, String> securityTokenCondition = new HashMap<>(1);
            securityTokenCondition.put("x-oss-security-token", securityToken);
            conditions.add(securityTokenCondition);

            Map<String, String> signatureVersionCondition = new HashMap<>(1);
            signatureVersionCondition.put("x-oss-signature-version", "OSS4-HMAC-SHA256");
            conditions.add(signatureVersionCondition);

            Map<String, String> credentialCondition = new HashMap<>(1);
            credentialCondition.put("x-oss-credential", x_oss_credential); // 替换为实际的 access key id
            conditions.add(credentialCondition);

            Map<String, String> dateCondition = new HashMap<>(1);
            dateCondition.put("x-oss-date", x_oss_date);
            conditions.add(dateCondition);

            conditions.add(Arrays.asList("content-length-range", 1, 10240000));
            conditions.add(Arrays.asList("eq", "$success_action_status", "200"));
            conditions.add(Arrays.asList("starts-with", "$key", uploadDir));

            policy.put("conditions", conditions);

            String jsonPolicy = mapper.writeValueAsString(policy);

            // 步骤2：构造待签名字符串（StringToSign）。
            String stringToSign = new String(Base64.encodeBase64(jsonPolicy.getBytes()));

            // 步骤3：计算SigningKey。
            byte[] dateKey = hmacsha256(("aliyun_v4" + accessKeySecret).getBytes(), date);
            byte[] dateRegionKey = hmacsha256(dateKey, region);
            byte[] dateRegionServiceKey = hmacsha256(dateRegionKey, "oss");
            byte[] signingKey = hmacsha256(dateRegionServiceKey, "aliyun_v4_request");

            // 步骤4：计算Signature。
            byte[] result = hmacsha256(signingKey, stringToSign);
            String signature = BinaryUtil.toHex(result);

            // 步骤5：设置回调。
            JSONObject jasonCallback = new JSONObject();
            jasonCallback.put("callbackUrl", callbackUrl);
            jasonCallback.put("callbackBody", "filename=${object}&size=${size}&mimeType=${mimeType}&height=${imageInfo.height}&width=${imageInfo.width}");
            jasonCallback.put("callbackBodyType", "application/x-www-form-urlencoded");
            String base64CallbackBody = BinaryUtil.toBase64String(jasonCallback.toString().getBytes());

            OSSUploadSignRespVO response = new OSSUploadSignRespVO();
            // 将数据添加到 map 中
            response.setVersion("OSS4-HMAC-SHA256");
            // 这里是易错点，不能直接传policy，需要做一下Base64编码
            response.setPolicy(stringToSign);
            response.setX_oss_credential(x_oss_credential);
            response.setX_oss_date(x_oss_date);
            response.setSignature(signature);
            response.setSecurity_token(securityToken);
            response.setDir(uploadDir);
            response.setHost(generateHost(bucket, ossProperties.isHttps()));
            response.setCallback(base64CallbackBody);
            response.setAccessKeyId(ossProperties.getAccessKeyId());
            // 返回带有状态码 200 (OK) 的 ResponseEntity，返回给Web端，进行PostObject操作
            return R.ok(response);
        } catch (Exception e) {
            log.error("生成OSS签名异常", e);
        }
        return R.fail();
    }

    @ApiOperation("身份证OCR")
    @PostMapping("/ocr/idCard")
    public R<IdCardVO> recognizeIdCard(@Validated @RequestBody UrlVO vo) {
        String url = vo.getUrl();
        String cacheKey = CacheConstants.OCR_CACHE + url;
        String cacheStr = redisCache.getCacheObject(cacheKey);
        if (StrUtil.isNotEmpty(cacheStr)) {
            return R.ok(JSONUtil.toBean(cacheStr, IdCardVO.class));
        }
        IdCard dto = ocrClient.recognizeIdCard(url);
        // 缓存
        redisCache.setCacheObject(cacheKey, JSONUtil.toJsonStr(dto), 10, TimeUnit.MINUTES);
        return R.ok(BeanUtil.toBean(dto, IdCardVO.class));
    }

    @ApiOperation("营业执照OCR")
    @PostMapping("/ocr/businessLicense")
    public R<BusinessLicenseVO> recognizeBusinessLicense(@Validated @RequestBody UrlVO vo) {
        String url = vo.getUrl();
        String cacheKey = CacheConstants.OCR_CACHE + url;
        String cacheStr = redisCache.getCacheObject(cacheKey);
        if (StrUtil.isNotEmpty(cacheStr)) {
            return R.ok(JSONUtil.toBean(cacheStr, BusinessLicenseVO.class));
        }
        BusinessLicense dto = ocrClient.recognizeBusinessLicense(url);
        // 缓存
        redisCache.setCacheObject(cacheKey, JSONUtil.toJsonStr(dto), 10, TimeUnit.MINUTES);
        return R.ok(BeanUtil.toBean(dto, BusinessLicenseVO.class));
    }

    public static void main(String[] args) {
        String str = FileUtil.readString("C:\\Users\\123\\Desktop\\步橘网总则.html", Charset.forName("UTF-8"));
        Map<String, String> json = new HashMap<>();
        json.put("title", "generalRules");
        json.put("content", str);
        System.out.println(JSONUtil.toJsonStr(json));
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @ApiOperation("保存html（需登录管理员用户）")
    @PostMapping("/html/save")
    public R saveHtmlContent(@Validated @RequestBody HtmlVO vo) {
        htmlService.saveHtml(vo.getTitle(), vo.getContent());
        return R.ok();
    }

    @ApiOperation("获取html内容")
    @GetMapping("/html/content/{title}")
    public String getHtmlContent(@PathVariable("title") String title, HttpServletResponse response) {
        response.setHeader("Content-Security-Policy", "frame-ancestors *");
        return htmlService.getHtmlContent(title);
    }

    /**
     * 通过指定有效的时长（秒）生成过期时间。
     *
     * @param seconds 有效时长（秒）。
     * @return ISO8601 时间字符串，如："2014-12-01T12:00:00.000Z"。
     */
    private String generateExpiration(long seconds) {
        // 获取当前时间戳（以秒为单位）
        long now = Instant.now().getEpochSecond();
        // 计算过期时间的时间戳
        long expirationTime = now + seconds;
        // 将时间戳转换为Instant对象，并格式化为ISO8601格式
        Instant instant = Instant.ofEpochSecond(expirationTime);
        // 定义时区为UTC
        ZoneId zone = ZoneOffset.UTC;
        // 将 Instant 转换为 ZonedDateTime
        ZonedDateTime zonedDateTime = instant.atZone(zone);
        // 定义日期时间格式，例如2023-12-03T13:00:00.000Z
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        // 格式化日期时间
        String formattedDate = zonedDateTime.format(formatter);
        // 输出结果
        return formattedDate;
    }

    private byte[] hmacsha256(byte[] key, String data) {
        try {
            // 初始化HMAC密钥规格，指定算法为HMAC-SHA256并使用提供的密钥。
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, "HmacSHA256");
            // 获取Mac实例，并通过getInstance方法指定使用HMAC-SHA256算法。
            Mac mac = Mac.getInstance("HmacSHA256");
            // 使用密钥初始化Mac对象。
            mac.init(secretKeySpec);
            // 执行HMAC计算，通过doFinal方法接收需要计算的数据并返回计算结果的数组。
            return mac.doFinal(data.getBytes());
        } catch (Exception e) {
            throw new RuntimeException("Failed to calculate HMAC-SHA256", e);
        }
    }

    private String generateHost(String bucket, Boolean isHttps) {
        String url = "http{0}://{1}.{2}";
        return StrUtil.indexedFormat(url, BooleanUtil.isTrue(isHttps) ? "s" : "", bucket, ossProperties.getEndPoint());
    }

}
