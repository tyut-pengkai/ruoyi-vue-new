package com.ruoyi.system.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class UpdateTest {

    @Test
    public void test() {
        List<String> sqlList = Arrays.asList(
                "1-2",
                "2-3",
                "3-4",
                "4-5",
                "5-6",
                "6-7",
                "7-8",
                "8-9",
                "9-10",
                "10-11",
                "11-12",
                "12-13",
                "13-14",
                "14-15",
               "15-16"
        );
        List<String> readySqlList = new ArrayList<>();
        Long currVersion = 5L;
        Long latestVersion = 10L;
        recursion(5L, 10L, sqlList, readySqlList);
        System.out.println(readySqlList);
    }


    private void recursion(Long fromVersion, Long toVersion, List<String> sqlList, List<String> readySqlList) {
        for (String sql : sqlList) {
            String[] split = sql.split("-");
            if (split.length == 2) {
                long oldVer = Long.parseLong(split[0]);
                long newVer = Long.parseLong(split[1]);
//                if(Objects.equals(fromVersion, toVersion)) {
//                    break;
//                }
                if (oldVer == fromVersion) {
                    readySqlList.add(sql);
                    recursion(newVer, toVersion, sqlList, readySqlList);
                    break;
                }
            }
        }
    }
}