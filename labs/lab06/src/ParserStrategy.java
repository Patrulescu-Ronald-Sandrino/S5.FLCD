import java.util.*;

public class ParserStrategy {
    private Map<Set<String>, Set<List<String>>> productionRules;

    public ParserStrategy(Map<Set<String>, Set<List<String>>> productionRules) {
        this.productionRules = productionRules;
    }

    public void expand(ParsingConfiguration configuration){
        configuration.next = new ParsingConfiguration(configuration);
        ParsingConfiguration nextConfig = configuration.next;

        String symbol = nextConfig.beta.pop();
        nextConfig.alpha.add(symbol);
        int indexMappingKey = nextConfig.alpha.size() - 1;

        if(!nextConfig.indexMapping.containsKey(indexMappingKey)){ // TODO: is this condition required?
            nextConfig.indexMapping.put(indexMappingKey, 1);
        }

        // get the production rule for the current symbol in the head of the input stack
        Integer productionNumber = nextConfig.indexMapping.get(indexMappingKey);
        List<String> firstProductionRuleOfSymbol = getLHSOfIthProductionRuleOfSymbol(symbol, productionNumber);

        // push the element of the LHS of the production rule to the input stack
         // reverse the elements of the LHS bc. last element should be pushed first
        nextConfig.beta.addAll(Util.reverse(firstProductionRuleOfSymbol));
    }

    private List<String> getLHSOfIthProductionRuleOfSymbol(String symbol, Integer i){
        Set<List<String>> productionRulesOfSymbol = productionRules.get(new LinkedHashSet<>(Collections.singleton(symbol)));
        return new ArrayList<>(new ArrayList<>(productionRulesOfSymbol).get(i - 1));
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
        configuration.next = new ParsingConfiguration(configuration);
        ParsingConfiguration nextConfig = configuration.next;

        String A = nextConfig.alpha.peek();
        Integer j = nextConfig.indexMapping.get(nextConfig.alpha.size() - 1);

        List<String> lhsOfCurrentProductionRule = getLHSOfIthProductionRuleOfSymbol(A, j);

        try {
            List<String> lhsOfNextProductionRule = getLHSOfIthProductionRuleOfSymbol(A, j + 1); // this should fail if ∃! A -> γ_{j+1}. MAYBE replace w/ if-else

            nextConfig.s = ParsingState.NORMAL;

            // pop from beta the lhs of the current production rule
            for (int i = 0; i < lhsOfCurrentProductionRule.size(); i++) {
                nextConfig.beta.pop();
            }

            // push to beta the lhs of the next production rule
            nextConfig.beta.addAll(Util.reverse(lhsOfNextProductionRule));

            // update the index mapping (actually updates the working stack)
            nextConfig.indexMapping.put(nextConfig.alpha.size() - 1, j + 1);
        }
        catch (Exception e) {
            var startingSymbol = new ArrayList<>(new ArrayList<>(productionRules.keySet()).get(0)).get(0); // WARNING: Assumes the starting symbol is the 1st inserted
            if (nextConfig.i == 1 && Objects.equals(A, startingSymbol)) {
                nextConfig.s = ParsingState.ERROR;

                // remove production rule from the working stack
                nextConfig.indexMapping.remove(nextConfig.alpha.size() - 1);
                nextConfig.alpha.pop();

                // pop from beta the lhs of the current production rule
                for (int i = 0; i < lhsOfCurrentProductionRule.size(); i++) {
                    nextConfig.beta.pop();
                }
            }
            else {
                // remove production rule from the working stack
                nextConfig.indexMapping.remove(nextConfig.alpha.size() - 1);
                nextConfig.alpha.pop();

                // pop from beta the lhs of the current production rule
                for (int i = 0; i < lhsOfCurrentProductionRule.size(); i++) {
                    nextConfig.beta.pop();
                }

                // push the symbol to the input stack
                nextConfig.beta.push(A);
            }
        }
    }

    public void success(ParsingConfiguration configuration){
        configuration.next = new ParsingConfiguration(configuration);
        configuration.next.s = ParsingState.FINAL;
    }
}
