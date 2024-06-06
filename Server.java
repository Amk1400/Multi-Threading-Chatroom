import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
//Help from: https://www.geeksforgeeks.org/multi-threaded-chat-application-set-1/
public class Server {
    static int nextUserIndex = 1;
    static final int port = 9806;
    static ArrayList<User> activeUsers = new ArrayList<>();
    
    public static void main(String[] args)
    {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Chat Server is running on port " + port);

            while (true){
                Socket socket = serverSocket.accept();
                System.out.println("user"  + nextUserIndex +" added");
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                User user = new User(socket,"user " + nextUserIndex, in, out);
                activeUsers.add(user);

                Thread thread = new Thread(user);
                thread.start();
                
                nextUserIndex++;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class User implements Runnable
{
    String name;
    BufferedReader in;
    PrintWriter out;
    Socket socket;
    Scanner scanner = new Scanner(System.in);
    
    public User(Socket socket, String name, BufferedReader in, PrintWriter out) {
        this.in = in;
        this.out = out;
        this.name = name;
        this.socket = socket;
    }

    @Override
    public void run() {
        String receivedMessage;
        while (true)
        {
            try
            {
                receivedMessage = in.readLine();
                for (User user : Server.activeUsers)
                {
                    user.out.println(this.name+" : " + receivedMessage);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        //closeBuffers();
    }

    private void closeBuffers() {
        try
        {
            this.in.close();
            this.out.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}