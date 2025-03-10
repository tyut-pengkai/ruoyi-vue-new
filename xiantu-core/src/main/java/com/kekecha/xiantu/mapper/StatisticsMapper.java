package com.kekecha.xiantu.mapper;

import com.kekecha.xiantu.domain.HistoryCount;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface StatisticsMapper {
    int increaseType(@Param("type") String type, @Param("timestamp") long timestamp);
    int clearType(@Param("type") String type);
    int getTypeCount(@Param("type") String type, @Param("timestamp") long timestamp);
    List<HistoryCount> getTypeHistoryCount(
            @Param("type") String type, @Param("start_ts") long start_timestamp, @Param("end_ts") long end_timestamp);
}
