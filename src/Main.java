package src;

import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException {
        Vstup scanner = new Vstup();
        ArrayList<Spoj> nacitaneSpoje = scanner.nacitajSpoje("input_data/spoje_id_DS1_J_1.csv");
        int[][] maticaVzdialenosti = scanner.nacitajMaticuVzdialenosti("input_data/Tij_DS1_J_1.csv");
        double[][] maticaSpotreby = scanner.nacitajMaticuSpotreby("input_data/Cij_DS1_J_1.csv");
        nacitaneSpoje.remove(0);

//        for (int i = 0; i < 25; i++) {
//            GenetickyAlgoritmus GA = new GenetickyAlgoritmus(nacitaneSpoje, maticaVzdialenosti, maticaSpotreby, 25.0);
//            GA.genetickyAlgoritmus(2000.0, 1800.0, 0.9, 10.0, 0.9, 0.1);
//            GA.vypisDNNR();


        MetodaSupra metodaSupra = new MetodaSupra(nacitaneSpoje, maticaVzdialenosti, maticaSpotreby);
        metodaSupra.metodaSupra(10, 0.4, 0.0009, 35);
        metodaSupra.vypisDNNR();

    }
}