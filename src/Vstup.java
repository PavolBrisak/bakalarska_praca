package src;

import src.Spoj;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Vstup {
    private final ArrayList<Spoj> spoje = new ArrayList<>();
    public Vstup() {

    }

    public ArrayList<Spoj> read(String nazovSuboru) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(nazovSuboru))) {
            br.readLine();
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
                    Spoj spojObj = new Spoj(index, id, spoj, linka, miestoOd, miestoDo, casOd, casDo, trvanie, vzdialenost, spotreba);
                    this.spoje.add(spojObj);
                }
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this.spoje;
    }
}