package elements;

import elements.distribution.Distribution;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Create extends Element {

    private int numberOfRebalances = 0;
    private final Boolean shouldRebalanceLines;

    public Create(double delay, String name, Distribution distribution, Boolean shouldRebalanceLines) {

        super(delay, name, distribution);

        tnext = 0.0;
        this.shouldRebalanceLines = shouldRebalanceLines;
    }

    public Create(double delay, String name, Distribution distribution) {

        super(delay, name, distribution);

        tnext = 0.0;
        this.shouldRebalanceLines = false;
    }

    @Override
    public void outAct() {

        super.outAct();

        rebalanceLines();

        tnext = tcurr + getDelay();
        inActNextElement();
    }

    protected void inActNextElement() {

        getNextElement().inAct();
    }

    private void rebalanceLines() {

        if (!shouldRebalanceLines) { return; }

        List<Process> processList = nextElements.stream()
                .map(element -> (element instanceof Process) ? (Process) element : null)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        List<Integer> queues = processList.stream()
                                .mapToInt(Process::getQueue)
                                .boxed()
                                .collect(Collectors.toList());

        int currentlyInQueue = queues.stream().mapToInt(Integer::intValue).sum();

        int processAmount = nextElements.size();

        if (currentlyInQueue <= processAmount) { return; }

        int average = (int)Math.round(((double)currentlyInQueue) / ((double)processAmount)) - 1;
        int diff = currentlyInQueue - average * processAmount;

        for (Process process: processList) {

            process.setQueue(average);
        }

        int c = 0;
        while (diff > 0) {

            if (c >= processAmount) { c = 0; }

            Process p = processList.get(c);
            p.setQueue(p.getQueue()+1);

            c++;
            diff--;
        }

        int size = processList.size();
        int rebalances = 0;
        for (int i = 0; i < size; i++) {
            int processQueue = processList.get(i).getQueue();
            int previousQueue = queues.get(i);

            rebalances += Math.abs(processQueue - previousQueue);
        }
        numberOfRebalances += rebalances / 2;

//        int updatedInQueue = processList
//                .stream()
//                .mapToInt(Process::getQueue)
//                .sum();
//
//        if (updatedInQueue != currentlyInQueue) {
//
//            System.out.println("Rebalance lines: \n\tCurrently in queue: "+currentlyInQueue+
//                    "\n\tUpdated in queue: "+updatedInQueue+
//                    "\n\tAverage: "+average+
//                    "\n\tDiff: "+diff);
//        }
    }

    public int getNumberOfRebalances() {
        return numberOfRebalances;
    }
}
