package src;

import java.util.*;

public class GenetickyAlgoritmus {
    private ArrayList<Riesenie> staraPopulacia = new ArrayList<>();
    private ArrayList<Riesenie> novaPopulacia = new ArrayList<>();
    private ArrayList<Spoj> spoje;
    private ArrayList<Spoj> pomocneSpoje = new ArrayList<>();
    private Riesenie DNNR = new Riesenie();
    private Random random = new Random();
    private int velkostPopulacie;
    private int[][] maticaVzdialenosti;
    private double[][] maticaSpotreby;

    public GenetickyAlgoritmus(ArrayList<Spoj> nacitaneSpoje, int[][] maticaVzdialenosti, double[][] maticaSpotreby, int velkostPopulacie) {
        this.spoje = new ArrayList<>(nacitaneSpoje);
        this.pomocneSpoje.addAll(this.spoje);
        this.maticaVzdialenosti = maticaVzdialenosti;
        this.maticaSpotreby = maticaSpotreby;
        this.velkostPopulacie = velkostPopulacie;
        this.vytvorPociatocnuPopulaciu(this.velkostPopulacie);
        for (Turnus turnus : this.dajNajlepsieRiesenie()) {
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
                        if (turnus.pridalSa(spoj, this.maticaVzdialenosti)) {
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

    public void genetickyAlgoritmus(double pocetIteracii, double pocetNeaktualizovaniaDNNR, double pravdepodobnostKrizenia, double pocetMutacii, double pravdepodobnostMutacie, double percentoTopRieseni) {
        int iteracia = 1;
        int neaktualizovaneDNNR = 0;
        while ((iteracia <= pocetIteracii) && (neaktualizovaneDNNR < pocetNeaktualizovaniaDNNR)) {
            boolean dosloKAktualizaciiDNNR = false;
//            System.out.println("iteracia - " + iteracia);
//            System.out.println("neaktulanizovaneDNNR - " + neaktualizovaneDNNR);
            this.naplnenieTopRieseniami(percentoTopRieseni);
            while (this.novaPopulacia.size() < this.velkostPopulacie) {
//                System.out.println("iteracia - " + iteracia);
//                System.out.println("neaktulanizovaneDNNR - " + neaktualizovaneDNNR);
//                System.out.println(this.staraPopulacia.size());
//                System.out.println(this.novaPopulacia.size());

                if (Math.random() <= pravdepodobnostKrizenia) {
                    int indexRodic1 = this.dajPoziciuRodica();
                    int indexRodic2;
                    do {
                        indexRodic2 = this.dajPoziciuRodica();
                    } while (indexRodic1 == indexRodic2);
                    Riesenie potomok = new Riesenie();
                    this.krizenie(indexRodic1, indexRodic2, potomok);
                    this.opravPotomka(potomok);
                    for (int i = 0; i < pocetMutacii; i++) {
                        double nahoda = this.random.nextDouble(0, 1);
                        if (nahoda <= pravdepodobnostMutacie) {
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
        int pocetVymen = this.random.nextInt(1, this.spoje.size() + 1);
        for (int i = 0; i < pocetVymen; i++) {
            int index1 = this.random.nextInt(this.spoje.size());
            int index2;
            do {
                index2 = this.random.nextInt(this.spoje.size());
            } while (index2 == index1);
            Collections.swap(this.spoje, index1, index2);
        }
    }

    public void ohodnotRiesenie(Riesenie riesenie) {
        //(pocet autobusov * vaha) + suma(spotreba)
        int vaha = 100;
        double ohodnotenie = 0.0;

        riesenie.setOhodnotenie(riesenie.getTurnusy().size());
    }

    private int dajPoziciuRodica() {
        double sumaOhodnoteni = 0.0;
        for (Riesenie riesenie:this.staraPopulacia) {
            sumaOhodnoteni += riesenie.getOhodnotenie();
        }
        double random = this.random.nextDouble();
        double pomocna = 0.0;
        for (Riesenie riesenie:this.staraPopulacia) {
            pomocna += riesenie.getOhodnotenie() / sumaOhodnoteni;
            if (random < pomocna) {
                return this.staraPopulacia.indexOf(riesenie);
            }
        }
        return 0;
    }

    private void krizenie(int poziciaRodica1, int poziciaRodica2, Riesenie potomok) {
        int bodKrizeniaRodic1 = this.random.nextInt(0, this.staraPopulacia.get(poziciaRodica1).getTurnusy().size());
        for (int i = 0; i <= bodKrizeniaRodic1; i++) {

            potomok.pridajTurnus(this.staraPopulacia.get(poziciaRodica1).getTurnusy().get(i));
        }
        int bodKrizeniaRodic2 = this.random.nextInt(0, this.staraPopulacia.get(poziciaRodica2).getTurnusy().size());
        for (int i = bodKrizeniaRodic2; i < this.staraPopulacia.get(poziciaRodica2).getTurnusy().size(); i++) {
            potomok.pridajTurnus(this.staraPopulacia.get(poziciaRodica2).getTurnusy().get(i));
        }
        potomok.vymazPrazdneTurnusy();
    }

    private void opravPotomka(Riesenie potomok) {
        int[] pocetOpakovani = new int[this.pomocneSpoje.size()];
        Iterator<Turnus> iteratorTurnusy = potomok.getTurnusy().iterator();
        while (iteratorTurnusy.hasNext()) {
            Turnus turnus = iteratorTurnusy.next();
            Iterator<Spoj> iteratorSpoje = turnus.getSpoje().iterator();
            while (iteratorSpoje.hasNext()) {
                Spoj spoj = iteratorSpoje.next();
                if (pocetOpakovani[spoj.getIndex()] == 1) {
                    iteratorSpoje.remove();
                } else {
                    pocetOpakovani[spoj.getIndex()]++;
                }
            }
        }
        for (int i = 0; i < pocetOpakovani.length; i++) {
            if (pocetOpakovani[i] == 0) {
                boolean pridany = false;
                for (Turnus turnus : potomok.getTurnusy()) {
                    if (turnus.pridalSa(this.pomocneSpoje.get(i), this.maticaVzdialenosti)) {
                        pridany = true;
                        break;
                    }
                }

                if (!pridany) {
                    Turnus novyTurnus = new Turnus();
                    novyTurnus.pridajSpoj(this.pomocneSpoje.get(i));
                    potomok.pridajTurnus(novyTurnus);
                }
            }
        }
        potomok.vymazPrazdneTurnusy();
    }

    private void mutacia(Riesenie potomok) {
        ArrayList<Spoj> vybraneSpoje = new ArrayList<>();
        int indexTurnusu = 0;
        int pocetVybranychSpojov = this.random.nextInt(1, potomok.getPocetSpojov() + 1);
        for (int i = pocetVybranychSpojov; i > 0; i--) {
            do {
                indexTurnusu = this.random.nextInt(0, potomok.getTurnusy().size());
            } while (potomok.getTurnusy().get(indexTurnusu).getSpoje().size() == 0);
            int indexSpoja = this.random.nextInt(0, potomok.getTurnusy().get(indexTurnusu).getSpoje().size());
            Spoj vybrany = potomok.getTurnusy().get(indexTurnusu).getSpoje().remove(indexSpoja);
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
                    if (turnus.pridalSa(spoj, this.maticaVzdialenosti)) {
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
        potomok.vymazPrazdneTurnusy();
    }

    private ArrayList<Turnus> dajNajlepsieRiesenie() {
        Riesenie najlepsieRiesenie = new Riesenie();
        najlepsieRiesenie.setOhodnotenie(Integer.MAX_VALUE);
        for (Riesenie riesenie : this.staraPopulacia) {
            if (riesenie.getOhodnotenie() < najlepsieRiesenie.getOhodnotenie()) {
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
        if (percentoTopRieseni >= 0 && percentoTopRieseni <= 1) {
            pocet = ((percentoTopRieseni * 100) * this.velkostPopulacie) / 100;
            pomocnaPopulacia.sort(Comparator.comparingDouble(Riesenie::getOhodnotenie));
            for (int i = 0; i < pocet; i++) {
                this.novaPopulacia.add(pomocnaPopulacia.get(i));
            }
        } else {
            System.out.println("Nesprávne zadané percento top riešení. Má byť medzi 0 a 1");
        }
    }

    private boolean jeLepsiAkoDNNR(Riesenie potomok) {
        return (potomok.getOhodnotenie() < this.DNNR.getOhodnotenie());
    }

    private void aktualizujDNNR(Riesenie potomok) {
        if (this.DNNR.getTurnusy().size() > 0) {
            this.DNNR.getTurnusy().subList(0, this.DNNR.getTurnusy().size()).clear();
        }
        this.DNNR.getTurnusy().addAll(potomok.getTurnusy());
        this.DNNR.vymazPrazdneTurnusy();
    }

    public void vypisDNNR() {
        this.DNNR.vypis();
    }

    public void vypisPocetVozidielDNNR() {
        System.out.println("Počet vozidiel: " + this.DNNR.getTurnusy().size());
    }
}
