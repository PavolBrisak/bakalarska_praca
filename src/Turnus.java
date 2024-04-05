package src;

import java.util.ArrayList;

public class Turnus {
    private ArrayList<Spoj> spoje = new ArrayList<>();

    public Turnus() {

    }

    public Turnus(Turnus turnus) {
        this.spoje.addAll(turnus.getSpoje());
    }

    public void pridajSpoj(Spoj spoj) {
        this.spoje.add(spoj);
    }

    public boolean pridalSa(Spoj spoj, int[][] maticaVzdialenosti) {
        // ak je turnus prazdny, pridaj spoj
        if (this.spoje.isEmpty()) {
            this.spoje.add(spoj);
            return true;
        }

        // ak sa da, tak pridaj spoj na prve miesto
        if ((this.spoje.get(0).getCasOd() >= spoj.getCasDo()) && (maticaVzdialenosti[spoj.getIndex()][this.spoje.get(0).getIndex()] <= (this.spoje.get(0).getCasOd() - spoj.getCasDo()))) {
            this.spoje.add(0, spoj);
            return true;
        }

        if (this.spoje.size() >= 2) {
            for (int i = 0; i < this.spoje.size() - 1; i++) {
                Spoj currentSpoj = this.spoje.get(i);
                Spoj nextSpoj = this.spoje.get(i + 1);

                // ak sa da, tak pridaj spoj na i-te miesto
                if ((currentSpoj.getCasDo() <= spoj.getCasOd()) && (maticaVzdialenosti[currentSpoj.getIndex()][spoj.getIndex()] <= (spoj.getCasOd() - currentSpoj.getCasDo())) && (spoj.getCasDo() <= nextSpoj.getCasOd()) && (maticaVzdialenosti[spoj.getIndex()][nextSpoj.getIndex()] <= (nextSpoj.getCasOd() - spoj.getCasDo()))) {
                    this.spoje.add(i + 1, spoj);
                    return true;
                }
            }
        }

        // ak sa da, tak pridaj spoj na posledne miesto
        if ((this.spoje.get(this.spoje.size() - 1).getCasDo() <= spoj.getCasOd()) && (maticaVzdialenosti[this.spoje.get(this.spoje.size() - 1).getIndex()][spoj.getIndex()] <= (spoj.getCasOd() - this.spoje.get(this.spoje.size() - 1).getCasDo()))) {
            this.spoje.add(spoj);
            return true;
        }
        return false;
    }

    public void vypis() {
        StringBuilder text = new StringBuilder("D - ");
        for (Spoj spoj : this.spoje) {
            text.append(spoj.getIndex()).append(" - ");
        }
        System.out.println(text + "D");
    }

    public ArrayList<Spoj> getSpoje() {
        return this.spoje;
    }
}
