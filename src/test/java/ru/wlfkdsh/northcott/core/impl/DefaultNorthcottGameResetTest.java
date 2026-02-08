package ru.wlfkdsh.northcott.core.impl;

import org.junit.jupiter.api.Test;
import ru.wlfkdsh.northcott.core.api.BoardSnapshot;
import ru.wlfkdsh.northcott.core.api.Player;

import static org.junit.jupiter.api.Assertions.*;

class DefaultNorthcottGameResetTest {

    @Test
    void default8x8HasExpectedInitialPosition() {
        DefaultNorthcottGame game = new DefaultNorthcottGame();

        assertEquals(8, game.rows());
        assertEquals(8, game.cols());
        assertEquals(Player.WHITE, game.currentPlayer());
        assertFalse(game.isGameOver());
        assertNull(game.winner());

        BoardSnapshot s = game.snapshot();
        assertEquals(8, s.rows());
        assertEquals(8, s.cols());

        for (int r = 0; r < s.rows(); r++) {
            int expectedWhite = (r % 2 == 0) ? 1 : 0;
            int expectedBlack = (r % 2 == 0) ? 7 : 6;

            assertEquals(expectedWhite, s.whiteCol(r), "white col at row=" + r);
            assertEquals(expectedBlack, s.blackCol(r), "black col at row=" + r);
            assertEquals(5, s.gap(r), "gap at row=" + r);
        }

        assertEquals(0, game.nimSum(), "default position should have nim-sum = 0");
    }

    @Test
    void snapshotIsDefensivelyCopiedFromGameState() {
        DefaultNorthcottGame game = new DefaultNorthcottGame();

        BoardSnapshot snap1 = game.snapshot();

        snap1.whiteCols()[0] = 999;
        snap1.blackCols()[0] = 999;

        BoardSnapshot snap2 = game.snapshot();
        assertNotEquals(999, snap2.whiteCol(0));
        assertNotEquals(999, snap2.blackCol(0));
    }
}
