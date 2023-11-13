import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * Ventana del cliente para mostrar información del pedido,
 * estado del pedido y poder realizar un pedido.
 * TODO:
     * Poder dejar copiar el número actual
     * Hacer un WaitTemplate para manejar las esperas
     * Remover código espagueti
     * Deshabilitar el botón de acción en tiempos determinados
     * Guardar datos del Usuario
 */
public class UberClientGUI extends JFrame implements ActionListener {
    private String id_client, nameClient, driver_id;
    private boolean hasAvaliableOrder;
    private JLabel titleLabel, infoLabel;
    private JSeparator separatorTitle;
    private JButton actionButton, logoutButton, closeButton, chatButton;
    private ResultSet clientInfo;

    private int order_id;
    private ScheduledExecutorService executor;

    // Constructor
    public UberClientGUI(String id_client){
        this.id_client = id_client;
        clientInfo = OperationsCRUD.getClientInfo(id_client); // Obtiene la información del cliente conectado

        try {
            // El nombre también lo obtiene.
            nameClient =
                    Objects.requireNonNull(clientInfo).getString("Cli_Nombre").split("\\s+")[0];

            OperationsCRUD.UpdateIp("Cliente", Integer.parseInt(id_client));

            hasAvaliableOrder = Objects.equals(clientInfo.getString("Cli_Pedido"), "1"); // Y su estado actual.
        } catch (SQLException e) {
            dispose(); // TODO: Mostrar un error en pantalla.
        }


        titleLabel = new JLabel("¿Qué tal, " + nameClient + "?");
        titleLabel.setBounds(10, 10, 400, 40);
        add(titleLabel);

        separatorTitle = new JSeparator();
        add(separatorTitle);

        infoLabel = new JLabel("""
                <html>
                    <p style='font-size: 100px; text-align: center;'>🔄️
                        <p style='font-size: 50px'> <br> </p>
                    </p>
                    <p style='text-align: center;'>
                        Cargando datos. Espera un momento, por favor...
                    </p>
                </html>
                """);
        infoLabel.setBounds(10, 60, 400, 590);
        add(infoLabel);

        chatButton = new JButton("Chat");
        chatButton.addActionListener(this);
        add(chatButton);
        chatButton.setBounds(10, 655,195, 50);


        actionButton = new JButton("Cancelar");
        actionButton.addActionListener(this);
        add(actionButton);
        actionButton.setBounds(10, 655, 400, 50);


        logoutButton = new JButton("Cerrar sesión");
        logoutButton.addActionListener(this);
        logoutButton.setBounds(10, 710, 195, 50);
        add(logoutButton);

        closeButton = new JButton("Salir");
        closeButton.addActionListener(this);
        closeButton.setBounds(215, 710, 195, 50);
        add(closeButton);

        new Styles(this, titleLabel, separatorTitle);
        remove(chatButton);

        // Crea un hilo programado cada 10 segundos con la función UpdateInterface.
        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(this::updateInterface, 0, 10, TimeUnit.SECONDS);


        // Propios de la ventana
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Ubernardo: Cliente");
        setSize(430,800);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Actualiza la información en pantalla
    private void updateInterface(){
        try {
            clientInfo = OperationsCRUD.getClientInfo(id_client); // Obtiene de nuevo información del cliente para
            //                                                      conocer su estado actual nuevamente.
            hasAvaliableOrder =
                    Objects.equals(Objects.requireNonNull(clientInfo).getString("Cli_Pedido"), "1");
            //      Verifica si tiene un pedido actual

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Muestra información respecto al pedido
        if (hasAvaliableOrder){
            infoLabel.setText(formatOrder()); // Muestra la orden

            ResultSet order = OperationsCRUD.getOrderInfoByClient(id_client);
            String order_status;
            try {
                order_status = order.getString("Ped_Estado");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            if (Objects.equals(order_status, "Tomado")){
                add(chatButton);
                actionButton.setBounds(215, 655, 195, 50);
            }
            actionButton.setText("Cancelar"); // Cambia el nombre del botón de acción
        } else {
            remove(chatButton);
            actionButton.setBounds(10, 655, 400, 50);
            infoLabel.setText("""
                    <html>
                        <p style='font-size: 100px; text-align: center;'>🚗❔
                            <p style='font-size: 50px'> <br> </p>
                        </p>
                        <p style='text-align: center;'>
                            Por el momento no has solicitado ningún servicio.
                            Presiona el botón <b><i>Pedir</i></b> para realizar tu pedido ahora mismo.
                        </p>
                        </html>
                    """);
            actionButton.setText("Pedir");
        }
    }

    // Diseña el string en HTML para mostrar el estado de la orden
    private String formatOrder(){
        // Obtiene información del pedido
        ResultSet order = OperationsCRUD.getOrderInfoByClient(id_client);

        // TODO: Estas variables sobran pero no sé como quitarlas
        String order_status = null, driver_info_formatted = null, start_place = null, end_place = null,
                distance_order = null, cost_order = null,
            date_order = null, time_order = null;

        try {
            // Obtiene el estado del pedido para ver si está pedido o tomado.
            order_id = order.getInt("Ped_Id");
            order_status = order.getString("Ped_Estado");
            switch (order_status) {
                case "Pedido":
                    order_status = "🚩 Esperando que algún conductor lo tome";
                    driver_info_formatted = "<p> &nbsp;&nbsp;&nbsp;&nbsp;🕒 Luego lo sabrás</p>";

                    break;
                case "Tomado":
                    // Agrega información del conductor
                    order_status = "🛣️ Ya va un conductor por tí";
                    driver_id = order.getString("Con_Id");
                    ResultSet driverInfo = OperationsCRUD.getDriverInfo(driver_id);
                    String placa_driver = driverInfo.getString("Con_Placa");
                    String color_driver = driverInfo.getString("Con_Color");
                    String name_driver = driverInfo.getString("Con_Nombre");
                    String phone_driver = driverInfo.getString("Con_Numero");
                    driver_info_formatted = """
                            <p> &nbsp;&nbsp;&nbsp;&nbsp;🚗 %s
                            <i style='color: #bbbbb2; font-size: 15px'> de color </i>%s</p>
                            <p> &nbsp;&nbsp;&nbsp;&nbsp;👤 %s</p>
                            <p> &nbsp;&nbsp;&nbsp;&nbsp;📞 %s</p>
                            """.formatted(placa_driver.replaceAll("(\\p{L}+)(\\p{N}+)", "$1-$2"),
                                        color_driver, name_driver, phone_driver);
                    break;
                default:
                    try {
                        throw new
                                IllegalStateException(
                                        "Unexpected value: " + order.getString("Ped_Estado"));
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }


            }

            // OBtiene información del pedido
            start_place = order.getString("Ped_LugarInicio");
            end_place = order.getString("Ped_LugarLlegada");
            distance_order = order.getString("Ped_Distancia");
            cost_order = order.getString("Ped_Costo");
            date_order = orderDateToString(order);
            time_order = orderTimeToString(order);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return """
                <html>
                        <h1> Información de tu pedido </h1>
                        <h2> ¿Cómo va tu pedido? </h2>
                            <p> &nbsp;&nbsp;&nbsp;&nbsp;%s</p>
                        <h2> ¿Quién y cómo te va a llevar? </h2>
                             %s
                        <h2> ¿Dónde es hoy? </h2>
                            <p> &nbsp;&nbsp;&nbsp;&nbsp;%s ➡️ %s</p>
                            <p> &nbsp;&nbsp;&nbsp;&nbsp;📏 %s km</p>
                        <h2> ¿Cuánto duele? </h2>
                            <p> &nbsp;&nbsp;&nbsp;&nbsp;💲%s <i style='color: #aaaaa2;'>($7000/km)</i></p>
                        <h2> Llevas esperando desde el</h2>
                            <p> &nbsp;&nbsp;&nbsp;&nbsp;🕒 %s
                                <i style='color: #bbbbb2; font-size: 12px'> a las </i>%s</p>
                        <p style='text-align: center;'>
                            <br>¿Pasa algo? Presiona el botón <b><i>Cancelar</i></b> si lo necesitas
                        </p>
                <html>
                """.formatted(order_status, driver_info_formatted, start_place, end_place,
                distance_order, cost_order, date_order, time_order);
    }

    // Formatea el tiempo de la orden a String
    public static String orderTimeToString(ResultSet order) throws SQLException {
        return new SimpleDateFormat("hh:mm a").format(
                new java.util.Date(order.getTime("Ped_Hora").getTime()));
    }

    // Formatea la fecha de la orden a String
    public static String orderDateToString(ResultSet order) throws SQLException {
        return new SimpleDateFormat("d 'de' MMMM").format(order.getDate("Ped_Fecha"));
    }


    // Evento de botones
    @Override
    public void actionPerformed(ActionEvent e) {
        // Botón de acción (Cancelar o pedir)
        if (e.getSource() == actionButton) {
            switch (actionButton.getText()) {
                
                case "Pedir":
                    // Invoca un cuadro de diálogo para pedir un servicio
                    SwingUtilities.invokeLater(() -> {
                        UberOrderGUI uberOGUI = new UberOrderGUI(this, Integer.parseInt(id_client));
                        int dialog_exit_code = uberOGUI.getExit_code();
                        if (dialog_exit_code != -4) {
                            hasAvaliableOrder = true;
                            infoLabel.setText("""
                                    <html>
                                        <p style='font-size: 100px; text-align: center;'>✅
                                            <p style='font-size: 50px'> <br> </p>
                                        </p>
                                        <p style='text-align: center;'>
                                            Ya quedó tu pedido. Solo espera un tan y listo...
                                        </p>
                                    </html>
                                    """);
                        }
                    });
                    break;

                case "Cancelar":
                    // Asegura la salida
                    int confirmExit =
                        JOptionPane.showConfirmDialog(this, "¿A lo bien ya no lo necesita?",
                                "¿En serio?", JOptionPane.YES_NO_OPTION);

                    if (confirmExit == JOptionPane.YES_OPTION){
                        // Ejecuta la cancelación del pedido.
                        int delete_code = OperationsCRUD.updateOrderStatusByClient(Integer.parseInt(id_client),
                                "CanceladoCli");
                        switch (delete_code){
                            case 0:
                                JOptionPane.showMessageDialog(this,
                                        "Listo, ya se abrió el pedido",
                                        "Hecho",
                                        JOptionPane.INFORMATION_MESSAGE);
                                break;
                            case -1:
                                JOptionPane.showMessageDialog(this,
                                        "Problema al cancelar el pedido",
                                        "Error",
                                        JOptionPane.ERROR_MESSAGE);
                                break;

                        }
                    }

                    break;

                default:
                    throw new IllegalStateException("Unexpected value: " + actionButton.getText());
            }
            // Actualiza la interfaz
            updateInterface();
        }

        if (e.getSource() == chatButton){
            new UberChatHandler(this,
                    "Cliente",
                    Integer.parseInt(id_client),
                    Integer.parseInt(driver_id));
        }



        // Cerrar sesión
        if (e.getSource() == logoutButton){
            executor.shutdownNow();
            dispose();
            MainWindow mWindow = new MainWindow();
            mWindow.setVisible(false);
            UberLoginGUI uberLGUI = new UberLoginGUI(mWindow);
        }

        // Salir
        if (e.getSource() == closeButton){
            executor.shutdownNow();
            dispose();
        }

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UberClientGUI uberCCGUI = new UberClientGUI("1");
        });
    }
}
