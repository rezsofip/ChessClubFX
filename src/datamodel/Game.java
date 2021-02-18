package datamodel;

public class Game {

    private Player player;
    private Player opponentPlayer;
    private int winner;

    public Game(Player player, Player opponentPlayer, int winner) {
        this.player = player;
        this.opponentPlayer = opponentPlayer;
        this.winner = winner;
    }

    public Player getPlayer() {
        return player;
    }

    public Player getOpponentPlayer() {
        return opponentPlayer;
    }

    public int getWinner() {
        return winner;
    }

    @Override
    public String toString() {
        if(this.winner == 1) {
            return player.getName() + " nyert " + opponentPlayer.getName() + " ellen.";
        } else if(this.winner == -1) {
            return player.getName() + " vesztett " + opponentPlayer.getName() + " ellen.";
        } else if(this.winner == 0) {
            return player.getName() + " döntetlent játszott " + opponentPlayer.getName() + " ellen.";
        }
        return "";
    }
}
