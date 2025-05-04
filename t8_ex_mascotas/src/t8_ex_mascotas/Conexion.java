package t8_ex_mascotas;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
private static Connection con;
	
	public static Connection getConnection() throws SQLException{
		String url= "jdbc:mysql://127.0.0.1:3307/mascotas";
		String user= "alumno";
		String password="alumno";
		
		if (con==null || con.isClosed()) {
			con=DriverManager.getConnection(url, user, password);
		}
		return con;
	}
}
