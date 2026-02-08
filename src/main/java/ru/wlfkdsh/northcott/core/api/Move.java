// src/main/java/ru/wlfkdsh/northcott/core/api/Move.java
package ru.wlfkdsh.northcott.core.api;

public record Move(Player player, int row, int fromCol, int toCol) {
    public Move {
        if (player == null) throw new IllegalArgumentException("player is null");
        if (row < 0) throw new IllegalArgumentException("row must be >= 0");
        if (fromCol < 0 || toCol < 0) throw new IllegalArgumentException("col must be >= 0");
    }
}
