package src;

import java.util.ArrayList;
import java.util.Objects;

public class Turnus {
    private final ArrayList<Spoj> spoje = new ArrayList<>();

    public Turnus() {

    }

    public void pridajSpoj(Spoj spoj) {
        this.spoje.add(spoj);
    }

    public boolean pridalSa(Spoj spoj) {
        if (this.spoje.isEmpty()) {
            this.spoje.add(spoj);
            return true;
        }

        for (int i = 0; i < this.spoje.size() - 1; i++) {
            Spoj currentSpoj = this.spoje.get(i);
            Spoj nextSpoj = this.spoje.get(i + 1);

            if ((spoj.getCasOd() == currentSpoj.getCasDo()) && (spoj.getMiestoOd().equals(currentSpoj.getMiestoDo())) && (spoj.getCasDo() < nextSpoj.getCasOd())) {
                this.spoje.add(i + 1, spoj);
                return true;
            }

            if ((spoj.getCasOd() > currentSpoj.getCasDo()) && (spoj.getMiestoDo().equals(nextSpoj.getMiestoOd())) && (spoj.getCasDo() == nextSpoj.getCasOd())) {
                this.spoje.add(i + 1, spoj);
                return true;
            }

            if ((spoj.getCasOd() == currentSpoj.getCasDo()) && (spoj.getMiestoOd().equals(currentSpoj.getMiestoDo())) && (spoj.getCasDo() == nextSpoj.getCasOd()) && (spoj.getMiestoDo().equals(nextSpoj.getMiestoOd()))) {
                this.spoje.add(i + 1, spoj);
                return true;
            }

            if (spoj.getCasOd() > currentSpoj.getCasDo() && spoj.getCasDo() < nextSpoj.getCasOd()) {
                this.spoje.add(i + 1, spoj);
                return true;
            }
        }

        if (spoj.getCasOd() >= this.spoje.get(this.spoje.size() - 1).getCasDo()) {
            this.spoje.add(spoj);
            return true;
        }
        return false;
    }

    public void vypis() {
        StringBuilder text = new StringBuilder("D - ");
        for (Spoj spoj:this.spoje) {
            text.append(spoj.getId()).append(" - ");
        }
        System.out.println(text + "D");
    }

    public ArrayList<Spoj> getSpoje() {
        return this.spoje;
    }
}
