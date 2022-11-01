import java.util.HashMap;
import java.util.Map;

public class PIF {
    private final Map<String, Map.Entry<Integer, Integer>> pif = new HashMap<>();

    public void add(String token, Map.Entry<Integer, Integer> position) {
        pif.put(token, position);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        for (Map.Entry<String, Map.Entry<Integer, Integer>> entry : pif.entrySet()) {
            Map.Entry<Integer, Integer> position = entry.getValue();
            result.append(entry.getKey()).append(" -> (").append(position.getKey()).append(", ").append(position.getValue()).append(")\n");
        }

        return result.toString();
    }
}
