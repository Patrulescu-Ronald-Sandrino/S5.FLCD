import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class FA {
    //region fields
    private final Set<String> states;
    private final Set<String> alphabet;
    private final String initialState;
    private final Set<String> finalStates;
    private final Set<Transition> transitions = new LinkedHashSet<>();
    //endregion

    //region getters
    public Set<String> getStates() {
        return states;
    }

    public Set<String> getAlphabet() {
        return alphabet;
    }

    public String getInitialState() {
        return initialState;
    }

    public Set<String> getFinalStates() {
        return finalStates;
    }

    public Set<Transition> getTransitions() {
        return transitions;
    }
    //endregion

    //region constructor
    public FA(String inputFilepath) {
        List<String> lines = readLines(inputFilepath);

        if (lines.size() < 5) {
            throw new IllegalArgumentException("Input file must have at least 5 lines");
        }

        states = new LinkedHashSet<>(Arrays.asList(lines.get(0).split(" ")));
        alphabet = new LinkedHashSet<>(Arrays.asList(lines.get(1).split(" ")));
        initialState = lines.get(2);
        finalStates = new LinkedHashSet<>(Arrays.asList(lines.get(3).split(" ")));

        // read the transitions
        for (int i = 4; i < lines.size(); i++) {
            String[] parts = lines.get(i).split(" ");
            if (parts.length != 3) {
                throw new IllegalArgumentException(String.format("Invalid transition at line %d: %s", i, lines.get(i)));
            }
            transitions.add(new Transition(parts[0], parts[1], parts[2]));
        }
    }

    private static List<String> readLines(String inputFilepath) {
        try {
            return Files.readAllLines(Paths.get(inputFilepath));
        } catch (IOException e) {
            throw new RuntimeException(String.format("Could not open file %s.", inputFilepath), e);
        }
    }
    //endregion


    public boolean isDeterministic() {
        for (String state : states) {
            for (String element : alphabet) {
                int count = 0;
                for (Transition transition : transitions) {
                    if (transition.getSourceState().equals(state) && transition.getElementFromAlphabet().equals(element)) {
                        count++;
                    }
                }
                if (count > 1) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isAccepted(String sequence) {
        String currentState = initialState;

        for (int i = 0; i < sequence.length(); i++) {
            String element = String.valueOf(sequence.charAt(i));
            if (!alphabet.contains(element)) {
                return false;
            }
            boolean found = false;
            for (Transition transition : transitions) {
                if (transition.getSourceState().equals(currentState) && transition.getElementFromAlphabet().equals(element)) {
                    currentState = transition.getDestinationState();
                    found = true;
                    break;
                }
            }
            if (!found) {
                return false;
            }
        }

        return finalStates.contains(currentState);
    }
}
