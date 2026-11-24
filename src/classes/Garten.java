package classes;

import java.util.Arrays;

public class Garten {

    private GartenFeld[][] felder;

    public Garten(int breite, int hoehe) {
        this.felder = new GartenFeld[breite][hoehe];

        for (int x = 0; x < breite; x++) {
            for (int y = 0; y < hoehe; y++) {
                felder[x][y] = new GartenFeld(x, y);
            }
        }
    }

    public void setFelder(int x, int y, GartenFeld feld)
    {
        this.felder[x][y] = feld;
    }

    public GartenFeld getFeld(int x, int y)
    {
        return felder[x][y];
    }

}
