import java.util.AbstractMap;
import java.util.Map;

public class HashTableSymbolTable implements SymbolTable {
    Object[] table;
    int size = 5000;

    public HashTableSymbolTable() {
        table = new Object[size];
    }

    @Override
    public Map.Entry<Integer, Integer> add(Object object) {
        int position = hash(object);

        // if there is something hashed to the position
        if (table[position] != null) {
            Object[] oldList = (Object[]) table[position];

            // if the object already exists, then return its position
            for (int i = 0; i < oldList.length; i++) {
                if (oldList[i].equals(object)) {
                    return new AbstractMap.SimpleEntry<>(position, i);
                }
            }

            // if the object doesn't exist, then add it
            Object[] newList = new Object[oldList.length + 1];

            System.arraycopy(oldList, 0, newList, 0, oldList.length);
            table[position] = newList;
            newList[oldList.length] = object;

            return new AbstractMap.SimpleEntry<>(position, oldList.length);
        }
        // if there's nothing hashed to the position
        else {
            table[position] = new Object[]{object};
            return new AbstractMap.SimpleEntry<>(position, 0);
        }
    }

    @Override
    public Object get(Map.Entry<Integer, Integer> position) {
        return ((Object[])table[position.getKey()])[position.getValue()];
    }

    private int hash(Object object) {
        return Math.abs(object.hashCode() % size);
    }


    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < table.length; i++) {
            if (table[i] != null) {
                Object[] list = (Object[]) table[i];
                for (int j = 0; j < list.length; j++) {
                    result.append(i).append(" -> ").append(j).append(" : ").append(list[j]).append("\n");
                }
            }
        }

        return result.toString();
    }
}
