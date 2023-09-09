import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/* Ventana principal que incluye
todos los módulos adyacentes
 */
public class MainWindow extends JFrame implements ActionListener, MouseListener {

    // Elementos de la ventana
    private JLabel welcomeLabel, chatLabel;
    private JButton fiboButton, chatServerButton, chatClientButton, closeButton;
    public static Font mainFont = new Font("Open Sans", Font.BOLD, 15); // Fuente por defecto para la ventana

    // Constructor de la ventana
    public MainWindow() {
        // Configuración de la ventana
        setLayout(null);
        getContentPane().setBackground(Color.decode("#7B919C"));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setTitle("Programa Principal");

        // Elementos de la ventana
        welcomeLabel = new JLabel("Bienvenido de nuevo");
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
        for (Component c : contentPane.getComponents()) { // Para cada componente

            c.setFont(mainFont); // Se le asigna la fuente principal
            c.setForeground(Color.decode("#191D23")); // Y el mismo color claro para tooodo

            if (c instanceof JButton) {
                ((JButton) c).addActionListener(this); // Y a todos los botones les asiga el action listener
                c.setBackground(Color.decode("#57707A")); // También colores
                c.setForeground(Color.decode("#DEDCDC"));
                c.addMouseListener(this);
            }
        }

        welcomeLabel.setFont(new Font("Open Sans", Font.BOLD, 30)); // El label va a tener otro tipo de fuente
        chatLabel.setFont(new Font("Open Sans", Font.BOLD, 20));
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

    // Cambiar el estilo de un componente
    private void changeStyle(Component c, Color bgColor, Color fgColor, Font font){
        c.setBackground(bgColor);
        c.setForeground(fgColor);
        c.setFont(font);
    }

    // Evento de pasar el ratón sobre el componente
    public void mouseEntered(MouseEvent event) {
        if (event.getSource() instanceof JButton){
            if (event.getSource() == chatServerButton){
                changeStyle(chatServerButton, Color.decode("#d0efff"), Color.decode("#191D23"),
                        mainFont.deriveFont(Font.BOLD,20));
            } else if (event.getSource() == chatClientButton) {
                changeStyle(chatClientButton, Color.decode("#a5d6b6"), Color.decode("#191D23"),
                        mainFont.deriveFont(Font.BOLD, 20));
            } else {
                changeStyle((Component) event.getSource(), Color.decode("#C5BAC4"), Color.decode("#191D23"),
                        mainFont.deriveFont(Font.PLAIN,20));
            }
        }
    }

    // Evento de ya no pasar el ratón sobre el componente
    public void mouseExited(MouseEvent event) {
        if (event.getSource() instanceof JButton){
            changeStyle((Component) event.getSource(), Color.decode("#57707A"), Color.decode("#DEDCDC"),
                    mainFont);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}


    // Creador de la Ventana
    public static void main(String[] args) {
        MainWindow mwindow = new MainWindow();
        mwindow.setBounds(0, 0, 430, 600);
        mwindow.setVisible(true);
        mwindow.setDefaultCloseOperation(EXIT_ON_CLOSE);
        mwindow.setLocationRelativeTo(null);

    }




}