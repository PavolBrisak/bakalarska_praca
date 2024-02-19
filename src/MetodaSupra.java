package src;

import java.util.ArrayList;
import java.util.Random;

public class MetodaSupra {
    ArrayList<Spoj> spoje;
    ArrayList<Double> pociatocneParametre = new ArrayList<>();
    private int[][] maticaVzdialenosti;
    private double[][] maticaSpotreby;
    int N = 100;
    ArrayList<Double> DNNR = new ArrayList<>();
    int ohodnotenieDNNR;
    int novenohodnotenieDNNR;
    private Random rand = new Random();
    ArrayList<Double> r = new ArrayList<>();
    double W = 0.0;

    MetodaSupra(ArrayList<Spoj> nacitaneSpoje, int[][] maticaVzdialenosti, double[][] maticaSpotreby, int size) {
        this.spoje = new ArrayList<>(nacitaneSpoje);
        this.maticaVzdialenosti = maticaVzdialenosti;
        this.maticaSpotreby = maticaSpotreby;

        this.nastavPociatocneParametre();
        System.out.println("Pociatocne parametre");
        for (Double d : this.pociatocneParametre) {
            System.out.println(d);
        }

        GenetickyAlgoritmus GA = new GenetickyAlgoritmus(this.spoje, this.maticaVzdialenosti, this.maticaSpotreby, this.spoje.size());
        GA.genetickyAlgoritmus(this.pociatocneParametre.get(0), this.pociatocneParametre.get(1), this.pociatocneParametre.get(2), this.pociatocneParametre.get(3), this.pociatocneParametre.get(4), this.pociatocneParametre.get(5));
        this.ohodnotenieDNNR = GA.dajOhodnotenieDNNR();
        GA.vypisDNNR();
        System.out.println("Ohodnotenie DNNR: " + this.ohodnotenieDNNR);

        this.DNNR.addAll(this.pociatocneParametre);
        System.out.println("DNNR");
        for (Double d : this.DNNR) {
            System.out.println(d);
        }
    }

    private void nastavPociatocneParametre() {
        //pocetIteracii
        int pocetIteracii = (int) (Math.random() * 2000 + 1);
        this.pociatocneParametre.add((double) pocetIteracii);

        //pocetNeaktualizovaniaDNNR
        int pocetNeaktualizovaniaDNNR = (int) (Math.random() * pocetIteracii + 1);
        this.pociatocneParametre.add((double) pocetNeaktualizovaniaDNNR);

        //pravdepodobnostKrizenia
        double pravdepodobnostKrizenia = Math.random();
        this.pociatocneParametre.add(pravdepodobnostKrizenia);

        //pocetMutacii
        int pocetMutacii = (int) (Math.random() * 100 + 1);
        this.pociatocneParametre.add((double) pocetMutacii);

        //pravdepodobnostMutacie
        double pravdepodobnostMutacie = Math.random();
        this.pociatocneParametre.add(pravdepodobnostMutacie);

        //percentoTopRieseni
        double percentoTopRieseni = Math.random();
        this.pociatocneParametre.add(percentoTopRieseni);
    }

    public void metodaSupra() {
        double[] pk = new double[this.pociatocneParametre.size()];

        for (int i = 0; i < pk.length; i++) {
            pk[i] = this.pociatocneParametre.get(i);
        }

        for (int krok = 0; krok < this.N; krok++) {
            // 1. faza
            int j = 0;
            int max_s = 30;

            while (j < max_s) {
                // náhodný vektor x
                double[] x = new double[this.pociatocneParametre.size()];
                for (int i = 0; i < x.length; i++) {
                    //x[i] =
                    //TODO zistit ako nastavit x
                }

                //TODO zistit ako nastavit w

                // vektor rj podľa vzťahu rj = w + x
                double[] rj = new double[this.pociatocneParametre.size()];
                for (int i = 0; i < rj.length; i++) {
                    rj[i] = this.W + x[i];
                }

                // bod pkj na základe rj
                double[] pkj = new double[this.pociatocneParametre.size()];
                for (int i = 0; i < pkj.length; i++) {
                    pkj[i] = pk[i] + rj[i];
                }

                GenetickyAlgoritmus GA = new GenetickyAlgoritmus(this.spoje, this.maticaVzdialenosti, this.maticaSpotreby, this.spoje.size());
                GA.genetickyAlgoritmus(pkj[0], pkj[1], pkj[2], pkj[3], pkj[4], pkj[5]);
                this.novenohodnotenieDNNR = GA.dajOhodnotenieDNNR();
                System.out.println("nove ohodnotenie: " + this.novenohodnotenieDNNR);
                System.out.println("stare ohodnotenie: " + this.ohodnotenieDNNR);

                if (this.novenohodnotenieDNNR < this.ohodnotenieDNNR) {
                    this.ohodnotenieDNNR = this.novenohodnotenieDNNR;
                    for (int i = 0; i < pkj.length; i++) {
                        this.DNNR.set(i, pkj[i]);
                    }
                }

                //aktualizuj hodnoty r a w
                this.upravR(pkj, pk);
                this.upravW();

                j++;
            }

            //2. faza
            int pocetAb = 0; //pocet krokov od posledneho zlepsenia hodnoty riesenia
            int maxPocetAb = 100;
            double alpha = 0.5; //TODO nastav na maxAlpha
            double minAlpha = 0.0001;
            double normaVektoraR = this.normaVektoraR();

            while (pocetAb < maxPocetAb && alpha >= minAlpha) {
                // vypocitaj novy bod p
                double[] p = new double[this.pociatocneParametre.size()];
                for (int i = 0; i < p.length; i++) {
                    p[i] = pk[i] + ((4 * alpha * this.r.get(i)) / normaVektoraR);
                }

                GenetickyAlgoritmus GA = new GenetickyAlgoritmus(this.spoje, this.maticaVzdialenosti, this.maticaSpotreby, this.spoje.size());
                GA.genetickyAlgoritmus(p[0], p[1], p[2], p[3], p[4], p[5]);
                this.novenohodnotenieDNNR = GA.dajOhodnotenieDNNR();
                System.out.println("nove ohodnotenie: " + this.novenohodnotenieDNNR);
                System.out.println("stare ohodnotenie: " + this.ohodnotenieDNNR);

                if (this.novenohodnotenieDNNR < this.ohodnotenieDNNR) {
                    this.ohodnotenieDNNR = this.novenohodnotenieDNNR;
                    for (int i = 0; i < p.length; i++) {
                        this.DNNR.set(i, p[i]);
                    }
                    pocetAb = 0;
                } else {
                    pocetAb++;
                }

                alpha = alpha / 2;
            }

            //TODO pokracuj novym pk
        }
    }

    private double normaVektoraR() {
        double sum = 0;
        for (Double d : this.r) {
            sum += Math.pow(d, 2);
        }
        return Math.sqrt(sum);
    }

    private void upravR(double[] pkj, double[] pk) {
        for (int i = 0; i < this.r.size(); i++) {
            this.r.set(i, this.r.get(i) + ((this.novenohodnotenieDNNR - this.ohodnotenieDNNR) * (pkj[i] - pk[i])));
        }
    }

    private void upravW() {
        //TODO zistit ako upravit w
//        this.W =
    }


}
