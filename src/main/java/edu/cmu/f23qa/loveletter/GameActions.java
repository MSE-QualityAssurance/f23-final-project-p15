package edu.cmu.f23qa.loveletter;

/**
 * The possible player actions to be taken during the game.
 */
abstract class GameActions {

    /**
     * Allows the user to guess a card that a player's hand contains (excluding another guard).
     * If the user is correct, the opponent loses the round and must lay down their card.
     * If the user is incorrect, the opponent is not affected.
     * @param in
     *          the input stream
     * @param opponent
     *          the targeted player
     * @param user
     *          the player who use the Guard card
     * @param deck
     *          the deck of cards
     */
    void useGuard(int guessedCard, Player opponent, Player user, Deck deck) {
        Card opponentCard = opponent.getHand().peek(0);
        // Effects of Assasin
        if (opponentCard == Card.ASSASIN) {
            System.out.println("The opponent held Assasin, you died!");
            user.eliminate();
            opponent.getHand().remove(0);       // there can only be one card in his hand at that time
            opponent.getDiscarded().add(Card.ASSASIN);
            if (deck.hasMoreCardsWithExtraCard()) {
                opponent.getHand().add(deck.draw());
            }
        }
        
        else if (opponentCard.value() == guessedCard) {
            System.out.println("You have guessed correctly!");
            opponent.eliminate();
        }
        
        else {
            System.out.println("You have guessed incorrectly");
        }
    }

    /**
     * Allows the user to guess a card that a player's hand contains.
     * If the user is correct, the user gets a token, and the opponent may discard the revealed card and draw a new card.
     * If the user is incorrect, the user is not affected.
     * @param user
     *          the initiator of the guessing
     * @param guessedCard
     *          the card that the user guesses
     * @param opponent
     *          the opponent
     * @param in
     *          the input stream
     * @param d
     *          the deck of cards
     * @param players
     *          the players in the game
     */
    void useBishop(Player user, int guessedNum, Player opponent, Reader in, Deck d, PlayerList players) {
        Card opponentCard = opponent.getHand().peek(0);
        if (opponentCard.value() == guessedNum) {
            System.out.println("You have guessed correctly! You will now get a token!");
            user.addToken();

            // check if the game ends with this token
            if (user.getTokens() >= 4) {
                System.out.println("You have won the game!");
                return;
            }
            
            System.out.println();
            System.out.println("To player " + opponent.getName() + ", would you like to discard the revealed card and draw a new card?");
            // when 0, the opponent wants to discard the revealed card and draw a new card
            if (in.drawWhenBishop() == 0) {
                System.out.println("You have discarded a " + opponentCard);
                opponent.getDiscarded().add(opponent.getHand().remove(0));
                // check if the deck is empty
                if (d.hasMoreCards()) {
                    opponent.getHand().add(d.draw());
                    System.out.println("You have drawn a " + opponent.getHand().peek(0));
                    return;
                }
                else {
                    System.out.println("The deck is empty! You are out for this round!");
                }
            }
            
        }
        else {
            System.out.println("You have guessed incorrectly.");
        }
    }

    /**
     * Allows the user to peek at the card of an opposing player.
     * @param opponent
     *          the targeted player
     */
    void usePriest(Player opponent) {
        Card opponentCard = opponent.getHand().peek(0);
        System.out.println(opponent.getName() + " shows you a " + opponentCard);
    }

    /**
     * Allows the user to compare cards with an opponent.
     * If the user's card is of higher value, the opposing player loses the round and their card.
     * If the user's card is of lower value, the user loses the round and their card.
     * If the two players have the same card, their used pile values are compared in the same manner.
     * @param user
     *          the initiator of the comparison
     * @param opponent
     *          the targeted player
     */
    void useBaron(Player user, Player opponent) {
        Card userCard = user.getHand().peek(0);
        Card opponentCard = opponent.getHand().peek(0);

        int cardComparison = Integer.compare(userCard.value(), opponentCard.value());
        if (cardComparison > 0) {
            System.out.println("You have won the comparison!");
            opponent.eliminate();
        } else if (cardComparison < 0) {
            System.out.println("You have lost the comparison");
            user.eliminate();
        }
    }

    /**
     * Allows the user to compare cards with an opponent.
     * If the user's card is of higher value, the opposing player wins the round and their card.
     * If the user's card is of lower value, the user wins the round and their card.
     * @param user
     *          the initiator of the comparison
     * @param opponent
     *         the targeted player
     */
    void useQueen(Player user, Player opponent) {
        Card userCard = user.getHand().peek(0);
        Card opponentCard = opponent.getHand().peek(0);

        int cardComparison = Integer.compare(userCard.value(), opponentCard.value());
        if (cardComparison > 0) {
            System.out.println("You have lost the comparison!");
            user.eliminate();
        } else if (cardComparison < 0) {
            System.out.println("You have won the comparison");
            opponent.eliminate();
        }
    }

    /**
     * Switches the user's protection for one turn. This protects them from being targeted.
     * @param user
     *          the current player
     */
    void useHandmaiden(Player user) {
        System.out.println("You are now protected until your next turn");
        user.switchProtection();
    }

    /**
     * Makes an opposing player lay down their card in their used pile and draw another.
     * @param opponent
     *          the targeted player
     * @param d
     *          the deck of cards
     */
    void usePrince(Player opponent, Deck d) {
        opponent.eliminate();
        if (d.hasMoreCardsWithExtraCard()) {
            opponent.getHand().add(d.draw());
        }
    }

    /**
     * Allows the user to switch cards with an opponent.
     * Swaps the user's hand for the opponent's.
     * @param user
     *          the initiator of the swap
     * @param opponent
     *          the targeted player
     */
    void useKing(Player user, Player opponent) {
        Card userCard = user.getHand().remove(0);
        Card opponentCard = opponent.getHand().remove(0);
        user.getHand().add(opponentCard);
        opponent.getHand().add(userCard);
    }

    /**
     * If the princess is played, the user loses the round and must lay down their hand.
     * @param user
     *          the current player
     */
    void usePrincess(Player user) {
        user.eliminate();
    }

    /**
     * Choose exactly 2 players who will switch hands. 
     * The player may look at one of them without revealing it to any other players.
     * @param opponent1
     *      One of the chosen player
     * @param opponent2
     *      Another chosen player
     */
    void useCardinal(Player opponent1, Player opponent2, Reader in) {
        // switch hand
        opponent1.switchHand(opponent2);

        // choose player to look at
        System.out.println(opponent1.getName());
        System.out.println(opponent2.getName());
        int pos = in.choosePlayerWhenCardinal();
        if (pos == 0) {
            System.out.println(opponent1.getName() + " shows you a " + opponent1.getHand().peek(0));
        } else if (pos == 1) {
            System.out.println(opponent2.getName() + " shows you a " + opponent2.getHand().peek(0));
        }
    }

    /**
     * If the Jester is played, a Jester token is given to the chosen player
     * If that player winds the round, the user gain a Token of Affection. 
     * 
     * @param players
     *      the list of all players
     * @para user
     *      the player who play the Jester
     * @para target
     *      the chosen player which the Jester will have effect on
     */
    void useJester(PlayerList players, Player user, Player target) {
        players.setJesterToken(user, target);
    }

    /**
     * If the Constable is played and when the player is out, he gets a token
     * @param players
     *      the list of all players
     * @param user
     *      the player who play the Constable
     */
    void useConstable (PlayerList players, Player user) {
        players.setPlayerWithConstable(user);
    }

    /**
     * The player can inspect the hands of either one or two players
     * @param opponent1 
     *      One chosen player
     * @param opponent2
     *      Another chosen player
     */
    void useBaroness(Player opponent1, Player opponent2) {
        if (opponent1 != null) {
            System.out.println(opponent1.getName() + " shows you a " + opponent1.getHand().peek(0));
        } 
        if (opponent2 != null) {
            System.out.println(opponent2.getName() + " shows you a " + opponent2.getHand().peek(0));
        }
    }

    /**
     * Choose a player and as long as the next card played has an effect that chooses one or more players, 
     * it has to at least choose the player that was marked by the Sycophant.
     * 
     * @param players
     *      The list of players
     * @param opponent
     *      The target to use Sycophant
     */
    void useSycophant(PlayerList players, Player opponent) {
        players.setPlayerWithSycophant(opponent);
    }
}
