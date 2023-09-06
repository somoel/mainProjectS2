import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Clase principal de la ejecución
public class MainWindow extends JFrame implements ActionListener {

    // Elementos de la ventana
    private JLabel welcomeLabel, chatLabel;
    private JButton fiboButton, chatServerButton, chatClientButton, closeButton;

    // Fuente usada en la ventana
    public static Font mainFont = new Font("Open Sans", Font.PLAIN, 15);

    // Constructor de la ventana
    public MainWindow() {
        // Configuración de la ventana
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Programa Principal");

        // Elementos de la ventana
        welcomeLabel = new JLabel("Bienvenido de nuevo", SwingConstants.CENTER);
        welcomeLabel.setBounds(10, 10, 400, 50);
        add(welcomeLabel);

        fiboButton = new JButton("¿Fibonacci?");
        fiboButton.setBounds(10, 120, 400, 70);
        add(fiboButton);


        chatLabel = new JLabel("¿Ó el chat?");
        chatLabel.setBounds(10, 250, 400, 50);
        add(chatLabel);

        chatServerButton = new JButton("Servidor");
        chatServerButton.setBounds(10, 300, 195, 70);
        add(chatServerButton);

        chatClientButton = new JButton("Cliente");
        chatClientButton.setBounds(215, 300, 195, 70);
        add(chatClientButton);

        closeButton = new JButton("Mejor me voy");
        closeButton.setBounds(10, 490, 400, 70);
        add(closeButton);

        // Formato a los componentes
        JRootPane rootPane = this.getRootPane();
        Container contentPane = rootPane.getContentPane();
        for (Component c : contentPane.getComponents()) {

            c.setFont(mainFont);

            if (c instanceof JButton) {
                ((JButton) c).addActionListener(this);
            }
        }

        welcomeLabel.setFont(new Font("Open Sans", Font.BOLD, 30));
    }

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


        }

        // Botón Chat Cliente
        if (event.getSource() == chatClientButton) {
            setVisible(false);
            ChatClientGUI ChatClientGUI = new ChatClientGUI();
            ChatClientGUI.setBounds(0, 0, 430, 500);
            ChatClientGUI.setVisible(true);
            ChatClientGUI.setLocationRelativeTo(null);

        }

        // Botón Cerrar
        if (event.getSource() == closeButton){
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