import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LanguageSpecification {
    private static final List<String> separators = new ArrayList<>();
    private static final List<String> operators = new ArrayList<>();
    private static final List<String> keywords = new ArrayList<>();
    private static final String STRING_LITERAL_REGEX = "\"[a-zA-Z0-9_=+\\-*/<!>?:{}(),; ]*\"";
    private static final String CHAR_LITERAL_REGEX = "'[a-zA-Z0-9_=+\\-*/<!>?:{}(),; ]'";

    public LanguageSpecification() {
        readTokens();
    }

    private boolean isKeyword(String token) {
        return keywords.contains(token);
    }

    private boolean isOperator(String token) {
        return operators.contains(token);
    }

    public boolean isSeparator(String token) {
        return separators.contains(token);
    }

    public boolean isSeparatorOperatorOrKeyword(String token) {
        return isSeparator(token) || isOperator(token) || isKeyword(token);
    }

    public boolean isIdentifier(String token) {
        return token.matches("^[a-zA-Z_][a-zA-Z0-9_]*$"); // any combination of letters, digits and underscores, starting with a letter or underscore
    }

    protected boolean isNumericConstant(String token) {
        return token.matches("^0|[+|-]?[1-9][0-9]*$");
    }

    protected boolean isStringConstant(String token) {
        return token.matches("^" + STRING_LITERAL_REGEX + "$");
    }

    protected boolean isCharConstant(String token) {
        return token.matches("^" + CHAR_LITERAL_REGEX + "$");
    }

    public boolean isConstant(String token) {
        return isNumericConstant(token) || isStringConstant(token) || isCharConstant(token);
    }

    private boolean isSeparatorButNotSpace(String token) {
        return separators.contains(token) && !token.equals(" ");
    }

    public Collection<String> tokenize(String line) { // TODO check
        List<String> tokens = new ArrayList<>();

        for (int i = 0; i < line.length() && !isStartOfComment(line, i); ++i) {
            String c = String.valueOf(line.charAt(i));
            String token = null;

            if (isSeparatorButNotSpace(c)) {
                token = c;
            } else if (c.equals("\"")) {
                token = extractStringLiteral(line, i);
            } else if (c.equals("'")) {
                token = extractCharLiteral(line, i);
            } else if (c.equals("+") || c.equals("-")) {
                token = extractSignToken(line, i, tokens);
            } else if (isPartOfOperator(c)) {
                token = extractOperator(line, i);
            } else if (!c.equals(" ")) {
                token = extractToken(line, i);
            }

            if (token != null) {
                tokens.add(token);
                i += token.length() - 1;
            }
        }

        return tokens;
    }

    private boolean isStartOfComment(String line, int position) {
        return line.length() > position + 1 && line.charAt(position) == '/' && line.charAt(position + 1) == '/';
    }

    private boolean isPartOfOperator(String c) { // TODO check
        return c.equals("!") || operators.stream().anyMatch(op -> op.startsWith(c));
    }

    private String extractCharLiteral(String line, int position) {
        StringBuilder token = new StringBuilder(String.valueOf(line.charAt(position)));
        int i = position + 1;

        while (i < line.length() && !String.valueOf(line.charAt(i)).equals("'")) {
            token.append(line.charAt(i));
            ++i;
        }

        if (i < line.length()) {
            token.append(line.charAt(i));
        }

        return token.toString();
    }

    private String extractStringLiteral(String line, int position) {
        StringBuilder token = new StringBuilder(String.valueOf(line.charAt(position)));
        int i = position + 1;

        while (i < line.length() && !String.valueOf(line.charAt(i)).equals("\"")) {
            token.append(line.charAt(i));
            ++i;
        }

        if (i < line.length()) {
            token.append(line.charAt(i));
        }

        return token.toString();
    }

    private String extractSignToken(String line, int position, List<String> tokens) {
        String c = String.valueOf(line.charAt(position));
        String previousToken = tokens.get(tokens.size() - 1);

        // sign is preceded by an identifier or a constant => sign is an operator
        if (isIdentifier(previousToken) || isConstant(previousToken)) {
            return c;
        }

        // sign is preceded by an operator or a separator => sign is part of a literal
        StringBuilder token = new StringBuilder(c);

        for (int i = position + 1; i < line.length() && Character.isDigit(c.charAt(0)); i++) {
            token.append(line.charAt(i));
        }

        return token.toString();
    }

    private String extractOperator(String line, int position) {
        String substring = line.substring(position, position + 2);

        if (isOperator(substring)) {
            return substring;
        }

        return String.valueOf(line.charAt(position));
    }

    private String extractToken(String line, int position) {
        StringBuilder token = new StringBuilder();

        for (int i = position; i < line.length(); ++i) {
            char c = line.charAt(i);

            if (isSeparator(String.valueOf(c)) || isOperator(String.valueOf(c))) {
                break;
            }

            token.append(c);
        }

        return token.toString();
    }

    private static void readTokens() {
        try (java.util.Scanner scanner = new java.util.Scanner(new BufferedReader(new FileReader("input/token.txt")))) {
            String lastCategory = null;

            while(scanner.hasNextLine()) {
                String line = scanner.nextLine();

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
