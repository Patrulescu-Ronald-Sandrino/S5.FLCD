import java.util.Arrays;
import java.util.List;

public class ProductionRule {
    private final List<String> leftHandSide;
    private final List<String> rightHandSide;

    public ProductionRule(String[] leftHandSide, String[] rightHandSide) {
        this.leftHandSide = Arrays.asList(leftHandSide);
        this.rightHandSide = Arrays.asList(rightHandSide);

    }

    public List<String> getLeftHandSide() {
        return leftHandSide;
    }

    public List<String> getRightHandSide() {
        return rightHandSide;
    }
}
