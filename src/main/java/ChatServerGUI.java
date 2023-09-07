import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class ChatServerGUI extends JFrame implements ActionListener {

    // Elementos de la ventana del servidor
    private JLabel titleLabel, ipLabel, clientMessageLabel, messageFromClientLabel;
    private JTextField textField;
    private JButton sendButton;
    // private String  output_message;
    private PrintWriter output;

    private ServerSocket serverSocket;
    private Socket clientSocket; // Variable para almacenar la conexión del cliente
    private BufferedReader input;

    // Constructor
    public ChatServerGUI() {
        setLayout(null);

        int port = 6969;

        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Chat: Servidor");

        // Contenido del label
        titleLabel = new JLabel("Bienvenido, servidor", SwingConstants.CENTER);
        titleLabel.setBounds(10, 10, 410, 30);
        add(titleLabel);

        ipLabel = new JLabel("IP del Servidor: " + getLocalIP());
        ipLabel.setBounds(10, 40, 400, 30);
        add(ipLabel);

        clientMessageLabel = new JLabel("EL cliente no se ha conectado.");
        clientMessageLabel.setBounds(10, 70, 400, 30);
        add(clientMessageLabel);

        messageFromClientLabel = new JLabel("No existe ningún mensaje del cliente.");
        messageFromClientLabel.setBounds(10,100, 400, 30);
        add(messageFromClientLabel);

        textField = new JTextField();
        textField.setBounds(10, 160, 300, 20);
        add(textField);

        sendButton = new JButton("Enviar");
        sendButton.setBounds(310, 160, 100, 20);
        sendButton.addActionListener(this);
        add(sendButton);

        // Iniciar el hilo para escuchar al cliente
        Thread receiveMessages = new Thread(() -> {
            try {
                clientSocket = serverSocket.accept();
                clientMessageLabel.setText("Cliente conectado desde: " + clientSocket.getInetAddress());
                input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                output = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                String input_message;
                while ((input_message = input.readLine()) != null) {
                    messageFromClientLabel.setText(input_message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        receiveMessages.start();
    }

    // Método para obtener la IP local del servidor
    private String getLocalIP() {
        try {
            InetAddress ipLocal = InetAddress.getLocalHost();
            return ipLocal.getHostAddress();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    // Método para mostrar los mensajes del cliente en la interfaz
    /*
    private void showClientMessages(String message) {
        SwingUtilities.invokeLater(() -> messageFromClientLabel.setText("El cliente dice: " + message));
    }

     */

    // Método para enviar mensajes al cliente
    /*
    private void sendMessagesToClient(String message) {
        output.println(message);
    }


     */
    // Método del botón
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == sendButton) {
            String output_message1 = textField.getText();
            // sendMessagesToClient(output_message); // Enviar mensaje al cliente
            output.println(output_message1);
            output.println("este sí envió");
            System.out.println(output_message1);
            System.out.println("este sí envió");
            textField.setText("");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ChatServerGUI serverGUI = new ChatServerGUI();
            serverGUI.setBounds(0, 0, 480, 240);
            serverGUI.setVisible(true);
        });
    }
}
