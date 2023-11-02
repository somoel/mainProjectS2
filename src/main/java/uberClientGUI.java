import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;


/*
Ventana del cliente para mostrar información del pedido,
estado del pedido y poder realizar un pedido.
 */
/* TODO:
        Poder dejar copiar el número actual
        Mostrar color de fondo del color del auto
        Rehacer el tamaño en base al contenido
 */
public class uberClientGUI extends JFrame implements ActionListener {
    private String cedulaLoged, nameClient, placaUber, colorUber, nameUber, phoneUber,
            inicioViaje, finViaje, kmViaje, costoViaje, horaViaje, fechaViaje;
    private boolean actual_status;
    private JLabel titleLabel, statusLabel, infoLabel;
    private JFrame backFrame;
    private JSeparator separatorTitle;
    private JButton actionButton, logoutButton, closeButton;

    // Constructor
    public uberClientGUI(String cedulaLoged, JFrame backFrame){
        this.cedulaLoged = cedulaLoged;

        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Cliente");

        titleLabel = new JLabel("¿Qué tal, " + nameClient + "?");
        titleLabel.setBounds(10, 10, 400, 40);
        add(titleLabel);

        separatorTitle = new JSeparator();
        add(separatorTitle);

        statusLabel = new JLabel("Este es tu pedido");
        statusLabel.setVerticalAlignment(SwingConstants.BOTTOM);
        statusLabel.setBounds(10, 60, 400, 30);
        add(statusLabel);

        infoLabel = new JLabel(formatOrder());
        infoLabel.setVerticalAlignment(SwingConstants.BOTTOM);
        infoLabel.setBounds(10, 90, 400, 400);
        add(infoLabel);


        actionButton = new JButton("Cancelar");
        actionButton.addActionListener(this);
        add(actionButton);
        actionButton.setBounds(10, 555, 400, 50);


        logoutButton = new JButton("Cerrar sesión");
        logoutButton.addActionListener(this);
        logoutButton.setBounds(10, 610, 195, 50);
        add(logoutButton);

        closeButton = new JButton("Salir");
        closeButton.addActionListener(this);
        closeButton.setBounds(215, 610, 195, 50);
        add(closeButton);

        new Styles(this, titleLabel, separatorTitle);
    }

    // Diseña el string en HTML para mostrar el estado de la orden
    private String formatOrder(){
        String white_text = new String(new char[167]).replace("\\0", "-");
        System.out.println(white_text);
        return """
                <html>
                        <p>🚗 <span style='background-color: yellow;'>ABC 123</span><i style='color: #bbbbb2; font-size: 15px'> de color </i> AZUL</p>
                        <p>👤MAICOL RONCANCIO</p>
                        <p>📞333 222 9889</p>
                        <p>📍<i style='color: #bbbbb2; font-size: 12px'>Desde </i>EL MIRADOR </p>
                        <p>&nbsp;⬇️</p>
                        <p>📍 <i style='color: #bbbbb2; font-size: 12px'>Hacia </i>LOS SAUCES</p>
                        <p>📏 0.5 km</p>
                        <p>💲3500 <i style='color: #aaaaa2;'>($7000/km)</i></p>
                        <p>🕒 3 de Nov<i style='color: #bbbbb2; font-size: 12px'> a las </i>12:34</p>
                        <p style='color: #fffff2; font-size: 10px'>
                             ------------------------------------------------------------------------
                        </p>
                <html>
                """;
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == actionButton && Objects.equals(actionButton.getText(), "Cancelar")){
            SwingUtilities.invokeLater(() -> {
                uberOrderGUI uberOGUI = new uberOrderGUI(this);
                uberOGUI.setBounds(0, 0, 430, 420);
                uberOGUI.setLocationRelativeTo(null);
                uberOGUI.setVisible(true);
            });
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            uberClientGUI uberCCGUI = new uberClientGUI("0", null);
            uberCCGUI.setBounds(0, 0, 430, 700);
            uberCCGUI.setLocationRelativeTo(null);
            uberCCGUI.setVisible(true);
        });
    }
}
