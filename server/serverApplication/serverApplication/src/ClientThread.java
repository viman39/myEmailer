import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread extends Thread{
    private Socket socket = null;
    public ClientThread(Socket socket){
        this.socket = socket;
    }
    public void run(){
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            DBConn con = new DBConn();
            String user = "";

            String request = in.readLine();

            try {
                while (!request.equals("exit")) {
                    System.out.println(request);
                    String[] tokens = request.split(" ");

                    if (tokens[0].equals("login")) {
                        int response = con.login(tokens[1], tokens[2]);
                        if (response == 0) {
                            out.println("You are loged in!\n" +
                                    "endOfResponse");
                            user = tokens[1];
                        } else {
                            out.println("Ohooh... try again!\n" +
                                    "endOfResponse");
                        }
                    } else if (tokens[0].equals("register")) {
                        int response = con.register(tokens[1], tokens[2]);
                        if (response == 0) {
                            out.println("You are loged in!\n" +
                                    "endOfResponse");
                            user = tokens[1];
                        } else {
                            out.println("Ohooh... try again!" + response + "\n" +
                                    "endOfResponse");
                        }
                    } else if (tokens[0].equals("friend") && !user.equals("")) {
                        String[] friends = new String[tokens.length - 1];
                        for (int i = 1; i < tokens.length; i++) {
                            friends[i - 1] = tokens[i];
                        }
                        int response = con.friend(user, friends);
                        if (response == 0) {
                            out.println("You are loged in!\n" +
                                    "endOfResponse");
                            user = tokens[1];
                        } else {
                            out.println("Ohooh... try again!\n" +
                                    "endOfResponse");
                        }
                    } else if (tokens[0].equals("message") && !user.equals("")) {
                        String message = "";
                        for (int i = 1; i < tokens.length; i++)
                            message = message + tokens[i] + " ";

                        int response = con.message(user, message);
                        if (response == 0) {
                            out.println("Message was sent!\n" +
                                    "endOfResponse");
                        } else {
                            out.println("Ohooh... try again!\n" +
                                    "endOfResponse");
                        }
                    } else if (tokens[0].equals("read") && !user.equals("")) {
                        String response = con.read(user);
                        if (response.equals("")) {
                            out.println("Your inbox is empty!\n" +
                                    "endOfResponse");
                        } else if (response.equals("-1")) {
                            out.println("Ohooh... try again!\n" +
                                    "endOfResponse");
                        } else {
                            out.println(response + "\n" +
                                    "endOfResponse");
                        }
                    } else if (tokens[0].equals("help")) {
                        out.println("login <name> <password>\n" +
                                "register <name> <password> <repeat password>\n" +
                                "friend <friend1> <friend2> ...\n" +
                                "message <yourMessage>\n" +
                                "read\n" +
                                "endOfResponse\n");
                    } else if (user.equals("")) {
                        out.println("You must login or register first!\n" +
                                "Try using \"help\"\n" +
                                "endOfResponse");
                    } else {
                        out.println("Invalid command!\n" +
                                "Try help!\n" +
                                "endOfResponse");
                    }

                    request = in.readLine();
                }
            } catch(ArrayIndexOutOfBoundsException e){
                out.println("Invalid number of parameteres...\n" +
                        "Try using <help>\n" +
                        "endOfResponse");
            }

            con.close();
            socket.close();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
