import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Clase principal de la ejecución
public class MainWindow extends JFrame implements ActionListener{

    // Elementos de la ventana
    private JLabel welcomeLabel, chatLabel;
    private JButton fiboButton, chatServerButton, chatClientButton, closeButton;

    // Constructor de la ventana
    public MainWindow(){
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Programa Principal");


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


    }

    public void actionPerformed(ActionEvent event){

    }

    // Creador de la Ventana
    public static void main(String[] args) {
        MainWindow mwindow = new MainWindow();
        mwindow.setBounds(0, 0, 430, 600);
        mwindow.setVisible(true);
        mwindow.setLocationRelativeTo(null);
    }
}