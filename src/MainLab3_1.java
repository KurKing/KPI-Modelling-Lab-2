import elements.Create;
import elements.Element;
import elements.Process;
import elements.distribution.Distribution;

import java.util.ArrayList;
import java.util.List;

public class MainLab3_1 {

    public static void main(String[] args) {

        Create c = new Create(0.5, "CREATOR", Distribution.EXPONENTIAL);

        List<Element> processList = processList(2);

        c.setNextElement(processList);

        ArrayList<Element> combinedList = new ArrayList<>(processList);
        combinedList.add(0, c);

        Model.simulate(combinedList, 50.0);
    }

    private static List<Element> processList(int amount) {

        ArrayList<Element> processArrayList = new ArrayList<>();

        for (int i = 0; i < amount; i++) {

            Process p = new Process(0.3, "PROCESSOR " + (i + 1), Distribution.EXPONENTIAL);
            p.setMaxqueue(3);

            processArrayList.add(p);
        }

        return processArrayList;
    }
}
