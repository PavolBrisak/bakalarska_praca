import java.util.ArrayList;

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
        //TODO kontrola stanic kde konci spoj

        for (int i = 0; i < this.spoje.size() - 1; i++) {
            Spoj currentSpoj = this.spoje.get(i);
            Spoj nextSpoj = this.spoje.get(i + 1);

            if (spoj.getCasOd() >= currentSpoj.getCasDo() && spoj.getCasDo() <= nextSpoj.getCasOd()) {
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
        String text = "D - ";
        for (Spoj spoj:this.spoje) {
            text += spoj.getId() + " - ";
        }
        System.out.println(text + "D");
    }

    public ArrayList<Spoj> getSpoje() {
        return spoje;
    }
}
