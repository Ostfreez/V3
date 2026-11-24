import classes.Pflanze;
import classes.PflanzenArt;
import database.GartenDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class PflanzenService {


    // KI weil -> Observable kein schimmer
    private final ObservableList<Pflanze> pflanzenList = FXCollections.observableArrayList();

    public PflanzenService(GartenDatabase db) {
        pflanzenList.addAll(db.loadAllPflanzen());
    }

    public ObservableList<Pflanze> getPflanzenList() {
        return pflanzenList;
    }

    // Praktisch, um nach Änderungen (Anlegen/Löschen) neu zu laden:
    public void refresh(GartenDatabase db) {
        pflanzenList.clear();
        pflanzenList.addAll(db.loadAllPflanzen());
    }
}