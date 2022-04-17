import java.io.*;
import java.net.*;
import java.util.*;
// Comment to push
public class PhoneServer {
    public static Hashtable<String, String> phoneBook = new Hashtable<String, String>();
    // The port number on which the server will be listening
    private static int port = 2014;
    // The server socket.
    private static ServerSocket listener = null;
    // The client socket.
    private static Socket clientSocket = null;

    public static void main(String[] args) throws Exception {
        
        try {
            listener = new ServerSocket(port);
            listener.setReuseAddress(true);
            while (true) {
                clientSocket = listener.accept();
                System.out.println("Client Connected " + clientSocket.getInetAddress().getHostAddress());
                ClientThread clientSock = new ClientThread(clientSocket);
                new Thread(clientSock).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (listener != null) {
                try {
                    listener.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * Open a server socket on the specified port number(2014)
         * and monitor the port for connection requests. When a
         * connection request is received, create a client request
         * thread, passing to its constructor a reference to the
         * Socket object that represents the established connection
         * with the client.
         */
    }
}

class ClientThread extends Thread {
    Socket socket;

    // constructor
    public ClientThread(Socket socket) {
        this.socket = socket;
    }

    // implement the run method
    public void run() {
        handleConnection(socket);
    }
    // implement the handleConnection method here.

    public void handleConnection(Socket socket2) {
        PrintWriter out = null;
        BufferedReader in = null;
        try {
            out = new PrintWriter(socket2.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket2.getInputStream()));
            String line;
            while((line = in.readLine()) != null){

                System.out.printf("Sent from the client : %s \n", line);
                //out.println(ServerHandle(line));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            try{
                if(out != null){
                    out.close();
                }
                if(in != null){
                    in.close();
                    socket2.close();
                }
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
    }
    public String ServerHandle(String line){
        String[] inputs = line.split(" ");
        System.out.println(inputs);
        if(inputs[0].equals("STORE")){
            PhoneServer.phoneBook.put(inputs[1], inputs[2]);
            return("100 " + "OK");
        }
        else if(inputs[0].equals("GET")){
            String val = PhoneServer.phoneBook.get(inputs[1]);
            if(val != null){
                return("200 " + val);
            }
            else{
                return("300 " + "Not Found");    
            }
        }
        else if(inputs[0].equals("REMOVE") ){
            String v = PhoneServer.phoneBook.get(inputs[1]);
            if(v != null){
                PhoneServer.phoneBook.remove(inputs[1]);
                return("100 " + "OK");
            }
            else{
                return("300 " + "Not Found");
            }
        }
        else{
            return("400 " + "Bad Request");
        }
    }
}
