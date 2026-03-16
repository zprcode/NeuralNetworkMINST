import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

    private static final int EPOCHS = Config.epochs;
    private static final String TRAIN_FILE = "Data/test.csv";
    private static final String TEST_FILE = "Data/train.csv";

    public static void main(String[] args) throws FileNotFoundException {

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("1 - Train | 2 - Test Single | 3 - Test Dataset | 0 - Exit");

            if (!sc.hasNextInt()) {
                sc.next();
                continue;
            }

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 ->
                        ConsoleUI.train(TRAIN_FILE, EPOCHS);

                case 2 -> {
                    System.out.println("Paste data:");
                    String input = sc.nextLine();
                    ConsoleUI.testSingle(input);
                }

                case 3 -> {
                    System.out.println("How many samples?");
                    int limit = sc.nextInt();
                    ConsoleUI.testDataset(TEST_FILE, limit);
                }

                case 0 -> {
                    return;
                }
            }
        }
    }
}