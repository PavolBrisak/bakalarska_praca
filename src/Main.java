package src;

import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException {
        Vstup scanner = new Vstup();
        ArrayList<Spoj> nacitaneSpoje = scanner.nacitajSpoje("input_data/spoje_id_DS1_J_1.csv");
        int[][] maticaVzdialenosti = scanner.nacitajMaticuVzdialenosti("input_data/Tij_DS1_J_1.csv");

//        Geneticky_algoritmus GA = new Geneticky_algoritmus(nacitaneSpoje,maticaVzdialenosti, 50);
//        GA.genetickyAlgoritmus(1000,200,0.3,5,0.4, 0.2);
//        GA.vypisDNNR();
    }
}