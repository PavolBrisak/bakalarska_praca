package src;

import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException {
        Vstup scanner = new Vstup();
        ArrayList<Spoj> nacitaneSpoje = scanner.read("input_data/data_spoje.csv");
        for (Spoj spoj:nacitaneSpoje) {
            System.out.println(spoj.toString());
        }
        Geneticky_algoritmus GA = new Geneticky_algoritmus(nacitaneSpoje, 50);
        GA.genetickyAlgoritmus(1000,200,5,0.4, 0.2);
    }
}