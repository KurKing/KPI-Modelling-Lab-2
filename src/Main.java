import elements.Create;
import elements.distribution.Distribution;
import elements.Process;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        Process p = new Process(10.0, "PROCESSOR", Distribution.EXPONENTIAL);
        p.setMaxqueue(5);

        Create c = new Create(2.0, "CREATOR", Distribution.EXPONENTIAL);
        c.setNextElement(p);

        Model.simulate(List.of(c, p), 1000.0);
    }
}