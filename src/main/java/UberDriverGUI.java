import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/*
    Ventana principal del conductor para tomar pedidos, ver pedido
    y realizar acciones

    TODO: Mostrar pantalla de carga pausando el actualizador
 */
public class UberDriverGUI extends JFrame implements ActionListener {
    private String id_driver, nameDriver = "(nombre)";
    private boolean actual_status;
    private JLabel titleLabel, infoLabel;
    private JSeparator separatorTitle;
    private JButton actionButton, logoutButton, closeButton;
    private ResultSet driverInfo, orderInfo;
    private boolean hasOrder;
    private ScheduledExecutorService executor;
    private int order_id;

    // Constructor
    public UberDriverGUI(String id_driver){
        this.id_driver = id_driver;
        driverInfo = OperationsCRUD.getDriverInfo(id_driver);

        try {
            // El nombre también lo obtiene.
            nameDriver =
                    Objects.requireNonNull(driverInfo).getString("Con_Nombre").split("\\s+")[0];
            hasOrder = Objects.equals(driverInfo.getString("Con_Pedido"), "1"); // Y su estado actual.
        } catch (SQLException e) {
            dispose(); // TODO: Mostrar un error en pantalla.
        }


        titleLabel = new JLabel("¿Todo bien, " + nameDriver + "?");
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


        actionButton = new JButton("Tomar Pedido");
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

        // Crea un hilo programado cada 10 segundos con la función UpdateInterface.
        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(this::updateInterface, 0, 20, TimeUnit.SECONDS);

        // Propios de la ventana
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Ubernardo: Conductor");
        setSize(430,800);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Actualiza la interfaz de usuario con base en su pedido actual.
    private void updateInterface(){
        String order_status = null;
        try {
            driverInfo = OperationsCRUD.getDriverInfo(id_driver); // Obtiene de nuevo información del cliente para
            //                                                      conocer su estado actual nuevamente.
            orderInfo = OperationsCRUD.getOrderInfoByDriver(id_driver);
            hasOrder = Objects.equals(driverInfo.getString("Con_Pedido"), "1");
            //      Verifica si tiene un pedido actual
            if (hasOrder) {
                order_status = orderInfo.getString("Ped_Estado");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (Objects.equals(order_status, "Tomado")) {
            actionButton.setText("Cancelar"); // Cambia el nombre del botón de acción
        } else {
            actionButton.setText("Tomar Pedido");
        }

        // Muestra información respecto al pedido
        if (hasOrder){
            infoLabel.setText(formatOrder()); // Muestra la orden

        } else {
            // Asiga el texto por defecto cuando no ha tomado ningún pedido
            infoLabel.setText("""
                    <html>
                        <p style='font-size: 100px; text-align: center;'>🚗❔
                            <p style='font-size: 50px'> <br> </p>
                        </p>
                        <p style='text-align: center;'>
                            Por el momento no has tomado ningún pedido.
                            Presiona el botón <b><i>Tomar Pedido</i></b> para observar la lista de pedidos
                             actualmente disponibles
                        </p>
                        </html>
                    """);
            actionButton.setText("Tomar Pedido");
        }
    }


    // Diseña el string en HTML para mostrar el estado de la orden
    private String formatOrder(){
        ResultSet order = OperationsCRUD.getOrderInfoByDriver(id_driver);

        String order_status = null, start_place = null, end_place = null,
                distance_order = null, cost_order = null,
                date_order = null, time_order = null, name_client = null, phone_client = null;

        try {
            order_id = order.getInt("Ped_Id");
            order_status = switch (order.getString("Ped_Estado")) {
                case "Tomado" -> "🚘  Estás en camino";
                case "Finalizado" -> "🏁  Completada la chamba";
                case "CanceladoCon" -> "❌  Lo cancelaste";
                case "CanceladoCli" -> "❌  Ya no lo necesita el cliente";
                default -> "ERROR";
            };

            start_place = order.getString("Ped_LugarInicio");
            end_place = order.getString("Ped_LugarLlegada");
            distance_order = order.getString("Ped_Distancia");

            String client_id = order.getString("Cli_Id");
            ResultSet clientInfo = OperationsCRUD.getClientInfo(client_id);
            name_client = clientInfo.getString("Cli_Nombre");
            phone_client = clientInfo.getString("Cli_Numero");

            cost_order = order.getString("Ped_Costo");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return """
                <html>
                    <h1> Información del pedido que tomaste </h1>
                    <h2> ¿Cómo va el pedido? </h2>
                        <p> &nbsp;&nbsp;&nbsp;&nbsp;%s</p>
                    <h2> ¿Dónde toca? </h2>
                        <p> &nbsp;&nbsp;&nbsp;&nbsp;%s ➡️ %s</p>
                        <p> &nbsp;&nbsp;&nbsp;&nbsp;📏 %s km</p>
                    <h2> ¿Por quién vas? </h2>
                        <p> &nbsp;&nbsp;&nbsp;&nbsp;👤 %s</p>
                        <p> &nbsp;&nbsp;&nbsp;&nbsp;📞 %s</p>
                    <h2> ¿Y la platica? </h2>
                        <p> &nbsp;&nbsp;&nbsp;&nbsp;💲%s <i style='color: #aaaaa2;'>($7000/km)</i></p>
                <html>
                """.formatted(order_status, start_place, end_place,
                distance_order, name_client, phone_client, cost_order);
    }


    // Evento de los botones
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == actionButton){
            int update_code;
            switch (actionButton.getText()){
                case "Tomar Pedido":
                    // Muestra la ventana de órdenes dispoibles
                    UberAvaliableOrdersGUI uberAOGUI = new UberAvaliableOrdersGUI(this);
                    order_id = uberAOGUI.getExitCode();
                    update_code =
                            OperationsCRUD.updateOrderStatusByDriver(
                                    Integer.parseInt(id_driver),
                                    order_id,
                                    "Tomado");

                    if (update_code != 0){
                        JOptionPane.showMessageDialog(this,
                                "Algo no funcionó",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    } else {
                        updateInterface();
                    }
                    break;

                case "Cancelar":
                    update_code = OperationsCRUD.updateOrderStatusByDriver(
                            Integer.parseInt(id_driver),
                            order_id,
                            "CanceladoCon"
                    );

                    if (update_code != 0){
                        JOptionPane.showMessageDialog(this,
                                "Algo no funcionó",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    } else {
                        updateInterface();
                    }
                    break;


                default:
                    throw new IllegalStateException("Unexpected value: " + actionButton.getText());
            }
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
            UberDriverGUI uberDGUI = new UberDriverGUI("2");
        });
    }

}
