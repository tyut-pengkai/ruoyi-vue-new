package com.ruoyi;

import java.io.*;
import java.sql.*;
import java.util.*;

/**
 * 数据库初始化工具类
 */
public class InitDatabase {
    
    private static final String URL = "jdbc:mysql://localhost:3306/?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=GMT%2B8&allowPublicKeyRetrieval=true";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "123456";
    private static final String DB_NAME = "ruoyi";
    
    public static void main(String[] args) {
        System.out.println("开始初始化数据库...");
        
        try {
            // 加载驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("驱动加载成功");
            
            // 连接到MySQL服务器
            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("连接MySQL服务器成功");
            
            // 创建数据库
            Statement stmt = conn.createStatement();
            String createDbSql = "CREATE DATABASE IF NOT EXISTS " + DB_NAME + " CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci";
            stmt.executeUpdate(createDbSql);
            System.out.println("创建数据库 " + DB_NAME + " 成功");
            
            // 切换到新创建的数据库
            conn.setCatalog(DB_NAME);
            System.out.println("切换到数据库 " + DB_NAME + " 成功");
            
            // 导入SQL脚本
            List<String> sqlFiles = Arrays.asList(
                "ry_20250522.sql",
                "quartz.sql",
                "database_menu_new.sql",
                "database_product.sql"
            );
            
            for (String sqlFile : sqlFiles) {
                String filePath = "c:\\Users\\zjj\\RuoYi-Vue\\sql\\" + sqlFile;
                File file = new File(filePath);
                if (file.exists()) {
                    System.out.println("开始导入脚本: " + sqlFile);
                    executeSqlFile(conn, filePath);
                    System.out.println("导入脚本 " + sqlFile + " 成功");
                } else {
                    System.out.println("警告: 脚本文件 " + filePath + " 不存在");
                    System.out.println("当前工作目录: " + System.getProperty("user.dir"));
                }
            }
            
            // 关闭连接
            stmt.close();
            conn.close();
            System.out.println("数据库初始化完成！");
            
        } catch (Exception e) {
            System.out.println("数据库初始化失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 执行SQL文件
     */
    private static void executeSqlFile(Connection conn, String filePath) throws Exception {
        File file = new File(filePath);
        StringBuilder sql = new StringBuilder();
        
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                // 跳过注释和空行
                if (line.isEmpty() || line.startsWith("--") || line.startsWith("#")) {
                    continue;
                }
                sql.append(line).append(" ");
                // 如果是完整的SQL语句，执行
                if (line.endsWith(";")) {
                    executeSql(conn, sql.toString());
                    sql.setLength(0);
                }
            }
            // 执行最后一条SQL语句（如果有）
            if (sql.length() > 0) {
                executeSql(conn, sql.toString());
            }
        }
    }
    
    /**
     * 执行SQL语句
     */
    private static void executeSql(Connection conn, String sql) throws Exception {
        if (sql.trim().isEmpty()) {
            return;
        }
        
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            // 忽略某些可能的错误，如重复创建表
            if (!e.getMessage().contains("already exists") && 
                !e.getMessage().contains("Duplicate entry")) {
                throw e;
            }
        }
    }
}