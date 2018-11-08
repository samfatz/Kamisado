import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Objects;

public class Program extends Application {

    private Stage mainStage;
    final int WINDOW_WIDTH = 620;
    final int WINDOW_HEIGHT = 710;

    @Override
    public void start(Stage primaryStage) throws Exception {
        mainStage = primaryStage;
        mainStage.setTitle("Kamisado");
        VBox menuLayout = new VBox(20);
        Button newGameButton = new Button("New Game");
        Button loadGameButton = new Button("Load Game");
        Button highscoresButton = new Button("View Highscores");

        menuLayout.setPadding(new Insets(20, 20, 20, 20));
        menuLayout.getChildren().addAll(newGameButton, loadGameButton, highscoresButton);
        menuLayout.setAlignment(Pos.CENTER);
        Scene menuScene = new Scene(menuLayout, this.WINDOW_WIDTH, this.WINDOW_HEIGHT);

        newGameButton.setOnAction(event -> newGameOptions(mainStage, menuScene));

        mainStage.setScene(menuScene);
        mainStage.show();
    }

    private void newGameOptions(Stage stage, Scene scene) {
        mainStage = stage;
        mainStage.setTitle("Game Mode");
        VBox vBox = new VBox(20);
        Label chooseMode = new Label("What mode you would like to play?");
        Button singlePlayer = new Button("Single Player");
        Button twoPlayer = new Button("Two Player");

        vBox.getChildren().addAll(chooseMode, singlePlayer, twoPlayer);
        vBox.setAlignment(Pos.CENTER);
        Scene options = new Scene(vBox, this.WINDOW_WIDTH, this.WINDOW_HEIGHT);


        twoPlayer.setOnAction(event -> {
            multiPlayerOptions(mainStage, scene);
        });

        singlePlayer.setOnAction(event -> {
            singlePlayerOptions(mainStage, scene);
        });


        mainStage.setScene(options);
        mainStage.show();

    }

    private void multiPlayerOptions(Stage stage, Scene scene) {
        mainStage = stage;
        mainStage.setTitle("Options");
        VBox optionsLayout = new VBox(20);
        Label player1UsernameLabel = new Label("Player 1 username: ");
        Label player2UsernameLabel = new Label("Player 2 username: ");
        Label bestOfLabel = new Label("Best of: ");
        Label speedMode = new Label("Speed mode: ");
        CheckBox speedModeBox = new CheckBox();
        TextField bestOfField = new TextField();
        TextField player1UsernameField = new TextField();
        TextField player2UsernameField = new TextField();
        player2UsernameField.setMaxWidth(200);
        player2UsernameField.setPromptText("Player 2 username");
        player1UsernameField.setMaxWidth(200);
        player1UsernameField.setPromptText("Player 1 username");
        bestOfField.setMaxWidth(200);
        bestOfField.setPromptText("Best of..");
        Button startGameButton = new Button("Start Game");
        Button backButton = new Button("Main Menu");

        backButton.setOnAction(event -> {
            stage.setScene(scene);
            stage.setTitle("Kamisado");
        });

        startGameButton.setOnAction(event -> {
            String p1Username = player1UsernameField.getText();
            String p2Username = player2UsernameField.getText();
            String bestOf = bestOfField.getText();
            if (!checkBestOf(bestOf)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Rounds must be numeric and odd!");
                alert.setTitle("Warning!");
                alert.setHeaderText("Invalid Entry!");
                alert.show();
                return;
            }
            selectColour(mainStage, scene, p1Username, p2Username, bestOf, speedModeBox.isSelected());
        });


        optionsLayout.getChildren().addAll(player1UsernameLabel, player1UsernameField, player2UsernameLabel,
                player2UsernameField, bestOfLabel, bestOfField, speedMode, speedModeBox, startGameButton, backButton);
        optionsLayout.setAlignment(Pos.CENTER);

        Scene optionsScene = new Scene(optionsLayout, this.WINDOW_WIDTH, this.WINDOW_HEIGHT);
        mainStage.setScene(optionsScene);
        mainStage.show();
    }

    private void singlePlayerOptions(Stage stage, Scene scene) {
        mainStage = stage;
        mainStage.setTitle("Options");
        VBox optionsLayout = new VBox(20);
        Label player1UsernameLabel = new Label("Player 1 username: ");
        Label bestOfLabel = new Label("Best of: ");
        Label speedMode = new Label("Speed mode: ");
        Label difficulty = new Label("Difficulty: ");
        CheckBox speedModeBox = new CheckBox();
        TextField bestOfField = new TextField();
        TextField player1UsernameField = new TextField();
        player1UsernameField.setMaxWidth(200);
        player1UsernameField.setPromptText("Player 1 username");
        bestOfField.setMaxWidth(200);
        bestOfField.setPromptText("Best of..");
        Button startGameButton = new Button("Start Game");
        Button backButton = new Button("Main Menu");
        ObservableList<String> options =
                FXCollections.observableArrayList(
                        "Easy",
                        "Hard"
                );
        final ComboBox comboBox = new ComboBox(options);
        comboBox.getSelectionModel().selectFirst();

        startGameButton.setOnAction(event -> {
            String bestOf = bestOfField.getText();
            if (!checkBestOf(bestOf)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Rounds must be numeric and odd!");
                alert.setTitle("Warning!");
                alert.setHeaderText("Invalid Entry!");
                alert.show();
                return;
            }
            boolean hardMode = false;
            String difficultyChoice = (String) comboBox.getValue();
            if (Objects.equals(difficultyChoice, "Hard")) {
                hardMode = true;
            }
            Player player = new Player(player1UsernameField.getText(), "white", 0);
            Game game = new Game(player);
            game.start(mainStage, scene, Integer.parseInt(bestOf), false, true, hardMode);
            mainStage.close();
        });

        backButton.setOnAction(event -> {
            stage.setScene(scene);
            stage.setTitle("Kamisado");
        });

        optionsLayout.getChildren().addAll(player1UsernameLabel, player1UsernameField, bestOfLabel,
                bestOfField, speedMode, speedModeBox, difficulty, comboBox, startGameButton, backButton);
        optionsLayout.setAlignment(Pos.CENTER);

        Scene optionsScene = new Scene(optionsLayout, this.WINDOW_WIDTH, this.WINDOW_HEIGHT);
        mainStage.setScene(optionsScene);
        mainStage.show();
    }

    private void selectColour(Stage stage, Scene scene, String p1Username, String p2Username, String bestOf, boolean speedModeBox) {
        final String whiteChoice = "white";
        final String blackChoice = "black";

        mainStage = stage;
        mainStage.setTitle("Select Colour");
        VBox optionsLayout = new VBox(10);
        Label colourPrompt = new Label(p1Username + " Pick your colour! ");
        Button whiteButton = new Button("White");
        Button blackButton = new Button("Black");
        Button backButton = new Button("Main Menu");

        backButton.setOnAction(event -> {
            stage.setScene(scene);
            stage.setTitle("Kamisado");
        });

        whiteButton.setOnAction(event -> {
            Player player1 = new Player(p1Username, whiteChoice, 0);
            Player player2 = new Player(p2Username, blackChoice, 0);
            Game game = new Game(player1, player2);
            game.start(mainStage, scene, Integer.parseInt(bestOf), speedModeBox, false, false);
            mainStage.close();
        });

        blackButton.setOnAction(event -> {
            Player player1 = new Player(p1Username, blackChoice, 0);
            Player player2 = new Player(p2Username, whiteChoice, 0);
            Game game = new Game(player1, player2);
            game.start(mainStage, scene, Integer.parseInt(bestOf), speedModeBox, false, false);
            mainStage.close();
        });


        optionsLayout.getChildren().addAll(colourPrompt, whiteButton, blackButton, backButton);
        optionsLayout.setAlignment(Pos.CENTER);

        Scene colourScene = new Scene(optionsLayout, this.WINDOW_WIDTH, this.WINDOW_HEIGHT);
        mainStage.setScene(colourScene);
        mainStage.show();
    }

    private boolean checkBestOf(String bestOf) {
        int d;
        try {
            d = Integer.parseInt(bestOf);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return !Objects.equals(bestOf, "0") && d % 2 != 0;
    }
}
