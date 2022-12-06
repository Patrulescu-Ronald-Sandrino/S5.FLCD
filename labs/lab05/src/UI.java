import java.util.*;

public class UI {
    private Grammar grammar = null;
    private boolean running = true;
    private final Map<String, String> options = new LinkedHashMap<>();
    private final Map<String, Runnable> optionsHandlers = new LinkedHashMap<>(); // may contain null handlers

    public UI() {
        addOptions();
    }

    private void addOptions() {
        addOption("0", "Exit");
        addOption("1", "Read grammar from file", this::readGrammarFromFile);
        addOption("2", "Print set of non-terminals");
        addOption("3", "Print set of terminals");
        addOption("4", "Print set of productions");
        addOption("5", "Print productions for a given non-terminal");
        addOption("6", "CFG check");
    }

    private void addOption(String option, String description, Runnable handler) {
        options.put(option, description);
        optionsHandlers.put(option, handler);
    }

    private void addOption(String option, String description) {
        addOption(option, description, null);
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);

        while (running) {
            options.forEach((option, description) -> System.out.println(option + ". " + description));
            System.out.print("\t Your option: ");

            handleUserOption(scanner.nextLine());
        }
    }

    /** @param option the option given by the user */
    private void handleUserOption(String option) {
        if (Objects.equals(option, "0")) {
            System.out.println("Exiting...");
            running = false;
        }
        else if (!options.containsKey(option)) {
            System.out.println("Invalid option");
        }
        else if (grammar == null && !Objects.equals(option, "1")) {
            System.out.println("You must read the grammar first!");
        }
        else {
            Runnable handler = optionsHandlers.get(option);

            if (handler != null)
                handler.run();
            else
                System.out.println("Not implemented yet");
        }
    }

    private void readGrammarFromFile() {
        System.out.print("Please enter the filepath of the grammar: ");
        String filepath = new Scanner(System.in).nextLine();
        grammar = new Grammar(filepath);
    }
}
