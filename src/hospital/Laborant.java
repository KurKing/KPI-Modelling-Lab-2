package hospital;

import elements.Process;
import elements.distribution.Distribution;

import java.util.Random;

public class Laborant extends Process {

    private final double goHomeProbability;
    private final Random random = new Random();

    public Laborant(double delay, String name, Distribution distribution, double goHomeProbability) {
        super(delay, name, distribution);
        this.goHomeProbability = goHomeProbability;
    }

    @Override
    protected void inActNextElement() {

        if (random.nextDouble() < goHomeProbability) { return; }
        super.inActNextElement();
    }

    @Override
    public double getDelayMean() {
        return 8.04;
    }
}
