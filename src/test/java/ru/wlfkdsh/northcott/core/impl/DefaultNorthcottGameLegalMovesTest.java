package ru.wlfkdsh.northcott.core.impl;

import org.junit.jupiter.api.Test;
import ru.wlfkdsh.northcott.core.api.Player;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DefaultNorthcottGameLegalMovesTest {

    @Test
    void legalDestinationsHaveExpectedOrderForWhiteAndBlack() {
        DefaultNorthcottGame game = new DefaultNorthcottGame(1, 5);

        assertEquals(Player.WHITE, game.currentPlayer());
        assertEquals(List.of(2, 3), game.legalDestinations(0), "WHITE must move right (ascending)");

        assertTrue(game.makeMove(0, 2), "WHITE move must be accepted");
        assertEquals(Player.BLACK, game.currentPlayer());

        assertEquals(List.of(3), game.legalDestinations(0), "BLACK must move left (descending)");
    }

    @Test
    void illegalRowReturnsEmptyList() {
        DefaultNorthcottGame game = new DefaultNorthcottGame(1, 5);

        assertTrue(game.legalDestinations(-1).isEmpty());
        assertTrue(game.legalDestinations(1).isEmpty());
        assertTrue(game.legalDestinations(999).isEmpty());
    }
}
