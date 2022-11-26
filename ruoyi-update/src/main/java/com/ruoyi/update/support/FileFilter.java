package com.ruoyi.update.support;

import lombok.Data;

@Data
public class FileFilter {

    private EnumValue.FilterType filterType;
    private String shortPath;
    private EnumValue.HandleStrategy handleStrategy;
    private EnumValue.DeleteStrategy deleteStrategy;
    private EnumValue.OverwriteStrategy overwriteStrategy;

    public FileFilter(EnumValue.FilterType filterType, String shortPath, EnumValue.HandleStrategy strategy) {
        this.filterType = filterType;
        this.shortPath = shortPath;
        this.handleStrategy = strategy;
    }

    public FileFilter(EnumValue.FilterType filterType, String shortPath, EnumValue.HandleStrategy strategy, EnumValue.DeleteStrategy deleteStrategy) {
        this.filterType = filterType;
        this.shortPath = shortPath;
        this.handleStrategy = strategy;
        this.deleteStrategy = deleteStrategy;
    }

    public FileFilter(String shortPath, EnumValue.HandleStrategy strategy) {
        this.shortPath = shortPath;
        this.handleStrategy = strategy;
    }

}
