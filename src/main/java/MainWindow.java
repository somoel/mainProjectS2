import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/* Ventana principal que incluye
todos los módulos adyacentes
TODO: Crear un JOptionPane personalizado.
    Organizar arbol de archivos
     Organizar constructores de cada ventana
 */
public class MainWindow extends JFrame implements ActionListener {

    // Elementos de la ventana
    private JLabel welcomeLabel, chatLabel, pongLabel, uberLabel;
    private JButton fiboButton, chatServerButton, chatClientButton, closeButton, pongButton, uberButton;


    // Constructor de la ventana
    public MainWindow() {
        // Configuración de la ventana
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Programa Principal");

        // Elementos de la ventana
        welcomeLabel = new JLabel("Bienvenido de nuevo", SwingConstants.CENTER);
        welcomeLabel.setBounds(10, 10, 400, 110);
        add(welcomeLabel);

        fiboButton = new JButton("¿Fibonacci?");
        fiboButton.setBounds(10, 120, 400, 50);
        fiboButton.addActionListener(this);
        add(fiboButton);


        chatLabel = new JLabel("¿Ó el chat?");
        chatLabel.setBounds(10, 190, 400, 30);
        add(chatLabel);

        chatServerButton = new JButton("Servidor");
        chatServerButton.setBounds(10, 220, 195, 50);
        chatServerButton.addActionListener(this);
        add(chatServerButton);

        chatClientButton = new JButton("Cliente");
        chatClientButton.setBounds(215, 220, 195, 50);
        chatClientButton.addActionListener(this);
        add(chatClientButton);


        pongLabel = new JLabel("Ah, ¿quieres jugar algo?");
        pongLabel.setBounds(10, 290, 400, 30);
        add(pongLabel);

        pongButton = new JButton("Pong");
        pongButton.setBounds(10, 320, 400, 50);
        pongButton.addActionListener(this);
        add(pongButton);


        uberLabel = new JLabel("Entonces, ¿un uber?");
        uberLabel.setBounds(10, 390, 400, 30);
        add(uberLabel);

        uberButton = new JButton("Ubernardo");
        uberButton.setBounds(10, 420, 400, 50);
        uberButton.addActionListener(this);
        add(uberButton);



        closeButton = new JButton("Mejor me voy");
        closeButton.setBounds(10, 510, 400, 50);
        closeButton.addActionListener(this);
        add(closeButton);

        new Styles(this, welcomeLabel, null); // Agrega colores

        for (Component c : getRootPane().getContentPane().getComponents()) {
            if (c instanceof JButton) {
                c.addMouseListener(new MouseAdapter() {

                    // Customiza los relieves de los botones de servidor y cliente
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        if (e.getSource() == chatServerButton) c.setBackground(Styles.lightBlue);
                        if (e.getSource() == chatClientButton) c.setBackground(Styles.pastelGreen);
                        c.setForeground(Styles.darkBlack);
                    }
                });
            }
        }

    }

    // Acción de los botones
    public void actionPerformed(ActionEvent event) {
        // Botón Fibonacci y llamada de la clase
        if (event.getSource() == fiboButton) {
            setVisible(false);

            FiboGUI fiboWindow = new FiboGUI(this);
            fiboWindow.setBounds(0, 0, 430, 500);
            fiboWindow.setVisible(true);
            fiboWindow.setLocationRelativeTo(null);
        }

        // Boton Chat Servidor
        if (event.getSource() == chatServerButton) {
            setVisible(false);

            ChatServerGUI chatServerGUI = new ChatServerGUI(this);
            chatServerGUI.setBounds(0, 0, 430, 800);
            chatServerGUI.setVisible(true);
            chatServerGUI.setLocationRelativeTo(null);


        }

        // Botón Chat Cliente
        if (event.getSource() == chatClientButton) {
            setVisible(false);
            ChatClientGUI ChatClientGUI = new ChatClientGUI(this);
            ChatClientGUI.setBounds(0, 0, 430, 800);
            ChatClientGUI.setVisible(true);
            ChatClientGUI.setLocationRelativeTo(null);

        }

        // Botón Pong
        if (event.getSource() == pongButton) {
            dispose();
            JFrame CristhianFrame = new JFrame("Ole");
            CristhianFrame.setBounds(0, 0, 430, 500);
            CristhianFrame.setVisible(true);
            CristhianFrame.setLocationRelativeTo(null);

            JLabel cLabel = new JLabel("Parce, porfa. Ponga el pong", SwingConstants.CENTER);
            cLabel.setBounds(0, 0, 430, 500);
            CristhianFrame.add(cLabel);
        }

        // Botón Ubernardo
        if (event.getSource() == uberButton){
            setVisible(false);
            uberLoginGUI uberLGUI = new uberLoginGUI(this);
            uberLGUI.setBounds(0, 0, 430, 500);
            uberLGUI.setVisible(true);
            uberLGUI.setLocationRelativeTo(null);
        }

        // Botón Cerrar
        if (event.getSource() == closeButton) {
            dispose();
        }
    }

    // Creador de la Ventana
    public static void main(String[] args) {
        MainWindow mwindow = new MainWindow();
        mwindow.setBounds(0, 0, 430, 600);
        mwindow.setVisible(true);
        mwindow.setDefaultCloseOperation(EXIT_ON_CLOSE);
        mwindow.setLocationRelativeTo(null);
    }

}