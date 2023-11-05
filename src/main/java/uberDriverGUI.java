import javax.swing.*;

/*
    Ventana principal del conductor para tomar pedidos, ver pedido
    y realizar acciones

    TODO: Falta el cancelar el pedido actual
 */
public class uberDriverGUI extends JFrame{
    private String id_driver, nameDriver = "(nombre)", placaUber, colorUber, nameUber, phoneUber,
            inicioViaje, finViaje, kmViaje, costoViaje, horaViaje, fechaViaje;
    private boolean actual_status;
    private JLabel titleLabel, statusLabel, infoLabel;
    private JSeparator separatorTitle;
    private JButton actionButton, logoutButton, closeButton;

    // Constructor
    public uberDriverGUI(String id_driver){
        this.id_driver = id_driver;

        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Conductor");

        titleLabel = new JLabel("¿Todo bien, " + nameDriver + "?");
        titleLabel.setBounds(10, 10, 400, 40);
        add(titleLabel);

        separatorTitle = new JSeparator();
        add(separatorTitle);

        statusLabel = new JLabel("No has tomado ningún pedido");
        statusLabel.setVerticalAlignment(SwingConstants.BOTTOM);
        statusLabel.setBounds(10, 60, 400, 30);
        add(statusLabel);

        infoLabel = new JLabel("<html>" +
                "<p style='font-size: 100px; text-align: center;'>🚗❔<p style='font-size: 50px'><<br></p></p>" +
                "<p style='text-align: center;'>" +
                    "Presiona el botón <b><i>Tomar Pedido</i></b> para ver la lista de pedidos disponibles" +
                "</p>" +
                "</html>");
        infoLabel.setVerticalAlignment(SwingConstants.BOTTOM);
        infoLabel.setBounds(10, 90, 400, 400);
        add(infoLabel);


        actionButton = new JButton("Tomar Pedido");
        actionButton.addActionListener(e -> SwingUtilities.invokeLater(() -> {
            UberAvaliableOrdersGUI uberAOGUI = new UberAvaliableOrdersGUI(this);
            uberAOGUI.setVisible(true);
        }));
        add(actionButton);
        actionButton.setBounds(10, 555, 400, 50);


        logoutButton = new JButton("Cerrar sesión");
        logoutButton.addActionListener(null);
        logoutButton.setBounds(10, 610, 195, 50);
        add(logoutButton);

        closeButton = new JButton("Salir");
        closeButton.addActionListener(null);
        closeButton.setBounds(215, 610, 195, 50);
        add(closeButton);

        new Styles(this, titleLabel, separatorTitle);
    }

    // Diseña el string en HTML para mostrar el estado de la orden
    private String formatOrder(){
        return """
                <html>
                        <p>📍<i style='color: #bbbbb2; font-size: 12px'>Desde </i>EL MIRADOR </p>
                        <p>&nbsp;⬇️</p>
                        <p>📍 <i style='color: #bbbbb2; font-size: 12px'>Hacia </i>LOS SAUCES</p>
                        <p>💲3500 <i style='color: #aaaaa2;'>($7000/km)</i></p>
                        <p>👤KEVIN VIASUS</p>
                        <p>📞333 222 9889</p>
                        <p>📏 0.5 km</p>
                        <p>🕒 3 de Nov<i style='color: #bbbbb2; font-size: 12px'> a las </i>12:34</p>
                        <p style='color: #fffff2; font-size: 10px'>
                             ------------------------------------------------------------------------
                        </p>
                <html>
                """;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            uberDriverGUI uberDGUI = new uberDriverGUI("0");
            uberDGUI.setBounds(0, 0, 430, 700);
            uberDGUI.setLocationRelativeTo(null);
            uberDGUI.setVisible(true);
        });
    }
}
