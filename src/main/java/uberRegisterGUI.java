import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* Ventana para registrarse en Ubernardo

 */
public class uberRegisterGUI extends JFrame implements ActionListener {

    // TODO: eliminar el actionlistener

    private JFrame backFrame;
    private JLabel titleLabel, welcomeLabel, cedulaLabel, passLabel,
            numberLabel, orLoginLabel, nameLabel, userTypeLabel;
    private JButton goToLoginButton, registerButton, backButton, closeButton;
    private JTextField cedulaField, numberField, nameField;
    private JPasswordField passField;
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

        String[] user_types = {"Selecciona una opción","Cliente", "Conductor"};
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


        numberLabel = new JLabel("Número");
        numberLabel.setBounds(10, 320, 400, 30);
        add(numberLabel);

        numberField = new JTextField();
        numberField.setBounds(10, 350, 400, 30);
        add(numberField);


        passLabel = new JLabel("Contraseña");
        passLabel.setBounds(10, 390, 400, 30);
        add(passLabel);

        passField = new JPasswordField();
        passField.setBounds(10, 420, 400, 30);
        passField.addActionListener(this);
        add(passField);



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
                new JTextField[]{nameField, cedulaField, passField, numberField}, separatorTitle);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
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
