package elements.probabilistic;

import elements.Element;

class ProbabilisticElement {

    private final Element element;
    private final double probability;

    public ProbabilisticElement(Element element, double probability) {

        this.element = element;
        this.probability = probability;
    }

    public Element getElement() {
        return element;
    }

    public double getProbability() {
        return probability;
    }
}
