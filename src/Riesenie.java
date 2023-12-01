package src;

import java.util.ArrayList;

public class Riesenie {
    private final ArrayList<Turnus> turnusy = new ArrayList<>();
    private int ohodnotenie;

    public Riesenie() {
    }

    public void pridajTurnus(Turnus turnus) {
        Turnus turnus1 = new Turnus(turnus);
        this.turnusy.add(turnus1);
    }

    public ArrayList<Turnus> getTurnusy() {
        return this.turnusy;
    }

    public int getOhodnotenie() {
        return ohodnotenie;
    }

    public void setOhodnotenie(int ohodnotenie) {
        this.ohodnotenie = ohodnotenie;
    }

    public void vypis() {
        System.out.println("*************************");
        for (Turnus turnus:this.turnusy) {
            turnus.vypis();
        }
        System.out.println("*************************");
    }

    public int getPocetSpojov() {
        int pocetSpojov = 0;
        for (Turnus turnus:this.getTurnusy()) {
            pocetSpojov += turnus.getSpoje().size();
        }
        return pocetSpojov;
    }

    public void vymazPrazdneTurnusy() {
        ArrayList<Integer> indexyNaVymazanie = new ArrayList<>();
        for (int i = 0; i < this.turnusy.size(); i++) {
            Turnus turnus = this.turnusy.get(i);
            if (turnus.getSpoje().isEmpty()) {
                indexyNaVymazanie.add(i);
            }
        }
        for (int i = indexyNaVymazanie.size() - 1; i >= 0; i--) {
            int indexNaVymazanie = indexyNaVymazanie.get(i);
            this.turnusy.remove(indexNaVymazanie);
        }
    }
}
