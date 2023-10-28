import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/* Ventana para registrarse en Ubernardo

 */
public class uberRegisterGUI extends JFrame implements ActionListener {

    /* TODO: eliminar el actionlistener
        Optimizar el checkEntrys
     */

    private JFrame backFrame;
    private JLabel titleLabel, welcomeLabel, cedulaLabel, passLabel,
            phoneLabel, orLoginLabel, nameLabel, userTypeLabel;
    private JButton goToLoginButton, registerButton, backButton, closeButton, showPassButton;
    private JTextField cedulaField, phoneField, nameField;
    private JPasswordField passField;
    private String[] user_types = {"Selecciona una opción","Cliente", "Conductor"};
    private JComboBox<String> userTypeBox;
    private JSeparator separatorTitle;

    public uberRegisterGUI(JFrame backFrame){
        this.backFrame = backFrame;

        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Ubernardo");

        titleLabel = new JLabel("Ubernardo");
        titleLabel.setBounds(10, 10, 400, 40);
        add(titleLabel);

        separatorTitle = new JSeparator();
        add(separatorTitle);

        welcomeLabel = new JLabel("Esto es fácil");
        welcomeLabel.setBounds(10, 60, 400, 30);
        add(welcomeLabel);


        userTypeLabel = new JLabel("Qué tipo de usuario serás?");
        userTypeLabel.setBounds(10, 110, 400, 30);
        add(userTypeLabel);


        userTypeBox = new JComboBox<>(user_types);
        userTypeBox.setBounds(10, 140, 400, 30);
        add(userTypeBox);

        // Personalización del Combobox
        userTypeBox.setUI(new BasicComboBoxUI() {
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
        userTypeBox.setBorder(
                BorderFactory.createCompoundBorder(new LineBorder(Styles.defaultBoder),
                        BorderFactory.createEmptyBorder(5, 5, 5, 5)));
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


        phoneLabel = new JLabel("Número");
        phoneLabel.setBounds(10, 320, 400, 30);
        add(phoneLabel);

        phoneField = new JTextField();
        phoneField.setBounds(10, 350, 400, 30);
        add(phoneField);


        passLabel = new JLabel("Contraseña");
        passLabel.setBounds(10, 390, 400, 30);
        add(passLabel);

        passField = new JPasswordField();
        passField.setBounds(10, 420, 310, 30);
        passField.addActionListener(this);
        add(passField);

        showPassButton = new JButton("Mostrar");
        showPassButton.setBounds(320, 420, 90, 28);
        showPassButton.addActionListener(this);
        add(showPassButton);

        registerButton = new JButton("Registrarse");
        registerButton.setBounds(10, 460, 400, 45);
        registerButton.addActionListener(this);
        add(registerButton);


        orLoginLabel = new JLabel("¿Ya tienes cuenta?");
        orLoginLabel.setBounds(10, 515, 400, 30);
        add(orLoginLabel);

        goToLoginButton = new JButton("Inicia Sesión");
        goToLoginButton.setBounds(10, 545, 400, 45);
        goToLoginButton.addActionListener(this);
        add(goToLoginButton);


        backButton = new JButton("Volver");
        backButton.setBounds(10, 615, 195, 45);
        add(backButton);

        closeButton = new JButton("Cerrar");
        closeButton.setBounds(215, 615, 195, 45);
        add(closeButton);


        new BackAndCloseB(this, this.backFrame, backButton, closeButton, null);

        new Styles(this, titleLabel,
                new JTextField[]{nameField, cedulaField, passField, phoneField}, separatorTitle);

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


    }

    // Mostrar error en JOptionPane y reiniciar entry de password
    private void showError(String message){
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
        passField.setText("");
    }

    // Revisa los entrys
    private boolean checkEntrys(){
        String name = nameField.getText(), cedula = cedulaField.getText(),
                phone = phoneField.getText(), pass = new String(passField.getPassword());

        if (userTypeBox.getSelectedItem() == user_types[0]){
            showError("No has seleccionado ninguna opción");
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
        }


        if (!cedula.matches("\\d+") || cedula.length() > 10 || cedula.length() < 9){
            showError("Eso no es una cédula");
            cedulaField.setText("");
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
            System.out.println("registraito");
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
            uberLGUI.setBounds(0, 0, 430, 500);
            uberLGUI.setLocationRelativeTo(null);
            uberLGUI.setVisible(true);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            uberRegisterGUI uberRGUI = new uberRegisterGUI(null);
            uberRGUI.setBounds(0, 0, 430, 700);
            uberRGUI.setLocationRelativeTo(null);
            uberRGUI.setVisible(true);
        });
    }


}
