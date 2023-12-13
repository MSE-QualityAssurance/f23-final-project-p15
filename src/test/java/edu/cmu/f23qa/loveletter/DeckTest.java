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

    @Test
    public void testBuildForStandardGame() {
        deck.build(4); // Standard game with 4 players
        assertEquals(16, deck.getDeckSize()); // Assuming a size method that returns the number of cards in the deck
    }

    @Test
    public void testBuildForPremiumGame() {
        deck.build(5); // Premium game with 5 players
        assertEquals(32, deck.getDeckSize()); // Standard + additional cards
    }

    @Test
    public void testRemoveCardsForTwoPlayers() {
        deck.build(2);
        deck.removeCards(2);
        assertEquals(12, deck.getDeckSize()); 
    }

    @Test
    public void testRemoveCardsForMoreThanTwoPlayers() {
        deck.build(3);
        deck.removeCards(3);
        assertEquals(15, deck.getDeckSize()); 
    }

    @Test
    public void testHasMoreCardsWithExtraCard() {
        deck.build(2);
        deck.removeCards(2);

        while (deck.hasMoreCards()) {
            deck.draw();
        }

        assertTrue(deck.hasMoreCardsWithExtraCard()); // Should return true as the removed card is added back
        deck.draw(); // Draw the added back card
        assertFalse(deck.hasMoreCardsWithExtraCard()); // No more cards
    }
}
