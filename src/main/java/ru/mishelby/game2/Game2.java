package ru.mishelby.game2;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

/**
 * Основной класс игры, управляющий процессом регистрации игроков, выполнением бросков и определением победителя.
 * Класс содержит логику для:
 * - Регистрации игроков с проверкой уникальности и корректности имён
 * - Выполнения бросков для каждого игрока
 * - Подсчёта очков и определения победителя по правилам игры
 * <p>
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
    private static final int MAX_NUMBER = 3;

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
                player.doThrow(RANDOM.nextInt(1, MAX_NUMBER));
            }
        }

        System.out.println("Players scores: ");

        for (Player player : PLAYERS) {
            System.out.println(
                    player.getName() + " " + Arrays.toString(player.getPlayerThrows()) + ": " + player.getScore()
            );
        }

        /**
         * Определяет победителя игры по следующим правилам:
         * 1. Победитель определяется по максимальному количеству очков
         * 2. Если у нескольких игроков одинаковое количество очков, то:
         *    - Сравниваются последние броски
         *    - Побеждает игрок с большим последним броском
         *    - Если и последние броски равны, то это ничья
         *
         * Алгоритм:
         * 1. Находит игрока с максимальным счетом
         * 2. Проверяет, есть ли другие игроки с таким же счетом (ничья)
         * 3. При ничьей сравнивает последние броски всех игроков с максимальным счетом
         * 4. Выводит имя победителя и его счет
         */

        var maxPlayerIndex = 0;
        var secondPlayerIndex = 0;

        int maxNumber = PLAYERS[0].getScore();
        var isDraw = false;

        System.out.println("\nChoosing the winner...\n");

        for (int i = 1; i < PLAYERS.length; i++) {
            if (maxNumber < PLAYERS[i].getScore()) {
                maxNumber = PLAYERS[i].getScore();
                maxPlayerIndex = i;
            }
        }

        for (int i = 1; i < PLAYERS.length; i++) {
            if (maxNumber == PLAYERS[i].getScore() && i != maxPlayerIndex) {
                isDraw = true;
                secondPlayerIndex = i;
                break;
            }
        }

        if (isDraw) {
            System.out.println("It's a draw! Comparing last throws...");
            int lastPlayerThrowOne = PLAYERS[maxPlayerIndex].getPlayerThrows()[Player.MAX_THROWS - 1];
            int lastPlayerThrowTwo = PLAYERS[secondPlayerIndex].getPlayerThrows()[Player.MAX_THROWS - 1];
            System.out.println("Last throws: " + lastPlayerThrowOne + " " + lastPlayerThrowTwo);

            if (lastPlayerThrowOne == lastPlayerThrowTwo) {
                System.out.println("It's a draw!");
            } else if (lastPlayerThrowOne > lastPlayerThrowTwo) {
                System.out.println("----------------------------------------------------");
                System.out.println("Winner: " + PLAYERS[maxPlayerIndex].getName() +
                        " with score: " + PLAYERS[maxPlayerIndex].getScore());
                System.out.println("----------------------------------------------------");
            } else {
                System.out.println("----------------------------------------------------");
                System.out.println("Winner: " + PLAYERS[secondPlayerIndex].getName() +
                        " with score: " + PLAYERS[secondPlayerIndex].getScore());
                System.out.println("----------------------------------------------------");
            }
        }
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
     * @param scanner      объект Scanner для чтения ввода с консоли
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
