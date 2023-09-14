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
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/* Ventana del servidor del chat
usando un socket y un serversocket
 */
public class ChatServerGUI extends JFrame implements ActionListener {

    // Elementos de la ventana del servidor
    private JLabel titleLabel, serverIPLabel, clientIPLabel;
    private JEditorPane clientMessageLabel;
    private JTextField textField;
    private JButton sendButton, backButton, closeButton;
    private JSeparator separatorTitle;
    private JScrollPane scrollMessage;
    private JScrollBar verticalScrollMessage;
    private BufferedReader input;
    private PrintWriter output;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private String input_message, last_message = "", show_time, fget_message, fsend_message;
    private InetAddress ipLocal;
    private JFrame backFrame;
    private LocalTime time;

    // Constructor
    public ChatServerGUI(JFrame backFrame) {
        this.backFrame = backFrame;

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
        clientMessageLabel.setBounds(10, 200, 400, 150);
        add(clientMessageLabel);

        scrollMessage = new JScrollPane(clientMessageLabel);
        scrollMessage.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        ChatClientGUI.CustomScrollBarUI customScrollBarUI = new ChatClientGUI.CustomScrollBarUI();
        scrollMessage.getVerticalScrollBar().setUI(customScrollBarUI);

        verticalScrollMessage = scrollMessage.getVerticalScrollBar();

        scrollMessage.setBounds(10, 150, 400, 210);
        add(scrollMessage);


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
                clientSocket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (NullPointerException ignored) {
            }
        });

        new Styles(this, titleLabel, textField, separatorTitle); // Agrega colores

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

                    fget_message =
                            last_message +
                                    "<div style='font-family:\"Product Sans\",Roboto, Helvetica; font-size: 15px'>" +
                                    "<p style='color: #a4a6ad; font-size: 13px;'><i>" +
                                    "Cliente" +
                                    "</i></p>" +
                                    input_message +
                                    "<span style='color: #a4a6ad; font-size: 10px; padding-left: 10px;'><i><br>" +
                                    " a las " + getActualTime() +
                                    "</i></span>" +
                                    "</div>" +
                                    "<div style='color: #fffff2; font-size: 4px'>" +
                                    new String(new char[167]).replace("\0", "-") +
                                    "</div>";

                    clientMessageLabel.setText(HTMLString(fget_message));
                    SwingUtilities.invokeLater(() ->
                            verticalScrollMessage.setValue(verticalScrollMessage.getMaximum()));
                    last_message = fget_message;
                }
            } catch (IOException ignored) {
            }
        });
        receiveMessages.start(); // Iniciar el hilo
    }


    // Método del botón
    public void actionPerformed(ActionEvent event) {
        if ((event.getSource() == sendButton || event.getSource() == textField) && (!textField.getText().isEmpty())) {
            // Enviar al output lo que está en el textField

            try {
                output.println(textField.getText());

                fsend_message =
                        last_message +
                                "<div style='font-family:\"Product Sans\",Roboto, Helvetica;" +
                                " font-size: 15px; text-align: right;'>" +
                                "<p style='color: #a4a6ad; font-size: 13px;'><i>" +
                                "Servidor (tú)" +
                                "</i></p>" +
                                textField.getText() +
                                "<span style='color: #a4a6ad; font-size: 10px; padding-left: 10px;'><i><br>" +
                                " a las " + getActualTime() +
                                "</i></span>" +
                                "</div>" +
                                "<div style='color: #fffff2; font-size: 4px'>" +
                                new String(new char[167]).replace("\0", "-") +
                                "</div>";

                clientMessageLabel.setText(HTMLString(fsend_message));

                verticalScrollMessage.setValue(verticalScrollMessage.getMaximum());
                last_message = fsend_message;


            } catch (NullPointerException e) {
                JOptionPane.showMessageDialog(this, "No se pudo enviar el mensaje.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
            textField.setText("");

        }
    }

    public String HTMLString(String string) {
        return "<html>" + string + "</html>";
    }

    public String getActualTime() {
        time = LocalTime.now(); // Captura el tiempo actual
        show_time = time.format(DateTimeFormatter.ofPattern("h:mm:ss a"));
        return show_time;
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
