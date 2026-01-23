package ru.mishelby.game5;

public class GameCard {
    private String name;
    private String description;

    public GameCard(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    protected String getCardInfo(){
        return name + ": " + description;
    }
}
