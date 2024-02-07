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

public class MainLab4 {

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

        List<Element> processes = new ArrayList<>();
        for (int i = 0; i < N; i++) {

            Process process = new Process(N, "PROCESSOR #" + (i + 1), Distribution.EXPONENTIAL);
            process.setMaxqueue(3);
            processes.add(process);
        }

        create.setNextElement(processes);

        List<Element> combinedList = new ArrayList<>(List.of(create));
        combinedList.addAll(processes);

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
