public class Create extends Element {

    public Create(double delay, String name, Distribution distribution) {

        super(delay, name, distribution);

        tnext = 0.0;
    }
    @Override
    public void outAct() {

        super.outAct();

        tnext = tcurr + getDelay();
        nextElement.inAct();
    }
}
