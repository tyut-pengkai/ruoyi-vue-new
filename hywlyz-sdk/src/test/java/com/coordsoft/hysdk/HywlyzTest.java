package com.coordsoft.hysdk;

import com.coordsoft.hysdk.encrypt.EncryptType;
import com.coordsoft.hysdk.utils.HyUtils;
import org.junit.Test;

public class HywlyzTest {

    private  Hywlyz hy = new Hywlyz();

    @org.junit.Before
    public void setUp() {
        System.out.println(hy.getSdkVer());
        hy.init("http://127.0.0.1/prod-api/api/v1/uSZNfxKRaXAqpJBoC5sMEFcuKjOG2xTP", "YIIgPA0881syIEQtmS8qUZXjUqgzhHDs", "1", EncryptType.NONE, "", EncryptType.NONE, "", "");
    }

    @Test
    public void test() {
        System.out.println(HyUtils.analyseResult(hy.isConnectedNg()));
        System.out.println(HyUtils.analyseResult(hy.appInfoNg()));
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

}
