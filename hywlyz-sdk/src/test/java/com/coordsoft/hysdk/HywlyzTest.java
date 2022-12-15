package com.coordsoft.hysdk;

import com.coordsoft.hysdk.encrypt.EncryptType;
import com.coordsoft.hysdk.utils.HyUtils;
import org.junit.Test;

public class HywlyzTest {

    @Test
    public void test() {
        Hywlyz hy = new Hywlyz();
//        hy.init("http://my.coordsoft.com/prod-api/api/v1/uSZNfxKRaXAqpJBoC5sMEFcuKjOG2xTP", "YIIgPA0881syIEQtmS8qUZXjUqgzhHDs", "1", EncryptType.NONE, "1234@#awd", EncryptType.NONE, "1234@#awd", "1234@#awd");
        hy.init("http://my.coordsoft.com/prod-api/api/v1/uSZNfxKRaXAqpJBoC5sMEFcuKjOG2xTP", "YIIgPA0881syIEQtmS8qUZXjUqgzhHDs", "1", EncryptType.NONE, "", EncryptType.NONE, "", "");
        System.out.println(HyUtils.analyseResult(hy.isConnectedNg()));
        System.out.println(HyUtils.analyseResult(hy.appInfoNg()));
    }

    @Test
    public void testTimeDiff() {
        Hywlyz hy = new Hywlyz();
        System.out.println(hy.getSdkVer());
        hy.init("http://my.coordsoft.com/prod-api/api/v1/uSZNfxKRaXAqpJBoC5sMEFcuKjOG2xTP", "YIIgPA0881syIEQtmS8qUZXjUqgzhHDs", "1", EncryptType.NONE, "", EncryptType.NONE, "", "");
        System.out.println(HyUtils.analyseResult(hy.timeDiffNg("2139-01-01 00:00:00", null, 0)));
        System.out.println(HyUtils.analyseResult(hy.timeDiffNg("2139-01-01 00:00:00", null, 1)));
        System.out.println(HyUtils.analyseResult(hy.timeDiffNg("2139-01-01 00:00:00", null, 2)));
    }


}
