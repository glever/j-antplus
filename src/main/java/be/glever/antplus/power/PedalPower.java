package be.glever.antplus.power;

public class PedalPower {
    private int percentage;
    private boolean zeroIsLeft;

    public PedalPower(boolean zeroIsLeft, int percentage) {
        this.zeroIsLeft = zeroIsLeft;
        this.percentage = percentage;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public boolean getZeroIsLeft() {
        return zeroIsLeft;
    }
}
