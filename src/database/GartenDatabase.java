package database;

import classes.Pflanze;
import classes.PflanzenArt;
import classes.PflanzenFarbe;
import classes.PflanzenTyp;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GartenDatabase {
    private Connection connection;
    private static final String DB_URL = "jdbc:sqlite:garten.db";

    public GartenDatabase() {
        try {
            connection = DriverManager.getConnection(DB_URL);
            createTables();
            System.out.println("✅ Datenbank initialisiert");
        } catch (SQLException e) {
            System.err.println("❌ Fehler bei Datenbankinitialisierung: " + e.getMessage());
        }
    }

    private void createTables() throws SQLException {
        String pflanzenArtSQL = """
            CREATE TABLE IF NOT EXISTS pflanzenarten (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                typ TEXT NOT NULL,
                farbe TEXT NOT NULL,
                bluetezeit TEXT,
                winterhart INTEGER DEFAULT 0,
                wissenschaftlich TEXT
            )
        """;

        String pflanzenSQL = """
            CREATE TABLE IF NOT EXISTS pflanzen (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                pflanzenart_id INTEGER NOT NULL,
                datum_gepflanzt TEXT NOT NULL,
                notiz TEXT,
                zustand TEXT,
                position_x INTEGER DEFAULT -1,
                position_y INTEGER DEFAULT -1,
                FOREIGN KEY (pflanzenart_id) REFERENCES pflanzenarten(id) ON DELETE CASCADE
            )
        """;

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(pflanzenArtSQL);
            stmt.execute(pflanzenSQL);
        }
    }

    // === PFLANZENARTEN METHODEN ===

    // Speichern mit Rückgabe der ID
    public int savePflanzenArt(PflanzenArt art) {
        String sql = """
            INSERT INTO pflanzenarten (name, typ, farbe, bluetezeit, winterhart, wissenschaftlich)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, art.getName());
            pstmt.setString(2, art.getArt().name());
            pstmt.setString(3, art.getFarbe().name());
            pstmt.setString(4, art.getBluetezeit());
            pstmt.setInt(5, art.isWinterhart() ? 1 : 0);
            pstmt.setString(6, art.getWissenschaftlich());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (Statement stmt = connection.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT last_insert_rowid()")) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Fehler beim Speichern der PflanzenArt: " + e.getMessage());
        }
        return -1;
    }

    // Alle PflanzenArten laden
    public List<PflanzenArt> loadAllPflanzenArten() {
        List<PflanzenArt> arten = new ArrayList<>();
        String sql = "SELECT * FROM pflanzenarten ORDER BY name";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                PflanzenArt art = new PflanzenArt();
                // Die ID speichern im Namen oder als extra Feld
                // Für jetzt: einfach die Attribute setzen
                art.setName(rs.getString("name"));
                art.setArt(PflanzenTyp.valueOf(rs.getString("typ")));
                art.setFarbe(PflanzenFarbe.valueOf(rs.getString("farbe")));
                art.setBluetezeit(rs.getString("bluetezeit"));
                art.setWinterhart(rs.getInt("winterhart") == 1);
                art.setWissenschaftlich(rs.getString("wissenschaftlich"));
                arten.add(art);
            }
        } catch (SQLException e) {
            System.err.println("❌ Fehler beim Laden der PflanzenArten: " + e.getMessage());
        }
        return arten;
    }

    // Eine bestimmte PflanzenArt laden
    public PflanzenArt getPflanzenArtById(int id) {
        String sql = "SELECT * FROM pflanzenarten WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                PflanzenArt art = new PflanzenArt();
                art.setName(rs.getString("name"));
                art.setArt(PflanzenTyp.valueOf(rs.getString("typ")));
                art.setFarbe(PflanzenFarbe.valueOf(rs.getString("farbe")));
                art.setBluetezeit(rs.getString("bluetezeit"));
                art.setWinterhart(rs.getInt("winterhart") == 1);
                art.setWissenschaftlich(rs.getString("wissenschaftlich"));
                return art;
            }
        } catch (SQLException e) {
            System.err.println("❌ Fehler beim Laden der PflanzenArt: " + e.getMessage());
        }
        return null;
    }

    // === PFLANZEN METHODEN ===

    // Pflanze speichern
    //TODO pflanzenArt aus db auswählen und nicht immer neu speichern
    public int savePflanze(Pflanze pflanze, int positionX, int positionY) {
        // Zuerst die PflanzenArt speichern (falls neu)
        int artId = savePflanzenArt(pflanze.getTyp());

        String sql = """
            INSERT INTO pflanzen (pflanzenart_id, datum_gepflanzt, notiz, zustand, position_x, position_y)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, artId);
            pstmt.setString(2, pflanze.getDatumGepflanzt());
            pstmt.setString(3, pflanze.getNotiz());
            pstmt.setString(4, pflanze.getZustand());
            pstmt.setInt(5, positionX);
            pstmt.setInt(6, positionY);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (Statement stmt = connection.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT last_insert_rowid()")) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Fehler beim Speichern der Pflanze: " + e.getMessage());
        }
        return -1;
    }

    // Alle Pflanzen laden (mit zugehöriger PflanzenArt)
    public List<Pflanze> loadAllPflanzen() {
        List<Pflanze> pflanzen = new ArrayList<>();
        String sql = """
        SELECT 
            p.id AS pflanze_id,
            p.datum_gepflanzt,
            p.notiz,
            p.zustand,
            p.position_x,
            p.position_y,
            pa.id AS art_id,
            pa.name AS art_name,
            pa.typ AS art_typ,
            pa.farbe AS art_farbe,
            pa.bluetezeit AS art_bluetezeit,
            pa.winterhart AS art_winterhart,
            pa.wissenschaftlich AS art_wissenschaftlich
        FROM pflanzen p
        JOIN pflanzenarten pa ON p.pflanzenart_id = pa.id
        ORDER BY p.id
    """;

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // Pflanze rekonstruieren
                //TODO wiederholt sich
                Pflanze pflanze = new Pflanze();
                pflanze.setId(rs.getInt("pflanze_id"));
                pflanze.setDatumGepflanzt(rs.getString("datum_gepflanzt"));
                pflanze.setNotiz(rs.getString("notiz"));
                pflanze.setZustand(rs.getString("zustand"));
                pflanze.setGartenfeldPos(Integer.parseInt(rs.getString("position_x")),
                        (Integer.parseInt(rs.getString("position_y"))));

                // PflanzenArt rekonstruieren
                PflanzenArt art = new PflanzenArt();
                art.setName(rs.getString("art_name"));
                art.setArt(PflanzenTyp.valueOf(rs.getString("art_typ")));
                art.setFarbe(PflanzenFarbe.valueOf(rs.getString("art_farbe")));
                art.setBluetezeit(rs.getString("art_bluetezeit"));
                art.setWinterhart(rs.getInt("art_winterhart") == 1);
                art.setWissenschaftlich(rs.getString("art_wissenschaftlich"));
                pflanze.setTyp(art);

                pflanzen.add(pflanze);

                // Debug-Ausgabe (optional)
                System.out.println("  Pflanze ID: " + pflanze.getId() +
                                           ", Art: " + art.getName());
            }
        } catch (SQLException e) {
            System.err.println("❌ Fehler beim Laden der Pflanzen: " + e.getMessage());
            e.printStackTrace(); // Für bessere Fehleranalyse
        }
        return pflanzen;
    }

    // Eine bestimmte Pflanze anhand ihrer ID laden
    public Pflanze loadPflanzeById(int id) {
        String sql = """
        SELECT 
            p.id AS pflanze_id,
            p.datum_gepflanzt,
            p.notiz,
            p.zustand,
            p.position_x,
            p.position_y,
            pa.id AS art_id,
            pa.name AS art_name,
            pa.typ AS art_typ,
            pa.farbe AS art_farbe,
            pa.bluetezeit AS art_bluetezeit,
            pa.winterhart AS art_winterhart,
            pa.wissenschaftlich AS art_wissenschaftlich
        FROM pflanzen p
        JOIN pflanzenarten pa ON p.pflanzenart_id = pa.id
        WHERE p.id = ?
    """;

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // Pflanze rekonstruieren
                //TODO hier die wiederholung
                Pflanze pflanze = new Pflanze();
                pflanze.setId(rs.getInt("pflanze_id"));
                pflanze.setDatumGepflanzt(rs.getString("datum_gepflanzt"));
                pflanze.setNotiz(rs.getString("notiz"));
                pflanze.setZustand(rs.getString("zustand"));
                pflanze.setGartenfeldPos(Integer.parseInt(rs.getString("position_x")),
                        (Integer.parseInt(rs.getString("position_y"))));

                // PflanzenArt rekonstruieren
                PflanzenArt art = new PflanzenArt();
                art.setName(rs.getString("art_name"));
                art.setArt(PflanzenTyp.valueOf(rs.getString("art_typ")));
                art.setFarbe(PflanzenFarbe.valueOf(rs.getString("art_farbe")));
                art.setBluetezeit(rs.getString("art_bluetezeit"));
                art.setWinterhart(rs.getInt("art_winterhart") == 1);
                art.setWissenschaftlich(rs.getString("art_wissenschaftlich"));
                pflanze.setTyp(art);

                return pflanze;
            } else {
                System.out.println("⚠️ Keine Pflanze mit ID " + id + " gefunden.");
            }
        } catch (SQLException e) {
            System.err.println("❌ Fehler beim Laden der Pflanze mit ID " + id + ": " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // Pflanzen nach Typ filtern
    public List<Pflanze> getPflanzenByTyp(PflanzenTyp typ) {
        List<Pflanze> pflanzen = new ArrayList<>();
        String sql = """
        SELECT 
            p.id AS pflanze_id,
            p.datum_gepflanzt,
            p.notiz,
            p.zustand,
            p.position_x,
            p.position_y,
            pa.id AS art_id,
            pa.name AS art_name,
            pa.typ AS art_typ,
            pa.farbe AS art_farbe,
            pa.bluetezeit AS art_bluetezeit,
            pa.winterhart AS art_winterhart,
            pa.wissenschaftlich AS art_wissenschaftlich
        FROM pflanzen p
        JOIN pflanzenarten pa ON p.pflanzenart_id = pa.id
        WHERE pa.typ = ?
        ORDER BY p.id
    """;

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, typ.name());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                // Gleiche Rekonstruktionslogik wie oben
                Pflanze pflanze = new Pflanze();
                pflanze.setId(rs.getInt("pflanze_id"));
                pflanze.setDatumGepflanzt(rs.getString("datum_gepflanzt"));
                pflanze.setNotiz(rs.getString("notiz"));
                pflanze.setZustand(rs.getString("zustand"));

                PflanzenArt art = new PflanzenArt();
                art.setName(rs.getString("art_name"));
                art.setArt(PflanzenTyp.valueOf(rs.getString("art_typ")));
                art.setFarbe(PflanzenFarbe.valueOf(rs.getString("art_farbe")));
                art.setBluetezeit(rs.getString("art_bluetezeit"));
                art.setWinterhart(rs.getInt("art_winterhart") == 1);
                art.setWissenschaftlich(rs.getString("art_wissenschaftlich"));
                pflanze.setTyp(art);

                pflanzen.add(pflanze);
            }
        } catch (SQLException e) {
            System.err.println("❌ Fehler beim Filtern: " + e.getMessage());
            e.printStackTrace();
        }
        return pflanzen;
    }

    // Pflanze löschen
    public boolean deletePflanze(int pflanzeId) {
        String sql = "DELETE FROM pflanzen WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, pflanzeId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("❌ Fehler beim Löschen der Pflanze: " + e.getMessage());
            return false;
        }
    }

    public Pflanze getPflanzeByPosition (int x, int y) {
       // Pflanze pflanze =  new Pflanze();

        String sql = """
        SELECT 
            p.id AS pflanze_id,
            p.datum_gepflanzt,
            p.notiz,
            p.zustand,
            p.position_x,
            p.position_y,
            pa.id AS art_id,
            pa.name AS art_name,
            pa.typ AS art_typ,
            pa.farbe AS art_farbe,
            pa.bluetezeit AS art_bluetezeit,
            pa.winterhart AS art_winterhart,
            pa.wissenschaftlich AS art_wissenschaftlich
        FROM pflanzen p
        JOIN pflanzenarten pa ON p.pflanzenart_id = pa.id
        WHERE p.position_x = ? AND p.position_y = ?
    """;

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, x);
            pstmt.setInt(2, y);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // Pflanze rekonstruieren
                //TODO hier die wiederholung
                Pflanze pflanze = new Pflanze();
                pflanze.setId(rs.getInt("pflanze_id"));
                pflanze.setDatumGepflanzt(rs.getString("datum_gepflanzt"));
                pflanze.setNotiz(rs.getString("notiz"));
                pflanze.setZustand(rs.getString("zustand"));
                pflanze.setGartenfeldPos(Integer.parseInt(rs.getString("position_x")),
                        (Integer.parseInt(rs.getString("position_y"))));

                // PflanzenArt rekonstruieren
                PflanzenArt art = new PflanzenArt();
                art.setName(rs.getString("art_name"));
                art.setArt(PflanzenTyp.valueOf(rs.getString("art_typ")));
                art.setFarbe(PflanzenFarbe.valueOf(rs.getString("art_farbe")));
                art.setBluetezeit(rs.getString("art_bluetezeit"));
                art.setWinterhart(rs.getInt("art_winterhart") == 1);
                art.setWissenschaftlich(rs.getString("art_wissenschaftlich"));
                pflanze.setTyp(art);

                return pflanze;
            } else {
                System.out.println(
                        "⚠️ Keine Pflanze auf Position " +
                        x + "/" +
                        y + " gefunden.");
            }
        } catch (SQLException e) {
            System.err.println("❌ Fehler beim Laden der Pflanze  " + x + "/" + y + ": " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // === HELPER METHODEN ===

    // Datenbank schließen
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Datenbankverbindung geschlossen.");
            }
        } catch (SQLException e) {
            System.err.println("Fehler beim Schließen: " + e.getMessage());
        }
    }

    // Tabellen zurücksetzen (für Tests)
    public void resetDatabase() {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS pflanzen");
            stmt.execute("DROP TABLE IF EXISTS pflanzenarten");
            createTables();
            System.out.println("Datenbank zurückgesetzt.");
        } catch (SQLException e) {
            System.err.println("Fehler beim Zurücksetzen: " + e.getMessage());
        }
    }
}
