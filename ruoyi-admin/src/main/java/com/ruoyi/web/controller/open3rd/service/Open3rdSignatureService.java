package com.ruoyi.web.controller.open3rd.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.HexFormat;
import java.util.Optional;
import java.util.UUID;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import com.ruoyi.common.config.Open3rdConfig;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.web.controller.open3rd.domain.Open3rdSignature;

@Service
public class Open3rdSignatureService
{
    public static final String HEADER_TOKEN = "X-TDOCS-Open3rd-Token";
    public static final String HEADER_TRACE_ID = "X-TDOCS-Trace-ID";
    public static final String HEADER_APP_ID = "X-TDOCS-APP-ID";
    public static final String HEADER_NONCE = "X-TDOCS-Nonce";
    public static final String HEADER_TIMESTAMP = "X-TDOCS-TimeStamp";
    public static final String HEADER_SIGNATURE = "X-TDOCS-Signature";

    private static final long SIGNATURE_EXPIRE_SECONDS = 60 * 60;

    private final Open3rdConfig open3rdConfig;

    public Open3rdSignatureService(Open3rdConfig open3rdConfig)
    {
        this.open3rdConfig = open3rdConfig;
    }

    public Optional<Open3rdError> validateHeaders(HttpServletRequest request)
    {
        String appId = request.getHeader(HEADER_APP_ID);
        String nonce = request.getHeader(HEADER_NONCE);
        String timeStamp = request.getHeader(HEADER_TIMESTAMP);
        String signature = request.getHeader(HEADER_SIGNATURE);

        if (StringUtils.isAnyEmpty(appId, nonce, timeStamp, signature))
        {
            return Optional.of(Open3rdError.INVALID_PARAM);
        }

        if (!appId.equals(open3rdConfig.getAppId()))
        {
            return Optional.of(Open3rdError.SIGNATURE_FAILED);
        }

        long requestTimestamp;
        try
        {
            requestTimestamp = Long.parseLong(timeStamp);
        }
        catch (NumberFormatException ex)
        {
            return Optional.of(Open3rdError.INVALID_PARAM);
        }

        long now = Instant.now().getEpochSecond();
        if (Math.abs(now - requestTimestamp) > SIGNATURE_EXPIRE_SECONDS)
        {
            return Optional.of(Open3rdError.SIGNATURE_FAILED);
        }

        String expectedSignature = sign(nonce, requestTimestamp, appId, open3rdConfig.getAppSecret());
        if (!signature.equalsIgnoreCase(expectedSignature))
        {
            return Optional.of(Open3rdError.SIGNATURE_FAILED);
        }

        return Optional.empty();
    }

    public Optional<Open3rdTokenUser> resolveToken(String token)
    {
        if (StringUtils.isEmpty(token))
        {
            return Optional.empty();
        }

        for (Open3rdConfig.Open3rdTokenConfig tokenConfig : open3rdConfig.getTokens())
        {
            if (token.equals(tokenConfig.getToken()))
            {
                return Optional.of(new Open3rdTokenUser(tokenConfig.getToken(), tokenConfig.getUserId()));
            }
        }

        if (open3rdConfig.getTokens().isEmpty())
        {
            return Optional.of(new Open3rdTokenUser(token, null));
        }

        return Optional.empty();
    }

    public String sign(String nonce, long timeStamp, String appId, String appSecret)
    {
        String raw = String.format("X-TDOCS-Nonce=%s&X-TDOCS-TimeStamp=%s&X-TDOCS-APP-ID=%s&APP-Secret=%s", nonce,
                timeStamp, appId, appSecret);
        return sha1Hex(raw);
    }

    public Open3rdSignature generateSignature()
    {
        String nonce = UUID.randomUUID().toString().replace("-", "");
        long timeStamp = Instant.now().getEpochSecond();
        String sign = sign(nonce, timeStamp, open3rdConfig.getAppId(), open3rdConfig.getAppSecret());
        return new Open3rdSignature(sign, nonce, timeStamp);
    }

    private String sha1Hex(String raw)
    {
        try
        {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] bytes = digest.digest(raw.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(bytes);
        }
        catch (Exception ex)
        {
            throw new IllegalStateException("Failed to create signature", ex);
        }
    }

    public static class Open3rdTokenUser
    {
        private final String token;
        private final String userId;
        public Open3rdTokenUser(String token, String userId)
        {
            this.token = token;
            this.userId = userId;
        }

        public String getToken()
        {
            return token;
        }

        public String getUserId()
        {
            return userId;
        }

    }
}
