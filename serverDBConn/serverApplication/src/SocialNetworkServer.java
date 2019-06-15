import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocialNetworkServer {
    public static final int PORT = 9000;

    public static void main(String[] args) throws IOException {
        SocialNetworkServer server = new SocialNetworkServer();
    }

    public SocialNetworkServer() throws IOException{
        ServerSocket serverSocket = null;
        try{
            serverSocket = new ServerSocket(PORT);
            while(true){
                System.out.println("Waiting for a client...");
                Socket socket = serverSocket.accept();
                new ClientThread(socket).start();
            }
        } catch(IOException e){
            e.printStackTrace();
        }
    }
}
