package ru.mishelby.repository;

import lombok.Getter;
import lombok.Setter;
import ru.mishelby.game.enums.ClassCard;
import ru.mishelby.game.enums.PlayerClass;


import java.util.List;
import java.util.Map;

import static ru.mishelby.game.enums.PlayerClass.*;

public final class CardRepository {

    @Getter
    @Setter
    private static Map<PlayerClass, List<ClassCard>> cards = initCards();

    private CardRepository(){}

    public static Map<PlayerClass, List<ClassCard>> initCards() {
        List<ClassCard> warriorCards = List.of(
                new ClassCard("WarriorCard1", "WarriorCard1Description1"),
                new ClassCard("WarriorCard2", "WarriorCard1Description2")
        );
        List<ClassCard> archerCards = List.of(new ClassCard("Card2", "Description2"));
        List<ClassCard> mageCards = List.of(new ClassCard("Card3", "Description3"));

        cards = Map.of(WARRIOR, warriorCards, ARCHER, archerCards, MAGE, mageCards);
        return cards;
    }
}
