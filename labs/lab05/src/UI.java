import java.util.*;

public class UI {
    private Grammar grammar = null;
    private boolean running = true;
    private final Map<String, String> options = new LinkedHashMap<>();
    private final Map<String, Runnable> optionsHandlers = new LinkedHashMap<>(); // may contain null handlers

    public UI() {
        addOptions();
    }

    //region running & handling options
    public void run() {
        Scanner scanner = new Scanner(System.in);

        while (running) {
            options.forEach((option, description) -> System.out.println(option + ". " + description));
            System.out.print("\t Your option: ");

            handleUserOption(scanner.nextLine());
            System.out.println();
        }
    }

    /**
     * @param option the option given by the user
     */
    private void handleUserOption(String option) {
        if (Objects.equals(option, "0")) {
            System.out.println("Exiting...");
            running = false;
        } else if (options.containsKey(option)) {
            chooseAndCallOptionHandler(option);
        } else {
            System.out.println("Invalid option");
        }
    }

    private void chooseAndCallOptionHandler(String option) {
        if (grammar == null && !Objects.equals(option, "1")) {
            System.out.println("You must read the grammar first!");
        } else {
            Runnable handler = optionsHandlers.get(option);

            if (handler != null)
                handler.run();
            else
                System.out.println("Not implemented yet");
        }
    }
    //endregion

    //region adding the options
    private void addOptions() {
        addOption("0", "Exit");
        addOption("1", "Read grammar from file", this::readGrammarFromFile);
        addOption("2", "Print set of non-terminals", this::printNonTerminals);
        addOption("3", "Print set of terminals", this::printTerminals);
        addOption("4", "Print set of productions", this::printProductions);
        addOption("5", "Print productions for a given non-terminal", this::printProductionsForNonTerminal);
        addOption("6", "CFG check", this::cfgCheck);
    }

    private void addOption(String option, String description, Runnable handler) {
        options.put(option, description);
        optionsHandlers.put(option, handler);
    }

    private void addOption(String option, String description) {
        addOption(option, description, null);
    }


    //endregion
    //region handlers

    private void readGrammarFromFile() {
        System.out.print("Please enter the filepath of the grammar: ");
        String filepath = new Scanner(System.in).nextLine();
        grammar = new Grammar(filepath);
    }

    private void printNonTerminals() {
        System.out.println(grammar.getNonTerminalSymbols());
    }

    private void printTerminals() {
        System.out.println(grammar.getTerminalSymbols());
    }

    private void printProductions() {
        Map<Set<String>, Set<List<String>>> productionRules = grammar.getProductionRules();

        productionRules.forEach((nonTerminal, productions) -> {
            productions.forEach(production -> {
                printProduction(nonTerminal, production);
            });
        });
    }

    private static void printProduction(Set<String> nonTerminal, List<String> production) {
        // print the left side of the production
        nonTerminal.stream().reduce((s, s2) -> s + " " + s2)
                .ifPresent(s -> System.out.print(s + " -> "));

        // print the right side of the production
        production.forEach(symbol -> System.out.print(symbol + " "));

        System.out.println();
    }

    private void printProductionsForNonTerminal() {
        System.out.print("Please enter the non-terminal: ");
        String nonTerminal = new Scanner(System.in).nextLine();

        Set<List<String>> productions = grammar.getProductionsForNonTerminal(nonTerminal);

        if (productions == null) {
            System.out.println("Invalid non-terminal");
        } else {
            System.out.println("Productions for " + nonTerminal + ":");

            productions.forEach(production -> {
                System.out.print(nonTerminal + " -> ");
                production.forEach(symbol -> System.out.print(symbol + " "));
                System.out.println();
            });
        }
    }

    private void cfgCheck() {
        System.out.println(grammar.isContextFree() ? "CFG" : "Not CFG");
    }
    //endregion
}
