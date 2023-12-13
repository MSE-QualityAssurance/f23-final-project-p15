package edu.cmu.f23qa.loveletter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DiscardPileTest {

    private DiscardPile discardPile;

    @BeforeEach
    public void setUp() {
        discardPile = new DiscardPile(); // Initialize a new DiscardPile before each test
    }

    // Test to check the total value of an empty discard pile
    @Test
    public void testValueWithNoCards() {
        assertEquals(0, discardPile.value(), "The value should be 0 when no cards are in the pile.");
    }

    // Test to determine the total value of a discard pile with one card
    @Test
    public void testValueWithOneCard() {
        discardPile.add(Card.PRINCE); // Add a card (PRINCE) to the discard pile
        assertEquals(Card.PRINCE.value(), discardPile.value(), "The value should equal the value of the single card in the pile.");
    }

    // Test to calculate the total value of a discard pile with multiple cards
    @Test
    public void testValueWithMultipleCards() {
        discardPile.add(Card.PRINCE);
        discardPile.add(Card.KING); // Add multiple cards to the discard pile
        int expectedValue = Card.PRINCE.value() + Card.KING.value();
        assertEquals(expectedValue, discardPile.value(), "The value should be the sum of the values of all cards in the pile.");
    }
}
