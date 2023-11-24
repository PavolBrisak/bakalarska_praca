package src;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class Geneticky_algoritmus {
    private ArrayList<Riesenie> staraPopulacia = new ArrayList<>();
    private ArrayList<Riesenie> novaPopulacia = new ArrayList<>();
    private ArrayList<Spoj> spoje;
    private Riesenie DNNR = new Riesenie();
    private int velkostPopulacie;

    public Geneticky_algoritmus(ArrayList<Spoj> nacitaneSpoje, int velkostPopulacie) {
        this.spoje = nacitaneSpoje;
        this.velkostPopulacie = velkostPopulacie;
        this.vytvorPociatocnuPopulaciu(velkostPopulacie);
        for (Turnus turnus:this.dajNajlepsieRiesenie()) {
            this.DNNR.pridajTurnus(turnus);
        }
    }

    public void vytvorPociatocnuPopulaciu(int velkostPopulacie) {
        for (int i = 0; i < velkostPopulacie; i++) {
            Riesenie noveRiesenie = new Riesenie();
            for (Spoj spoj : this.spoje) {
                if (noveRiesenie.getTurnusy().size() == 0) {
                    Turnus novyTurnus = new Turnus();
                    novyTurnus.pridajSpoj(spoj);
                    noveRiesenie.pridajTurnus(novyTurnus);
                } else {
                    boolean pridany = false;
                    for (Turnus turnus : noveRiesenie.getTurnusy()) {
                        if (turnus.pridalSa(spoj)) {
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

    public void genetickyAlgoritmus(int pocetIteracii, int pocetNeaktualizovaniaDNNR, double pravdepodobnostKrizenia, int pocetMutacii, double pravdepodobnostMutacie, double percentoTopRieseni) {
        int iteracia = 1;
        int neaktualizovaneDNNR = 0;
        boolean dosloKAktualizaciiDNNR = false;
        while ((iteracia <= pocetIteracii) && (neaktualizovaneDNNR < pocetNeaktualizovaniaDNNR)) {
            this.naplnenieTopRieseniami(percentoTopRieseni);
            while (this.novaPopulacia.size() != this.velkostPopulacie) {
                if (Math.random() <= pravdepodobnostKrizenia) {
                    int indexRodic1 = this.dajPoziciuRodica();
                    int indexRodic2;
                    do {
                        indexRodic2 = this.dajPoziciuRodica();
                    } while (indexRodic1 == indexRodic2);
                    Riesenie potomok = this.krizenie(indexRodic1, indexRodic2);
                    //TODO opravit potomka po krizeni
                    for (int i = 0; i < pocetMutacii; i++) {
                        if (Math.random() <= pravdepodobnostMutacie) {
                            this.mutacia(potomok);
                        }
                    }
                    this.ohodnotRiesenie(potomok);
                    if (this.jeLepsiAkoDNNR(potomok)) {
                        this.aktualizujDNNR(potomok);
                        dosloKAktualizaciiDNNR = true;
                    }
                    this.novaPopulacia.add(potomok);
                }
            }
            this.nahradenieStarejNovou();
            iteracia++;
            if (dosloKAktualizaciiDNNR) {
                neaktualizovaneDNNR = 0;
            } else {
                neaktualizovaneDNNR++;
            }
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

    private int dajPoziciuRodica() {
        Random random = new Random();
        return random.nextInt(0, this.staraPopulacia.size());
    }

    private Riesenie krizenie(int poziciaRodica1, int poziciaRodica2) {
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

    private void mutacia(Riesenie potomok) {
        ArrayList<Spoj> vybraneSpoje = new ArrayList<>();
        Random random = new Random();
        int indexTurnusu = 0;
        int pocetVybranychSpojov = random.nextInt(1, potomok.getPocetSpojov() + 1);
        for (int i = pocetVybranychSpojov; i > 0; i--) {
            do {
                indexTurnusu = random.nextInt(0, potomok.getTurnusy().size());
            } while (potomok.getTurnusy().get(indexTurnusu).getSpoje().size() == 0);
            int indexSpoja = random.nextInt(0, potomok.getTurnusy().get(indexTurnusu).getSpoje().size());
            Spoj vybrany = potomok.getTurnusy().get(indexTurnusu).getSpoje().remove(indexSpoja);
            if (potomok.getTurnusy().get(indexTurnusu).getSpoje().size() == 0) {
                potomok.getTurnusy().remove(indexTurnusu);
            }
            vybraneSpoje.add(vybrany);
        }
        Collections.shuffle(vybraneSpoje);
        for (Spoj spoj : vybraneSpoje) {
            if (potomok.getTurnusy().size() == 0) {
                Turnus novyTurnus = new Turnus();
                novyTurnus.pridajSpoj(spoj);
                potomok.pridajTurnus(novyTurnus);
            } else {
                boolean pridany = false;
                for (Turnus turnus : potomok.getTurnusy()) {
                    if (turnus.pridalSa(spoj)) {
                        pridany = true;
                        break;
                    }
                }
                if (!pridany) {
                    Turnus novyTurnus = new Turnus();
                    novyTurnus.pridajSpoj(spoj);
                    potomok.pridajTurnus(novyTurnus);
                }
            }
        }
    }

    private ArrayList<Turnus> dajNajlepsieRiesenie() {
        Riesenie najlepsieRiesenie = new Riesenie();
        najlepsieRiesenie.setOhodnotenie(0);
        for (Riesenie riesenie:this.staraPopulacia) {
            if (riesenie.getOhodnotenie() > najlepsieRiesenie.getOhodnotenie()) {
                najlepsieRiesenie = riesenie;
            }
        }
        return najlepsieRiesenie.getTurnusy();
    }

    private void nahradenieStarejNovou() {
        if (this.staraPopulacia.size() > 0) {
            this.staraPopulacia.subList(0, this.staraPopulacia.size()).clear();
        }
        this.staraPopulacia.addAll(this.novaPopulacia);
        if (this.novaPopulacia.size() > 0) {
            this.novaPopulacia.subList(0, this.novaPopulacia.size()).clear();
        }
    }

    private void naplnenieTopRieseniami(double percentoTopRieseni) {
        ArrayList<Riesenie> pomocnaPopulacia = new ArrayList<>(this.staraPopulacia);
        double pocet;
        if (percentoTopRieseni > 0 && percentoTopRieseni <= 1) {
            pocet = ((percentoTopRieseni * 100) * this.velkostPopulacie) / 100;
            pomocnaPopulacia.sort(Comparator.comparingDouble(Riesenie::getOhodnotenie));
            for (int i = 0; i <= pocet; i++) {
                this.novaPopulacia.add(pomocnaPopulacia.get(i));
            }
        } else {
            System.out.println("Nesprávne zadané percento top riešení. Má byť medzi 0 a 1");
        }
    }

    private boolean jeLepsiAkoDNNR(Riesenie potomok) {
        return (this.DNNR.getOhodnotenie() > potomok.getOhodnotenie());
    }

    private void aktualizujDNNR(Riesenie potomok) {
        if (this.DNNR.getTurnusy().size() > 0) {
            this.DNNR.getTurnusy().subList(0, this.DNNR.getTurnusy().size()).clear();
        }
        this.DNNR.getTurnusy().addAll(potomok.getTurnusy());
    }

    public void vypisDNNR() {
        this.DNNR.vypis();
    }
}
