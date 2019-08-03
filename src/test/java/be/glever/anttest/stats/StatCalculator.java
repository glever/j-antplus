package be.glever.anttest.stats;

import be.glever.antplus.hrm.datapage.main.HrmDataPage4PreviousHeartBeatEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * for fun. push antmessages and retrieve some stats after the push.
 */
public class StatCalculator {
    private static final Logger LOG = LoggerFactory.getLogger(StatCalculator.class);

    public static final int HEARTBEAT_UNITS_SEC = 1 / 1024; // TODO bug. resolves to 0.
    public static final int HEARTBEAT_ROLLOVER = 64000;
    public static final int HEARTBEAT_ROLLOVER_IN_MILLIS = 1000 * (HEARTBEAT_UNITS_SEC * HEARTBEAT_ROLLOVER); // 62500
    private List<HrmDataPage4PreviousHeartBeatEvent> heartBeats = new ArrayList<>();

    public StatSummary push(HrmDataPage4PreviousHeartBeatEvent dataPage) {
        heartBeats.add(dataPage);
        return createStatSummary();
    }

    private StatSummary createStatSummary() {
        StatSummary statSummary = new StatSummary();
        statSummary.setLastHeartBeat(heartBeats.get(heartBeats.size() - 1));
        int window = 10;
        List<HrmDataPage4PreviousHeartBeatEvent> periodHeartBeats = heartBeats.size() < window ? heartBeats : heartBeats.subList(heartBeats.size() - window, heartBeats.size());

        int rmssdListSize = Math.min(heartBeats.size(), 100);
        List<HrmDataPage4PreviousHeartBeatEvent> rmssdPeriod = heartBeats.subList(heartBeats.size() - rmssdListSize, heartBeats.size());

        double meanRr = calcMeanRr(periodHeartBeats);
        double standardDeviationDelta = calcStdDevRr(periodHeartBeats, meanRr);

        statSummary.setMeanRr(meanRr);
        statSummary.setStdDevRr(standardDeviationDelta);
        statSummary.setRmssd(calcRmssd(rmssdPeriod));

        return statSummary;
    }

    private double calcStdDevRr(List<HrmDataPage4PreviousHeartBeatEvent> periodHeartBeats, double meanDelta) {
        return periodHeartBeats.stream().filter(page -> page.getPreviousHeartBeatEventTime() < page.getHeartBeatEventTime())
                .mapToDouble(page -> Math.abs(meanDelta - (calcRr(page))))
                .average().getAsDouble();
    }

    private double calcMeanRr(List<HrmDataPage4PreviousHeartBeatEvent> periodHeartBeats) {
        return periodHeartBeats.stream()
                .mapToDouble(this::calcRr)
                .average()
                .getAsDouble();
    }

    private double calcRr(HrmDataPage4PreviousHeartBeatEvent page) {
        double prevTime = page.getHeartBeatEventTime();
        double curTime = page.getPreviousHeartBeatEventTime();
        if (curTime < prevTime) {
            curTime += HEARTBEAT_ROLLOVER_IN_MILLIS;
        }
        return curTime - prevTime;
    }

    private double calcRmssd(List<HrmDataPage4PreviousHeartBeatEvent> periodHeartBeats) {
        double squareSuccessiveDiffs = 0;
        for (int i = 0; i < periodHeartBeats.size() - 1; i++) {
            double prevRr = calcRr(periodHeartBeats.get(i));
            double curRr = calcRr(periodHeartBeats.get(i + 1));
            squareSuccessiveDiffs += Math.pow(curRr - prevRr, 2);
        }
        double meanSquareSuccessiveDiffs = squareSuccessiveDiffs / (periodHeartBeats.size() - 1);
        double rmssd = Math.sqrt(meanSquareSuccessiveDiffs);
        LOG.info("calcRmssd: periodHeartBeats.size()={}, meanSquareSuccessiveDiffs={}, rmssd={}", periodHeartBeats.size(), meanSquareSuccessiveDiffs, rmssd);
        return rmssd;
    }
}
