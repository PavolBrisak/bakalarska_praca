package src;

import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException {
        Vstup scanner = new Vstup();
        ArrayList<Spoj> nacitaneSpoje = scanner.nacitajSpoje("input_data/spoje_id_DS3_J_1.csv");
        int[][] maticaVzdialenosti = scanner.nacitajMaticuVzdialenosti("input_data/Tij_DS3_J_1.csv");
        double[][] maticaSpotreby = scanner.nacitajMaticuSpotreby("input_data/Cij_DS3_J_1.csv");

//        for (int i = 1; i <= 50; i++) {
//            System.out.println("Iterácia: " + i);
//            GenetickyAlgoritmus GA = new GenetickyAlgoritmus(nacitaneSpoje, maticaVzdialenosti, maticaSpotreby, nacitaneSpoje.size());
//            GA.genetickyAlgoritmus(1000.0,700.0,0.9,5.0,0.7, 0.4);
//            GA.vypisPocetVozidielDNNR();
//            GA.vypisDNNR();
//            System.out.println();
//        }

        MetodaSupra metodaSupra = new MetodaSupra(nacitaneSpoje, maticaVzdialenosti, maticaSpotreby, nacitaneSpoje.size());
        metodaSupra.metodaSupra();

    }
}