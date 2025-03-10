package com.kekecha.xiantu.service;


import com.kekecha.xiantu.domain.HistoryCount;

import java.util.List;

public interface IStatisticsService {
    int increaseType(String type, long timestamp);
    int clearType(String type);
    int getTypeCount(String type, long timestamp);
    List<HistoryCount> getTypeHistoryCount(String type, long start_timestamp, long end_timestamp);
}
