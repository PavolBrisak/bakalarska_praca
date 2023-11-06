import java.io.IOException;
import java.util.ArrayList;

public class main {
    public static void main(String[] args) throws IOException {
        Vstup scanner = new Vstup();
        ArrayList<Spoj> nacitaneSpoje = scanner.nacitajSpoje("data_spoje.csv");
        for (Spoj spoj:nacitaneSpoje) {
            System.out.println(spoj.toString());
        }
    }
}