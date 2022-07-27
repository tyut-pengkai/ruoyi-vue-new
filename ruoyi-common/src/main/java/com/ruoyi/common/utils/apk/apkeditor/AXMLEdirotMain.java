package com.ruoyi.common.utils.apk.apkeditor;

/**
 * Created by zl on 15/9/8.
 */
public class AXMLEdirotMain {

    public static byte[] AXML(String in) throws Exception {
        AXMLEditor editor;
        try {
            editor = new AXMLEditor();
            editor.setOrigFile(in);
            return editor.create();
        } catch (Exception e) {
            throw e;
        }
    }
}
