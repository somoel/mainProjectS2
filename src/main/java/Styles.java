import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/* Decora todas las ventanas de manera uniforme
siguiendo el mismo estilo de IU
 */
public class Styles {
    private JFrame frame;
    private JLabel titleLabel;
    private JTextField textField;
    private JSeparator underlineTitle;

    // Paleta de colores usados en la interfaz
    public static Color darkBlack = Color.decode("#191D23"),
            brickOrange = Color.decode("#aa4e32"),
            boneWhite = Color.decode("#fffff2"),
            offOrange = Color.decode("#f0af88"),
            lightBlue = Color.decode("#D0EFFF"),
            pastelGreen = Color.decode("#A5D6B6"),
            darkBlue = Color.decode("#b0cad8");

    // Fuente principal
    public static Font mainFont = new Font("Product Sans", Font.PLAIN, 20);

    // Constructor y unica función
    public Styles(JFrame frame, JLabel titleLabel, JTextField textField, JSeparator underlineTitle) {
        this.frame = frame;
        this.titleLabel = titleLabel;
        this.textField = textField;
        this.underlineTitle = underlineTitle;

        this.frame.setLayout(null);
        this.frame.getContentPane().setBackground(boneWhite);
        this.frame.setResizable(false);

        // Obtiene cada componente del frame
        for (Component c : this.frame.getRootPane().getContentPane().getComponents()) {

            c.setFont(mainFont); // Asigna la fuente
            c.setForeground(darkBlack); // Y el color de la fuente

            if (c instanceof JButton) { // Para cada botón le asigna un Mouse Listener
                c.addMouseListener(new MouseAdapter() {

                    // Al pasar el cursor sobre un botón
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        c.setBackground(offOrange);
                        c.setForeground(darkBlack);
                        c.setFont(mainFont.deriveFont(Font.ITALIC, 25));
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

        this.titleLabel.setFont(mainFont.deriveFont(Font.BOLD, 30)); // Fuente para el título

        // Fuente para el textfield con su debido color
        if (this.textField != null) {
            this.textField.setBackground(boneWhite);
            this.textField.setForeground(darkBlack);
        }

        // Creador del Underline de los títulos.
        if (this.underlineTitle != null) {
            this.underlineTitle.setBounds(0, this.titleLabel.getY() + this.titleLabel.getHeight(), 430, 5);
            underlineTitle.setBorder(BorderFactory.createLineBorder(offOrange, 3));
        }

    }
}
