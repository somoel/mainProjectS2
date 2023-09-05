import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

// Clase del Chat: Cliente.
public class ChatClient extends JFrame implements ActionListener {

    // elementos de la ventana del cliente
    private JLabel titleLabel, nLabel;
    private JTextField nField;
    private JButton client;

    // Constructor con un InputDialog dentro
    public ChatClient() {

        setLayout(null);

        String ip_local = "¡Error!";

        try {
            InetAddress ip_local_INET = InetAddress.getLocalHost();
            ip_local = ip_local_INET.getHostAddress();

        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }


        String direccion = JOptionPane.showInputDialog("Su IP es: " + ip_local + "\n\nIngrese la IP del servidor");



        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Chat: Cliente");
        //contenido del label
        titleLabel = new JLabel("Bienvenido, cliente ", SwingConstants.CENTER);
        titleLabel.setBounds(10, 10, 410, 30);
        add(titleLabel);
    }

    // Método del Botón
    public void actionPerformed(ActionEvent event) {

    }

}
