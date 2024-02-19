package src;

import java.util.ArrayList;

public class MetodaSupra {
    ArrayList<Spoj> spoje;
    ArrayList<Double> pociatocneParametre = new ArrayList<>();
    private int[][] maticaVzdialenosti;
    private double[][] maticaSpotreby;
    int N = 100;

    MetodaSupra(ArrayList<Spoj> nacitaneSpoje, int[][] maticaVzdialenosti, double[][] maticaSpotreby, int size) {
        this.spoje = new ArrayList<>(nacitaneSpoje);
        this.maticaVzdialenosti = maticaVzdialenosti;
        this.maticaSpotreby = maticaSpotreby;

        this.nastavPociatocneParametre();
        for (Double d : this.pociatocneParametre) {
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
        for (int i = 0; i < this.N; i++) {

        }
    }


}
