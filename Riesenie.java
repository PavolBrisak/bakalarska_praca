import java.util.ArrayList;

public class Riesenie {
    private final ArrayList<Turnus> turnusy = new ArrayList<>();

    public Riesenie() {
    }

    public void pridajTurnus(Turnus turnus) {
        this.turnusy.add(turnus);
    }

    public ArrayList<Turnus> getTurnusy() {
        return this.turnusy;
    }

    public void vypis() {
        System.out.println("*************************");
        for (Turnus turnus:this.turnusy) {
            turnus.vypis();
        }
        System.out.println("*************************");
    }
}
