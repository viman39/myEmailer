package Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Reader{
    private static Reader reader = null;
    private Connection conn = null;

    private Reader(Connection conn){
        this.conn = conn;
    }

    public static Reader getInstance(){
        if(reader == null){
            Reader reader = new Reader(DBConn.getConnection());
        }

        return reader;
    }

    public ResultSet selectClientId(String name){
        String query = "SELECT * FROM clients WHERE name = ?;";
        ResultSet result = null;

        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, name);
            result = stmt.executeQuery();
            stmt.close();

            return result;
        } catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public ResultSet selectClient(String name, String password){
        String query = "SELECT * FROM clients WHERE name = ? and password = ?;";

        try{
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, name);
            stmt.setString(2, password);
            ResultSet result = stmt.executeQuery();
            stmt.close();

            return result;
        } catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public boolean checkFriendship(int id_client1, int id_client2){
        String query = "SELECT * FROM friends WHERE (id_client1 = ? and id_client2 = ?) or (id_client1 = ? and id_client2 = ?);";
        ResultSet result = null;

        try{
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, id_client1);
            stmt.setInt(2, id_client2);
            stmt.setInt(3, id_client2);
            stmt.setInt(4, id_client1);
            result = stmt.executeQuery();
            stmt.close();
        } catch(Exception e){
            e.printStackTrace();
        }

        return result == null;
    }

    public ResultSet selectFriends(int id_client){
        String query = "SELECT * FROM friends WHERE id_client1 = ? or id_client2 = ?;";
        ResultSet result = null;

        try{
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, id_client);
            stmt.setInt(2, id_client);
            result = stmt.executeQuery();
            stmt.close();
        } catch(Exception e){
            e.printStackTrace();
        }

        return result;
    }

    public ResultSet selectMessages(int id_client){
        String query = "SELECT * FROM messages WHERE id_receiver = ?;";
        ResultSet result = null;

        try{
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, id_client);
            result = stmt.executeQuery();
            stmt.close();
        } catch(Exception e){
            e.printStackTrace();
        }

        return result;
    }
}
