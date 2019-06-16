import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class ClientThread extends Thread{
    private Socket socket = null;

    public ClientThread(Socket socket){
        this.socket = socket;
    }

    public void run(){
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            ClientRequestManager clientRequestManager = ClientRequestManager.getInstance();
            String request = in.readLine();

            while (!request.equals("exit")) {
                List<String> response = clientRequestManager.requestParser(request);
                out.print(response);
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
