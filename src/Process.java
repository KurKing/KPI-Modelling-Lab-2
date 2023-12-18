public class Process extends Element {

    private int queue, maxqueue, failure;
    private double meanQueue;

    public Process(double delay, String name, Distribution distribution) {

        super(delay, name, distribution);

        queue = 0;
        maxqueue = Integer.MAX_VALUE;
        meanQueue = 0.0;
    }

    public void setMaxqueue(int maxqueue) {
        this.maxqueue = maxqueue;
    }

    @Override
    public void inAct() {

        switch (state) {
            case LOCKED -> {

                if (queue < maxqueue) {

                    queue += 1;
                    return;
                }
                failure++;
            }
            case UNLOCKED -> {

                state = MachineState.LOCKED;
                tnext = tcurr + getDelay();
            }
        }
    }
    @Override
    public void outAct() {

        super.outAct();

        tnext = Double.MAX_VALUE;
        state = MachineState.UNLOCKED;

        if (queue > 0) {

            queue -= 1;
            tnext = tcurr + getDelay();
            state = MachineState.LOCKED;
        }
    }

    @Override
    public void printInfo() {
        super.printInfo();
        System.out.println("failure = " + this.getFailure());
    }
    @Override
    public void doStatistics(double delta) {
        meanQueue += queue * delta;
    }

    public double getMeanQueue() {
        return meanQueue;
    }

    public int getFailure() {
        return failure;
    }
}