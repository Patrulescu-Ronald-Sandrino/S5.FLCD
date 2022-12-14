import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ParserAlgorithm {
    private static ParsingConfiguration initialConfiguration = new ParsingConfiguration(Arrays.asList("S"));
    private final Grammar grammar;
    private final ParserStrategy strategy;
    private final List<String> sequence;

    public ParserAlgorithm(Grammar g, List<String> w){
        this.grammar = g;
        this.strategy = new ParserStrategy(g);
        this.sequence = w;
    }

    private ParsingTable createParsingTable(ParsingConfiguration finalConfiguration){
        ParsingTree tree = new ParsingTree();
        int treeIndex = 0;

        List<String> alphaAsList = Util.reverse(new ArrayList<>(finalConfiguration.alpha));
        
        while(treeIndex < alphaAsList.size()){
            ParsingTreeNode symbolNode = new ParsingTreeNode(alphaAsList.get(treeIndex));
            if(grammar.isNonTerminalSymbol(symbol)){
                if(tree.root == null){
                    tree.root = currentNode;
                }
                grammar.getLHSOfIthProductionRuleOfSymbol(symbol, finalConfiguration.indexMapping.get(treeIndex));
            }
        }
    }


    public void execute(){
        ParsingConfiguration config = initialConfiguration;

        while(config.s != ParsingState.FINAL && config.s != ParsingState.ERROR){
            if(config.s == ParsingState.NORMAL){
                if(config.i == sequence.size() + 1 && config.beta.isEmpty()){
                    // SUCCESS
                    this.strategy.success(config);
                    config = config.next;
                }
                else{
                    if(grammar.isNonTerminalSymbol(config.beta.peek())){
                        // EXPAND
                        this.strategy.expand(config);
                        config = config.next;
                    }
                    else if(config.beta.peek().equals(this.sequence.get(config.i - 1))){
                        // ADVANCE
                        this.strategy.advance(config);
                        config = config.next;
                    }
                    else{
                        // MOMENTARY INSUCCESS
                        this.strategy.momentaryInsuccess(config);
                        config = config.next;
                    }
                }
            }
            else if (config.s == ParsingState.BACK){
                if (grammar.isTerminalSymbol(config.alpha.peek())){
                    // BACK
                    this.strategy.back(config);
                    config = config.next;
                }
                else {
                    // ANOTHER TRY
                    this.strategy.anotherTry(config);
                    config = config.next;
                }
            }
        }

        if(config.s == ParsingState.ERROR){
            // PRINT ERROR MESSAGE
        }
        else{
            // PRINT SUCCESS MESSAGE


        }

    }

}
