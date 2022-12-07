import java.util.*;

public class ParserStrategy {
    private Map<Integer, Integer> indexMapping = new LinkedHashMap<>();
    private Map<Set<String>, Set<List<String>>> productionRules;

    public ParserStrategy(Map<Set<String>, Set<List<String>>> productionRules) {
        this.productionRules = productionRules;
    }

    public void expand(ParsingConfiguration configuration){
        configuration.next = new ParsingConfiguration(configuration);
        String symbol = configuration.next.beta.pop();
        configuration.next.alpha.add(symbol);

        if(!indexMapping.containsKey(configuration.next.alpha.size() - 1)){
            indexMapping.put(configuration.next.alpha.size() - 1, 1);
        }

        Set<List<String>> productionRuleResult = productionRules.get(new LinkedHashSet<>(Collections.singleton(symbol)));
        List<String> productionRule = productionRuleResult.stream().toList().get(indexMapping.get(configuration.next.alpha.size() - 1));

        Collections.reverse(productionRule);
        configuration.next.beta.addAll(productionRule);

    }

    public void advance(ParsingConfiguration configuration){
        configuration.next = new ParsingConfiguration(configuration);
        configuration.next.i += 1;
        configuration.next.alpha.add(configuration.next.beta.pop());
    }
    
    public void momentaryInsuccess(ParsingConfiguration configuration){
        configuration.next = new ParsingConfiguration(configuration);
        configuration.next.s = ParsingState.BACK;
    }

    public void back(ParsingConfiguration configuration){
        configuration.next = new ParsingConfiguration(configuration);
        configuration.next.i -= 1;
        configuration.next.beta.add(configuration.next.alpha.pop());
    }

    public void anotherTry(ParsingConfiguration configuration){

    }

    public void success(ParsingConfiguration configuration){
        configuration.next = new ParsingConfiguration(configuration);
        configuration.next.s = ParsingState.FINAL;
    }
}
