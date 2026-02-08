package ru.wlfkdsh.northcott.core.impl;

import org.junit.jupiter.api.Test;
import ru.wlfkdsh.northcott.core.api.BoardSnapshot;
import ru.wlfkdsh.northcott.core.api.Player;

import static org.junit.jupiter.api.Assertions.*;

class DefaultNorthcottGameMakeMoveValidationTest {

    @Test
    void makeMoveRejectsIllegalMovesAndDoesNotChangeState() {
        DefaultNorthcottGame game = new DefaultNorthcottGame(1, 5);
        BoardSnapshot before = game.snapshot();

        assertEquals(Player.WHITE, game.currentPlayer());
        assertFalse(game.isGameOver());

        assertFalse(game.makeMove(-1, 2));
        assertFalse(game.makeMove(1, 2));

        assertFalse(game.makeMove(0, -1));
        assertFalse(game.makeMove(0, 0));   // назад / не между фишками
        assertFalse(game.makeMove(0, 1));   // в ту же клетку
        assertFalse(game.makeMove(0, 4));   // занято BLACK
        assertFalse(game.makeMove(0, 99));  // вне доски

        BoardSnapshot after = game.snapshot();

        assertEquals(before.whiteCol(0), after.whiteCol(0), "WHITE position must not change");
        assertEquals(before.blackCol(0), after.blackCol(0), "BLACK position must not change");
        assertEquals(Player.WHITE, game.currentPlayer(), "Current player must not change");
        assertFalse(game.isGameOver());
        assertNull(game.winner());
    }

    @Test
    void makeMoveReturnsFalseAfterGameOver() {
        DefaultNorthcottGame game = new DefaultNorthcottGame(1, 5);

        assertTrue(game.makeMove(0, 3));
        assertTrue(game.isGameOver());
        assertEquals(Player.WHITE, game.winner());

        assertFalse(game.makeMove(0, 2));
        assertFalse(game.makeMove(0, 1));
    }
}
