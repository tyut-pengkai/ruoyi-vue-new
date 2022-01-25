package com.ruoyi.common.license.bo;

import de.schlichtherle.license.AbstractKeyStoreParam;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * 自定义KeyStoreParam，用于将公私钥存储文件存放到其他磁盘位置而不是项目中
 */
public class CustomKeyStoreParam extends AbstractKeyStoreParam {

    //公钥/私钥在磁盘上的存储路径
    private String storePath;
    //访问秘钥库的密码
    private String storePwd;
    //别称
    private String alias;
    //密钥密码（需要妥善保管，不能让使用者知道）
    private String keyPwd;

    public CustomKeyStoreParam(Class clazz, String resource, String alias, String storePwd, String keyPwd) {
        super(clazz, resource);
        this.storePath = resource;
        this.alias = alias;
        this.storePwd = storePwd;
        this.keyPwd = keyPwd;
    }

    @Override
    public String getAlias() {
        return alias;
    }

    @Override
    public String getStorePwd() {
        return storePwd;
    }

    @Override
    public String getKeyPwd() {
        return keyPwd;
    }

    /**
     * 用于将公私钥存储文件存放到其他磁盘位置而不是项目中
     *
     * @return
     * @throws IOException
     */
    @Override
    public InputStream getStream() throws IOException {
        final InputStream in = new FileInputStream(storePath);
        if (null == in) {
            throw new FileNotFoundException(storePath);
        }
        return in;
    }
}
