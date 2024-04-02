package src;

public class Moznost {
    private final int id;
    private final String title;
    private double min;
    private double max;

    public Moznost(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public Moznost(int id, String title, double min, double max) {
        this.id = id;
        this.title = title;
        this.min = min;
        this.max = max;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }
}
