import elements.Create;
import elements.Element;
import elements.Process;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Model {

    private static double meanAmount = 0;

    public static double simulate(List<Element> list, double time) {

        List<Process> processList = list.stream()
                .map(element -> (element instanceof Process) ? (Process) element : null)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

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

            meanAmount += processList.stream().mapToInt(Process::getClientsAmount).sum() * (tnext - tcurr);

//            System.out.println("\n"+nextElement.getName()+"\'s time = "+tnext+"\n");

            tcurr = tnext;

            for (Element e : list) {  e.setTcurr(tcurr); }

            nextElement.outAct();

            for (Element e : list) {

                if (e.getTnext() == tcurr) {

                    e.outAct();
                }

//                e.printInfo();
            }
        }

        printResult(list, tcurr);
        return tcurr;
    }

    private static void printResult(List<Element> list, double tcurr) {

        System.out.println("\n\t\t\tRESULTS:");

        for (Element e : list) {

            if (e.getName().contains("HALL")) { continue; }

            e.printResult();
            if (e instanceof Process) {

                Process p = (Process) e;
                System.out.println("\tMean Length of Queue: " + String.format("%.4f", p.getMeanQueue() / tcurr) +
                        "\n\tMean time in process: " + String.format("%.2f", (p.getMeanQueue() / tcurr + 1) * p.getDelayMean()) +
                        "\n\tMean Locked: " + String.format("%.2f", Math.min(p.getMeanLocked() / tcurr, 1.0) * 100) + "%" +
                        "\n\tFailure: " + p.getFailure() +
                        "\n\tFailure Probability: " + String.format("%.2f", Math.min(p.getFailure() / ((double) p.getQuantity() + p.getFailure()), 1.0) * 100) + "%" +
                        "\n\tMean leave time: " + String.format("%.2f", p.getMeanLeaveTime()));
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
        System.out.println("Mean amount: " + meanAmount / tcurr);

        int rebalances = list.stream()
                .map(element -> (element instanceof Create) ? (Create) element : null)
                .filter(Objects::nonNull)
                .mapToInt(Create::getNumberOfRebalances)
                .sum();
        System.out.println("Rebalances: " + rebalances);
    }
}