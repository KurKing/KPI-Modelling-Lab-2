package elements;

import elements.distribution.Distribution;
import elements.distribution.FunRand;
import elements.probabilistic.ProbabilisticElementPicker;
import elements.state.MachineState;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class Element {

    private final String name;
    private final Distribution distribution;

    private Element nextElement;
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

        nextElement = null;
    }

    protected double getDelay() {

        switch (distribution) {
            case EXPONENTIAL:
                return FunRand.Exp(delayMean);
            case NORMAL:
                return FunRand.Norm(delayMean, delayDev);
            case UNIFORM:
                return FunRand.Unif(delayMean, delayDev);
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

    public void setNextElement(Element nextElement) {
        this.nextElement = nextElement;
    }
    public void addElement(double probability, Element element) {
        nextElementsPicker.addElement(probability, element);
    }
    protected final Element getNextElement() {

        if (nextElementsPicker.isEmpty()) {

            return nextElement;
        }

        return nextElementsPicker.getElement();
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

    public void doStatistics(double delta){
    }
}