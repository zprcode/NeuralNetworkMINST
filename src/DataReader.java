import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class DataReader {

    List<Data> allData = new ArrayList<>();

    public void read(String file) throws FileNotFoundException {
        Scanner sc = new Scanner(new File(file));
        while (sc.hasNextLine()) {
            Data data = new Data();
            data.inputs = new int[Config.inputs];
            String[] datatemp = sc.nextLine().split(",");
            for (int i = 0; i < datatemp.length; i++) {
                if (i == 0) data.label = Integer.parseInt(datatemp[0]);
                else {
                    data.inputs[i - 1] = Integer.parseInt(datatemp[i]);
                    //System.out.println(data.inputs[i-1]);
                }
            }
            allData.add(data);
        }
    }

    public void readnr(String nr) throws FileNotFoundException {
        allData.clear();
            Data data = new Data();
            data.inputs = new int[Config.inputs];
            String[] datatemp = nr.split(",");
            for (int i = 0; i < datatemp.length; i++) {
                if (i == 0) data.label = Integer.parseInt(datatemp[0]);
                else {
                    data.inputs[i - 1] = Integer.parseInt(datatemp[i]);
                    //System.out.println(data.inputs[i-1]);
                }
            }
            allData.add(data);
    }

    public List<Data> getAllData() {
        return allData;
    }

    public void shuffleData(){
        Collections.shuffle(allData);
    }
}
