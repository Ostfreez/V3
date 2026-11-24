import classes.Pflanze;
import classes.PflanzenArt;
import database.GartenDatabase;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class MainController {

    private PflanzenService pflanzenService;
    private GartenDatabase db;
    private Pflanze aktuellePflanze;

    @FXML    private ListView<Pflanze> pflanzen_listView;
    @FXML    private Label message_label; // von JavaFX iniziiert

    @FXML    private TextField bluethZeit_tf;
    @FXML    private TextField breit_tf;
    @FXML    private TextField farbe_tf;
    @FXML    private TextField gepflanzt_tf;
    @FXML    private TextField hoehe_tf;
    @FXML    private TextField notizen_tf;
    @FXML    private TextField pflanze_tf;
    @FXML    private TextField pflanzenArt_tf;
    @FXML    private TextField winterh_tf;
    @FXML    private TextField zustand_tf;

    @FXML
    public void initialize() {
        System.out.println("MainController.initialize()");
        db = new GartenDatabase();
        pflanzenService = new PflanzenService(db);
        // 1. Datenbankverbindung öffnen

        pflanzen_listView.setItems(pflanzenService.getPflanzenList());

        //Listener für Pflanzenauswahl
        pflanzen_listView.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldValue, newValue) -> onPflanzeSelectionChange(newValue));
        // 3. UI mit den Daten befüllen
        if (!pflanzen_listView.getItems().isEmpty()) {
            pflanzen_listView.getSelectionModel().selectFirst();
        } else {
            message_label.setText("Keine Pflanzen in der Datenbank.");
        }

        //textView mit Datenquelle verknüpfen

    }
// für listView
    private void onPflanzeSelectionChange(Pflanze selectedPflanze) {

        if (selectedPflanze == null)
            {System.out.println("Keine Pflanze ausgewählt");
            return;}
        else {
            setGUIFromPflanze(selectedPflanze);
            message_label.setText("Ausgewählt: " + selectedPflanze.getTyp().getName());
        }
    }


    private Pflanze getSelectedPflanze() {

        return pflanzen_listView.getSelectionModel()
                .getSelectedItem();
    }


    @FXML
    void onButtonClickAnlegen(ActionEvent event) {

    }

    @FXML
    void onButtonClickAktualisieren(ActionEvent event) {


    }


    @FXML
    void onButtonClickDelete(ActionEvent event) {
        //TODO datenbank -> löschmethode

    }

    @FXML
    void onButtonClickReset(ActionEvent event) {
        resetGUI();

    }

    @FXML
    void onButtonClickbspLaden(ActionEvent event) {
        Pflanze p = db.loadPflanzeById(7);
        if (p != null) {
            setGUIFromPflanze(p);
            message_label.setText("Pflanze ID 7 geladen.");
        } else {
            message_label.setText("Pflanze ID 7 nicht gefunden.");
        }

    }

    @FXML
    void onFeldClick(javafx.event.ActionEvent event) {
        Button b = (Button) event.getSource();
        Integer x = GridPane.getColumnIndex(b);
        Integer y = GridPane.getRowIndex(b);
        message_label.setText("Feld geklickt: " + x + "/" + y);

        //NULLPOINTER ? ? ?

        try {
            setGUIFromPflanze(db.getPflanzeByPosition(x, y));
            //b.setStyle(""); // einfärben aus
        } catch (Exception e) {
            message_label.setText("Feld geklickt: " + x + "/" + y + " KEINE PFLANZE VORHANDEN");
            resetGUI();
            //b.setStyle("-fx-background-color: #ff5555; -fx-text-fill: white;"); // rot einfärben bei falsch
        }
    }

// ------------- HILFS-METHODEN
    private void setGUIFromPflanze(Pflanze selectedPflanze) {
        breit_tf.setText(String.valueOf(selectedPflanze.getGartenFeld().getX()));
        hoehe_tf.setText(String.valueOf((selectedPflanze.getGartenFeld().getY())));
        pflanze_tf.setText(selectedPflanze.getTyp().getName());
        farbe_tf.setText(selectedPflanze.getTyp().getFarbe().name());
        bluethZeit_tf.setText(selectedPflanze.getTyp().getBluetezeit());
        winterh_tf.setText(selectedPflanze.getTyp().isWinterhart() ? "ja" : "nein");
        gepflanzt_tf.setText(selectedPflanze.getDatumGepflanzt());
        zustand_tf.setText(selectedPflanze.getZustand());
        notizen_tf.setText(selectedPflanze.getNotiz());
        pflanzenArt_tf.setText(selectedPflanze.getTyp().getArt().name());  // z.B. "BLUME"
    }
    private void resetGUI(){
        breit_tf.setText("");
        hoehe_tf.setText("");
        pflanze_tf.setText("");
        farbe_tf.setText("");
        bluethZeit_tf.setText("");
        winterh_tf.setText("");
        gepflanzt_tf.setText("");
        zustand_tf.setText("");
        notizen_tf.setText("");
        pflanzenArt_tf.setText("");  // z.B. "BLUME"

    }


    public void closeDatabase() {
        if (db != null) {
            db.close();
        }
    }

}
