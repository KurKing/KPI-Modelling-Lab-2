package elements;

import elements.distribution.Distribution;
import elements.state.MachineState;

public class Process extends Element {

    private int queue, maxqueue, failure;

    private boolean shouldTryToUseNextProcess = false;

    private double meanQueue, meanLocked;
    private double lockTime;

    public Process(double delay, String name, Distribution distribution) {

        super(delay, name, distribution);

        queue = 0;
        maxqueue = Integer.MAX_VALUE;
        meanQueue = 0.0;
        meanLocked = 0.0;

        tnext = Double.MAX_VALUE;
    }

    public void setMaxqueue(int maxqueue) {
        this.maxqueue = maxqueue;
    }

    public void allowToUseNextProcess() {

        shouldTryToUseNextProcess = true;
    }

    @Override
    public void inAct() {

        switch (state) {
            case LOCKED -> {

                if (queue < maxqueue) {

                    queue += 1;
                    return;
                }

                Element nextElement = getNextElement();
                if (shouldTryToUseNextProcess && nextElement != null) {

                    nextElement.inAct();
                    return;
                }

                failure++;
            }
            case UNLOCKED -> {

                state = MachineState.LOCKED;
                lockTime = tcurr;
                tnext = tcurr + getDelay();
            }
        }
    }
    @Override
    public void outAct() {

        if (state == MachineState.UNLOCKED) { return; }

        super.outAct();

        tnext = Double.MAX_VALUE;
        state = MachineState.UNLOCKED;
        meanLocked += tcurr - lockTime;

        Element nextElement = getNextElement();
        if (nextElement != null) {
            nextElement.inAct();
        }

        if (queue > 0) {

            queue -= 1;
            tnext = tcurr + getDelay();
            state = MachineState.LOCKED;
            lockTime = tcurr;
        }
    }

    @Override
    public void printInfo() {

        super.printInfo();

        System.out.println("\tFailure: " + failure);
    }
    @Override
    public void doStatistics(double delta) {
        meanQueue += queue * delta;
    }

    public double getMeanQueue() {
        return meanQueue;
    }

    public double getMeanLocked() {
        return meanLocked;
    }

    public int getFailure() {
        return failure;
    }
}