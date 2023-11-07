import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

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
        Random random = new Random();
        int pocetVymen = random.nextInt(this.spoje.size()) + 1;
        System.out.println("pocetVymen - " + pocetVymen);
        for (int i = 0; i < pocetVymen; i++) {
            int index1 = random.nextInt(this.spoje.size());
            int index2;
            do {
                index2 = random.nextInt(this.spoje.size());
            } while (index2 == index1);
            System.out.println("index1 - " + index1);
            System.out.println("index2 - " + index2);

            Collections.swap(this.spoje, index1, index2);
        }
    }
}
