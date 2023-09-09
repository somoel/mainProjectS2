import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

// Clase del cálculo de Fibonacci
public class FiboGUI extends JFrame implements ActionListener {

    // Elementos de la ventana fibonacci
    private JLabel titleLabel, nLabel, resultLabel;
    private JTextField nField;
    private JButton calcFiboButton, backButton, closeButton;

    private int n; // Variable a contar

    private JFrame backFrame; // Manejo de la ventana anterior

    // Constructor
    public FiboGUI(JFrame backFrame) {
        this.backFrame = backFrame;

        // Propios de la ventana
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Programa Fibonacci");

        titleLabel = new JLabel("Fibonacci", SwingConstants.RIGHT);
        titleLabel.setBounds(10, 10, 400, 50);
        add(titleLabel);

        nLabel = new JLabel("Ingrese la cantidad de números de la serie de Fibonacci");
        nLabel.setBounds(10, 100, 400, 50);
        add(nLabel);

        nField = new JTextField();
        nField.setHorizontalAlignment(JTextField.CENTER);
        nField.setBounds(115, 150, 70, 50);
        nField.addActionListener(this);
        add(nField);

        calcFiboButton = new JButton("Calcular");
        calcFiboButton.setBounds(195, 150, 120, 50);
        calcFiboButton.addActionListener(this);
        add(calcFiboButton);

        resultLabel = new JLabel("Aquí debería aparecer el resultado");
        resultLabel.setVerticalAlignment(resultLabel.TOP);
        resultLabel.setBounds(10, 210, 400, 210);
        add(resultLabel);

        backButton = new JButton("Volver");
        backButton.setBounds(10, 430, 195, 30);
        add(backButton);

        closeButton = new JButton("Cerrar");
        closeButton.setBounds(215, 430, 195, 30);
        add(closeButton);

        new Styles(this, titleLabel, nField); // Agrega colores

        nField.setFont(Styles.mainFont.deriveFont(Font.PLAIN, 25));

        new BackAndCloseB(this, this.backFrame, backButton, closeButton, null); // Funciones de volver y cerrar

    }

    public void actionPerformed(ActionEvent event) {

        // Calculo y muestreo de la serie de Fibonacci
        if (event.getSource() == calcFiboButton || event.getSource() == nField) {
            try {
                n = Integer.parseInt(nField.getText()); // Obtener el número
            } catch (Exception NumberFormatException) {
                JOptionPane.showMessageDialog(this, "Error\n\nDebe ser un número entero.");
            }

            if (n < 0) { // Revisión de número positivo
                JOptionPane.showMessageDialog(this, "Error\n\nDebe ser un número positivo.");
            }
            nField.setText("");

            // Devolución del arreglo con los números de la secuencia
            switch (n) {
                case 0 -> resultLabel.setText("Resultado: Ningún número.");
                case 1 -> resultLabel.setText("Resultado: 0");
                case 2 -> resultLabel.setText("Resultado: 0, 1");
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


                    // impresion de la secuencia fiboonacci
                    resultLabel.setText("<html>Resultado: " +
                            Arrays.toString(arreglo).substring(1, Arrays.toString(arreglo).length() - 1) +
                            "</html>");
                }
            }


        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FiboGUI fibo = new FiboGUI(null);

            fibo.setBounds(0, 0, 430, 500);
            fibo.setLocationRelativeTo(null);
            fibo.setVisible(true);
        });
    }


}
