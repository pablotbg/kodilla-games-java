import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class FourInARow extends Application {

    private final Image imgSceneBack = new Image("file:src/main/resources/sceneBack.jpg");
    private final Image imgPlay = new Image("file:src/main/resources/button1.png");
    private final Image imgReset = new Image("file:src/main/resources/button4.png");
    private final Image imgRules = new Image("file:src/main/resources/button2.png");
    private final Image imgExit = new Image("file:src/main/resources/button3.png");

    public static Button buttonPlay;

    @Override
    public void start(Stage primaryStage) {

        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, false);
        BackgroundImage backgroundImage = new BackgroundImage(imgSceneBack, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setBackground(background);

        Playground paneGame = new Playground();
        paneGame.createPlayground();

        StackPane stackPlay = new StackPane();
        ImageView imagePlay = new ImageView(imgPlay);
        buttonPlay = new Button("PLAY");
        buttonPlay.setOnAction(event -> {
            paneGame.AI();
            paneGame.playAI();
            buttonPlay.setDisable(true);
        });
        stackPlay.getChildren().addAll(imagePlay, buttonPlay);

        StackPane stackReset = new StackPane();
        ImageView imageReset = new ImageView(imgReset);
        Button buttonReset = new Button("RESET");
        buttonReset.setOnAction(event -> paneGame.reset());
        stackReset.getChildren().addAll(imageReset, buttonReset);

        StackPane stackRules = new StackPane();
        ImageView imageRules = new ImageView(imgRules);
        Button buttonRules = new Button("RULES");
        buttonRules.setOnAction(event -> gameRules());
        stackRules.getChildren().addAll(imageRules, buttonRules);

        StackPane stackExit = new StackPane();
        ImageView imageExit = new ImageView(imgExit);
        Button buttonExit = new Button("EXIT");
        buttonExit.setOnAction(event -> exitGame());
        stackExit.getChildren().addAll(imageExit, buttonExit);

        VBox vbBox = new VBox(stackPlay, stackReset, stackRules, stackExit);
        vbBox.setAlignment(Pos.BASELINE_CENTER);
        vbBox.setPadding(new Insets(0, 20, 0, 20));

        Pane paneMenu = new Pane(vbBox);

        grid.add(paneMenu, 1, 0);
        grid.add(paneGame, 0, 0);

        Scene scene = new Scene(grid, 750, 550, Color.BLACK);
        scene.getStylesheets().add("file:src/main/resources/style.css");

        primaryStage.setTitle("Four In A Row");
        primaryStage.setWidth(750);
        primaryStage.setHeight(550);
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void gameRules() {

        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Rules");
        dialog.setHeaderText("How To Play");
        ButtonType type = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.setContentText("Four In A Row (also known as Connect Four) is a " +
                "two-player connection board game in " +
                "which the players then take turns " +
                "dropping colored discs from the top into a seven-column, " +
                "six-row vertically suspended grid. The pieces fall straight down, " +
                "occupying the next available space within the column. " +
                "The objective of the game is to be the first to form a horizontal, " +
                "vertical, or diagonal line of four of one's own discs." +
                "The first player to do this wins. If the board is full and no four " +
                "are formed, there is a tie.");
        dialog.getDialogPane().getButtonTypes().add(type);
        dialog.getDialogPane().setStyle("-fx-pref-width: 400; -fx-pref-height: 250");
        dialog.show();
    }

    private void exitGame() {
        Platform.exit();
        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }
}