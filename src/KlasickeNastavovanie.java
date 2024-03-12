package src;

import java.util.ArrayList;

public class KlasickeNastavovanie {
    private ArrayList<Spoj> spoje;
    private int[][] maticaVzdialenosti;
    private double[][] maticaSpotreby;

    private ArrayList<Double> nastavenia;
    private ArrayList<Double> vyhodnotenia;

    KlasickeNastavovanie(ArrayList<Spoj> nacitaneSpoje, int[][] maticaVzdialenosti, double[][] maticaSpotreby) {
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
