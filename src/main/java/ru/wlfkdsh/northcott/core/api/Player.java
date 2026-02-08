// src/main/java/ru/wlfkdsh/northcott/core/api/Player.java
package ru.wlfkdsh.northcott.core.api;

public enum Player {
    WHITE,
    BLACK;

    public Player opposite() {
        return this == WHITE ? BLACK : WHITE;
    }

    public String displayNameRu() {
        return this == WHITE ? "Белые" : "Чёрные";
    }
}
