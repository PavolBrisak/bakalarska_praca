import java.util.ArrayList;

public class Geneticky_algoritmus {
    private final ArrayList<Riesenie> pociatocnaPopulacia = new ArrayList<>();
    private final ArrayList<Spoj> spoje;

    public Geneticky_algoritmus(ArrayList<Spoj> nacitaneSpoje) {
        this.spoje = nacitaneSpoje;
        this.vytvorPociatocnuPopulaciu();
    }

    public void vytvorPociatocnuPopulaciu() {
        int pocetPermutacii = 50;
        for (int i = 0; i < pocetPermutacii; i++) {
            Riesenie noveRiesenie = new Riesenie();
            for (Spoj spoj : this.spoje) {
                if (noveRiesenie.getTurnusy().size() == 0) {
                    Turnus novyTurnus = new Turnus();
                    novyTurnus.pridajSpoj(spoj);
                    noveRiesenie.pridajTurnus(novyTurnus);
                } else {
                    boolean pridany = false;
                    for (Turnus turnus : noveRiesenie.getTurnusy()) {
                        if (turnus.daSaPridat(spoj)) {
                            pridany = true;
                            break;
                        }
                    }
                    if (!pridany) {
                        Turnus novyTurnus = new Turnus();
                        novyTurnus.pridajSpoj(spoj);
                        noveRiesenie.pridajTurnus(novyTurnus);
                    }
                }
            }
            this.pociatocnaPopulacia.add(noveRiesenie);
            this.novaPermutacia();
        }
    }

    public void novaPermutacia() {

    }
}
