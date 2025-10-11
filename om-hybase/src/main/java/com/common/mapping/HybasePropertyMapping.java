package com.common.mapping;

import com.common.extractor.HybaseExtractor;
import lombok.Data;

import java.util.List;

/**
 *
 */
@Data
public class HybasePropertyMapping {
    private String columnName;
    private String propertyName;
    private List<HybaseExtractor> hybaseExtractorList;
}
