import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws UnknownHostException, IOException{
        String sendData;
        Scanner scanner = new Scanner(System.in);

        try {
            Socket socket = new Socket("localhost",9806);
            System.out.println("Connected to chat server");
            // obtaining input and out streams
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));


            Thread sendMessage = new Thread(new Runnable()
            {
                @Override
                public void run() {
                    while (true) {
                        try {
                            String request = scanner.nextLine();
                            out.println(request);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            });

            Thread readMessage = new Thread(new Runnable()
            {
                @Override
                public void run() {

                    while (true) {
                        try {
                            String recievedMessage = in.readLine();
                            System.out.println(recievedMessage);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });


            sendMessage.start();
            readMessage.start();
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
