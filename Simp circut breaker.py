import random

class Player:
    def __init__(self, name, vending_machine):
        self.name = name
        self.max_lives = 3
        self.lives = 3
        self.inventory = []
        self.vending_machine = vending_machine
        self.is_unpowered = False

    def take_damage(self, amount=1):
        for _ in range(amount):
            if self.lives > 0:
                self.lives -= 1
                print(f"{self.name} took damage! {self.lives} lives remaining.")

    def heal(self):
        if self.lives < self.max_lives:
            self.lives += 1
            print(f"{self.name} healed! {self.lives} lives remaining.")
        else:
            print(f"{self.name} is already at max health ({self.max_lives})!")

    def add_item(self, item):
        if len(self.inventory) >= 8:
            print(f"Inventory full! {self.name} lost {item}.")
            return
        self.inventory.append(item)
        desc = self.vending_machine.get_item_description(item)
        print(f"{self.name} picked up a {item}! ({desc})")

class PowerStrip:
    def __init__(self):
        self.batteries = []
        self.charged_count = 0
        self.multiplier = 1

    def load(self, battery_list):
        self.batteries = battery_list
        self.charged_count = sum(1 for b in self.batteries if b)

    def pull_trigger(self):
        if not self.batteries: return None
        is_charged = self.batteries.pop(0)
        if is_charged: self.charged_count -= 1
        print(f"CLICK! It was a {'CHARGED' if is_charged else 'DEAD'} battery.")
        return is_charged

class VendingMachine:
    def __init__(self):
        self.items = {
            "BUTTON": "Discards current battery and views next one.",
            "VOLTAGE METER": "Reveals if the next battery is charged.",
            "RESET BUTTON": "Heals you for one life.",
            "POWER SWITCH": "Opponent skips their next turn.",
            "HAMMER": "Doubles the next damage (2x Voltage)."
        }

    def get_item_description(self, name):
        return self.items.get(name, "No description.")

    def generate_round(self, round_num):
        total = random.randint(2, 8) if round_num > 1 else 3
        charged = 1 if round_num == 1 else random.randint(1, total - 1)
        bats = [True] * charged + [False] * (total - charged)
        random.shuffle(bats)
        print(f"\n[Vending Machine]: Dispensing {charged} charged and {total-charged} dead batteries.")
        return bats

class Game:
    def __init__(self, overcharged=False):
        self.vm = VendingMachine()
        self.player = Player("You", self.vm)
        self.electrician = Player("Electrician", self.vm)
        self.strip = PowerStrip()
        self.is_overcharged = overcharged
        self.round_num = 0
        self.player_turn = True

    def start(self):
        print("\n*** Welcome to Circuit Breaker (Python Port) ***")
        while self.player.lives > 0 and self.electrician.lives > 0:
            self.start_round()
            self.play_loop()
        self.check_win()

    def start_round(self):
        self.round_num += 1
        print(f"\n--- Round {self.round_num} ---")
        self.strip.load(self.vm.generate_round(self.round_num))
        self.player.add_item(random.choice(list(self.vm.items.keys())))
        self.electrician.add_item(random.choice(list(self.vm.items.keys())))

    def play_loop(self):
        while self.strip.batteries and self.player.lives > 0 and self.electrician.lives > 0:
            current = self.player if self.player_turn else self.electrician
            if current.is_unpowered:
                print(f"{current.name} is unpowered! Skipping turn.")
                current.is_unpowered = False
                self.player_turn = not self.player_turn
                continue
            
            print(f"\n--- {current.name}'s Turn ---")
            if current == self.player:
                self.handle_player_turn()
            else:
                self.handle_ai_turn()

    def handle_player_turn(self):
        print(f"Inventory: {self.player.inventory}")
        choice = input("(S)elf, (O)pponent, or (U)se [item]: ").strip().lower()
        if choice == 's':
            if self.strip.pull_trigger():
                self.player.take_damage(self.strip.multiplier)
                self.player_turn = False
            self.strip.multiplier = 1
        elif choice == 'o':
            if self.strip.pull_trigger():
                self.electrician.take_damage(self.strip.multiplier)
            self.player_turn = False
            self.strip.multiplier = 1

    def handle_ai_turn(self):
        # Simplified AI: 50/50 risk
        if random.random() < 0.5:
            print("Electrician connects to... THEMSELVES!")
            if self.strip.pull_trigger():
                self.electrician.take_damage()
                self.player_turn = True
        else:
            print("Electrician connects to... YOU!")
            if self.strip.pull_trigger():
                self.player.take_damage()
            self.player_turn = True

    def check_win(self):
        if self.player.lives <= 0:
            print("You feel a hundred thousand volts... Game Over.")
        else:
            print("The Electrician is down! You win!")

if __name__ == "__main__":
    game = Game()
    game.start()
