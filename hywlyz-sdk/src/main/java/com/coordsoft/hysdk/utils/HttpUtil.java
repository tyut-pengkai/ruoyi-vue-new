package com.coordsoft.hysdk.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class HttpUtil {

    private static BufferedReader reader = null;
    private static HttpURLConnection connection;

    public static String post(String urlStr, String params, Map<String, String> headers) {
        CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
        synchronized (HttpUtil.class) {
            try {
                URL url = new URL(urlStr);
                connection = (HttpURLConnection) url.openConnection();
                // 设置需要输出内容的标识，如果需要传递给服务器表单数据，那么此项需为 true
                connection.setDoOutput(true);
                // 不使用缓存
                connection.setUseCaches(false);
                // 设置 RequestHeader 与 HTTP 的请求方式
                connection.setRequestProperty("content-type", "application/json; charset=utf-8");
                if (headers != null && headers.size() > 0) {
                    for (Map.Entry<String, String> item : headers.entrySet()) {
                        connection.setRequestProperty(item.getKey(), item.getValue());
                    }
                }
                connection.setRequestMethod("POST");
                // 应该指的是与服务器建立连接的最大时间
                connection.setConnectTimeout(1000);
                // 读取数据的最大时间，这个不要设置得太短，不然会很容易出现超时的异常
                connection.setReadTimeout(6000);
                // 使用 DataOutputStream 进行输出操作
                DataOutputStream output = new DataOutputStream(connection.getOutputStream());
                // 传输的内容格式就如同 URL 的规范一样(a=1&b=2)，最好进行一次 URL 编码操作
                //            String content = URLEncoder.encode(params, "utf-8");
                output.write(params.getBytes(StandardCharsets.UTF_8));
                // 输出完记得关
                output.flush();
                output.close();
                // 如果不是正常的请求结果则抛出异常，注意获取结果的步骤一定要在请求内容给完之后
                if (connection.getResponseCode() != 200) {
                    throw new Exception(connection.getResponseCode() + "," + connection.getResponseMessage());
                }
                // 使用 InputStream 进行接收操作
                InputStream input = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder response = new StringBuilder();
                // 这里把 reader 中的内容一行行地读取到 response 中
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                // 返回最终的结果
                return response.toString();
            } catch (Exception ex) {
                // 返回错误消息
                ex.printStackTrace();
                return "Http error: " + ex.getMessage();
            } finally {
                // 请求结束后释放资源
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }
    }

}
