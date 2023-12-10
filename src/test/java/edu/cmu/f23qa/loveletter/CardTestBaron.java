package edu.cmu.f23qa.loveletter;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class CardTestBaron {
    /*
     * After drawing, I discard the Baron and choose  [normal player]. 
     * My card is a Priest, and  [normal player] is a Guard. 
     * Since my card has a higher value, the [normal player] is knocked out of the round.
     */
    @Test
    void testBaronUserWins() {
        Hand userHand = new Hand(new ArrayList<>(Arrays.asList(Card.PRIEST)));
        Hand opponentHand = new Hand(new ArrayList<>(Arrays.asList(Card.GUARD)));

        Player user = Mockito.mock(Player.class);
        Player opponent = Mockito.mock(Player.class);

        Mockito.when(user.getHand()).thenReturn(userHand);
        Mockito.when(opponent.getHand()).thenReturn(opponentHand);

        GameActions gameActions = new GameActions() {};
        gameActions.useBaron(user, opponent);

        Assertions.assertFalse(opponent.isAlive(), "Opponent should be eliminated");
    }

    @Test
    void testBaronOpponentWins(){
        Hand userHand = new Hand(new ArrayList<>(Arrays.asList(Card.PRIEST)));
        Hand opponentHand = new Hand(new ArrayList<>(Arrays.asList(Card.KING)));

        Player user = Mockito.mock(Player.class);
        Player opponent = Mockito.mock(Player.class);

        Mockito.when(user.getHand()).thenReturn(userHand);
        Mockito.when(opponent.getHand()).thenReturn(opponentHand);

        GameActions gameActions = new GameActions() {};
        gameActions.useBaron(user, opponent);

        Assertions.assertFalse(user.isAlive(), "User should be eliminated");
    }

    @Test
    void testBaronTiesMock(){
        Hand userHand = new Hand(new ArrayList<>(Arrays.asList(Card.HANDMAIDEN)));
        Hand opponentHand = new Hand(new ArrayList<>(Arrays.asList(Card.HANDMAIDEN)));

        Player user = Mockito.mock(Player.class);
        Player opponent = Mockito.mock(Player.class);

        Mockito.when(user.getHand()).thenReturn(userHand);
        Mockito.when(opponent.getHand()).thenReturn(opponentHand);

        GameActions gameActions = new GameActions() {};
        gameActions.useBaron(user, opponent);

        Assertions.assertFalse(opponent.isAlive() && user.isAlive(), "Nothing happens");
    }
}
