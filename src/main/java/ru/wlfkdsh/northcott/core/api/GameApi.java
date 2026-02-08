// src/main/java/ru/wlfkdsh/northcott/core/api/GameApi.java
package ru.wlfkdsh.northcott.core.api;

import java.util.List;
import java.util.Optional;

public interface GameApi {
    int rows();
    int cols();

    Player currentPlayer();
    BoardSnapshot snapshot();

    boolean isGameOver();
    Player winner(); // null если игра не завершена

    int nimSum(); // XOR по "промежуткам" между фишками

    List<Integer> legalDestinations(int row); // для текущего игрока

    boolean makeMove(int row, int toCol); // ход текущего игрока

    Optional<Move> bestMove(); // оптимальный ход для текущего игрока (если есть)

    void reset();
}
