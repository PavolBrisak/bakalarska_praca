import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Vstup {
    private ArrayList<Spoj> spoje = new ArrayList<>();
    public Vstup() {

    }

    public ArrayList<Spoj> nacitajSpoje(String nazovSuboru) throws IOException{
        BufferedReader br = new BufferedReader(new FileReader(nazovSuboru));
        try {
            String line = br.readLine();
            while (line != null) {
                String[] parts = line.split(";");
                if (parts.length == 4) {
                    String casOd = parts[0];
                    String casDo = parts[1];
                    String miestoOd = parts[2];
                    String miestoDo = parts[3];
                    Spoj spoj = new Spoj(casOd, casDo, miestoOd, miestoDo);
                    spoje.add(spoj);
                }
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return this.spoje;
    }
}