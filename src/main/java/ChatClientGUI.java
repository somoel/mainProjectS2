import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;

public class ChatClientGUI extends JFrame implements ActionListener {

    // Elementos de la ventana del cliente.
    private JLabel titleLabel, ipLabel, serverMessageLabel;
    private JTextField textField;
    private JButton sendButton;
    private BufferedReader input;
    private PrintWriter output;
    private Socket socket;
    private String input_message, output_message, server_IP;

    // Constructor
    public ChatClientGUI() {

        server_IP = JOptionPane.showInputDialog("Ingrese la IP"); // Se pide la IP del servidor

        // Creación del socket con la IP del server y los IOs
        try {
            socket = new Socket(server_IP, 6969);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al conectar al servidor: " + e.getMessage());
        }

        // Propios de la ventana
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Chat: Cliente");

        titleLabel = new JLabel("Bienvenido, cliente", SwingConstants.CENTER);
        titleLabel.setBounds(10, 10, 400, 30);
        add(titleLabel);

        ipLabel = new JLabel("Conectado al servidor " + server_IP);
        ipLabel.setBounds(10, 40, 400, 30);
        add(ipLabel);

        serverMessageLabel = new JLabel("Ningún mensaje por ahora.");
        serverMessageLabel.setBounds(10, 70, 400, 30);
        add(serverMessageLabel);

        textField = new JTextField();
        textField.setBounds(10, 120, 300, 20);
        add(textField);

        sendButton = new JButton("Enviar");
        sendButton.setBounds(310, 120, 100, 20);
        sendButton.addActionListener(this);
        add(sendButton);

        // Iniciar el hilo para recibir mensajes del servidor
        Thread receiveMessages = new Thread(() -> {
            try {
                // Bucle que asigna el mensaje al label
                while ((input_message = input.readLine()) != null) {
                    serverMessageLabel.setText(input_message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        receiveMessages.start(); // Iniciar el hilo
    }

    // Método del botón
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == sendButton) {
            // Enviar al output lo que está en el textField
            output_message = textField.getText();
            output.println(output_message);
            textField.setText("");
        }
    }

    // Main
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ChatClientGUI clientGUI = new ChatClientGUI();
            clientGUI.setBounds(0, 0, 430, 500);
            clientGUI.setLocationRelativeTo(null);
            clientGUI.setVisible(true);
        });
    }
}
