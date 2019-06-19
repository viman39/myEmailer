package Database;

import java.sql.Connection;
import java.sql.DriverManager;

class DBConn {
    static private Connection conn = null;

    private DBConn(){}

    public static Connection getConnection(){
        try{
            if(conn == null){
                Class.forName("com.mysql.cj.jdbc.Driver");
                conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/messenger?autoReconnect=true&useSSL=false", "root", "");
            }
        } catch(Exception e){
            e.printStackTrace();
        }

        return conn;
    }

    public void close(){
        try {
            conn.close();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
