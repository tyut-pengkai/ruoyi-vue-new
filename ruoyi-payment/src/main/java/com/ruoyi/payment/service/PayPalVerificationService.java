package com.ruoyi.payment.service;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.PublicKey;
import java.security.Signature;
import java.util.Base64;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.CRC32;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ruoyi.payment.config.PayPalConfig;

import cn.hutool.core.util.StrUtil;

/**
 * PayPal Webhook 签名验证服务
 */
@Service
public class PayPalVerificationService {

    private static final Logger log = LoggerFactory.getLogger(PayPalVerificationService.class);

    @Autowired
    private PayPalConfig payPalConfig;

    @Autowired
    private RestTemplate restTemplate;

    // 用于缓存从PayPal下载的证书，避免每次都重新下载
    private final ConcurrentHashMap<String, X509Certificate> certificateCache = new ConcurrentHashMap<>();

    /**
     * 验证PayPal Webhook请求的签名.
     *
     * @param request HttpServletRequest对象，用于获取请求头
     * @param requestBody 请求体原始内容
     * @return true 如果签名有效, false 否则.
     */
    public boolean verifyWebhookSignature(HttpServletRequest request, String requestBody) {
        // 1. 从请求头获取必要信息
        final String transmissionId = request.getHeader("PAYPAL-TRANSMISSION-ID");
        final String timeStamp = request.getHeader("PAYPAL-TRANSMISSION-TIME");
        final String signature = request.getHeader("PAYPAL-TRANSMISSION-SIG");
        final String certUrl = request.getHeader("PAYPAL-CERT-URL");
        final String authAlgo = request.getHeader("PAYPAL-AUTH-ALGO");
        final String webhookId = payPalConfig.getWebhookId();

        if (StrUtil.hasBlank(transmissionId, timeStamp, signature, certUrl, authAlgo, webhookId)) {
            log.error("Webhook验证失败：请求头缺少必要的验证参数。");
            return false;
        }

        try {
            // 2. 计算请求体的CRC32校验和
            CRC32 crc32 = new CRC32();
            crc32.update(requestBody.getBytes("UTF-8"));
            long crc32Value = crc32.getValue();

            // 3. 构建待验证的原始消息字符串
            // 格式: transmission_id|timestamp|webhook_id|crc32
            String originalMessage = String.format("%s|%s|%s|%d", transmissionId, timeStamp, webhookId, crc32Value);
            log.debug("构建的待验证消息: {}", originalMessage);

            // 4. 获取并验证签名证书
            X509Certificate certificate = getAndCacheCertificate(certUrl);
            if (certificate == null) {
                log.error("无法获取或验证签名证书: {}", certUrl);
                return false;
            }

            // 5. 使用公钥进行签名校验
            PublicKey publicKey = certificate.getPublicKey();
            Signature verifier = Signature.getInstance(authAlgo.replace("with", "with").toUpperCase()); // 例如 "SHA256withRSA"
            verifier.initVerify(publicKey);
            verifier.update(originalMessage.getBytes("UTF-8"));

            byte[] signatureBytes = Base64.getDecoder().decode(signature);

            boolean isVerified = verifier.verify(signatureBytes);
            log.info("Webhook签名验证结果: {}", isVerified ? "成功" : "失败");
            return isVerified;

        } catch (Exception e) {
            log.error("Webhook签名验证过程中发生异常", e);
            return false;
        }
    }

    /**
     * 从URL下载证书并缓存。
     * @param certUrl 证书的URL
     * @return X509Certificate 证书对象
     */
    private X509Certificate getAndCacheCertificate(String certUrl) {
        if (certificateCache.containsKey(certUrl)) {
            log.debug("从缓存中获取证书: {}", certUrl);
            return certificateCache.get(certUrl);
        }

        try {
            // PayPal要求使用TLSv1.2
            System.setProperty("https.protocols", "TLSv1.2");
            
            byte[] certBytes = restTemplate.getForObject(certUrl, byte[].class);
            if (certBytes == null) {
                log.error("从URL下载证书失败: {}", certUrl);
                return null;
            }

            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            X509Certificate certificate = (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(certBytes));
            
            // 校验证书域名是否为paypal.com的子域名
            String subjectDn = certificate.getSubjectX500Principal().getName();
            if (!isValidPaypalCertificate(subjectDn)) {
                 log.error("证书域名 {} 不是有效的PayPal域名", subjectDn);
                 return null;
            }

            certificateCache.put(certUrl, certificate);
            log.debug("成功下载并缓存证书: {}", certUrl);
            return certificate;
        } catch (Exception e) {
            log.error("下载或解析证书时发生异常", e);
            return null;
        }
    }

    /**
     * 从证书的Subject DN中提取CN(Common Name)并验证其是否为PayPal的有效子域名。
     * @param subjectDn 证书的Subject DN字符串
     * @return 如果是有效的PayPal域名则返回true,否则返回false
     */
    private boolean isValidPaypalCertificate(String subjectDn) {
        if (StrUtil.isBlank(subjectDn)) {
            return false;
        }
        // DN的格式通常是 "CN=example.com, O=Organization, L=City, ..."
        // 我们需要从中提取出CN的值
        String[] parts = subjectDn.split(",");
        for (String part : parts) {
            String trimmedPart = part.trim();
            if (trimmedPart.toUpperCase().startsWith("CN=")) {
                String commonName = trimmedPart.substring(3);
                // 验证CN是否以.paypal.com结尾或者是paypal.com本身
                if (commonName.equalsIgnoreCase("paypal.com") || commonName.toLowerCase().endsWith(".paypal.com")) {
                    return true;
                }
            }
        }
        return false;
    }
} 