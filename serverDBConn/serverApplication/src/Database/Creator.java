package Database;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class Creator {
    private static Creator selecter = null;
    private Connection conn = null;

    private Creator(Connection conn){
        this.conn = conn;
    }

    public static Creator getInstance(){
        if(selecter == null){
            selecter = new Creator(DBConn.getConnection());
        }

        return selecter;
    }

    public boolean insertClient(String name, String password){
        String query = "INSERT INTO clients(name, password) VALUES(?, ?);";
        int result = 0;

        try{
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, name);
            stmt.setString(2, password);
            result = stmt.executeUpdate();
            stmt.close();
        } catch(Exception e){
            e.printStackTrace();
        }

        return (result != 0);
    }

    public boolean insertFriendship(int id_friend1, int id_friend2){
        String query = "INSERT INTO friends(id_client1, id_client2) VALUES(?, ?);";
        int result = 0;

        try{
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, id_friend1);
            stmt.setInt(2, id_friend2);
            result = stmt.executeUpdate();
            stmt.close();
        } catch(Exception e){
            e.printStackTrace();
        }

        return (result != 0);
    }

    public boolean insertMessage(int id_sender, int id_receiver, String message){
        String query = "INSERT INTO messages(id_sender, id_receiver, message) VALUES(?, ?, ?);";
        int result = 0;

        try{
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, id_sender);
            stmt.setInt(2, id_receiver);
            stmt.setString(3, message);
            result = stmt.executeUpdate();
            stmt.close();
        } catch(Exception e){
            e.printStackTrace();
        }

        return (result!=0);
    }
}
