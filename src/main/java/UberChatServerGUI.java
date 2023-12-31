import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/* Ventana del servidor del chat
usando un socket y un serversocket
 */
public class UberChatServerGUI extends JDialog implements ActionListener {

    // Elementos de la ventana del servidor
    private JLabel titleLabel, serverIPLabel, clientIPLabel;
    private JEditorPane clientMessageLabel;
    private JTextField textField;
    private JButton sendButton, backButton, closeButton;
    private JSeparator separatorTitle;
    private JScrollPane scrollMessage;
    private JScrollBar verticalScrollMessage;

    // Elementos del Socket
    private BufferedReader input;
    private PrintWriter output;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private String input_message;
    private String last_message = "";
    private String fget_message;
    private String fsend_message;
    private JFrame backFrame; // Obtener el backframe
    // Tiempo para cada mensaje
    private InetAddress ipLocal; // IP local para mostrar

    // Constructor
    public UberChatServerGUI(JFrame backFrame) {
        super(backFrame,"Chat", true);

        // Se crea el serversocket
        try {
            serverSocket = new ServerSocket(6969);
        } catch (IOException e) {
            if (e.getMessage().equals("Address already in use: bind")) {
                JOptionPane.showMessageDialog(null,
                        "Actualmente hay un servidor abierto en este dispositivo",
                        "Error", JOptionPane.ERROR_MESSAGE);
                setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            } else {
                e.printStackTrace();
            }
        }

        // Se obtiene la IP local
        try {
            ipLocal = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

        titleLabel = new JLabel("Chat: Servidor");
        titleLabel.setBounds(10, 10, 400, 40);
        add(titleLabel);

        separatorTitle = new JSeparator();
        add(separatorTitle);

        serverIPLabel = new JLabel("<html>IP del Servidor: <b>" + ipLocal.getHostAddress() + "</b></html>");
        serverIPLabel.setBounds(10, 80, 400, 30);
        add(serverIPLabel);

        clientIPLabel = new JLabel("<html>Esperando conexión...</html>");
        clientIPLabel.setVerticalAlignment(clientIPLabel.TOP);
        clientIPLabel.setBounds(10, 110, 400, 50);
        add(clientIPLabel);

        clientMessageLabel = new JEditorPane();
        clientMessageLabel.setContentType("text/html");
        clientMessageLabel.setBackground(Styles.boneWhite);
        clientMessageLabel.setText("<html><div style='font-size: 15px;" +
                " font-family: \"Product Sans\", Roboto; text-align: center;'>" +
                "<br><br><br><br><br><br>Ningún mensaje por ahora</div></html>");
        clientMessageLabel.setEditable(false);
        clientMessageLabel.setBounds(10, 200, 400, 510);
        add(clientMessageLabel);

        // Scrollbar
        scrollMessage = new JScrollPane(clientMessageLabel);
        scrollMessage.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        ChatUtilities.CustomScrollBarUI customScrollBarUI = new ChatUtilities.CustomScrollBarUI();
        scrollMessage.getVerticalScrollBar().setUI(customScrollBarUI);

        verticalScrollMessage = scrollMessage.getVerticalScrollBar();

        scrollMessage.setBounds(10, 150, 400, 510);
        add(scrollMessage);


        textField = new JTextField();
        textField.setBounds(10, 660, 300, 40);
        textField.addActionListener(this);
        textField.setEnabled(false);
        add(textField);

        sendButton = new JButton("Enviar");
        sendButton.setBounds(310, 660, 100, 40);
        sendButton.addActionListener(this);
        sendButton.setEnabled(false);
        add(sendButton);

        backButton = new JButton("Volver");
        backButton.setBounds(10, 715, 195, 45);
        add(backButton);

        closeButton = new JButton("Cerrar");
        closeButton.setBounds(215, 715, 195, 45);
        add(closeButton);

        new Styles(this, titleLabel, new JTextField[]{textField}, separatorTitle); // Agrega colores

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
                // Especifica la IP del cliente conectado
                clientIPLabel.setText("Cliente conectado desde: " +
                        clientSocket.getInetAddress().toString().substring(1));

                textField.setEnabled(true);
                sendButton.setEnabled(true);

                // Input y Output
                input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                output = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()), true);

                // Bucle que asigna el mensaje al label
                while ((input_message = input.readLine()) != null) {

                    // Darle formato HTML al mensaje recibido
                    fget_message = ChatUtilities.formatMessage(last_message, input_message,
                            "Cliente", false);

                    clientMessageLabel.setText(fget_message); // Asigna el texto
                    SwingUtilities.invokeLater(() ->
                            verticalScrollMessage.setValue(verticalScrollMessage.getMaximum())); // Bajar el scrollbar
                    last_message = fget_message; // Obtener el mensaje recibido y sumarlo a la cola de mensajes.
                }
            } catch (IOException ignored) {
            }
        });
        receiveMessages.start(); // Iniciar el hilo

        // Propios de la ventana
        setLayout(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle("Chat: Servidor");
        setSize(430,800);
        setLocationRelativeTo(null);
        setVisible(true);
    }


    // Método del botón
    public void actionPerformed(ActionEvent event) {
        if ((event.getSource() == sendButton || event.getSource() == textField) && (!textField.getText().isEmpty())) {
            // Enviar al output lo que está en el textField

            try {
                output.println(textField.getText());

                // Mostrar un eco del mensaje enviado sumado al mensaje anterior
                fsend_message = ChatUtilities.formatMessage(last_message, textField.getText(),
                        "Servidor (tú)", true);

                clientMessageLabel.setText(fsend_message); // Asigna el texto

                verticalScrollMessage.setValue(verticalScrollMessage.getMaximum()); // Bajar el scrollbar
                last_message = fsend_message; // Asignar al historial


            } catch (NullPointerException e) {
                JOptionPane.showMessageDialog(this, "No se pudo enviar el mensaje.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
            textField.setText("");

        }
    }

    // Main
    public static void main(String[] args) {
        new UberChatServerGUI(null);
    }
}

