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
            reader = new Reader(DBConn.getConnection());
        }

        return reader;
    }

    public ResultSet selectClientInfo(int id_client){
        String query = "SELECT * FROM clients WHERE id = ?;";
        ResultSet result = null;

        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, id_client);
            result = stmt.executeQuery();
        } catch(Exception e){
            e.printStackTrace();
        }

        return result;
    }

    public int selectClientId(String name){
        String query = "SELECT id FROM clients WHERE name = ?";
        ResultSet result;
        int id = -1;

        try{
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, name);
            result = stmt.executeQuery();
            result.first();
            id = result.getInt("id");
        } catch (Exception e){
            e.printStackTrace();
        }

        return id;
    }

    public ResultSet selectClient(String name, String password){
        String query = "SELECT * FROM clients WHERE name = ? and password = ?;";
        ResultSet result;
        try{
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, name);
            stmt.setString(2, password);
            result = stmt.executeQuery();
        } catch(Exception e){
            e.printStackTrace();
            result = null;
        }

        return result;
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
        } catch(Exception e){
            e.printStackTrace();
        }

        return result;
    }
}
