package Database;

import java.sql.Connection;

public class Updater {
    private static Updater updater = null;
    private Connection conn = null;

    private Updater(Connection conn){
        this.conn = conn;
    }

    public static Updater getInstance(){
        if(updater == null){
            updater = new Updater(DBConn.getConnection());
        }

        return updater;
    }
}
