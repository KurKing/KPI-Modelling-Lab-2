import elements.Create;
import elements.Process;
import elements.distribution.Distribution;

import java.util.List;

public class MainSingleLab2 {

    public static void main(String[] args) {

        Process p = new Process(2.0, "PROCESSOR 1", Distribution.EXPONENTIAL);
        p.setMaxqueue(5);

        Create c = new Create(1.0, "CREATOR", Distribution.EXPONENTIAL, true);

        c.setNextElement(List.of(p));

        Model.simulate(List.of(c, p), 50.0);
    }
}
