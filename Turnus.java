import java.util.ArrayList;

public class Turnus {
    private final ArrayList<Spoj> spoje = new ArrayList<>();

    public Turnus() {

    }

    public void pridajSpoj(Spoj spoj) {
        this.spoje.add(spoj);
    }

    public boolean daSaPridat(Spoj spoj) {
        if (this.spoje.isEmpty()) {
            return true;
        }

        int spojCasOd = spoj.getCasOd();
        int spojCasDo = spoj.getCasDo();


        if (spojCasDo < this.spoje.get(0).getCasOd()) {
            this.spoje.add(0, spoj);
            return true;
        }


        for (int i = 0; i < this.spoje.size() - 1; i++) {
            Spoj currentSpoj = this.spoje.get(i);
            Spoj nextSpoj = this.spoje.get(i + 1);

            if (spojCasOd >= currentSpoj.getCasDo() && spojCasDo <= nextSpoj.getCasOd()) {
                this.spoje.add(i + 1, spoj);
                return true;
            }
        }


        if (spojCasOd >= this.spoje.get(this.spoje.size() - 1).getCasDo()) {
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

}
