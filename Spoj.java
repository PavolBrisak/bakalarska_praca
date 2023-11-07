import java.lang.Integer;

public class Spoj {
    private String casOdString;
    private String casDoString;
    private String miestoOd;
    private String miestoDo;
    private int casOd;
    private int casDo;
    private int id;


    public Spoj(String casOd, String casDo, String miestoOd, String miestoDo, int id) {
        this.casOdString = casOd;
        this.casDoString = casDo;
        this.miestoOd = miestoOd;
        this.miestoDo = miestoDo;
        this.casOd = this.getCasInSeconds(this.casOdString);
        this.casDo = this.getCasInSeconds(this.casDoString);
        this.id = id;

    }

    public int getCasInSeconds(String cas) {
        cas = cas.replace("\uFEFF", "").trim();
        String[] timeParts = cas.split(":");
        if (timeParts.length == 2) {
            int hours = Integer.parseInt(timeParts[0]);
            int minutes = Integer.parseInt(timeParts[1]);
            return hours * 3600 + minutes * 60;
        } else {
            throw new IllegalArgumentException("Invalid time format for casOd: " + cas);
        }
    }

    public int getCasOd() {
        return this.casOd;
    }

    public void setCasOd(int casOd) {
        this.casOd = casOd;
    }

    public int getCasDo() {
        return this.casDo;
    }

    public void setCasDo(int casDo) {
        this.casDo = casDo;
    }


    public String getMiestoOd() {
        return this.miestoOd;
    }

    public void setMiestoOd(String miestoOd) {
        this.miestoOd = miestoOd;
    }

    public String getMiestoDo() {
        return this.miestoDo;
    }

    public void setMiestoDo(String miestoDo) {
        this.miestoDo = miestoDo;
    }

    public int getId() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Spoj{" +
                "id='" + this.id + '\'' +
                "casOd='" + this.casOd + '\'' +
                ", casDo='" + this.casDo + '\'' +
                ", miestoOd='" + this.miestoOd + '\'' +
                ", miestoDo='" + this.miestoDo + '\'' +
                '}';
    }
}
