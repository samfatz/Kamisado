import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import java.util.*;

public class Board extends Program {
    private String colours[][] = new String[][]{
            {"orange", "blue", "cyan", "pink", "yellow", "red", "green", "brown"},
            {"red", "orange", "pink", "green", "blue", "yellow", "brown", "cyan"},
            {"green", "pink", "orange", "red", "cyan", "brown", "yellow", "blue"},
            {"pink", "cyan", "blue", "orange", "brown", "green", "red", "yellow"},
            {"yellow", "red", "green", "brown", "orange", "blue", "cyan", "pink"},
            {"blue", "yellow", "brown", "cyan", "red", "orange", "pink", "green"},
            {"cyan", "brown", "yellow", "blue", "green", "pink", "orange", "red"},
            {"brown", "green", "red", "yellow", "pink", "cyan", "blue", "orange"},
    };
    private Button menuButton;
    private BorderPane border;
    private Stage stage;
    private boolean pieceSelected = false, speedMode, firstTurn, singlePlayer = false, hardMode = false;
    private Piece selectedPiece, pieceToMove;
    private ArrayList<Circle> possibleMoveIndicators;
    private ArrayList<Piece> whitePieces;
    private ArrayList<Piece> blackPieces;
    private Scene mainMenuScene;
    private Stage mainMenuStage;
    private String currentPlayer = "white";
    private int roundsToWin, bestOf;
    private Player player1;
    private Player player2;
    private Label currentPlayerLabel = new Label(currentPlayer.substring(0, 1).toUpperCase() + currentPlayer.substring(1).toLowerCase() + "'s turn!");
    private ArrayList<BoardSquare> validMoves;
    private final long TURN_TIME = 4000;
    Timer timer;


    /**
     * Main method that is ran when a new game starts. Fills the board with squares and pieces
     * and initialises all the listeners for them.
     * @param mainMenuStage Stage for the main menu, to be used when main menu button is clicked
     * @param mainMenuScene Scene for the main menu, to be used when main menu button is clicked
     * @param player1 Player 1 object
     * @param player2 Player 2 object
     * @param roundsToWin Whatever the player chose they want to play best of. eg 3, 5.
     * @param speedMode
     */
    public void load(Stage mainMenuStage, Scene mainMenuScene, Player player1, Player player2, int roundsToWin, boolean speedMode, boolean singlePlayer, boolean hardMode)  {
        this.mainMenuScene = mainMenuScene;
        this.mainMenuStage = mainMenuStage;
        this.player1 = player1;
        this.player2 = player2;
        this.bestOf = roundsToWin;
        this.speedMode = speedMode;
        firstTurn = true;

        if (singlePlayer) {
            this.singlePlayer = true;
            if (hardMode) {
                this.hardMode = true;
            }
            else{
                this.hardMode = false;
            }
        }
        // Calculate the max amount of wins a player needs
        if (roundsToWin % 2 != 0) {
            roundsToWin++;
            this.roundsToWin = roundsToWin / 2;
        }
        else{
            this.roundsToWin = roundsToWin/2+1;
        }

        loadUI(player1.getUsername(), player2.getUsername());
        ArrayList<BoardSquare> squares = new ArrayList<>();
        possibleMoveIndicators = new ArrayList<>();
        validMoves = new ArrayList<>();
        GridPane rootGridPane = fillBoard(squares);
        border.setCenter(rootGridPane);
        Scene scene = new Scene(border, this.WINDOW_WIDTH, this.WINDOW_HEIGHT);
        stage.setScene(scene);
        stage.show();

        ArrayList<Piece> pieces = new ArrayList<>();
        pieces = loadPieces(rootGridPane, pieces);
        populatepieceArrays(pieces);
        loadButtonListeners(pieces, squares, rootGridPane);

        // Listeners
        menuButton.setOnAction(event -> {
            stage.close();
            mainMenuStage.setScene(mainMenuScene);
            mainMenuStage.show();
        });
    }
    
    /**
     * Populates the two arrays: whitePieces and blackPieces with the correct colour of piece
     * @param pieces the ArrayList of all the pieces to be parsed
     */
    private void populatepieceArrays(ArrayList<Piece> pieces){
        whitePieces = new ArrayList<>();
        blackPieces = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            blackPieces.add(pieces.get(i));
        }
        for (int i = 8; i < 16; i++) {
            whitePieces.add(pieces.get(i));
        }
    }

    /**
     * Initialises a listener for every piece and boardsquare
     * @param squares Arraylist of boardsquares to create listeners for
     * @param rootGridPane The main gridpane, necesarry to pass into movePiece method when
     * @param pieces Arraylist of pieces to create listeners for
     */
    private void loadButtonListeners(ArrayList<Piece> pieces, ArrayList<BoardSquare> squares, GridPane rootGridPane){
        // Piece listeners
        for (Piece tempPiece : pieces) {
            if (tempPiece != selectedPiece){
                if (firstTurn ) {
                    if (whitePieces.contains(tempPiece)) {
                        tempPiece.requestFocus();
                    }
                }
                tempPiece.setOnMouseEntered(event -> {
                    tempPiece.setStyleString("-fx-background-color: " +
                            tempPiece.getColour() + "," +
                            tempPiece.getPlayerColour() +
                            "        white;\n" +
                            "    -fx-background-insets: 8,12;\n" +
                            "    -fx-background-radius: 20,20;");
                });
                tempPiece.setOnMouseExited(event -> {
                    tempPiece.setStyleString("-fx-background-color: " +
                            tempPiece.getPlayerColour() + "," +
                            tempPiece.getColour() +
                            "        white;\n" +
                            "    -fx-background-insets: 6,10;\n" +
                            "    -fx-background-radius: 40,40;");
                });
                tempPiece.setOnKeyReleased(event -> {
                    tempPiece.setStyleString("-fx-background-color: " +
                            tempPiece.getColour() + "," +
                            tempPiece.getPlayerColour() +
                            "        white;\n" +
                            "    -fx-background-insets: 8,12;\n" +
                            "    -fx-background-radius: 20,20;");
                });
                tempPiece.setOnKeyPressed(event -> {
                    tempPiece.setStyleString("-fx-background-color: " +
                            tempPiece.getPlayerColour() + "," +
                            tempPiece.getColour() +
                            "        white;\n" +
                            "    -fx-background-insets: 6,10;\n" +
                            "    -fx-background-radius: 40,40;");
                });
            }

            tempPiece.setOnAction(event -> {
                if(!Objects.equals(tempPiece.getPlayerColour(), currentPlayer)){
                    showWrongPlayerWarning();
                    return;
                }
                if (pieceSelected && firstTurn) {
                    selectedPiece = null;
                    validMoves.clear();
                    pieceSelected = false;
                    hideAvailableMoves(rootGridPane);
                    return;
                }
                if (!pieceSelected) {
                    tempPiece.setSelected(true);
                    pieceSelected = true;
                    selectedPiece = tempPiece;
                    selectedPiece.setStyleString(" -fx-background-color: \n" +
                            selectedPiece.getColour() + "," +
                            selectedPiece.getPlayerColour() +
                            "        white;\n" +
                            "    -fx-background-insets: 6,10;\n" +
                            "    -fx-background-radius: 40,40;\n");
                    calcAvailableMoves(rootGridPane, validMoves, squares, tempPiece);
                }
            });
        }

        // BoardSquare listeners
        for (BoardSquare tempSquare : squares) {
            tempSquare.setOnMouseEntered(event -> {
                tempSquare.setStyle("-fx-background-color: white; \n -fx-opacity: 10%;");
            });
            tempSquare.setOnMouseExited(event -> {
                tempSquare.setStyle("-fx-background-color: " + tempSquare.getColour() + ";");
            });
            tempSquare.setOnKeyReleased(event -> {
                tempSquare.setStyle("-fx-background-color: white; \n -fx-opacity: 10%;");
            });
            tempSquare.setOnKeyPressed(event -> {
                tempSquare.setStyle("-fx-background-color: " + tempSquare.getColour() + ";");
            });
            if (firstTurn) {
                if (tempSquare.getYordinate() == 7 || tempSquare.getYordinate() == 0) {
                    tempSquare.setFocusTraversable(false);
                }
            }
            tempSquare.setOnAction(event -> {
                if (pieceSelected) {
                    if (validMoves.contains(tempSquare)) {
                        movePiece(rootGridPane, selectedPiece, tempSquare.getXordinate(), tempSquare.getYordinate());
                        hideAvailableMoves(rootGridPane);

                        /* switch the active player */
                        if(Objects.equals(selectedPiece.getPlayerColour(), "white")){
                            currentPlayer = "black";
                        }
                        else{
                            currentPlayer = "white";
                        }
                        checkGame(selectedPiece);

                        this.currentPlayerLabel.setText(currentPlayer.substring(0, 1).toUpperCase() + currentPlayer.substring(1).toLowerCase() + " moves next!");
                        selectedPiece.setStyleString(" -fx-background-color: \n" +
                                selectedPiece.getPlayerColour() + "," + //todo fix this nullPointer when reloading a game
                                selectedPiece.getColour() +
                                "        white;\n" +
                                "    -fx-background-insets: 6,10;\n" +
                                "    -fx-background-radius: 40,40;\n");

                        /* get the next piece for the other player */
                        if (Objects.equals(currentPlayer, "black")) {
                            getNextSelectedPiece(squares, rootGridPane, tempSquare, blackPieces);
                            if (singlePlayer) {
                                computerMove();
                            }
                        }
                        else if (Objects.equals(currentPlayer, "white")) {
                            getNextSelectedPiece(squares, rootGridPane, tempSquare, whitePieces);
                        }

                    }
                    else{
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid move!");
                        alert.setHeaderText("Error!");
                        alert.show();
                    }
            }
            });
        }
    }

    private void checkTimeout(Timer timer) {
        if (speedMode) {
            timer.schedule(new TimerTask() {
                public void run() {
                    Platform.runLater(new Runnable() {
                        public void run() {
                            String winner;
                            if (Objects.equals(currentPlayer, "white")) {
                                winner = "black";
                            }else{
                                winner = "white";
                            }
                            endGameMessage(winner, true);
                        }
                    });
                }
            }, TURN_TIME);
        }
    }

    /**
     * Makes a move automatically if the game is in single player mode.
     * On easy difficulty, a random valid move is chosen.
     * On hard difficulty, a winning move will always be made if it exists, if
     * it doesnt exist, then the most aggressive move possible will be made.
     */
    private void computerMove(){
        Random rand = new Random();
        BoardSquare move = null;
        BoardSquare mostAggressive = validMoves.get(0);
        if (validMoves.size() > 0) {
            if (!hardMode) { // if easy mode
                /* make a random valid move */
                int random = rand.nextInt(validMoves.size());
                move = validMoves.get(random);
                move.fire();
            }
            else{ // if hard mode
                for (BoardSquare potentialMove : validMoves) {
                    if (potentialMove.getYordinate() == 7) {
                        move = potentialMove;
                    }
                    if (potentialMove.getYordinate() > mostAggressive.getYordinate()) {
                        mostAggressive = potentialMove;
                    }
                }
                if (move == null) {
                    move = mostAggressive;
                }
                move.fire();
            }

            //todo implement different difficulties
        }
    }

    /**
     * Automatically selects the next player's piece to move based on the landing square of the previous player's move
     * @param squares ArrayList of all the boardsquares
     * @param rootGridPane GridPane containing the board
     * @param tempSquare Square that was previously landed on
     * @param inputPieces ArrayList of pieces which will either be the black pieces or white pieces
     */
    private void getNextSelectedPiece(ArrayList<BoardSquare> squares, GridPane rootGridPane, BoardSquare tempSquare, ArrayList<Piece> inputPieces) {
        firstTurn = false;
        for (Piece tempPiece : inputPieces) {
            if (Objects.equals(tempPiece.getColour(), tempSquare.getColour())) {
                selectedPiece = tempPiece;
                System.out.println("getNextSelected - selectedPiece = " + selectedPiece.getColour());
                validMoves = new ArrayList<>();
                calcAvailableMoves(rootGridPane, validMoves, squares, selectedPiece);
                if (validMoves.size() == 0) {
                    System.out.println("deadlock");
                    handleDeadlock(rootGridPane, squares, selectedPiece);
                }
                selectedPiece.setStyleString(" -fx-background-color: \n" +
                        selectedPiece.getColour() + "," +
                        selectedPiece.getPlayerColour() +
                        "        white;\n" +
                        "    -fx-background-insets: 6,10;\n" +
                        "    -fx-background-radius: 40,40;\n");
                pieceSelected = true;
            }
        }
    }

    /**
     * Contains the logic to deal with deadlocked pieces.
     * If a piece is deadlocked (i.e cannot move), then the current player must
     * skip their turn. This is treated as a move of zero length. Therefore the piece
     * the opponent must use is the one whose colour matches the colour of the square
     * the deadlocked piece occupies.
     * @param rootGridPane The gridpane containing the board
     * @param squares The ArrayList of all the squares on the board
     * @param selectedPiece The currently selected piece (will be the deadlocked piece)
     */
    private void handleDeadlock(GridPane rootGridPane, ArrayList<BoardSquare> squares, Piece selectedPiece){
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "You're deadlocked and cannot move!");
        alert.setTitle("Oh No!");
        alert.setHeaderText("You're stuck :(");
        alert.show();
        movePiece(rootGridPane, selectedPiece, selectedPiece.getXordinate(), selectedPiece.getYordinate());
        hideAvailableMoves(rootGridPane);
        if(Objects.equals(selectedPiece.getPlayerColour(), "white")){
            currentPlayer = "black";
        }
        else{
            currentPlayer = "white";
        }
        this.currentPlayerLabel.setText(currentPlayer.substring(0, 1).toUpperCase() + currentPlayer.substring(1).toLowerCase() + " moves next!");
        selectedPiece.setStyleString(" -fx-background-color: \n" +
                selectedPiece.getPlayerColour() + "," + //todo fix this nullPointer when reloading a game
                selectedPiece.getColour() +
                "        white;\n" +
                "    -fx-background-insets: 6,10;\n" +
                "    -fx-background-radius: 40,40;\n");
        // get opposing team piece whose square colour matches my piece colour
        BoardSquare recursiveSquare = null;
        for (BoardSquare temp : squares) {
            if (temp.getXordinate() == pieceToMove.getXordinate() && temp.getYordinate() == pieceToMove.getYordinate()) {
                recursiveSquare = temp;
            }
        }
        if (Objects.equals(currentPlayer, "black")) {
            getNextSelectedPiece(squares, rootGridPane, recursiveSquare, blackPieces);
        }
        if (Objects.equals(currentPlayer, "white")) {
            getNextSelectedPiece(squares, rootGridPane, recursiveSquare, whitePieces);
        }
    }

    /**
     * Displays a pop up window informing the player its not their turn
     */
    private void showWrongPlayerWarning() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("It's not your turn!");
        alert.setTitle("Warning");
        alert.setHeaderText("Wrong player!");
        alert.show();
    }

    /**
     * Displays small circles on squares which would be valid moves.
     * Through me, you enter into eternal pain. Abandon all hope, ye who enter here.
     * @param rootGridPane the gridPane containing the board
     * @param validMoves ArrayList to contain the squares belonging to valid moves
     * @param squares All the boardsquares on the board
     * @param pieceToMove Piece to calculate valid moves for
     */
    private void calcAvailableMoves(GridPane rootGridPane, ArrayList<BoardSquare> validMoves, ArrayList<BoardSquare> squares, Piece pieceToMove) {
        this.pieceToMove = pieceToMove;
        // White piece rules
        if (Objects.equals(currentPlayer, "white")) {
            ArrayList<BoardSquare> candidateValidSquares = new ArrayList<>();
            ArrayList<BoardSquare> invalidSquares = new ArrayList<>();
            ArrayList<BoardSquare> rightDiagonalMap = new ArrayList<>();
            ArrayList<BoardSquare> leftDiagonalMap = new ArrayList<>();

            /*
             iterate through all squares, add all squares in front of the piece to
             a list of candidate valid squares. If the square is unoccupied, add it
             to the list of valid moves.
              */
            for (BoardSquare tempSquare : squares) {
                if (tempSquare.getXordinate() == pieceToMove.getXordinate() && tempSquare.getYordinate() < pieceToMove.getYordinate()) {
                    candidateValidSquares.add(tempSquare);
                    if (!tempSquare.getOccupied()) {
                        validMoves.add(tempSquare);
                    }
                }
            }

            /*
            Calculate diagonal moves
             */
            int currentPieceX = pieceToMove.getXordinate(), currentPieceY = pieceToMove.getYordinate();
            for (BoardSquare square : squares) {
                int i = 1;
                while (i < 7){
                    // right side diagonal map
                    if ((square.getXordinate() == currentPieceX + i) && (square.getYordinate() == currentPieceY - i)) {
                        rightDiagonalMap.add(square);
                        validMoves.add(square);
                    }
                    // left side diagonal map
                    if ((square.getXordinate() == currentPieceX - i) && (square.getYordinate() == currentPieceY - i)) {
                        leftDiagonalMap.add(square);
                        validMoves.add(square);
                    }
                    i++;
                }
            }
            
            /*
            Prevent diagonal movement through occupied squares.
            if an occupied square exists within the right side diagonal map
            then store the coordinates of that square and add that square
            to the list of invalid squares
             */
            int occupiedRightX = 100, occupiedRightY = -100;
            for (BoardSquare square : rightDiagonalMap) {
                if (square.getOccupied()) {
                    occupiedRightX = square.getXordinate();
                    occupiedRightY = square.getYordinate();
                    invalidSquares.add(square);
                }
            }
            for (BoardSquare square : rightDiagonalMap) {
                if (square.getXordinate() > occupiedRightX && square.getYordinate() < occupiedRightY) {
                    invalidSquares.add(square);
                }
            }

            /* Repeat this process for the left side diagonal map,
            altering the occupied values as necessary.
             */
            int occupiedLeftX = -100, occupiedLeftY = -100;
            for (BoardSquare square : leftDiagonalMap) {
                if (square.getOccupied()) {
                    occupiedLeftX = square.getXordinate();
                    occupiedLeftY = square.getYordinate();
                    invalidSquares.add(square);
                }
            }
            for (BoardSquare square : leftDiagonalMap) {
                if (square.getXordinate() < occupiedLeftX && square.getYordinate() < occupiedLeftY) {
                    invalidSquares.add(square);
                }
            }

            /*
              iterate through the candidate valid squares, if one  is unoccupied and
              has the same x ordinate as the piece, then iterate through the list of
              valid moves. If an item in this list has a smaller Y ordinate than the
              previously selected square (ie its behind it), add that to a list of
              invalid squares.
             */
            for (BoardSquare boardSquare : candidateValidSquares) {
                if (boardSquare.getOccupied() && boardSquare.getXordinate() == pieceToMove.getXordinate()) {
                    for (BoardSquare removeSquare : validMoves) {
                        if (removeSquare.getXordinate() == boardSquare.getXordinate() && removeSquare.getYordinate() < boardSquare.getYordinate()) {
                            invalidSquares.add(removeSquare);
                        }
                    }
                }
            }
            /*
            iterate through the list of invalid squares, if the list of valid moves
            contains any of the invalid moves, then remove that square from valid moves
             */
            for (BoardSquare removeSquare : invalidSquares) {
                if (validMoves.contains(removeSquare)) {
                    validMoves.remove(removeSquare);
                }
            }
            /*
            Draw the circles on the valid moves
             */
            for (BoardSquare validMove : validMoves) {
                Circle possibleMoveIndicator = loadCircles();
                rootGridPane.add(possibleMoveIndicator, validMove.getXordinate(), validMove.getYordinate());
            }

            // Black piece rules
        } else if (Objects.equals(currentPlayer, "black")) {
            ArrayList<BoardSquare> candidateValidSquares = new ArrayList<>();
            ArrayList<BoardSquare> invalidSquares = new ArrayList<>();
            ArrayList<BoardSquare> rightDiagonalMap = new ArrayList<>();
            ArrayList<BoardSquare> leftDiagonalMap = new ArrayList<>();

            /*
             iterate through all squares, add all squares in front of the piece to
             a list of candidate valid squares. If the square is unoccupied, add it
             to the list of valid moves.
              */
            for (BoardSquare tempSquare : squares) {
                if (tempSquare.getXordinate() == pieceToMove.getXordinate() && tempSquare.getYordinate() > pieceToMove.getYordinate()) {
                    candidateValidSquares.add(tempSquare);
                    if (!tempSquare.getOccupied()) {
                        validMoves.add(tempSquare);
                    }
                }
            }

            /*
             Calculate diagonal moves
             */
            int currentPieceX = pieceToMove.getXordinate(), currentPieceY = pieceToMove.getYordinate();
            for (BoardSquare square : squares) {
                int i = 1;
                // right side diagonal path
                while (i < 7){
                    if ((square.getXordinate() == currentPieceX + i) && (square.getYordinate() == currentPieceY + i)) {
                        rightDiagonalMap.add(square);
                        validMoves.add(square);
                    }
                    if ((square.getXordinate() == currentPieceX - i) && (square.getYordinate() == currentPieceY + i)) {
                        leftDiagonalMap.add(square);
                        validMoves.add(square);
                    }
                    i++;
                }
            }

            /*
            Prevent diagonal movement through occupied squares
             */
            int occupiedRightX = 100, occupiedRightY = 100;
            // Right side
            for (BoardSquare square : rightDiagonalMap) {
                if (square.getOccupied()) {
                    occupiedRightX = square.getXordinate();
                    occupiedRightY = square.getYordinate();
                    invalidSquares.add(square);
                }
            }
            for (BoardSquare square : rightDiagonalMap) {
                if (square.getXordinate() > occupiedRightX && square.getYordinate() > occupiedRightY) {
                    invalidSquares.add(square);
                }
            }
            int occupiedLeftX = -100, occupiedLeftY = 100;
            // Left side
            for (BoardSquare square : leftDiagonalMap) {
                if (square.getOccupied()) {
                    occupiedLeftX = square.getXordinate();
                    occupiedLeftY = square.getYordinate();
                    invalidSquares.add(square);
                }
            }
            for (BoardSquare square : leftDiagonalMap) {
                if (square.getXordinate() < occupiedLeftX && square.getYordinate() > occupiedLeftY) {
                    invalidSquares.add(square);
                }
            }


            for (BoardSquare boardSquare : candidateValidSquares) {
                if (boardSquare.getOccupied() && boardSquare.getXordinate() == pieceToMove.getXordinate()) {
                    for (BoardSquare removeSquare : validMoves) {
                        if (removeSquare.getXordinate() == boardSquare.getXordinate() && removeSquare.getYordinate() > boardSquare.getYordinate()) {
                            invalidSquares.add(removeSquare);
                        }
                    }
                }
            }
            for (BoardSquare removeSquare : invalidSquares) {
                if (validMoves.contains(removeSquare)) {
                    validMoves.remove(removeSquare);
                }
            }
            for (BoardSquare validMove : validMoves) {
                Circle possibleMoveIndicator = loadCircles();
                rootGridPane.add(possibleMoveIndicator, validMove.getXordinate(), validMove.getYordinate());
            }
        } // end else if
    } // end method

    /**
     * Creates a styled circle and adds it to an ArrayList
     * @return The styled circle
     */
    private Circle loadCircles() {
        Circle possibleMoveIndicator = new Circle(10);
        possibleMoveIndicator.setStroke(Color.BLACK);
        possibleMoveIndicator.setStrokeWidth(3);
        possibleMoveIndicator.setFill(Color.WHITE);
        possibleMoveIndicator.setOpacity(0.25);
        possibleMoveIndicators.add(possibleMoveIndicator);
        return possibleMoveIndicator;
    }

    /**
     * Removes the circles drawn by calcAvailableMoves()
     * @param rootGridPane The gridpane containing the board
     */
    private void hideAvailableMoves(GridPane rootGridPane) {
        for (Circle tempCircle : possibleMoveIndicators) {
            rootGridPane.getChildren().remove(tempCircle);
        }
    }

    /**
     * Loads all the UI elements into the borderPane
     * @param player1Username username from the options menu
     * @param player2Username username2 from the options menu
     */
    private void loadUI(String player1Username, String player2Username){
        border = new BorderPane();
        HBox topBar = new HBox();
        HBox botBar = new HBox();
        topBar.setPadding(new Insets(10, 10, 10, 10));
        botBar.setPadding(new Insets(10, 10, 10, 10));
        border.setTop(topBar);
        border.setBottom(botBar);
        stage = new Stage();
        stage.setTitle("Game");
        menuButton = new Button("Main Menu");
        Label versusLabel = new Label("P1: " + player1Username + " - " + player1.getScore()
                + " vs P2: " + player2Username + " - " + player2.getScore());
        Label scoreLabel = new Label("Best of: " + bestOf);
        scoreLabel.setPadding(new Insets(0, 0, 0, 50));
        currentPlayerLabel.setPadding(new Insets(0, 0, 0, 80));
        topBar.setAlignment(Pos.CENTER);
        botBar.setAlignment(Pos.CENTER);
        topBar.getChildren().addAll(versusLabel, scoreLabel, currentPlayerLabel);
        botBar.getChildren().addAll(menuButton);
    }

    /**
     * Moves a piece to the target grid position
     * @param gridPane The gridpane the piece belongs to
     * @param piece The piece to move
     * @param x X ordinate of target grid position
     * @param y Y ordinate of target grid position
     */
    private void movePiece(GridPane gridPane, Piece piece, int x, int y) {
        BoardSquare square;
        square = (BoardSquare) getNodeFromGridPane(gridPane, piece.getXordinate(), piece.getYordinate());
        square.setOccupied(false);
        gridPane.getChildren().remove(piece);
        piece.setCoordinates(x, y);
        gridPane.add(piece, x, y);
        square = (BoardSquare) getNodeFromGridPane(gridPane, x, y);
        square.setOccupied(true);
    }

    /**
     * Returns the node at the specified grid positition of the specified gridpane
     * @param gridPane The gridpane to search
     * @param col Column position to check
     * @param row Row position to check
     * @return The node at the desired grid position
     */
    private Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }

    /**
     * Creates the pieces for each player and adds them to the board. Adds each piece to an ArrayList and returns
     * that ArrayList.
     * @param rootGridPane The gridpane to load the pieces on to
     * @param pieces The ArrayList to populate with pieces
     * @return The populated ArrayList
     */
    private ArrayList<Piece> loadPieces(GridPane rootGridPane, ArrayList<Piece> pieces) {
        int i = 0;
        while (i < 8) {
            Piece pieceWhite = new Piece("tower", i, 0, colours[0][i], "black");
            pieces.add(pieceWhite);
            rootGridPane.add(pieceWhite, i, 0);
            BoardSquare squareWhite = (BoardSquare) getNodeFromGridPane(rootGridPane, i, 0);
            pieceWhite.setBoardSquare(squareWhite);
            squareWhite.setOccupied(true);
            i++;
        }
        int j = 0;
        while (j < 8) {
            Piece pieceBlack = new Piece("tower", j, 7, colours[7][j], "white");
            pieces.add(pieceBlack);
            rootGridPane.add(pieceBlack, j, 7);
            BoardSquare squareBlack = (BoardSquare) getNodeFromGridPane(rootGridPane, j, 7);
            pieceBlack.setBoardSquare(squareBlack);
            squareBlack.setOccupied(true);
            j++;
        }
        return pieces;
    }

    /**
     * Populates a 8x8 gridpane with BoardSquare and colours them.
     * @param squares The ArrayList of squares to fill the board with
     * @return the finished gridpane
     */
    private GridPane fillBoard(ArrayList<BoardSquare> squares) {
        GridPane rootGridPane = new GridPane();
        final int size = 8;
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                BoardSquare square = new BoardSquare(col, row);
                String colour;
                colour = colours[row][col];
                square.setColour(colour);
                squares.add(square);
                rootGridPane.add(square, col, row);
            }
        }
        for (int i = 0; i < size; i++) {
            rootGridPane.getColumnConstraints().add(new ColumnConstraints(5, Control.USE_COMPUTED_SIZE, Double.POSITIVE_INFINITY, Priority.ALWAYS, HPos.CENTER, true));
            rootGridPane.getRowConstraints().add(new RowConstraints(5, Control.USE_COMPUTED_SIZE, Double.POSITIVE_INFINITY, Priority.ALWAYS, VPos.CENTER, true));
        }
        return rootGridPane;
    }

    /**
     * Checks if a player has a piece at the end of the board
     * Resulting in a victory in the basic game mode
     * @param movedPiece Piece that has been moved
     */
    private void checkGame(Piece movedPiece){
        // check white piece win conditions
        if (Objects.equals(movedPiece.getPlayerColour(), "white") && movedPiece.getYordinate() == 0) {
            if (player1.getColour() == "white") {
                player1.setScore(player1.getScore() + 1);
                if (player1.getScore() == roundsToWin) {
                    endGameMessage(player1.getUsername(), false);
                }
                else{
                    nextRound(player1.getUsername());
                }
            }
            else{
                player2.setScore(player2.getScore() + 1);
                if (player2.getScore() == roundsToWin) {
                    endGameMessage(player2.getUsername(), false);
                }
                else{
                    nextRound(player2.getUsername());
                }
            }
        }
        // check black piece win conditions
        if (Objects.equals(movedPiece.getPlayerColour(), "black") && movedPiece.getYordinate() == 7) {
            if (Objects.equals(player1.getColour(), "black")) {
                player1.setScore(player1.getScore() + 1);
                if (player1.getScore() == roundsToWin) {
                    endGameMessage(player1.getUsername(), false);
                }
                else{
                    nextRound(player1.getUsername());
                }
            }
            else{
                player2.setScore(player2.getScore() + 1);
                if (player2.getScore() == roundsToWin) {
                    endGameMessage(player2.getUsername(), false);
                }
                else{
                    nextRound(player2.getUsername());
                }
            }
        }
    }

    /**
     * Displays window informing the players the game has been one
     * @param winner The username of the winning player
     * @param timeOut
     */
    private void endGameMessage(String winner, boolean timeOut) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, winner + " wins the game. Play again?");
        alert.setTitle("End of game");
        if (timeOut) {
            alert.setHeaderText("You ran out of time!");
        }
        else{
            alert.setHeaderText("Congratulations!");
        }
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                pieceSelected = false;
                selectedPiece = null;
                stage.close();
                player1.setScore(0);
                player2.setScore(0);
                load(mainMenuStage, mainMenuScene, player1, player2, bestOf, speedMode, singlePlayer, hardMode);
            }
            if (response == ButtonType.CANCEL) {
                stage.close();
                mainMenuStage.setScene(mainMenuScene);
                mainMenuStage.show();
            }
        });
    }

    /**
     * Displays a pop up window at the end of a round, informing the user
     * who won the round, then loads a new game with the updated score.
     * @param winner The colour of the player who won the round
     */
    private void nextRound(String winner){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, winner + " wins the round.");
        alert.setTitle("Round Over");
        alert.setHeaderText("Congratulations!");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                pieceSelected = false;
                selectedPiece = null;
                stage.close();
                load(mainMenuStage, mainMenuScene, player1, player2, bestOf, speedMode, singlePlayer, hardMode);
            }

        });

    }
}
