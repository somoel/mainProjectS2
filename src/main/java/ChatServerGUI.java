import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;


// Clase del Chat: server
public class ChatServerGUI extends JFrame implements ActionListener {

    // elementos de la ventana del cliente
    private JLabel titleLabel, ipLabel, ipClientLabel, clientMessageLabel;
    private JTextField textField;
    private JButton sendButton;
    private String output_message;
    private PrintWriter output;


    // Constructor con un InputDialog dentro
    public ChatServerGUI() {

        setLayout(null);
//importacion del socket
        ServerSocket serverSocket;
        Socket socket;
        int port = 6969;
        BufferedReader input;

        String ip_local = "¡Error!";

        try {
            InetAddress ip_local_INET = InetAddress.getLocalHost();
            ip_local = ip_local_INET.getHostAddress();

        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Chat: Servidor");

        //contenido del label
        titleLabel = new JLabel("Bienvenido, servidor ", SwingConstants.CENTER);
        titleLabel.setBounds(10, 10, 410, 30);
        add(titleLabel);

        ipLabel = new JLabel("IP del Servidor: " + ip_local);
        ipLabel.setBounds(10, 40, 400, 30);
        add(ipLabel);

        ipClientLabel = new JLabel("Cliente sin conectar");
        ipClientLabel.setBounds(10, 70, 400, 30);
        add(ipClientLabel);

        clientMessageLabel = new JLabel("Ningún mensaje por ahora");
        clientMessageLabel.setBounds(10, 100, 400, 30);
        add(clientMessageLabel);

        textField = new JTextField();
        textField.setBounds(10, 160, 300, 20);
        add(textField);

        sendButton = new JButton("Enviar");
        sendButton.setBounds(10, 160, 100, 20);
        sendButton.addActionListener(this);
        add(sendButton);


        try {
            serverSocket = new ServerSocket(port);

            while (true){
                socket = serverSocket.accept();
                ipClientLabel.setText("Cliente conectado desde: " + socket.getInetAddress());
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
                String input_message;


                while ((input_message = input.readLine()) != null) {
                    clientMessageLabel.setText("El cliente dice: " + input_message);
                }

                ipClientLabel.setText("Cliente desconectado. Esperando un nuevo cliente");
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


