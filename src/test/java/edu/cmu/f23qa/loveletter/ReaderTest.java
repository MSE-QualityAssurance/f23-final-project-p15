package edu.cmu.f23qa.loveletter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;


public class ReaderTest {
    private Reader reader;
    private Scanner mockScanner;
    private ByteArrayOutputStream outputStream;
    private InputStream inputStream;
    private PlayerList players = new PlayerList();
    private Thread inputThread;


    @BeforeEach
    public void setUp() {
        mockScanner = Mockito.mock(Scanner.class);
        reader = new Reader(mockScanner);

        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        inputStream = System.in;
    }

    // This test case is for the scenario when the number of opponents is valid.
    @Test
    public void testGetNumOpponentsValid() {
        Mockito.when(mockScanner.nextInt()).thenReturn(2);
        int result = reader.getNumOpponents();
        assertEquals(2, result);
    }

    // This test case is for the scenario when the number of opponents is initially invalid
    @Test
    public void testGetNumOpponentsInvalid() {
        Mockito.when(mockScanner.nextInt()).thenReturn(-1, 2);
        int result = reader.getNumOpponents();
        assertEquals(2, result);
        String consoleOutput = outputStream.toString();
        assertTrue(consoleOutput.contains("Invalid number"));
    }

    // This test case is for valid draw choice when Bishop
    @Test
    public void testDrawWhenBishopValid() {
        Mockito.when(mockScanner.nextInt()).thenReturn(0);
        int result = reader.drawWhenBishop();
        assertEquals(0, result);

        String consoleOutput = outputStream.toString();
        assertTrue(consoleOutput.contains("0. Yes"));
        assertTrue(consoleOutput.contains("1. No"));
        assertTrue(consoleOutput.contains("Your choice: "));
    }

    // This test case is for invalid draw choice when Bishop
    @Test
    public void testDrawWhenBishopInvalid() {
        Mockito.when(mockScanner.nextInt()).thenReturn(2, 1);
        int result = reader.drawWhenBishop();
        assertEquals(1, result);

        String consoleOutput = outputStream.toString();
        assertTrue(consoleOutput.contains("Invalid choice!"));
    }

    // This test case is for valid card number when Bishop
    @Test
    public void testPickCardNumberWhenBishopValid() {
        Mockito.when(mockScanner.nextInt()).thenReturn(6);
        int result = reader.pickCardNumberWhenBishop();
        assertEquals(6, result);
    }

    // This test case is for invalid card number when Bishop
    @Test
    public void testPickCardNumberWhenBishopInvalid() {
        Mockito.when(mockScanner.nextInt()).thenReturn(10, 0);
        int result = reader.pickCardNumberWhenBishop();
        assertEquals(0, result);

        String consoleOutput = outputStream.toString();
        assertTrue(consoleOutput.contains("Invalid card number"));
    }

    // This test case is for valid opponent when Bishop
    @Test
    public void testChoosePlayerWhenCardinalValid() {
        Mockito.when(mockScanner.nextLine()).thenReturn("0");
        int result = reader.choosePlayerWhenCardinal();

        assertEquals(0, result);
    }

    // This test case is for no opponent when Bishop
    @Test
    public void testChoosePlayerWhenCardinalEnter() {
        Mockito.when(mockScanner.nextLine()).thenReturn("");
        int result = reader.choosePlayerWhenCardinal();

        assertEquals(-1, result);
    }

    // This test case is for invalid opponent when Bishop
    @Test
    public void testChoosePlayerWhenCardinalInvalid() {
        Mockito.when(mockScanner.nextLine()).thenReturn("2", "1");
        int result = reader.choosePlayerWhenCardinal();
        assertEquals(1, result);

        String consoleOutput = outputStream.toString();
        assertTrue(consoleOutput.contains("Please enter a valid position"));
    }

    // This test case is for valid opponent when Guard, for 2-4 players
    @Test
    public void testPickCardNumberWhenGuardNotPremiumValid() {
        Mockito.when(mockScanner.nextInt()).thenReturn(5);
        int result = reader.pickCardNumberWhenGuard(false);
        assertEquals(5, result);
    }

    // This test case is for invalid opponent when Guard, for 2-4 players
    @Test
    public void testPickCardNumberWhenGuardNotPremiumInvalid() {
        Mockito.when(mockScanner.nextInt()).thenReturn(0, 8);
        int result = reader.pickCardNumberWhenGuard(false);
        assertEquals(8, result);

        String consoleOutput = outputStream.toString();
        assertTrue(consoleOutput.contains("Invalid card value!"));
    }

    // This test case is for valid opponent when Guard, for 5-8 players
    @Test
    public void testPickCardNumberWhenGuardPremiumValid() {
        Mockito.when(mockScanner.nextInt()).thenReturn(0);
        int result = reader.pickCardNumberWhenGuard(true);
        assertEquals(0, result);
    }

    // This test case is for invalid opponent when Guard, for 5-8 players
    @Test
    public void testPickCardNumberWhenGuardPremiumInvalid() {
        Mockito.when(mockScanner.nextInt()).thenReturn(1, 9);
        int result = reader.pickCardNumberWhenGuard(true);
        assertEquals(9, result);

        String consoleOutput = outputStream.toString();
        assertTrue(consoleOutput.contains("Invalid card value!"));
    }

    // This test case is for valid card position
    @Test
    public void testGetCardValid() {
        Mockito.when(mockScanner.nextLine()).thenReturn("1");
        int result = reader.getCard();
        assertEquals(1, result);
    }

    // This test case is for invalid card position
    @Test
    public void testGetCardInvalid() {
        Mockito.when(mockScanner.nextLine()).thenReturn("-1", "0");
        int result = reader.getCard();
        assertEquals(0, result);

        String consoleOutput = outputStream.toString();
        assertTrue(consoleOutput.contains("Please enter a valid card position"));
    }

    // This test case is for get 2 players (min)
    @Test
    public void testGetPlayerMinNum() {
        Mockito.when(mockScanner.nextLine()).thenReturn("u1", "u2", "");
        List<String> result = reader.getPlayers(2, 8);
        assertEquals(Arrays.asList("u1", "u2"), result);
    }

    // This test case is for get 5 players
    @Test
    public void testGetPlayerNum5() {
        Mockito.when(mockScanner.nextLine()).thenReturn("u1", "u2", "u3", "u4", "u5", "");
        List<String> result = reader.getPlayers(2, 8);
        assertEquals(Arrays.asList("u1", "u2", "u3", "u4", "u5"), result);
    }

    // This test case is for get 8 players (input 9 but already reaches max)
    @Test
    public void testGetPlayerMaxNum() {
        Mockito.when(mockScanner.nextLine()).thenReturn("u1", "u2", "u3", "u4", "u5", "u6", "u7", "u8", "u9_not_valid");
        List<String> result = reader.getPlayers(2, 8);
        assertEquals(Arrays.asList("u1", "u2", "u3", "u4", "u5", "u6", "u7", "u8"), result);
        
        String consoleOutput = outputStream.toString();
        assertTrue(consoleOutput.contains("There are already 8 players in the game, game should start now!"));
    }

    // This test case is for get 1 player (invalid, not enough)
    @Test
    public void testGetPlayerNum1() {
        Mockito.when(mockScanner.nextLine()).thenReturn("u1", "", "u2", "");
        List<String> result = reader.getPlayers(2, 8);

        String consoleOutput = outputStream.toString();
        assertTrue(consoleOutput.contains("There must be at least 2 players, please add more players!"));
    }


    // This test case is for get opponent when player input is invalid
    /* 
    @Test  
    public void testGetOpponentNullNullNull() {
        List<String> playerNames = new ArrayList<>(
            Arrays.asList("u1", "u2", "u3", "u4", "u5"));
        players.addPlayers(playerNames);

        Mockito.when(mockScanner.nextLine()).thenReturn("u6", "u2");
        // assertNull(players.getPlayer(mockScanner.nextLine()));
        // assertNotNull(players.getPlayer(mockScanner.nextLine()));
        Player opponent = reader.getOpponent(players, null, null, null);
 
        String consoleOutput = outputStream.toString();
        assertTrue(consoleOutput.contains("This player is not in the game"));
    }
    */

    // This test case is for get opponent when sycophantPlayer is not alive and thus returns null
    @Test
    public void testGetOpponentNotNullNullInvalid(){
        List<String> playerNames = new ArrayList<>(
            Arrays.asList("u1", "u2", "u3", "u4", "u5"));
        players.addPlayers(playerNames);

        Player mockSycophantPlayer = Mockito.mock(Player.class);

        // mock that playerWithSycophant is not alive
        when(mockSycophantPlayer.isAlive()).thenReturn(false);
        Player opponent = reader.getOpponent(players, mockSycophantPlayer, null, null);
        assertNull(opponent);
    }

    // This test case is for get opponent when sycophantPlayer is valid and thus returns sycophantPlayer
    @Test
    public void testGetOpponentNotNullNullValid() {
        List<String> playerNames = new ArrayList<>(
            Arrays.asList("u1", "u2", "u3", "u4", "u5"));
        players.addPlayers(playerNames);

        Player mockPlayerWithSycophant = Mockito.mock(Player.class);

        // mock that playerWithSycophant is alive and unprotected
        when(mockPlayerWithSycophant.isAlive()).thenReturn(true);
        when(mockPlayerWithSycophant.isProtected()).thenReturn(false);
        when(mockPlayerWithSycophant.getName()).thenReturn("u1");

        // user is null, must choose sycophant
        Player opponent = reader.getOpponent(players, mockPlayerWithSycophant, null, null);
        assertEquals(mockPlayerWithSycophant, opponent);
    }


    /* 
    @Test
    public void testGetOpponentNullNotNull() throws InterruptedException {
        List<String> playerNames = new ArrayList<>(
            Arrays.asList("u1", "u2", "u3", "u4", "u5"));
        players.addPlayers(playerNames);

        Player mockUser = Mockito.mock(Player.class);

        Mockito.when(mockScanner.nextLine()).thenReturn("u2", "");
         
        // mock that playerWithSycophant is alive and unprotected
        when(mockUser.getName()).thenReturn("u2");
 
        inputThread = new Thread(() -> reader.getOpponent(players, null, mockUser, null));
        inputThread.start();

        Thread.sleep(100);
        inputThread.interrupt();

        String consoleOutput = outputStream.toString();
        //System.out.println("console output: "+consoleOutput);
        assertEquals("hahahah", consoleOutput);
        //assertTrue(consoleOutput.contains("You cannot target yourself"));
    }
    */

}
