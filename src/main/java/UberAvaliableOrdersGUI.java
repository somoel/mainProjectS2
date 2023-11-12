import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

/*
    Cuadro de diálogo para mostrar pedidos disponibles para
    ser tomados por el conductor.

TODO: Moverse entre pedidos con flecha arriba y flecha abajo
    Aumentar la sensibilidad de la rueda del ratón.
    Handlear cuando no haya ningún pedido
 */
public class UberAvaliableOrdersGUI extends JDialog implements ActionListener{

    private JLabel titleLabel, ordersLabel;
    private JEditorPane[] edPanes;
    private JPanel availableOptionsPanel;
    private JButton takeOrderButton, cancelButton;
    private JSeparator separatorTitle;

    private int exitCode;
    private ResultSet avaliableOrders;

    // Constructor
    public UberAvaliableOrdersGUI(JFrame backFrame) {
        super(backFrame, "Pedidos Disponibles", true);

        titleLabel = new JLabel("A chambear");
        titleLabel.setBounds(10, 10, 450, 40);
        add(titleLabel);

        ordersLabel = new JLabel("Estos son los pedidos disponibles");
        ordersLabel.setBounds(10, 60, 450, 30);
        add(ordersLabel);

        separatorTitle = new JSeparator();
        add(separatorTitle);

        // Panel para mostrar enlistados cada elemento
        availableOptionsPanel = new JPanel();
        availableOptionsPanel.setLayout(new BoxLayout(availableOptionsPanel, BoxLayout.Y_AXIS));
        // Funcionará como un box layout

        availableOptionsPanel.setBounds(10, 100, 430, 200);

        avaliableOrders = OperationsCRUD.getAvaliableOrders();
        int countAO = 0;
        try {
            while (Objects.requireNonNull(avaliableOrders).next()) {
                countAO++;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


        // Arreglo para cada elemento obtenido de los pedidos disponibles
        edPanes = new JEditorPane[countAO];

        for (int i = 0; i < edPanes.length; i++) {
            // Configuración de cada pedido
            edPanes[i] = new JEditorPane();
            edPanes[i].setContentType("text/html");
            edPanes[i].setOpaque(true);
            edPanes[i].setBackground(Styles.boneWhite);
            edPanes[i].setEditable(false);
            edPanes[i].setBorder(new EmptyBorder(10, 0, 10, 0));


            int finalI = i;

            // Eventos del mouse para subrayar el seleccionado
            edPanes[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    for (JEditorPane edPane : edPanes){
                        edPane.setBackground(Styles.boneWhite);
                    }
                    edPanes[finalI].setBackground(Styles.offOrange);
                    takeOrderButton.setEnabled(true);

                }
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (edPanes[finalI].getBackground() != Styles.offOrange)
                        edPanes[finalI].setBackground(Color.LIGHT_GRAY);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if (edPanes[finalI].getBackground() != Styles.offOrange) {
                        edPanes[finalI].setBackground(Styles.boneWhite);
                    }
                }

            });

            availableOptionsPanel.add(edPanes[i]);
        }

        // Agrega la información de cada pedido disponible a cada edPane

        avaliableOrders = OperationsCRUD.getAvaliableOrders();

        try {
            for (JEditorPane edPane: edPanes){
                avaliableOrders.next();
                int orderId = avaliableOrders.getInt("Ped_Id");
                String start = avaliableOrders.getString("Ped_LugarInicio");
                String end = avaliableOrders.getString("Ped_LugarLlegada");
                String cost = avaliableOrders.getString("Ped_Costo");
                String date = UberClientGUI.orderDateToString(avaliableOrders);
                String time = UberClientGUI.orderTimeToString(avaliableOrders);
                edPane.setText(formatAvaliableOrder(start, end, cost, date, time));
                edPane.putClientProperty("Ped_Id", orderId);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Scrollpane para desplazarse entre pedidos disponibles
        JScrollPane scrollPane = new JScrollPane(availableOptionsPanel);

        // Configuración del scrollpane
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        ChatUtilities.CustomScrollBarUI customScrollBarUI = new ChatUtilities.CustomScrollBarUI();
        scrollPane.getVerticalScrollBar().setUI(customScrollBarUI);
        scrollPane.addMouseWheelListener(e -> {
            JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
            verticalScrollBar.setValue(
                    verticalScrollBar.getValue() + e.getWheelRotation() * verticalScrollBar.getUnitIncrement()
            );
        });

        scrollPane.setBounds(10, 100, 450, 300);
        add(scrollPane);

        SwingUtilities.invokeLater(() -> scrollPane.getVerticalScrollBar().setValue(0));


        takeOrderButton = new JButton("Voy por ese");
        takeOrderButton.setBounds(10, 405, 220, 50);
        takeOrderButton.addActionListener(this);
        takeOrderButton.setEnabled(false);
        add(takeOrderButton);

        cancelButton = new JButton("Ya no");
        cancelButton.setBounds(240, 405, 220, 50);
        cancelButton.addActionListener(this);
        add(cancelButton);


        Styles styles = new Styles(this, titleLabel, separatorTitle);

        // Propios de la ventana
        setLayout(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(480,500);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Aplica código HTML y formato establecido al pedido
    private String formatAvaliableOrder(String start, String end, String cost, String date, String time){
        return """
            <html><div style='font-size: 15px; font-family: "Product Sans", Roboto; text-align: center;'>
                    %s  ➡️  %s<br>
                    💲%s &nbsp;&nbsp;&nbsp;
                    🕒  %s<i style='font-size: 12px'> a las </i>%s
                  </div></html>
            """.formatted(start, end, cost, date, time);
    }


    // Obtiene el código de salida al cerrar el cuadro de diálogo
    public int getExitCode() {
        return exitCode;
    }

    // Botones
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == takeOrderButton){
            for (JEditorPane edPane : edPanes) {
                if (edPane.getBackground() == Styles.offOrange) {
                    exitCode = (int) edPane.getClientProperty("Ped_Id");
                    dispose();
                }
            }
        }

        if (e.getSource() == cancelButton){
            exitCode = 0;
            dispose();
        }

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UberAvaliableOrdersGUI uberAOGUI = new UberAvaliableOrdersGUI(null);
        });
    }

}
