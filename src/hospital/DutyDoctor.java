package hospital;

import elements.Element;
import elements.Process;
import elements.distribution.Distribution;

import java.util.List;

public class DutyDoctor extends Process  {

    private int queueType1, queueType2, queueType3;
    private int type1Executed, type2Executed, type3Executed;

    private Patient currentPatient;

    private List<Element> registration;

    public DutyDoctor(String name, Distribution distribution) {
        super(0, name, distribution);
    }

    public void inAct(Patient patientType) {

        currentPatient = patientType;

        switch (state) {
            case UNLOCKED -> {

                currentPatient = patientType;
                inActForUNLOCKEDState();
            }
            case LOCKED -> {

                if (getQueue() < maxqueue) {

                    switch (patientType) {
                        case FIRST_TYPE -> {
                            queueType1 += 1;
                        }
                        case SECOND_TYPE -> {
                            queueType2 += 1;
                        }
                        case THIRD_TYPE -> {
                            queueType3 += 1;
                        }
                    }
                    return;
                }

                failure++;
            }
        }
    }

    @Override
    protected void inActNextElement() {

        List<Element> nextElements;
        if (currentPatient == Patient.FIRST_TYPE) {
            // Choose accompanying
            nextElements = this.nextElements;
        } else {
            // Choose registration window
            nextElements = registration;
        }

        Element nextElement = chooseBestNextElement(nextElements);
        if (nextElement != null) {
            nextElement.inAct();
        }
    }

    @Override
    protected void pickUpEventFromQueue() {

        if (queueType1 > 0) {

            queueType1 -= 1;
            currentPatient = Patient.FIRST_TYPE;
        } else if (queueType2 > 0) {

            queueType2 -= 1;
            currentPatient = Patient.SECOND_TYPE;
        } else if (queueType3 > 0) {

            queueType3 -= 1;
            currentPatient = Patient.THIRD_TYPE;
        }

        inActForUNLOCKEDState();
    }

    @Override
    protected void inActForUNLOCKEDState() {

        switch (currentPatient) {
            case FIRST_TYPE -> type1Executed += 1;
            case SECOND_TYPE -> type2Executed += 1;
            case THIRD_TYPE -> type3Executed += 1;
        }

        super.inActForUNLOCKEDState();
    }

    @Override
    public int getQueue() {
        return queueType1 + queueType2 + queueType3;
    }

    @Override
    protected double getDelay() {
        return currentPatient.getRegistryTime();
    }

    public void setRegistration(List<Element> registration) {
        this.registration = registration;
    }

    @Override
    public double getDelayMean() {

        return (Patient.FIRST_TYPE.getRegistryTime() * type1Executed +
                Patient.SECOND_TYPE.getRegistryTime() * type2Executed +
                Patient.THIRD_TYPE.getRegistryTime() * type3Executed) / (type1Executed + type2Executed + type3Executed);
    }
}
