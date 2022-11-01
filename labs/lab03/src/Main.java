

public class Main {
    public static void main(String[] args) {
        for (String program: new String[]{"p1", "p2", "p3", "p1err"}) {
            System.out.println("--------------------------------------------------------------------------");

            System.out.println("Program " + program);
            Scanner.scan("input/" + program + ".txt",
                    "output/" + program + "_pif.txt",
                    "output/" + program + "_st.txt");

            System.out.println("--------------------------------------------------------------------------");
            System.out.println();
        }
    }
}
