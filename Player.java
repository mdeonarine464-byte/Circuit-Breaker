import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private int lives;
    private List<String> inventory;
    private int maxLives; 
    private boolean isUnpowered = false; 
    private VendingMachine vendingMachine;

    public Player(String name, VendingMachine vm) {
        this.name = name;
        this.lives = 3;
        this.inventory = new ArrayList<>();
        this.vendingMachine = vm;
        this.maxLives = 3; // Default max lives for standard game
    }

    public void takeDamage() {
        if (this.lives > 0) {
            this.lives--;
            System.out.println(this.name + " took damage! " + this.lives + " lives remaining.");
        }
    }

    public void heal() {
        if (this.lives < this.maxLives) {
            this.lives++;
            System.out.println(this.name + " healed! " + this.lives + " lives remaining.");
        } else {
            System.out.println(this.name + " is already at max health (" + this.maxLives + ")!");
        }
    }
    
     // Resets the player's current lives to their maximum possible lives. 
    public void healToFull() {
        this.lives = this.maxLives;
    }

    // Setters used by the Game class to manage health during stages
    public void setLives(int newLives) {
        this.lives = newLives;
        if (this.lives > this.maxLives) {
            this.lives = this.maxLives;
        }
    }
    
    public void setMaxLives(int newMaxLives) {
        this.maxLives = newMaxLives;
        // Ensure current lives don't exceed new max lives
        if (this.lives > this.maxLives) {
            this.lives = this.maxLives;
        }
    }

    public void setUnpowered(boolean status) { this.isUnpowered = status; }
    public boolean isUnpowered() { return this.isUnpowered; }
    public boolean isAlive() { return this.lives > 0; }
    public String getName() { return name; }
    public int getLives() { return lives; }
    public int getMaxLives() { return maxLives; }
    public List<String> getInventory() { return inventory; }
    
    public void addItem(String item) { 
         if (this.inventory.size() >= 8) {
        System.out.println("inventory is full! Use more items!" + this.name + " lost " + item + ".");
        return;
    }
        this.inventory.add(item); 
        String description = vendingMachine.getItemDescription(item); 
        System.out.println(this.name + " picked up a " + item + "! (" + description + ")");
    }
    
    public boolean useItem(String item) { return this.inventory.remove(item); }
}
