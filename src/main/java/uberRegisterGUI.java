import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import java.awt.*;
import java.awt.event.*;
import java.util.Objects;

/* Ventana para registrarse en Ubernardo

 */
public class uberRegisterGUI extends JFrame implements ActionListener {

    private JFrame backFrame;
    private JLabel titleLabel, welcomeLabel, cedulaLabel, passLabel, emailLabel,
            phoneLabel, orLoginLabel, nameLabel, userTypeLabel, placaAndColorLabel;
    private JButton goToLoginButton, registerButton, backButton, closeButton, showPassButton;
    private JTextField cedulaField, phoneField, nameField, placaField, emailField;
    private JPasswordField passField;
    private String[] user_types = {"Selecciona una opción","Cliente", "Conductor"},
            colors_car = {"Elige un color", "Negro", "Gris", "Blanco", "Rojo", "Azul", "Verde", "Naranja", "Otro"};
    private JComboBox<String> userTypeBox, colorBox;
    private JSeparator separatorTitle;

    public uberRegisterGUI(JFrame backFrame){
        this.backFrame = backFrame;

        titleLabel = new JLabel("Ubernardo");
        titleLabel.setBounds(10, 10, 400, 40);
        add(titleLabel);

        separatorTitle = new JSeparator();
        add(separatorTitle);

        welcomeLabel = new JLabel("Esto es fácil");
        welcomeLabel.setBounds(10, 60, 400, 30);
        add(welcomeLabel);


        userTypeLabel = new JLabel("¿Qué tipo de usuario serás?");
        userTypeLabel.setBounds(10, 110, 400, 30);
        add(userTypeLabel);


        userTypeBox = new JComboBox<>(user_types);
        userTypeBox.setBounds(10, 140, 400, 30);
        add(userTypeBox);

        // Personalización del Combobox
        setupComboBox(userTypeBox);

        userTypeBox.addItemListener(e -> {
            if (e.getItem() == user_types[2]) {
                placaField.setEnabled(true);
                colorBox.setEnabled(true);
            }
        });
        userTypeBox.setBackground(Styles.boneWhite);


        nameLabel = new JLabel("Nombre");
        nameLabel.setBounds(10, 180, 400, 30);
        add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(10, 210, 400, 30);
        add(nameField);


        cedulaLabel = new JLabel("Cédula");
        cedulaLabel.setBounds(10, 250, 400, 30);
        add(cedulaLabel);

        cedulaField = new JTextField();
        cedulaField.setBounds(10, 280, 400, 30);
        add(cedulaField);

        emailLabel = new JLabel("Correo");
        emailLabel.setBounds(10,320, 400, 30);
        add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(10, 350, 400, 30);
        add(emailField);


        phoneLabel = new JLabel("Número");
        phoneLabel.setBounds(10, 390, 400, 30);
        add(phoneLabel);

        phoneField = new JTextField();
        phoneField.setBounds(10, 420, 400, 30);
        add(phoneField);


        placaAndColorLabel = new JLabel("Placa y color de tu carro");
        placaAndColorLabel.setBounds(10, 460, 400, 30);
        add(placaAndColorLabel);

        placaField = new JTextField();
        placaField.setBounds(10, 490, 195, 30);
        placaField.setEnabled(false);
        add(placaField);

        colorBox = new JComboBox<>(colors_car);
        colorBox.setBounds(215, 490, 195, 30);
        colorBox.setEnabled(false);
        add(colorBox);

        setupComboBox(colorBox);
        colorBox.addItemListener(e -> colorBox.setEditable(e.getItem() == colors_car[8]));

        passLabel = new JLabel("Contraseña");
        passLabel.setBounds(10, 530, 400, 30);
        add(passLabel);

        passField = new JPasswordField();
        passField.setBounds(10, 560, 310, 30);
        passField.addActionListener(this);
        add(passField);

        showPassButton = new JButton("Mostrar");
        showPassButton.setBounds(320, 560, 90, 28);
        showPassButton.addActionListener(this);
        add(showPassButton);

        registerButton = new JButton("Registrarse");
        registerButton.setBounds(10, 595, 400, 45);
        registerButton.addActionListener(this);
        add(registerButton);


        orLoginLabel = new JLabel("¿Ya tienes cuenta?");
        orLoginLabel.setBounds(10, 650, 400, 30);
        add(orLoginLabel);

        goToLoginButton = new JButton("Inicia Sesión");
        goToLoginButton.setBounds(10, 680, 400, 45);
        goToLoginButton.addActionListener(this);
        add(goToLoginButton);


        backButton = new JButton("Volver");
        backButton.setBounds(10, 730, 195, 45);
        add(backButton);

        closeButton = new JButton("Cerrar");
        closeButton.setBounds(215, 730, 195, 45);
        add(closeButton);


        new BackAndCloseB(this, this.backFrame, backButton, closeButton, () -> MainWindow.main(null));

        new Styles(this, titleLabel,
                new JTextField[]{nameField, cedulaField, passField, phoneField, placaField, emailField},
                separatorTitle);

        // Estilos para el botón de mostrar contraseña
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


        // Propios de la ventana
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Uber: Registrarse");
        setSize(430,820);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /*
    Configuración de color para un combobox
     */
    public static void setupComboBox(JComboBox<String> eComboBox){

        eComboBox.setUI(new BasicComboBoxUI() {
            @Override
            protected ComboPopup createPopup() {
                return new BasicComboPopup(comboBox) {
                    @Override
                    protected JScrollPane createScroller() {
                        JScrollPane scroller = new JScrollPane(list,
                                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                        scroller.getViewport().setBackground(Color.RED);
                        return scroller;
                    }
                };
            }
        });
        eComboBox.setBorder(
                BorderFactory.createCompoundBorder(new LineBorder(Styles.defaultBoder),
                        BorderFactory.createEmptyBorder(5, 5, 5, 5)));
    }

    // Mostrar error en JOptionPane y reiniciar entry de password
    private void showError(String message){
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
        passField.setText("");
    }

    // Revisa los entrys
    private boolean checkEntrys(){
        placaField.setText(placaField.getText().toUpperCase());
        String name = nameField.getText(), cedula = cedulaField.getText(),
                phone = phoneField.getText(), placa = placaField.getText(),
                pass = new String(passField.getPassword()),
                email = emailField.getText();

        if (userTypeBox.getSelectedItem() == user_types[0]){
            showError("No has seleccionado ningún tipo de usuario");
            return false;
        } else if (userTypeBox.getSelectedItem() == user_types[2])
            if (!placa.matches("^[A-Z]{3}[0-9]{3}$")) {
                showError("Está mal la placa");
                placaField.setText("");
                return false;
            } else if (colorBox.getSelectedItem() == colors_car[0]){
                showError("No has seleccionado ningún color de carro");
                return false;
            }

        for (int i = 0; i < name.length(); i++) {
            if (!(Character.isAlphabetic(name.charAt(i)) || Character.isWhitespace(name.charAt(i)))) {
                showError("¿Y ese nombre?");
                nameField.setText("");
                return false;
            }
        }
        if (name.length() > 50 || name.length() < 5){
            showError("Que nombre tan raro");
            nameField.setText("");
            return false;
        }


        if (!cedula.matches("\\d+") || cedula.length() > 10 || cedula.length() < 9){
            showError("Eso no es una cédula");
            cedulaField.setText("");
            return false;
        }

        if (!email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")){
            showError("Ese correo no sirve");
            emailField.setText("");
            return false;
        }

        if (!phone.matches("\\d+") || phone.length() != 10){
            showError("Ese número está raro");
            phoneField.setText("");
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
        // Registrarse
        if ((e.getSource() == registerButton || e.getSource() == passField) && checkEntrys()){
            if (operationsCRUD.registerUser(
                    (String) Objects.requireNonNull(userTypeBox.getSelectedItem()), nameField.getText(),
                    cedulaField.getText(), emailField.getText(), phoneField.getText(), placaField.getText(),
                    (String) colorBox.getSelectedItem(), new String(passField.getPassword())
            )){
                JOptionPane.showMessageDialog(this,
                        "Listo, se registró rey.", "Hecho", JOptionPane.INFORMATION_MESSAGE);
            } else {
                System.out.println("troste");
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

        // Mejor loguearse
        if (e.getSource() == goToLoginButton){
            dispose();
            uberLoginGUI uberLGUI = new uberLoginGUI(null);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            uberRegisterGUI uberRGUI = new uberRegisterGUI(null);
        });
    }


}
