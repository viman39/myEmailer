import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DBConn {
    private Connection con;
    private Statement stmt;
    private ResultSet result;

    public DBConn(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/messenger?autoReconnect=true&useSSL=false", "root", "");
            stmt = con.createStatement();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     *
     * @param name
     * @param pass
     * @return 0 = OK
     * @return 1 = name already exists
     * @return -1 = database error
     */
    public int register(String name, String pass){
        try {
            String sqlCheck = "SELECT * FROM clients WHERE name = \'" + name + "\';";
            result = stmt.executeQuery(sqlCheck);
            if(result.first()){
                return 1;
            }

            String sqlInsert = "INSERT INTO clients(name, password) VALUES(\'" + name + "\', \'" + pass + "\');";
            stmt.executeUpdate(sqlInsert);
            return 0;
        } catch(Exception e){
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * @param name
     * @param pass
     * @return 0 = OK
     * @return 1 = name or password incorrect
     * @return -1 = database error
     */
    public int login(String name, String pass){
        try{
            String sql = "SELECT * FROM clients WHERE name = \'" + name + "\' and password = \'" + pass + "\';";
            result = stmt.executeQuery(sql);
            return result.first() ? 0 : 1;
        } catch(Exception e){
            e.printStackTrace();
            return -1;
        }
    }

    /**
     *
     * @param name
     * @param friends list of friends
     * @return 0 OK
     * @return -1 database error
     * @return -2 friends doesn't exist!
     */
    public int friend(String name, String[] friends){
        try {
            String sqlGetId = "SELECT * FROM clients WHERE name = \'" + name + "\';";
            result = stmt.executeQuery(sqlGetId);
            result.first();
            int id = result.getInt("id");

            for(String friend : friends){
                String sqlGetFriendId = "SELECT * FROM clients WHERE name = \'" + friend + "\';";
                result = stmt.executeQuery(sqlGetFriendId);

                int friendId;
                if(result.first()) {
                    friendId = result.getInt("id");
                } else return -2;

                String sqlCheckEntry = "SELECT * FROM friends WHERE (id_client1 = " + id + " and id_client2 = " + friendId + ") or (id_client1 = " + friendId + " and id_client2 = " + id + ");";
                result = stmt.executeQuery(sqlCheckEntry);
                if(!result.first()) {
                    String sqlAddFriends = "INSERT INTO friends (id_client1, id_client2) VALUES( " + id + ", " + friendId + ");";
                    stmt.executeUpdate(sqlAddFriends);
                }
            }
            return 0;
        } catch(Exception e){
            e.printStackTrace();
            return -1;
        }
    }

    /**
     *
     * @param name
     * @param message
     * @return 0 = OK
     * @return -1 = database error
     */
    public int message(String name, String message){
        try{
            String sqlGetSenderId = "SELECT * FROM clients WHERE name =\'" + name + "\';";
            result = stmt.executeQuery(sqlGetSenderId);
            result.first();
            int senderId = result.getInt("id");

            String getFriendsIds = "SELECT * FROM friends WHERE id_client1 = " + senderId + " or id_client2 = " + senderId + ";";
            result = stmt.executeQuery(getFriendsIds);
            while(result.next()){
                String sqlAddMessage = "INSERT INTO messages(id_sender, id_receiver, message) VALUES( " + senderId + ", " +
                        (senderId == result.getInt("id_client1") ? result.getInt("id_client2") : result.getInt("id_client1")) +
                        ", \'" + message + "\');";
                con.createStatement().executeUpdate(sqlAddMessage);
            }
            return 0;
        } catch(Exception e){
            e.printStackTrace();
            return -1;
        }
    }

    /**
     *
     * @param name
     * @return String messages OK
     * @return String "" no messages
     * @return -1 database error
     */
    public String read(String name){
        try{
            String messages = "";

            String sqlGetNameId = "SELECT * FROM clients WHERE name = \'" + name + "\';";
            result = stmt.executeQuery(sqlGetNameId);
            result.first();
            int idReceiver = result.getInt("id");

            String sqlGetMessages = "SELECT * FROM messages WHERE id_receiver = " +idReceiver + " ;";
            result = stmt.executeQuery(sqlGetMessages);

            while(result.next()){
                messages = messages + result.getInt("id_sender") + ":" + result.getString("message") + "\n";
                con.createStatement().executeUpdate("DELETE FROM messages WHERE id_receiver = " + idReceiver + " and message = \'" + result.getString("message") + "\' ;");
            }

            return messages;
        } catch(Exception e){
            e.printStackTrace();
            return "-1";
        }
    }

    public void close(){
        try {
            result.close();
            stmt.close();
            con.close();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
