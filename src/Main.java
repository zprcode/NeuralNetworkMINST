import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

    int epochs = Config.epochs;
    String file = "Data/train.csv";
    String testFile = "Data/test.csv";

    DataReader dataReader = new DataReader();
    Network network = new Network();

    public static void main(String[] args) throws FileNotFoundException {
        new Main().start();
    }

    public void start() throws FileNotFoundException {
        Scanner sc = new Scanner(System.in);

        while(true){
            System.out.println("1 - Train | 2 - Test Single | 3 - Test Dataset | 0 - Exit");
            if(!sc.hasNextInt()) { sc.next(); continue; }
            int choice = sc.nextInt();
            sc.nextLine();

            if(choice == 1) train();
            else if(choice == 2) {
                System.out.println("Paste data");
                test();
            }
            else if(choice == 3) {
                System.out.println("How many samples?");
                int limit = sc.nextInt();
                testDataset(limit);
            }
            else if(choice == 0) break;
        }
    }

    public void train() throws FileNotFoundException {
        dataReader.read(file);
        var data = dataReader.getAllData();

        if (network.layers.isEmpty()) {
            network.assignLayers(data);
        }
        for (int j = 0; j < epochs; j++) {
            dataReader.shuffleData();

            for (int i = 0; i < data.size(); i++) {
                network.assignL1(data, i);
                network.forwardAll();
                network.backprop();
            }

            System.out.println("Epoch " + (j + 1) + " finished.");
            network.start = 0;
        }
    }

    public void testDataset(int limit) throws FileNotFoundException {
        dataReader.read(testFile);
        var data = dataReader.getAllData();

        if (network.layers.isEmpty()) {
            network.assignLayers(data);
        }

        int actualLimit = Math.min(limit, data.size());
        int correctGuesses = 0;

        System.out.println("testing " + actualLimit + " samples");

        for (int i = 0; i < actualLimit; i++) {
            network.assignL1(data, i);
            network.forwardAll();

            Layer outLayer = network.layers.getLast();
            int prediction = 0;
            double maxVal = -1.0;

            for (int j = 0; j < outLayer.neurons.size(); j++) {
                if (outLayer.neurons.get(j).value > maxVal) {
                    maxVal = outLayer.neurons.get(j).value;
                    prediction = j;
                }
            }

            if (prediction == network.curLabel) {
                correctGuesses++;
            }
        }

        printFinalAccuracy(correctGuesses, actualLimit);
    }

    private void test() throws FileNotFoundException {
        Scanner sc = new Scanner(System.in);
        String nr = sc.nextLine();
        if (nr.isEmpty()) return;

        dataReader.readnr(nr);
        if (network.layers.isEmpty()) {
            network.assignLayers(dataReader.getAllData());
        }

        network.assignL1(dataReader.getAllData(), 0);
        network.forwardAll();
        printNetwork(network);
    }

    //AI
    private void printFinalAccuracy(int correct, int total) {
        double accuracy = (double) correct / total * 100;
        System.out.println("\n" + "=".repeat(35));
        System.out.println("      DATASET TEST RESULTS      ");
        System.out.println("=".repeat(35));
        System.out.printf("Total Samples  : %d\n", total);
        System.out.printf("Correct Guesses: %d\n", correct);
        System.out.printf("Wrong Guesses  : %d\n", (total - correct));
        System.out.println("-".repeat(35));
        System.out.printf("ACCURACY       : %.2f%%\n", accuracy);
        System.out.println("=".repeat(35) + "\n");
    }

    private void printNetwork(Network network) {
        Layer outLayer = network.layers.getLast();
        int prediction = -1;
        double maxConfidence = -1.0;
        int actualLabel = network.curLabel;

        System.out.println("\n" + "=".repeat(30));
        System.out.println("      NETWORK PREDICTION      ");
        System.out.println("=".repeat(30));
        System.out.println("Digit | Confidence | Visualization");
        System.out.println("------|------------|--------------");

        for (int j = 0; j < outLayer.neurons.size(); j++) {
            double value = outLayer.neurons.get(j).value;

            if (value > maxConfidence) {
                maxConfidence = value;
                prediction = j;
            }

            String bar = "█".repeat((int) (value * 10));
            System.out.printf("  %d   |   %5.2f%%   | %s\n", j, value * 100, bar);
        }

        System.out.println("-".repeat(30));
        System.out.println("Result   : " + (prediction == actualLabel ? "✅ CORRECT" : "❌ WRONG"));
        System.out.println("Guessed  : **" + prediction + "**");
        System.out.println("Expected : **" + actualLabel + "**");
        System.out.println("=".repeat(30) + "\n");
    }
}