import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class VendingMachine {
    private Random random = new Random();
    private final String[] ITEM_TYPES = {"BUTTON", "VOLTAGE METER", "RESET BUTTON", "POWER SWITCH", "HAMMER"};
    private final Map<String, String> ITEM_DESCRIPTIONS;

    public VendingMachine() {
        ITEM_DESCRIPTIONS = new HashMap<>();
        ITEM_DESCRIPTIONS.put("BUTTON", "Discards the next battery in the strip and views the next battery.");
        ITEM_DESCRIPTIONS.put("VOLTAGE METER", "Reveals if the next battery is charged or dead.");
        ITEM_DESCRIPTIONS.put("RESET BUTTON", "Heals you for one life.");
        ITEM_DESCRIPTIONS.put("POWER SWITCH", "Causes the opponent to skip their next turn.");
        ITEM_DESCRIPTIONS.put("HAMMER", "Doubles the damage of the next connected battery (2x Voltage).");
    }

    public String getRandomItem() {
        return ITEM_TYPES[random.nextInt(ITEM_TYPES.length)];
    }

    public String getItemDescription(String itemName) {
        return ITEM_DESCRIPTIONS.getOrDefault(itemName, "Nothing. If it had an Item description it would be stupid");
    }

    public List<Boolean> generateBatteriesForRound(int currentRound) {
        int totalBatteries = random.nextInt(7) + 2;
        int chargedBatteries;
        
        if (currentRound == 1) {
            chargedBatteries = 1;
            totalBatteries = 3;
        } else if (currentRound < 4) {
            chargedBatteries = random.nextInt(totalBatteries - 1) + 1;
        } else {
            chargedBatteries = random.nextInt(totalBatteries) + 1;
            if (chargedBatteries >= totalBatteries) chargedBatteries = totalBatteries - 1; 
        }

        int deadBatteries = totalBatteries - chargedBatteries;
        if (deadBatteries == 0) { deadBatteries = 1; chargedBatteries--; }
        if (chargedBatteries == 0) { chargedBatteries = 1; deadBatteries--; }

        List<Boolean> newBatteries = new ArrayList<>(totalBatteries);
        for(int i = 0; i < chargedBatteries; i++) newBatteries.add(true);
        for(int i = 0; i < deadBatteries; i++) newBatteries.add(false);
        Collections.shuffle(newBatteries);

        System.out.println("\n[Vending Machine]: Dispensing " + chargedBatteries + " charged and " + deadBatteries + " dead batteries.");
        return newBatteries;
    }
}
