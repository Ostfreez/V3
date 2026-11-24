package classes;

public class GartenFeld {
    private int x;
    private int y;
    private Pflanze pflanze;

    public GartenFeld() {}

    public GartenFeld(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Pflanze getPflanze() {
        return pflanze;
    }

    public void setPflanze(Pflanze pflanze)
    {
        this.pflanze = pflanze;
    }

    @Override
    public String toString()
    {
        return "-----------GartenFeld-----------" +
                "\nx:\t" + x +
                "\ny:\t" + y +
                "\n" + pflanze +
                "\n-------------------------------";
    }
}
