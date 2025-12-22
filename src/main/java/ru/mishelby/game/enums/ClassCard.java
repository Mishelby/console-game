package ru.mishelby.game.enums;


public record ClassCard(
        String cardName,
        String cardDescription
) {

    public String stringInfo() {
        return """
           • %s
             └─ %s
           """.formatted(cardName, cardDescription);
    }
}
