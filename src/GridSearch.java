package src;

import java.util.ArrayList;
import java.util.List;

public class GridSearch {
    private ArrayList<Spoj> spoje;
    private ArrayList<Double> fixneNastavenia = new ArrayList<>(List.of(5.0, 500.0, 0.5, 50.0, 0.5, 0.5, 500.0));
    private int[][] maticaVzdialenosti;
    private double[][] maticaSpotreby;

    private ArrayList<Double> nastavenia;
    private ArrayList<Double> vyhodnotenia;

    GridSearch(ArrayList<Spoj> nacitaneSpoje, int[][] maticaVzdialenosti, double[][] maticaSpotreby) {
        this.spoje = nacitaneSpoje;
        this.maticaVzdialenosti = maticaVzdialenosti;
        this.maticaSpotreby = maticaSpotreby;
    }

    public void nastavParameter(double zaciatok, double koniec, double krok, int pocetReplikacii, String typ) {
        this.nastavenia = new ArrayList<>();
        this.vyhodnotenia = new ArrayList<>();

        int pocetDesatinnychMiest = this.dajPocetDesatinnychMiest(krok);

        for (double p = zaciatok; p <= koniec; p += krok) {
            p = Math.round(p * Math.pow(10, pocetDesatinnychMiest)) / Math.pow(10, pocetDesatinnychMiest);
            double vysledok = 0;
            for (int i = 0; i < pocetReplikacii; i++) {
                vysledok += this.zbehniGenetickyAlgoritmus(p, typ);
            }
            this.nastavenia.add(p);
            this.vyhodnotenia.add(vysledok / pocetReplikacii);
        }
    }

    private double zbehniGenetickyAlgoritmus(double p, String typ) {
        GenetickyAlgoritmus GA = new GenetickyAlgoritmus(this.spoje, this.maticaVzdialenosti, this.maticaSpotreby, this.fixneNastavenia.get(6));
        switch (typ) {
            case "Počet minút":
                GA.run(p, this.fixneNastavenia.get(1), this.fixneNastavenia.get(2), this.fixneNastavenia.get(3), this.fixneNastavenia.get(4), this.fixneNastavenia.get(5));
                return GA.dajOhodnotenieDNNR();
            case "Počet neaktualizovania DNNR":
                GA.run(this.fixneNastavenia.get(0), p, this.fixneNastavenia.get(2), this.fixneNastavenia.get(3), this.fixneNastavenia.get(4), this.fixneNastavenia.get(5));
                return GA.dajOhodnotenieDNNR();
            case "Pravdepodobnosť kríženia":
                GA.run(this.fixneNastavenia.get(0), this.fixneNastavenia.get(1), p, this.fixneNastavenia.get(3), this.fixneNastavenia.get(4), this.fixneNastavenia.get(5));
                return GA.dajOhodnotenieDNNR();
            case "Počet mutácií":
                GA.run(this.fixneNastavenia.get(0), this.fixneNastavenia.get(1), this.fixneNastavenia.get(2), p, this.fixneNastavenia.get(4), this.fixneNastavenia.get(5));
                return GA.dajOhodnotenieDNNR();
            case "Pravdepodobnosť mutácie":
                GA.run(this.fixneNastavenia.get(0), this.fixneNastavenia.get(1), this.fixneNastavenia.get(2), this.fixneNastavenia.get(3), p, this.fixneNastavenia.get(5));
                return GA.dajOhodnotenieDNNR();
            case "Percento top riešení":
                GA.run(this.fixneNastavenia.get(0), this.fixneNastavenia.get(1), this.fixneNastavenia.get(2), this.fixneNastavenia.get(3), this.fixneNastavenia.get(4), p);
                return GA.dajOhodnotenieDNNR();
            case "Veľkosť populácie":
                GA = new GenetickyAlgoritmus(this.spoje, this.maticaVzdialenosti, this.maticaSpotreby, p);
                GA.run(this.fixneNastavenia.get(0), this.fixneNastavenia.get(1), this.fixneNastavenia.get(2), this.fixneNastavenia.get(3), this.fixneNastavenia.get(4), this.fixneNastavenia.get(5));
                return GA.dajOhodnotenieDNNR();
        }
        return 0;
    }

    public void vypisCelkoveVysledky() {
        for (int i = 0; i < this.nastavenia.size(); i++) {
            System.out.println("Nastavenie: " + this.nastavenia.get(i) + ", vysledok: " + this.vyhodnotenia.get(i));
        }
    }

    public void vypisNajlepsieNastavenie() {
        double najlepsieNastavenie = this.nastavenia.get(0);
        double najlepsieVyhodnotenie = this.vyhodnotenia.get(0);
        for (int i = 1; i < this.nastavenia.size(); i++) {
            if (this.vyhodnotenia.get(i) < najlepsieVyhodnotenie) {
                najlepsieNastavenie = this.nastavenia.get(i);
                najlepsieVyhodnotenie = this.vyhodnotenia.get(i);
            }
        }
        System.out.println("Najlepsie nastavenie: " + najlepsieNastavenie + ", vysledok: " + najlepsieVyhodnotenie);
    }

    private int dajPocetDesatinnychMiest(double cislo) {
        String cisloString = Double.toString(cislo);
        int indexDesatinnejBodky = cisloString.indexOf('.');
        return cisloString.length() - indexDesatinnejBodky - 1;
    }
}
