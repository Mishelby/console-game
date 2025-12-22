package ru.mishelby.game;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.mishelby.game.enums.PlayerClass;

@Getter
@Setter
@NoArgsConstructor
public class Player {
    private String name;
    private int level;
    private PlayerClass playerClass;

    public Player(String name){
        this.name = name;
        this.level = 1;
    }
}
