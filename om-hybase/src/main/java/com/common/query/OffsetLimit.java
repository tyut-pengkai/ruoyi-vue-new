package com.common.query;

import lombok.Data;

@Data(staticConstructor = "of")
public class OffsetLimit {
    public static final OffsetLimit ALL = new OffsetLimit(1, Integer.MAX_VALUE);
    public static final OffsetLimit DEFAULT = new OffsetLimit(1, 10);
    public static final OffsetLimit UNIQUE = new OffsetLimit(1, 1);
    private final Integer pageNum;
    private final Integer pageSize;
}
