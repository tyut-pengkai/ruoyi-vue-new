package com.ruoyi.payment.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HTTP请求工具类
 */
public class HttpUtils {
    
    private static final Logger log = LoggerFactory.getLogger(HttpUtils.class);
    
    /**
     * 发送HTTP GET请求
     *
     * @param url     请求URL
     * @param params  URL参数
     * @param headers HTTP请求头
     * @return 响应内容
     */
    public static String get(String url, Map<String, Object> params, Map<String, String> headers) {
        StringBuilder sb = new StringBuilder(url);
        
        // 添加URL参数
        if (params != null && !params.isEmpty()) {
            sb.append("?");
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        String response = "";
        
        try {
            URL requestUrl = new URL(sb.toString());
            conn = (HttpURLConnection) requestUrl.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(15000);
            
            // 设置请求头
            if (headers != null && !headers.isEmpty()) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    conn.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }
            
            conn.connect();
            
            // 检查响应码
            int statusCode = conn.getResponseCode();
            if (statusCode >= 200 && statusCode < 300) {
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            } else {
                reader = new BufferedReader(new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8));
            }
            
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            
            response = result.toString();
            
        } catch (Exception e) {
            log.error("HTTP GET请求异常", e);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (conn != null) {
                    conn.disconnect();
                }
            } catch (IOException e) {
                log.error("关闭HTTP连接异常", e);
            }
        }
        
        return response;
    }
    
    /**
     * 发送HTTP POST请求
     *
     * @param url     请求URL
     * @param body    请求体
     * @param headers HTTP请求头
     * @return 响应内容
     */
    public static String post(String url, String body, Map<String, String> headers) {
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        OutputStream outputStream = null;
        String response = "";
        
        try {
            URL requestUrl = new URL(url);
            conn = (HttpURLConnection) requestUrl.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(15000);
            
            // 设置请求头
            if (headers != null && !headers.isEmpty()) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    conn.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }
            
            // 发送请求体
            if (body != null && !body.isEmpty()) {
                outputStream = conn.getOutputStream();
                outputStream.write(body.getBytes(StandardCharsets.UTF_8));
                outputStream.flush();
            }
            
            // 检查响应码
            int statusCode = conn.getResponseCode();
            if (statusCode >= 200 && statusCode < 300) {
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            } else {
                reader = new BufferedReader(new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8));
            }
            
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            
            response = result.toString();
            
        } catch (Exception e) {
            log.error("HTTP POST请求异常", e);
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
                if (reader != null) {
                    reader.close();
                }
                if (conn != null) {
                    conn.disconnect();
                }
            } catch (IOException e) {
                log.error("关闭HTTP连接异常", e);
            }
        }
        
        return response;
    }
} 