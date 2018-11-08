import javafx.geometry.Insets;
import javafx.scene.control.Button;

public class Piece extends Button {
    // Fields
    private String type;
    private String colour;
    private String playerColour;
    private int xordinate, yordinate;
    private boolean selected;
    private BoardSquare boardSquare;
    private String styleString;


    // Mutator methods
    public String getPlayerColour() {
        return playerColour;
    }

    public void setPlayerColour(String playerColour) {
        this.playerColour = playerColour;
    }

    public BoardSquare getBoardSquare() {
        return boardSquare;
    }

    public void setBoardSquare(BoardSquare boardSquare) {
        this.boardSquare = boardSquare;
    }

    public int getXordinate() {
        return xordinate;
    }

    public void setXordinate(int xordinate) {
        this.xordinate = xordinate;
    }

    public int getYordinate() {
        return yordinate;
    }

    public void setYordinate(int yordinate) {
        this.yordinate = yordinate;
    }

    public boolean getSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getColour() {
        return colour;
    }

    public void setCoordinates(int xordinate, int yordinate){
        this.xordinate = xordinate;
        this.yordinate = yordinate;
    }

    public void setStyleString(String styleString) {
        this.styleString = styleString;
        this.setStyle(styleString);
    }

    /**
     * Creates a piece and sets its values.
     * @param type The type of the piece, i.e tower, sumo, double-sumo...
     * @param xordinate X ordinate of the pieces location on the gridpane
     * @param yordinate Y ordinate of the pieces location on the gridpane
     * @param pieceColour Colour of the piece: green, red, brown, yellow etc...
     * @param playerColour Border colour which determines what player it belongs to. White or black
     */
    public Piece(String type, int xordinate, int yordinate, String pieceColour, String playerColour) {

        styleString = " -fx-background-color: \n" +
                playerColour + "," +
                pieceColour +
                "        white;\n" +
                "    -fx-background-insets: 6,10;\n" +
                "    -fx-background-radius: 40,40;\n";
        this.selected = false;
        this.boardSquare = null;
        this.setMaxSize(60, 60);
        this.setMinSize(60, 60);
        this.setStyle(styleString);
        this.setPadding(new Insets(0, 100, 0, 0));
        this.type = type;
        this.colour = pieceColour;
        this.playerColour = playerColour;
        this.xordinate = xordinate;
        this.yordinate = yordinate;
    }
}
