package src;

import java.lang.Integer;

public class Spoj {
    private int index;
    private String id;
    private String spoj;
    private String linka;
    private int miestoOd;
    private int miestoDo;
    private int casOd;
    private int casDo;
    private int trvanie;
    private double vzdialenost;
    private double spotreba;


    public Spoj(int index, String id, String spoj, String linka, int miestoOd, int miestoDo, int casOd, int casDo, int trvanie, double vzdialenost, double spotreba) {
        this.index = index;
        this.id = id;
        this.spoj = spoj;
        this.linka = linka;
        this.miestoOd = miestoOd;
        this.miestoDo = miestoDo;
        this.casOd = casOd;
        this.casDo = casDo;
        this.trvanie = trvanie;
        this.vzdialenost = vzdialenost;
        this.spotreba = spotreba;
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


    @Override
    public String toString() {
        return "Spoj{" +
                "index=" + index +
                ", id='" + id + '\'' +
                ", spoj='" + spoj + '\'' +
                ", linka='" + linka + '\'' +
                ", miestoOd=" + miestoOd +
                ", miestoDo=" + miestoDo +
                ", casOd=" + casOd +
                ", casDo=" + casDo +
                ", trvanie=" + trvanie +
                ", vzdialenost=" + vzdialenost +
                ", spotreba=" + spotreba +
                '}';
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSpoj() {
        return spoj;
    }

    public void setSpoj(String spoj) {
        this.spoj = spoj;
    }

    public String getLinka() {
        return linka;
    }

    public void setLinka(String linka) {
        this.linka = linka;
    }

    public int getMiestoOd() {
        return miestoOd;
    }

    public void setMiestoOd(int miestoOd) {
        this.miestoOd = miestoOd;
    }

    public int getMiestoDo() {
        return miestoDo;
    }

    public void setMiestoDo(int miestoDo) {
        this.miestoDo = miestoDo;
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

    public int getTrvanie() {
        return trvanie;
    }

    public void setTrvanie(int trvanie) {
        this.trvanie = trvanie;
    }

    public double getVzdialenost() {
        return vzdialenost;
    }

    public void setVzdialenost(double vzdialenost) {
        this.vzdialenost = vzdialenost;
    }

    public double getSpotreba() {
        return spotreba;
    }

    public void setSpotreba(double spotreba) {
        this.spotreba = spotreba;
    }
}
