import javafx.geometry.Pos;
import javafx.scene.control.Button;

public class BoardSquare extends Button {
    private String colour;
    private int xordinate, yordinate;
    private boolean occupied;

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    private Piece piece;

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

    public boolean getOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
        this.setStyle("-fx-background-color: "+colour+";");
    }

    public BoardSquare(int xordinate, int yordinate){
        colour = "";
        this.setAlignment(Pos.CENTER);
        this.setMinSize(75, 75);
        this.setWidth(125);
        this.setHeight(125);
        this.xordinate = xordinate;
        this.yordinate = yordinate;
    }


}
