import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;

public class ClientThread extends Thread{
    private Socket socket = null;

    public ClientThread(Socket socket){
        this.socket = socket;
    }

    public void run(){
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ClientRequestManager clientRequestManager = ClientRequestManager.getInstance();

            String request = in.readLine();
            System.out.println(request);

            while (!request.equals("exit")) {
                List<String> response = clientRequestManager.requestParser(request);
                out.writeObject(response);
                out.flush();
            }

            this.socket.close();

        } catch(SocketException se) {
            System.out.println("User closed the client!");
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
