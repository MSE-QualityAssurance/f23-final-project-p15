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
     */
    void useGuard(String guessedCard, Player opponent) {
        // ArrayList<String> cardNames = new ArrayList<>(Arrays.asList(Card.CARD_NAMES));

        // System.out.print("Which card would you like to guess: ");
        // String cardName = in.nextLine();

        // while (!cardNames.contains(cardName.toLowerCase()) || cardName.equalsIgnoreCase("guard")) {
        //     System.out.println("Invalid card name");
        //     System.out.print("Which card would you like to guess: ");
        //     cardName = in.nextLine();
        // }

        Card opponentCard = opponent.getHand().peek(0);
        if (opponentCard.getName().equalsIgnoreCase(guessedCard)) {
            System.out.println("You have guessed correctly!");
            opponent.eliminate();
        } else {
            System.out.println("You have guessed incorrectly");
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
        } else {
            System.out.println("You have the same card!");
            if (opponent.getDiscarded().value() > user.getDiscarded().value()) {
                System.out.println("You have lost the used pile comparison");
                user.eliminate();
            } else {
                System.out.println("You have won the used pile comparison");
                opponent.eliminate();
            }
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
        if (d.hasMoreCards()) {
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

    // /**
    //  * Useful method for obtaining a chosen target from the player list.
    //  * @param in
    //  *          the input stream
    //  * @param playerList
    //  *          the list of players
    //  * @param user
    //  *          the player choosing an opponent
    //  * @return the chosen target player
    //  */
    // Player getOpponent(Scanner in, PlayerList playerList, Player user) {
    //     Player opponent = null;
    //     boolean validTarget = false;
    //     while (!validTarget) {
    //         System.out.print("Who would you like to target: ");
    //         String opponentName = in.nextLine();
    //         opponent = playerList.getPlayer(opponentName);
    //         if (opponent == null) {
    //             System.out.println("This player is not in the game");
    //         } else if (opponent.isProtected()) {
    //             System.out.println("This player is protected by a handmaiden");
    //         } else if (opponent.getName().equals(user.getName())) {
    //             System.out.println("You cannot target yourself");
    //         } else if (!opponent.getHand().hasCards()) {
    //             System.out.println("This player is out of cards");
    //         } else {
    //             validTarget = true;
    //         }
    //     }
    //     return opponent;
    // }

}
