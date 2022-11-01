import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Scanner {
    /*
     * Input: Programs p1/p2/p3/p1err and token.txt (see Lab 1a)
     * Output: PIF.out, ST.out, message “lexically correct” or “lexical error + location”
     * */
    public static void scan(String programFilename, String pifFilename, String symbolTableFilename) {
        Scanner scanner = new Scanner(programFilename, pifFilename, symbolTableFilename);
        scanner.tryReadFileAndScan();
    }

    private final String programFilename;
    private final String PIFFilename;
    private final String symbolTableFilename;

    private final LanguageSpecification languageSpecification = new LanguageSpecification();
    private final SymbolTable symbolTable = new HashTableSymbolTable();
    private final PIF pif = new PIF();
    private final List<String> errors = new ArrayList<>();

    private Scanner(String programFilename, String pifFilename, String symbolTableFilename) {
        this.programFilename = programFilename;
        PIFFilename = pifFilename;
        this.symbolTableFilename = symbolTableFilename;
    }

    private void tryReadFileAndScan() {
        try (java.util.Scanner scanner = new java.util.Scanner(new File(programFilename))) {
            scan(scanner);
        } catch (IOException e) {
            throw new RuntimeException("Could not read file", e);
        }
    }

    private void scan(java.util.Scanner scanner) throws IOException {
        Map<Integer, Collection<String>> lineTokensMap = getTokens(scanner);
        generatePIF(lineTokensMap);
        writeAnalysisResults();
    }

    private Map<Integer, Collection<String>> getTokens(java.util.Scanner scanner) {
        Map<Integer, Collection<String>> lineTokensMap = new HashMap<>();
        int lineNumber = 1;

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            lineTokensMap.put(lineNumber++, languageSpecification.tokenize(line));
        }
        return lineTokensMap;
    }

    private void generatePIF(Map<Integer, Collection<String>> lineTokensMap) {
        for (Map.Entry<Integer, Collection<String>> entry : lineTokensMap.entrySet()) {
            int line = entry.getKey();
            Collection<String> tokens = entry.getValue();

            for (String token : tokens) {
                if (languageSpecification.isSeparatorOperatorOrKeyword(token)) {
                    pif.add(token, new AbstractMap.SimpleEntry<>(-1, -1));
                } else if (languageSpecification.isConstant(token)) {
                    pif.add("constant", symbolTable.add(token));
                } else if (languageSpecification.isIdentifier(token)) {
                    pif.add("identifier", symbolTable.add(token));
                } else {
                    errors.add(String.format("Lexical error at line %d: %s", line, token));
                }
            }
        }
    }

    private void writeAnalysisResults() {
        writeFile(PIFFilename, pif.toString());
        writeFile(symbolTableFilename, symbolTable.toString());

        if (errors.isEmpty()) {
            System.out.println("Lexically correct");
        } else {
            System.out.println("Lexical errors:");
            errors.forEach(System.out::println);
        }
    }

    private static void writeFile(String path, String content) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            writer.write(content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
