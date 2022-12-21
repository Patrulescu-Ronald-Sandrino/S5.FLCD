public class Node {
    public String symbol;
    public Node parent;
    public Node left;

    public Node(String symbol) {
        this.symbol = symbol;
    }

    public Node(String symbol, Node parent) {
        this.symbol = symbol;
        this.parent = parent;
    }
}
