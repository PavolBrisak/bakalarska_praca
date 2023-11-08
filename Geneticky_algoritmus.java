import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Geneticky_algoritmus {
    private ArrayList<Riesenie> staraPopulacia = new ArrayList<>();
    private ArrayList<Spoj> spoje;
    private Riesenie DNNR;

    public Geneticky_algoritmus(ArrayList<Spoj> nacitaneSpoje) {
        this.spoje = nacitaneSpoje;
        this.vytvorPociatocnuPopulaciu();
        this.DNNR = this.dajNajlepsieRiesenie();
    }

    private Riesenie dajNajlepsieRiesenie() {
        Riesenie najlepsieRiesenie = new Riesenie();
        najlepsieRiesenie.setOhodnotenie(0);
        for (Riesenie riesenie:this.staraPopulacia) {
            if (riesenie.getOhodnotenie() > najlepsieRiesenie.getOhodnotenie()) {
                najlepsieRiesenie = riesenie;
            }
        }
        return najlepsieRiesenie;
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
            this.ohodnotRiesenie(noveRiesenie);
            this.staraPopulacia.add(noveRiesenie);
            this.novaPermutacia();
        }
    }

    public void genetickyAlgoritmus() {
        int pocetIteracii = 0;
        int pocetNeaktualizovaniaDNNR = 0;
        while ((pocetIteracii <= 1000) || (pocetNeaktualizovaniaDNNR <= 200)) {
            ArrayList<Riesenie> novaPopulacia = new ArrayList<>();
            //TODO nastavit cyklus na prechadzanie vsetkych rodicov
            int indexRodic1 = this.dajPoziciuRodica();
            int indexRodic2;
            do {
                indexRodic2 = this.dajPoziciuRodica();
            } while (indexRodic1 == indexRodic2);
            Riesenie potomok = this.krizenie(indexRodic1, indexRodic2);
            //TODO skontroluj potomka, ak je chybny, oprav ho a ohodnot ho
            novaPopulacia.add(potomok);
            this.staraPopulacia = novaPopulacia;
            pocetIteracii++;
            //TODO ak nenastalo aktualizovanie
            pocetNeaktualizovaniaDNNR++;
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

    public void ohodnotRiesenie(Riesenie riesenie) {
        riesenie.setOhodnotenie(riesenie.getTurnusy().size());
    }

    public int dajPoziciuRodica() {
        Random random = new Random();
        return random.nextInt(0, this.staraPopulacia.size());
    }

    public Riesenie krizenie(int poziciaRodica1, int poziciaRodica2) {
        Random random = new Random();
        int bodKrizeniaRodic1 = random.nextInt(0,this.staraPopulacia.get(poziciaRodica1).getTurnusy().size());
        Riesenie potomok = new Riesenie();
        for (int i = 0; i < bodKrizeniaRodic1; i++) {
            potomok.pridajTurnus(this.staraPopulacia.get(poziciaRodica1).getTurnusy().get(i));
        }
        int bodKrizeniaRodic2 = random.nextInt(0,this.staraPopulacia.get(poziciaRodica2).getTurnusy().size());
        for (int i = bodKrizeniaRodic2; i < this.staraPopulacia.get(poziciaRodica2).getTurnusy().size(); i++) {
            potomok.pridajTurnus(this.staraPopulacia.get(poziciaRodica2).getTurnusy().get(i));
        }
        return potomok;
    }
}
