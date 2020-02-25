import java.io.*;
import java.net.*;
import javax.swing.*;
import java.net.InetAddress;
import java.util.Scanner;

public class PrototypeClient {

    public static void main(String[] args) {
        try {
            String sentence;
            int port = 3330;
            InetAddress inet = InetAddress.getLocalHost();
            Socket socket = new Socket(inet, port);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            String userInput;
            String outputFromServer;

            String hello = "hello";

            while((userInput = stdIn.readLine()) != null) {
                outputFromServer = stdIn.readLine();
                out.println(outputFromServer);
                out.println(hello);

                String servResponse = in.readLine();
                System.out.println("Server: " + servResponse);
                if (outputFromServer.contains("PROGEND")) {
                    break;
                }


            }


        }

        catch (IOException e) {
            System.out.println(e);
        }



    }

}
