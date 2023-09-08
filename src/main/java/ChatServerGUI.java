import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/* Ventana del servidor del chat
usando un socket y un serversocket
 */
public class ChatServerGUI extends JFrame implements ActionListener {

    // Elementos de la ventana del servidor
    private JLabel titleLabel, serverIPLabel, clientIPLabel, clientMessageLabel;
    private JTextField textField;
    private JButton sendButton, backButton, closeButton;
    private BufferedReader input;
    private PrintWriter output;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private String input_message, output_message;
    private InetAddress ipLocal;
    private JFrame backFrame;
    private LocalTime time;

    // Constructor
    public ChatServerGUI(JFrame backFrame) {
        this.backFrame = backFrame;

        // Se crea el serversocket y se obtiene la IP local
        try {
            serverSocket = new ServerSocket(6969);
            ipLocal = InetAddress.getLocalHost();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Propios de la ventana
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Chat: Servidor");

        titleLabel = new JLabel("Bienvenido, servidor", SwingConstants.CENTER);
        titleLabel.setBounds(10, 10, 410, 30);
        add(titleLabel);

        serverIPLabel = new JLabel("IP del Servidor: " + ipLocal.getHostAddress());
        serverIPLabel.setBounds(10, 40, 400, 30);
        add(serverIPLabel);

        clientIPLabel = new JLabel("EL cliente no se ha conectado.");
        clientIPLabel.setBounds(10, 70, 400, 30);
        add(clientIPLabel);

        clientMessageLabel = new JLabel("Aquí aparecerán los mensajes del cliente.");
        clientMessageLabel.setBounds(10, 100, 400, 30);
        add(clientMessageLabel);

        textField = new JTextField();
        textField.setBounds(10, 160, 300, 20);
        textField.addActionListener(this);
        add(textField);

        sendButton = new JButton("Enviar");
        sendButton.setBounds(310, 160, 100, 20);
        sendButton.addActionListener(this);
        add(sendButton);

        backButton = new JButton("Volver");
        backButton.setBounds(10, 185, 195, 30);
        add(backButton);

        closeButton = new JButton("Cerrar");
        closeButton.setBounds(215, 185, 195, 30);
        add(closeButton);

        // Funciones de volver y cerrar
        new BackAndCloseB(this, this.backFrame, backButton, closeButton, () -> {
            try {
                serverSocket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });


        // Iniciar el hilo para recibir mensajes del cliente
        Thread receiveMessages = new Thread(() -> {
            try {
                clientSocket = serverSocket.accept(); // Espera la conexión del cliente
                clientIPLabel.setText("Cliente conectado desde: " + clientSocket.getInetAddress().toString().substring(1)); // Especifica la IP del cliente conectado

                // Input y Output
                input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                output = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()), true);

                // Bucle que asigna el mensaje al label
                while ((input_message = input.readLine()) != null) {
                    time = LocalTime.now();
                    clientMessageLabel.setText("El cliente dice: " + input_message +
                            " (a las " + time.format(DateTimeFormatter.ofPattern("h:mm:ss a")) + ")");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        receiveMessages.start(); // Iniciar el hilo
    }


    // Método del botón
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == sendButton || event.getSource() == textField) {
            // Enviar al output lo que está en el textField
            output_message = textField.getText();
            output.println(output_message);
            textField.setText("");
        }
    }

    // Main
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ChatServerGUI serverGUI = new ChatServerGUI(null);

            serverGUI.setBounds(0, 0, 430, 500);
            serverGUI.setLocationRelativeTo(null);
            serverGUI.setVisible(true);
        });
    }
}
