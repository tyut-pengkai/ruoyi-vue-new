package com.ruoyi.xkt.thirdpart.kuaidi100;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.xkt.dto.express.TrackRecordDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liangyq
 * @date 2025-07-17
 */
@Slf4j
@Component
public class Kuaidi100Client {

    @Value("${kuaidi100.key:qlukmRsd2685}")
    private String key;
    @Value("${kuaidi100.customer:C24AD9F267EBCDD0D9C6B85EEC2CB4FD}")
    private String customer;
    @Value("${kuaidi100.secret:07515dc1a74e4d01bf91bcf77e6f6cfe}")
    private String secret;
    @Value("${kuaidi100.userid:3a0ea45a2d1e4a4fba9d86918553656d}")
    private String userid;

    /**
     * 查询快递100公司代码
     *
     * @param waybillNum
     * @return
     */
    public String queryComCode(String waybillNum) {
        if (StrUtil.isNotEmpty(waybillNum)) {
            String url = "http://www.kuaidi100.com/autonumber/auto?num={0}&key={1}";
            String fullUrl = StrUtil.indexedFormat(url, waybillNum, key);
            HttpRequest httpRequest = HttpUtil.createGet(fullUrl);
            String result = httpRequest.execute().body();
            JSONArray array = JSON.parseArray(result);
            if (!array.isEmpty()) {
                return array.getJSONObject(0).getString("comCode");
            }
        }
        return null;
    }

    /**
     * 查询物流轨迹
     *
     * @param waybillNum
     * @param phoneNumber
     * @return
     */
    public List<TrackRecordDTO> queryTrack(String waybillNum, String phoneNumber) {
        String comCode = queryComCode(waybillNum);
        if (comCode != null) {
            try {
                JSONObject param = new JSONObject();
                param.put("com", comCode);
                param.put("num", waybillNum);
                /**
                 * 收、寄件人的电话号码（手机和固定电话均可，只能填写一个，顺丰速运、顺丰快运必填，其他快递公司选填。
                 * 如座机号码有分机号，分机号无需传入；如号码是电商虚拟号码需传入“-“后的后四位）
                 */
                param.put("phone", phoneNumber);
                /**
                 * 添加此字段表示开通行政区域解析功能。空：关闭（默认）；
                 * 1：开通行政区域解析功能以及物流轨迹增加物流状态名称；
                 * 4: 开通行政解析功能以及物流轨迹增加物流高级状态名称、状态值并且返回出发、目的及当前城市信息；
                 * 8：在4的基础上额外返回预计到达时间和预计轨迹信息，支持的快递公司见在途时效预估支持的快递公司
                 */
                param.put("resultv2", "0");
                /**
                 * 返回格式：0：json格式（默认），1：xml，2：html，3：text
                 */
                param.put("show", "0");
                /**
                 * 返回结果排序:desc降序（默认）,asc 升序
                 */
                param.put("order", "desc");
                /**
                 * 签名， 用于验证身份， 按param + key + customer 的顺序进行MD5加密（注意加密后字符串一定要转32位大写），
                 * 不需要加上“+”号
                 */
                String sign = SecureUtil.md5(param.toString() + key + customer).toUpperCase();
                String url = "https://poll.kuaidi100.com/poll/query.do";
                HttpRequest httpRequest = HttpUtil.createPost(url);
                httpRequest.header("Content-Type", "application/x-www-form-urlencoded");
                httpRequest.form("customer", customer);
                httpRequest.form("param", param.toString());
                httpRequest.form("sign", sign);
                String result = httpRequest.execute().body();
                log.info("快递100轨迹查询：{}", result);
                JSONObject rtnObj = JSON.parseObject(result);
                JSONArray data = rtnObj.getJSONArray("data");
                if (data != null) {
                    List<TrackRecordDTO> records = new ArrayList<>(data.size());
                    for (int i = 0; i < data.size(); i++) {
                        JSONObject row = data.getJSONObject(i);
                        records.add(new TrackRecordDTO("", row.getString("context"), "",
                                DateUtil.parseDateTime(row.getString("time"))));
                    }
                    return records;
                }
            } catch (Exception e) {
                log.error("快递100物流轨迹信息查询异常", e);
            }
        }
        return ListUtil.empty();
    }

}
