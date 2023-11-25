package src;

import java.util.*;

public class Geneticky_algoritmus {
    private ArrayList<Riesenie> staraPopulacia = new ArrayList<>();
    private ArrayList<Riesenie> novaPopulacia = new ArrayList<>();
    private ArrayList<Spoj> spoje;
    private ArrayList<Spoj> pomocneSpoje = new ArrayList<>();
    private Riesenie DNNR = new Riesenie();
    private int velkostPopulacie;
    private int[][] maticaVzdialenosti;

    public Geneticky_algoritmus(ArrayList<Spoj> nacitaneSpoje, int[][] maticaVzdialenosti, int velkostPopulacie) {
        this.spoje = nacitaneSpoje;
        this.pomocneSpoje.addAll(this.spoje);
        this.maticaVzdialenosti = maticaVzdialenosti;
        this.velkostPopulacie = velkostPopulacie;
        this.vytvorPociatocnuPopulaciu(this.velkostPopulacie);
        for (Turnus turnus:this.dajNajlepsieRiesenie()) {
            this.DNNR.pridajTurnus(turnus);
        }
        this.DNNR.vypis();
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
//            noveRiesenie.vypis();
            this.staraPopulacia.add(noveRiesenie);
            this.novaPermutacia();
        }
    }

    public void genetickyAlgoritmus(int pocetIteracii, int pocetNeaktualizovaniaDNNR, double pravdepodobnostKrizenia, int pocetMutacii, double pravdepodobnostMutacie, double percentoTopRieseni) {
        int iteracia = 1;
        int neaktualizovaneDNNR = 0;
        while ((iteracia <= pocetIteracii) && (neaktualizovaneDNNR < pocetNeaktualizovaniaDNNR)) {
            boolean dosloKAktualizaciiDNNR = false;
            System.out.println("iteracia - " + iteracia);
            System.out.println("neaktulanizovaneDNNR - " + neaktualizovaneDNNR);

//            System.out.println("DNNR");
//            this.DNNR.vypis();

            this.naplnenieTopRieseniami(percentoTopRieseni);
//            System.out.println("stara");
//            for (Riesenie riesenie : this.staraPopulacia) {
//                riesenie.vypis();
//            }
//            System.out.println("nova");
//            for (Riesenie riesenie : this.novaPopulacia) {
//                riesenie.vypis();
//            }
            while (this.novaPopulacia.size() < this.velkostPopulacie) {
//                System.out.println(this.staraPopulacia.size());
//                System.out.println(this.novaPopulacia.size());
                if (Math.random() <= pravdepodobnostKrizenia) {
                    int indexRodic1 = this.dajPoziciuRodica();
                    int indexRodic2;
                    do {
                        indexRodic2 = this.dajPoziciuRodica();
                    } while (indexRodic1 == indexRodic2);
                    Riesenie potomok = this.krizenie(indexRodic1, indexRodic2);
//                    System.out.println("Potomok pred upravou");
//                    potomok.vypis();
                    this.opravPotomka(potomok);
//                    System.out.println("Potomok po uprave");
//                    potomok.vypis();

//                    System.out.println("Potomok pred mutaciou");
//                    potomok.vypis();
                    for (int i = 0; i < pocetMutacii; i++) {
                        if (Math.random() <= pravdepodobnostMutacie) {
                            this.mutacia(potomok);
//                            System.out.println("Potomok po mutacii");
//                            potomok.vypis();
                        }
                    }
                    this.ohodnotRiesenie(potomok);
                    if (this.jeLepsiAkoDNNR(potomok)) {
                        this.aktualizujDNNR(potomok);
                        dosloKAktualizaciiDNNR = true;
                    }
//                    System.out.println("Potomok pred vkladanim");
//                    potomok.vypis();
                    this.novaPopulacia.add(potomok);
                }
            }
//            System.out.println("nova");
//            for (Riesenie riesenie : this.novaPopulacia) {
//                riesenie.vypis();
//            }
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
        int pocetVymen = random.nextInt(1, this.spoje.size() + 1);
        for (int i = 0; i < pocetVymen; i++) {
            int index1 = random.nextInt(this.spoje.size());
            int index2;
            do {
                index2 = random.nextInt(this.spoje.size());
            } while (index2 == index1);
            Collections.swap(this.spoje, index1, index2);
        }
    }

    public void ohodnotRiesenie(Riesenie riesenie) {
        //Pocet autobusov
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
        for (int i = 0; i <= bodKrizeniaRodic1; i++) {
            potomok.pridajTurnus(this.staraPopulacia.get(poziciaRodica1).getTurnusy().get(i));
        }
        int bodKrizeniaRodic2 = random.nextInt(0,this.staraPopulacia.get(poziciaRodica2).getTurnusy().size());
        for (int i = bodKrizeniaRodic2; i < this.staraPopulacia.get(poziciaRodica2).getTurnusy().size(); i++) {
            potomok.pridajTurnus(this.staraPopulacia.get(poziciaRodica2).getTurnusy().get(i));
        }
        return potomok;
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
        for (Riesenie riesenie:this.staraPopulacia) {
            if (riesenie.getOhodnotenie() < najlepsieRiesenie.getOhodnotenie()) {
                najlepsieRiesenie = riesenie;
            }
        }
        return najlepsieRiesenie.getTurnusy();
    }

    private void nahradenieStarejNovou() {
//        System.out.println("stara");
//        for (Riesenie riesenie:this.staraPopulacia) {
//            riesenie.vypis();
//        }
        if (this.staraPopulacia.size() > 0) {
            this.staraPopulacia.subList(0, this.staraPopulacia.size()).clear();
        }
//        System.out.println("stara vymazana");
//        for (Riesenie riesenie:this.staraPopulacia) {
//            riesenie.vypis();
//        }
        this.staraPopulacia.addAll(this.novaPopulacia);
//        System.out.println("stara pridana");
//        for (Riesenie riesenie:this.staraPopulacia) {
//            riesenie.vypis();
//        }
//        System.out.println("nova");
//        for (Riesenie riesenie:this.novaPopulacia) {
//            riesenie.vypis();
//        }
        if (this.novaPopulacia.size() > 0) {
            this.novaPopulacia.subList(0, this.novaPopulacia.size()).clear();
        }
//        System.out.println("nova vymazana");
//        for (Riesenie riesenie:this.novaPopulacia) {
//            riesenie.vypis();
//        }
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
