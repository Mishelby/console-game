package ru.mishelby.game2;

import java.util.Random;
import java.util.Scanner;

/**
 * Основной класс игры, управляющий процессом регистрации игроков, выполнением бросков и определением победителя.
 * Класс содержит логику для:
 * - Регистрации игроков с проверкой уникальности и корректности имён
 * - Выполнения бросков для каждого игрока
 * - Подсчёта очков и определения победителя по правилам игры
 *
 * Игра предполагает:
 * - Максимум 4 игрока
 * - Каждый игрок делает 5 бросков
 * - Броски генерируются случайным образом от 0 до 9 включительно
 * - Победитель определяется по сумме бросков, при равенстве - по последнему броску
 */
public class Game2 {
    /**
     * Константа максимального количества игроков в игре
     */
    private static final int MAX_PLAYERS = 4;

    /**
     * Константа максимального значения для броска
     */
    private static final int MAX_NUMBER = 10;

    /**
     * Генератор случайных чисел для бросков
     */
    private static final Random RANDOM = new Random();

    /**
     * Массив для хранения зарегистрированных игроков
     */
    private static final Player[] PLAYERS = new Player[MAX_PLAYERS];

    /**
     * Индекс для отслеживания текущего количества зарегистрированных игроков
     */
    private static int currentIndex;

    /**
     * Точка входа в программу. Запускает процесс игры:
     * 1. Приветствует игроков
     * 2. Регистрирует игроков
     * 3. Выполняет броски для всех игроков
     * 4. Выводит очки игроков
     * 5. Определяет и выводит победителя
     *
     * @param args аргументы командной строки (не используются)
     */
    public static void main(String[] args) {
        System.out.println("================================================================");
        System.out.println("Hello, let's play a game!\n");
        var scanner = new Scanner(System.in);

        registrationPlayers(scanner);

        for (Player player : PLAYERS) {
            for (var i = 0; i < Player.MAX_THROWS; i++) {
                player.doThrow(RANDOM.nextInt(MAX_NUMBER));
            }
        }

        System.out.println("Players scores: ");

        for (Player player : PLAYERS) {
            System.out.println(player.getName() + ": " + player.getScore());
        }

        /**
         * Здесь сразу указываем имя победителя и его очки
         * Это нужно для дальнейшего сравнения с другими игроками
         * Если есть игроки, у которых больше очков, то мы перезаписываем имя победителя и его очки
         * Если очки равны, то мы сравниваем последний бросок игрока с последним броском другого игрока
         */
        int maxNumber = PLAYERS[0].getScore();
        String winnerName = PLAYERS[0].getName();

        System.out.println("\nChoosing the winner...\n");
        for (int i = 0; i < PLAYERS.length; i++) {
            System.out.printf("Current player name with max score: %s, current max number%d%n"
                    .formatted(winnerName, maxNumber));
            System.out.println();
            for (int j = i + 1; j < PLAYERS.length; j++) {
                // Если очки игрока больше, то мы перезаписываем имя победителя и его очки
                if (maxNumber < PLAYERS[j].getScore()) {
                    maxNumber = PLAYERS[j].getScore();
                    winnerName = PLAYERS[j].getName();
                    System.out.println("|-----------------------------------------------|");
                    System.out.println("New max number: " + maxNumber + " by " + winnerName);
                    System.out.println("|-----------------------------------------------|\n");
                    break;
                }

                if (maxNumber == PLAYERS[j].getScore() && !winnerName.equals(PLAYERS[j].getName())) {
                    int lastThrowPlayerI = PLAYERS[i].getPlayerThrows()[MAX_PLAYERS];
                    int lastThrowPlayerJ = PLAYERS[j].getPlayerThrows()[MAX_PLAYERS];

                    System.out.printf("Equals scores: First player %s, sore: %d || second player %s, score: %d%n"
                            .formatted(winnerName, maxNumber, PLAYERS[j].getName(), PLAYERS[j].getScore()));
                    System.out.println("|-----------------------------------------------|\n");

                    if (lastThrowPlayerI > lastThrowPlayerJ) {
                        System.out.printf("Last throw: First player %s, sore: %d || second player %s, score: %d%n"
                                .formatted(winnerName, lastThrowPlayerI, PLAYERS[j].getName(), lastThrowPlayerJ));

                        maxNumber = PLAYERS[i].getScore();
                        winnerName = PLAYERS[i].getName();
                    } else {
                        System.out.printf("Last throw: First player %s, sore: %d || second player %s, score: %d%n"
                                .formatted(winnerName, lastThrowPlayerI, PLAYERS[j].getName(), lastThrowPlayerJ));

                        maxNumber = PLAYERS[j].getScore();
                        winnerName = PLAYERS[j].getName();
                    }
                }
            }
        }

        System.out.println("----------------------------------------------------");
        System.out.println("Winner: " + winnerName + " with score: " + maxNumber);
        System.out.println("----------------------------------------------------");
    }

    /**
     * Регистрирует игроков в игре, запрашивая их имена и создавая объекты Player.
     * Метод проверяет, что каждое имя является допустимым, прежде чем продолжить.
     * После регистрации выводит приветствие каждому игроку.
     *
     * @param scanner объект Scanner для чтения ввода с консоли
     */
    public static void registrationPlayers(Scanner scanner) {
        for (int i = 0; i < MAX_PLAYERS; i++) {
            String validName = isValidName(i + 1, scanner);
            var player = new Player(validName);

            PLAYERS[currentIndex] = player;
            incrementIndex();
        }

        for (Player player : PLAYERS) {
            System.out.printf("Hello: %s %n<------>%n %n"
                    .formatted(player.getName()));
        }
    }

    /**
     * Проверяет корректность имени игрока и уникальность имени среди уже зарегистрированных игроков.
     * Запрашивает ввод имени до тех пор, пока не будет введено корректное имя.
     *
     * @param playerNumber номер игрока (1-4)
     * @param scanner объект Scanner для чтения ввода с консоли
     * @return корректное имя игрока
     */
    private static String isValidName(int playerNumber, Scanner scanner) {
        var isValid = false;

        System.out.println("Player " + playerNumber + ", enter your name: ");
        String playerName = scanner.nextLine();

        while (!isValid) {
            if (playerName.isBlank() || (playerName.length() < 2 || playerName.length() > 10)) {
                System.out.println("Invalid name, please enter a name between 2 and 10 characters: ");
                System.out.println("================================================================\n");
                playerName = scanner.nextLine();
            }

            if (isNameAvailable(playerName)) {
                isValid = true;
            } else {
                System.out.println("Name is not available, please enter a different name: ");
                playerName = scanner.nextLine();
            }
        }

        System.out.println("================================================================\n");
        return playerName;
    }

    /**
     * Проверяет, доступно ли указанное имя для использования.
     * Имя считается недоступным, если оно уже используется одним из зарегистрированных игроков.
     *
     * @param name имя для проверки
     * @return true, если имя доступно, false - если уже занято
     */
    private static boolean isNameAvailable(String name) {
        // Пропускаем первого игрока, т.к его имя точно будет уникальным
        if (currentIndex < 1) return true;

        var isAvailable = true;

        // Проходим циклом по текущему количеству зарегистрированных игроков и сравниваем имя с каждым из них,
        // если имя совпадает, то игрок с таким именем уже зарегистрирован
        for (int i = 0; i < currentIndex; i++) {
            if (PLAYERS[i].getName().equals(name)) {
                isAvailable = false;
                break;
            }
        }

        return isAvailable;
    }

    /**
     * Увеличивает счётчик зарегистрированных игроков на 1.
     */
    public static void incrementIndex() {
        currentIndex++;
    }
}
