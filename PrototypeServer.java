import java.io.*;
import java.net.*;
import java.util.Scanner;
import javax.swing.*;

public class PrototypeServer {

    public static void main(String[] args)  {

        try {
            int port = 3330;
            ServerSocket server = new ServerSocket(port);
            Socket clientSocket = server.accept();

            if (clientSocket.isConnected()) {
                System.out.println("Connected to server. Type anything, to stop data stream, type PROGEND");
            }

            else {
                System.out.println("Not connected, something went wrong.");
            }

            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

            String inputLine = in.readLine();
            String outputLine;
            String ok = "ok";

            while(true) {
                System.out.println("Client: " + inputLine);
                out.println(inputLine);
                out.println(ok);

                if (inputLine.contains("PROGEND")) {
                    break;
                }

            }



        }

        catch(IOException i) {
            System.out.println(i);
        }


    }
}
