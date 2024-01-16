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
            Process p = new Process(3, "REGISTRATION " + (i+1), Distribution.ERLANG);
            p.setDelayDev(4.5);
            registration.add(p);
        }

        List<Element> dutyToRegistrationHall = getHall(2, 5, "from duty");
        for (Element hall: dutyToRegistrationHall) {
            hall.setNextElement(registration);
        }

        for (DutyDoctor dutyDoctor: dutyDoctors) {
            dutyDoctor.setNextElement(accompanying);
            dutyDoctor.setRegistration(dutyToRegistrationHall);
        }

        List<Element> registrationToDutyHall = getHall(2, 5, "from registration");
        for (Element hall: registrationToDutyHall) {
            hall.setNextElement(List.of(creator));
        }

        List<Element> laborants = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            Laborant p = new Laborant(2, "LABORANT " + (i+1), Distribution.ERLANG, 0.5);
            p.setDelayDev(4);
            p.setNextElement(registrationToDutyHall);
            laborants.add(p);
        }

        for (Element r: registration) {
            r.setNextElement(laborants);
        }

        List<Element> combinedList = new ArrayList<>();
        combinedList.add(creator);
        combinedList.addAll(dutyDoctorsElement);
        combinedList.addAll(accompanying);
        combinedList.addAll(dutyToRegistrationHall);
        combinedList.addAll(registration);
        combinedList.addAll(registrationToDutyHall);
        combinedList.addAll(laborants);

        Model.simulate(combinedList, 10000.0);
    }

    private static List<Element> getHall(double delay, double dev, String name) {

        List<Element> halls = new ArrayList<>();
        for (int i = 0; i < 3; i++) {

            Process p = new Process(delay, "HALL "+name, Distribution.NORMAL);
            p.setDelayDev(dev);
            halls.add(p);
        }

        return halls;
    }
}
