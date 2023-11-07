import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Se realiza tod0 el CRUD con la base de datos.
 * TODO: Revisar la conexión a internet.
 */
public class OperationsCRUD {

    // Constantes para la conexión con la base de datos.
    private static final String URL = "jdbc:mysql://sql10.freesqldatabase.com:3306/sql10659471";
    private static final String USUARIO = "sql10659471";
    private static final String CONTRASENA = "8sX7wd4iRk";

    // Conexión con la base de datos.
    private static Connection con;

    // Crea la conexión con la base de datos.
    // TODO: Evitar que se invoque en cada CRUD.
    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos: " + e.getMessage());
        }
        return connection; // Devuelve la conexión
    }

    // Cierra la conexión actual y el ResultSet
    public static void closeResSetAndCon(ResultSet rs) {
        try {
            rs.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Valida el inicio de sesión
    public static int validateLogin(String userType, String cedula, String password) {
        // Prepara el query
        con = getConnection();
        String shortType = userType.substring(0, 3) + "_";
        String id = shortType + "Id";
        String cedCol = shortType + "Cedula";
        String passCol = shortType + "Pass";

        String query = "SELECT " + id + " FROM " + userType + " WHERE " + cedCol + " = ? AND " + passCol + " = ?";

        try {
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, cedula);
            statement.setString(2, password);

            ResultSet resultado = statement.executeQuery(); // Lo ejecuta

            if (resultado.next()) {
                return resultado.getInt(id); // Devuelve el ID del usuario si las credenciales son correctas
            } else {
                return -1; // Retorna -1 si las credenciales son incorrectas
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -2; // En caso de error, retorna -2
        } catch (NullPointerException e) {
            e.printStackTrace();
            return -3; // Si no hay conexión a internet, retorna -3
        }
    }

    // Registra un usuario con todas sus características
    public static int registerUser(String userType, String name, String cedula,
                                   String email, String phone, String placa,
                                   String color, String pass) {
        // Preparar la consulta
        con = getConnection();
        String shortType = userType.substring(0, 3);

        String query;

        // Separa la consulta por Cliente o Conductor
        if (userType.equals("Cliente")) {
            query = "INSERT INTO Cliente (Cli_Cedula, Cli_Nombre, Cli_Pass, Cli_Numero, Cli_Pedido, Cli_Email)" +
                    "VALUES (?, ?, ?, ?, ?, ?)";


        } else {
            query = "INSERT INTO Conductor (Con_Cedula, Con_Nombre, Con_Pass, Con_Numero, Con_Pedido, Con_Email," +
                    " Con_Placa, Con_Color) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        }

        try {
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, cedula);
            statement.setString(2, name);
            statement.setString(3, pass);
            statement.setString(4, phone);
            statement.setString(5, "0");
            statement.setString(6, email);
            if (userType.equals("Conductor")) {
                statement.setString(7, placa);
                statement.setString(8, color);
            }

            // La ejecuta
            if (statement.executeUpdate() > 0) {
                return 0; // Si funciona
            } else {
                return -1; // Si no creó ningúna fila
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return -2; // Si ocurre un error SQL
        }
    }

    // Obtiene información del cliente por su ID
    public static ResultSet getClientInfo(String id) {
        con = getConnection();
        String query = "SELECT * FROM Cliente WHERE Cli_Id = ?";
        return QueryToResultSet(id, query);
    }

    // Obtiene información del pedido vigente por ID del cliente.
    public static ResultSet getOrderInfoByClient(String idCli) {
        con = getConnection();
        String query = "SELECT * FROM Pedido WHERE Cli_Id = ? " +
                "AND (Ped_Estado != 'Finalizado' AND Ped_Estado != 'Cancelado')";
        return QueryToResultSet(idCli, query);
    }

    // Obtiene información del conductor por su ID
    public static ResultSet getDriverInfo(String id) {
        con = getConnection();
        String query = "SELECT * FROM Conductor WHERE Con_Id = ?";
        return QueryToResultSet(id, query);
    }

    // Obtiene una query y devuelve un resultset
    private static ResultSet QueryToResultSet(String id, String query) {
        try {
            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1, Integer.parseInt(id));
            ResultSet result = statement.executeQuery();

            if (result.next())
                return result;

            else return null;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Crea un pedido
    public static int createOrder(String start, String stop, int distance, int cost, int idCli) {
        // Prepara la consulta
        con = getConnection();
        // Para agregar el pedido
        String query = "INSERT INTO Pedido" +
                " (Ped_Hora, Ped_Fecha, Ped_LugarInicio, Ped_LugarLlegada, Ped_Distancia, " +
                "Ped_Costo, Ped_Estado, Cli_Id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        // Para modificar el estado actual del cliente
        String query2 = "UPDATE Cliente SET Cli_Pedido = 1 WHERE Cli_Id = ?";

        try {
            PreparedStatement statement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setTime(1, Time.valueOf(LocalTime.now()));
            statement.setDate(2, Date.valueOf(LocalDate.now()));
            statement.setString(3, start);
            statement.setString(4, stop);
            statement.setInt(5, distance);
            statement.setInt(6, cost);
            statement.setString(7, "Pedido");
            statement.setInt(8, idCli);

            PreparedStatement statement2 = con.prepareStatement(query2);
            statement2.setInt(1, idCli);


            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                statement2.executeUpdate(); // Ejecuta la actualización del usuario
                //                              cuando se crea el pedido (no testeado)

                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1); // Devuelve la ID de la nueva orden
                } else {
                    return -1; // Si no se generó nada
                }
            } else {
                return -2; // Si no se generó nada x2 XD
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -3; // Error SQL
        }

    }

    // Actualiza el estado de una orden por el ID del cliente. Esta solo
    // se puede actualizar para tomado o pedido.
    public static int updateOrderStatusByClient(int cli_ID, String status) {
        // Prepara la consulta
        con = getConnection();
        String query = "UPDATE Pedido SET Ped_Estado = ? WHERE Cli_Id = ? " +
                "AND (Ped_Estado != 'Finalizado' OR Ped_Estado != 'Cancelado')";
        String query2 = "UPDATE Cliente SET Cli_Pedido = 0 WHERE Cli_Id = ?";
        try {
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, status);
            statement.setInt(2, cli_ID);

            PreparedStatement statement2 = con.prepareStatement(query2);
            statement2.setInt(1, cli_ID);
            statement2.executeUpdate();

            statement.executeUpdate(); // Ejecuta la consulta
            return 0; // Si salió bien (está mal esta lógica)

        } catch (SQLException e) {
            e.printStackTrace();
            return -1; // Si ocurre un SQL error.
        }
    }


}
