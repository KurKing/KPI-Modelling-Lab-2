package elements;

import elements.distribution.Distribution;

public class Create extends Element {

    public Create(double delay, String name, Distribution distribution) {

        super(delay, name, distribution);

        tnext = 0.0;
    }

    @Override
    public void outAct() {

        super.outAct();

        tnext = tcurr + getDelay();
        getNextElement().inAct();
    }

    @Override
    protected Element chooseBestNextElement() {

        if (nextElements == null || nextElements.isEmpty()) { return null; }

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
        return super.chooseBestNextElement();
    }
}
