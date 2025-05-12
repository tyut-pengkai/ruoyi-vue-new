package com.easycode.cloud;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest( classes = {StockInApplication.class},webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BaseTest {

    @Test
    public void  contextLoad() throws Exception {
        int lineNo = 1;
        assertTrue(true);
        System.out.println(lineNo++);
        System.out.println(lineNo++);
        System.out.println(lineNo);
    }
}
