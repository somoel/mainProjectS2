import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;

public class ChatServerGUI extends JFrame implements ActionListener {

    // Elementos de la ventana del servidor
    private JLabel titleLabel, serverIPLabel, clientIPLabel, clientMessageLabel;
    private JTextField textField;
    private JButton sendButton;
    private BufferedReader input;
    private PrintWriter output;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private String input_message,output_message;
    private InetAddress ipLocal;

    // Constructor
    public ChatServerGUI() {

        try {
            serverSocket = new ServerSocket(6969);
            ipLocal = InetAddress.getLocalHost();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Chat: Servidor");

        // Contenido del label
        titleLabel = new JLabel("Bienvenido, servidor", SwingConstants.CENTER);
        titleLabel.setBounds(10, 10, 410, 30);
        add(titleLabel);

        serverIPLabel = new JLabel("IP del Servidor: " + ipLocal.getHostAddress());
        serverIPLabel.setBounds(10, 40, 400, 30);
        add(serverIPLabel);

        clientIPLabel = new JLabel("EL cliente no se ha conectado.");
        clientIPLabel.setBounds(10, 70, 400, 30);
        add(clientIPLabel);

        clientMessageLabel = new JLabel("No existe ningún mensaje del cliente.");
        clientMessageLabel.setBounds(10,100, 400, 30);
        add(clientMessageLabel);

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
                clientIPLabel.setText("Cliente conectado desde: " + clientSocket.getInetAddress());

                input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                output = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()), true);

                while ((input_message = input.readLine()) != null) {
                    clientMessageLabel.setText(input_message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        receiveMessages.start();
    }


    // Método del botón
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == sendButton) {
            output_message = textField.getText();
            output.println(output_message);
            textField.setText("");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ChatServerGUI serverGUI = new ChatServerGUI();
            serverGUI.setBounds(0, 0, 430, 500);
            serverGUI.setLocationRelativeTo(null);
            serverGUI.setVisible(true);
        });
    }
}
