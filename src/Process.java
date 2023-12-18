public class Process extends Element {

    private int queue, maxqueue, failure;
    private double meanQueue;

    public Process(double delay, String name, Distribution distribution) {

        super(delay, name, distribution);

        queue = 0;
        maxqueue = Integer.MAX_VALUE;
        meanQueue = 0.0;
    }
    @Override
    public void inAct() {

        if (getState() == 0) {

            setState(1);
            setTnext(getTcurr() + getDelay());
        } else {
            if (getQueue() < getMaxqueue()) {
                setQueue(getQueue() + 1);
            } else {
                failure++;
            }
        }
    }
    @Override
    public void outAct() {

        super.outAct();

        setTnext(Double.MAX_VALUE);
        setState(0);

        if (getQueue() > 0) {

            setQueue(getQueue() - 1);
            setState(1);
            setTnext(getTcurr() + getDelay());
        }
    }
    public int getFailure() {
        return failure;
    }
    public int getQueue() {
        return queue;
    }
    public void setQueue(int queue) {
        this.queue = queue;
    }
    public int getMaxqueue() {
        return maxqueue;
    }
    public void setMaxqueue(int maxqueue) {
        this.maxqueue = maxqueue;
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
}