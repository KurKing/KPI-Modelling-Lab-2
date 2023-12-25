import elements.Create;
import elements.Process;
import elements.distribution.Distribution;

import java.util.List;

public class MainSingle {

    public static void main(String[] args) {

        Process p = new Process(1.0, "PROCESSOR 1", Distribution.EXPONENTIAL);
        p.setMaxqueue(5);

        Create c = new Create(2.0, "CREATOR", Distribution.EXPONENTIAL);

        c.setNextElement(p);

        Model.simulate(List.of(c, p), 50.0);
    }
}
