import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
    private JSeparator separatorTitle;
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

        titleLabel = new JLabel("Chat: Servidor");
        titleLabel.setBounds(10, 10, 400, 40);
        add(titleLabel);

        separatorTitle = new JSeparator();
        add(separatorTitle);

        serverIPLabel = new JLabel("<html>IP del Servidor: <b>" + ipLocal.getHostAddress() + "</b></html>");
        serverIPLabel.setBounds(10, 80, 400, 30);
        add(serverIPLabel);

        clientIPLabel = new JLabel("<html>El cliente no se ha conectado.<br>Esperando conexión...</html>");
        clientIPLabel.setVerticalAlignment(clientMessageLabel.TOP);
        clientIPLabel.setBounds(10, 110, 400, 50);
        add(clientIPLabel);

        clientMessageLabel = new JLabel("Aquí aparecerán los mensajes del cliente.");
        clientMessageLabel.setVerticalAlignment(clientMessageLabel.BOTTOM);
        clientMessageLabel.setBounds(10, 200, 400, 150);
        add(clientMessageLabel);

        textField = new JTextField();
        textField.setBounds(10, 360, 300, 40);
        textField.addActionListener(this);
        textField.setEnabled(false);
        add(textField);

        sendButton = new JButton("Enviar");
        sendButton.setBounds(310, 360, 100, 40);
        sendButton.addActionListener(this);
        sendButton.setEnabled(false);
        add(sendButton);

        backButton = new JButton("Volver");
        backButton.setBounds(10, 415, 195, 45);
        add(backButton);

        closeButton = new JButton("Cerrar");
        closeButton.setBounds(215, 415, 195, 45);
        add(closeButton);

        // Funciones de volver y cerrar
        new BackAndCloseB(this, this.backFrame, backButton, closeButton, () -> {
            try {
                serverSocket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        new Styles(this, titleLabel,textField, separatorTitle); // Agrega colores

        separatorTitle.setBorder(BorderFactory.createLineBorder(Styles.darkBlue, 3));

        sendButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                sendButton.setFont(Styles.mainFont);
            }
        });



        // Iniciar el hilo para recibir mensajes del cliente
        Thread receiveMessages = new Thread(() -> {
            try {
                clientSocket = serverSocket.accept(); // Espera la conexión del cliente
                clientIPLabel.setText("Cliente conectado desde: " + clientSocket.getInetAddress().toString().substring(1)); // Especifica la IP del cliente conectado

                textField.setEnabled(true);
                sendButton.setEnabled(true);

                // Input y Output
                input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                output = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()), true);

                // Bucle que asigna el mensaje al label
                while ((input_message = input.readLine()) != null) {
                    time = LocalTime.now();
                    clientMessageLabel.setText("<html>El cliente dice: " + input_message +
                            " (a las " + time.format(DateTimeFormatter.ofPattern("h:mm:ss a")) + ")</html>");
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
            // TODO: Evitar que se envíen mensajes vacíos.

            // Revisar si se envía el mensaje
            try {
                output.println(output_message);
            } catch (NullPointerException e) {
                JOptionPane.showMessageDialog(this, "No se pudo enviar el mensaje",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
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
