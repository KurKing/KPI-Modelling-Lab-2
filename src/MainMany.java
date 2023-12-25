import elements.Create;
import elements.distribution.Distribution;
import elements.Process;

import java.util.List;

public class MainMany {

    public static void main(String[] args) {

        Process p = new Process(2.0, "PROCESSOR 1", Distribution.EXPONENTIAL);
        p.setMaxqueue(5);
//        p.allowToUseNextProcess();

        Process p1 = new Process(2.0, "PROCESSOR 2", Distribution.EXPONENTIAL);
        p1.setMaxqueue(5);
//        p1.allowToUseNextProcess();

        Process p2 = new Process(2.0, "PROCESSOR 3", Distribution.EXPONENTIAL);
        p2.setMaxqueue(5);

        Create c = new Create(1.0, "CREATOR", Distribution.EXPONENTIAL);

        c.setNextElement(p);
        p.setNextElement(p1);

        p1.addElement(0.7, p2);
        p1.addElement(0.3, p1);

        Model.simulate(List.of(c, p, p1, p2), 50.0);
    }
}