package src;

import java.util.ArrayList;
import java.util.Random;

public class MetodaSupra {
    private ArrayList<Spoj> spoje;
    private ArrayList<Double> pociatocneParametre = new ArrayList<>();
    private int[][] maticaVzdialenosti;
    private double[][] maticaSpotreby;
    private ArrayList<Double> DNNR = new ArrayList<>();
    private Riesenie DNNRriesenie = new Riesenie();
    private double ohodnotenieDNNR;
    private double novenohodnotenieDNNR;
    private Random rand = new Random();
    private double[] r;
    private double hornaHranicaPocetIteracii = 1500;
    private double hornaHranicaPocetMutacii = 100;
    private double hornaHranicaVelkostPopulacie = 1000;
    private double dolnaHranicaVelkostPopulacie = 2;

    MetodaSupra(ArrayList<Spoj> nacitaneSpoje, int[][] maticaVzdialenosti, double[][] maticaSpotreby) {
        this.spoje = new ArrayList<>(nacitaneSpoje);
        this.maticaVzdialenosti = maticaVzdialenosti;
        this.maticaSpotreby = maticaSpotreby;

        this.nastavPociatocneParametre();
        System.out.println("Pociatocne parametre");
        for (Double d : this.pociatocneParametre) {
            System.out.println(d);
        }

        GenetickyAlgoritmus GA = new GenetickyAlgoritmus(this.spoje, this.maticaVzdialenosti, this.maticaSpotreby, this.pociatocneParametre.get(6).intValue());
        GA.genetickyAlgoritmus(this.pociatocneParametre.get(0), this.pociatocneParametre.get(1), this.pociatocneParametre.get(2), this.pociatocneParametre.get(3), this.pociatocneParametre.get(4), this.pociatocneParametre.get(5));
        this.ohodnotenieDNNR = GA.dajOhodnotenieDNNR();
        GA.vypisDNNR();
        System.out.println("Ohodnotenie DNNR: " + this.ohodnotenieDNNR);

        this.DNNR.addAll(this.pociatocneParametre);
        System.out.println("DNNR");
        for (Double d : this.DNNR) {
            System.out.println(d);
        }

        this.r = new double[this.pociatocneParametre.size()];
    }

    private void nastavPociatocneParametre() {
        //pocetIteracii
        int pocetIteracii = (int) (Math.random() * this.hornaHranicaPocetIteracii + 1);
        this.pociatocneParametre.add((double) pocetIteracii);

        //pocetNeaktualizovaniaDNNR
        int pocetNeaktualizovaniaDNNR = (int) (Math.random() * pocetIteracii + 1);
        this.pociatocneParametre.add((double) pocetNeaktualizovaniaDNNR);

        //pravdepodobnostKrizenia
        double pravdepodobnostKrizenia = Math.random();
        this.pociatocneParametre.add(pravdepodobnostKrizenia);

        //pocetMutacii
        int pocetMutacii = (int) (Math.random() * this.hornaHranicaPocetMutacii + 1);
        this.pociatocneParametre.add((double) pocetMutacii);

        //pravdepodobnostMutacie
        double pravdepodobnostMutacie = Math.random();
        this.pociatocneParametre.add(pravdepodobnostMutacie);

        //percentoTopRieseni
        double percentoTopRieseni = Math.random();
        this.pociatocneParametre.add(percentoTopRieseni);

        //velkostPopulacie
        double velkostPopulacie = (int) (Math.random() * (this.hornaHranicaVelkostPopulacie - this.dolnaHranicaVelkostPopulacie + 1) + this.dolnaHranicaVelkostPopulacie);
        this.pociatocneParametre.add(velkostPopulacie);
    }

    public void metodaSupra(int pocetKrokov, double parameterZabudania, double intenzitaUcenia, int max_s) {
        double[] pk = new double[this.pociatocneParametre.size()];

        for (int i = 0; i < pk.length; i++) {
            pk[i] = this.pociatocneParametre.get(i);
        }

        for (int krok = 0; krok < pocetKrokov; krok++) {
            System.out.println("Krok: " + krok);

            double[] W = new double[this.pociatocneParametre.size()];

            // 1. faza
            int j = 0;

            while (j < max_s) {
                // náhodný vektor x
                double[] x = new double[this.pociatocneParametre.size()];
                double[] nahodneParametre = this.dajNahodneParametre();
                System.arraycopy(nahodneParametre, 0, x, 0, x.length);

                // vektor rj podľa vzťahu rj = w + x
                double[] rj = new double[this.pociatocneParametre.size()];
                for (int i = 0; i < rj.length; i++) {
                    rj[i] = W[i] + x[i];
                }

                this.skontrolujParametre(rj);

                // bod pkj na základe rj
                double[] pkj = new double[this.pociatocneParametre.size()];
                for (int i = 0; i < pkj.length; i++) {
                    pkj[i] = pk[i] + rj[i];
                }

                this.skontrolujParametre(pkj);

                GenetickyAlgoritmus GA = new GenetickyAlgoritmus(this.spoje, this.maticaVzdialenosti, this.maticaSpotreby, pkj[6]);
                GA.genetickyAlgoritmus(pkj[0], pkj[1], pkj[2], pkj[3], pkj[4], pkj[5]);
                this.novenohodnotenieDNNR = GA.dajOhodnotenieDNNR();
//                System.out.println("nove ohodnotenie: " + this.novenohodnotenieDNNR);
//                System.out.println("stare ohodnotenie: " + this.ohodnotenieDNNR);

                if (this.novenohodnotenieDNNR < this.ohodnotenieDNNR) {
                    this.ohodnotenieDNNR = this.novenohodnotenieDNNR;
                    for (int i = 0; i < pkj.length; i++) {
                        this.DNNR.set(i, pkj[i]);
                    }

                    Riesenie riesenie = GA.getDNNR();
                    this.DNNRriesenie = new Riesenie(riesenie);
                }

                //aktualizuj hodnoty r a w
                this.upravR(pkj, pk);
                this.upravW(W, parameterZabudania, intenzitaUcenia, pkj, pk);


                j++;
            }

            //2. faza
            int pocetAb = 0; //pocet krokov od posledneho zlepsenia hodnoty riesenia
            int maxPocetAb = 100;
            double alpha = 1;
            double minAlpha = 0.00001;
            double normaVektoraR = this.normaVektoraR();

            while (pocetAb < maxPocetAb && alpha >= minAlpha) {
                // vypocitaj novy bod p
                double[] p = new double[this.pociatocneParametre.size()];
                for (int i = 0; i < p.length; i++) {
                    p[i] = pk[i] + ((4 * alpha * this.r[i]) / normaVektoraR);
                }

                this.skontrolujParametre(p);

                GenetickyAlgoritmus GA = new GenetickyAlgoritmus(this.spoje, this.maticaVzdialenosti, this.maticaSpotreby, p[6]);
                GA.genetickyAlgoritmus(p[0], p[1], p[2], p[3], p[4], p[5]);
                this.novenohodnotenieDNNR = GA.dajOhodnotenieDNNR();
//                System.out.println("nove ohodnotenie: " + this.novenohodnotenieDNNR);
//                System.out.println("stare ohodnotenie: " + this.ohodnotenieDNNR);

                if (this.novenohodnotenieDNNR < this.ohodnotenieDNNR) {
                    this.ohodnotenieDNNR = this.novenohodnotenieDNNR;
                    for (int i = 0; i < p.length; i++) {
                        this.DNNR.set(i, p[i]);
                    }

                    Riesenie riesenie = GA.getDNNR();
                    this.DNNRriesenie = new Riesenie(riesenie);

                    pocetAb = 0;
                } else {
                    pocetAb++;
                }

                alpha = alpha / 2;
            }

            for (int i = 0; i < pk.length; i++) {
                pk[i] = this.DNNR.get(i);
            }
        }
    }

    private void skontrolujParametre(double[] rj) {
        for (int i = 0; i < rj.length; i++) {
            if (i == 0) {
                if (rj[i] < 0) {
                    rj[i] = 0.0;
                } else if (rj[i] > this.hornaHranicaPocetIteracii) {
                    rj[i] = this.hornaHranicaPocetIteracii;
                }
                rj[i] = Math.round(rj[i]);
            } else if (i == 1) {
                if (rj[i] < 0) {
                    rj[i] = 0.0;
                } else if (rj[i] > rj[0]) {
                    rj[i] = rj[0];
                }
                rj[i] = Math.round(rj[i]);
            } else if (i == 2) {
                if (rj[i] < 0) {
                    rj[i] = 0.0;
                } else if (rj[i] > 1) {
                    rj[i] = 1.0;
                }
            } else if (i == 3) {
                if (rj[i] < 0) {
                    rj[i] = 0.0;
                } else if (rj[i] > this.hornaHranicaPocetMutacii) {
                    rj[i] = this.hornaHranicaPocetMutacii;
                }
                rj[i] = Math.round(rj[i]);
            } else if (i == 4) {
                if (rj[i] < 0) {
                    rj[i] = 0.0;
                } else if (rj[i] > 1) {
                    rj[i] = 1.0;
                }
            } else if (i == 5) {
                if (rj[i] < 0) {
                    rj[i] = 0.0;
                } else if (rj[i] > 1) {
                    rj[i] = 1.0;
                }
            } else if (i == 6) {
                if (rj[i] < this.dolnaHranicaVelkostPopulacie) {
                    rj[i] = this.dolnaHranicaVelkostPopulacie;
                } else if (rj[i] > this.hornaHranicaVelkostPopulacie) {
                    rj[i] = this.hornaHranicaVelkostPopulacie;
                }
                rj[i] = Math.round(rj[i]);
            }
        }
    }

    private double[] dajNahodneParametre() {
        double[] nahodneParametre = new double[this.pociatocneParametre.size()];
        int i = 0;

        int pocetIteracii = (int) (Math.random() * this.hornaHranicaPocetIteracii + 1);
        nahodneParametre[i] = pocetIteracii;
        i++;

        //pocetNeaktualizovaniaDNNR
        int pocetNeaktualizovaniaDNNR = (int) (Math.random() * pocetIteracii + 1);
        nahodneParametre[i] = pocetNeaktualizovaniaDNNR;
        i++;

        //pravdepodobnostKrizenia
        double pravdepodobnostKrizenia = Math.random();
        nahodneParametre[i] = (pravdepodobnostKrizenia);
        i++;

        //pocetMutacii
        int pocetMutacii = (int) (Math.random() * this.hornaHranicaPocetMutacii + 1);
        nahodneParametre[i] = pocetMutacii;
        i++;

        //pravdepodobnostMutacie
        double pravdepodobnostMutacie = Math.random();
        nahodneParametre[i] = (pravdepodobnostMutacie);
        i++;

        //percentoTopRieseni
        double percentoTopRieseni = Math.random();
        nahodneParametre[i] = (percentoTopRieseni);

        //velkostPopulacie
        double velkostPopulacie = (int) (Math.random() * (this.hornaHranicaVelkostPopulacie - this.dolnaHranicaVelkostPopulacie + 1) + this.dolnaHranicaVelkostPopulacie);
        nahodneParametre[i] = velkostPopulacie;

        return nahodneParametre;
    }

    private double normaVektoraR() {
        double sum = 0;
        for (Double d : this.r) {
            sum += Math.pow(d, 2);
        }
        return Math.sqrt(sum);
    }

    private void upravR(double[] pkj, double[] pk) {
        for (int i = 0; i < this.r.length; i++) {
            this.r[i] = this.r[i] + ((this.novenohodnotenieDNNR - this.ohodnotenieDNNR) * (pkj[i] - pk[i]));
        }
        this.skontrolujParametre(this.r);
    }

    private void upravW(double[] W, double parameterZabudania, double intenzitaUcenia, double[] pkj, double[] pk) {
        for (int i = 0; i < W.length; i++) {
            W[i] = parameterZabudania * W[i] + intenzitaUcenia * ((this.novenohodnotenieDNNR - this.ohodnotenieDNNR) * (pkj[i] - pk[i]));
        }

        this.skontrolujParametre(W);
    }

    public void vypisDNNR() {
        System.out.println("DNNR parametre:");
        for (Double d : this.DNNR) {
            System.out.print(d);
            System.out.print(" ");
        }
        System.out.println();

        this.DNNRriesenie.vypis();
    }
}
