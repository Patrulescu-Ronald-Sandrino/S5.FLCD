import java.util.Map;

public interface SymbolTable {
    Map.Entry<Integer, Integer> add(Object object);

    Object get(Map.Entry<Integer, Integer> position);
}
