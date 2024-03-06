package src;

import java.util.ArrayList;
import java.util.List;

public class MetodaSupra {
    private ArrayList<Double> pociatocneParametre = new ArrayList<>(List.of(5.0, 500.0, 1.0, 10.0, 0.5, 0.1, 150.0));
    private ArrayList<Spoj> spoje;
    private int[][] maticaVzdialenosti;
    private double[][] maticaSpotreby;
    private ArrayList<Double> DNNR = new ArrayList<>();
    private Riesenie DNNRriesenie = new Riesenie();
    private double ohodnotenieDNNR;
    private double noveOhodnotenieDNNR;
    private double[] r;
    private double[] W;
    private double hornaHranicaPocetMinut = 5;
    private double hornaHranicaPocetMutacii = 100;
    private double hornaHranicaVelkostPopulacie = 1000;
    private double dolnaHranicaVelkostPopulacie = 2;
    private double hornaHranicaPocetNeaktualizovaniaDNNR = 1000;

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

    public void metodaSupra(int pocetKrokov, double parameterZabudania, double intenzitaUcenia, int max_s, double okolieAplha) {
        double[] pk = new double[this.pociatocneParametre.size()];

        for (int i = 0; i < pk.length; i++) {
            pk[i] = this.pociatocneParametre.get(i);
        }

        for (int krok = 0; krok < pocetKrokov; krok++) {
            System.out.println("Krok: " + krok);

            // 1. faza
            int j = 0;

            while (j < max_s) {
                // náhodný vektor x
                double[] x = new double[this.pociatocneParametre.size()];
                double[] nahodneParametre = this.dajNahodneParametreVOkoliAlpha(okolieAplha, pk);
                System.arraycopy(nahodneParametre, 0, x, 0, x.length);

                // vektor rj podľa vzťahu rj = w + x
                double[] rj = new double[this.pociatocneParametre.size()];
                for (int i = 0; i < rj.length; i++) {
                    rj[i] = this.W[i] + x[i];
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

                j++;
            }

            //2. faza
            int pocetAb = 0; //pocet krokov od posledneho zlepsenia hodnoty riesenia
            int maxPocetAb = 100;
            double alpha = 1;
            double minAlpha = 0.05;
            double normaVektoraR = this.normaVektora(this.r);

            while (pocetAb < maxPocetAb && alpha >= minAlpha) {
                // vypocitaj novy bod p
                double[] p = new double[this.pociatocneParametre.size()];
                for (int i = 0; i < p.length; i++) {
                    p[i] = pk[i] + ((4 * alpha * this.r[i]) / normaVektoraR);
                }

                this.skontrolujParametre(p);

                GenetickyAlgoritmus GA = new GenetickyAlgoritmus(this.spoje, this.maticaVzdialenosti, this.maticaSpotreby, p[6]);
                GA.genetickyAlgoritmus(p[0], p[1], p[2], p[3], p[4], p[5]);
                this.noveOhodnotenieDNNR = GA.dajOhodnotenieDNNR();

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

            for (int i = 0; i < pk.length; i++) {
                pk[i] = this.DNNR.get(i);
            }
        }
    }

    private void skontrolujParametre(double[] vektor) {
        for (int i = 0; i < vektor.length; i++) {
            if (i == 0) {
                if (vektor[i] < 0) {
                    vektor[i] = 0.0;
                } else if (vektor[i] > this.hornaHranicaPocetMinut) {
                    vektor[i] = this.hornaHranicaPocetMinut;
                }
                vektor[i] = Math.round(vektor[i]);
            } else if (i == 1) {
                if (vektor[i] < 0) {
                    vektor[i] = 0.0;
                } else if (vektor[i] > this.hornaHranicaPocetNeaktualizovaniaDNNR) {
                    vektor[i] = this.hornaHranicaPocetNeaktualizovaniaDNNR;
                }
                vektor[i] = Math.round(vektor[i]);
            } else if (i == 2) {
                if (vektor[i] < 0) {
                    vektor[i] = 0.0;
                } else if (vektor[i] > 1) {
                    vektor[i] = 1.0;
                }
            } else if (i == 3) {
                if (vektor[i] < 0) {
                    vektor[i] = 0.0;
                } else if (vektor[i] > this.hornaHranicaPocetMutacii) {
                    vektor[i] = this.hornaHranicaPocetMutacii;
                }
                vektor[i] = Math.round(vektor[i]);
            } else if (i == 4) {
                if (vektor[i] < 0) {
                    vektor[i] = 0.0;
                } else if (vektor[i] > 1) {
                    vektor[i] = 1.0;
                }
            } else if (i == 5) {
                if (vektor[i] < 0) {
                    vektor[i] = 0.0;
                } else if (vektor[i] > 1) {
                    vektor[i] = 1.0;
                }
            } else if (i == 6) {
                if (vektor[i] < this.dolnaHranicaVelkostPopulacie) {
                    vektor[i] = this.dolnaHranicaVelkostPopulacie;
                } else if (vektor[i] > this.hornaHranicaVelkostPopulacie) {
                    vektor[i] = this.hornaHranicaVelkostPopulacie;
                }
                vektor[i] = Math.round(vektor[i]);
            }
        }
    }

    private double[] dajNahodneParametreVOkoliAlpha(double okolieAlpha, double[] pk) {
        double[] nahodneParametre = new double[this.pociatocneParametre.size()];
        int i = 0;

        //pocetMinut
        double hornaHranicaPocetMinut = pk[i] + ((this.hornaHranicaPocetMinut - pk[i]) * okolieAlpha);
        double dolnaHranicaPocetMinut = pk[i] - ((pk[i] - 0) * okolieAlpha);
        double pocetMinut = (Math.random() * (hornaHranicaPocetMinut - dolnaHranicaPocetMinut + 1) + dolnaHranicaPocetMinut);
        nahodneParametre[i] = pocetMinut;
        i++;

        //pocetNeaktualizovaniaDNNR
        double hornaHranicaPocetNeaktualizovaniaDNNR = pk[i] + ((this.hornaHranicaPocetNeaktualizovaniaDNNR - pk[i]) * okolieAlpha);
        double dolnaHranicaPocetNeaktualizovaniaDNNR = pk[i] - ((pk[i] - 0) * okolieAlpha);
        int pocetNeaktualizovaniaDNNR = (int) (Math.random() * (hornaHranicaPocetNeaktualizovaniaDNNR - dolnaHranicaPocetNeaktualizovaniaDNNR + 1) + dolnaHranicaPocetNeaktualizovaniaDNNR);
        nahodneParametre[i] = pocetNeaktualizovaniaDNNR;
        i++;

        //pravdepodobnostKrizenia
        double hornaHranicaPravdepodobnostKrizenia = pk[i] + ((1 - pk[i]) * okolieAlpha);
        double dolnaHranicaPravdepodobnostKrizenia = pk[i] - ((pk[i] - 0) * okolieAlpha);
        double pravdepodobnostKrizenia = (Math.random() * (hornaHranicaPravdepodobnostKrizenia - dolnaHranicaPravdepodobnostKrizenia + 1) + dolnaHranicaPravdepodobnostKrizenia);
        nahodneParametre[i] = (pravdepodobnostKrizenia);
        i++;

        //pocetMutacii
        double hornaHranicaPocetMutacii = pk[i] + ((this.hornaHranicaPocetMutacii - pk[i]) * okolieAlpha);
        double dolnaHranicaPocetMutacii = pk[i] - ((pk[i] - 0) * okolieAlpha);
        int pocetMutacii = (int) (Math.random() * (hornaHranicaPocetMutacii - dolnaHranicaPocetMutacii + 1) + dolnaHranicaPocetMutacii);
        nahodneParametre[i] = pocetMutacii;
        i++;

        //pravdepodobnostMutacie
        double hornaHranicaPravdepodobnostMutacie = pk[i] + ((1 - pk[i]) * okolieAlpha);
        double dolnaHranicaPravdepodobnostMutacie = pk[i] - ((pk[i] - 0) * okolieAlpha);
        double pravdepodobnostMutacie = (Math.random() * (hornaHranicaPravdepodobnostMutacie - dolnaHranicaPravdepodobnostMutacie + 1) + dolnaHranicaPravdepodobnostMutacie);
        nahodneParametre[i] = (pravdepodobnostMutacie);
        i++;

        //percentoTopRieseni
        double hornaHranicaPercentoTopRieseni = pk[i] + ((1 - pk[i]) * okolieAlpha);
        double dolnaHranicaPercentoTopRieseni = pk[i] - ((pk[i] - 0) * okolieAlpha);
        double percentoTopRieseni = (Math.random() * (hornaHranicaPercentoTopRieseni - dolnaHranicaPercentoTopRieseni + 1) + dolnaHranicaPercentoTopRieseni);
        nahodneParametre[i] = (percentoTopRieseni);
        i++;

        //velkostPopulacie
        double hornaHranicaVelkostPopulacie = pk[i] + ((this.hornaHranicaVelkostPopulacie - pk[i]) * okolieAlpha);
        double dolnaHranicaVelkostPopulacie = pk[i] - ((pk[i] - this.dolnaHranicaVelkostPopulacie) * okolieAlpha);
        double velkostPopulacie = (int) (Math.random() * (hornaHranicaVelkostPopulacie - dolnaHranicaVelkostPopulacie + 1) + dolnaHranicaVelkostPopulacie);
        nahodneParametre[i] = velkostPopulacie;

        return nahodneParametre;
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
            this.r[i] = this.r[i] + ((this.noveOhodnotenieDNNR - this.ohodnotenieDNNR) * (pkj[i] - pk[i]));
        }
        this.skontrolujParametre(this.r);
    }

    private void upravW(double[] W, double parameterZabudania, double intenzitaUcenia, double[] pkj, double[] pk) {
        for (int i = 0; i < W.length; i++) {
            W[i] = (parameterZabudania * W[i]) + intenzitaUcenia * ((this.ohodnotenieDNNR - this.noveOhodnotenieDNNR) * (pkj[i] - pk[i]));
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
