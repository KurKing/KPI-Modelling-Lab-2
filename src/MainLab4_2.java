import elements.Create;
import elements.Element;
import elements.Process;
import elements.distribution.Distribution;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class MainLab4_2 {

    public static void main(String[] args) {

        for (int i = 10; i < 1000000; i+=50000) {
            launch(i);
        }

        for (int i = 1000000; i < 10000000; i+=500000) {
            launch(i);
        }
    }

    private static void launch(int N) {

        System.out.println(N + "-N model launch time: " + formatMillisecondsToTime(launchModel(N)));
    }

    private static long launchModel(int N) {

        Create create = new Create(1.0, "CREATOR", Distribution.NONE);

        List<Element> combinedList = new ArrayList<>(List.of(create));
        List<Element> pList = new ArrayList<>();

        for (int i = 0; i < N; i++) {

            Process process = new Process(N / 2, "PROCESSOR #" + (i + 1), Distribution.EXPONENTIAL);
            process.setMaxqueue(3);

            List<Element> subList = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                Process subprocess = new Process(N / 2, "SUBPROCESSOR #" + (i + 1) + "." + (j + 1), Distribution.EXPONENTIAL);
                subprocess.setMaxqueue(3);
                subList.add(subprocess);
            }

            process.setNextElement(subList);
            combinedList.addAll(subList);

            pList.add(process);
        }

        create.setNextElement(pList);

        combinedList.addAll(pList);

        Instant start = Instant.now();

        Model.simulate(combinedList, 50, false);

        return Duration.between(start, Instant.now()).toMillis();
    }

    private static String formatMillisecondsToTime(long milliseconds) {

        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss.SSS");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        Date date = new Date(milliseconds);

        return sdf.format(date);
    }
}
