package com.ruoyi.framework.oss;

import com.aliyun.oss.*;
import com.aliyun.oss.common.auth.Credentials;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.common.auth.STSAssumeRoleSessionCredentialsProvider;
import com.aliyun.oss.common.comm.Protocol;
import com.aliyun.oss.model.*;
import io.jsonwebtoken.lang.Assert;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Base64;
import java.util.Date;

/**
 * @author liangyq
 * @date 2025-03-24
 */
@Slf4j
public class OSSClientWrapper {

    private final OSS client;

    private final OSSConfiguration configuration;

    public OSSClientWrapper(OSSConfiguration configuration) {
        this.configuration = configuration;
        ClientBuilderConfiguration config = getClientBuilderConfiguration();
        client = new OSSClientBuilder().build(configuration.getEndpoint(), configuration.getAccessKeyId(), configuration.getAccessKeySecret(), config);
    }

    public OSSClientWrapper(STSAssumeRoleSessionCredentialsProvider credentialsProvider, OSSConfiguration configuration) {
        this.configuration = configuration;
        ClientBuilderConfiguration config = getClientBuilderConfiguration();
        client = new OSSClientBuilder().build(configuration.getEndpoint(), credentialsProvider, config);
    }

    /**
     * 创建临时访问客户端
     * 临时访问客户端过期时间通过OSSConfiguration#expiredDuration配置
     *
     * @return 临时访问客户端
     */
    public OSSClientWrapper createSTSClient() {
        STSAssumeRoleSessionCredentialsProvider credentialsProvider = this.createSTSCredentialsProvider();
        Credentials credentials = credentialsProvider.getCredentials();
        OSSConfiguration newOSSConfiguration = this.configuration.clone();
        newOSSConfiguration.setAccessKeyId(credentials.getAccessKeyId());
        newOSSConfiguration.setAccessKeySecret(credentials.getSecretAccessKey());
        return new OSSClientWrapper(credentialsProvider, newOSSConfiguration);
    }

    /**
     * 创建临时访问key值 一般用于给前端提供领时上传功能
     * 临时访问客户端过期时间通过OSSConfiguration#expiredDuration配置
     *
     * @return 临时访问key
     */
    public Credentials createStsCredentials() {
        return this.createSTSCredentialsProvider().getCredentials();
    }

    /**
     * 文件上传
     *
     * @param key         key
     * @param inputStream 文件流
     */
    public void upload(String key, InputStream inputStream)
            throws Exception {
        upload(key, inputStream, null);
    }

    /**
     * 文件上传
     *
     * @param key         key
     * @param inputStream 文件流
     * @param contentType 文件类型
     */
    public void upload(String key, InputStream inputStream, String contentType)
            throws Exception {
        upload(key, inputStream, contentType, null);
    }

    /**
     * 文件上传
     *
     * @param key                key
     * @param inputStream        文件流
     * @param contentType        文件类型
     * @param contentDisposition 下载描述信息
     */
    public void upload(String key, InputStream inputStream, String contentType, String contentDisposition) throws Exception {
        ObjectMetadata objectMeta = new ObjectMetadata();
        objectMeta.setContentLength(inputStream.available());
        // 可以在metadata中标记文件类型
        if (contentType == null) {
//            contentType = deduceContentType(key);
        }
        objectMeta.setContentType(contentType);
        // 设置下载名
        objectMeta.setContentDisposition(contentDisposition);
        client.putObject(configuration.getBucketName(), key, inputStream, objectMeta);
        if (!client.doesObjectExist(configuration.getBucketName(), key)) {
            throw new IllegalStateException("文件上传失败");
        }
    }

    /**
     * 移动文件
     *
     * @param sourceKey 源KEY
     * @param targetKey 目标KEY
     */
    public void move(String sourceKey, String targetKey) {
        if (!client.doesObjectExist(configuration.getBucketName(), sourceKey)) {
            throw new IllegalStateException("源文件信息不存在");
        }
        client.copyObject(configuration.getBucketName(), sourceKey, configuration.getBucketName(), targetKey);
        if (!client.doesObjectExist(configuration.getBucketName(), targetKey)) {
            throw new IllegalStateException("移动文件失败[1]");
        }
        if (!this.delete(sourceKey)) {
            throw new IllegalStateException("移动文件失败[2]");
        }
    }

    /**
     * 根据key，获取访问路径
     *
     * @param key        key（文件相对路径）
     * @param expireTime 过期时间，单位毫秒
     */
    public URL generateUrl(String key, Long expireTime) throws Exception {
        return generateUrl(key, expireTime, false);
    }

    /**
     * 根据key，获取访问路径
     *
     * @param key        key（文件相对路径）
     * @param expireTime 过期时间，单位毫秒
     * @param down       是否生成直接下载的url
     */
    public URL generateUrl(String key, Long expireTime, boolean down) throws Exception {
        Assert.notNull(expireTime, "过期时间为空");
        GeneratePresignedUrlRequest request;
        Date expiration = new Date(System.currentTimeMillis() + expireTime);
        request = new GeneratePresignedUrlRequest(this.configuration.getBucketName(), key);
        request.setExpiration(expiration);
        request.setMethod(HttpMethod.GET);
        ResponseHeaderOverrides header = new ResponseHeaderOverrides();
//        header.setContentType(this.deduceContentType(key));
        if (down) {
            //设置响应头强制下载
            header.setContentDisposition("attachment;");
        }
        request.setResponseHeaders(header);
        return this.client.generatePresignedUrl(request);
    }

    public String generateBase64(String key) {
        if (key == null) {
            return null;
        }
        try {
            OSSObject ossObject = this.client.getObject(this.configuration.getBucketName(), key);
            if (ossObject == null) {
                return null;
            }
            InputStream inputStream = ossObject.getObjectContent();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] data = new byte[1024];
            int length;
            while ((length = inputStream.read(data)) != -1) {
                outputStream.write(data, 0, length);
            }
            outputStream.close();
            String base64 = Base64.getEncoder().encodeToString(outputStream.toByteArray());
            return String.format("data:image/png;base64,%s", base64);
        } catch (Exception e) {
            log.error("generateBase64", e);
        }
        return null;
    }

    private String deduceContentType(String key) {
        int index = key.lastIndexOf(".");
        String fileExtension = null;
        if (index != -1) {
            fileExtension = key.substring(index);
        }
        //文件的后缀名
        if (".bmp".equalsIgnoreCase(fileExtension)) {
            return "image/bmp";
        }
        if (".gif".equalsIgnoreCase(fileExtension)) {
            return "image/gif";
        }
        if (".jpeg".equalsIgnoreCase(fileExtension)) {
            return "image/jpeg";
        }
        if (".jpg".equalsIgnoreCase(fileExtension)) {
            return "image/jpg";
        }
        if (".png".equalsIgnoreCase(fileExtension)) {
            return "image/png";
        }
        if (".html".equalsIgnoreCase(fileExtension)) {
            return "text/html";
        }
        if (".txt".equalsIgnoreCase(fileExtension)) {
            return "text/plain";
        }
        if (".vsd".equalsIgnoreCase(fileExtension)) {
            return "application/vnd.visio";
        }
        if (".ppt".equalsIgnoreCase(fileExtension) || ".pptx".equalsIgnoreCase(fileExtension)) {
            return "application/vnd.ms-powerpoint";
        }
        if (".doc".equalsIgnoreCase(fileExtension) || ".docx".equalsIgnoreCase(fileExtension)) {
            return "application/msword";
        }
        if (".xml".equalsIgnoreCase(fileExtension)) {
            return "text/xml";
        }
        if (".zip".equalsIgnoreCase(fileExtension)) {
            return "application/zip";
        }
        if (".pdf".equalsIgnoreCase(fileExtension)) {
            return "application/pdf";
        }
        if (".xls".equalsIgnoreCase(fileExtension) || ".xlsx".equalsIgnoreCase(fileExtension)) {
            return "application/vnd.ms-excel";
        }
        //默认返回类型
        return "image/jpeg";
    }

    public InputStream getObject(String key) throws Exception {
        OSSObject ossObject = client.getObject(configuration.getBucketName(), key);
        return ossObject.getObjectContent();
    }

    /**
     * 下载文件
     */
    public void download(OSSClient client, String key, String filename)
            throws OSSException, ClientException {
        client.getObject(new GetObjectRequest(configuration.getBucketName(), key), new File(filename));
    }

    /**
     * 删除oss上的文件
     *
     * @return 成功删除返回true 不存在返回false
     */
    public boolean delete(String key) {
        //检查是否是有效文件
        boolean exists = client.doesObjectExist(configuration.getBucketName(), key);
        if (exists) {
            client.deleteObject(configuration.getBucketName(), key);
        }
        return exists;
    }

    public OSS getOriginalClient() {
        return client;
    }

    private ClientBuilderConfiguration getClientBuilderConfiguration() {
        ClientBuilderConfiguration config = new ClientBuilderConfiguration();
        config.setProtocol(configuration.isHttps() ? Protocol.HTTPS : Protocol.HTTP);
        return config;
    }

    private STSAssumeRoleSessionCredentialsProvider createSTSCredentialsProvider() {
        try {
            return CredentialsProviderFactory.newSTSAssumeRoleSessionCredentialsProvider(
                    configuration.getRegionId(),
                    configuration.getAccessKeyId(),
                    configuration.getAccessKeySecret(),
                    configuration.getRoleArn()
            )
                    .withExpiredDuration(configuration.getExpiredDuration());
        } catch (com.aliyuncs.exceptions.ClientException e) {
            log.error("createStsCredentialsProvider ERROR", e);
            throw new RuntimeException("create createStsCredentialsProvider error");
        }
    }

    public OSSConfiguration getConfiguration() {
        return this.configuration;
    }

}
