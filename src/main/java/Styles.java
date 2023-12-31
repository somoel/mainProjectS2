import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/* Decora todas las ventanas de manera uniforme
siguiendo el mismo estilo de IU
 */

/*TODO: Organizar bien el constructor y hacer varios métodos.
        Optimizar constructores con varios textfields.
 */
public class Styles {
    private JFrame frame;
    private JLabel titleLabel;
    private JDialog dialog;
    private JTextField[] textFields;
    private JLabel icon_label;
    private JTextField textField;
    private JSeparator underlineTitle;
    private Image iconLogo = new ImageIcon("src/main/resources/blue_icon.png").getImage();


    // Paleta de colores usados en la interfaz
    public static Color darkBlack = Color.decode("#191D23"),
            brickOrange = Color.decode("#aa4e32"),
            boneWhite = Color.decode("#fffff2"),
            offOrange = Color.decode("#f0af88"),
            lightBlue = Color.decode("#D0EFFF"),
            pastelGreen = Color.decode("#A5D6B6"),
            darkBlue = Color.decode("#b0cad8"),
            defaultBoder = Color.decode("#7a8a99");

    // Fuentes derivadas de la principal
    public static Font mainFont = new Font("Product Sans", Font.PLAIN, 20),
            italicMainFont = mainFont.deriveFont(Font.ITALIC, 25),
            titleMainFont = mainFont.deriveFont(Font.BOLD, 30),
            smallerMainFont = mainFont.deriveFont(Font.PLAIN, 15);

    // Reconstructor por defecto para los frames
    private void BasicStyles(){
        BasicStyles(this.frame, this.frame.getContentPane());
    }

    // Estilos básicos para dividir correctamente los constructores
    private void BasicStyles(Window win, Container pane){
        if (win instanceof JFrame){
            this.frame.setResizable(false);
        } else if (win instanceof Dialog){
            this.dialog.setResizable(false);}


        win.setLayout(null);
        win.setIconImage(iconLogo);

        panelStyle(pane); // Optimizar panel por panel

        // Label del Ícono
        icon_label = new JLabel();
        icon_label.setBounds(titleLabel.getWidth() - 40, 10, 40, 40);

        // Redimensión del Ícono para que quede al tamaño del Label
        Icon iconImage = new ImageIcon(
                iconLogo.getScaledInstance(icon_label.getWidth()
                        , icon_label.getHeight(), Image.SCALE_SMOOTH));

        // Agrega el ícono al JLabel
        icon_label.setIcon(iconImage);
        win.add(icon_label);

        this.titleLabel.setFont(titleMainFont); // Fuente para el título

        // Creador del Underline de los títulos.
        if (this.underlineTitle != null) {
            this.underlineTitle.setBounds(0, this.titleLabel.getY() + this.titleLabel.getHeight(),
                    480, 5);
            underlineTitle.setBorder(BorderFactory.createLineBorder(offOrange, 3));
        }
    }

    public static void panelStyle(Container pane) {
        pane.setBackground(boneWhite);
        // Obtiene cada componente del frame
        for (Component c : pane.getComponents()) {

            c.setFont(mainFont); // Asigna la fuente
            c.setForeground(darkBlack); // Y el color de la fuente

            if (c instanceof JButton) { // Para cada botón le asigna un Mouse Listener
                c.addMouseListener(new MouseAdapter() {

                    // Al pasar el cursor sobre un botón
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        c.setBackground(offOrange);
                        c.setForeground(darkBlack);
                        c.setFont(italicMainFont);
                    }

                    // Al sacar el cursor sobre dicho botón
                    @Override
                    public void mouseExited(MouseEvent e) {
                        c.setBackground(brickOrange);
                        c.setForeground(boneWhite);
                        c.setFont(mainFont);
                    }
                });

                // Para todos los botones
                c.setBackground(brickOrange);
                c.setForeground(boneWhite);
            }
        }
    }


    // Constructor y unica función
    public Styles(JFrame frame, JLabel titleLabel, JTextField textField, JSeparator underlineTitle) {
        this.frame = frame;
        this.titleLabel = titleLabel;
        this.textField = textField;
        this.underlineTitle = underlineTitle;

        BasicStyles();

        // Fuente para el textfield con su debido color
        if (this.textField != null) {
            this.textField.setBackground(boneWhite);
            this.textField.setForeground(darkBlack);
        }

    }

    public Styles(JFrame frame, JLabel titleLabel, JTextField[] textFields, JSeparator underlineTitle) {
        this.frame = frame;
        this.titleLabel = titleLabel;
        this.textFields = textFields;
        this.underlineTitle = underlineTitle;

        BasicStyles();

        textFieldStyles();
    }

    private void textFieldStyles() {
        // Fuente para el textfield con su debido color
        for (JTextField textF: this.textFields) {
            if (textF != null) {
                textF.setBackground(boneWhite);
                textF.setForeground(darkBlack);
            }
        }
    }

    public Styles(JFrame frame, JLabel titleLabel, JSeparator underlineTitle){
        this.frame = frame;
        this.titleLabel = titleLabel;
        this.underlineTitle = underlineTitle;

        BasicStyles();
    }

    // Constructor para JDialogs con múltiples textfields
    public Styles(JDialog dialog, JLabel titleLabel, JTextField[] textFields, JSeparator underlineTitle) {
        this.titleLabel = titleLabel;
        this.dialog = dialog;
        this.underlineTitle = underlineTitle;
        this.textFields = textFields;
        BasicStyles(this.dialog, this.dialog.getContentPane());
        textFieldStyles();
    }

    public Styles(JDialog dialog, JLabel titleLabel, JSeparator underlineTitle) {
        this.titleLabel = titleLabel;
        this.dialog = dialog;
        this.underlineTitle = underlineTitle;
        BasicStyles(this.dialog, this.dialog.getContentPane());
    }


}
