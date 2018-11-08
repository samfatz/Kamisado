import java.util.ArrayList;

public class Player {

    private String username;
    private String colour;
    private int score;
    public ArrayList<Piece> pieces;

    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        this.score = score;
    }
    public String getUsername() {
        return username;
    }
    public String getColour() {
        return colour;
    }


    public Player(String username, String colour, int score) {
        this.username = username;
        this.colour = colour;
        this.score = score;
    }

}
