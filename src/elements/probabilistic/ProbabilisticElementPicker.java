package elements.probabilistic;

import elements.Element;

import java.util.*;

public class ProbabilisticElementPicker {

    private final Random random = new Random();
    private List<ProbabilisticElement> elements = new ArrayList<>();

    public boolean isEmpty() {

        return elements.isEmpty();
    }

    public void addElement(double probability, Element element) {

        elements.add(new ProbabilisticElement(element, probability));

        Collections.sort(elements, Comparator.comparingDouble(ProbabilisticElement::getProbability));
    }

    public Element getElement() {

        if (elements.isEmpty()) { return null; }

        Double randomValue = random.nextDouble();

        double cumulativeProbability = 0.0;

        for (ProbabilisticElement probabilisticElement : elements) {
            cumulativeProbability += probabilisticElement.getProbability();

            if (randomValue <= cumulativeProbability) {
                return probabilisticElement.getElement();
            }
        }

        return null;
    }
}
