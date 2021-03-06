import Database.Creator;
import Database.Deleter;
import Database.Reader;
import Database.Updater;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ClientRequestManager {
    private static ClientRequestManager clientRequestManager = null;
    private String userName = "Victor";
    private Creator creator;
    private Deleter deleter;
    private Reader reader;
    private Updater updater;

    private ClientRequestManager(){
        creator = Creator.getInstance();
        deleter = Deleter.getInstance();
        updater = Updater.getInstance();
        reader = Reader.getInstance();
    }

    public static ClientRequestManager getInstance(){
        if(clientRequestManager == null)
            clientRequestManager = new ClientRequestManager();

        return clientRequestManager;
    }

    public List<ResponseMessage> requestParser(String request){
        String[] tokens = request.split(" ");

        List<ResponseMessage> response = new ArrayList<>();

        try{
            if(tokens[0].equals("login")){
                response = login(tokens[1], tokens[2]);
            } else if(tokens[0].equals("register")){
                response = register(tokens[1], tokens[2]);
            } else if(tokens[0].equals("friend") && userName != null){
                String[] friends = new String[tokens.length-1];
                System.arraycopy(tokens, 1, friends, 0, tokens.length-1);

                response = friend(friends);
            } else if(tokens[0].equals("unfriend") && userName != null){
                response = unfriend(tokens[1]);
            } else if(tokens[0].equals("message") && userName != null){
                String message = "";
                for(int i=1; i<tokens.length; i++){
                    message = message + tokens[i];
                }

                response = message(message);
            } else if(tokens[0].equals("read") && userName != null){
                response = read();
            } else if(tokens[0].equals("logout") && userName != null){
                userName = "";
            } else if(tokens[0].equals("help")){
                response = help();
            } else if(userName == null){
                response.add(new ResponseMessage("You must login first!"));
            } else{
                response.add(new ResponseMessage("Invalid command!"));
                response.add(new ResponseMessage("Use help for more information!"));
            }
        } catch(ArrayIndexOutOfBoundsException e){
            response.add(new ResponseMessage("Invalid number of parameters!"));
            response.add(new ResponseMessage("Try using help!"));
        }

        return response;
    }

    private List<ResponseMessage> login(String name, String password){
        ResultSet result = reader.selectClient(name, password);
        List<ResponseMessage> response = new ArrayList<>();

        if (result != null){
            response.add(new ResponseMessage("You are logged in!"));
        } else{
            response.add(new ResponseMessage("Something went wrong"));
        }

        return response;
    }

    private List<ResponseMessage> register(String name, String password){
        List<ResponseMessage> response = new ArrayList<>();

        if(reader.selectClientId(name) != -1){
            response.add(new ResponseMessage("This username already exists"));
        }

        if(creator.insertClient(name, password)){
            response.add(new ResponseMessage("Register complete!"));
        } else{
            response.add(new ResponseMessage("Something went wrong"));
        }

        return response;
    }

    private List<ResponseMessage> friend(String[] friends){
        int id_userName = 0;
        List<ResponseMessage> response = new ArrayList<>();

        try {
            id_userName = reader.selectClientId(userName);
        } catch(Exception e){
            System.out.println("Failed to get user ID");
            e.printStackTrace();
        }

        for(String friend:friends){
            try {
                int id_friend = reader.selectClientId(friend);

                if(reader.checkFriendship(id_userName, id_friend)){
                    response.add(new ResponseMessage("You are already friends with "+friend));
                    continue;
                }

                if (creator.insertFriendship(id_userName, id_friend)){
                    response.add(new ResponseMessage("You are now friend with "+friend));
                } else{
                    response.add(new ResponseMessage("Failed to add "+friend+" in your friends list"));
                }
            } catch(Exception e){
                e.printStackTrace();
            }
        }

        return response;
    }

    private List<ResponseMessage> unfriend(String friend){
        List<ResponseMessage> response = new ArrayList<>();

        int id_friend = reader.selectClientId(friend);
        if(id_friend == -1){
            response.add(new ResponseMessage("The user "+friend+" doesn't exist"));
        }

        int id_userName = reader.selectClientId(userName);
        if(deleter.deleteFriendship(id_friend, id_userName)){
            response.add(new ResponseMessage("You are no longer friend with "+friend));
        } else{
            response.add(new ResponseMessage("Something went wrong!"));
        }

        return response;
    }

    private List<ResponseMessage> message(String message){
        int id_user = reader.selectClientId(userName);
        List<ResponseMessage> response = new ArrayList<>();

        try {
            ResultSet friends = reader.selectFriends(id_user);
            while(friends.next()){
                int id_friend = friends.getInt("id_client1") == id_user ? friends.getInt("id_client2") : friends.getInt("id_client1");

                creator.insertMessage(id_user, id_friend, message);
            }
            response.add(new ResponseMessage("The message was sent to your friends!"));
        } catch(Exception e){
            response.add(new ResponseMessage("Something went wrong!"));
            e.printStackTrace();
        }

        return response;
    }

    private List<ResponseMessage> read(){
        List<ResponseMessage> response = new ArrayList<>();
        int id_client = reader.selectClientId(userName);

        try{
            ResultSet messages = reader.selectMessages(id_client);
            if(messages == null){
                response.add(new ResponseMessage("You have no messages"));
                return response;
            }
            while(messages.next()){
                ResultSet sender_info = reader.selectClientInfo(messages.getInt("id_sender"));
                String sender_name = sender_info.getString("name");
                sender_info.close();

                response.add(new ResponseMessage(sender_name +": "+messages.getString("message")));
            }
        } catch(Exception e){
            e.printStackTrace();
            response.add(new ResponseMessage("Something went wrong"));
        }

        return response;
    }

    private List<ResponseMessage> help(){
        List<ResponseMessage> response = new ArrayList<>();

        response.add(new ResponseMessage("login <name> <username>"));
        response.add(new ResponseMessage("register <name> <password> <repeatPassword>"));
        response.add(new ResponseMessage("friend <name1> <name2> ..."));
        response.add(new ResponseMessage("unfriend <name1>"));
        response.add(new ResponseMessage("message <message>"));
        response.add(new ResponseMessage("read"));
        response.add(new ResponseMessage("logout"));

        return response;
    }
}
