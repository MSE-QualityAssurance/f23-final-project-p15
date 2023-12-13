package edu.cmu.f23qa.loveletter;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class GameActionTest {

    private ByteArrayOutputStream outputStream;

    private GameActions gameActions;
    private Hand mockHand1, mockHand2;
    private DiscardPile mockDiscardPile1, mockDiscardPile2;
    private Player player1, player2;
    private Reader mockReader;

    @BeforeEach
    public void setUp() {
        // Redirection output stream
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        // Create test doubles
        gameActions = new GameActions(){};
        mockHand1 = mock(Hand.class);
        mockHand2 = mock(Hand.class);
        mockDiscardPile1 = mock(DiscardPile.class);
        mockDiscardPile2 = mock(DiscardPile.class);
        player1 = new Player("P1", mockHand1, mockDiscardPile1);
        player2 = new Player("P2", mockHand2, mockDiscardPile2);
        mockReader = mock(Reader.class);
    }

    /**
     * Test for the effects of Cardinal
     * The user will look at the first opponents' hand after they switch their hand
     */
    @Test
    public void testUseCardinalLookFirstOpponent(){
        // Define the behavior of test doubles
        when(mockHand1.peek(0)).thenReturn(Card.BARON);
        when(mockHand2.peek(0)).thenReturn(Card.GUARD);
        when(mockReader.choosePlayerWhenCardinal()).thenReturn(0);

        // Before useCardinal()
        assertEquals(player1.getHand().peek(0), Card.BARON);
        assertEquals(player2.getHand().peek(0), Card.GUARD);

        gameActions.useCardinal(player1, player2, mockReader);

        // Assert switching hand
        assertEquals(player1.getHand().peek(0), Card.GUARD);
        assertEquals(player2.getHand().peek(0), Card.BARON);

        // Assert revealing card and output
        String output = outputStream.toString();
        assertTrue(output.contains("P1 shows you a " + Card.GUARD));
    }

    /**
     * Test for the effects of Cardinal
     * The user will look at the second opponent's hand after they switch their hand
     */
    @Test
    public void testUseCardinalLookSecondOpponent(){
        // Define the behavior of test doubles
        when(mockHand1.peek(0)).thenReturn(Card.BARON);
        when(mockHand2.peek(0)).thenReturn(Card.GUARD);
        when(mockReader.choosePlayerWhenCardinal()).thenReturn(1);

        // Before useCardinal()
        assertEquals(player1.getHand().peek(0), Card.BARON);
        assertEquals(player2.getHand().peek(0), Card.GUARD);

        gameActions.useCardinal(player1, player2, mockReader);

        // Assert switching hand
        assertEquals(player1.getHand().peek(0), Card.GUARD);
        assertEquals(player2.getHand().peek(0), Card.BARON);

        // Assert revealing card and output
        String output = outputStream.toString();
        assertTrue(output.contains("P2 shows you a " + Card.BARON));
    }

}
