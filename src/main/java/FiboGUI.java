import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

// Clase del cálculo de Fibonacci
public class FiboGUI extends JFrame implements ActionListener {

    // elementos de la ventana fibonacci
    private JLabel titleLabel, nLabel, resultLabel;
    private JTextField nField;
    private JButton calcFiboButton;

    // Variable a contar
    private int n;

    // Constructor
    public FiboGUI() {
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Programa Fibonacci");
        //contenido del label
        titleLabel = new JLabel("Números de Fibonacci", SwingConstants.CENTER);
        titleLabel.setBounds(10, 10, 410, 30);
        add(titleLabel);

        nLabel = new JLabel("Ingrese la cantidad de números de la serie\n" +
                "de Fibonacci que desea obtener");
        nLabel.setBounds(10, 40, 410, 30);
        add(nLabel);

        nField = new JTextField();
        nField.setBounds(10, 70, 410, 30);
        add(nField);

        calcFiboButton = new JButton("Calcular");
        calcFiboButton.setBounds(10, 100, 410, 30);
        calcFiboButton.addActionListener(this);
        add(calcFiboButton);

        resultLabel = new JLabel("");
        resultLabel.setBounds(10, 130, 410, 30);
        add(resultLabel);

    }

    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == calcFiboButton) {
            try {
                n = Integer.parseInt(nField.getText());
            } catch (Exception NumberFormatException) {
                JOptionPane.showMessageDialog(this, "Error\n\nDebe ser un número entero.");
            }

            if (n < 0) {
                JOptionPane.showMessageDialog(this, "Error\n\nDebe ser un número positivo.");
            }

            // Devolución del arreglo con los números de la secuencia
            switch (n) {

                case 0:
                    resultLabel.setText("");

                case 1:
                    resultLabel.setText("0");
                    break;

                case 2:
                    resultLabel.setText("[0, 1]");
                    break;

                default:

                    // se crea un arreglo del tamaño que el usuario decida
                    int[] arreglo = new int[n];
                    arreglo[0] = 0;
                    arreglo[1] = 1;

                    /* se crea una iteracion con la variable i que permitira
                    sumar los numeros anteriores para obtener la
                    secuencia fibonacci*/
                    for (int i = 2; i < n; i++) {
                        arreglo[i] = arreglo[i - 1] + arreglo[i - 2];
                    }


                    // impresion de la secuencia fiboonacci
                    resultLabel.setText(Arrays.toString(arreglo));
            }

        }
    }


}
