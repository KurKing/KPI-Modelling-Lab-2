package hospital;

import elements.Process;
import elements.distribution.Distribution;

public class Registation extends Process {

    public Registation(double delay, String name, Distribution distribution) {
        super(delay, name, distribution);
    }

    @Override
    public double getDelayMean() {
        return 13.56;
    }
}
