package ru.wlfkdsh.northcott.core;

public enum Player {
    LEFT,
    RIGHT;

    public Player opponent() {
        return this == LEFT ? RIGHT : LEFT;
    }
}
