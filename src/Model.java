import elements.Element;
import elements.Process;

import java.util.List;

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
                System.out.println("\tMean Length of Queue: " + p.getMeanQueue() / tcurr +
                        "\n\tMean Locked: " + p.getMeanLocked() / tcurr +
                        "\n\tFailure: " + p.getFailure() +
                        "\n\tFailure Probability: " + String.format("%.2f", Math.min(p.getFailure() / (double) p.getQuantity(), 1.0) * 100) + "%");
            }
        }
    }
}