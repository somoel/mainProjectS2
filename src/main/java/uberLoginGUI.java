import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;

/* Inicio de Sesión de Ubernardo
 */
/*
TODO: Cambiar el logo de Ubernardo
      Agregar el "olvidaste tu contraseña"
 */

public class uberLoginGUI extends JFrame implements ActionListener{

    private JFrame backFrame;
    private JLabel titleLabel, welcomeLabel, cedulaLabel, passLabel, orRegisterLabel, userTypeLabel;
    private JButton loginButton, registerButton, backButton, closeButton, showPassButton;
    private JTextField cedulaField;
    private JPasswordField passField;
    private JSeparator separatorTitle;
    private String[]  user_types = {"Selecciona una opción","Cliente", "Conductor"};
    private JComboBox<String> userTypeBox;


    // Constructor de la ventana
    public uberLoginGUI(JFrame backFrame){
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

        userTypeLabel = new JLabel("¿Qué tipo de usuario serás?");
        userTypeLabel.setBounds(10, 110, 400, 30);
        add(userTypeLabel);


        userTypeBox = new JComboBox<>(user_types);
        userTypeBox.setBounds(10, 140, 400, 30);
        add(userTypeBox);

        // Personalización del Combobox
        uberRegisterGUI.setupComboBox(userTypeBox);
        userTypeBox.addItemListener(e -> loginButton.setEnabled(e.getItem() != user_types[0]));
        userTypeBox.setBackground(Styles.boneWhite);


        cedulaLabel = new JLabel("Cédula");
        cedulaLabel.setBounds(10, 190, 400, 30);
        add(cedulaLabel);

        cedulaField = new JTextField();
        cedulaField.setBounds(10, 220, 400, 30);
        add(cedulaField);


        passLabel = new JLabel("Contraseña");
        passLabel.setBounds(10, 270, 400, 30);
        add(passLabel);

        passField = new JPasswordField();
        passField.setBounds(10, 300, 310, 30);
        passField.addActionListener(this);
        add(passField);

        showPassButton = new JButton("Mostrar");
        showPassButton.setBounds(320, 300, 90, 28);
        showPassButton.addActionListener(this);
        add(showPassButton);



        loginButton = new JButton("Iniciar Sesión");
        loginButton.setBounds(10, 340, 400, 45);
        loginButton.addActionListener(this);
        loginButton.setEnabled(false);
        add(loginButton);


        orRegisterLabel = new JLabel("¿Aún no tienes cuenta?");
        orRegisterLabel.setBounds(10, 395, 400, 40);
        add(orRegisterLabel);

        registerButton = new JButton("Registrarse");
        registerButton.setBounds(10, 435, 400, 45);
        registerButton.addActionListener(this);
        add(registerButton);


        backButton = new JButton("Volver");
        backButton.setBounds(10, 485, 195, 45);
        add(backButton);

        closeButton = new JButton("Cerrar");
        closeButton.setBounds(215, 485, 195, 45);
        add(closeButton);


        new BackAndCloseB(this, this.backFrame, backButton, closeButton, () -> MainWindow.main(null));

        new Styles(this, titleLabel, new JTextField[]{cedulaField, passField}, separatorTitle);

        // Estilos para el botón de ver contraseña
        showPassButton.setFont(Styles.smallerMainFont);
        showPassButton.setForeground(Styles.darkBlack);
        showPassButton.setBackground(Styles.boneWhite);
        showPassButton.setVerticalAlignment(SwingConstants.CENTER);
        showPassButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                showPassButton.setBackground(Styles.offOrange);
                showPassButton.setFont(Styles.smallerMainFont);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                showPassButton.setForeground(Styles.darkBlack);
                showPassButton.setBackground(Styles.boneWhite);
                showPassButton.setFont(Styles.smallerMainFont);
            }
        });
    }

    // Muestra error con un mensaje de diálogo adicionado de siempre reiniciar el campo de la contraseña
    private void showError(String message){
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
        passField.setText("");
    }

    // Revisa los campos de texto
    private boolean checkEntrys(){
        String cedula = cedulaField.getText(), pass = new String(passField.getPassword());

        if (!cedula.matches("\\d+") || cedula.length() > 10 || cedula.length() < 9){
            showError("Eso no es una cédula");
            cedulaField.setText("");
            return false;
        }

        if (pass.length() < 8 || pass.length() > 20){
            showError("La contraseña debe ser de 8 a 20 caracteres");
            return false;
        }

        return true;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Iniciar sesión
        if ((e.getSource() == loginButton || e.getSource() == passField) && checkEntrys()){

            String user_id = String.valueOf(operationsCRUD.validateLogin(
                    (String) Objects.requireNonNull(userTypeBox.getSelectedItem()),
                    cedulaField.getText(),
                    new String(passField.getPassword())));

            if (!user_id.equals("-1")){
                dispose();
                switch ((String) userTypeBox.getSelectedItem()){
                    case "Cliente":
                        SwingUtilities.invokeLater(() -> {
                            uberClientGUI uberCCGUI = new uberClientGUI(user_id);
                            uberCCGUI.setBounds(0, 0, 430, 700);
                            uberCCGUI.setLocationRelativeTo(null);
                            uberCCGUI.setVisible(true);
                        });
                        break;
                    case "Conductor":
                        SwingUtilities.invokeLater(() -> {
                            uberDriverGUI uberDGUI = new uberDriverGUI(user_id);
                            uberDGUI.setBounds(0, 0, 430, 700);
                            uberDGUI.setLocationRelativeTo(null);
                            uberDGUI.setVisible(true);
                        });
                }
            }
        }

        // Mostrar contraseña
        if (e.getSource() == showPassButton){
            if(passField.getEchoChar() == 0){
                passField.setEchoChar('•');
                showPassButton.setText("Mostrar");
            } else {
                passField.setEchoChar((char) 0);
                showPassButton.setText("Ocultar");
            }
        }

        // Registrarse mejor
        if (e.getSource() == registerButton){
            dispose();
            uberRegisterGUI uberRGUI = new uberRegisterGUI(null);
            uberRGUI.setBounds(0, 0, 430, 700);
            uberRGUI.setLocationRelativeTo(null);
            uberRGUI.setVisible(true);
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            uberLoginGUI uberLGUI = new uberLoginGUI(null);
            uberLGUI.setBounds(0, 0, 430, 570);
            uberLGUI.setLocationRelativeTo(null);
            uberLGUI.setVisible(true);
        });
    }



}
