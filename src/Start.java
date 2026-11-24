import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Start extends Application {
    
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        // ========================================== AUS Vorlesung
        Parent root = FXMLLoader.load(getClass().getResource("Main.fxml") ); // VBox
        Scene scene = new Scene(root);
        // ==========================================
        // Für komplexere Applikationen mit Zugriff auf die Controller-Instanz:
        // FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Main.fxml"));
        // Scene scene = new Scene( fxmlLoader.load());
        // ==========================================
        stage.setScene(scene);
        stage.setWidth(1200);
        stage.setHeight(900);
        // ==========================================
        stage.show();
        // ==========================================
        stage.setTitle("Hennings Garten-Software");
        stage.setAlwaysOnTop(true);
        // Weitere Einstellungen für das stage-Objekt
    }

}
