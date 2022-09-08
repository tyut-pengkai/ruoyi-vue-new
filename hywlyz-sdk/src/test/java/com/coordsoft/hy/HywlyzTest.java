package com.coordsoft.hy;

import com.coordsoft.hy.encrypt.EncryptType;
import com.coordsoft.hy.utils.HyUtils;
import org.junit.Test;

public class HywlyzTest {

    @Test
    public void test() {

        Hywlyz hy = new Hywlyz();
//        hy.init("http://my.coordsoft.com/prod-api/api/v1/uSZNfxKRaXAqpJBoC5sMEFcuKjOG2xTP", "YIIgPA0881syIEQtmS8qUZXjUqgzhHDs",
//                "1", EncryptType.NONE, "1234@#awd", EncryptType.NONE, "1234@#awd", "1234@#awd");
        hy.init("http://my.coordsoft.com/prod-api/api/v1/uSZNfxKRaXAqpJBoC5sMEFcuKjOG2xTP", "YIIgPA0881syIEQtmS8qUZXjUqgzhHDs",
                "1", EncryptType.NONE, "", EncryptType.NONE, "", "");

        System.out.println(HyUtils.analyseResult(hy.isConnected_ng()));

        System.out.println(HyUtils.analyseResult(hy.appInfo_ng()));

    }


}
