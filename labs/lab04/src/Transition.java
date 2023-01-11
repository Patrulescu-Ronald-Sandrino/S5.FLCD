public class Transition {
    private final String sourceState;
    private final String elementFromAlphabet;
    private final String destinationState;

    public Transition(String sourceState, String elementFromAlphabet, String destinationState) {
        this.sourceState = sourceState;
        this.elementFromAlphabet = elementFromAlphabet;
        this.destinationState = destinationState;
    }

    public String getSourceState() {
        return sourceState;
    }

    public String getElementFromAlphabet() {
        return elementFromAlphabet;
    }

    public String getDestinationState() {
        return destinationState;
    }

    @Override
    public String toString() {
        return "δ(" + sourceState + ", " + elementFromAlphabet + ") = " + destinationState;
    }
}
