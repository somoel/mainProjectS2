import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

// Clase del cálculo de Fibonacci
public class FiboGUI extends JFrame implements ActionListener {

    // Elementos de la ventana fibonacci
    private JLabel titleLabel, nLabel;
    private JTextField nField;
    private JButton calcFiboButton, backButton, closeButton;
    private JScrollPane scrollResult;
    private JEditorPane resultLabel;

    private int n; // Variable a contar

    private String result;

    private JFrame backFrame; // Manejo de la ventana anterior
    private JSeparator underlineTitle;

    // Constructor
    public FiboGUI(JFrame backFrame) {
        this.backFrame = backFrame;

        titleLabel = new JLabel("Fibonacci");
        titleLabel.setVerticalAlignment(titleLabel.BOTTOM);
        titleLabel.setBounds(10, 10, 400, 40);
        add(titleLabel);

        underlineTitle = new JSeparator();
        add(underlineTitle);

        nLabel = new JLabel("<html><p style='text-align: center;'>Ingrese la cantidad de números<br>de la serie de Fibonacci</p></html>");
        nLabel.setHorizontalAlignment(nLabel.CENTER);
        nLabel.setBounds(10, 65, 400, 50);
        add(nLabel);

        nField = new JTextField();
        nField.setHorizontalAlignment(JTextField.CENTER);
        nField.setBounds(95, 130, 70, 50);
        nField.addActionListener(this);
        add(nField);

        calcFiboButton = new JButton("Calcular");
        calcFiboButton.setBounds(175, 130, 150, 50);
        calcFiboButton.addActionListener(this);
        add(calcFiboButton);

        resultLabel = new JEditorPane();
        resultLabel.setContentType("text/html");
        resultLabel.setEditable(false);
        resultLabel.setBackground(Styles.boneWhite);
        resultLabel.setText("<html><div style='font-size: 15px; font-family: \"Product Sans\"," +
                " Roboto; text-align: center;'>" +
                "Aquí debería aparecer el resultado" +
                "</div></html>");
        resultLabel.setBounds(10, 200, 400, 210);
        add(resultLabel);


        scrollResult = new JScrollPane(resultLabel);
        scrollResult.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        ChatUtilities.CustomScrollBarUI customScrollBarUI = new ChatUtilities.CustomScrollBarUI();
        scrollResult.getVerticalScrollBar().setUI(customScrollBarUI);
        scrollResult.setBounds(10, 200, 400, 210);
        add(scrollResult);



        backButton = new JButton("Volver");
        backButton.setBounds(10, 415, 195, 45);
        add(backButton);

        closeButton = new JButton("Cerrar");
        closeButton.setBounds(215, 415, 195, 45);
        add(closeButton);

        new Styles(this, titleLabel, nField, underlineTitle); // Agrega colores

        nField.setFont(Styles.mainFont.deriveFont(Font.PLAIN, 25));

        new BackAndCloseB(this, this.backFrame, backButton, closeButton, null); // Funciones de volver y cerrar

        // Propios de la ventana
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Serie de fibonacci");
        setSize(430,500);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent event) {

        // Calculo y muestreo de la serie de Fibonacci
        if (event.getSource() == calcFiboButton || event.getSource() == nField) {
            try {
                n = Integer.parseInt(nField.getText());// Obtener el número"
            } catch (Exception NumberFormatException) {
                JOptionPane.showMessageDialog(this, "Error\n\nDebe ser un número entero.");
            }

            if (n < 0) { // Revisión de número positivo
                JOptionPane.showMessageDialog(this, "Error\n\nDebe ser un número positivo.");
            } else if ( n > 93) {
                JOptionPane.showMessageDialog(this, "Error\n\nNo puede pasar de 93.");
            } else {
                nField.setText("");

                // Devolución del arreglo con los números de la secuencia

                switch (n) {
                    case 0 -> result = "Ningún número";
                    case 1 -> result = "0";
                    case 2 -> result = "0, 1";
                    default -> {

                        // se crea un arreglo del tamaño que el usuario decida
                        long[] arreglo = new long[n];
                        arreglo[0] = 0;
                        arreglo[1] = 1;

                        /* se crea una iteracion con la variable i que permitira
                        sumar los numeros anteriores para obtener la
                        secuencia fibonacci*/
                        for (int i = 2; i < n; i++) {
                            arreglo[i] = arreglo[i - 1] + arreglo[i - 2];
                        }

                        result = Arrays.toString(arreglo).substring(1, Arrays.toString(arreglo).length() - 1);

                    }
                }
                // impresión de la secuencia fiboonacci
                resultLabel.setText(
                        "<html>" +
                                "<div style='font-size: 40px; font-family: \"Product Sans\", Roboto; text-align: center;'>" +
                                result +
                                "</div></html>");


            }
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FiboGUI fibo = new FiboGUI(null);
        });
    }


}
