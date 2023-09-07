import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;

public class ChatClientGUI extends JFrame implements ActionListener {

    // Elementos de la ventana del cliente
    private JLabel titleLabel, ipLabel, serverMessageLabel;
    private JTextField textField;
    private JButton sendButton;
    private String output_message;
    private PrintWriter output;
    private Socket socket;
    private BufferedReader input;

    // Constructor
    public ChatClientGUI() {
        setLayout(null);

        int port = 6969;
        String serverIP = JOptionPane.showInputDialog("Ingrese la IP"); // IP del servidor predefinida

        try {
            socket = new Socket(serverIP, port);
            output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al conectar al servidor: " + e.getMessage());
        }

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Chat: Cliente");

        // Contenido del label
        titleLabel = new JLabel("Bienvenido, cliente", SwingConstants.CENTER);
        titleLabel.setBounds(10, 10, 400, 30);
        add(titleLabel);

        ipLabel = new JLabel("Conectado al servidor " + serverIP);
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
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String server_message;
                while ((server_message = input.readLine()) != null) {
                    final String finalServerMessage = server_message; // Capturar el mensaje en una variable final
                    SwingUtilities.invokeLater(() -> showServerMessages(finalServerMessage));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        receiveMessages.start();
    }

    // Método para mostrar los mensajes del servidor en la interfaz
    private void showServerMessages(String message) {
        serverMessageLabel.setText("Servidor dice: " + message);
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
            ChatClientGUI clientGUI = new ChatClientGUI();
            clientGUI.setBounds(0, 0, 480, 240);
            clientGUI.setVisible(true);
        });
    }
}
