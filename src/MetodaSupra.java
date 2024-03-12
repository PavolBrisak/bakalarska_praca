package src;

import java.util.ArrayList;
import java.util.List;

public class MetodaSupra {
    // pocetMinut pocetNeaktualizovaniaDNNR pravdepodobnostKrizenia pocetMutacii pravdepodobnostMutacie percentoTopRieseni velkostPopulacie
    private ArrayList<Double> pociatocneParametre = new ArrayList<Double>(List.of(1.0, 100.0, 0.5, 90.0, 0.5, 0.02, 15.0));
    private ArrayList<Double> horneADolneHranice = new ArrayList<Double>(List.of(1.0, 5.0, 10.0, 1000.0, 0.1, 1.0, 0.0, 100.0, 0.0, 1.0, 0.0, 1.0, 2.0, 1000.0));
    private ArrayList<Spoj> spoje;
    private int[][] maticaVzdialenosti;
    private double[][] maticaSpotreby;
    private ArrayList<Double> DNNR = new ArrayList<>();
    private Riesenie DNNRriesenie = new Riesenie();
    private double ohodnotenieDNNR;
    private double noveOhodnotenieDNNR;
    private double[] r;
    private double[] W;

    MetodaSupra(ArrayList<Spoj> nacitaneSpoje, int[][] maticaVzdialenosti, double[][] maticaSpotreby) {
        this.spoje = new ArrayList<>(nacitaneSpoje);
        this.maticaVzdialenosti = maticaVzdialenosti;
        this.maticaSpotreby = maticaSpotreby;

        GenetickyAlgoritmus GA = new GenetickyAlgoritmus(this.spoje, this.maticaVzdialenosti, this.maticaSpotreby, this.pociatocneParametre.get(6));
        GA.genetickyAlgoritmus(this.pociatocneParametre.get(0), this.pociatocneParametre.get(1), this.pociatocneParametre.get(2), this.pociatocneParametre.get(3), this.pociatocneParametre.get(4), this.pociatocneParametre.get(5));
        this.ohodnotenieDNNR = GA.dajOhodnotenieDNNR();

        this.DNNR.addAll(this.pociatocneParametre);

        this.r = new double[this.pociatocneParametre.size()];
        this.W = new double[this.pociatocneParametre.size()];
    }

    public void metodaSupra(int pocetKrokov, double parameterZabudania, double intenzitaUcenia, int max_s, double A) {
        double[] pk = new double[this.pociatocneParametre.size()];

        for (int i = 0; i < pk.length; i++) {
            pk[i] = this.pociatocneParametre.get(i);
        }

        for (int krok = 1; krok <= pocetKrokov; krok++) {
            System.out.println("Krok: " + krok);

            System.out.println("Pk:");
            for (double v : pk) {
                System.out.print(v + " ");
            }
            System.out.println();

            // 1. faza
            int j = 0;

            while (j < max_s) {
                // náhodný vektor x
                double[] x = new double[this.pociatocneParametre.size()];
                double[] nahodneParametre = this.dajNahodneParametreVOkoliA(A, pk);
                System.arraycopy(nahodneParametre, 0, x, 0, x.length);

                System.out.println("X:");
                for (double v : x) {
                    System.out.print(v + " ");
                }
                System.out.println();

                // vektor rj podľa vzťahu rj = w + x
                double[] rj = new double[this.pociatocneParametre.size()];
                for (int i = 0; i < rj.length; i++) {
                    rj[i] = this.W[i] + x[i];
                    rj[i] = Math.round(rj[i] * 100.0) / 100.0;
                }

                System.out.println("Rj:");
                for (double v : rj) {
                    System.out.print(v + " ");
                }
                System.out.println();

                // bod pkj na základe rj
                double[] pkj = new double[this.pociatocneParametre.size()];
                for (int i = 0; i < pkj.length; i++) {
                    pkj[i] = pk[i] + rj[i];
                    pkj[i] = Math.round(pkj[i] * 100.0) / 100.0;
                }

                this.skontrolujParametre(pkj);

                System.out.println("Pkj:");
                for (double v : pkj) {
                    System.out.print(v + " ");
                }
                System.out.println();

                GenetickyAlgoritmus GA = new GenetickyAlgoritmus(this.spoje, this.maticaVzdialenosti, this.maticaSpotreby, pkj[6]);
                GA.genetickyAlgoritmus(pkj[0], pkj[1], pkj[2], pkj[3], pkj[4], pkj[5]);
                this.noveOhodnotenieDNNR = GA.dajOhodnotenieDNNR();

                if (this.noveOhodnotenieDNNR < this.ohodnotenieDNNR) {
                    this.ohodnotenieDNNR = this.noveOhodnotenieDNNR;
                    for (int i = 0; i < pkj.length; i++) {
                        this.DNNR.set(i, pkj[i]);
                    }

                    Riesenie riesenie = GA.getDNNR();
                    this.DNNRriesenie = new Riesenie(riesenie);
                }

                //aktualizuj hodnoty r a w
                this.upravR(pkj, pk);
                this.upravW(this.W, parameterZabudania, intenzitaUcenia, pkj, pk);

                System.out.println("R:");
                for (double v : this.r) {
                    System.out.print(v + " ");
                }
                System.out.println();

                System.out.println("W:");
                for (double v : this.W) {
                    System.out.print(v + " ");
                }
                System.out.println();
                System.out.println("*******************************");

                j++;
            }

            //2. faza
            int pocetAb = 0; //pocet krokov od posledneho zlepsenia hodnoty riesenia
            int maxPocetAb = 10;
            double alpha = 1;
            double minAlpha = 0.05;
            double normaVektoraR = this.normaVektora(this.r);
            double stareOhodnotenieP = Double.MAX_VALUE;

//            double[] pomocna = this.dajNahodneParametreVOkoliA(A, pk);
            double[] pomocna = this.dajLokalnuHranicu(pk, A);

            while (pocetAb < maxPocetAb) {
                // vypocitaj novy bod p
                double[] p = new double[this.pociatocneParametre.size()];
                for (int i = 0; i < p.length; i++) {
                    p[i] = pk[i] + (pomocna[i] * alpha * (this.r[i] / normaVektoraR));
                    p[i] = Math.round(p[i] * 100.0) / 100.0;
                }

                this.skontrolujParametre(p);

                System.out.println("P:");
                for (double v : p) {
                    System.out.print(v + " ");
                }
                System.out.println();

                GenetickyAlgoritmus GA = new GenetickyAlgoritmus(this.spoje, this.maticaVzdialenosti, this.maticaSpotreby, p[6]);
                GA.genetickyAlgoritmus(p[0], p[1], p[2], p[3], p[4], p[5]);
                this.noveOhodnotenieDNNR = GA.dajOhodnotenieDNNR();

                if (this.noveOhodnotenieDNNR < stareOhodnotenieP) {
                    stareOhodnotenieP = this.noveOhodnotenieDNNR;

                    System.arraycopy(p, 0, pk, 0, pk.length);
                }

                if (this.noveOhodnotenieDNNR < this.ohodnotenieDNNR) {
                    this.ohodnotenieDNNR = this.noveOhodnotenieDNNR;
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
        }
    }

    private void skontrolujParametre(double[] vektor) {
        for (int i = 0; i < vektor.length; i++) {
            int index = i * 2;
            if (vektor[i] < this.horneADolneHranice.get(index)) {
                vektor[i] = this.horneADolneHranice.get(index);
            } else if (vektor[i] > this.horneADolneHranice.get(index + 1)) {
                vektor[i] = this.horneADolneHranice.get(index + 1);
            }
        }
    }

    private double[] dajNahodneParametreVOkoliA(double A, double[] pk) {
        double[] nahodneParametre = new double[this.pociatocneParametre.size()];

        for (int i = 0; i < pk.length; i++) {
            int index = i * 2;

            double rozdiel = this.horneADolneHranice.get(index + 1) - pk[i];
            double rozdiel2 = pk[i] - this.horneADolneHranice.get(index);
            double vacsiRozdiel = Math.max(rozdiel, rozdiel2);

            double hornaHranica = pk[i] + (vacsiRozdiel * A);
            double dolnaHranica = pk[i] - (vacsiRozdiel * A);
            double nahodnaHodnota = (Math.random() * (hornaHranica - dolnaHranica + 1) + dolnaHranica);
            nahodneParametre[i] = nahodnaHodnota;
            nahodneParametre[i] = Math.round(nahodneParametre[i] * 100.0) / 100.0;
        }

        return nahodneParametre;
    }

    private double[] dajLokalnuHranicu(double[] vektor, double A) {
        double[] hranice = new double[this.pociatocneParametre.size()];

        for (int i = 0; i < vektor.length; i++) {
            int index = i * 2;

            double rozdiel = this.horneADolneHranice.get(index + 1) - vektor[i];
            double rozdiel2 = vektor[i] - this.horneADolneHranice.get(index);
            double vacsiRozdiel = Math.max(rozdiel, rozdiel2);

            hranice[i] = (vacsiRozdiel * A);
        }

        return hranice;
    }

    private double normaVektora(double[] vektor) {
        double sum = 0;
        for (Double clen : vektor) {
            sum += Math.pow(clen, 2);
        }
        return Math.sqrt(sum);
    }

    private void upravR(double[] pkj, double[] pk) {
        for (int i = 0; i < this.r.length; i++) {
            this.r[i] = this.r[i] + ((this.ohodnotenieDNNR - this.noveOhodnotenieDNNR) * (pkj[i] - pk[i]));
            this.r[i] = Math.round(this.r[i] * 100.0) / 100.0;
        }
    }

    private void upravW(double[] W, double parameterZabudania, double intenzitaUcenia, double[] pkj, double[] pk) {
        for (int i = 0; i < W.length; i++) {
            W[i] = (parameterZabudania * W[i]) + intenzitaUcenia * ((this.ohodnotenieDNNR - this.noveOhodnotenieDNNR) * (pkj[i] - pk[i]));
            W[i] = Math.round(W[i] * 100.0) / 100.0;
        }
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
