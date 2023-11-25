package src;

import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException {
        Vstup scanner = new Vstup();
        ArrayList<Spoj> nacitaneSpoje = scanner.nacitajSpoje("input_data/spoje_id_DS1_J_1.csv");
        int[][] maticaVzdialenosti = scanner.nacitajMaticuVzdialenosti("input_data/Tij_DS1_J_1.csv");

        Geneticky_algoritmus GA = new Geneticky_algoritmus(nacitaneSpoje,maticaVzdialenosti, nacitaneSpoje.size() * 3);
        GA.genetickyAlgoritmus(20,20,0.9,5,0.7, 0.2);
        GA.vypisDNNR();
    }
}