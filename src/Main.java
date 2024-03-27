package src;

import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException {
        int cisloDatasetu = 1;

        Vstup scanner = new Vstup();
        ArrayList<Spoj> nacitaneSpoje = scanner.nacitajSpoje("input_data/spoje_id_DS" + cisloDatasetu + "_J_1.csv");
        int[][] maticaVzdialenosti = scanner.nacitajMaticuVzdialenosti("input_data/Tij_DS" + cisloDatasetu + "_J_1.csv");
        double[][] maticaSpotreby = scanner.nacitajMaticuSpotreby("input_data/Cij_DS" + cisloDatasetu + "_J_1.csv");

        // odstranenie diep
        nacitaneSpoje.remove(0);
        nacitaneSpoje.remove(nacitaneSpoje.size() - 1);

//        GenetickyAlgoritmus GA = new GenetickyAlgoritmus(nacitaneSpoje, maticaVzdialenosti, maticaSpotreby, 200);
//        GA.genetickyAlgoritmus(5.0, 500.0, 0.5, 5.0, 0.25, 0.1);
//        GA.vypisDNNR();

//        double vysledok = 0;
//        int pocetReplikacii = 50;
//        for (int i = 0; i < pocetReplikacii; i++) {
//            GenetickyAlgoritmus GA = new GenetickyAlgoritmus(nacitaneSpoje, maticaVzdialenosti, maticaSpotreby, 1100);
//            GA.genetickyAlgoritmus(10.0, 900.0, 0.2, 5, 0.05, 0.35);
//            GA.vypisDNNR();
//            vysledok += GA.dajOhodnotenieDNNR();
//        }
//        System.out.println("Priemerné ohodnotenie DNNR: " + vysledok / pocetReplikacii);

        MetodaSupra metodaSupra = new MetodaSupra(nacitaneSpoje, maticaVzdialenosti, maticaSpotreby);
        metodaSupra.metodaSupra(10, 0.5, 0.05, 10, 0.25, 5);
        metodaSupra.vypisDNNR();

//        KlasickeNastavovanie nastavovanie = new KlasickeNastavovanie(nacitaneSpoje, maticaVzdialenosti, maticaSpotreby);
//        nastavovanie.nastavParameter(2.0, 1502.0, 100.0, 15, "velkostPopulacie");
//        nastavovanie.vypisCelkoveVysledky();
//        nastavovanie.vypisNajlepsieNastavenie();
    }
}