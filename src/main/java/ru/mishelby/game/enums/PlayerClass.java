package ru.mishelby.game.enums;

public enum PlayerClass {
    WARRIOR,
    ARCHER,
    UNKNOWN,
    MAGE;

    public static PlayerClass fromString(String value){
        for (PlayerClass cl : PlayerClass.values()) {
            if(cl.name().equalsIgnoreCase(value)){
                return cl;
            }
        }

        return UNKNOWN;
    }
}
