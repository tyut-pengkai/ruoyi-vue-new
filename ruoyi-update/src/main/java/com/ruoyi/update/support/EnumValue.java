package com.ruoyi.update.support;

public class EnumValue {

    public enum HandleStrategy {
        HANDLE, IGNORE
    }

    public enum DeleteStrategy {
        HANDLE, IGNORE
    }

    public enum OverwriteStrategy {
        HANDLE, IGNORE
    }

    public enum DiffType {
        ADD, DEL, UPDATE, NONE
    }

    public enum FilterType {
        DIR, FILE
    }

}
