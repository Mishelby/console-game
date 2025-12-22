package ru.mishelby.game;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mishelby.game.enums.ClassCard;
import ru.mishelby.game.enums.PlayerClass;
import ru.mishelby.repository.CardRepository;

import java.util.*;
import java.util.stream.Collectors;

import static ru.mishelby.game.enums.PlayerClass.*;

public class MainGame {
    static Logger logger = LoggerFactory.getLogger(MainGame.class);
    static boolean isWannaPlay = true;


    public static void main(String[] args) {
        var firstScanner = new Scanner(System.in);

        logger.info("Do you wanna play the game? [yes/no]");

        while (isWannaPlay) {
            String userAnswer = firstScanner.nextLine();

            if (!"yes".equalsIgnoreCase(userAnswer) && !"no".equalsIgnoreCase(userAnswer)) {
                logger.info("Invalid answer! Please enter just [yes/no]");
                continue;
            }

            if ("yes".equalsIgnoreCase(userAnswer)) {
                logger.info("You can read the game rules below\n");
                break;
            }

            if ("no".equalsIgnoreCase(userAnswer)) {
                isWannaPlay = false;
                break;
            }

        }

        if (isWannaPlay) {
            String classes =
                    Arrays.stream(PlayerClass.values())
                            .filter(cl -> !UNKNOWN.equals(cl))
                            .map(PlayerClass::name).collect(Collectors.joining(","));

            logger.info("You need to enter your nickname and your class [{}]: ", classes);

            logger.info("First player: Enter your name!");
            String firstPlayerName = firstScanner.nextLine();
            Player playerOne = new Player();

            while (firstPlayerName.isBlank() || firstPlayerName.length() <= 2) {
                logger.info("Invalid name! Name could not be empty or less than 3 chars");
                firstPlayerName = firstScanner.nextLine();
            }

            playerOne.setName(firstPlayerName);
            logger.info("Good! Now you need to choose your class: [{}]", classes);

            CardRepository.initCards();
            Map<PlayerClass, List<ClassCard>> cards = CardRepository.getCards();
            List<PlayerClass> classOrder = new ArrayList<>(
                    Arrays.stream(PlayerClass.values())
                            .filter(cl -> !UNKNOWN.equals(cl)).toList());


            PlayerClass selectedClass = chooseYourClass(firstScanner, classOrder, cards);

            playerOne.setPlayerClass(selectedClass);
            logger.info("Good! you can see info about your char: {}: {}",
                    playerOne.getName(), playerOne.getPlayerClass().name());

            logger.info("Second player: Enter your name!");
            String secondPlayerName = firstScanner.nextLine();

            logger.info("Hello, {} you will play versus {}\n", firstPlayerName, secondPlayerName);
        } else {
            logger.info("Ok, bye!");
        }

    }

    private static PlayerClass chooseYourClass(Scanner scanner,
                                               List<PlayerClass> classOrder,
                                               Map<PlayerClass, List<ClassCard>> cards) {
        int index = 0;

        while (true) {
            PlayerClass playerClass = classOrder.get(index);

            boolean isChosen = showClassAndAsk(scanner, playerClass, cards);
            if (isChosen) return playerClass;

            index = (index + 1) % classOrder.size();
        }

    }

    private static boolean showClassAndAsk(
            Scanner scanner,
            PlayerClass playerClass,
            Map<PlayerClass, List<ClassCard>> cards
    ) {
        List<ClassCard> classCards = cards.get(playerClass);
        String cardsString = classCards.stream()
                .map(ClassCard::stringInfo)
                .collect(Collectors.joining());

        switch (playerClass) {
            case WARRIOR -> System.out.println("""
                    \n
                    ====================================
                    Class: Warrior
                    ------------------------------------
                    Health : 20
                    Defense: 7
                    Damage : 4
                    ------------------------------------
                    Description:
                    A frontline fighter who relies on strength and heavy armor.
                    Excels in close combat and can withstand large amounts of damage.
                    ------------------------------------
                    Cards:
                    """ + cardsString +
                    "====================================");

            case ARCHER -> System.out.println("""
                     \n
                    ====================================
                    Class: Archer
                    ------------------------------------
                    Health : 15
                    Defense: 4
                    Damage : 6
                    ------------------------------------
                    Description:
                    A ranged attacker specializing in precision and speed.
                    Deals high damage from a distance but is vulnerable in close combat.
                    ------------------------------------
                    Cards:
                    """ + cardsString +
                    "====================================");

            case MAGE -> System.out.println("""
                     \n
                    ====================================
                    Class: Mage
                    ------------------------------------
                    Health : 10
                    Defense: 3
                    Damage : 10
                    ------------------------------------
                    Description:
                    A master of magic who uses spells to destroy enemies.
                    Extremely powerful but fragile, requires careful positioning.
                    ------------------------------------
                    Cards:
                    """ + cardsString +
                    "====================================");
        }

        System.out.print("Do you want to play as this class? [yes/no]:\n ");
        return "yes".equalsIgnoreCase(scanner.nextLine());
    }
}
