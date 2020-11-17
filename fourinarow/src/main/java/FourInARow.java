import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FourInARow extends Application {

    private final Image imgSceneBack = new Image("file:src/main/resources/sceneBack.jpg");
    private final Image imgPlay = new Image("file:src/main/resources/button1.png");
    private final Image imgRules = new Image("file:src/main/resources/button2.png");
    private final Image imgExit = new Image("file:src/main/resources/button3.png");

    private static final int COLUMNS = 7;
    private static final int ROWS = 6;
    private static final int CIRCLE_DIAMETER = 80;

    private static final String PLAYER_ONE = "Player One";
    private static final String PLAYER_TWO = "Player Two";

    private static final String discColor1 = "#cb8709";
    private static final String discColor2 = "#0383a1";

    private boolean isPLayerOneTurn = true;

    private final Disc[][] insertedDiscsArray = new Disc[ROWS][COLUMNS];

    private boolean isAllowedToInsert = true;

    private GridPane grid;
    private Pane paneGame;
    private Label playerNameLabel;

    @Override
    public void start(Stage primaryStage) {

        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, false);
        BackgroundImage backgroundImage = new BackgroundImage(imgSceneBack, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);

        grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setBackground(background);
        createPlayground();

        DropShadow shadow = new DropShadow();

        StackPane stackPlay = new StackPane();
        ImageView imagePlay = new ImageView(imgPlay);
        Button buttonPlay = new Button("PLAY");
        buttonPlay.setStyle("-fx-background-color: transparent; -fx-font-size: 18; " +
                "-fx-font-family: 'Arial Black'; -fx-text-fill: #ffffff");
        buttonPlay.setEffect(shadow);
        buttonPlay.setOnAction(event -> resetGame());
        stackPlay.getChildren().addAll(imagePlay, buttonPlay);
        stackPlay.setStyle("-fx-padding: 20");

        StackPane stackRules = new StackPane();
        ImageView imageRules = new ImageView(imgRules);
        Button buttonRules = new Button("RULES");
        buttonRules.setStyle("-fx-background-color: transparent; -fx-font-size: 18; " +
                "-fx-font-family: 'Arial Black'; -fx-text-fill: #ffffff");
        buttonRules.setEffect(shadow);
        buttonRules.setOnAction(event -> gameRules());
        stackRules.getChildren().addAll(imageRules, buttonRules);
        stackRules.setStyle("-fx-padding: 20");

        StackPane stackExit = new StackPane();
        ImageView imageExit = new ImageView(imgExit);
        Button buttonExit = new Button("EXIT");
        buttonExit.setStyle("-fx-background-color: transparent; -fx-font-size: 18; " +
                "-fx-font-family: 'Arial Black'; -fx-text-fill: #ffffff");
        buttonExit.setEffect(shadow);
        buttonExit.setOnAction(event -> exitGame());
        stackExit.getChildren().addAll(imageExit, buttonExit);
        stackExit.setStyle("-fx-padding: 20");

        playerNameLabel = new Label();
        playerNameLabel.setText(isPLayerOneTurn?PLAYER_ONE : PLAYER_TWO);
        playerNameLabel.setAlignment(Pos.CENTER);
        playerNameLabel.setStyle("-fx-background-color: transparent;-fx-pref-width: 100; " +
                "-fx-pref-height: 50; -fx-font-size: 16; -fx-text-fill: white;" +
                "-fx-font-family: 'Arial Black'");
        Label whichTurnLabel = new Label("Turn");
        whichTurnLabel.setAlignment(Pos.CENTER);
        whichTurnLabel.setStyle("-fx-background-color: transparent; -fx-pref-width: 100; " +
                "-fx-pref-height: 30; -fx-font-size: 16; -fx-text-fill: white");

        VBox vbBox = new VBox(stackPlay, stackRules, stackExit,
                playerNameLabel, whichTurnLabel);
        vbBox.setAlignment(Pos.BASELINE_CENTER);
        vbBox.setStyle("-fx-background-color: transparent; -fx-max-width: 150; -fx-padding: 20");

        Pane paneMenu = new Pane(vbBox);
        paneGame = new Pane();

        grid.add(paneMenu, 1, 0, 1, 1);
        grid.add(paneGame, 0, 0, 1, 1);

        Scene scene = new Scene(grid, 900, 650, Color.BLACK);

        primaryStage.setTitle("Four In A Row");
        primaryStage.setWidth(900);
        primaryStage.setHeight(650);
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void createPlayground() {

        Shape rectangleWithHoles = new Rectangle((COLUMNS + 1) * CIRCLE_DIAMETER,
                (ROWS + 1) * CIRCLE_DIAMETER);

        for(int row = 0; row<ROWS; row++) {
            for(int col = 0; col<COLUMNS; col++) {
                Circle circle = new Circle();
                circle.setRadius(CIRCLE_DIAMETER / 2);
                circle.setCenterX(CIRCLE_DIAMETER / 2);
                circle.setCenterY(CIRCLE_DIAMETER / 2);
                circle.setSmooth(true);
                circle.setTranslateX(col * (CIRCLE_DIAMETER + 5) + CIRCLE_DIAMETER/4);
                circle.setTranslateY(row * (CIRCLE_DIAMETER + 5) + CIRCLE_DIAMETER/4);

                rectangleWithHoles = Shape.subtract(rectangleWithHoles,circle);
            }
        }
        rectangleWithHoles.setFill(Color.DARKGREY);

        grid.add(rectangleWithHoles,0,0);

        List<Rectangle> rectangleList = new ArrayList<>();

        for(int col = 0; col<COLUMNS; col++) {
            Rectangle rectangle = new Rectangle(CIRCLE_DIAMETER,(ROWS + 1)
                    * CIRCLE_DIAMETER);
            rectangle.setFill(Color.TRANSPARENT);
            rectangle.setTranslateX(col * (CIRCLE_DIAMETER + 5) + CIRCLE_DIAMETER / 4);
            rectangle.setOnMouseEntered(event -> rectangle.setFill(Color.valueOf("#eeeeee26")));
            rectangle.setOnMouseExited(event -> rectangle.setFill(Color.TRANSPARENT));

            final int finalCol = col;
            rectangle.setOnMouseClicked(event -> {
                if(isAllowedToInsert){
                    isAllowedToInsert = false;
                    insertDisc(new Disc(isPLayerOneTurn), finalCol);
                }
            });
            rectangleList.add(rectangle);
        }

        for(Rectangle rectangle: rectangleList) {
            grid.add(rectangle, 0, 0);
        }
    }

    private void insertDisc(Disc disc, int column) {

        int row = ROWS - 1;
        while(row >= 0) {
            if(getDiscIfPresent(row,column) == null) break;
            row--;
        }
        if(row < 0) return;   //If it is full, we cannot inset anyore disc

        insertedDiscsArray[row][column] = disc;
        paneGame.getChildren().add(disc);

        disc.setTranslateX(column * (CIRCLE_DIAMETER + 5) + CIRCLE_DIAMETER / 4);

        int currentRow = row;
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.5),disc);
        translateTransition.setToY(row * (CIRCLE_DIAMETER + 5) + CIRCLE_DIAMETER / 4);
        translateTransition.setOnFinished(event -> {

            isAllowedToInsert = true;
            if (gameEnded(currentRow , column)) {
                gameOver();
            }
            isPLayerOneTurn = !isPLayerOneTurn;
            playerNameLabel.setText(isPLayerOneTurn?PLAYER_ONE : PLAYER_TWO);
        });
        translateTransition.play();
    }

    private boolean gameEnded(int row, int column) {

        List<Point2D> verticalPoints = IntStream.rangeClosed(row - 3,row + 3).mapToObj(r -> new Point2D(r,column)).collect(Collectors.toList());
        List<Point2D> horizontalPoints = IntStream.rangeClosed(column - 3,column + 3).mapToObj(c -> new Point2D(row,c)).collect(Collectors.toList());

        Point2D startPoint1 = new Point2D(row - 3,column + 3);
        List<Point2D> diagonal1Points = IntStream.rangeClosed(0,6).mapToObj(i -> startPoint1.add(i,-i)).collect(Collectors.toList());

        Point2D startPoint2 = new Point2D(row - 3,column - 3);
        List<Point2D> diagonal2Points = IntStream.rangeClosed(0,6).mapToObj(i -> startPoint2.add(i,i)).collect(Collectors.toList());

        return checkCombination(verticalPoints) || checkCombination(horizontalPoints) || checkCombination(diagonal1Points) || checkCombination(diagonal2Points);
    }

    private boolean checkCombination(List<Point2D> Points) {

        int chain = 0;

        for(Point2D point: Points) {
            int rowIndexForArray = (int) point.getX();
            int columnIndexForArray = (int) point.getY();

            Disc disc = getDiscIfPresent(rowIndexForArray,columnIndexForArray);
            if(disc != null && disc.isPlayerOneMove == isPLayerOneTurn) {
                chain++;
                if(chain == 4) {
                    gameOver();
                }
            } else {
                chain = 0;
            }
        }
        return false;
    }

    private Disc getDiscIfPresent(int row,int column) {
        if (row >= ROWS || row <0 || column >= COLUMNS || column < 0) return null;
        else return insertedDiscsArray[row][column];
    }

    private void gameOver() {

        String winner = isPLayerOneTurn ? PLAYER_ONE : PLAYER_TWO;

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Four In A Row");
        dialog.setHeaderText("The Winner Is " + winner);
        dialog.setContentText("Do you want to play again?");
        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No, Exit");
        dialog.getDialogPane().getButtonTypes().add(yesButton);
        dialog.getDialogPane().getButtonTypes().add(noButton);
        Platform.runLater(()-> {
            Optional<ButtonType> buttonClicked = dialog.showAndWait();
                if(buttonClicked.isPresent() && buttonClicked.get() == yesButton) {
                    resetGame();
                }
                else{
                    Platform.exit();
                    System.exit(0);
                }
        });
    }

    public void resetGame() {

        paneGame.getChildren().clear();

        for (Disc[] discs : insertedDiscsArray) {
            Arrays.fill(discs, null);
        }

        isPLayerOneTurn = true;
        playerNameLabel.setText(PLAYER_ONE);

        createPlayground();
    }

    private static class Disc extends Circle {

        private final boolean isPlayerOneMove;

        public Disc(boolean isPlayerOneMove) {

            this.isPlayerOneMove = isPlayerOneMove;
            setRadius(CIRCLE_DIAMETER / 2);
            setFill(isPlayerOneMove ? Color.valueOf(discColor1):Color.valueOf(discColor2));
            setCenterX(CIRCLE_DIAMETER/2);
            setCenterY(CIRCLE_DIAMETER/2);
        }
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