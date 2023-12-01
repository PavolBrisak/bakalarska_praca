package src;

import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException {
        Vstup scanner = new Vstup();
        ArrayList<Spoj> nacitaneSpoje = scanner.nacitajSpoje("input_data/spoje_id_DS6_J_1.csv");
        int[][] maticaVzdialenosti = scanner.nacitajMaticuVzdialenosti("input_data/Tij_DS6_J_1.csv");

        Geneticky_algoritmus GA = new Geneticky_algoritmus(nacitaneSpoje,maticaVzdialenosti, nacitaneSpoje.size());
        GA.genetickyAlgoritmus(100,100,0.9,5,0.7, 0.4);
        GA.vypisDNNR();
    }
}