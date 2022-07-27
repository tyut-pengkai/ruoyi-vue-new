package com.ruoyi.common.utils.apk.apkeditor.axmleditor.editor;

import com.ruoyi.common.utils.apk.apkeditor.axmleditor.decode.AXMLDoc;
import com.ruoyi.common.utils.apk.apkeditor.axmleditor.decode.BTagNode;
import com.ruoyi.common.utils.apk.apkeditor.axmleditor.decode.BXMLNode;
import com.ruoyi.common.utils.apk.apkeditor.axmleditor.decode.StringBlock;
import com.ruoyi.common.utils.apk.apkeditor.axmleditor.utils.TypedValue;


/**
 * 修改<application>节点属性
 * <p>
 * ApplicationInfoEditor applicationInfoEditor = new ApplicationInfoEditor(doc);
 * applicationInfoEditor.setEditorInfo(new ApplicationInfoEditor.EditorInfo("app_name", false)); //设置app name 和是否开启debuggable
 * applicationInfoEditor.commit();
 * <p>
 * Created by zl on 15/9/8.
 */
public class ApplicationInfoEditor extends BaseEditor<ApplicationInfoEditor.EditorInfo> {
    public ApplicationInfoEditor(AXMLDoc doc) {
        super(doc);
    }

    @Override
    public String getEditorName() {
        return NODE_APPLICATION;
    }

    @Override
    protected void editor() {
        BTagNode node = (BTagNode) findNode();
        if (node != null) {
            if (node.getAttrStringForKey(editorInfo.label_Name) != -1) {
                editorInfo.App = doc.getStringBlock().getStringFor(node.getAttrStringForKey(editorInfo.label_Name));
                final StringBlock stringBlock = doc.getStringBlock();
                node.setAttrStringForKey(editorInfo.label_Name, editorInfo.label_Value);
                stringBlock.setString(editorInfo.label_Value, editorInfo.label);
            } else {
                BTagNode.Attribute Appinfo_attr = new BTagNode.Attribute(attr_name, attr_name, TypedValue.TYPE_STRING);
                Appinfo_attr.setString(editorInfo.label_Value);
                node.setAttribute(Appinfo_attr);
                doc.getStringBlock().setString(editorInfo.label_Value, editorInfo.label);
            }
        }
    }

    @Override
    protected BXMLNode findNode() {
        return doc.getApplicationNode();
    }

    @Override
    protected void registStringBlock(StringBlock sb) {
        namespace = sb.putString(NAME_SPACE);
        attr_name = sb.putString(NAME);
        attr_value = sb.putString(VALUE);
        editorInfo.label_Name = sb.putString(EditorInfo.LABEL);
        editorInfo.label_Value = sb.addString(String.valueOf(editorInfo.label));
    }


    public static class EditorInfo {
        public static final String LABEL = "name";
        public static String App = "";
        private String label;
        private int label_Name;
        private int label_Value;

        public EditorInfo(String label) {
            this.label = label;
        }
    }

}
