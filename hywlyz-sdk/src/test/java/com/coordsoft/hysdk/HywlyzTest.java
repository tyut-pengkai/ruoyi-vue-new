package com.coordsoft.hysdk;

import com.coordsoft.hysdk.enums.EncryptType;
import com.coordsoft.hysdk.utils.HyUtils;
import org.junit.Test;

public class HywlyzTest {

    private final Hywlyz hy = new Hywlyz();

    @org.junit.Before
    public void setUp() {
        System.out.println("SDK版本：" + hy.getSdkVer());

        // 将此处设置为true显示SDK内部信息
        hy.setShowLog(false);

        hy.init("https://demo.coordsoft.com/prod-api/api/v1/uSZNfxKRaXAqpJBoC5sMEFcuKjOG2xTP", "YIIgPA0881syIEQtmS8qUZXjUqgzhHDs", "1",
                EncryptType.AES_CBC_PKCS5Padding, "123456", EncryptType.AES_CBC_PKCS5Padding, "123456", "123456");
    }

    @Test
    public void test() {
        System.out.println(HyUtils.analyseResult(hy.isConnectedNg()));
        System.out.println(HyUtils.analyseResult(hy.appInfoNg()));
        System.out.println(HyUtils.analyseResult(hy.globalVariableGetNg("json", false)));
    }

    @Test
    public void testLogin() {
        System.out.println(HyUtils.analyseResult(hy.loginNc("OSURn3OhatUVX56PpmsH", "666", "")));
    }

    @Test
    public void testTimeDiff() {
        System.out.println(HyUtils.analyseResult(hy.timeDiffNg("2139-01-01 00:00:00", null, 0)));
        System.out.println(HyUtils.analyseResult(hy.timeDiffNg("2139-01-01 00:00:00", null, 1)));
        System.out.println(HyUtils.analyseResult(hy.timeDiffNg("2139-01-01 00:00:00", null, 2)));
    }

    @Test
    public void testAppUserInfo() {
        System.out.println(HyUtils.analyseResult(hy.appUserInfoNc("OSURn3OhatUVX56PpmsH")));
        System.out.println(HyUtils.analyseResult(hy.appUserInfoNu("admin")));
    }

    @Test
    public void testLatestVersionInfoComputeNg() {
        System.out.println(HyUtils.analyseResult(hy.latestVersionInfoComputeNg()));
    }

    @Test
    public void testLog()
    {
        hy.setShowLog(true);
        hy.showLogInfo("111");
        hy.showLogError("test");
    }

}
