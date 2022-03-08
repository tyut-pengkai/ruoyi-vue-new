package com.ruoyi.sale.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.ruoyi.common.utils.ZxingUtils;
import org.springframework.stereotype.Service;

@Service
public class AlipayServiceImpl {

    public void test() {
        AlipayClient alipayClient = new DefaultAlipayClient(
                "https://openapi.alipaydev.com/gateway.do",
                "2016102000727957",
                "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC0/phi1ABrXfKPbRqkt/w9KBOVh0RO6fCmRxbWxZoy23HsPJB4l9/J55Npaduz9jVdpJWhlIH76TQbL4Aw+vX/bap6mRCS7qy16XovELkqhBEq9kMVz93zuak4lBpJWY5NiMMXGyIdO+zCMv8r+bIS9VlFBPu/dm5YOMQ4FXiNWU/yVlMeit4YMgw6G3IwmiHF/794s3d2m2Kfr2mOPNyx0wSbXMpiCA65Jw5A1iX0LNtCCkkeuvAH99oDyQwirlMhpK/LJr6j8dns7JFGkAtPEyXWuOqdVXikjtPLecV/YOMEym1O0Lfs5aeYVRV2o4ecja/Pdaz/JJBJKhygpOTJAgMBAAECggEBAJbSoYGZUFAoFXzXWiBxAMylnMw5z/5Ci7rD+pA2UeyXWTOWtH0Jcf757qklAWPRg17pS5c9/aNCDZ2p05T9TAjyBeHrsxf9tAZS7PJTaTm4m+XFGNoQQdBbolv3boA5FJAfqxKSFbduvDiH7oNiq7WIpj8RjAdcVU9G3pwtqCuAG0XtWqeQskIr4j7O5e/DeOqsooDIqeVpBNVor47WoJ38fsY1Deixyockag00nSXaT5GBg7pD1ReXBE+l+l/WDah2XDLEr+eW8SyhOJbaP8N46hmc5M6nlk/irQU5Vb1RCVQVOlkNXAWbC7+0xiiSqv3C9c/vYeZIcotT2FsmhJECgYEA9oDafIUTq+eB9FP4sIWPDxripaeclWsbsbYZ0IQMuCwckc767vXVqhQOcfTbmnKPN94oQ3uO7jIBNNGJxFe3nnrJLJO0O2IFcQ7rJWdpaD3sfCCAKvoWJYRk2FE1EXOFimA/CC19R2WWJoAD/f3+t9GUj9ZRi2OZE8Xr3Pr08N8CgYEAu/eouWHHoIEi01Dgc49caMKs/IDNx/jY4GCRUXPpYYiOuzzKw/BZ+qAdDH3vqJ083Eeoi8xGZF9Fxc9Atvl7SHzDOkWjI43LEVeSgbcy+zRvq3szL+YXROpO3DiX3c9WCW+BmI1PqmqDpJhvqjmLbPUKPv3cU8HcHAYcMwOdF1cCgYAvLYQjdtjH+tv9ZiDfsAAsVOnx6H1of4JiZcbVCKDikta49VNDbtuA3KvTFZj+G1TbzXIJUFmPrxRaBoyGfn9PHpLoLDC/eMgv1jodA4jCAbAEJbhCAXFBpvAiEpDEkUaKsFb/+qzSgFfXcILTFsysY7k6OjuLIPnINgYpWgKNIwKBgGcevW/GlvAVKHfp3NlJAxduBd0ZBMv6V3DxSYf4IUci1bse5NaN269Fe+pIhNxqNuNaZLsdPFkAc5TL2OMJB3uDBs/HOHLe7VL8SiHj0ZJC+CiJlFFo18c1DEKAwcAsaTUP+Xcpv1Tszn/UKR6oJzeFTzOzrdY9enXdXEcYamxNAoGAQTS/WT0wzvebALsDcJptn7KeIbzUDEo4mO3nHd5lrMuEYEjK8HwSvqUWip7FgniClXmMiZ76SH8L4KyImwgkFsQrppKCz486vJI7nAtND07fGlOVyugTSxN4AktSU2hFZG5Y1kXMOwSlVL7mojk12jGOZt3cdxVBckcKBtHhHL8=",
                "json", "GBK",
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgvhIg0spf1cPGjqyNB6Ev2iL3RKG3XPeeH8a6r43I1yLs7OyDoR9eBhS0R2Uwl7yaK6g3mFBPfhLulAfz7Sxymf+H01k+cR2fkjg1oVw7I7dr2xunyZs8oKwKe0WMeEE10T7bJrubp3UAVrV1vKnWyeTMVt/J6+T9+Geo9fDg49VXuwOvTxe4eeZS+QAyhcWYMCk3Amy/ZpRRa4qJ50LFtKeF52CtCWhENW5uQUNadnyv091ssb0nZZAoM02EnfeeC6UxW+x7uPr4QR265+L3TC7c0+21U1OU5iXHI8caX4wprQDxaMnuqJMm0ki2Gri6os+GT051VZ7vJuZW4MJ5QIDAQAB",
                "RSA2");
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
        request.setNotifyUrl("");
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", "20210817010101003");
        bizContent.put("total_amount", 0.01);
        bizContent.put("subject", "测试商品");

//// 商品明细信息，按需传入
//JSONArray goodsDetail = new JSONArray();
//JSONObject goods1 = new JSONObject();
//goods1.put("goods_id", "goodsNo1");
//goods1.put("goods_name", "子商品1");
//goods1.put("quantity", 1);
//goods1.put("price", 0.01);
//goodsDetail.add(goods1);
//bizContent.put("goods_detail", goodsDetail);

//// 扩展信息，按需传入
//JSONObject extendParams = new JSONObject();
//extendParams.put("sys_service_provider_id", "2088511833207846");
//bizContent.put("extend_params", extendParams);

//// 结算信息，按需传入
//JSONObject settleInfo = new JSONObject();
//JSONArray settleDetailInfos = new JSONArray();
//JSONObject settleDetail = new JSONObject();
//settleDetail.put("trans_in_type", "defaultSettle");
//settleDetail.put("amount", 0.01);
//settleDetailInfos.add(settleDetail);
//settleInfo.put("settle_detail_infos", settleDetailInfos);
//bizContent.put("settle_info", settleInfo);

//// 二级商户信息，按需传入
//JSONObject subMerchant = new JSONObject();
//subMerchant.put("merchant_id", "2088000603999128");
//bizContent.put("sub_merchant", subMerchant);

//// 业务参数信息，按需传入
//JSONObject businessParams = new JSONObject();
//businessParams.put("busi_params_key", "busiParamsValue");
//bizContent.put("business_params", businessParams);

//// 营销信息，按需传入
//JSONObject promoParams = new JSONObject();
//promoParams.put("promo_params_key", "promoParamsValue");
//bizContent.put("promo_params", promoParams);

        try {
            request.setBizContent(bizContent.toString());
            AlipayTradePrecreateResponse response = alipayClient.execute(request);

            System.out.println(JSON.toJSONString(response));
            if (response.isSuccess()) {
                System.out.println("调用成功");
                // 需要修改为运行机器上的路径
                String filePath = String.format("C:/Users/GuZhiwei/Desktop/qr-%s.png", response.getOutTradeNo());
                ZxingUtils.getQRCodeImge(response.getQrCode(), 256, filePath);
            } else {
                System.out.println("调用失败");
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
    }


}
