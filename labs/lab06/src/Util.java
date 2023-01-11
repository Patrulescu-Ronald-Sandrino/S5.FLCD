import java.io.FileWriter;
import java.io.IOException;
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


    public static String leftJustifiedFormatting(int count, int width) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < count; i++) {
            sb.append("%").append(width).append("s");
        }

        return sb.toString();
    }

    public static void writeToFile(String filePath, String content) {
        try {
            FileWriter fileWriter = new FileWriter(filePath);
            fileWriter.write(content);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String extractFileNameWithoutExtensionFromPath(String path) {
        String[] split = path.split("/");
        String fileName = split[split.length - 1];
        String[] split2 = fileName.split("\\.");
        return split2[0];
    }
}
