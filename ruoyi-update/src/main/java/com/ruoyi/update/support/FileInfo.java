package com.ruoyi.update.support;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Objects;

@Data
public class FileInfo {

    private String shortPath;
    private String md5;
    private Long size;
    @JSONField(serialize = false)
//    @JsonIgnore
//    private File file;
    private EnumValue.HandleStrategy handleStrategy; //处理方式：处理/忽略
    private EnumValue.DiffType diffType; //文件更新类型：增/删/改/无

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        FileInfo other = (FileInfo) obj;
        return Objects.equals(shortPath, other.shortPath);
    }
}
