import java.util.Arrays;

public class Main {
        public static void main(String[] args) {
        String grammarFilePathAsString = "../lab05/g1.txt";
        String[] word = {"a", "c", "b", "c"};

        Grammar grammar = new Grammar(grammarFilePathAsString);
        ParserAlgorithm parserAlgorithm = new ParserAlgorithm(grammar, Arrays.asList(word));
        parserAlgorithm.execute();
    }
}
