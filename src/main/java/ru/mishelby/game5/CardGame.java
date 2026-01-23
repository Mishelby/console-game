package ru.mishelby.game5;

import java.util.Arrays;
import java.util.Scanner;

public class CardGame {
    private static final Scanner scanner = new Scanner(System.in);
    private static final String[] PLAYER_NAMES = new String[2];
    private static final CardRepository cardRepository = new CardRepository();
    private static final int MAX_HP = 20;

    private static int currentIndex = 0;
    private static boolean isGameStarted = true;
    private static boolean isAppRunning = true;
    private static int currentMove = 1;

    public static void main(String[] args) {

        while (isAppRunning) {
            Player playerOne = new Player();
            String validNameOne = getValidName(scanner);
            int validHpOne = getValidHp(scanner);
            registrationPlayer(playerOne, validNameOne, validHpOne);

            System.out.println();

            Player playerTwo = new Player();
            String validNameTwo = getValidName(scanner);
            int validHpTwo = getValidHp(scanner);
            registrationPlayer(playerTwo, validNameTwo, validHpTwo);

            System.out.println();

            startGame(playerOne, playerTwo, scanner);

            System.out.println("Do you want to play again? (y/n)");
            String userAnswer = scanner.nextLine();

            while (!userAnswer.equalsIgnoreCase("y") && !userAnswer.equalsIgnoreCase("n")) {
                System.out.println("Invalid answer!");
                userAnswer = scanner.nextLine();
            }

            if (userAnswer.equalsIgnoreCase("y")) {
                setNewGameParameters();
                isAppRunning = true;
                isGameStarted = true;
            }else{
                isAppRunning = false;
            }

        }
    }

    private static void setNewGameParameters() {
        Arrays.fill(PLAYER_NAMES, null);
        currentIndex = 0;
        currentMove = 1;
    }

    public static void startGame(Player playerOne, Player playerTwo, Scanner scanner) {
        System.out.println("Game started!");

        while (isGameStarted) {
            if (playerOne.getHp() == 0 || playerTwo.getHp() == 0) {
                System.out.println("Game over!");
                isGameStarted = false;
                break;
            }
            System.out.println("Current move: " + currentMove);
            System.out.println();

            if (currentMove % 3 == 0) {
                System.out.println("Choose your card: ");
                for (GameCard card : cardRepository.getCards()) {
                    System.out.println(card.getCardInfo());
                }

                setCard(playerOne, scanner);
                setCard(playerTwo, scanner);

                getPlayerNumber(playerOne, scanner);
                System.out.println();
                getPlayerNumber(playerTwo, scanner);
                makeMoveWithCards(playerOne, playerTwo);
            } else {
                getPlayerNumber(playerOne, scanner);
                System.out.println();
                getPlayerNumber(playerTwo, scanner);

                makeMoveWithoutCards(playerOne, playerTwo);
            }

        }
    }

    private static void makeMoveWithCards(Player playerOne, Player playerTwo) {
        if (playerOne.getCurrentNumber() == playerTwo.getCurrentNumber()) {
            System.out.println();
            System.out.println("You have chosen the same numbers! Both players lost 1 hp");

            checkCard(playerOne, playerTwo, 1);

            System.out.println(playerOne.getName() + " hp: " + playerOne.getHp());
            System.out.println(playerTwo.getName() + " hp: " + playerTwo.getHp());
            System.out.println();
        } else if (playerOne.getCurrentNumber() > playerTwo.getCurrentNumber()) {
            int damage = getDamage(playerOne, playerTwo);
            System.out.println(playerOne.getName() + " won! You lost " + damage + " hp");

            checkCard(playerOne, playerTwo, damage);

            System.out.println(playerTwo.getName() + " hp: " + playerTwo.getHp());
            System.out.println();
        } else {
            int damage = getDamage(playerTwo, playerOne);
            System.out.println(playerTwo.getName() + " won! You lost " + damage + " hp");

            checkCard(playerOne, playerTwo, damage);

            System.out.println(playerOne.getName() + " hp: " + playerOne.getHp());
            System.out.println();
        }
        currentMove++;
    }

    private static void makeMoveWithoutCards(Player playerOne, Player playerTwo) {
        if (playerOne.getCurrentNumber() == playerTwo.getCurrentNumber()) {
            System.out.println();
            System.out.println("You have chosen the same numbers! Both players lost 1 hp");

            playerOne.setHp(playerOne.getHp() - 1);
            playerTwo.setHp(playerTwo.getHp() - 1);

            System.out.println(playerOne.getName() + " hp: " + playerOne.getHp());
            System.out.println(playerTwo.getName() + " hp: " + playerTwo.getHp());
            System.out.println();
        } else if (playerOne.getCurrentNumber() > playerTwo.getCurrentNumber()) {
            int damage = getDamage(playerOne, playerTwo);
            System.out.println(playerOne.getName() + " won! You lost " + damage + " hp");

            playerTwo.setHp(playerTwo.getHp() - damage);

            System.out.println(playerTwo.getName() + " hp: " + playerTwo.getHp());
            System.out.println();
        } else {
            int damage = getDamage(playerTwo, playerOne);
            System.out.println(playerTwo.getName() + " won! You lost " + damage + " hp");

            playerOne.setHp(playerOne.getHp() - damage);

            System.out.println(playerOne.getName() + " hp: " + playerOne.getHp());
            System.out.println();
        }
        currentMove++;
    }

    private static int getDamage(Player playerOne, Player playerTwo) {
        return playerOne.getCurrentNumber() - playerTwo.getCurrentNumber();
    }

    private static void checkCard(Player playerOne, Player playerTwo, int damage) {
        if (isPlayersHaveCards(playerOne, playerTwo)) {
            if ("Heal".equalsIgnoreCase(playerOne.getCardName()) && "Heal".equalsIgnoreCase(playerTwo.getCardName())) {
                System.out.println("Both players are using Heal!");
                playerOne.setHp(Math.min(playerOne.getHp() + 2, MAX_HP));
                playerTwo.setHp(Math.min(playerTwo.getHp() + 2, MAX_HP));
            } else if ("Heal".equalsIgnoreCase(playerOne.getCardName())) {
                playerOne.setHp(playerOne.getHp() + 2);
            } else if ("Heal".equalsIgnoreCase(playerTwo.getCardName())) {
                playerTwo.setHp(playerTwo.getHp() + 2);
            }

            boolean playerOneShield = "Shield".equalsIgnoreCase(playerOne.getCardName());
            boolean playerTwoShield = "Shield".equalsIgnoreCase(playerTwo.getCardName());

            if (playerOneShield && playerTwoShield) {
                System.out.println("Both players are protected by Shield! No damage taken");
            } else if (playerOneShield) {
                System.out.println(playerOne.getName() + " is protected by Shield!");
                playerTwo.setHp(playerTwo.getHp() - damage);
            } else if (playerTwoShield) {
                System.out.println(playerTwo.getName() + " is protected by Shield!");
                playerOne.setHp(playerOne.getHp() - damage);
            } else {
                playerTwo.setHp(playerTwo.getHp() - damage);
                playerOne.setHp(playerOne.getHp() - damage);
            }
        }
    }

    private static boolean isPlayersHaveCards(Player playerOne, Player playerTwo) {
        return playerOne.getCardName() != null && playerTwo.getCardName() != null;
    }

    private static void setCard(Player player, Scanner scanner) {
        System.out.println(player.getName() + " choose your card: ");
        while (true) {
            String cardName = scanner.nextLine();
            boolean isValidCard = false;

            for (GameCard card : cardRepository.getCards()) {
                if (card.getName().equalsIgnoreCase(cardName)) {
                    player.setCardName(cardName);
                    System.out.println(player.getName() + " your card is: " + cardName);
                    isValidCard = true;
                    break;
                }
            }

            if (isValidCard) {
                break;
            } else {
                System.out.println("Invalid card! Try again:");
            }
        }
    }

    private static void getPlayerNumber(Player player, Scanner scanner) {
        System.out.print(player.getName() + " enter your number[from 1 to 6]: ");
        int number = scanner.nextInt();
        scanner.nextLine();

        if (number < 1 || number > 6) {
            while (number < 1 || number > 6) {
                System.out.print("Invalid number!");
                number = scanner.nextInt();
                scanner.nextLine();
            }
        }

        player.setCurrentNumber(number);
        System.out.println(player.getName() + " your number is: " + number);
        System.out.println();
    }

    public static boolean isNameAlreadyUse(String name, int currentPlayerIndex) {
        if (currentPlayerIndex > 0) {
            for (int i = 0; i < currentIndex; i++) {
                if (PLAYER_NAMES[i].equals(name)) return true;
            }
        }

        return false;
    }

    public static void registrationPlayer(Player player, String name, int hp) {
        player.setName(name);
        player.setHp(hp);
    }

    public static int getValidHp(Scanner scanner) {
        System.out.print("Enter your hp: ");
        int hp = scanner.nextInt();
        scanner.nextLine();
        while (true) {
            if (hp < 10 || hp > 20) {
                System.out.println("Invalid hp! Choose it between 10 and 20");
                hp = scanner.nextInt();
                scanner.nextLine();
            } else {
                return hp;
            }
        }
    }

    public static String getValidName(Scanner scanner) {
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();

        while (true) {
            if (name.isBlank() || name.length() < 3) {
                System.out.println("Invalid name! Try again");
                name = scanner.nextLine();
            } else if (isNameAlreadyUse(name, currentIndex)) {
                System.out.println("Name already use! Choose another name: ");
                name = scanner.nextLine();
            } else {
                PLAYER_NAMES[currentIndex++] = name;
                return name;
            }
        }
    }
}
