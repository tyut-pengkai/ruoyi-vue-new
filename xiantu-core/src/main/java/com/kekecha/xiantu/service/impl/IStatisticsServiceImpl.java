package com.kekecha.xiantu.service.impl;

import com.kekecha.xiantu.domain.HistoryCount;
import com.kekecha.xiantu.mapper.StatisticsMapper;
import com.kekecha.xiantu.service.IStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IStatisticsServiceImpl implements IStatisticsService {
    @Autowired
    StatisticsMapper statisticsMapper;

    public int increaseType(String type, long timestamp)
    {
        return statisticsMapper.increaseType(type, timestamp);
    }

    public int clearType(String type)
    {
        return statisticsMapper.clearType(type);
    }

    public int getTypeCount(String type, long timestamp)
    {
        return statisticsMapper.getTypeCount(type, timestamp);
    }

    public List<HistoryCount> getTypeHistoryCount(String type, long start_timestamp, long end_timestamp)
    {
        return statisticsMapper.getTypeHistoryCount(type, start_timestamp, end_timestamp);
    }
}
