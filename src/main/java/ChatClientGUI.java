import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.io.*;
import java.net.*;

// Clase del Chat: Cliente.
public class ChatClientGUI extends JFrame implements ActionListener {

    // elementos de la ventana del cliente
    private JLabel titleLabel, ipServerLabel, serverMessageLabel;
    private JTextField textField;
    private JButton sendButton;
    private String output_message;
    private PrintWriter output;

    // Constructor con un InputDialog dentro
    public ChatClientGUI() {
        Socket socket;
        BufferedReader input;
        String host = "";
        int port = 6969;

        host = JOptionPane.showInputDialog("Ingrese la IP del Servidor");


        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Chat: Cliente");

        //contenido del label
        titleLabel = new JLabel("Bienvenido, cliente", SwingConstants.CENTER);
        titleLabel.setBounds(10, 10, 400, 30);
        add(titleLabel);

        ipServerLabel = new JLabel("Está conectado al servidor " + host);
        ipServerLabel.setBounds(10, 40, 400, 30);
        add(ipServerLabel);

        serverMessageLabel = new JLabel("Ningún mensaje por ahora.");
        serverMessageLabel.setBounds(10, 90, 400, 30);
        add(serverMessageLabel);

        textField = new JTextField();
        textField.setBounds(10, 120, 300, 20);
        add(textField);

        sendButton = new JButton("Enviar");
        sendButton.setBounds(310, 120, 100, 20);
        sendButton.addActionListener(this);
        add(sendButton);


        try {
            while (true) {
                socket = new Socket(host, port);
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
                String input_message;

                while ((input_message = input.readLine()) != null) {
                    serverMessageLabel.setText("El servidor dice: " + input_message);
                }

                ipServerLabel.setText("Servidor desconectado.");
                socket.close();
            }



        } catch (IOException e){
            e.printStackTrace();
        }






    }

    // Método del Botón
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == sendButton){
            output_message = textField.getText();
            output.println(output_message);
            textField.setText("");
        }
    }

}
