import javafx.scene.Scene;
import javafx.stage.Stage;

public class Game {
    private Board board;
    private Player player1;
    private Player player2;
    private int score[];

    public int[] getScore() {
        return score;
    }

    public void setScore(int[] score) {
        this.score = score;
    }

    public Player getPlayer1() {
        return player1;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }


    public Game(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    public Game (Player player) {
        this.player1 = player;
        this.player2 = new Player("Computer", "black", 0);
    }

    public void start(Stage mainStage, Scene scene, int bestOf, boolean speedMode, boolean singlePlayer, boolean hardMode){
        board = new Board();
        board.load(mainStage, scene, player1, player2, bestOf, speedMode, singlePlayer, hardMode);
       // player1.pieces = board.getWhitePieces();
       // player2.pieces = board.getBlackPieces();
    }
}
