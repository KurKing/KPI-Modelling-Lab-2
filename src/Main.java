import java.util.List;

public class Main {
    public static void main(String[] args) {

        Create c = new Create(2.0, "CREATOR", Distribution.EXPONENTIAL);
        Process p = new Process(10.0, "PROCESSOR", Distribution.EXPONENTIAL);

        c.setNextElement(p);
        p.setMaxqueue(5);

        new Model(List.of(c, p)).simulate(1000.0);
    }
}