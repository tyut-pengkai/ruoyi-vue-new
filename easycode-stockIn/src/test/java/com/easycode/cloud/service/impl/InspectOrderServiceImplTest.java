package com.easycode.cloud.service.impl;

import com.easycode.cloud.BaseTest;
import com.weifu.cloud.domain.dto.SendInspectResultDto;
import com.weifu.cloud.domain.dto.SendSealedResultIQCDto;
import com.easycode.cloud.service.IInspectOrderService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

class InspectOrderServiceImplTest extends BaseTest {
    @Autowired
    private IInspectOrderService inspectOrderService;

    @Test
    @DisplayName("质检后处理")
    void sendSealedResultIQC() throws Exception {
        //
        SendSealedResultIQCDto reuslt = new SendSealedResultIQCDto();
        reuslt.setTaskCode("SJ-20241202006");
        reuslt.setDestroyCount("10");

      int r =  inspectOrderService.sendSealedResultIQC(reuslt);
        Assertions.assertEquals(200, r);
    }

    @Test
    @DisplayName("送检结果")
    void sendInspectResult() throws Exception {
        SendInspectResultDto detailDto = new SendInspectResultDto();
        detailDto.setTaskCode("SJ-20241202006");
        detailDto.setResult("1");
        inspectOrderService.sendInspectResult(detailDto);
    }

    public static void main(String[] args) {
        // 创建线程池，线程池中有 10 个线程
        List<Integer> a = new ArrayList<>();
        a.add(1);
        a.add(2);
        a.add(3);
        a.add(4);
        ExecutorService executorService = Executors.newFixedThreadPool(a.size());
        // 父线程中提交任务
        for (int i = 0; i < a.size(); i++) {

            int finalI = i;
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                   int b =  active(a.get(finalI));
                   System.out.println(b);
                }
            });
        }
        //executorService.invokeAll();
        System.out.println("-------------------------");
        // 关闭线程池
        executorService.shutdown();
    }

    private static Integer active(int a){
        return a;
    }

    @Test
    @DisplayName("移库")
    void stockMove() throws Exception{

    }

}
