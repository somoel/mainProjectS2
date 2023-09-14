import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/* Ventana del cliente del chat
usando un socket
 */
public class ChatClientGUI extends JFrame implements ActionListener {

    // Elementos de la ventana del cliente.
    private JLabel titleLabel, serverIPLabel, clientIPLabel;
    private JEditorPane serverMessageLabel;
    private JTextField textField;
    private JButton sendButton, backButton, closeButton;
    private JSeparator separatorTitle;
    private JScrollPane scrollMessage;
    private JScrollBar verticalScrollMessage;
    private BufferedReader input;
    private PrintWriter output;
    private Socket socket;
    private String input_message, server_IP, ip_local = "Error",
            last_message = "", show_time, fget_message, fsend_message;
    private JFrame backFrame;
    private LocalTime time;
    private InetAddress ipLocal;

    // Constructor
    public ChatClientGUI(JFrame backFrame) {
        this.backFrame = backFrame;


        server_IP = JOptionPane.showInputDialog(null, "Ingrese la IP del servidor"
                , "Servidor", JOptionPane.INFORMATION_MESSAGE); // Se pide la IP del servidor


        // Creación del socket con la IP del server y los IOs
        try {

            /* Obtener la IP del cliente para mostrarla en pantalla y
            asegurarse de que el servidor esté conectado realmente al cliente.
             */
            ipLocal = InetAddress.getLocalHost();

            ip_local = ipLocal.getHostAddress();

            socket = new Socket(server_IP, 6969);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

            if (server_IP.isEmpty()) server_IP = "localhost";

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al conectar al servidor: " + e.getMessage());
            server_IP = "null";
        }

        // Propios de la ventana
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Chat: Cliente");

        titleLabel = new JLabel("Chat: Cliente");
        titleLabel.setBounds(10, 10, 400, 40);
        add(titleLabel);

        separatorTitle = new JSeparator();
        add(separatorTitle);

        serverIPLabel = new JLabel("Conectado al servidor: " + server_IP);
        serverIPLabel.setBounds(10, 80, 400, 30);
        add(serverIPLabel);

        clientIPLabel = new JLabel("IP del cliente: " + ip_local);
        clientIPLabel.setBounds(10, 110, 400, 30);
        add(clientIPLabel);


        serverMessageLabel = new JEditorPane();
        serverMessageLabel.setContentType("text/html");
        serverMessageLabel.setBackground(Styles.boneWhite);
        serverMessageLabel.setText("<html><div style='font-size: 15px; font-family: \"Product Sans\", Roboto; text-align: center;'><br><br><br><br><br><br>Ningún mensaje por ahora</div></html>");
        serverMessageLabel.setEditable(false);
        serverMessageLabel.setBounds(10, 150, 400, 210);
        add(serverMessageLabel);


        scrollMessage = new JScrollPane(serverMessageLabel);
        scrollMessage.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        CustomScrollBarUI customScrollBarUI = new CustomScrollBarUI();
        scrollMessage.getVerticalScrollBar().setUI(customScrollBarUI);

        verticalScrollMessage = scrollMessage.getVerticalScrollBar();



        scrollMessage.setBounds(10, 150, 400, 210);
        add(scrollMessage);


        textField = new JTextField();
        textField.setBounds(10, 360, 300, 40);
        textField.addActionListener(this);
        add(textField);

        sendButton = new JButton("Enviar");
        sendButton.setBounds(310, 360, 100, 40);
        sendButton.addActionListener(this);
        add(sendButton);

        backButton = new JButton("Volver");
        backButton.setBounds(10, 415, 195, 45);
        add(backButton);

        closeButton = new JButton("Cerrar");
        closeButton.setBounds(215, 415, 195, 45);
        add(closeButton);


        // Funciones de volver y cerrar
        new BackAndCloseB(this, this.backFrame, backButton, closeButton, null);

        new Styles(this, titleLabel, textField, separatorTitle); // Agrega colores

        separatorTitle.setBorder(BorderFactory.createLineBorder(Styles.pastelGreen, 3));

        sendButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                sendButton.setFont(Styles.mainFont);
            }
        });

        // Iniciar el hilo para recibir mensajes del servidor
        Thread receiveMessages = new Thread(() -> {
            try {

                // Bucle que asigna el mensaje al label
                while ((input_message = input.readLine()) != null) {


                    fget_message =
                            last_message +
                                    "<div style='font-family:\"Product Sans\",Roboto, Helvetica; font-size: 15px'>" +
                                        "<p style='color: #a4a6ad; font-size: 13px;'><i>" +
                                            "Servidor" +
                                        "</i></p>" +
                                        input_message +
                                        "<span style='color: #a4a6ad; font-size: 10px; padding-left: 10px;'><i><br> a las " + getActualTime() +
                                        "</i></span>" +
                                    "</div>" +
                                    "<div style='color: #fffff2; font-size: 4px'>" +
                                        new String(new char[167]).replace("\0", "-") +
                                    "</div>";


                    serverMessageLabel.setText(HTMLString(fget_message));
                    System.out.println("asig");
                    SwingUtilities.invokeLater(() -> verticalScrollMessage.setValue(verticalScrollMessage.getMaximum()));

                    System.out.println("nado");
                    last_message = fget_message;
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "El servidor cerró la conexión.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            } catch (NullPointerException ignored) {
            }
        });
        receiveMessages.start(); // Iniciar el hilo
    }

    // Método del botón
    public void actionPerformed(ActionEvent event) {
        // Se escucha del botón y el enter del textField
        if ((event.getSource() == sendButton || event.getSource() == textField)
                && (!textField.getText().isEmpty())) {

            try {
                output.println(textField.getText());

                fsend_message =
                        last_message +
                                "<div style='font-family:\"Product Sans\",Roboto, Helvetica; font-size: 15px; text-align: right;'>" +
                                    "<p style='color: #a4a6ad; font-size: 13px;'><i>" +
                                        "Cliente (tú)" +
                                    "</i></p>" +
                                    textField.getText() +
                                    "<span style='color: #a4a6ad; font-size: 10px; padding-left: 10px;'><i><br> a las " + getActualTime() +
                                    "</i></span>" +
                                "</div>" +
                                "<div style='color: #fffff2; font-size: 4px'>" +
                                new String(new char[167]).replace("\0", "-") +
                                "</div>";

                serverMessageLabel.setText(HTMLString(fsend_message));

                verticalScrollMessage.setValue(verticalScrollMessage.getMaximum());
                last_message = fsend_message;

            } catch (NullPointerException e) {
                JOptionPane.showMessageDialog(this, "Output desconocido",
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

    class CustomScrollBarUI extends BasicScrollBarUI {
        private Color scrollBarColor = Styles.offOrange;

        @Override
        protected void configureScrollBarColors() {
            thumbColor = scrollBarColor;
            thumbDarkShadowColor = scrollBarColor.darker();
            thumbHighlightColor = scrollBarColor.brighter();
            thumbLightShadowColor = scrollBarColor;
            trackColor = Styles.boneWhite;
            trackHighlightColor = Color.WHITE;
        }

        @Override
        protected JButton createDecreaseButton(int orientation) {
            return createZeroButton();
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            return createZeroButton();
        }

        private JButton createZeroButton() {
            JButton button = new JButton();
            Dimension zeroDim = new Dimension(0, 0);
            button.setPreferredSize(zeroDim);
            button.setMinimumSize(zeroDim);
            button.setMaximumSize(zeroDim);
            return button;
        }



    }
    // Main
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ChatClientGUI clientGUI = new ChatClientGUI(null);
            clientGUI.setBounds(0, 0, 430, 500);
            clientGUI.setLocationRelativeTo(null);
            clientGUI.setVisible(true);
        });
    }
}