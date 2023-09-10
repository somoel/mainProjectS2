import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/* Ventana principal que incluye
todos los módulos adyacentes
 */
public class MainWindow extends JFrame implements ActionListener{

    // Elementos de la ventana
    private JLabel welcomeLabel, chatLabel;
    private JButton fiboButton, chatServerButton, chatClientButton, closeButton;


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
        fiboButton.setBounds(10, 170, 400, 70);
        fiboButton.addActionListener(this);
        add(fiboButton);


        chatLabel = new JLabel("¿Ó el chat?", SwingConstants.CENTER);
        chatLabel.setBounds(10, 250, 400, 50);
        add(chatLabel);

        chatServerButton = new JButton("Servidor");
        chatServerButton.setBounds(10, 300, 195, 70);
        chatServerButton.addActionListener(this);
        add(chatServerButton);

        chatClientButton = new JButton("Cliente");
        chatClientButton.setBounds(215, 300, 195, 70);
        chatClientButton.addActionListener(this);
        add(chatClientButton);

        closeButton = new JButton("Mejor me voy");
        closeButton.setBounds(10, 490, 400, 70);
        closeButton.addActionListener(this);
        add(closeButton);

        new Styles(this, welcomeLabel, null, null); // Agrega colores

        for (Component c: getRootPane().getContentPane().getComponents()){
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


        chatLabel.setFont(Styles.mainFont.deriveFont(Font.BOLD, 20));
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
            chatServerGUI.setBounds(0, 0, 430, 500);
            chatServerGUI.setVisible(true);
            chatServerGUI.setLocationRelativeTo(null);


        }

        // Botón Chat Cliente
        if (event.getSource() == chatClientButton) {
            setVisible(false);
            ChatClientGUI ChatClientGUI = new ChatClientGUI(this);
            ChatClientGUI.setBounds(0, 0, 430, 500);
            ChatClientGUI.setVisible(true);
            ChatClientGUI.setLocationRelativeTo(null);

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