import elements.Element;
import elements.Process;

import java.util.List;
import java.util.Objects;

public class Model {

    public static void simulate(List<Element> list, double time) {

        double tcurr = 0.0;
        Element nextElement = null;

        while (tcurr < time) {

            double tnext = Double.MAX_VALUE;

            for (Element e : list) {

                if (e.getTnext() < tnext) {

                    tnext = e.getTnext();
                    nextElement = e;
                }
            }

            for (Element e : list) {

                e.doStatistics(tnext - tcurr);
            }

            System.out.println("\n"+nextElement.getName()+"\'s time = "+tnext+"\n");

            tcurr = tnext;

            for (Element e : list) {  e.setTcurr(tcurr); }

            nextElement.outAct();

            for (Element e : list) {

                if (e.getTnext() == tcurr) {

                    e.outAct();
                }

                e.printInfo();
            }
        }

        printResult(list, tcurr);
    }

    private static void printResult(List<Element> list,double tcurr) {

        System.out.println("\n\t\t\tRESULTS:");

        for (Element e : list) {

            e.printResult();
            if (e instanceof Process) {

                Process p = (Process) e;
                System.out.println("\tMean Length of Queue: " + String.format("%.4f", p.getMeanQueue() / tcurr) +
                        "\n\tMean Locked: " + String.format("%.2f", Math.min(p.getMeanLocked() / tcurr, 1.0) * 100) + "%" +
                        "\n\tFailure: " + p.getFailure() +
                        "\n\tFailure Probability: " + String.format("%.2f", Math.min(p.getFailure() / ((double) p.getQuantity() + p.getFailure()), 1.0) * 100) + "%");
            }
        }

        int generalQuantity = list.stream()
                    .map(element -> (element instanceof Process) ? (Process) element : null)
                    .filter(Objects::nonNull)
                    .mapToInt(Process::getQuantity)
                    .sum();

        int generalFailure = list.stream()
                .map(element -> (element instanceof Process) ? (Process) element : null)
                .filter(Objects::nonNull)
                .mapToInt(Process::getFailure)
                .sum();

        int currentlyInQueue = list.stream()
                .map(element -> (element instanceof Process) ? (Process) element : null)
                .filter(Objects::nonNull)
                .mapToInt(Process::getQueue)
                .sum();

        System.out.println("\nGENERAL DATA: \n\tQuantity: " + generalQuantity +
                "\n\tFailure: " + generalFailure +
                "\n\tFailure Probability: " + String.format("%.2f", Math.min(generalFailure / ((double) generalQuantity + generalFailure), 1.0) * 100) + "%" +
                "\n\tIn queue: " + currentlyInQueue);

        System.out.println("\nGENERAL SUMM: " + (generalQuantity + generalFailure + currentlyInQueue));
    }
}