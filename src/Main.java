/*
Hier entsteht eine Software für unser Garten
* */

import classes.*;

public class Main {
    public static void main(String[] args) {
        //Garten meinGarten = new Garten(10, 25);

        PflanzenArt tulpe = new PflanzenArt();
        tulpe.setName("Tulpe");
        tulpe.setArt(PflanzenTyp.BLUME);
        tulpe.setFarbe(PflanzenFarbe.GELB);
        tulpe.setWinterhart(true);
        tulpe.setWissenschaftlich("Pflanzung September bis Dezember");
        tulpe.setBluetezeit("April bis Mai");

        PflanzenArt dahlie = new PflanzenArt();
        dahlie.setName("Dahlie");
        dahlie.setArt(PflanzenTyp.STAUDE);
        dahlie.setFarbe(PflanzenFarbe.LILA);
        dahlie.setWinterhart(false);
        dahlie.setWissenschaftlich("Pflanzung ab mitte Mai");
        dahlie.setBluetezeit("ab Juli");

        Pflanze pflanze_test =  new Pflanze();
        pflanze_test.setId(1000);
        pflanze_test.setTyp(dahlie);
        pflanze_test.setDatumGepflanzt("2026-08-13");
        pflanze_test.setNotiz("Dies ist die erste Pflanze die ich hier einsetzen möchte");
        pflanze_test.setZustand("Starke Knollen");

        Pflanze pflanze_test2 =  new Pflanze();
        pflanze_test2.setId(1002);
        pflanze_test2.setTyp(tulpe);
        pflanze_test2.setDatumGepflanzt("2026-08-14");
        pflanze_test2.setNotiz("Dies ist die zweite Pflanze die ich hier einsetzen möchte");
        pflanze_test2.setZustand("Keine Staunässe");

        System.out.println("------------------------------------------------");
        GartenFeld aa = new GartenFeld(0,0);
        GartenFeld ab = new GartenFeld(0,1);
        aa.setPflanze(pflanze_test);
        ab.setPflanze(pflanze_test2);
        Garten zuHause = new Garten(10,25);
        zuHause.setFelder(0,0, aa);
        zuHause.setFelder(0,1, ab);

        System.out.println(zuHause.getFeld(0,0));
        System.out.println(zuHause.getFeld(0,1));
        System.out.println("_________________________________________________________");
        System.out.println(pflanze_test.getTyp().getFarbe());

    }
}