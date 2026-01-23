package ru.mishelby.game5;

public class CardRepository {
    private static final GameCard[] cards = {
            new GameCard("Heal", "Regeneration 2 hp for player"),
            new GameCard("Shield", "Player doesn't take damage"),
    };

    public GameCard[] getCards() {
        return cards;
    }

}
