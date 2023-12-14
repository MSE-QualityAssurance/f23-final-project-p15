package edu.cmu.f23qa.loveletter;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GameTest {

    private ByteArrayOutputStream outputStream;

    private GameActions gameActions;
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
        gameActions = new GameActions(){};
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
     * Test for checking the round winner.
     * The case that only one player alive.
     */
    @Test
    public void testCheckForRoundWinnerWithOnePlayerAlive() {

        Player player1 = Mockito.mock(Player.class);
        LinkedList<Player> players = new LinkedList<>(Arrays.asList(player1));
        PlayerList playerList = Mockito.mock(PlayerList.class);
        Game game = new Game(mockReader, playerList, mockDeck);

        // Define the behavior of test doubles
        when(player1.getTokens()).thenReturn(1);
        when(playerList.getAlivePlayers()).thenReturn(players);
        when(playerList.checkWinnerForJesterToken(players)).thenReturn(null);
        when(playerList.checkPlayerForConstableToken()).thenReturn(null);

        List<Player> expectedWinners = new ArrayList<Player>(Arrays.asList(player1));

        // Assert revealing card and output
        Assertions.assertEquals(game.checkForRoundWinner(), expectedWinners);
        Assertions.assertEquals(game.lastRoundWinner, expectedWinners.get(0));
        

        
    }

    /**
     * Test for checking the round winner.
     * The case that multiple player alive, and they have different hand card values.
     * Assume neither winner have Jester token nor any discarded Constable is alive.
     */
    @Test
    public void testCheckForRoundWinnerWithOneHighestHand() {

        Player player1 = Mockito.mock(Player.class);
        Player player2 = Mockito.mock(Player.class);
        LinkedList<Player> players = new LinkedList<>(Arrays.asList(player1, player2));
        LinkedList<Player> winnerPlayers = new LinkedList<>(Arrays.asList(player1));
        PlayerList playerList = Mockito.mock(PlayerList.class);
        Game game = new Game(mockReader, playerList, mockDeck);

        // Define the behavior of test doubles
        when(playerList.getAlivePlayers()).thenReturn(players);
        when(playerList.checkWinnerForJesterToken(players)).thenReturn(null);
        when(playerList.checkPlayerForConstableToken()).thenReturn(null);
        when(playerList.compareHand(players)).thenReturn(winnerPlayers);

        List<Player> expectedWinners = new ArrayList<Player>(Arrays.asList(player1));

        // Assert revealing card and output
        Assertions.assertEquals(game.checkForRoundWinner(), expectedWinners);
        Assertions.assertEquals(game.lastRoundWinner, expectedWinners.get(0));
    
    } 

    /**
     * Test for checking the round winner.
     * The case that multiple player alive, and they have the same hand card values
     * and different discard piles value.
     * Assume neither winner have Jester token nor any discarded Constable is alive.
     */
    @Test
    public void testCheckForRoundWinnerWithOneHighestDiscardPile() {

        Player player1 = Mockito.mock(Player.class);
        Player player2 = Mockito.mock(Player.class);
        LinkedList<Player> players = new LinkedList<>(Arrays.asList(player1, player2));
        LinkedList<Player> winnerPlayers = new LinkedList<>(Arrays.asList(player1, player2));
        LinkedList<Player> winnerPlayersPile = new LinkedList<>(Arrays.asList(player1));
        PlayerList playerList = Mockito.mock(PlayerList.class);
        Game game = new Game(mockReader, playerList, mockDeck);

        // Define the behavior of test doubles
        when(playerList.getAlivePlayers()).thenReturn(players);
        when(playerList.checkWinnerForJesterToken(players)).thenReturn(null);
        when(playerList.checkPlayerForConstableToken()).thenReturn(null);
        when(playerList.compareHand(players)).thenReturn(winnerPlayers);
        when(playerList.compareUsedPiles(winnerPlayers)).thenReturn(winnerPlayersPile);

        List<Player> expectedWinners = new ArrayList<Player>(Arrays.asList(player1));

        // Assert revealing card and output
        Assertions.assertEquals(game.checkForRoundWinner(), expectedWinners);
        Assertions.assertEquals(game.lastRoundWinner, expectedWinners.get(0));
    }

    /**
     * Test for checking the round winner.
     * The case that multiple player alive, and they have the same hand card values
     * and the same discard piles value.
     * Assume neither winner have Jester token nor any discarded Constable is alive.
     */
    @Test
    public void testCheckForRoundWinnerWithMultipleHighestDiscardPile() {
        Player player1 = Mockito.mock(Player.class);
        Player player2 = Mockito.mock(Player.class);
        LinkedList<Player> players = new LinkedList<>(Arrays.asList(player1, player2));
        LinkedList<Player> winnerPlayers = new LinkedList<>(Arrays.asList(player1, player2));
        LinkedList<Player> winnerPlayersPile = new LinkedList<>(Arrays.asList(player1, player2));
        PlayerList playerList = Mockito.mock(PlayerList.class);
        Game game = new Game(mockReader, playerList, mockDeck);

        // Define the behavior of test doubles
        when(playerList.getAlivePlayers()).thenReturn(players);
        when(playerList.checkWinnerForJesterToken(players)).thenReturn(null);
        when(playerList.checkPlayerForConstableToken()).thenReturn(null);
        when(playerList.compareHand(players)).thenReturn(winnerPlayers);
        when(playerList.compareUsedPiles(winnerPlayers)).thenReturn(winnerPlayersPile);

        List<Player> expectedWinners = new ArrayList<Player>(Arrays.asList(player1, player2));

        // Assert revealing card and output
        Assertions.assertEquals(game.checkForRoundWinner(), expectedWinners);
        Assertions.assertEquals(game.lastRoundWinner, expectedWinners.get(0));
    }

    /**
     * Test for checking the round winner.
     * The case that one of the winners have Jester token,
     * and the one who discard counstable is not alive.
     * Assume only one player is alive.
     */
    @Test
    public void testCheckForRoundWinnerWithJesterAndCounstable() {
        Player player1 = new Player("u1", mockHand1, mockDiscardPile1);
        Player player2 = new Player("u2", mockHand1, mockDiscardPile1);

        LinkedList<Player> players = new LinkedList<>(Arrays.asList(player1));
        PlayerList playerList = Mockito.mock(PlayerList.class);
        Game game = new Game(mockReader, playerList, mockDeck);

        // Define the behavior of test doubles
        when(playerList.getAlivePlayers()).thenReturn(players);
        when(playerList.checkWinnerForJesterToken(players)).thenReturn(player2);
        when(playerList.checkPlayerForConstableToken()).thenReturn(player2);

        // Assert revealing card and output
        game.checkForRoundWinner();
        Assertions.assertEquals(player1.getTokens(), 1);
        Assertions.assertEquals(player2.getTokens(), 2);

    }

}
