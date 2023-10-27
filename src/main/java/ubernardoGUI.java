import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ubernardoGUI extends JFrame implements ActionListener{

    // TODO: Eliminar la implementación de Action Listener

    private JFrame backFrame;
    private JLabel titleLabel, welcomeLabel, cedulaLabel, passLabel, orRegisterLabel;
    private JButton loginButton, registerButton, backButton, closeButton;
    private JTextField cedulaField, passField;
    private JSeparator separatorTitle;
    public ubernardoGUI(JFrame backFrame){
        this.backFrame = backFrame;

        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Ubernardo");

        titleLabel = new JLabel("Ubernardo");
        titleLabel.setBounds(10, 10, 400, 40);
        add(titleLabel);

        separatorTitle = new JSeparator();
        add(separatorTitle);

        welcomeLabel = new JLabel("Bienvenido de nuevo");
        welcomeLabel.setBounds(10, 60, 400, 30);
        add(welcomeLabel);


        cedulaLabel = new JLabel("Cédula");
        cedulaLabel.setBounds(10, 110, 400, 30);
        add(cedulaLabel);

        cedulaField = new JTextField();
        cedulaField.setBounds(10, 140, 400, 30);
        cedulaField.addActionListener(this);
        add(cedulaField);


        passLabel = new JLabel("Contraseña");
        passLabel.setBounds(10, 190, 400, 30);
        add(passLabel);

        passField = new JTextField();
        passField.setBounds(10, 220, 400, 30);
        passField.addActionListener(this);
        add(passField);



        loginButton = new JButton("Iniciar Sesión");
        loginButton.setBounds(10, 260, 400, 45);
        loginButton.addActionListener(this);
        add(loginButton);


        orRegisterLabel = new JLabel("¿Aún no tienes cuenta?");
        orRegisterLabel.setBounds(10, 315, 400, 40);
        add(orRegisterLabel);

        registerButton = new JButton("Registrarse");
        registerButton.setBounds(10, 355, 400, 45);
        registerButton.addActionListener(this);
        add(registerButton);


        backButton = new JButton("Volver");
        backButton.setBounds(10, 415, 195, 45);
        add(backButton);

        closeButton = new JButton("Cerrar");
        closeButton.setBounds(215, 415, 195, 45);
        add(closeButton);


        new BackAndCloseB(this, this.backFrame, backButton, closeButton, null);

        new Styles(this, titleLabel, cedulaField, separatorTitle);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ubernardoGUI uberGUI = new ubernardoGUI(null);
            uberGUI.setBounds(0, 0, 430, 500);
            uberGUI.setLocationRelativeTo(null);
            uberGUI.setVisible(true);
        });
    }

}
