package src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MetodaSupra {
    // pocetMinut pocetNeaktualizovaniaDNNR pravdepodobnostKrizenia pocetMutacii pravdepodobnostMutacie percentoTopRieseni velkostPopulacie
    private ArrayList<Double> pociatocneParametre = new ArrayList<Double>(List.of(5.5, 500.0, 0.5, 50.0, 0.5, 0.5, 500.0));
    private ArrayList<Double> horneADolneHranice = new ArrayList<Double>(List.of(1.0, 10.0, 10.0, 1000.0, 0.1, 1.0, 0.0, 100.0, 0.0, 1.0, 0.0, 1.0, 2.0, 1500.0));
    private ArrayList<Integer> pocetDesMiest = new ArrayList<Integer>(List.of(1, 0, 2, 0, 2, 2, 0));
    private ArrayList<Spoj> spoje;
    private int[][] maticaVzdialenosti;
    private double[][] maticaSpotreby;
    private ArrayList<Double> DNNR = new ArrayList<>();
    private Riesenie DNNRriesenie = new Riesenie();
    private double ohodnotenieDNNR;
    private double noveOhodnotenie;
    private double ohodnoteniePk;
    private double[] r;
    private double[] W;

    MetodaSupra(ArrayList<Spoj> nacitaneSpoje, int[][] maticaVzdialenosti, double[][] maticaSpotreby) {
        this.spoje = new ArrayList<>(nacitaneSpoje);
        this.maticaVzdialenosti = maticaVzdialenosti;
        this.maticaSpotreby = maticaSpotreby;

        GenetickyAlgoritmus GA = new GenetickyAlgoritmus(this.spoje, this.maticaVzdialenosti, this.maticaSpotreby, this.pociatocneParametre.get(6));
        GA.run(this.pociatocneParametre.get(0), this.pociatocneParametre.get(1), this.pociatocneParametre.get(2), this.pociatocneParametre.get(3), this.pociatocneParametre.get(4), this.pociatocneParametre.get(5));
        this.ohodnotenieDNNR = GA.dajOhodnotenieDNNR();
        this.ohodnoteniePk = GA.dajOhodnotenieDNNR();

        this.DNNR.addAll(this.pociatocneParametre);

        this.r = new double[this.pociatocneParametre.size()];
        this.W = new double[this.pociatocneParametre.size()];
    }

    public void run(int N, double B, double C, int max_s, double A, int maxPocetAb) {
        double[] pk = new double[this.pociatocneParametre.size()];

        for (int i = 0; i < pk.length; i++) {
            pk[i] = this.pociatocneParametre.get(i);
        }

        for (int krok = 1; krok <= N; krok++) {
            System.out.println("Krok: " + krok);

            // 1. faza
            int j = 0;

            Arrays.fill(this.r, 0);
            Arrays.fill(this.W, 0);

            while (j < max_s) {
                // náhodný vektor x
                double[] x = new double[this.pociatocneParametre.size()];
                double[] nahodneParametre = this.dajLokalnuHranicu(pk, A);
                System.arraycopy(nahodneParametre, 0, x, 0, x.length);

                for (int i = 0; i < x.length; i++) {
                    // daj nahodne cislo z intervalu <-x, x>
                    x[i] = Math.random() * (x[i] - (-x[i])) + (-x[i]);
                }

                this.zaokruhliParametre(x);

                // vektor rj podľa vzťahu rj = w + x
                double[] rj = new double[this.pociatocneParametre.size()];
                for (int i = 0; i < rj.length; i++) {
                    rj[i] = this.W[i] + x[i];
                }

                this.zaokruhliParametre(rj);

                // bod pkj na základe rj
                double[] pkj = new double[this.pociatocneParametre.size()];
                for (int i = 0; i < pkj.length; i++) {
                    pkj[i] = pk[i] + rj[i];
                }

                this.zaokruhliParametre(pkj);
                this.skontrolujParametre(pkj);

                GenetickyAlgoritmus GA = new GenetickyAlgoritmus(this.spoje, this.maticaVzdialenosti, this.maticaSpotreby, pkj[6]);
                GA.run(pkj[0], pkj[1], pkj[2], pkj[3], pkj[4], pkj[5]);
                this.noveOhodnotenie = GA.dajOhodnotenieDNNR();

                if (this.noveOhodnotenie < this.ohodnotenieDNNR) {
                    this.ohodnotenieDNNR = this.noveOhodnotenie;
                    for (int i = 0; i < pkj.length; i++) {
                        this.DNNR.set(i, pkj[i]);
                    }

                    Riesenie riesenie = GA.getDNNR();
                    this.DNNRriesenie = new Riesenie(riesenie);
                }

                //aktualizuj hodnoty r a w
                this.upravR(pkj, pk, this.ohodnoteniePk);
                this.upravW(this.W, B, C, pkj, pk, this.ohodnoteniePk);

                j++;
            }

            //2. faza
            int pocetAb = 0;
            double alpha = 1;
            double stareOhodnotenieP = Double.MAX_VALUE;
            double[] najlepsiP = new double[this.pociatocneParametre.size()];

            double[] pomocna = this.dajLokalnuHranicu(pk, A);

            while (pocetAb < maxPocetAb) {
                // vypocitaj novy bod p
                double[] p = new double[this.pociatocneParametre.size()];

                //prvy napad - problem ze sa nasobia malinke cisla a teda posun je minimalny
                for (int i = 0; i < p.length; i++) {
                    double absolutnaHodnota = Math.abs(this.r[i]);
                    p[i] = pk[i] + (pomocna[i] * alpha * (this.r[i] / absolutnaHodnota));
                }

                this.zaokruhliParametre(p);
                this.skontrolujParametre(p);

                GenetickyAlgoritmus GA = new GenetickyAlgoritmus(this.spoje, this.maticaVzdialenosti, this.maticaSpotreby, p[6]);
                GA.run(p[0], p[1], p[2], p[3], p[4], p[5]);
                this.noveOhodnotenie = GA.dajOhodnotenieDNNR();

                if (this.noveOhodnotenie < stareOhodnotenieP) {
                    stareOhodnotenieP = this.noveOhodnotenie;
                    System.arraycopy(p, 0, najlepsiP, 0, najlepsiP.length);
                }

                if (this.noveOhodnotenie < this.ohodnotenieDNNR) {
                    this.ohodnotenieDNNR = this.noveOhodnotenie;
                    for (int i = 0; i < p.length; i++) {
                        this.DNNR.set(i, p[i]);
                    }

                    Riesenie riesenie = GA.getDNNR();
                    this.DNNRriesenie = new Riesenie(riesenie);
                }

                pocetAb++;
                alpha = alpha / 2;
            }

            System.arraycopy(najlepsiP, 0, pk, 0, pk.length);
            this.ohodnoteniePk = stareOhodnotenieP;
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

    private double[] dajLokalnuHranicu(double[] vektor, double A) {
        double[] hranice = new double[this.pociatocneParametre.size()];

        for (int i = 0; i < vektor.length; i++) {
            int index = i * 2;

            double rozdiel = this.horneADolneHranice.get(index + 1) - vektor[i];
            double rozdiel2 = vektor[i] - this.horneADolneHranice.get(index);
            double vacsiRozdiel = Math.max(rozdiel, rozdiel2);

            hranice[i] = vacsiRozdiel * A;
        }

        return hranice;
    }

    private void upravR(double[] pkj, double[] pk, double ohodnoteniePk) {
        for (int i = 0; i < this.r.length; i++) {
            this.r[i] = this.r[i] + ((ohodnoteniePk - this.noveOhodnotenie) * (pkj[i] - pk[i]));
            this.r[i] = Math.round(this.r[i] * 100.0) / 100.0;
        }
    }

    private void upravW(double[] W, double B, double C, double[] pkj, double[] pk, double ohodnoteniePk) {
        for (int i = 0; i < W.length; i++) {
            W[i] = (B * W[i]) + C * ((ohodnoteniePk - this.noveOhodnotenie) * (pkj[i] - pk[i]));
            W[i] = Math.round(W[i] * 100.0) / 100.0;
        }
    }

    private void zaokruhliParametre(double[] vektor) {
        for (int i = 0; i < vektor.length; i++) {
            vektor[i] = Math.round(vektor[i] * Math.pow(10, this.pocetDesMiest.get(i))) / Math.pow(10, this.pocetDesMiest.get(i));
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
