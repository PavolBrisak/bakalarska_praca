package src;

import java.util.ArrayList;

public class KlasickeNastavovanie {
    private ArrayList<Spoj> spoje;
    private int[][] maticaVzdialenosti;
    private double[][] maticaSpotreby;

    private double[] nastavenia;
    private double[] vyhodnotenia;

    KlasickeNastavovanie(ArrayList<Spoj> nacitaneSpoje, int[][] maticaVzdialenosti, double[][] maticaSpotreby) {
        this.spoje = nacitaneSpoje;
        this.maticaVzdialenosti = maticaVzdialenosti;
        this.maticaSpotreby = maticaSpotreby;
    }

    public void nastavParameter(double zaciatok, double koniec, double krok, int pocetReplikacii, String typ) {
        this.nastavenia = new double[(int) ((koniec - zaciatok) / krok) + 1];
        this.vyhodnotenia = new double[(int) ((koniec - zaciatok) / krok) + 1];

        for (double p = zaciatok; p <= koniec; p += krok) {
            double vysledok = 0;
            for (int i = 0; i < pocetReplikacii; i++) {
                vysledok += this.zbehniGenetickyAlgoritmus(p, typ);
            }
            this.nastavenia[(int) ((p - zaciatok) / krok)] = p;
            this.vyhodnotenia[(int) ((p - zaciatok) / krok)] = vysledok / pocetReplikacii;
        }
    }

    private double zbehniGenetickyAlgoritmus(double p, String typ) {
        GenetickyAlgoritmus GA = new GenetickyAlgoritmus(this.spoje, this.maticaVzdialenosti, this.maticaSpotreby, 150);
        switch (typ) {
            case "pocetMinut":
                GA.genetickyAlgoritmus(p, 500, 1.0, 10.0, 0.5, 0.1);
                return GA.dajOhodnotenieDNNR();
            case "pocetNeaktualizovaniaDNNR":
                GA.genetickyAlgoritmus(5, p, 1.0, 10.0, 0.5, 0.1);
                return GA.dajOhodnotenieDNNR();
            case "pravdepodobnostKrizenia":
                GA.genetickyAlgoritmus(5, 500, p, 10.0, 0.5, 0.1);
                return GA.dajOhodnotenieDNNR();
            case "pocetMutacii":
                GA.genetickyAlgoritmus(5, 500, 1.0, p, 0.5, 0.1);
                return GA.dajOhodnotenieDNNR();
            case "pravdepodobnostMutacie":
                GA.genetickyAlgoritmus(5, 500, 1.0, 10.0, p, 0.1);
                return GA.dajOhodnotenieDNNR();
            case "percentoTopRieseni":
                GA.genetickyAlgoritmus(5, 500, 1.0, 10.0, 0.5, p);
                return GA.dajOhodnotenieDNNR();
            case "velkostPopulacie":
                GA = new GenetickyAlgoritmus(this.spoje, this.maticaVzdialenosti, this.maticaSpotreby, p);
                GA.genetickyAlgoritmus(5, 500, 1.0, 10.0, 0.5, 0.1);
                return GA.dajOhodnotenieDNNR();
            default:
                //TODO doplnit vynimku
                return 0;
        }
    }

    private void vypisCelkoveVysledky() {
        for (int i = 0; i < this.nastavenia.length; i++) {
            System.out.println(this.nastavenia[i] + " " + this.vyhodnotenia[i]);
        }
    }

    private void vypisNajlepsieNastavenie() {
        double najlepsieNastavenie = this.nastavenia[0];
        double najlepsieVyhodnotenie = this.vyhodnotenia[0];
        for (int i = 1; i < this.nastavenia.length; i++) {
            if (this.vyhodnotenia[i] < najlepsieVyhodnotenie) {
                najlepsieNastavenie = this.nastavenia[i];
                najlepsieVyhodnotenie = this.vyhodnotenia[i];
            }
        }
        System.out.println("Najlepsie nastavenie: " + najlepsieNastavenie + ", vysledok: " + najlepsieVyhodnotenie);
    }
}
