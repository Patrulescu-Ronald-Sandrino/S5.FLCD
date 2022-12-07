import java.util.List;
import java.util.Queue;
import java.util.Stack;

public class ParsingConfiguration {
    public ParsingState s;
    public int i;
    public Stack<String> alpha;
    public Queue<String> beta;

    public ParsingConfiguration(ParsingState s, int i, Stack<String> alpha, Queue<String> beta) {
        this.s = s;
        this.i = i;
        this.alpha = alpha;
        this.beta = beta;
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
