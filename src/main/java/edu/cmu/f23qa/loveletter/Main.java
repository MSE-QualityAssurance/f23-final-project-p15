package edu.cmu.f23qa.loveletter;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Reader in = new Reader(new Scanner(System.in));
        PlayerList players = new PlayerList();
        Deck deck = new Deck();
        Game g = new Game(in, players, deck);
        g.setPlayers();
        g.start();
    }

}
