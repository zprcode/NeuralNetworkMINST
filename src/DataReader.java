import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class DataReader {

    private final List<Data> allData = new ArrayList<>();
    private boolean LLM = Config.LLM;
    int D = Config.Dimensions;
    Embeddings emb = new Embeddings();

    //Number
    public void read(String file) throws FileNotFoundException {
        try (Scanner sc = new Scanner(new File(file))) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (!line.isEmpty()) {
                    processLine(line);
                }
            }
        }
    }

    public void readnr(String nr) {
        allData.clear();
        if (nr != null && !nr.isEmpty()) {
            processLine(nr);
        }
    }

    private void processLine(String line) {
        String[] temp = line.split("[, .:]+");
        int length = temp.length;
        int size = length;
        if(LLM){
            size *= 8;
        }
        String[] datatemp = new String[size];
        if(LLM){
            for (int i = 0; i < length; i++) {
                float[] embArray = emb.getArray(temp[i]);
                for (int j = 0; j < D; j++) {
                    datatemp[i * D + j] = String.valueOf(embArray[j]);
                }
            }
        }
        else datatemp = temp;
        Data data = new Data();
        data.inputs = new float[size];

        if(!LLM) {
            for (int i = 0; i < datatemp.length; i++) {
                float value = Float.parseFloat(datatemp[i].trim());
                if (i == 0) {
                    data.label = value;
                } else if (i - 1 < Config.inputs) {
                    data.inputs[i - 1] = value;
                }
            }
        }
        else{
            for (int i = 0; i < datatemp.length; i++) {
                float value = Float.parseFloat(datatemp[i].trim());
                data.inputs[i] = value;
            }
        }
        allData.add(data);
    }

    //Word


    public List<Data> getAllData() {
        return allData;
    }

    public void shuffleData() {
        Collections.shuffle(allData);
    }
}