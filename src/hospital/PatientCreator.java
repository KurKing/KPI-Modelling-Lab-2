package hospital;

import elements.Create;
import elements.Element;
import elements.distribution.Distribution;

import java.util.Random;

public class PatientCreator extends Create {

    private final Random random = new Random();
    private int inActAmount = 0;

    private int type1Created, type2Created, type3Created;

    public PatientCreator(double delay, String name, Distribution distribution) {
        super(delay, name, distribution);
    }

    @Override
    public void inAct() {

        inActAmount += 1;
        inActNextPatient(Patient.FIRST_TYPE);
    }

    @Override
    protected void inActNextElement() {

        Patient next = getPatient();

        switch (next) {
            case FIRST_TYPE -> type1Created+=1;
            case SECOND_TYPE -> type2Created+=1;
            case THIRD_TYPE -> type3Created+=1;
        }

        inActNextPatient(next);
    }

    private void inActNextPatient(Patient patient) {

        Element nextElement = getNextElement();

        if (nextElement instanceof DutyDoctor) {

            DutyDoctor dutyDoctor = (DutyDoctor) nextElement;
            dutyDoctor.inAct(patient);
            return;
        }

        nextElement.inAct();
    }

    private Patient getPatient() {

        double r = random.nextDouble();

        if (r < 0.5) { return Patient.FIRST_TYPE; }
        if (r < 0.6) { return Patient.SECOND_TYPE; }
        return Patient.THIRD_TYPE;
    }

    public int getInActAmount() {
        return inActAmount;
    }

    public String creationAmountStats() {

        return "#1-"+type1Created+"; #2-"+type2Created+"; #3-"+type3Created+";";
    }
}
