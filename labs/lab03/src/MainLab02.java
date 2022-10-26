import java.util.Map;

public class MainLab02 {
    static SymbolTable symbolTable = new HashTableSymbolTable();
    
    public static void main(String[] args) {

        add(1);
        add(2);
        add(3);
        add("abc");
        add(2);
        add("z");
        add("abc");
    }
    
    public static void add(Object o) {
        Map.Entry<Integer, Integer> position = symbolTable.add(o);

        System.out.printf("%s at %s%n", o, position);
    }
}