package classes;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseTest {
    public static void main(String[] args) {
        // Die URL zeigt auf eine Datei 'test.db' im Projektverzeichnis [citation:1]
        String url = "jdbc:sqlite:test.db";

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                System.out.println("✅ Erfolgreich mit SQLite verbunden!");
                System.out.println("Die Datenbank-Datei 'test.db' wurde im Projektordner erstellt.");
            }
        } catch (SQLException e) {
            System.err.println("❌ Verbindung fehlgeschlagen! Fehler: " + e.getMessage());
            e.printStackTrace();
        }
    }
}