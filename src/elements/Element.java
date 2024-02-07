package elements;

import elements.distribution.Distribution;
import elements.distribution.FunRand;
import elements.probabilistic.ProbabilisticElementPicker;
import elements.state.MachineState;

import java.util.List;

public class Element {

    private final String name;
    private final Distribution distribution;

    protected List<Element> nextElements;
    private final ProbabilisticElementPicker nextElementsPicker = new ProbabilisticElementPicker();

    protected MachineState state;

    protected double tnext;
    protected double delayMean, delayDev;
    protected double tcurr;

    protected int quantity;

    public Element(double delay, String name, Distribution distribution){

        this.name = name;
        this.distribution = distribution;
        state = MachineState.UNLOCKED;

        tnext = 0.0;
        delayMean = delay;
        tcurr = tnext;

        nextElements = null;
    }

    protected double getDelay() {

        switch (distribution) {
            case EXPONENTIAL:
                return FunRand.Exp(delayMean);
            case NORMAL:
                return FunRand.Norm(delayMean, delayDev);
            case UNIFORM:
                return FunRand.Unif(delayMean, delayDev);
            case ERLANG:
                return FunRand.Erlang(delayMean, delayDev);
            default:
                return delayMean;
        }
    }

    public int getQuantity() {
        return quantity;
    }

    public void setTcurr(double tcurr) {
        this.tcurr = tcurr;
    }

    public void setNextElement(List<Element> nextElements) {
        this.nextElements = nextElements;
    }
    public void addElement(double probability, Element element) {
        nextElementsPicker.addElement(probability, element);
    }
    protected final Element getNextElement() {

        if (nextElementsPicker.isEmpty()) {

            return chooseBestNextElement();
        }

        return nextElementsPicker.getElement();
    }
    protected Element chooseBestNextElement() {

        return chooseBestNextElement(nextElements);
    }

protected Element chooseBestNextElement(List<Element> nextElements) {

    if (nextElements == null || nextElements.isEmpty()) { return null; }

    if (nextElements.size() == 1) { return nextElements.get(0); }

    Process currentBest = null;

    for (Element element : nextElements) {

        Process process = (Process) element;
        if (process == null) { continue; }

        if (process.getQueue() == 0) { return process; }

        if (currentBest == null) {

            currentBest = process;
            continue;
        }

        if (currentBest.getQueue() > process.getQueue()) {
            currentBest = process;
        }
    }

    if (currentBest != null) {
        return currentBest;
    }

    if (nextElements == null || nextElements.isEmpty()) { return null; }
    return nextElements.get(0);
}
    public void inAct() {
    }
    public void outAct() {
        quantity++;
    }
    public double getTnext() {
        return tnext;
    }

    public void printResult() {
        System.out.println(getName()+ "\n\tQuantity = "+ quantity);
    }
    public void printInfo(){
        System.out.println(getName() + "\n\tState: " + state +
                "\n\tQuantity: " + quantity +
                "\n\tTnext: " + tnext);
    }
    public String getName() {
        return name;
    }

    public double getDelayMean() {

        if (delayDev > 0) {
            return (delayMean + delayDev) / 2;
        }

        return delayMean;
    }

    public void setDelayDev(double delayDev) {
        this.delayDev = delayDev;
    }

    public void doStatistics(double delta){
    }
}