import elements.Create;
import elements.distribution.Distribution;
import elements.Process;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        Process p = new Process(1.0, "PROCESSOR 1", Distribution.EXPONENTIAL);
        p.setMaxqueue(5);

        Process p1 = new Process(1.0, "PROCESSOR 2", Distribution.EXPONENTIAL);
        p1.setMaxqueue(5);

        Process p2 = new Process(1.0, "PROCESSOR 3", Distribution.EXPONENTIAL);
        p2.setMaxqueue(5);

        Create c = new Create(3.0, "CREATOR", Distribution.EXPONENTIAL);

        c.setNextElement(p);
        p.setNextElement(p1);
        p1.setNextElement(p2);

        Model.simulate(List.of(c, p, p1, p2), 20.0);
    }
}