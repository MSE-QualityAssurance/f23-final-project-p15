package edu.cmu.f23qa.loveletter;

import java.util.*;

/**
 * The main game class. Contains methods for running the game.
 */
public class Game extends GameActions {
    private PlayerList players;
    private Deck deck;
    private Player lastRoundWinner = null;

    /**
     * The input stream.
     */
    private Reader in;

    /**
     * Public constructor for a Game object.
     * 
     * @param in
     *           the input stream
     */
    public Game(Reader in) {
        this.players = new PlayerList();
        this.deck = new Deck();
        this.in = in;
    }

    /**
     * Public constructor for a Game object.
     * @param in
     * @param players
     * @param deck
     *          the input stream
     */
    public Game(Reader in, PlayerList players, Deck deck) {
        this.players = players;
        this.deck = deck;
        this.in = in;
    }

    /**
     * Sets up the players that make up the player list.
     */
    public void setPlayers() {
        List<String> playerNames = in.getPlayers(2, 8);
        this.players.addPlayers(playerNames);
        this.players.setTokensToWin();
    }

    /**
     * The main game loop.
     */
    public void start() {
        while (!checkIfGameEnds()) {
            setUpRound();
            playRound();
        } 
        // end of game
    }

    /**
     * Sets up each round of the game by resetting plaer states and preparing the deck
     */
    private void setUpRound() {
        players.reset(); // clear their hands and discards
        setDeck();

        players.dealCards(deck);
        players.setBeginner(lastRoundWinner);
    }

    /**
     * Play one round
     */
    private void playRound() {
        while (players.getAlivePlayers().size() != 1 && deck.hasMoreCards()) {
            Player turn = players.getCurrentPlayer();

            if (turn.isAlive()) {
                players.printUsedPiles();
                System.out.println("\n" + turn.getName() + "'s turn:");
                if (turn.isProtected()) {
                    turn.switchProtection();
                }
                turn.getHand().add(deck.draw());

                int royaltyPos = turn.getHand().royaltyPos();
                if (royaltyPos != -1) {
                    if (royaltyPos == 0 && turn.getHand().peek(1).value() == 7) {
                        playCard(turn.getHand().remove(1), turn);
                    } else if (royaltyPos == 1 && turn.getHand().peek(0).value() == 7) {
                        playCard(turn.getHand().remove(0), turn);
                    } else {
                        playCard(getCard(turn), turn);
                    }
                } else {
                    playCard(getCard(turn), turn);
                }
            }
            // check if the game already ends
            if (checkIfGameEnds()) {
                break;
            }
            // check if the round ends
            checkForRoundWinner();
        }
    }

    /**
     * Check if the game ends
     * @return true if the game ends, false if not
     */ 
    private boolean checkIfGameEnds() {
        List<Player> gameWinners = players.getGameWinner();
        if (gameWinners.size() == 0) {
            return false;
        }
        else if (gameWinners.size() == 1) {
            System.out.println(gameWinners.get(0) + " has won the game and the heart of the princess!");
            return true;
        }
        // in case of a tie
        // when gameWinners.size() > 1
        else{
            System.out.println("It's a tie! Let's play one more round!");
            return false;
        }
    }

    /**
     * Builds a new full deck and shuffles it.
     */
    private void setDeck() {
        this.deck.build(this.players.getNumPlayers());
        this.deck.shuffle();
        // remove cards from the deck initially, according to the rule
        this.deck.removeCards(this.players.getNumPlayers());
    }

    /**
     * Determines the card used by the player and performs the card's action.
     * 
     * @param card
     *             the played card
     * @param user
     *             the player of the card
     */
    private void playCard(Card card, Player user) {
        user.getDiscarded().add(card);

        // Get opponent
        Player opponent = null;
        Player opponent2 = null;

        List<Card> needOneOpponent = Arrays.asList(
            Card.GUARD, Card.PRIEST, Card.BARON, Card.PRINCE, 
            Card.KING, Card.QUEEN, Card.JESTER, Card.BISHOP);
      
        if (needOneOpponent.contains(card)) {
            if (card == Card.PRINCE) {
                if (players.getNumAvailablePlayers(null) < 1) {
                    System.out.println("No enough players can be chosen");
                    return;
                }
                opponent = in.getOpponent(players, null, null);
            } else {
                if (players.getNumAvailablePlayers(user) < 1) {
                    System.out.println("No enough players can be chosen");
                    return;
                }
                opponent = in.getOpponent(players, user, null);
            }
        }

        if (card == Card.CARDINAL) {
            if (players.getNumAvailablePlayers(null) < 2) {
                System.out.println("No enough players can be chosen");
                return;
            }
            opponent = in.getOpponent(players, null, null);
            opponent2 = in.getOpponent(players, null, opponent);
        }

        if (card == Card.BARONESS) {
            int numAvailablePlayers = players.getNumAvailablePlayers(user);
            // No available player, skip
            if (numAvailablePlayers < 1) {
                System.out.println("No enough players can be chosen");
                return;

            // One available player, choose one opponent
            } else if (numAvailablePlayers == 1) {
                System.out.println("Please choose the only player whose hand you want to inspect");
                opponent = in.getOpponent(players, user, null);

            // More than one players, choose one or two players
            } else {
                int numOpponents = in.getNumOpponents();
                opponent = in.getOpponent(players, user, null);
                if (numOpponents == 2) {
                    opponent2 = in.getOpponent(players, user, opponent);
                } 
            }
        }

        // Handlers
        switch (card) {
            case GUARD:
                String guessedCardOfGuard = in.pickCardWhenGuard();
                useGuard(guessedCardOfGuard, opponent, user, deck);
                break;
            case PRIEST:
                usePriest(opponent);
                break;
            case BARON:
                useBaron(user, opponent);
                break;
            case HANDMAIDEN:
                useHandmaiden(user);
                break;
            case PRINCE:
                usePrince(opponent, deck);
                break;
            case KING:
                useKing(user, opponent);
                break;
            case COUNTESS:
                usePrincess(user);
                break;
            case BISHOP:
                int guessedCardOfBishop = in.pickCardNumberWhenBishop();
                useBishop(user, guessedCardOfBishop, opponent, in, deck, players);
                break;
            case QUEEN:
                useQueen(user, opponent);
            case CARDINAL:
                useCardinal(opponent, opponent2, in);
            case JESTER:
                useJester(players, user, opponent);
            case BARONESS:
                useBaroness(opponent, opponent2);
            default:
                break;
        }
    }

    /**
     * Allows for the user to pick a card from their hand to play.
     *
     * @param user
     *             the current player
     *
     * @return the chosen card
     */
    private Card getCard(Player user) {
        user.getHand().print();
        int idx = in.getCard();

        return user.getHand().remove(idx);
    }

    /**
     * Check for winner after a round ends
     * 
     * @return the winners of this round
     */
    private List<Player> checkForRoundWinner() {
        List<Player> winners = new ArrayList<Player>();
        List<Player> alivePlayers = players.getAlivePlayers();

        // only one player alive, this round ends and he becomes the winner
        if (alivePlayers.size() == 1) {
            winners = alivePlayers;
        }

        // multiple players alive, check their hands
        else {
            List<Player> winnersAfterCmpHands = players.compareHand(alivePlayers);
            if (winnersAfterCmpHands.size() == 1) {
                winners = winnersAfterCmpHands;
            } 
            
            // all alive players have the same value in hand, check their discard piles
            else {
                winners = players.compareUsedPiles(winnersAfterCmpHands);
            }
        }

        // add token of affection for each winner
        for (Player winner : winners) {
            winner.addToken();
        }

        // check whether winners have Jester token 
        Player playerSetJesterToken = players.checkWinnerForJesterToken(winners);
        if (playerSetJesterToken != null) {
            playerSetJesterToken.addToken();
        }

        // set the latest winner
        lastRoundWinner = winners.get(0);

        return winners;
    }
}
