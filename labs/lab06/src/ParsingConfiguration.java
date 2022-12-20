import java.util.*;
import java.util.stream.Collectors;

public class ParsingConfiguration {
    public ParsingState s = ParsingState.NORMAL;
    public int i = 1;
    public Stack<String> alpha = new Stack<>();
    public Map<Integer, Integer> indexMapping = new LinkedHashMap<>();

    public Stack<String> beta = new Stack<>();
    public ParsingConfiguration next;

    public ParsingConfiguration(ParsingState s, int i, List<String> alpha, Map<Integer, Integer> indexMapping, List<String> beta) {
        this.s = s;
        this.i = i;
        this.alpha = Util.listToStack(alpha);
        this.indexMapping = indexMapping;
        this.beta = Util.listToStackReverse(beta);
    }

    public ParsingConfiguration(List<String> input) {
        beta.addAll(input);
    }

    public ParsingConfiguration(ParsingConfiguration configuration) {
        this.s = configuration.s;
        this.i = configuration.i;
        this.alpha = (Stack<String>) configuration.alpha.clone();
        this.indexMapping = new LinkedHashMap<>(configuration.indexMapping);
        this.beta = (Stack<String>) configuration.beta.clone();
        this.next = configuration.next;
    }

    @Override
    public String toString() {
        StringBuilder alphaString = new StringBuilder();
        int index = 0;
        for (String symbol : this.alpha) {
            alphaString.append(symbol);
            if (indexMapping.containsKey(index)) {
                alphaString.append("[").append(indexMapping.get(index)).append("]");
            }
            if (index < this.alpha.size() - 1) {
                alphaString.append(" ");
            }
            index++;
        }

        StringBuilder betaString = new StringBuilder();
        for (String symbol : this.beta) {
            betaString.append(symbol).append(" ");
        }

        return "(" + s.toString() + ", " + i + ", " + alphaString + ", " + betaString + ")";
    }

    public List<Map.Entry<String, Integer>> alphaToListOfProductionsString() {
        List<Map.Entry<String, Integer>> alphaAsList = new ArrayList<>();

        int index = 0;
        for (String symbol : this.alpha) {
            if (indexMapping.containsKey(index)) {
                Integer localRuleNumber = indexMapping.get(index);
                alphaAsList.add(new AbstractMap.SimpleEntry<>(symbol, localRuleNumber));
            }
            index++;
        }

        return alphaAsList;
    }
}
