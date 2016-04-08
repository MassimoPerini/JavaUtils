import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class WBeM {
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.jdbc.Driver");				//Carico classe connessione db
            Connection conn =DriverManager.getConnection(
					"jdbc:mysql://localhost/cim?" + "user=root&password="
				);
            Statement stmt;
            PreparedStatement pstmt;
            ResultSet rs;

            // creo la tabella
            stmt = conn.createStatement();

            stmt.executeUpdate("DROP TABLE IF EXISTS pc");
            stmt.executeUpdate("CREATE TABLE pc " +
                    "(id INTEGER primary key auto_increment, " +
                    "info TEXT NOT NULL, " +
                    "description TEXT NOT NULL)");

            // inserisco due record
            pstmt = conn.prepareStatement("INSERT INTO pc " + "(info, description) values (?,?)");

            pstmt.setString(1, "Mario");
            pstmt.setString(2, "Rossi");
            pstmt.execute();

            pstmt.setString(1, "Marco");
            pstmt.setString(2, "Bianchi");
            pstmt.execute();

			pstmt.close(); // rilascio le risorse
			stmt.close(); // rilascio le risorse
            conn.close(); // termino la connessione
        }
        catch(ClassNotFoundException e)
        {
            System.out.println(e);
        }
        catch(SQLException e)
        {
            System.out.println(e);
        }
    }
}
