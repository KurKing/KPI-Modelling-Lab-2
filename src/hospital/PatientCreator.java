package hospital;

import elements.Create;
import elements.Element;
import elements.distribution.Distribution;

public class PatientCreator extends Create {

    private final Patient patientType;

    public PatientCreator(Patient patientType) {

        super(patientType.getCreationDelay(), patientType.getName() + " CREATOR", Distribution.EXPONENTIAL);
        this.patientType = patientType;
    }

    @Override
    protected void inActNextElement() {

        Element nextElement = getNextElement();

        if (nextElement instanceof DutyDoctor) {

            DutyDoctor dutyDoctor = (DutyDoctor) nextElement;
            dutyDoctor.inAct(patientType);
            return;
        }

        nextElement.inAct();
    }
}
