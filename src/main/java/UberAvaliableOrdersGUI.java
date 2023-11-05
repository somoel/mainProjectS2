import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

/*
    Cuadro de diálogo para mostrar pedidos disponibles para
    ser tomados por el conductor.

TODO: Moverse entre pedidos con flecha arriba y flecha abajo
    Aumentar la sensibilidad de la rueda del ratón.
    Handlear cuando no haya ningún pedido
 */
public class UberAvaliableOrdersGUI extends JDialog{

    private JLabel titleLabel, ordersLabel;
    private JEditorPane[] edPanes;
    private JPanel availableOptionsPanel;
    private JButton takeOrderButton, cancelButton;
    private JSeparator separatorTitle;

    // Constructor
    public UberAvaliableOrdersGUI(JFrame backFrame) {
        super(backFrame, "Pedidos Disponibles", true);

        titleLabel = new JLabel("A chambear");
        titleLabel.setBounds(10, 10, 400, 40);
        add(titleLabel);

        ordersLabel = new JLabel("Estos son los pedidos disponibles");
        ordersLabel.setBounds(10, 60, 400, 30);
        add(ordersLabel);

        separatorTitle = new JSeparator();
        add(separatorTitle);

        // Panel para mostrar enlistados cada elemento
        availableOptionsPanel = new JPanel();
        availableOptionsPanel.setLayout(new BoxLayout(availableOptionsPanel, BoxLayout.Y_AXIS));
        // Funcionará como un box layout ^
        availableOptionsPanel.setBounds(10, 100, 380, 200);

        // Arreglo para cada elemento obtenido de los pedidos disponibles
        edPanes = new JEditorPane[10];
        for (int i = 0; i < edPanes.length; i++) {
            // Configuración de cada pedido
            edPanes[i] = new JEditorPane();
            edPanes[i].setContentType("text/html");
            edPanes[i].setText(formatAvaliableOrder());
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

        scrollPane.setBounds(10, 100, 400, 300);
        add(scrollPane);

        SwingUtilities.invokeLater(() -> scrollPane.getVerticalScrollBar().setValue(0));


        takeOrderButton = new JButton("Voy por ese");
        takeOrderButton.setBounds(10, 405, 195, 50);
        takeOrderButton.addActionListener(e -> {
            for (JEditorPane edPane: edPanes){
                if (edPane.getBackground() == Styles.offOrange){
                    System.out.println("pedido tomado: " + edPane.getText());
                }
            }
        });
        takeOrderButton.setEnabled(false);
        add(takeOrderButton);

        cancelButton = new JButton("Ya no");
        cancelButton.setBounds(215, 405, 195, 50);
        cancelButton.addActionListener(e -> dispose());
        add(cancelButton);


        Styles styles = new Styles(this, titleLabel, separatorTitle);

        // Propios de la ventana
        setLayout(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(430,500);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Aplica código HTML y formato establecido al pedido
    private String formatAvaliableOrder(){
        return """
                <html><div style='font-size: 15px; font-family: "Product Sans", Roboto;'>
                        📍 EL MIRADOR ➡️ LOS SAUCES<br>
                        💲3500 &nbsp;&nbsp;&nbsp;
                        🕒 3<i style='font-size: 12px'> de </i>Nov<i style='font-size: 12px'> a las </i>12:34
                      </div></html>
                """;
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UberAvaliableOrdersGUI uberAOGUI = new UberAvaliableOrdersGUI(null);
        });
    }

}
