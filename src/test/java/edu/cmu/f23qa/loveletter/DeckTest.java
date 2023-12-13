package edu.cmu.f23qa.loveletter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DeckTest {

    private Deck deck;

    @BeforeEach
    public void setUp() {
        deck = new Deck();
    }

    // Test to ensure the deck is correctly built for a standard game with 4 players
    @Test
    public void testBuildForStandardGame() {
        deck.build(4);
        assertEquals(16, deck.getDeckSize());
    }

    // Test to ensure the deck is correctly built for a premium game with 5 players
    @Test
    public void testBuildForPremiumGame() {
        deck.build(5);
        assertEquals(32, deck.getDeckSize());
    }

    // Test to check correct removal of cards in a 2-player game
    @Test
    public void testRemoveCardsForTwoPlayers() {
        deck.build(2);
        deck.removeCards(2);
        assertEquals(12, deck.getDeckSize());
    }

    // Test to check correct removal of cards in a game with more than 2 players
    @Test
    public void testRemoveCardsForMoreThanTwoPlayers() {
        deck.build(3);
        deck.removeCards(3);
        assertEquals(15, deck.getDeckSize());
    }

    // Test to verify if the deck correctly handles the scenario with extra card
    @Test
    public void testHasMoreCardsWithExtraCard() {
        deck.build(2);
        deck.removeCards(2);

        while (deck.hasMoreCards()) {
            deck.draw();
        }

        assertTrue(deck.hasMoreCardsWithExtraCard());
        deck.draw();
        assertFalse(deck.hasMoreCardsWithExtraCard());
    }
}
