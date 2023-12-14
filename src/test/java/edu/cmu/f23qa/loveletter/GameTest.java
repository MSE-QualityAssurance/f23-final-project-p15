package edu.cmu.f23qa.loveletter;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GameTest {

    private ByteArrayOutputStream outputStream;

    private Hand mockHand1, mockHand2;
    private DiscardPile mockDiscardPile1, mockDiscardPile2;
    private Player player1, player2;
    private Reader mockReader;

    private PlayerList mockPlayerList;
    private Game game;
    private Deck mockDeck;

    @BeforeEach
    public void setUp() {
        // Redirection output string
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        // Mock PlayerList and Game
        mockHand1 = mock(Hand.class);
        mockHand2 = mock(Hand.class);
        mockDiscardPile1 = mock(DiscardPile.class);
        mockDiscardPile2 = mock(DiscardPile.class);
        player1 = new Player("P1", mockHand1, mockDiscardPile1);
        player2 = new Player("P2", mockHand2, mockDiscardPile2);
        mockReader = mock(Reader.class);


        mockDeck=mock(Deck.class);
        mockPlayerList = mock(PlayerList.class);
        game = new Game(mockReader, mockPlayerList, mockDeck);
    }

    //Verify that the PlayerList is correctly populated with the expected number of players.
    @Test
    public void testSetPlayers() {

        PlayerList spyPlayerList = spy(PlayerList.class);
        game = new Game(mockReader, spyPlayerList, mockDeck);

        when(mockReader.getPlayers(2, 8)).thenReturn(Arrays.asList("Alice", "Bob"));
        game.setPlayers();
        assertEquals(2, spyPlayerList.getNumPlayers(), "There should be 2 players in the game");
    }

    // Mock getGameWinner() to return an empty list
    @Test
    public void testCheckIfGameEndsNoWinners() {
        when(mockPlayerList.getGameWinner()).thenReturn(Collections.emptyList());

        assertFalse(game.checkIfGameEnds(), "Game should not end when no player has reached the winning condition");
        assertEquals("", outputStream.toString().trim(), "Output should be empty");
    }

    // Mock getGameWinner() to return a list with one winner
    @Test
    public void testCheckIfGameEndsSingleWinner() {
        Player winner = new Player("Winner");

        when(mockPlayerList.getGameWinner()).thenReturn(Collections.singletonList(winner));
        for (int i = 0; i < 7; i++) {
            winner.addToken(); // Winner gets 7 tokens
        }
        assertTrue(game.checkIfGameEnds(), "Game should end with a single winner");
        assertEquals("Winner (7 tokens) has won the game and the heart of the princess!", outputStream.toString().trim());
    }

    // Mock getGameWinner() to return a list with multiple winners
    @Test
    public void testCheckIfGameEndsMultipleWinners() {
        // Mock getGameWinner() to return a list with multiple winners
        Player Winner1 = new Player("Winner1");
        Player Winner2 = new Player("Winner2");
        when(mockPlayerList.getGameWinner()).thenReturn(Arrays.asList(Winner1, Winner2));

        assertFalse(game.checkIfGameEnds(), "Game should not end when there's a tie");
    }

    /**
     * Test for pick a card from the player's hand to play in this turn
     * when the player hold the Countess and Price together
     */
    @Test
    public void testGetCardCountessPince() {
        // Create input
        Hand hand = new Hand(new ArrayList<>(Arrays.asList(Card.COUNTESS, Card.PRINCE)));
        Player user = new Player("U1", hand, mockDiscardPile1);

        // Assert output
        Card card = game.getCard(user);
        assertEquals(Card.COUNTESS, card);
    }

    /**
     * Test for pick a card from the player's hand to play in this turn
     * when the player hold the Countess and King together
     */
    @Test
    public void testGetCardKingCountess() {
        // Create input
        Hand hand = new Hand(new ArrayList<>(Arrays.asList(Card.KING, Card.COUNTESS)));
        Player user = new Player("U1", hand, mockDiscardPile1);

        // Assert output
        Card card = game.getCard(user);
        assertEquals(Card.COUNTESS, card);
    }

    /**
     * Test for pick a card from the player's hand to play in this turn
     * when the player hold the Countess but not no King or Prince
     */
    @Test
    public void testGetCardCountessOther() {
        // Create input
        Hand hand = new Hand(new ArrayList<>(Arrays.asList(Card.GUARD, Card.COUNTESS)));
        Player user = new Player("U1", hand, mockDiscardPile1);

        // Define mock behavior
        when(mockReader.getCard()).thenReturn(0);

        // Assert output
        Card card = game.getCard(user);
        assertEquals(Card.GUARD, card);
    }

    /**
     * Test play card for cards need one opponent but no one can be chooson
     */
    @Test
    public void testPlayCardGetNullOpponent() {
        // Set test doubles
        Game spyGame = spy(game);

        // Set input
        Card card = Card.GUARD;
        Player player = new Player("P");

        // Verify outputs
        boolean output = game.playCard(card, player);
        when(spyGame.getOpponentsForTurn(player, card)).thenReturn(null);
        assertEquals(player.getDiscarded().getCards().get(0), card);
        assertEquals(output, false);
    }

    /**
     * Test play card for each card with sucessfully get opponents
     */
    @Test
    public void testPlayCardWithOpponent() {
        // Set test doubles
        Game spyGame = spy(game);

        List<Card> allCards = Arrays.asList(
            Card.GUARD, Card.PRIEST, Card.BARON, Card.PRINCE,
            Card.KING, Card.QUEEN, Card.JESTER, Card.BISHOP, 
            Card.SYCOPHANT, Card.CARDINAL, Card.BARONESS,
            Card.COUNTESS, Card.HANDMAIDEN, Card.ASSASIN, Card.CONSTABLE, Card.COUNT); 
        List<Player> opponents = new ArrayList<Player>(Arrays.asList(player1, player2));

        for (Card card : allCards) {
            // Set input
            Player player = new Player("P");
            when(spyGame.getOpponentsForTurn(player, card)).thenReturn(opponents);
            
            // Verify outputs
            game.playCard(card, player);
            assertEquals(player.getDiscarded().getCards().get(0), card);
        }
    }
}
