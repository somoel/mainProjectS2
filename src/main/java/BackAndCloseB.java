import javax.swing.*;

/* Esta clase es un módulo complemento de las subventanas
generadas a partir del menú principal implementando acciones
comunes para el botón de volver y salir.
 */
public class BackAndCloseB {
    private JFrame frame, backFrame;
    private JButton backButton, closeButton;
    private Runnable run;

    // Constructor y función principal
    public BackAndCloseB(JFrame frame, JFrame backFrame, JButton backButton, JButton closeButton, Runnable run) {
        this.frame = frame; // Frame solicitado
        this.backFrame = backFrame; // Frame anterior al solicitado
        this.backButton = backButton; // Botón de volver
        this.closeButton = closeButton; // Botón de cerrrar
        this.run = run; // Ejecutar alguna acción al usar el botón de volver

        // Evento del botón de volver
        this.backButton.addActionListener(e -> {
            if (this.run != null) {
                this.run.run(); // Runnear
            }
            this.frame.dispose(); // Cerrar la ventana actual
            try {
                SwingUtilities.invokeLater(() -> this.backFrame.setVisible(true)); // Volver a la ventana principal
            } catch (java.lang.NullPointerException ignored) {
            }
        });

        // Evento del botón de cerrar
        this.closeButton.addActionListener(e -> {
            if (this.run != null) {
                this.run.run();
            }
            this.frame.dispose(); // Cerrar la ventana actual
            try {
                this.backFrame.dispose(); // Cierra la ventana anterior
            } catch (java.lang.NullPointerException ignored) {
            }
        });
    }
}
