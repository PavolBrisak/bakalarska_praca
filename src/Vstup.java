package src;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Vstup {
    private final ArrayList<Spoj> spoje = new ArrayList<>();
    public Vstup() {

    }

    public ArrayList<Spoj> nacitajSpoje(String nazovSuboru) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(nazovSuboru))) {
            br.readLine();
//            br.readLine();
            String line = br.readLine();
            while (line != null) {
                String[] parts = line.split(";");
                if (parts.length == 11) {
                    int index = Integer.parseInt(parts[0]);
                    String id = parts[1];
                    String spoj = parts[2];
                    String linka = parts[3];
                    int miestoOd = Integer.parseInt(parts[4]);
                    int miestoDo = Integer.parseInt(parts[5]);
                    int casOd = Integer.parseInt(parts[6]);
                    int casDo = Integer.parseInt(parts[7]);
                    int trvanie = Integer.parseInt(parts[8]);
                    double vzdialenost = Double.parseDouble(parts[9]);
                    double spotreba = Double.parseDouble(parts[10]);
                    Spoj novySpoj = new Spoj(index, id, spoj, linka, miestoOd, miestoDo, casOd, casDo, trvanie, vzdialenost, spotreba);
                    this.spoje.add(novySpoj);
                }
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this.spoje;
    }

    public int[][] nacitajMaticuVzdialenosti(String nazovSuboru) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(nazovSuboru))) {
            int pocetRiadkov;
            int pocetStlpcov;
            String riadky = br.readLine();
            pocetRiadkov = Integer.parseInt(riadky);
            String stlpce = br.readLine();
            pocetStlpcov = Integer.parseInt(stlpce);

//            pocetRiadkov--;
//            pocetStlpcov--;
//            br.readLine();

            int[][] matica = new int[pocetRiadkov][pocetStlpcov];
            for (int i = 0; i < pocetRiadkov; i++) {
                String line = br.readLine();
                String[] parts = line.split(";");

                for (int j = 0; j < pocetStlpcov; j++) {
                    matica[i][j] = Integer.parseInt(parts[j]);
                }
            }
            return matica;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public double[][] nacitajMaticuSpotreby(String nazovSuboru) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(nazovSuboru))) {
            int pocetRiadkov;
            int pocetStlpcov;
            String riadky = br.readLine();
            pocetRiadkov = Integer.parseInt(riadky);
            String stlpce = br.readLine();
            pocetStlpcov = Integer.parseInt(stlpce);

//            pocetRiadkov--;
//            pocetStlpcov--;
//            br.readLine();

            double[][] matica = new double[pocetRiadkov][pocetStlpcov];
            for (int i = 0; i < pocetRiadkov; i++) {
                String line = br.readLine();
                String[] parts = line.split(";");

                for (int j = 0; j < pocetStlpcov; j++) {
                    matica[i][j] = Double.parseDouble(parts[j]);
                }
            }
            return matica;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}