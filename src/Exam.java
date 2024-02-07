import elements.Create;
import elements.Element;
import elements.Process;
import elements.distribution.Distribution;

import java.util.ArrayList;
import java.util.List;

public class Exam {

    public static void main(String[] args) {

        List<Element> windows = new ArrayList<>();
        for (int i = 0; i < 2; i++) {

            Process bankWindow = new Process(1.5, "ВІКОНЦЕ #"+(i+1), Distribution.EXPONENTIAL);
            bankWindow.setMaxqueue(3);

            windows.add(bankWindow);
        }

        Create create = new Create(2.5, "CREATOR", Distribution.EXPONENTIAL, true);
        create.setNextElement(windows);

        ArrayList<Element> combinedList = new ArrayList<>(List.of(create));
        combinedList.addAll(windows);

        Model.simulate(combinedList, 1000);
    }
}
