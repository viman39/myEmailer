package Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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

    public boolean deleteFriendship(int id_friend1, int id_friend2){
        String query = "DELETE FROM friends WHERE (id_client1 = ? and id_client2 = ?) or (id_client1 = ? and id_client2 = ?);";
        ResultSet result = null;

        try{
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, id_friend1);
            stmt.setInt(2, id_friend2);
            stmt.setInt(3, id_friend2);
            stmt.setInt(4, id_friend1);
            result = stmt.executeQuery();
        } catch(Exception e){
            e.printStackTrace();
        }

        return result == null;
    }
}
