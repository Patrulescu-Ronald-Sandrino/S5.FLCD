import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class Util {
    public static <T> List<T> reverse(List<T> list) {
        List<T> reversed = new ArrayList<>(list);
        Collections.reverse(reversed);
        return reversed;
    }

    public static <T> Stack<T> listToStack(List<T> list) {
        Stack<T> stack = new Stack<>();

        stack.addAll(list);

        return stack;
    }

    public static <T> Stack<T> listToStackReverse(List<T> list) {
        return listToStack(reverse(list));
    }
}
