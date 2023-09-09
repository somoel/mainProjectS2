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

    // Paleta de colores usados en la interfaz
    public static Color darkBlue = Color.decode("#191D23"),
            darkGreen = Color.decode("#57707A"),
            lightGreen = Color.decode("#7B919C"),
            coldGray = Color.decode("#989DAA"),
            offPink = Color.decode("#C5BAC4"),
            rareWhite = Color.decode("#DEDCDC"),
            lightBlue = Color.decode("#D0EFFF"),
            pastelGreen = Color.decode("#A5D6B6");

    // Fuente principal
    public static Font mainFont = new Font("Open Sans", Font.PLAIN, 15);

    // Constructor y unica función
    public Styles(JFrame frame, JLabel titleLabel, JTextField textField){
        this.frame = frame;
        this.titleLabel = titleLabel;
        this.textField = textField;

        this.frame.setLayout(null);
        this.frame.getContentPane().setBackground(lightGreen);
        this.frame.setResizable(false);

        // Obtiene cada componente del frame
        for (Component c: this.frame.getRootPane().getContentPane().getComponents()){

            c.setFont(mainFont); // Asigna la fuente
            c.setForeground(darkBlue); // Y el color de la fuente

            if (c instanceof JButton){ // Para cada botón le asigna un Mouse Listener
                c.addMouseListener(new MouseAdapter() {

                    // Al pasar el cursor sobre un botón
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        c.setBackground(offPink);
                        c.setForeground(darkBlue);
                        c.setFont(mainFont.deriveFont(Font.BOLD, 20));
                    }

                    // Al sacar el cursor sobre dicho botón
                    @Override
                    public void mouseExited(MouseEvent e) {
                        c.setBackground(darkGreen);
                        c.setForeground(rareWhite);
                        c.setFont(mainFont);
                    }
                });

                // Para todos los botones
                c.setBackground(darkGreen);
                c.setForeground(rareWhite);
            }
        }

        this.titleLabel.setFont(new Font("Arial Black", Font.PLAIN, 30)); // Fuente para el título

        // Fuente para el textfield con su debido color
        if (this.textField != null) {
            this.textField.setBackground(coldGray);
            this.textField.setForeground(rareWhite);
        }
    }
}
