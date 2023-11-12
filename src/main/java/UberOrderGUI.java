import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*
Cuadro de diálogo para pedir un servicio de Uber
 */
/*
TODO: Cambiar las entradas de texto por Combobox y agregar lógica. @maicolo
 */
public class UberOrderGUI extends JDialog implements ActionListener {
    private JLabel titleLabel, startLabel, endLabel, distanceLabel;
    private JTextField startField, endField, distanceField;
    private JButton orderButton, cancelButton;
    private JSeparator separatorTitle;
    private int id_client, exit_code;


    // Constructor
    public UberOrderGUI(JFrame backFrame, int id_client){

        super(backFrame, "Pedido", true);
        this.id_client = id_client;

        titleLabel = new JLabel("Pidamos eso rápido");
        titleLabel.setBounds(10, 10, 400, 40);
        add(titleLabel);

        separatorTitle = new JSeparator();
        add(separatorTitle);


        startLabel = new JLabel("¿Dónde te recogemos?");
        startLabel.setBounds(10, 60, 400, 30);
        add(startLabel);

        startField = new JTextField();
        startField.setBounds(10, 90, 400, 30);
        add(startField);


        endLabel = new JLabel("¿Para dónde es hoy?");
        endLabel.setBounds(10, 130, 400, 30);
        add(endLabel);

        endField = new JTextField();
        endField.setBounds(10, 160, 400, 30);
        add(endField);


        distanceLabel = new JLabel("¿Cuántos kilómetros son en trayecto?");
        distanceLabel.setBounds(10, 200, 400, 30);
        add(distanceLabel);

        distanceField = new JTextField();
        distanceField.setBounds(10, 230, 400, 30);
        distanceField.addActionListener(this);
        add(distanceField);


        orderButton = new JButton("Listo, que llegue");
        orderButton.setBounds(10, 270, 400, 50);
        orderButton.addActionListener(this);
        add(orderButton);


        cancelButton = new JButton("Mejor no");
        cancelButton.setBounds(10, 330, 400, 50);
        cancelButton.addActionListener(this);
        add(cancelButton);

        // Decorador
        new Styles(this, titleLabel,
                new JTextField[] {startField, endField, distanceField},
                separatorTitle);

        // Propios de la ventana
        setLayout(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(430,420);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Muestra el error en un JOptionPane y reinicia el textfield proveniente
    private void showError(String message, JTextField resetField){
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
        resetField.setText("");
    }

    // Revisa las entradas de texto por inconsistencias
    private boolean checkEntrys() {
        String start = startField.getText(), end = endField.getText();
        float distance;
        try {
            distance = Float.parseFloat(distanceField.getText());
        } catch (Exception ex) {
            showError("Cuidado con la distancia", distanceField);
            return false;
        }
        if (start.length() < 5 || start.length() > 50) {
            showError("¿Dónde comienza?", startField);
            return false;
        }

        if (end.length() < 5 || end.length() > 50) {
            showError("¿Hasta dónde?", endField);
            return false;
        }

        if (distance < 0) showError("¿Vas a viajar en reversa?", distanceField);
        else if (distance < 0.3) showError("Para un servicio así mejor ve a pie", distanceField);
        else if (distance > 35)
            showError("Sólo se puede viajar 35 km como máximo (SanBer hasta Fusa)", distanceField);

        return true;
    }

    public int getExit_code() {
        return exit_code;
    }

    // Evento de los botones
    @Override
    public void actionPerformed(ActionEvent e) {
        // Crear pedido
        if ((e.getSource() == orderButton || e.getSource() == distanceField) && checkEntrys()){

            // Genera el pedido
            int id_recent_order = OperationsCRUD.createOrder(startField.getText(),
                    endField.getText(),
                    Integer.parseInt(distanceField.getText()),
                    Integer.parseInt(distanceField.getText()) * 7000,
                    id_client);
            exit_code = id_recent_order;
            switch (id_recent_order){
                case -1:
                    showError("Error interno", distanceField);
                    exit_code = 0;
                    break;
                case -2:
                    showError("No se registró el pedido", distanceField);
                    break;
                case -3:
                    showError("Error SQL", distanceField);
                    break;
                default:
                    dispose();
            }
        }

        // Botón de cancelar
        if (e.getSource() == cancelButton){
            if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(this,
                    "¿Estás seguro de que ya no necesitas un servicio?", "¿Seguro?",
                    JOptionPane.YES_NO_OPTION)) {
                exit_code = -4;
                dispose();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UberOrderGUI uberOGUI = new UberOrderGUI(null, 2);
        });
    }
}
