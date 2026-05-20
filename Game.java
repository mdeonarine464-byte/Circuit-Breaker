import java.util.Scanner;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.List;
import java.lang.Math;

public class Game {
    private Player player;
    private Player electrician;
    private PowerStrip powerStrip;
    private VendingMachine vendingMachine;
    private Scanner scanner = new Scanner(System.in);
    private Random random = new Random();
    private boolean playerTurn = true;
    private boolean gameOver = false;
    private int currentRound = 0;
    private boolean isOvercharged;
    private boolean forceRoundEnd = false;

    // OVERCHARGED specific fields
    private final int STAGE_TWO_START = 2; 
    private final int STAGE_THREE_START = 3; 
    private final int MAX_LIVES_STAGE_TWO = 7;
    private final int MAX_LIVES_STAGE_THREE = 12;
    private int itemsPerRound = 0;
    private final int STANDARD_MAX_LIVES = 3;

    public Game(boolean initialIsOvercharged) {
        this.isOvercharged = initialIsOvercharged;
        this.vendingMachine = new VendingMachine();
        this.player = new Player("You", this.vendingMachine);
        this.electrician = new Player("Electrician", this.vendingMachine);
        this.powerStrip = new PowerStrip();
        
        if (!isOvercharged) {
            this.playerTurn = true; 
        }
    }

    public void start() {
        if (!isOvercharged) {
            startScreenDisplay();//displayStartScreen();
             System.out.println("\n*** Tutorial ***");
             System.out.println("You have a power strip that you plug batteries into ");
             System.out.println("Live batteries shock you dead ones dont");
             System.out.println("\nOn your turn:");
             System.out.println("Use S to connect the battery to your strip ");
             System.out.println("Use O to connect to the opponents ");
             System.out.println("Use U then the item name to use an item ");
             System.out.println("Welcome to Circuit Breaker!");
        }
       
        while (!gameOver) {
            startNewRound();
            if (!gameOver) {
                playRound();
            }
        }
        endScreen();
    }
    
    
    
    
    private void endScreen() {
        System.out.println("***************************************************************************************************************************************");
        System.out.println("**  ,ad8888ba,          db         88b           d88  88888888888        ,ad8888ba,    8b           d8  88888888888  88888888ba   88 **");
        System.out.println("** d8\"'    `\"8b        d88b        888b         d888  88                d8\"'    `\"8b   `8b         d8'  88           88      \"8b  88 **");
        System.out.println("**d8'                 d8'`8b       88`8b       d8'88  88               d8'        `8b   `8b       d8'   88           88      ,8P  88 **");
        System.out.println("**88                 d8'  `8b      88 `8b     d8' 88  88aaaaa          88          88    `8b     d8'    88aaaaa      88aaaaaa8P'  88 **");
        System.out.println("**88      88888     d8YaaaaY8b     88  `8b   d8'  88  88\"\"\"\"\"          88          88     `8b   d8'     88\"\"\"\"\"      88\"\"\"\"88'    88 **");
        System.out.println("**Y8,        88    d8\"\"\"\"\"\"\"\"8b    88   `8b d8'   88  88               Y8,        ,8P      `8b d8'      88           88    `8b    \"\" **");
        System.out.println("** Y8a.    .a88   d8'        `8b   88    `888'    88  88                Y8a.    .a8P        `888'       88           88     `8b   aa **");
        System.out.println("**  `\"Y88888P\"   d8'          `8b  88     `8'     88  88888888888        `\"Y8888Y\"'          `8'        88888888888  88      `8b  88 **");
        System.out.println("***************************************************************************************************************************************");
    }
    
    private void displayStartScreen() {
        System.out.println("Walking through the rain you see it in the back alley of Cyberhell city; the tarp, the table and the undeniable ");
        System.out.println("prescence of the electrician with in their deep dark trench coat that shelids whatever lies inside from the rain, ");
        System.out.println("and his mask whith two neon side the left a electric blue and  rageful red on the right but no matter how bright "); 
        System.out.println("it glows the trench coat swallows the light like a void. The hum of the neon blue lights in the vending machine "); 
        System.out.println("that lights up the table where you see the power strip");
        System.out.println("                                                                                  ");
        System.out.println("Above it the neon sign illuminating. ");
        System.out.println("                                                                                  ");
        
        
        
        
        System.out.println("**************************************************************************************");
        System.out.println("**  ,ad8888ba,   88                                       88                        **");
        System.out.println("** d8\"'    `\"8b  \"\"                                       \"\"    ,d                  **");
        System.out.println("**d8'                                                           88                  **");
        System.out.println("**88             88  8b,dPPYba,   ,adPPYba,  88       88  88  MM88MMM               **");
        System.out.println("**88             88  88P'   \"Y8  a8\"     \"\"  88       88  88    88                  **");
        System.out.println("**Y8,            88  88          8b          88       88  88    88                  **");
        System.out.println("** Y8a.    .a8P  88  88          \"8a,   ,aa  \"8a,   ,a88  88    88,                 **");
        System.out.println("**  `\"Y8888Y\"'   88  88           `\"Ybbd8\"'   `\"YbbdP'Y8  88    \"Y888               **");
        System.out.println("**                                                                                  **");
        System.out.println("**                                                                                  **");
        System.out.println("**                                                                                  **");
        System.out.println("**88888888ba                                       88                               **");
        System.out.println("**88      \"8b                                      88                               **");
        System.out.println("**88      ,8P                                      88                               **");
        System.out.println("**88aaaaaa8P'  8b,dPPYba,   ,adPPYba,  ,adPPYYba,  88   ,d8   ,adPPyba,  8b,dPPYba, **");
        System.out.println("**88\"\"\"\"\"\"8b,  88P'   \"Y8  a8P_____88  \"\"     `Y8  88 ,a8\"   a8P_____88  88P'   \"Y8 **");
        System.out.println("**88      `8b  88          8PP\"\"\"\"\"\"\"  ,adPPPPP88  8888[     8PP\"\"\"\"\"\"\"  88         **");
        System.out.println("**88      a8P  88          \"8b,   ,aa  88,    ,88  88`\"Yba,  \"8b,   ,aa  88         **");
        System.out.println("**88888888P\"   88           `\"Ybbd8\"'  `\"8bbdP\"Y8  88   `Y8a  `\"Ybbd8\"'  88         **");
        System.out.println("**************************************************************************************");
        System.out.println(" not responsible for power outage caused by **OVERCHARGED** ");
        
    }
    //used universi 
    private void startScreenDisplay() {
        System.out.println("Walking through the rain you see it in the back alley of Cyberhell city; the tarp, the table and the undeniable ");
        System.out.println("presents of the electrician with in their deep dark trench coat that shelids whatever lies inside from the rain, ");
        System.out.println("and his mask whith two neon side the left a electric blue and  rageful red on the right but no matter how bright "); 
        System.out.println("it glows the trench coat swallows the light like a void. The hum of the neon blue lights in the vending machine "); 
        System.out.println("that lights up the table where you see the power strip");
        System.out.println("                                                                                  ");
        System.out.println("Above it the neon sign illuminating. ");
        System.out.println("                                                                                  ");
        
        
        
        
        System.out.println("******************************************************************************");
        System.out.println("**88888888ba                                                                **");
        System.out.println("**88      \"8b                                                               **");
        System.out.println("**88      ,8P                                                               **");
        System.out.println("**88aaaaaa8P'  ,adPPYba,   8b      db      d8   ,adPPYba,  8b,dPPYba,       **");
        System.out.println("**88\"\"\"\"\"\"'   a8\"     \"8a  `8b    d88b    d8'  a8P_____88  88P'   \"Y8       **");
        System.out.println("**88          8b       d8   `8b  d8'`8b  d8'   8PP\"\"\"\"\"\"\"  88               **");
        System.out.println("**88          \"8a,   ,a8\"    `8bd8'  `8bd8'    \"8b,   ,aa  88               **");
        System.out.println("**88           `\"YbbdP\"'       YP      YP       `\"Ybbd8\"'  88               **");
        System.out.println("**                                                                          **");
        System.out.println("**                                                                          **");
        System.out.println("**                                                                          **");
        System.out.println("**  ,ad8888ba,                                                              **");
        System.out.println("** d8\"'    `\"8b                  ,d                                         **");
        System.out.println("**d8'        `8b                 88                                         **");
        System.out.println("**88          88  88       88  MM88MMM  ,adPPYYba,   ,adPPYb,d8   ,adPPYba, **");
        System.out.println("**88          88  88       88    88     \"\"     `Y8  a8\"    `Y88  a8P_____88 **");
        System.out.println("**Y8,        ,8P  88       88    88     ,adPPPPP88  8b       88  8PP\"\"\"\"\"\"\" **");
        System.out.println("** Y8a.    .a8P   \"8a,   ,a88    88,    88,    ,88  \"8a,   ,d88  \"8b,   ,aa **");
        System.out.println("**  `\"Y8888Y\"'     `\"YbbdP'Y8    \"Y888  `\"8bbdP\"Y8   `\"YbbdP\"Y8   `\"Ybbd8\"' **");
        System.out.println("**                                                   aa,    ,88             **");
        System.out.println("**                                                    \"Y8bbdP\"              **");
        System.out.println("******************************************************************************");
    System.out.println(" not responsible for broken circuit breakers caused by **OVERCHARGED** ");
        
    }
    
    

    private void startNewRound() {
        currentRound++; 
        System.out.println("\n--- Starting Round " + currentRound + " ---");
        
        if (isOvercharged) {
            int livesToAdd = 0;
            int newMaxCap = 0;

            if (currentRound == 1) {
                itemsPerRound = 0; 
                newMaxCap = STANDARD_MAX_LIVES; 
            } else if (currentRound == STAGE_TWO_START) {
                System.out.println("\n*** STAGE 2 REACHED! LET'S GO CARNIVAL! ***");
                livesToAdd = 4;
                newMaxCap = MAX_LIVES_STAGE_TWO;
                itemsPerRound = 2; 
            } else if (currentRound >= STAGE_THREE_START) { 
                 System.out.println("\n*** STAGE 3 REACHED! EVERYONE AT THE CIRCUIT BREAKER CARNIVAL! ***");
                 livesToAdd = 5; 
                 newMaxCap = MAX_LIVES_STAGE_THREE;
                 itemsPerRound = 4; 
            }

            player.setMaxLives(newMaxCap);
            electrician.setMaxLives(newMaxCap);

            if (livesToAdd > 0) {
                player.setLives(player.getLives() + livesToAdd);
                electrician.setLives(electrician.getLives() + livesToAdd);
            }
            
        } else {
            player.setMaxLives(STANDARD_MAX_LIVES);
            electrician.setMaxLives(STANDARD_MAX_LIVES);
        }

        powerStrip.loadBatteries(vendingMachine.generateBatteriesForRound(currentRound));
        
        if (isOvercharged) {
            for (int i = 0; i < itemsPerRound; i++) {
                player.addItem(vendingMachine.getRandomItem());
                electrician.addItem(vendingMachine.getRandomItem());
            }
        } else {
            player.addItem(vendingMachine.getRandomItem());
            electrician.addItem(vendingMachine.getRandomItem());
        }

        if (currentRound == 1) {
            playerTurn = true;
        } else {
            playerTurn = random.nextBoolean();
        }
    }
    
    private void playRound() {
        while (!powerStrip.isEmpty() && player.isAlive() && electrician.isAlive() && !forceRoundEnd) {
            if ((playerTurn && player.isUnpowered()) || (!playerTurn && electrician.isUnpowered())) {
                Player current = playerTurn ? player : electrician;
                System.out.println("\n--- " + current.getName() + "'s Turn Skipped (Unpowered) ---");
                current.setUnpowered(false);
                playerTurn = !playerTurn;
                continue;
            }
            Player current = playerTurn ? player : electrician;
            System.out.println("\n--- " + current.getName() + "'s Turn ---");
            displayStatus();

            if (current == player) {
                handlePlayerTurn(current);
            } else {
                handleElectricianTurn(current);
            }
            
            if (!player.isAlive() || !electrician.isAlive()) break;
        }
        
        forceRoundEnd = false; 
        checkWinCondition();
    }

    private void applyDamage(Player target) {
        int damage = powerStrip.getVoltageMultiplier();
        for (int i = 0; i < damage; i++) {
            target.takeDamage();
        }
        powerStrip.setVoltageMultiplier(1);
    }

    private void handlePlayerTurn(Player current) {
        boolean validActionTaken = false;
        while (!validActionTaken) {
            String inventoryDisplay = current.getInventory().stream().collect(Collectors.joining(", "));
            System.out.println("Inventory: [" + inventoryDisplay + "]");
            System.out.println("Choose an option: (S)elf connect, (O)pponent connect, or (U)se item [Item Name]");
            
            String input = scanner.nextLine().trim().toLowerCase(); 

            if (input.equals("overcharged")) {
                if (!isOvercharged) {
                    validActionTaken = true; 
                    System.out.println("\n--- OH PARTY TIME! OVERCHARGED ACTIVATED ---");
                    isOvercharged = true;
                    player.getInventory().clear();
                    electrician.getInventory().clear();
                    player.setMaxLives(2);
                    electrician.setMaxLives(2);
                    player.setLives(2);
                    electrician.setLives(2);
                    currentRound = 0; 
                    forceRoundEnd = true; 
                    return;
                    
                    
                    
                } else {
                    System.out.println("What do you want it to be OVER-OVERCHARGED?");
                    continue;
                }
            }
            
            if (input.equals("s")) {
                boolean live = powerStrip.pullTrigger();
                if (live) {
                    applyDamage(current);
                    playerTurn = !playerTurn;
                } else {
                    System.out.println("You kept your turn!");
                }
                validActionTaken = true;
            } else if (input.equals("o")) {
                boolean live = powerStrip.pullTrigger();
                if (live) {
                    applyDamage(electrician);
                } else {
                    System.out.println("It was a dead battery. No damage to the electrician.");
                }
                playerTurn = !playerTurn;
                validActionTaken = true;
            } else if (input.startsWith("u ")) {
                String itemToUse = input.substring(2).trim();

                if (!itemToUse.isEmpty()) {
                    String matchedItem = current.getInventory().stream()
                        .filter(item -> item.equalsIgnoreCase(itemToUse))
                        .findFirst()
                        .orElse(null);

                    if (matchedItem != null) {
                        useItem(current, matchedItem);
                        validActionTaken = true;
                    } else {
                        System.out.println("You don't have that item, or misspelled it. Try again.");
                    }
                } else {
                    System.out.println("No item name entered. Try again.");
                }
            } else {
                System.out.println("Invalid input. Try again.");
            }
        }
    }

    private void useItem(Player user, String item) {
        System.out.println(user.getName() + " uses the " + item + "!"); 
        user.useItem(item);

        switch (item) {
            case "BUTTON":
                boolean wasCharged = powerStrip.inspectCurrentBattery();
                String discardedType = wasCharged ? "CHARGED" : "DEAD";
                powerStrip.discardCurrentBattery();
                System.out.println("The discarded battery was " + discardedType + ".");
                boolean isLiveB = powerStrip.inspectCurrentBattery();
                System.out.println("The NEXT battery is " + (isLiveB ? "CHARGED" : "DEAD") + ".");
                break;
            case "VOLTAGE METER":
                boolean isLiveMVF = powerStrip.inspectCurrentBattery();
                System.out.println("The next battery is " + (isLiveMVF ? "CHARGED" : "DEAD") + ".");
                break;
            case "RESET BUTTON":
                user.heal();
                break;
            case "POWER SWITCH":
                Player opponent = (user == player) ? electrician : player;
                opponent.setUnpowered(true); 
                System.out.println(opponent.getName() + " is unpowered and will skip their next turn!");
                break;
            case "HAMMER":
                powerStrip.setVoltageMultiplier(2);
                System.out.println("The battery is smashed. The next connection will deal 2 damage!");
                break;
        }
    }

    private void handleElectricianTurn(Player current) {
        int liveCount = powerStrip.getChargedCount();
        int totalCount = powerStrip.getBatteryCount();
        double liveOdds = (double) liveCount / totalCount;
        Player opponent = (current == player) ? electrician : player;
        List<String> inventory = current.getInventory();

        if (isOvercharged) { 
            if (inventory.contains("VOLTAGE METER") && liveOdds > 0.3 && liveOdds < 0.7) {
                useItem(current, "VOLTAGE METER");
                handleElectricianTurn(current); 
                return;
            }
            if (inventory.contains("BUTTON") && liveOdds > 0.45 && liveOdds < 0.55) {
                useItem(current, "BUTTON");
                handleElectricianTurn(current); 
                return;
            }
            if (inventory.contains("POWER SWITCH") && liveOdds <= 0.5) {
                 useItem(current, "POWER SWITCH");
            }
            
            if (powerStrip.inspectCurrentBattery()) {
                if (inventory.contains("HAMMER") && opponent.getLives() > 1) {
                     useItem(current, "HAMMER");
                     handleElectricianTurn(current); 
                     return;
                }
                System.out.println("The Electrician plugs the battery into YOUR STRIP!");
                powerStrip.pullTrigger();
                applyDamage(player);
                playerTurn = !playerTurn;
                return;

            } else {
                if (inventory.contains("RESET BUTTON") && current.getLives() < current.getMaxLives()) {
                    useItem(current, "RESET BUTTON");
                    handleElectricianTurn(current);
                    return;
                }
                System.out.println("The Electrician plugs the battery into their strip!");
                powerStrip.pullTrigger();
                System.out.println("Electrician kept their turn with a dead battery!");
                return; 
            }
        } else {
             if (!current.getInventory().isEmpty()) {
                if (current.getInventory().contains("HAMMER") && powerStrip.inspectCurrentBattery()) {
                     useItem(current, "HAMMER");
                } else if (random.nextDouble() < 0.3) {
                    String item = current.getInventory().get(0);
                    useItem(current, item);
                }
            }
        }
        
        boolean connectSelf = (liveOdds <= 0.5);

        if (connectSelf) {
            System.out.println("The Electrician plugs the battery into their strip!");
            boolean live = powerStrip.pullTrigger();
            if (live) {
                applyDamage(current);
                playerTurn = !playerTurn;
            } else {
                System.out.println("Electrician kept their turn with a dead battery!");
            }
        } else {
            System.out.println("The Electrician plugs the battery into YOUR STRIP!");
            boolean live = powerStrip.pullTrigger();
            if (live) {
                applyDamage(player);
            }
            if (!isOvercharged) {
                 playerTurn = !playerTurn;
            }
        }
    }
    
    private void displayStatus() {
        String playerInv = player.getInventory().stream().collect(Collectors.joining(", "));
        String electricianInv = electrician.getInventory().stream().collect(Collectors.joining(", "));

        System.out.println("--- Status ---");
        System.out.printf("%-15s Lives: %d | Inventory: [%s]%n", player.getName(), player.getLives(), playerInv);
        System.out.printf("%-15s Lives: %d | Inventory: [%s]%n", electrician.getName(), electrician.getLives(), electricianInv);
        System.out.println("Batteries left in Vending Machine: " + (powerStrip.getBatteryCount()));
        System.out.println("--------------");
    }

    private void checkWinCondition() {
        if (!player.isAlive()) {
            System.out.println("You feel hundred thousand volts rush through your body as you start to spaz out the last thing you hear is \"I Think somebody broke his circuits\"");
            gameOver = true;
        } else if (!electrician.isAlive()) {
            System.out.println("The Electrician is spazing out on the floor his mask falls off his face, he's screaming his lungs out begging for his life! You just killed him! You're a winner! ");
            gameOver = true;
        }
    }
}
