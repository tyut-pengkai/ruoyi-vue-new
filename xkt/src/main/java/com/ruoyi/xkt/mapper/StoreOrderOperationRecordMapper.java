package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.StoreOrderOperationRecord;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author liangyq
 * @date 2025-04-02 12:48
 */
@Repository
public interface StoreOrderOperationRecordMapper extends BaseMapper<StoreOrderOperationRecord> {

    int batchInsert(List<StoreOrderOperationRecord> records);
}
