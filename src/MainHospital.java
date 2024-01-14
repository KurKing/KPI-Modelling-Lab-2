import elements.Element;
import elements.Process;
import elements.distribution.Distribution;
import hospital.DutyDoctor;
import hospital.Laborant;
import hospital.PatientCreator;

import java.util.ArrayList;
import java.util.List;

public class MainHospital {

    public static void main(String[] args) {

        PatientCreator creator = new PatientCreator(15, "CREATOR", Distribution.EXPONENTIAL);

        List<DutyDoctor> dutyDoctors = List.of(
            new DutyDoctor("DUTY 1", Distribution.EXPONENTIAL),
            new DutyDoctor("DUTY 2", Distribution.EXPONENTIAL)
        );
        List<Element> dutyDoctorsElement = new ArrayList<>();
        for (DutyDoctor doctor: dutyDoctors) {
            dutyDoctorsElement.add(doctor);
        }
        creator.setNextElement(dutyDoctorsElement);

        List<Element> accompanying = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Process p = new Process(8, "ACCOMP " + (i+1), Distribution.NORMAL);
            p.setDelayDev(3);
            accompanying.add(p);
        }

        List<Element> registration = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            // ERLANG
            Process p = new Process(8, "REGISTRATION " + (i+1), Distribution.EXPONENTIAL);
            registration.add(p);
        }

        List<Element> dutyToRegistrationHall = getHall(2, 5);
        for (Element hall: dutyToRegistrationHall) {
            hall.setNextElement(registration);
        }

        for (DutyDoctor dutyDoctor: dutyDoctors) {
            dutyDoctor.setNextElement(accompanying);
            dutyDoctor.setRegistration(dutyToRegistrationHall);
        }

        List<Element> registrationToDutyHall = getHall(2, 5);
        for (Element hall: dutyToRegistrationHall) {
            hall.setNextElement(List.of(creator));
        }

        List<Element> laborants = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            // ERLANG
            Laborant p = new Laborant(8, "LABORANT " + (i+1), Distribution.EXPONENTIAL, 0.5);
            p.setNextElement(registrationToDutyHall);
            laborants.add(p);
        }

        List<Element> combinedList = new ArrayList<>();
        combinedList.add(creator);
        combinedList.addAll(dutyDoctorsElement);
        combinedList.addAll(accompanying);
        combinedList.addAll(registration);
        combinedList.addAll(dutyToRegistrationHall);
        combinedList.addAll(registrationToDutyHall);
        combinedList.addAll(laborants);

        Model.simulate(combinedList, 10000.0);
    }

    private static List<Element> getHall(double delay, double dev) {

        List<Element> halls = new ArrayList<>();
        for (int i = 0; i < 100; i++) {

            Process p = new Process(delay, "HALL", Distribution.NORMAL);
            p.setDelayDev(dev);
            halls.add(p);
        }

        return halls;
    }
}
