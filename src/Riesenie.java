package src;

import java.util.ArrayList;

public class Riesenie {
    private final ArrayList<Turnus> turnusy = new ArrayList<>();
    private double ohodnotenie;
    private int pocetVozidiel;

    public Riesenie() {
    }

    public Riesenie(Riesenie riesenie) {
        this.turnusy.addAll(riesenie.getTurnusy());
        this.ohodnotenie = riesenie.getOhodnotenie();
        this.pocetVozidiel = riesenie.getPocetVozidiel();
    }

    public void pridajTurnus(Turnus turnus) {
        Turnus turnus1 = new Turnus(turnus);
        this.turnusy.add(turnus1);
    }

    public ArrayList<Turnus> getTurnusy() {
        return this.turnusy;
    }

    public double getOhodnotenie() {
        return this.ohodnotenie;
    }

    public void setOhodnotenie(double ohodnotenie) {
        this.ohodnotenie = ohodnotenie;
    }

    public void vypis() {
        System.out.println("Počet vozidiel: " + this.pocetVozidiel);
        System.out.println("Ohodnotenie: " + this.ohodnotenie);
        for (Turnus turnus:this.turnusy) {
            turnus.vypis();
        }
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

    public int getPocetVozidiel() {
        return this.pocetVozidiel;
    }

    public void setPocetVozidiel(int pocetVozidiel) {
        this.pocetVozidiel = pocetVozidiel;
    }
}
