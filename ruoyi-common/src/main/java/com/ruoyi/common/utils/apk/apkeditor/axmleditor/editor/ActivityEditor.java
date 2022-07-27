package com.ruoyi.common.utils.apk.apkeditor.axmleditor.editor;

import com.ruoyi.common.utils.apk.apkeditor.axmleditor.decode.AXMLDoc;
import com.ruoyi.common.utils.apk.apkeditor.axmleditor.decode.BTagNode;
import com.ruoyi.common.utils.apk.apkeditor.axmleditor.decode.BXMLNode;
import com.ruoyi.common.utils.apk.apkeditor.axmleditor.decode.StringBlock;
import com.ruoyi.common.utils.apk.apkeditor.axmleditor.utils.TypedValue;

import java.util.List;

public class ActivityEditor extends BaseEditor<ActivityEditor.EditorInfo> {

    private int ActivityItem;

    public ActivityEditor(AXMLDoc doc) {
        super(doc);
        setEditor(NAME, VALUE);
    }

    @Override
    public String getEditorName() {
        return NODE_METADATA;
    }

    @Override
    protected void editor() {
        doc.getStringBlock().setString(doc.getStringBlock().putString(editorInfo.MainActivity), doc.getStringBlock().getStringFor(editorInfo.metaName_Index));
        BXMLNode application = doc.getApplicationNode(); //manifest node
        List<BXMLNode> children = application.getChildren();
        BTagNode meta_data = (BTagNode) findNode();

        BTagNode.Attribute name_attr = new BTagNode.Attribute(namespace, attr_name, TypedValue.TYPE_STRING);
        name_attr.setString(doc.getStringBlock().putString(editorInfo.MainActivity));
        BTagNode.Attribute value_attr = new BTagNode.Attribute(namespace, attr_value, TypedValue.TYPE_STRING);
        value_attr.setString(editorInfo.metaValue_Index);
        meta_data = new BTagNode(-1, this.ActivityItem);
        meta_data.setAttribute(name_attr);
        meta_data.setAttribute(value_attr);
        children.add(meta_data);
        doc.getStringBlock().setString(editorInfo.metaValue_Index, editorInfo.ActivityLabel);
    }

    @Override
    protected BXMLNode findNode() {
        BXMLNode application = doc.getApplicationNode(); //manifest node
        List<BXMLNode> children = application.getChildren();
        BTagNode meta_data = null;
        end:
        for (BXMLNode node : children) {
            BTagNode m = (BTagNode) node;
            if ((this.ActivityItem == m.getName()) && (m.getAttrStringForKey(attr_name) == editorInfo.metaName_Index)) {
                meta_data = m;
                break end;
            }
        }
        return meta_data;
    }


    @Override
    protected void registStringBlock(StringBlock sb) {
        namespace = sb.putString(NAME_SPACE);
        ActivityItem = sb.putString("activity");

        attr_name = sb.putString(NAME);
        attr_value = sb.putString("label");

        editorInfo.metaName_Index = sb.putString(editorInfo.ActivityName);
        editorInfo.metaValue_Index = sb.addString(editorInfo.ActivityLabel);
    }

    public static class EditorInfo {
        private String ActivityName;
        private String ActivityLabel;
        private String MainActivity;
        private int metaName_Index;
        private int metaValue_Index;

        public EditorInfo(String MainActivity, String ActivityName, String ActivityLabel) {
            this.MainActivity = MainActivity;
            this.ActivityName = ActivityName;
            this.ActivityLabel = ActivityLabel;
        }
    }
}
