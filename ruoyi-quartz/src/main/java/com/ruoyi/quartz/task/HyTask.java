package com.ruoyi.quartz.task;

import com.ruoyi.quartz.task.impl.ClearTrailTimesTask;
import com.ruoyi.quartz.task.impl.ClearUnbindlTimesTask;
import com.ruoyi.quartz.task.impl.RecordAppUserNumTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component("hyTask")
@Slf4j
public class HyTask {

    @Resource
    private RecordAppUserNumTask recordAppUserNumTask;
    @Resource
    private ClearTrailTimesTask clearTrailTimesTask;
    @Resource
    private ClearUnbindlTimesTask clearUnbindlTimesTask;

    public void recordAppUserNum() {
        recordAppUserNumTask.recordAppUserNum();
    }

    public void clearTrailTimes() {
        clearTrailTimesTask.clearTrailTimes();
    }

    public void clearUnbindTimes() {
        clearUnbindlTimesTask.clearUnbindTimes();
    }

}
