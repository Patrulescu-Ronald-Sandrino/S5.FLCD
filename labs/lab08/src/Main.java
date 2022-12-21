import java.util.Arrays;

public class Main {
    public static String[] g1 = new String[]{"../lab05/g1.txt", "../lab08/in/seq.txt"};
    public static String[] g2 = new String[]{"../lab05/g2.txt", "../lab08/in/PIF.out"};

    public static void main(String[] args) {
        String[] input = g2;
        String grammarFilePathAsString = input[0];
        String outputFilePathAsString = Util.extractFileNameWithoutExtensionFromPath(grammarFilePathAsString) + ".out.txt";

        Grammar grammar = new Grammar(grammarFilePathAsString);
        SequenceReader reader = new SequenceReader(input[1]);
        ParserAlgorithm parserAlgorithm = new ParserAlgorithm(grammar, reader.getSequence());
        parserAlgorithm.execute(outputFilePathAsString);
    }
}