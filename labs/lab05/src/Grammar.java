import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

// g1 is in course 6, page 7
public class Grammar {
    private Set<String> nonTerminalSymbols = new LinkedHashSet<>();
    private Set<String> terminalSymbols = new LinkedHashSet<>();
    private Map<Set<String>, Set<List<String>>> productionRules = new LinkedHashMap<>();
    private String startSymbol;

    // content of grammar file (strings separated by spaces):
    // line 1: non-terminals
    // line 2: term
    // line 3: starting
    // line 4 -> ...: production rule: left-hand-side -> right-hand-side

    public Grammar(String filePathAsString) {
        this.readGrammarFromFile(Paths.get(filePathAsString));
    }

    private void readGrammarFromFile(Path filePath) {
        try {
            File grammarFile = new File(filePath.toUri());
            Scanner fileReader = new Scanner(grammarFile);
            int lineIndex = 1;
            while (fileReader.hasNextLine()) {
                String fileLine = fileReader.nextLine();
                if (lineIndex == 1) {
                    this.nonTerminalSymbols.addAll(Arrays.asList(fileLine.split(" ")));
                } else if (lineIndex == 2) {
                    this.terminalSymbols.addAll(Arrays.asList(fileLine.split(" ")));
                } else if (lineIndex == 3) {
                    this.startSymbol = fileLine;
                } else {
                    String[] productionRule = fileLine.split("->");
                    if (productionRule.length != 2) {
                        throw new RuntimeException();
                    }
                    String[] ruleLeftHandSide =
                            productionRule[0]
                                    .trim()
                                    .split(" ");

                    String[] ruleRightHandSide =
                            Arrays.stream(
                                            productionRule[1]
                                                    .trim()
                                                    .split(" "))
                                    .map(ruleSymbol -> (ruleSymbol == "ε") ? "" : ruleSymbol)
                                    .toArray(String[]::new);

                    if (this.productionRules.containsKey(new LinkedHashSet<>(Arrays.asList(ruleLeftHandSide)))) {
//                        TODO: dig for compute
                        this.productionRules.get(new LinkedHashSet<>(Arrays.asList(ruleLeftHandSide))).add(Arrays.asList(ruleRightHandSide));
                    } else {
                        this.productionRules.put(new LinkedHashSet<>(Arrays.asList(ruleLeftHandSide)), Collections.singleton(Arrays.asList(ruleRightHandSide)));
                    }
                }
                lineIndex += 1;
            }
            // map<string, list<string>>
            System.out.println("Finished");
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }
}
