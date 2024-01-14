package hospital;

import elements.Create;
import elements.Element;
import elements.distribution.Distribution;

import java.util.Random;

public class PatientCreator extends Create {

    private final Random random = new Random();

    public PatientCreator(double delay, String name, Distribution distribution) {
        super(delay, name, distribution);
    }

    @Override
    public void inAct() {

        inActNextPatient(Patient.FIRST_TYPE);
    }

    @Override
    protected void inActNextElement() {

        inActNextPatient(getPatient());
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
}
