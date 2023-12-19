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

                e.doStatistics(tnext - tcurr);
            }

            printEventTime(nextElement.getName(), tnext);

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

    private static void printEventTime(String eventName, double tnext) {

        System.out.println("\nIt's time for event in "+eventName+", time = "+tnext);
    }

    private static void printResult(List<Element> list,double tcurr) {

        System.out.println("\n-------------RESULTS-------------");

        for (Element e : list) {

            e.printResult();
            if (e instanceof Process) {

                Process p = (Process) e;
                System.out.println("mean length of queue = "+p.getMeanQueue() / tcurr
                        + "\nmean locked = "+p.getMeanLocked() / tcurr
                        + "\nfailure probability = " +
                        p.getFailure() / (double) p.getQuantity());
            }
        }
    }
}