package com.ruoyi.system.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.script.*;
import java.util.List;

@SpringBootTest
public class RemoteFunctionTest {


    public static void main(String[] args) {
        ScriptEngineManager sem = new ScriptEngineManager();
        List<ScriptEngineFactory> engineFactories = sem.getEngineFactories();
        for (ScriptEngineFactory factory : engineFactories) {
            System.out.println(factory.getLanguageName());
        }
    }

    @Test
    public void test() throws ScriptException, InterruptedException {
        ScriptEngineManager sem = new ScriptEngineManager();
        ScriptEngine engine = sem.getEngineByName("javascript");     //python or jython,

        List<ScriptEngineFactory> engineFactories = sem.getEngineFactories();
        for (ScriptEngineFactory factory : engineFactories) {
            System.out.println(factory.getEngineName());
        }

        //向上下文中存入变量
        engine.put("msg", "just a test");
        //定义类user
        String str = "msg += '!!!';var user = {name:'tom',age:23,hobbies:['football','basketball']}; ";
        engine.eval(str);

        //从上下文引擎中取值
        String msg = (String) engine.get("msg");
        String name = (String) engine.get("name");
        String[] hb = (String[]) engine.get("hobbies");
        System.out.println(msg);
        System.out.println(name + ":" + hb[0]);

        //定义数学函数
        engine.eval("function add (a, b) {c = a + b; return c; }");

        //取得调用接口
        Invocable jsInvoke = (Invocable) engine;

        //定义run()函数
        engine.eval("function run() {print('www.java2s.com');}");
        Invocable invokeEngine = (Invocable) engine;
        Runnable runner = invokeEngine.getInterface(Runnable.class);

        //定义线程运行之
        Thread t = new Thread(runner);
        t.start();
        t.join();

        //导入其他java包
        String jsCode = "importPackage(java.util);var list2 = Arrays.asList(['A', 'B', 'C']);";
        engine.eval(jsCode);
        List<String> list2 = (List<String>) engine.get("list2");
        for (String val : list2) {
            System.out.println(val);
        }

    }
}