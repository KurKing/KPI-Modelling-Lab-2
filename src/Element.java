public class Element {

    private static int nextId = 0;

    private final String name;
    private final int id;
    private final Distribution distribution;

    protected Element nextElement;

    protected double tnext;
    protected double delayMean, delayDev;
    protected double tcurr;
    protected int state;

    private int quantity;


    public Element(double delay, String name, Distribution distribution){

        this.name = name;
        this.distribution = distribution;

        id = nextId;
        nextId++;

        tnext = 0.0;
        delayMean = delay;
        tcurr = tnext;
        state = 0;

        nextElement=null;
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
    public double getTcurr() {
        return tcurr;
    }
    public void setTcurr(double tcurr) {
        this.tcurr = tcurr;
    }
    public int getState() {
        return state;
    }
    public void setState(int state) {
        this.state = state;
    }

    public void setNextElement(Element nextElement) {
        this.nextElement = nextElement;
    }
    public void inAct() {
    }
    public void outAct(){
        quantity++;
    }
    public double getTnext() {
        return tnext;
    }
    public void setTnext(double tnext) {
        this.tnext = tnext;
    }
    public double getDelayMean() {
        return delayMean;
    }
    public void setDelayMean(double delayMean) {
        this.delayMean = delayMean;
    }
    public int getId() {
        return id;
    }

    public void printResult(){
        System.out.println(getName()+ " quantity = "+ quantity);
    }
    public void printInfo(){
        System.out.println(getName()+ " state= " +state+
                " quantity = "+ quantity+
                " tnext= "+tnext);
    }
    public String getName() {
        return name;
    }

    public void doStatistics(double delta){
    }
}