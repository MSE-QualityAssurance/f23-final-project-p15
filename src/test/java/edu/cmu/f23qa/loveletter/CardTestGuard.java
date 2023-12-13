package edu.cmu.f23qa.loveletter;


import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class CardTestGuard {

    // Correct Guess with Guard Card
    @Test
    void testGuardCorrectGuess() {
        Hand userHand = new Hand(new ArrayList<>(Arrays.asList(Card.GUARD)));
        Hand opponentHand = new Hand(new ArrayList<>(Arrays.asList(Card.PRIEST))); 
        DiscardPile userDiscardPile = Mockito.mock(DiscardPile.class);
        DiscardPile opponentDiscardPile = Mockito.mock(DiscardPile.class);
        Player user = new Player("User", userHand, userDiscardPile);
        Player opponent = new Player("Opponent", opponentHand, opponentDiscardPile);
        Deck deck = Mockito.mock(Deck.class);

        GameActions gameActions = new GameActions() {};
        gameActions.useGuard(2, opponent, user, deck); 

        Assertions.assertFalse(opponent.isAlive(), "Opponent should be eliminated for having a Priest");
    }

    // Incorrect Guess with Guard Card
    @Test
    void testGuardIncorrectGuess() {
        Hand userHand = new Hand(new ArrayList<>(Arrays.asList(Card.GUARD)));
        Hand opponentHand = new Hand(new ArrayList<>(Arrays.asList(Card.PRIEST))); 
        DiscardPile userDiscardPile = Mockito.mock(DiscardPile.class);
        DiscardPile opponentDiscardPile = Mockito.mock(DiscardPile.class);
        Player user = new Player("User", userHand, userDiscardPile);
        Player opponent = new Player("Opponent", opponentHand, opponentDiscardPile);
        Deck deck = Mockito.mock(Deck.class);

        GameActions gameActions = new GameActions() {};
        gameActions.useGuard(6, opponent, user, deck); 

        Assertions.assertTrue(opponent.isAlive(), "Opponent should not be eliminated for having a Priest");
    }

}