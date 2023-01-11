import java.io.FileWriter;
import java.io.IOException;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class LanguageSpecificationWithFA extends LanguageSpecification {
    @Override
    public boolean isIdentifier(String token) {
        // "^[a-zA-Z_][a-zA-Z0-9_]*$"
        return new FA(new CreateIdentifierFile().createFile("identifier.in")).isAccepted(token);
    }

    @Override
    protected boolean isNumericConstant(String token) {
        // "^0|[+|-]?[1-9][0-9]*$"
        return new FA(new CreateNumericConstantFile().createFile("numeric-constant.in")).isAccepted(token);
    }

    @Override
    protected boolean isStringConstant(String token) {
        return super.isStringConstant(token);
    }

    @Override
    protected boolean isCharConstant(String token) {
        return super.isCharConstant(token);
    }
}


abstract class CreateFAFile {
    protected abstract void writeToFile(FileWriter writer) throws IOException;

    public final String createFile(String filepath) {
        try (FileWriter fileWriter = new FileWriter(filepath)) {
            writeToFile(fileWriter);
        } catch (IOException e) {
            throw new RuntimeException(String.format("Failed to create file %s", filepath), e);
        }

        return filepath;
    }

}

class CreateIdentifierFile extends CreateFAFile {
    @Override
    protected void writeToFile(FileWriter writer) throws IOException {
        writer.write("q0 q1\n"); // states

        for (int i = 'a'; i <= 'z'; i++) writer.write((char) i + " "); // alphabet - lowercase letters
        for (int i = 'A'; i <= 'Z'; i++) writer.write((char) i + " "); // alphabet - uppercase letters
        for (int i = '0'; i <= '9'; i++) writer.write((char) i + " "); // alphabet - digits
        writer.write("_\n"); // alphabet - underscore

        writer.write("q0\n"); // initial state
        writer.write("q1\n"); // final states

        // transitions
        Object[] elementsFromAlphabetForTransitionsFrom_q0_to_q1 = Stream.of(
                IntStream.range('a', 'z' + 1).mapToObj(i -> (char) i),
                IntStream.range('A', 'Z' + 1).mapToObj(i -> (char) i),
                Stream.of('_')
        ).flatMap(i -> i).toArray();

        for (Object o : elementsFromAlphabetForTransitionsFrom_q0_to_q1) {
            writer.write("q0 " + o + " q1\n");
        }

        Object[] elementsFromAlphabetForTransitionsFrom_q1_to_q1 = Stream.of(
                IntStream.range('a', 'z' + 1).mapToObj(i -> (char) i),
                IntStream.range('A', 'Z' + 1).mapToObj(i -> (char) i),
                IntStream.range('0', '9' + 1).mapToObj(i -> (char) i),
                Stream.of('_')
        ).flatMap(i -> i).toArray();

        for (Object o : elementsFromAlphabetForTransitionsFrom_q1_to_q1) {
            writer.write("q1 " + o + " q1\n");
        }
    }
}

class CreateNumericConstantFile extends CreateFAFile {
    @Override
    protected void writeToFile(FileWriter writer) throws IOException {
        writer.write("q0 q1 q2 q3\n"); // states

        for (int i = '0'; i <= '9'; i++) writer.write((char) i + " "); // alphabet - digits
        writer.write("+ -\n"); // alphabet - plus and minus

        writer.write("q0\n"); // initial state
        writer.write("q1 q3\n"); // final states

        // transitions
        writer.write("q0 0 q1\n");
        writer.write("q0 + q2\n");
        writer.write("q0 - q2\n");

        for(String o: new String[]{"q0", "q2", "q3"}) {
            for (int i = '1'; i <= '9'; i++) {
                writer.write(o + " " + (char) i + " q3\n");
            }
        }
    }
}