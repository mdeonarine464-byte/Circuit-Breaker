import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class PowerStrip {
    private List<Boolean> batteries = new ArrayList<>();
    private int chargedBatteriesLoaded = 0;
    private int voltageMultiplier = 1;

    public void loadBatteries(List<Boolean> batteriesToLoad) {
        batteries.clear();
        this.batteries.addAll(batteriesToLoad);
        this.chargedBatteriesLoaded = (int) batteriesToLoad.stream().filter(b -> b).count();
    }

    public boolean pullTrigger() {
        if (isEmpty()) {
            System.out.println("All batteries are used. Dispensing new battires.");
            return false;
        }
        boolean isCharged = batteries.remove(0);
        if (isCharged) chargedBatteriesLoaded--;
        System.out.println("CLICK! It was a " + (isCharged ? "CHARGED" : "DEAD") + " battery.");
        int currentMult = this.voltageMultiplier; 
    this.voltageMultiplier = 1; 
    
    return isCharged;
    }

    public void discardCurrentBattery() {
        if (!isEmpty()) {
            boolean discardedCharged = batteries.remove(0);
            if (discardedCharged) chargedBatteriesLoaded--;
            System.out.println("The current battery was discarded.");
        }
    }

    public boolean inspectCurrentBattery() {
        return !isEmpty() && batteries.get(0);
    }

    public int getBatteryCount() { return batteries.size(); }
    public int getChargedCount() { return chargedBatteriesLoaded; }
    public void setVoltageMultiplier(int multiplier) { this.voltageMultiplier = multiplier; }
    public int getVoltageMultiplier() { return this.voltageMultiplier; }
    public boolean isEmpty() { return batteries.isEmpty(); }
}
