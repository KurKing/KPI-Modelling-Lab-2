package elements;

import elements.distribution.Distribution;
import elements.state.MachineState;

import java.util.ArrayList;

public class Process extends Element {

    private int queue, maxqueue, failure;

    private boolean shouldTryToUseNextProcess = false;

    private double meanQueue, meanLocked;
    private ArrayList<Double> leaveTime = new ArrayList();
    private double lastLeaveTime = 0;

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

        leaveTime.add(tcurr - lastLeaveTime);
        lastLeaveTime = tcurr;

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

    public int getClientsAmount() {

        if (state == MachineState.LOCKED) {
            return queue + 1;
        }

        return queue;
    }

    public double getMeanLeaveTime() {

        double sum = leaveTime.stream().mapToDouble(Double::doubleValue).sum();
        return sum / leaveTime.size();
    }

    public int getQueue() {
        return queue;
    }

    public void setQueue(int queue) {
        this.queue = queue;
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