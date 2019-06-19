import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SocialNetworkClient {
    public static final int PORT = 9000;
    public static final String address = "127.0.0.1";

    public static void main(String[] args) throws IOException {
        SocialNetworkClient client = new SocialNetworkClient();
        client.start();
    }

    public void start() throws IOException{
        try(
                Socket socket = new Socket(address, PORT);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
        ) {
            Scanner scanner = new Scanner(System.in);
            String request = scanner.nextLine();

            while(!request.equals("exit")){
                out.println(request);

                try {
                    Object object = in.readObject();
                    List<String> response = (ArrayList<String>) object;
                    for(String respond : response){
                        System.out.println(respond);
                    }
                } catch(Exception e){
                    e.printStackTrace();
                    System.out.println("Something went wrong");
                }
                request = scanner.nextLine();
            }

            out.println("exit");
        } catch(IOException e){
            e.printStackTrace();
        }
    }
}
