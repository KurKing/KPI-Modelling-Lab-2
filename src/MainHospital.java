import elements.Element;
import elements.Process;
import elements.distribution.Distribution;
import hospital.DutyDoctor;
import hospital.Laborant;
import hospital.PatientCreator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

        double tcurr = Model.simulate(combinedList, 10000.0);

        System.out.println("\nTIME for 1: "+formatMinutesToHoursAndMinutes(getSumTime(List.of(
                dutyDoctorsElement,
                accompanying
        ), tcurr)));
        System.out.println("TIME for 2 and 3: "+formatMinutesToHoursAndMinutes(getSumTime(List.of(
                dutyDoctorsElement,
                dutyToRegistrationHall,
                registration,
                laborants,
                registrationToDutyHall,
                dutyDoctorsElement,
                accompanying
        ), tcurr)));
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

    private static double getSumTime(List<List<Element>> items, double tcurr) {

        return items.stream()
                .mapToDouble(item -> getAverageMeanTimeOfList(item, tcurr))
                .sum();
    }

    private static double getAverageMeanTimeOfList(List<Element> list, double tcurr) {

        return list.stream()
                .map(element -> (element instanceof Process) ? (Process) element : null)
                .filter(Objects::nonNull)
                .mapToDouble(Process::getMeanLeaveTime)
                .filter(value -> !Double.isNaN(value))
                .max()
                .orElse(0.0);
    }

    public static String formatMinutesToHoursAndMinutes(double minutes) {

        long minutesLong = (long) minutes;
        long hours = minutesLong / 60;
        long remainingMinutes = minutesLong % 60;
        return String.format("%02d:%02d", hours, remainingMinutes);
    }
}
