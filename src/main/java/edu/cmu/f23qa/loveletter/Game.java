package edu.cmu.f23qa.loveletter;

import java.util.*;

/**
 * The main game class. Contains methods for running the game.
 */
public class Game extends GameActions {
    private PlayerList players;
    private Deck deck;
    public Player lastRoundWinner = null;

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
            checkForRoundWinner();
        } 
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
        System.out.println("A new round begins!");

        while (players.getAlivePlayers().size() != 1 && deck.hasMoreCards()) {
            Player turn = players.getCurrentPlayer();

            if (turn.isAlive()) {
                players.printUsedPiles();
                System.out.println("\n" + turn.getName() + "'s turn:");

                // Reverse handmaid status
                if (turn.isProtected()) {
                    turn.switchProtection();
                }

                // Draw a new card
                turn.getHand().add(deck.draw());

                // Choose a card and play
                Card card = getCard(turn);
                playCard(card, turn);
            }

            // check if the game already ends
            if (checkIfGameEnds()) {
                return;
            }
        }
    }

    /**
     * Check if the game ends
     * @return true if the game ends, false if not
     */ 
    public boolean checkIfGameEnds() {
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

    public List<Player> getOpponentsForTurn(Player user, Card card) {
        // Get the player with Sycophant token and reset the token
        Player playerWithSycophant = players.getPlayerWithSycophant();
        players.setPlayerWithSycophant(null);    // reset

        Player opponent1 = null;
        Player opponent2 = null;
        
        // Get one opponent
        List<Card> needOneOpponent = Arrays.asList(
            Card.GUARD, Card.PRIEST, Card.BARON, Card.PRINCE, 
            Card.KING, Card.QUEEN, Card.JESTER, Card.BISHOP, Card.SYCOPHANT);
      
        if (needOneOpponent.contains(card)) {
            // For cards which can target themselves
            if (card == Card.PRINCE || card == Card.SYCOPHANT) { 
                if (players.getNumAvailablePlayers(null) < 1) {
                    System.out.println("No enough players can be chosen");
                    return null;
                } else {
                    opponent1 = in.getOpponent(players, playerWithSycophant, null, null);
                    if (opponent1 == null) {
                        return null;
                    } else {
                        return new ArrayList<>(Arrays.asList(opponent1));
                    }
                }

            // For cards which can not target themselves
            } else {
                if (players.getNumAvailablePlayers(user) < 1) {
                    System.out.println("No enough players can be chosen");
                    return null;
                } else {
                    opponent1 = in.getOpponent(players, playerWithSycophant, user, null);
                    if (opponent1 == null) {
                        return null;
                    } else {
                        return new ArrayList<>(Arrays.asList(opponent1));
                    }
                }
            }
        }

        // Get two opponents
        if (card == Card.CARDINAL) {
            if (players.getNumAvailablePlayers(null) < 2) {
                System.out.println("No enough players can be chosen");
                return null;

            } 
            else {
                opponent1 = in.getOpponent(players, playerWithSycophant, null, null);
                if (opponent1 == null) {
                    return null;
                }
                opponent2 = in.getOpponent(players, playerWithSycophant, null, opponent1);
                return new ArrayList<>(Arrays.asList(opponent1, opponent2));
            }
        }

        // Get one or two opponents
        if (card == Card.BARONESS) {
            int numAvailablePlayers = players.getNumAvailablePlayers(user);
    
            // No available player, skip
            if (numAvailablePlayers < 1) {
                System.out.println("No enough players can be chosen");
                return null;

            // One available player, choose one opponent
            } else if (numAvailablePlayers == 1) {
                opponent1 = in.getOpponent(players, playerWithSycophant, user, null);
                if (opponent1 == null) {
                    return null;
                } else {
                    return new ArrayList<>(Arrays.asList(opponent1));
                }

            // More than one players, choose one or two players
            } else {
                int numOpponents = in.getNumOpponents();
                opponent1 = in.getOpponent(players, playerWithSycophant, user, null);
                if (opponent1 == null) {
                    return null;
                }
                if (numOpponents == 1) {
                    return new ArrayList<>(Arrays.asList(opponent1));
                } else {
                    opponent2 = in.getOpponent(players, playerWithSycophant, user, opponent1);
                    return new ArrayList<>(Arrays.asList(opponent1, opponent2));
                }
            }
        } 
        
        return null;
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

        List<Card> needOpponent = Arrays.asList(
            Card.GUARD, Card.PRIEST, Card.BARON, Card.PRINCE, 
            Card.KING, Card.QUEEN, Card.JESTER, Card.BISHOP,
            Card.SYCOPHANT, Card.CARDINAL, Card.BARONESS);
        
        List<Player> opponents = new ArrayList<Player>();
        Player opponent = null;
        Player opponent2 = null;

        if (needOpponent.contains(card)) {
            opponents = getOpponentsForTurn(user, card);
            // No one can be chosen, the card loses effects
            if (opponents == null) {
                return;
            }
            
            opponent = opponents.get(0);
            if (opponents.size() == 2) {
                opponent2 = opponents.get(1);
            }
        }

        // Handlers
        // all cards go here execpt for countess, count    
        switch (card) {
            case GUARD:
                boolean ifPremium = false;
                if (players.getNumPlayers() >= 5) {
                    ifPremium = true;
                }
                
                int guessedCardOfGuard = in.pickCardNumberWhenGuard(ifPremium);
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
            case PRINCESS:
                usePrincess(user);
                break;
            case BISHOP:
                int guessedCardOfBishop = in.pickCardNumberWhenBishop();
                useBishop(user, guessedCardOfBishop, opponent, in, deck, players);
                break;
            case QUEEN:
                useQueen(user, opponent);
                break;
            case CARDINAL:
                useCardinal(opponent, opponent2, in);
                break;
            case CONSTABLE:
                useConstable(players, user);
                break;
            case JESTER:
                useJester(players, user, opponent);
                break;
            case BARONESS:
                useBaroness(opponent, opponent2);
                break;
            case SYCOPHANT:
                useSycophant(players, opponent);
                break;
            default:
                break;
        }
    }

    /**
     * Pick a card from the player's hand to play in this turn.
     *
     * @param user
     *             the current player
     *
     * @return the chosen card
     */
    private Card getCard(Player user) {
        // Check if the user holds Countess as well as King or Prince
        int royaltyPos = user.getHand().royaltyPos();
        if (royaltyPos != -1) {
            if (royaltyPos == 0 && user.getHand().peek(1) == Card.COUNTESS) {
                return user.getHand().remove(1);

            } else if (royaltyPos == 1 && user.getHand().peek(0) == Card.COUNTESS) {
                return user.getHand().remove(0);

            } 
        }

        user.getHand().print();
        int idx = in.getCard();

        return user.getHand().remove(idx);
    }

    /**
     * Check for winner after a round ends
     * 
     * @return the winners of this round
     */
    public List<Player> checkForRoundWinner() {
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

        // check whether the player who discarded Constable is alive
        Player playerWithConstable = players.checkPlayerForConstableToken();
        if (playerWithConstable!= null && !playerWithConstable.isAlive()) {
            playerWithConstable.addToken();
        }

        // set the latest winner
        lastRoundWinner = winners.get(0);

        System.out.println("The round ends!!! Winner(s): " + winners);
        System.out.println("----------------------------------------------------------\n\n");

        return winners;
    }

    // New method to get the number of players in the game
    public int getPlayerCount() {
        return this.players.getNumPlayers();
    }
}
