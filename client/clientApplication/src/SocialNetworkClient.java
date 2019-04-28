import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
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
                BufferedReader in = new BufferedReader((new InputStreamReader(socket.getInputStream())))
        ) {
            Scanner scanner = new Scanner(System.in);
            String request = scanner.nextLine();

            while(!request.equals("exit")){
                out.println(request);
                String response = in.readLine();


                System.out.println(response);

                request = scanner.nextLine();
            }
        } catch(IOException e){
            e.printStackTrace();
        }
    }
}
