import java.util.List;

public class ParsingTreeNode {
    public String symbol;
    public ParsingTreeNode parent;
    public List<ParsingTreeNode> children;

    public ParsingTreeNode(String symbol) {
        this.symbol = symbol;
    }
}
