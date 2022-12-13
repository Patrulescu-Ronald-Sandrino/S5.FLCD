import java.util.List;
import java.util.Queue;
import java.util.Stack;

public class ParsingConfiguration {
    public ParsingState s = ParsingState.NORMAL;
    public int i = 1;
    public Stack<String> alpha = new Stack<>();
    public Stack<String> beta = new Stack<>();
    public ParsingConfiguration next;

    public ParsingConfiguration(List<String> input){
        beta.addAll(input);
    }

    public ParsingConfiguration(ParsingState s, int i, Stack<String> alpha, Stack<String> beta) {
        this.s = s;
        this.i = i;
        this.alpha = alpha;
        this.beta = beta;
        this.next = null;
    }

    public ParsingConfiguration(ParsingConfiguration configuration){
        this.s = configuration.s;
        this.i = configuration.i;
        this.alpha = configuration.alpha;
        this.beta = configuration.beta;
        this.next = configuration.next;
    }

    @Override
    public String toString() {
        StringBuilder alphaString = new StringBuilder();
        for (String symbol: this.alpha) {
            alphaString.append(symbol).append(" ");
        }

        StringBuilder betaString = new StringBuilder();
        for(String symbol: this.beta) {
            betaString.append(symbol).append(" ");
        }

        return "(" + s.toString() + ", " + i + ", " + alphaString + ", " + betaString + ")";
    }
}