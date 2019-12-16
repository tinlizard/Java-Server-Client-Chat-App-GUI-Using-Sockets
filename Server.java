import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.*;
import java.io.*;
import java.awt.*;

class Server extends JFrame {
    //GUI variables
    private JFrame frame;
    private JTextField textField;
    private JTextArea windowForCommunication;
    private JPanel panel;
    private JScrollPane scrollPane;

    //Server variables
    private final int port = 4447;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private BufferedReader input;
    private BufferedReader stdIn;
    private PrintWriter output;

    //main method
    public static void main(String[] args) throws IOException {
        Server serverObj = new Server();
        serverObj.start();
    }

    private Server() {
        //Frame initialization
        frame = new JFrame();
        frame.setSize(400,400);
        frame.setTitle("Server Messaging");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        //Text field initialization
        textField = new JTextField("", 240);
        textField.setEditable(false);

        //Action listener for text field, activates whenever the user hits enter on their keyboard to submit message
        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayMessage(e.getActionCommand());
                sendMessageToClient(e.getActionCommand());
                textField.setText("");
            }
        });

        //Text area initialization
        windowForCommunication = new JTextArea();

        //Scroll pane initialization
        scrollPane = new JScrollPane();

        //Panel initialization, adding text field and text area to panel
        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(textField, BorderLayout.SOUTH);
        panel.add(windowForCommunication, BorderLayout.CENTER);
        panel.add(scrollPane, BorderLayout.EAST);

        //adding panel to frame, setting frame visibility to true
        frame.add(panel);
        frame.setVisible(true);

        //Important to have this code in a try and catch statement otherwise it will not work!
        try {
            serverSocket = new ServerSocket(port);
            clientSocket = serverSocket.accept();
            input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            stdIn = new BufferedReader(new InputStreamReader(System.in));
            output = new PrintWriter(clientSocket.getOutputStream(), true);

        }
        catch(IOException e) {
            System.out.println(e);
        }

    }

    //Main method for starting program and connection with client
    private void start() {
        windowForCommunication.append("Awaiting client connection..." + "\n");

        //check if client is connected, if yes, add the following text to the chat window, then enable communication
        if (clientSocket.isConnected()) {
            windowForCommunication.append("Connection is established. Type 'stopconnection' to stop connection" + "\n");
            textField.setEditable(true);
        }

        //Begin communication, end if "stopconnection" is typed by Server/Client. If it is, call closeStreams method.
        try {
            String userInput;

            while((userInput = input.readLine())  != null) {
                if(userInput.contains("stopconnection")){
                    closeStreams();
                    break;
                }

                displayMessage(userInput + "\n");

            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    //Method for displaying message to GUI
    private void displayMessage(String message){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                windowForCommunication.append(message);
            }
        });
    }

    //method for sending message to client
    private void sendMessageToClient(String messageToClient){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                output.println("\n" + "Server: " + messageToClient);
                output.flush();
            }
        });
    }

    //Method to close input and output streams once communication is finished
    private void closeStreams() {
        try {
            windowForCommunication.append("Closing connection now." + "\n");
            sendMessageToClient("ENDING CHAT: Closing connection now");
            input.close();
            output.close();
            serverSocket.close();
        }
        catch(IOException e) {
            System.out.println(e);
            windowForCommunication.append("Uh oh. Seems like there was a communication error....!");
        }
    }

}
