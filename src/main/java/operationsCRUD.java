import java.sql.*;

public class operationsCRUD {


    private static final String URL = "jdbc:mysql://sql10.freesqldatabase.com:3306/sql10659471";
    private static final String USUARIO = "sql10659471";
    private static final String CONTRASENA = "8sX7wd4iRk";

    private static Connection con;

    public static Connection obtenerConexion() {
        Connection conexion = null;
        try {
            conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
            if (conexion != null) {
                System.out.println("Conexión exitosa a la base de datos.");
            }
        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos: " + e.getMessage());
        }
        return conexion;
    }

    public static int validateLogin(String userType, String cedula, String password) {
        con = obtenerConexion();
        String shortType = userType.substring(0, 3) + "_";
        String id = shortType + "Id";
        String cedCol = shortType + "Cedula";
        String passCol = shortType + "Pass";

        String query = "SELECT " + id + " FROM " + userType + " WHERE " + cedCol + " = ? AND " + passCol + " = ?";

        try {
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, cedula);
            statement.setString(2, password);

            ResultSet resultado = statement.executeQuery();

            if (resultado.next()) {
                int idUsuario = resultado.getInt(id);
                System.out.println("¡Inicio de sesión exitoso!");
                return idUsuario; // Devuelve el ID del usuario si las credenciales son correctas
            } else {
                System.out.println("Credenciales inválidas. Por favor, inténtelo de nuevo.");
                return -1; // Retorna -1 si las credenciales son incorrectas
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -2; // En caso de error, retorna -2
        } catch (NullPointerException e){
            e.printStackTrace();
            return -3;
        }
    }

    public static boolean registerUser(String userType, String name, String cedula,
                                       String email, String phone, String placa,
                                       String color, String pass){
        con = obtenerConexion();
        String shortType = userType.substring(0, 3);

        String query;

        if (userType.equals("Cliente")){
            query = "INSERT INTO Cliente (Cli_Cedula, Cli_Nombre, Cli_Pass, Cli_Numero, Cli_Pedido, Cli_Email)" +
                    "VALUES (?, ?, ?, ?, ?, ?)";


        } else {
            query = "INSERT INTO Conductor (Con_Cedula, Con_Nombre, Con_Pass, Con_Numero, Con_Pedido, Con_Email," +
                    " Con_Placa, Con_Color) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        }

        try{
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, cedula);
            statement.setString(2, name);
            statement.setString(3, pass);
            statement.setString(4, phone);
            statement.setString(5, "0");
            statement.setString(6, email);
            if (userType.equals("Conductor")){
                statement.setString(7, placa);
                statement.setString(8, color);
            }

            if (statement.executeUpdate() > 0){
                System.out.println("registradiño");
                return true;
            } else {
                System.out.println("no se registró jaj");
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


}
