package edu.cmu.f23qa.loveletter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HandTest {

    private Hand hand;

    @BeforeEach
    public void setUp() {
        hand = new Hand();
    }

    @Test
    public void testRoyaltyPosWithNoRoyalCards() {
        hand.add(Card.GUARD); // Assuming Card.GUARD is not a royal card
        assertEquals(-1, hand.royaltyPos(), "Should return -1 when no royal cards are present");
    }

    @Test
    public void testRoyaltyPosWithOneRoyalCard() {
        hand.add(Card.KING); // Assuming Card.KING is a royal card
        assertEquals(0, hand.royaltyPos(), "Should return the index of the royal card");
    }

    @Test
    public void testRoyaltyPosWithMultipleCardsIncludingRoyal() {
        hand.add(Card.GUARD);
        hand.add(Card.PRINCE); // Assuming Card.PRINCE is a royal card
        hand.add(Card.GUARD);
        assertEquals(1, hand.royaltyPos(), "Should return the index of the first royal card");
    }
}
