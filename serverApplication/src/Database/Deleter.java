package Database;

import java.sql.Connection;

public class Deleter {
    private static Deleter deleter = null;
    private Connection conn = null;

    private Deleter(Connection conn){
        this.conn = conn;
    }

    public static Deleter getInstance(){
        if(deleter == null){
            deleter = new Deleter(DBConn.getConnection());
        }

        return deleter;
    }
}
