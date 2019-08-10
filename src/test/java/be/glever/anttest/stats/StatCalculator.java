package be.glever.anttest.stats;

import be.glever.antplus.hrm.datapage.AbstractHRMDataPage;
import be.glever.antplus.hrm.datapage.main.HrmDataPage4PreviousHeartBeatEvent;
import be.glever.util.logging.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * for fun. push antmessages and retrieve some stats after the push.
 */
public class StatCalculator {
    //    public static final long HEARTBEAT_UNITS_SEC = 1 / 1024; // TODO bug. resolves to 0.
    public static final long HEARTBEAT_ROLLOVER = 64000;
    public static final long HEARTBEAT_ROLLOVER_IN_MILLIS = 62500; //1000 * (HEARTBEAT_UNITS_SEC * HEARTBEAT_ROLLOVER);
    private static final Log LOG = Log.getLogger(StatCalculator.class);
    private List<HrmDataPage4PreviousHeartBeatEvent> heartBeats = new ArrayList<>();

    private static boolean isNewHeartBeatEventTime(HrmDataPage4PreviousHeartBeatEvent page) {
        return page.getPreviousHeartBeatEventTime() < page.getHeartBeatEventTime();
    }

    public StatSummary push(HrmDataPage4PreviousHeartBeatEvent dataPage) {
        if (isNewHeartBeatEventTime(dataPage)) {
            heartBeats.add(dataPage);
        }
        return createStatSummary();
    }

    private StatSummary createStatSummary() {
        StatSummary statSummary = new StatSummary();
        statSummary.setLastHeartBeat(heartBeats.get(heartBeats.size() - 1));

        final int listSize = 500;
        List<HrmDataPage4PreviousHeartBeatEvent> periodHeartBeats = getLast(heartBeats, listSize);
        List<HrmDataPage4PreviousHeartBeatEvent> rmssdPeriod = getLast(heartBeats, listSize);

        double meanRr = calcMeanRr(periodHeartBeats);
        double standardDeviationDelta = calcStdDevRr(periodHeartBeats, meanRr);
        double rmssd = calcRmssd(periodHeartBeats);
        double meanHeartRate = periodHeartBeats.stream().mapToDouble(AbstractHRMDataPage::getComputedHeartRateInBpm).average().orElse(0);

//        double meanRr = calcMeanRr(rmssdPeriod);
//        double standardDeviationDelta = calcStdDevRr(rmssdPeriod, meanRr);
//        double rmssd = calcRmssd(rmssdPeriod);

        statSummary.setMeanRr(meanRr);
        statSummary.setStdDevRr(standardDeviationDelta);
        statSummary.setRmssd(rmssd);
        statSummary.setMeanHeartRate(meanHeartRate);

        return statSummary;
    }

    private List<HrmDataPage4PreviousHeartBeatEvent> getLast(List<HrmDataPage4PreviousHeartBeatEvent> heartBeats, int slice) {
        return heartBeats.size() < slice ? heartBeats : heartBeats.subList(heartBeats.size() - slice, heartBeats.size());
    }

    private double calcStdDevRr(List<HrmDataPage4PreviousHeartBeatEvent> periodHeartBeats, double meanDelta) {
        return periodHeartBeats.stream()
                .mapToDouble(page -> Math.abs(meanDelta - (calcRr(page))))
                .average()
                .orElse(0);
    }

    private double calcMeanRr(List<HrmDataPage4PreviousHeartBeatEvent> periodHeartBeats) {
        return periodHeartBeats.stream()
                .mapToDouble(this::calcRr)
                .average()
                .orElse(0);
    }

    private double calcRr(HrmDataPage4PreviousHeartBeatEvent page) {
        double curTime = page.getHeartBeatEventTime();
        double prevTime = page.getPreviousHeartBeatEventTime();
        if (curTime < prevTime) {
            curTime += HEARTBEAT_ROLLOVER;
        }
        return curTime - prevTime;
    }

    private double calcRmssd(List<HrmDataPage4PreviousHeartBeatEvent> periodHeartBeats) {
        double squareSuccessiveDiffs = 0;
        int size = periodHeartBeats.size() - 1;
        for (int i = 0; i < size; i++) {
            double prevRr = calcRr(periodHeartBeats.get(i));
            double curRr = calcRr(periodHeartBeats.get(i + 1));
            squareSuccessiveDiffs += Math.pow(curRr - prevRr, 2);
        }
        double meanSquareSuccessiveDiffs = squareSuccessiveDiffs / size;
        return Math.sqrt(meanSquareSuccessiveDiffs);
    }
}
