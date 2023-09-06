import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;

public class ChatClientGUI extends JFrame implements ActionListener {

    // Elementos de la ventana del cliente
    private JLabel titleLabel, ipServerLabel, serverMessageLabel;
    private JTextField textField;
    private JButton sendButton;
    private String output_message;
    private PrintWriter output;
    private Socket socket;

    // Constructor
    public ChatClientGUI() {
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Chat: Cliente");

        titleLabel = new JLabel("Bienvenido, cliente", SwingConstants.CENTER);
        titleLabel.setBounds(10, 10, 400, 30);
        add(titleLabel);

        ipServerLabel = new JLabel("No está conectado al servidor.");
        ipServerLabel.setBounds(10, 40, 400, 30);
        add(ipServerLabel);

        serverMessageLabel = new JLabel("Ningún mensaje por ahora.");
        serverMessageLabel.setBounds(10, 90, 400, 30);
        add(serverMessageLabel);

        textField = new JTextField();
        textField.setBounds(10, 120, 300, 20);
        add(textField);

        sendButton = new JButton("Conectar y Enviar");
        sendButton.setBounds(310, 120, 150, 20);
        sendButton.addActionListener(this);
        add(sendButton);
    }

    // Método para establecer la conexión con el servidor
    private void conectarAServidor(String host, int port) {
        try {
            socket = new Socket(host, port);
            output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            ipServerLabel.setText("Conectado al servidor " + host);
            serverMessageLabel.setText("El servidor dice: Conectado al servidor.");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al conectar al servidor: " + e.getMessage());
        }
    }

    // Método del botón
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == sendButton) {
            String host = JOptionPane.showInputDialog("Ingrese la IP del Servidor");
            int port = 6969;
            conectarAServidor(host, port);

            if (socket != null && output != null) {
                output_message = textField.getText();
                output.println(output_message);
                textField.setText("");
            }
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
