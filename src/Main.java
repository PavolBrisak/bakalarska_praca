package src;

import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException {
        int cisloDatasetu = 6;

        Vstup scanner = new Vstup();
        ArrayList<Spoj> nacitaneSpoje = scanner.nacitajSpoje("input_data/spoje_id_DS" + cisloDatasetu + "_J_1.csv");
        int[][] maticaVzdialenosti = scanner.nacitajMaticuVzdialenosti("input_data/Tij_DS" + cisloDatasetu + "_J_1.csv");
        double[][] maticaSpotreby = scanner.nacitajMaticuSpotreby("input_data/Cij_DS" + cisloDatasetu + "_J_1.csv");

        // odstranenie diep
        nacitaneSpoje.remove(0);
        nacitaneSpoje.remove(nacitaneSpoje.size() - 1);

//        double vysledok = 0;
//        for (int i = 0; i < 1000; i++) {
//            GenetickyAlgoritmus GA = new GenetickyAlgoritmus(nacitaneSpoje, maticaVzdialenosti, maticaSpotreby, 193.01124474197195);
//            GA.genetickyAlgoritmus(1.0034184843979481, 968.5783682223001, 0.9997013866805557, 0.0, 0.0014571988944675565, 0.7786107018961468);
//            GA.vypisDNNR();
//            vysledok += GA.dajOhodnotenieDNNR();
//        }
//        System.out.println(vysledok / 1000);


        MetodaSupra metodaSupra = new MetodaSupra(nacitaneSpoje, maticaVzdialenosti, maticaSpotreby);
        metodaSupra.metodaSupra(25, 0.5, 0.05, 20, 0.3);
        metodaSupra.vypisDNNR();

//        KlasickeNastavovanie nastavovanie = new KlasickeNastavovanie(nacitaneSpoje, maticaVzdialenosti, maticaSpotreby);
//        nastavovanie.nastavParameter(2.0, 1500.0, 50.0, 100, "velkostPopulacie");
//        nastavovanie.vypisCelkoveVysledky();
//        nastavovanie.vypisNajlepsieNastavenie();
    }
}