package com.ruoyi.common.utils.apk.apkeditor;


import com.ruoyi.common.utils.apk.apkeditor.axmleditor.decode.AXMLDoc;
import com.ruoyi.common.utils.apk.apkeditor.axmleditor.editor.PermissionEditor;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;


/**
 * Created by zl on 15/9/11.
 */
public class AXMLEditor {
    private String origFile;

    public void setOrigFile(String origFile) {
        this.origFile = origFile;
    }

    public byte[] create() throws Exception {
        File newXML = null;
        ByteArrayOutputStream a = new ByteArrayOutputStream();
        try {
            newXML = new File(origFile);
            AXMLDoc doc = new AXMLDoc();
            doc.parse(new FileInputStream(newXML));
            PermissionEditor permissionEditor = new PermissionEditor(doc);
            permissionEditor.setEditorInfo(new PermissionEditor.EditorInfo()
                    .with(new PermissionEditor.PermissionOpera("android.permission.本验证由红叶网络验证提供").add())
                    .with(new PermissionEditor.PermissionOpera("android.permission.QQ.1179403448").add())
                    .with(new PermissionEditor.PermissionOpera("android.permission.COORDSOFT").add())
                    .with(new PermissionEditor.PermissionOpera("android.permission.INTERNET").add())
                    .with(new PermissionEditor.PermissionOpera("android.permission.ACCESS_NETWORK_STATE").add())
                    .with(new PermissionEditor.PermissionOpera("android.permission.ACCESS_WIFI_STATE").add())
                    .with(new PermissionEditor.PermissionOpera("android.permission.READ_PHONE_STATE").add())
            );
            permissionEditor.commit();
            doc.build(a);
            doc.release();
        } catch (Exception e) {
            throw e;
        }
        return a.toByteArray();
    }
}
