package classes;

import database.GartenDatabase;

public class DatabaseTestFull {
    public static void main(String[] args) {
        GartenDatabase db = new GartenDatabase();

        // 1. Neue PflanzenArt speichern
        PflanzenArt tulpe = new PflanzenArt();
        tulpe.setName("Tulpe");
        tulpe.setArt(PflanzenTyp.BLUME);
        tulpe.setFarbe(PflanzenFarbe.ROT);
        tulpe.setBluetezeit("April-Mai");
        tulpe.setWinterhart(true);
        tulpe.setWissenschaftlich("Tulipa gesneriana");

        int artId = db.savePflanzenArt(tulpe);
        System.out.println("PflanzenArt gespeichert mit ID: " + artId);

        // 2. Neue Pflanze speichern (mit Position)
        Pflanze pflanze1 = new Pflanze();
        pflanze1.setTyp(tulpe);
        pflanze1.setDatumGepflanzt("2026-08-22");
        pflanze1.setNotiz("Erste Testpflanze");
        pflanze1.setZustand("Gesund");

        int pflanzeId = db.savePflanze(pflanze1, 5, 3);
        System.out.println("Pflanze gespeichert mit ID: " + pflanzeId);

        db.deletePflanze(2);
        db.deletePflanze(3);
        db.deletePflanze(4);

        // 3. Alle Pflanzen laden
        System.out.println("\n=== Alle Pflanzen ===");
        for (Pflanze p : db.loadAllPflanzen()) {
            System.out.println("Pflanze ID:  " + p.getId());
            System.out.println("  Art:       " + p.getTyp().getName());
            System.out.println("  Gepflanzt: " + p.getDatumGepflanzt());
            System.out.println("  Notiz:     " + p.getNotiz());
            System.out.println("  Zustand:   " + p.getZustand());
            System.out.println("---");
        }

        // 4. Datenbank schließen
        db.close();
    }
}