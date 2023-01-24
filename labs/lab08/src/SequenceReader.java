import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SequenceReader {
    private List<String> sequence;

    public SequenceReader(String filePathAsString) {
        this.importSequenceFromFile(Paths.get(filePathAsString));
    }

    private void importSequenceFromFile(Path filePath){
        sequence = new ArrayList<>();

        try {
            File grammarFile = new File(filePath.toUri());
            Scanner fileReader = new Scanner(grammarFile);
            while(fileReader.hasNextLine()){
                String fileLine = fileReader.nextLine();
                sequence.add(fileLine.replaceAll("\\s+", "").split("->")[0]);
            }
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public List<String> getSequence() {
        return this.sequence;
    }
}
