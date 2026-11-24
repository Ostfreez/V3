package classes;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class PflanzenArt {
    // um EINE liste zu erstellen, wo alle pflanzenarten hinzugefügt werden, sobald diese instanziert werden
    public static List<PflanzenArt> pflanzenart = new ArrayList<>();

    private String name;
    private PflanzenTyp art;
    private PflanzenFarbe farbe;
    private String bluetezeit;
    private boolean winterhart;
    private String wissenschaftlich;

    public PflanzenArt(){
        name = "noch Keiner";
        art = PflanzenTyp.FREI;
        farbe = PflanzenFarbe.FREI;
        bluetezeit = "NOCH NICHT GESETZT";
        winterhart = false;
        wissenschaftlich = "NICHTS BEKANNT";
        pflanzenart.add(this); // hier wird die pflanzenart zur liste hinzugefügt
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PflanzenTyp getArt() {
        return art;
    }

    public void setArt(PflanzenTyp art) {
        this.art = art;
    }

    public PflanzenFarbe getFarbe() {
        return farbe;
    }

    public void setFarbe(PflanzenFarbe farbe) {
        this.farbe = farbe;
    }

    public String getBluetezeit() {
        return bluetezeit;
    }

    public void setBluetezeit(String bluetezeit) {
        this.bluetezeit = bluetezeit;
    }

    public boolean isWinterhart() {
        return winterhart;
    }

    public void setWinterhart(boolean winterhart) {
        this.winterhart = winterhart;
    }

    public String getWissenschaftlich() {
        return wissenschaftlich;
    }

    public void setWissenschaftlich(String wissenschaftlich) {
        this.wissenschaftlich = wissenschaftlich;
    }

    // pflanzenart - liste auf null setzen
    public static void resetPflanzenArt() {
        pflanzenart = new ArrayList<>();
    }



    @Override
    public String toString() {
        return
                "\nName: " + name +
                "\nArt: " + capitalizeFirstLetter(art.name()) +
                "\nFarbe: " + capitalizeFirstLetter(farbe.name()) +
                "\nBlütezeit: " + bluetezeit +
                "\nWinterhart: " + (winterhart ? "Ja": "Nein") +
                "\nZusatz: " + wissenschaftlich;
    }
    // ENUM in Normale schrift
    private String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }
}
