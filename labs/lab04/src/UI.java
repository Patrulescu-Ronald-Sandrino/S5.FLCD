import java.util.Scanner;

public class UI {
    private FA fa = null;

    public void run() {
        while (true) {
            System.out.println("0. Exit");
            System.out.println("1. Read FA from file");
            System.out.println("2. Display the set of states");
            System.out.println("3. Display the alphabet");
            System.out.println("4. Display all transitions");
            System.out.println("5. Display the initial state");
            System.out.println("6. Display the set of final states");
            System.out.println("7. Check if FA is deterministic");
            System.out.println("8. Check if a sequence is accepted by FA");
            System.out.print("\t Your option: ");

            int option;

            try {
                option = Integer.parseInt(new Scanner(System.in).nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid option");
                continue;
            }

            if (fa == null && option > 1 && option <= 8) {
                System.out.println("You must read the FA first!");
                continue;
            }

            switch (option) {
                case 0: {
                    System.out.println("Exiting...");
                    return;
                }
                case 1: {
                    System.out.print("Please enter the filepath of the FA: ");
                    String filepath = new Scanner(System.in).nextLine();
                    try {
                        fa = new FA(filepath);
                    } catch (RuntimeException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                }
                case 2: {
                    System.out.println(fa.getStates());
                    break;
                }
                case 3: {
                    System.out.println(fa.getAlphabet());
                    break;
                }
                case 4: {
                    System.out.println(fa.getTransitions());
                    break;
                }
                case 5: {
                    System.out.println(fa.getInitialState());
                    break;
                }
                case 6: {
                    System.out.println(fa.getFinalStates());
                    break;
                }
                case 7: {
                    System.out.println(fa.isDeterministic());
                    break;
                }
                case 8: {
                    System.out.print("Please enter the sequence: ");
                    String sequence = new Scanner(System.in).nextLine();
                    System.out.println(fa.isAccepted(sequence));
                    break;
                }
                default: {
                    System.out.println("Invalid option");
                }
            }
        }
    }
}
