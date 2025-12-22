package ru.mishelby.game2;

import lombok.Data;

@Data
public class Player {
    protected static final int MAX_THROWS = 5;

    private String name;
    private int[] playerThrows;
    private int currentIndex;

    public Player(String name) {
        this.name = name;
        this.playerThrows = new int[MAX_THROWS];
        this.currentIndex = 0;
    }

    public void doThrow(int number) {
        if (currentIndex < MAX_THROWS) {
            playerThrows[currentIndex] = number;
            incrementIndex();
        }
    }

    public int getScore() {
        var sum = 0;
        for (int playerThrow : this.getPlayerThrows()) sum += playerThrow;
        return sum;
    }

    public void incrementIndex() {
        currentIndex++;
    }
}
