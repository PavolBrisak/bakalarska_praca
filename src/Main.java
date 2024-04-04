package src;

import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException {
        boolean end = false;

        System.out.println("Vitajte v aplikacii pre riešenie úlohy návrhu turnusov pomocou genetického algoritmu a metódy SUPRA.");

        while (!end) {
            System.out.println("Vyberte si, s čím chcete pracovať:");
            Moznosti moznosti = new Moznosti();
            moznosti.addOption(1, "Genetický algoritmus");
            moznosti.addOption(2, "Metóda Supra");
            moznosti.addOption(3, "Grid search");
            int volba = moznosti.getOptionCLI("Vyberte si možnosť", true);

            System.out.println("Zvolte si dataset, s ktorým chcete pracovať:");
            int cisloDatasetu = Pomocnik.readIntFromConsole("Zadajte číslo datasetu: ", 1, 10);
            Vstup scanner = new Vstup();
            ArrayList<Spoj> nacitaneSpoje = scanner.nacitajSpoje("input_data/spoje_id_DS" + cisloDatasetu + "_J_1.csv");
            int[][] maticaVzdialenosti = scanner.nacitajMaticuVzdialenosti("input_data/Tij_DS" + cisloDatasetu + "_J_1.csv");
            double[][] maticaSpotreby = scanner.nacitajMaticuSpotreby("input_data/Cij_DS" + cisloDatasetu + "_J_1.csv");

            // odstranenie diep
            nacitaneSpoje.remove(0);
            nacitaneSpoje.remove(nacitaneSpoje.size() - 1);

            System.out.println("Zvolená metóda: " + moznosti.getOption(volba).getTitle());

            switch (volba) {
                case 1:
                    System.out.println("Zadajte parametre genetického algoritmu:");
                    double pocetMinut = Pomocnik.readDoubleFromConsole("Zadajte počet minút: ", 0.5);
                    double pocetNeaktualizovaniaDNNR = Pomocnik.readIntFromConsole("Zadajte počet neaktualizovania DNNR: ", 10);
                    double pravdepodobnostKrizenia = Pomocnik.readDoubleFromConsole("Zadajte pravdepodobnosť kríženia: ", 0.05, 1.0);
                    double pocetMutacii = Pomocnik.readIntFromConsole("Zadajte počet mutácií: ", 0);
                    double pravdepodobnostMutacie = Pomocnik.readDoubleFromConsole("Zadajte pravdepodobnosť mutácie: ", 0.0, 1.0);
                    double percentoTopRieseni = Pomocnik.readDoubleFromConsole("Zadajte percento top riešení: ", 0.0, 1.0);
                    double velkostPopulacie = Pomocnik.readIntFromConsole("Zadajte veľkosť populácie: ", 2);
                    int pocetOpakovani = Pomocnik.readIntFromConsole("Zadajte počet replikácií: ", 1);

                    double vysledok = 0;
                    System.out.println("Spúšťam genetický algoritmus...");
                    for (int i = 0; i < pocetOpakovani; i++) {
                        GenetickyAlgoritmus GA = new GenetickyAlgoritmus(nacitaneSpoje, maticaVzdialenosti, maticaSpotreby, velkostPopulacie);
                        GA.run(pocetMinut, pocetNeaktualizovaniaDNNR, pravdepodobnostKrizenia, pocetMutacii, pravdepodobnostMutacie, percentoTopRieseni);
                        GA.vypisDNNR();
                        vysledok += GA.dajOhodnotenieDNNR();
                        System.out.println();
                    }
                    System.out.println("Priemerné ohodnotenie DNNR: " + vysledok / pocetOpakovani);
                    System.out.println();

                    break;
                case 2:
                    System.out.println("Zadajte parametre metódy Supra:");
                    int N = Pomocnik.readIntFromConsole("Zadajte počet krokov: ", 1);
                    double A = Pomocnik.readDoubleFromConsole("Zadajte hodnotu A: ", 0.01, 1.0);
                    double B = Pomocnik.readDoubleFromConsole("Zadajte hodnotu B: ", 0.0, 2.0);
                    double C = Pomocnik.readDoubleFromConsole("Zadajte hodnotu C: ", 0.0, 1.0);
                    int max_s = Pomocnik.readIntFromConsole("Zadajte počet skúšok: ", 1);
                    int maxPocetAb = Pomocnik.readIntFromConsole("Zadajte koľko bodov p sa vytvorí: ", 1);

                    System.out.println("Spúšťam metódu Supra...");
                    MetodaSupra metodaSupra = new MetodaSupra(nacitaneSpoje, maticaVzdialenosti, maticaSpotreby);
                    metodaSupra.run(N, B, C, max_s, A, maxPocetAb);
                    metodaSupra.vypisDNNR();

                    break;
                case 3:
                    System.out.println("Zvolte si parameter, ktorý chcete nastaviť:");
                    Moznosti moznostiGridSearch = new Moznosti();
                    moznostiGridSearch.addOption(1, "Počet minút", 1.0, 100.0);
                    moznostiGridSearch.addOption(2, "Počet neaktualizovania DNNR", 10.0, 1000.0);
                    moznostiGridSearch.addOption(3, "Pravdepodobnosť kríženia", 0.05, 1.0);
                    moznostiGridSearch.addOption(4, "Počet mutácií", 0.0, 100.0);
                    moznostiGridSearch.addOption(5, "Pravdepodobnosť mutácie", 0.0, 1.0);
                    moznostiGridSearch.addOption(6, "Percento top riešení", 0.0, 1.0);
                    moznostiGridSearch.addOption(7, "Veľkosť populácie", 2.0, 1500.0);
                    int volbaGridSearch = moznostiGridSearch.getOptionCLI("Vyberte si možnosť", true);

                    double zaciatok = Pomocnik.readDoubleFromConsole("Zadajte začiatok: ", moznostiGridSearch.getOption(volbaGridSearch).getMin(), moznostiGridSearch.getOption(volbaGridSearch).getMax());
                    double koniec = Pomocnik.readDoubleFromConsole("Zadajte koniec: ", zaciatok, moznostiGridSearch.getOption(volbaGridSearch).getMax());
                    double krok = Pomocnik.readDoubleFromConsole("Zadajte krok: ", 0.005, koniec - zaciatok);
                    int pocetReplikacii = Pomocnik.readIntFromConsole("Zadajte počet replikácií: ", 1);

                    System.out.println("Spúšťam grid search...");
                    GridSearch gridSearch = new GridSearch(nacitaneSpoje, maticaVzdialenosti, maticaSpotreby);
                    gridSearch.nastavParameter(zaciatok, koniec, krok, pocetReplikacii, moznostiGridSearch.getOption(volbaGridSearch).getTitle());
                    gridSearch.vypisCelkoveVysledky();
                    gridSearch.vypisNajlepsieNastavenie();
                    break;
            }

            System.out.println();
            System.out.println("Chcete ísť odznova?");
            Moznosti moznostiYesNo = new Moznosti();
            moznostiYesNo.addYesNoOptions();
            int pokracovat = moznostiYesNo.getOptionCLI("Vyberte si možnosť", false);
            end = pokracovat != 0;
        }
        System.out.println("Ďakujem za použitie aplikácie.");
    }
}