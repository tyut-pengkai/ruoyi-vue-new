import java.sql.*;

public class TestDBConnection {
    public static void main(String[] args) {
        System.out.println("Testing database connection...");
        
        try {
            // Load MySQL driver explicitly
            System.out.println("Loading MySQL driver...");
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("MySQL driver loaded successfully!");
            
            // Test 1: Connect to MySQL server without specifying database
            String url = "jdbc:mysql://localhost:3306/?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=GMT%2B8&allowPublicKeyRetrieval=true";
            String username = "root";
            String password = "123456";
            
            System.out.println("Connecting to MySQL server...");
            Connection conn = DriverManager.getConnection(url, username, password);
            System.out.println("Connected to MySQL server successfully!");
            
            // Test 2: List all databases
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SHOW DATABASES");
            System.out.println("\nAvailable databases:");
            boolean ruoyiExists = false;
            while (rs.next()) {
                String dbName = rs.getString(1);
                System.out.println("- " + dbName);
                if (dbName.equals("ruoyi")) {
                    ruoyiExists = true;
                    System.out.println("  - ruoyi database found!");
                }
            }
            
            if (!ruoyiExists) {
                System.out.println("  - ruoyi database NOT found!");
            }
            
            // Test 3: Try to connect directly to ruoyi database
            System.out.println("\nTrying to connect directly to ruoyi database...");
            String ruoyiUrl = "jdbc:mysql://localhost:3306/ruoyi?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=GMT%2B8&allowPublicKeyRetrieval=true";
            Connection ruoyiConn = DriverManager.getConnection(ruoyiUrl, username, password);
            System.out.println("Connected to ruoyi database successfully!");
            
            // Test 4: List tables in ruoyi database
            Statement ruoyiStmt = ruoyiConn.createStatement();
            ResultSet ruoyiRs = ruoyiStmt.executeQuery("SHOW TABLES");
            System.out.println("\nTables in ruoyi database:");
            int tableCount = 0;
            while (ruoyiRs.next()) {
                System.out.println("- " + ruoyiRs.getString(1));
                tableCount++;
            }
            System.out.println("\nFound " + tableCount + " tables in ruoyi database");
            
            // Close connections
            ruoyiRs.close();
            ruoyiStmt.close();
            ruoyiConn.close();
            rs.close();
            stmt.close();
            conn.close();
            
            System.out.println("\nAll tests passed!");
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}