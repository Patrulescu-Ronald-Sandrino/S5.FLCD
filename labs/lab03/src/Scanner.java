import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Scanner {
    /*
     * Input: Programs p1/p2/p3/p1err and token.txt (see Lab 1a)
     * Output: PIF.out, ST.out, message “lexically correct” or “lexical error + location”
     * */
    public static void scan(String programFilename, String pifFilename, String symbolTableFilename) {
        Scanner scanner = new Scanner(programFilename, pifFilename, symbolTableFilename);
        scanner.tryReadFileAndScan();
    }


    private static final List<String> operators = new ArrayList<>();
    private static final List<String> separators = new ArrayList<>();
    private static final List<String> keywords = new ArrayList<>();

    static {
        readTokens();
    }

    private final String programFilename;
    private final String PIFFilename;
    private final String symbolTableFilename;

    private final SymbolTable symbolTable = new HashTableSymbolTable();
    private final PIF pif = new PIF();
    private final List<String> errors = new ArrayList<>();

    private Scanner(String programFilename, String pifFilename, String symbolTableFilename) {
        this.programFilename = programFilename;
        PIFFilename = pifFilename;
        this.symbolTableFilename = symbolTableFilename;
    }

    private void tryReadFileAndScan() {
        try (BufferedReader br = Files.newBufferedReader(Paths.get(programFilename))) {
            scan(br);
        } catch (IOException e) {
            throw new RuntimeException("Could not read file", e);
        }
    }

    private void scan(BufferedReader br) throws IOException {
        String line;
        int lineNumber = 1;

        while ((line = br.readLine()) != null) {
            String[] tokens = line.split(" ");
        }
    }

    private static void readTokens() {
        try (Stream<String> linesStream = Files.lines(Paths.get("input/token.txt"))) {
            List<String> lines = linesStream.collect(Collectors.toList());
            String lastCategory = null;

            for (String line : lines) {
                if (line.startsWith("#")) {
                    lastCategory = line.substring(2);
                    continue;
                }

                if (lastCategory == null) {
                    continue;
                }

                switch (lastCategory) {
                    case "operators":
                        operators.add(line);
                        break;
                    case "separators":
                        separators.add(line);
                        break;
                    case "keywords":
                        keywords.add(line);
                        break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not read tokens", e);
        }
    }
}
