package classes;

public class Pflanze {
    private int id;
    private PflanzenArt typ = new PflanzenArt();
    private String datumGepflanzt;
    private String notiz;
    private String zustand;
    private GartenFeld gartenFeld = new GartenFeld();

    public Pflanze(){
        id = -1;
        datumGepflanzt = "2026-08-12";
        notiz = " - NOCH KEIN EINTRAG - ";
        zustand = " - NOCH KEIN BEFUND - ";
    }

    public Pflanze(int id, PflanzenArt typ, String datumGepflanzt, String notiz, String zustand) {
        this.id = id;
        this.typ = typ;
        this.datumGepflanzt = datumGepflanzt;
        this.notiz = notiz;
        this.zustand = zustand;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public PflanzenArt getTyp()
    {
        return typ;
    }

    public void setTyp(PflanzenArt typ)
    {
        this.typ = typ;
    }

    public String getDatumGepflanzt()
    {
        return datumGepflanzt;
    }

    public void setDatumGepflanzt(String datumGepflanzt)
    {
        this.datumGepflanzt = datumGepflanzt;
    }

    public String getNotiz()
    {
        return notiz;
    }

    public void setNotiz(String notiz)
    {
        this.notiz = notiz;
    }

    public String getZustand()
    {
        return zustand;
    }

    public void setZustand(String zustand)
    {
        this.zustand = zustand;
    }

    public GartenFeld getGartenFeld() {
        return gartenFeld;
    }
    public void setGartenfeldPos(int x, int y){
        gartenFeld.setX(x);
        gartenFeld.setY(y);
    }

    public void setGartenFeld(GartenFeld gartenFeld) {
        this.gartenFeld = gartenFeld;
    }

    @Override
    public String toString()
    {
        return  "Id: " + id +
                "\n" + typ +
                "\nGepflanzt: " + datumGepflanzt +
                "\nNotiz: " + notiz +
                "\nZustand: " + zustand ;
    }
}
