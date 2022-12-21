import java.util.*;

public class ParserAlgorithm {
    private static final ParsingConfiguration initialConfiguration = new ParsingConfiguration(Arrays.asList("S"));
    private final Grammar grammar;
    private final ParserStrategy strategy;
    private final List<String> sequence;

    public ParserAlgorithm(Grammar g, List<String> w) {
        this.grammar = g;
        this.strategy = new ParserStrategy(g);
        this.sequence = w;
    }

//    private ParsingTable createParsingTable(ParsingConfiguration finalConfiguration){
//        ParsingTree tree = new ParsingTree();
//        int treeIndex = 0;
//
//        List<String> alphaAsList = Util.reverse(new ArrayList<>(finalConfiguration.alpha));
//
//        while(treeIndex < alphaAsList.size()){
//            ParsingTreeNode symbolNode = new ParsingTreeNode(alphaAsList.get(treeIndex));
//            if(grammar.isNonTerminalSymbol(symbol)){
//                if(tree.root == null){
//                    tree.root = currentNode;
//                }
//                grammar.getLHSOfIthProductionRuleOfSymbol(symbol, finalConfiguration.indexMapping.get(treeIndex));
//            }
//        }
//    }

    public static void main(String[] args) {
        String grammarFilePathAsString = "../lab05/g1.txt";
        String[] word = {"a", "c", "b", "c"};

        Grammar grammar = new Grammar(grammarFilePathAsString);
        ParserAlgorithm parserAlgorithm = new ParserAlgorithm(grammar, Arrays.asList(word));
        parserAlgorithm.execute();
    }

    public void execute() {
        ParsingConfiguration config = initialConfiguration;

        while (config.s != ParsingState.FINAL && config.s != ParsingState.ERROR) {
            if (config.s == ParsingState.NORMAL) {
                if (config.i == sequence.size() + 1 && config.beta.isEmpty()) {
                    // SUCCESS
                    this.strategy.success(config);
                    config = config.next;
                } else {
                    if (grammar.isNonTerminalSymbol(config.beta.peek())) {
                        // EXPAND
                        this.strategy.expand(config);
                        config = config.next;
                    } else if (config.beta.peek().equals(this.sequence.get(config.i - 1))) {
                        // ADVANCE
                        this.strategy.advance(config);
                        config = config.next;
                    } else {
                        // MOMENTARY INSUCCESS
                        this.strategy.momentaryInsuccess(config);
                        config = config.next;
                    }
                }
            } else if (config.s == ParsingState.BACK) {
                if (grammar.isTerminalSymbol(config.alpha.peek())) {
                    // BACK
                    this.strategy.back(config);
                    config = config.next;
                } else {
                    // ANOTHER TRY
                    this.strategy.anotherTry(config);
                    config = config.next;
                }
            }
        }

        if (config.s == ParsingState.ERROR) {
            // PRINT ERROR MESSAGE
            System.out.println("ERROR");
            return;
        }

        // PRINT SUCCESS MESSAGE
        for (final List<String> row : configToTable(config)) {
            // java print table https://stackoverflow.com/questions/18672643/how-to-print-a-table-of-information-in-java
            System.out.printf(Util.leftJustifiedFormatting(4, 17) + "%n", row.toArray());
        }
    }

    private List<List<String>> configToTable(ParsingConfiguration config) {
        return treeToTable(alphaToTree(getAlphaFromConfig(config)));
    }

    // function that takes config and converts it into a stack of productions rules
    private Stack<Map.Entry<String, List<String>>> getAlphaFromConfig(ParsingConfiguration config) {
        Stack<Map.Entry<String, List<String>>> alpha = new Stack<>();

        for (Map.Entry<String, Integer> entry : Util.reverse(config.alphaToListOfProductionsString())) {
            String symbol = entry.getKey();
            Integer number = entry.getValue();

            List<String> productionRuleRHS = grammar.getLHSOfIthProductionRuleOfSymbol(symbol, number);
            alpha.push(new AbstractMap.SimpleEntry<>(symbol, productionRuleRHS));
        }

        return alpha;
    }

    private List<Node> alphaToTree(Stack<Map.Entry<String, List<String>>> alpha) {
        String lhs = alpha.peek().getKey();

        List<Node> tree = new ArrayList<>();
        Node root = new Node(lhs);
        tree.add(root);
        Queue<Node> queue = new LinkedList<>();
        queue.add(root);

        List<String> productionRuleRHS;
        while (!queue.isEmpty()) {
            Node currentNode = queue.poll();
            productionRuleRHS = alpha.pop().getValue();

            for (int i = 0; i < productionRuleRHS.size(); i++) {
                String rhsElement = productionRuleRHS.get(i);

                Node child = new Node(rhsElement, currentNode);
                if (i != 0)
                    child.left = tree.get(tree.size() - 1);
                tree.add(child);
                if (grammar.isNonTerminalSymbol(rhsElement))
                    queue.add(child);
            }
        }

        return tree;
    }

    private List<List<String>> treeToTable(List<Node> tree) {
        List<List<String>> table = new ArrayList<>();

        table.add(Arrays.asList("index", "info", "parent", "right sibling"));
        for (int i = 0; i < tree.size(); i++) {
            Node node = tree.get(i);

            List<String> row = new ArrayList<>();
            row.add(String.valueOf(i + 1));
            row.add(node.symbol);
            row.add(String.valueOf(tree.indexOf(node.parent) + 1));
            row.add(String.valueOf(tree.indexOf(node.left) + 1));
            table.add(row);
        }

        return table;
    }
}