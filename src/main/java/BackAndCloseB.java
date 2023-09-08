import javax.swing.*;

/* Esta clase es un módulo complemento de las subventanas
generadas a partir del menú principal implementando acciones
comunes para el botón de volver y salir.
 */
public class BackAndCloseB {
    private JFrame frame, backFrame;
    private JButton backButton, closeButton;

    // Constructor y función principal
    public BackAndCloseB (JFrame frame, JFrame backFrame, JButton backButton, JButton closeButton){
        this.frame = frame; // Frame solicitado
        this.backFrame = backFrame; // Frame anterior al solicitado
        this.backButton = backButton; // Botón de volver
        this.closeButton = closeButton; // Botón de cerrrar

        // Evento del botón de volver
        this.backButton.addActionListener(e -> {
            this.frame.dispose(); // Cerrar la ventana actual
            try {
                SwingUtilities.invokeLater(() -> backFrame.setVisible(true)); // Volver a la ventana principal
            } catch (java.lang.NullPointerException ignored) {}
        });

        // Evento del botón de cerrar
        this.closeButton.addActionListener(e -> {
                this.frame.dispose(); // Cerrar la ventana actual
                try {
                    this.backFrame.dispose(); // Cierra la ventana anterior
                } catch (java.lang.NullPointerException ignored) {}
            }
        );
    }
}
