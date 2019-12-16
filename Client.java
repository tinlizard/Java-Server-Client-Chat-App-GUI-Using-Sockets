//Client code 

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.*;
import java.io.*;

class Client {
    //GUI variables
    private JFrame clientFrame;
    private JPanel clientPanel;
    private JTextArea clientTextArea;
    private JTextField clientTextField;
    private JScrollPane clientScrollPane;

    //Client variables
    private int port = 4447;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private BufferedReader stdIn;
    private InetAddress inetAddress;

    //main method
    public static void main(String[] args) throws IOException {
        Client clientConnect = new Client();
        clientConnect.communicateWithServer();
    }

    private Client() {
        //frame initialisation
        clientFrame = new JFrame();
        clientFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        clientFrame.setSize(400,400);
        clientFrame.setTitle("Client Messaging");
        clientFrame.setResizable(false);

        //text field initialisation
        clientTextField = new JTextField("", 240);
        clientTextField.setEditable(true);

        //action listener that checks if the enter button has been pressed, if yes display message to server, and send message
        clientTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource() == clientTextField) {
                    displayMessageToServer(e.getActionCommand());
                    sendMessageToServer(e.getActionCommand());
                    clientTextField.setText("");
                }
            }
        });

        //text area initialisation
        clientTextArea = new JTextArea();

        //scroll pane initialization
        clientScrollPane = new JScrollPane();

        //panel initialization and adding GUI variables to panel
        clientPanel = new JPanel();
        clientPanel.setLayout(new BorderLayout());
        clientPanel.add(clientTextField, BorderLayout.SOUTH);
        clientPanel.add(clientTextArea, BorderLayout.CENTER);
        clientPanel.add(clientScrollPane, BorderLayout.EAST);

        //adding panel to frame and making it visible
        clientFrame.add(clientPanel);
        clientFrame.setVisible(true);

        try {
            //initialisation of socket variables
            inetAddress = InetAddress.getLocalHost();
            clientSocket = new Socket(inetAddress, port);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            stdIn = new BufferedReader(new InputStreamReader(System.in));
            out = new PrintWriter(clientSocket.getOutputStream(), true);
        }
        catch(IOException e) {
            System.out.println(e);
        }
    }

    //main method for communicating with server
    private void communicateWithServer() {
        try {
            //connecting to server
            clientTextArea.append("Connecting to server..." + "\n");
            clientTextArea.append("Connected to server. You may now communicate. Type 'stopconnection' to stop connection");

            //String for storing user input
            String userInput;

            //while loop for continous communication
            while((userInput = in.readLine()) != null) {
                if(userInput.contains("stopconnection")){
                    closeStreams();
                    break;
                }
                displayMessageToServer(userInput + "\n");

            }
        }

        catch(IOException e){
                System.out.println(e);
            }
    }

    //method for displaying message to server
    private void displayMessageToServer(String messageToServer){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                clientTextArea.append(messageToServer);
            }
        });
    }

    //sending message to server
    private void sendMessageToServer(String serverMessage) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                out.println("\n" + "Client: " + serverMessage);
                out.flush();
            }
        });
    }

    //closing streams when communication is done
    private void closeStreams() {
        try {
            clientTextArea.append("Closing connection now." + "\n");
            sendMessageToServer("ENDING CHAT: Closing connection now");
            in.close();
            out.close();
        }
        catch(IOException e) {
            System.out.println(e);
            clientTextArea.append("Uh oh. Seems like there was a communication error....!");
        }
    }
}
