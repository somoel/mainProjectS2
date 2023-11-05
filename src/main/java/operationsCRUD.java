import java.sql.*;

public class operationsCRUD {


    private static final String URL = "jdbc:mysql://sql10.freesqldatabase.com:3306/sql10659471";
    private static final String USUARIO = "sql10659471";
    private static final String CONTRASENA = "8sX7wd4iRk";

    private static Connection con = obtenerConexion();

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
        String shortType = userType.substring(0, 3);
        String id = shortType + "_Id";
        String cedCol = shortType + "_Cedula";
        String passCol = shortType + "_Pass";

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
            return -1; // En caso de error, retorna -1
        }
    }


}
