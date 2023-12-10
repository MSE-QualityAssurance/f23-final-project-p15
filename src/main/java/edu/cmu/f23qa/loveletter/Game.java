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

    // /**
    //  * Public constructor for a Game object.
    //  * @param in
    //  * @param players
    //  * @param deck
    //  *          the input stream
    //  */
    // public Game(Reader in, PlayerList players, Deck deck) {
    //     this.players = players;
    //     this.deck = deck;
    //     this.in = in;
    // }

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
        while (players.getGameWinner().size() != 1) {
            setUpRound();
            playRound();
            checkIfGameEnds();
        } // end of game

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
     * 
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
            // check if the round ends
            checkForRoundWinner();
        }
    }

    /**
     * Check if the game ends
     */ 
    private void checkIfGameEnds() {
        List<Player> gameWinners = players.getGameWinner();
        if (gameWinners.size() == 1) {
            System.out.println(gameWinners.get(0) + " has won the game and the heart of the princess!");
        }
        // in case of a tie
        else if (gameWinners.size() > 1) {
            System.out.println("It's a tie! Let's play one more round!");
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
        int value = card.value();
        String name = card.name();

        user.getDiscarded().add(card);

        // Get opponent
        List<Integer> needOpponent = Arrays.asList(1, 2, 3, 5, 6);
        Player opponent = null;
        if (needOpponent.contains(value)) {
            if (value == 5) {
                opponent = in.getOpponent(players);
            } else {
                opponent = in.getOpponentNotSelf(players, user);
            }
        }

        // Handlers
        switch (value) {
            case 1:
                String guessedCard = in.pickCardWhenGuard();
                useGuard(guessedCard, opponent);
                break;
            case 2:
                usePriest(opponent);
                break;
            case 3:
                useBaron(user, opponent);
                break;
            case 4:
                useHandmaiden(user);
                break;
            case 5:
                usePrince(opponent, deck);
                break;
            case 6:
                useKing(user, opponent);
                break;
            case 8:
                usePrincess(user);
                break;
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
            } else {
                winners = players.compareUsedPiles(winnersAfterCmpHands);
            }
        }

        for (Player winner : winners) {
            winner.addToken();
        }

        lastRoundWinner = winners.get(0);

        return winners;
    }
}
