import java.util.*;

public class PIF {
    private final List<Map.Entry<String, Map.Entry<Integer, Integer>>> pif = new ArrayList<>();

    public void add(String token, Map.Entry<Integer, Integer> position) {
        pif.add(new AbstractMap.SimpleEntry<>(token, position));
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        for (Map.Entry<String, Map.Entry<Integer, Integer>> entry : pif) {
            Map.Entry<Integer, Integer> position = entry.getValue();
            result.append(entry.getKey()).append(" -> (").append(position.getKey()).append(", ").append(position.getValue()).append(")\n");
        }

        return result.toString();
    }
}
