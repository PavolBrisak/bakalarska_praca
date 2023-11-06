public class Spoj {
    private String casOd;
    private String casDo;
    private String miestoOd;
    private String miestoDo;

    public Spoj(String casOd, String casDo, String miestoOd, String miestoDo) {
        this.casOd = casOd;
        this.casDo = casDo;
        this.miestoOd = miestoOd;
        this.miestoDo = miestoDo;
    }

    public void setCasOd(String casOd) {
        this.casOd = casOd;
    }

    public void setCasDo(String casDo) {
        this.casDo = casDo;
    }

    public void setMiestoOd(String miestoOd) {
        this.miestoOd = miestoOd;
    }

    public void setMiestoDo(String miestoDo) {
        this.miestoDo = miestoDo;
    }

    public String getCasOd() {
        return casOd;
    }

    public String getCasDo() {
        return casDo;
    }

    public String getMiestoOd() {
        return miestoOd;
    }

    public String getMiestoDo() {
        return miestoDo;
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
