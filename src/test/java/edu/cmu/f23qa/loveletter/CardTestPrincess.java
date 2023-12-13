package edu.cmu.f23qa.loveletter;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class CardTestPrincess {
    // User be eliminated for discarding Princess
    @Test
    void testDiscardPrincessVoluntarily() {
        Hand userHand = new Hand(new ArrayList<>(Arrays.asList(Card.PRINCESS)));
        DiscardPile discardPile = Mockito.mock(DiscardPile.class);
        Player user = new Player("user", userHand, discardPile);

        GameActions gameActions = new GameActions() {};
        gameActions.usePrincess(user);

        Assertions.assertFalse(user.isAlive(), "User should be eliminated for discarding Princess");
    }

}
