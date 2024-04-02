package src;

import java.util.ArrayList;
import java.util.List;

public class Moznosti {
    private List<Moznost> options = new ArrayList<>();

    public void addOption(int id, String title) {
        options.add(new Moznost(id, title));
    }

    public void addOption(int id, String title, double min, double max) {
        options.add(new Moznost(id, title, min, max));
    }

    public boolean hasOptions() {
        return !options.isEmpty();
    }

    public void addYesNoOptions() {
        addOption(YesNoOptions.YES.ordinal(), "Áno");
        addOption(YesNoOptions.NO.ordinal(), "Nie");
    }

    public int getOptionCLI(String question, boolean checkForMinMax) {
        System.out.println("\n" + question);

        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;

        for (Moznost option : options) {
            System.out.println("\t" + option.getId() + " - " + option.getTitle());
            if (checkForMinMax) {
                if (option.getId() < min) min = option.getId();
                if (option.getId() > max) max = option.getId();
            }
        }

        if (!checkForMinMax) {
            min = options.get(0).getId();
            max = options.get(options.size() - 1).getId();
        }

        return Pomocnik.readIntFromConsole(min, max);
    }

    enum YesNoOptions {
        YES,
        NO
    }

    public Moznost getOption(int id) {
        for (Moznost option : options) {
            if (option.getId() == id) {
                return option;
            }
        }
        return null;
    }
}
