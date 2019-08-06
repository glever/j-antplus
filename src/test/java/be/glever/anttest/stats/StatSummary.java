package be.glever.anttest.stats;

import be.glever.antplus.hrm.datapage.main.HrmDataPage4PreviousHeartBeatEvent;

public class StatSummary {
    private HrmDataPage4PreviousHeartBeatEvent currentBeat;
    private double meanRr;
    private double stdDevRr;
    private double rmssd;
    private double meanHeartRate;

    public void setLastHeartBeat(HrmDataPage4PreviousHeartBeatEvent hrmDataPage4PreviousHeartBeatEvent) {

        this.currentBeat = hrmDataPage4PreviousHeartBeatEvent;
    }

    public void setMeanRr(double meanRr) {
        this.meanRr = meanRr;
    }


    public void setStdDevRr(double stdDevRr) {
        this.stdDevRr = stdDevRr;
    }

    @Override
    public String toString() {
        return "StatSummary{" +
//                "currentBeat=" + currentBeat +
                ", delta=" + (currentBeat.getHeartBeatEventTime() - currentBeat.getPreviousHeartBeatEventTime()) +
                ", meanHr=" + meanHeartRate +
                ", meanRr=" + meanRr +
                ", stdDevRr=" + stdDevRr +
                ", rmssd=" + rmssd +
                '}';
    }

    public void setRmssd(double rmssd) {
        this.rmssd = rmssd;
    }

    public void setMeanHeartRate(double meanHeartRate) {

        this.meanHeartRate = meanHeartRate;
    }
}
