public class Spoj {
    private String casOdString;
    private String casDoString;
    private String miestoOd;
    private String miestoDo;
    private int casOd;
    private int casDo;


    public Spoj(String casOd, String casDo, String miestoOd, String miestoDo) {
        this.casOdString = casOd;
        this.casDoString = casDo;
        this.miestoOd = miestoOd;
        this.miestoDo = miestoDo;
        this.casDo = this.getCasInSeconds(this.casDoString);
        this.casOd = this.getCasInSeconds(this.casOdString);

    }

    public int getCasInSeconds(String cas) {
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
        return casOd;
    }

    public void setCasOd(int casOd) {
        this.casOd = casOd;
    }

    public int getCasDo() {
        return casDo;
    }

    public void setCasDo(int casDo) {
        this.casDo = casDo;
    }

    public String getCasOdString() {
        return casOdString;
    }

    public void setCasOdString(String casOdString) {
        this.casOdString = casOdString;
    }

    public String getCasDoString() {
        return casDoString;
    }

    public void setCasDoString(String casDoString) {
        this.casDoString = casDoString;
    }

    public String getMiestoOd() {
        return miestoOd;
    }

    public void setMiestoOd(String miestoOd) {
        this.miestoOd = miestoOd;
    }

    public String getMiestoDo() {
        return miestoDo;
    }

    public void setMiestoDo(String miestoDo) {
        this.miestoDo = miestoDo;
    }

    @Override
    public String toString() {
        return "Spoj{" +
                "casOd='" + casOd + '\'' +
                ", casDo='" + casDo + '\'' +
                ", miestoOd='" + miestoOd + '\'' +
                ", miestoDo='" + miestoDo + '\'' +
                '}';
    }
}
