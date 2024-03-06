package src;

import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException {
        int cisloDatasetu = 2;

        Vstup scanner = new Vstup();
        ArrayList<Spoj> nacitaneSpoje = scanner.nacitajSpoje("input_data/spoje_id_DS" + cisloDatasetu + "_J_1.csv");
        int[][] maticaVzdialenosti = scanner.nacitajMaticuVzdialenosti("input_data/Tij_DS" + cisloDatasetu + "_J_1.csv");
        double[][] maticaSpotreby = scanner.nacitajMaticuSpotreby("input_data/Cij_DS" + cisloDatasetu + "_J_1.csv");

        // odstranenie diep
        nacitaneSpoje.remove(0);
        nacitaneSpoje.remove(nacitaneSpoje.size() - 1);

//        GenetickyAlgoritmus GA = new GenetickyAlgoritmus(nacitaneSpoje, maticaVzdialenosti, maticaSpotreby, 25.0);
//        GA.genetickyAlgoritmus(3.0, 1800.0, 0.9, 10.0, 0.9, 0.1);
//        GA.vypisDNNR();


        MetodaSupra metodaSupra = new MetodaSupra(nacitaneSpoje, maticaVzdialenosti, maticaSpotreby);
        metodaSupra.metodaSupra(10, 0.5, 0.5, 10, 0.2);
        metodaSupra.vypisDNNR();

        KlasickeNastavovanie nastavovanie = new KlasickeNastavovanie(nacitaneSpoje, maticaVzdialenosti, maticaSpotreby);
        nastavovanie.nastavParameter(0.0, 1.0, 0.05, 100, "pravdepodobnostMutacie");
        nastavovanie.vypisCelkoveVysledky();
        nastavovanie.vypisNajlepsieNastavenie();
    }
}