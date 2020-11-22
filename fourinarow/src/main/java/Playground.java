import java.util.ArrayList;
import java.util.Optional;

import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;


public class Playground extends Pane {

    Integer[][] slots = new Integer[6][7];
    Circle[][] circles = new Circle[6][7];
    VBox[] columns = new VBox[7];
    boolean p1ayerTurn = true;
    boolean playingVsAI = false;
    int winner = 0;

    public Playground() { }

    public void createPlayground() {
        Shape rectangleWithHoles = new Rectangle(560, 450);
        rectangleWithHoles.setFill(Color.DARKGREY);
        rectangleWithHoles.setSmooth(true);
        getChildren().add(rectangleWithHoles);

        for (int j = 0; j < 7; j++) {
            columns[j] = new VBox(10);
            columns[j].setTranslateX(80 * j + 10);
            columns[j].setTranslateY(10);
            getChildren().add(columns[j]);
        }

        for (int j = 0; j < 7; j++) {
            for (int i = 0; i < 6; i++) {
                circles[i][j] = new Circle(31, Color.BLACK);
                columns[j].getChildren().add(circles[i][j]);
            }
        }
    }

    public void win(int color) {
        // horizontal
        for (int j = 0; j < 4; j++) {
            for (int i = 0; i < 6; i++) {
                if (slots[i][j] == color && slots[i][j + 1] == color
                        && slots[i][j + 2] == color
                        && slots[i][j + 3] == color) {
                    winner = color;
                    gameOver();
                }
            }
        }

        // vertical
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 7; j++) {
                if (slots[i][j] == color && slots[i + 1][j] == color
                        && slots[i + 2][j] == color
                        && slots[i + 3][j] == color) {
                    winner = color;
                    gameOver();
                }
            }
        }

        // diagonal /
        for (int i = 3; i < 6; i++) {
            for (int j = 3; j < 7; j++) {
                if (slots[i][j] == color && slots[i - 1][j - 1] == color
                        && slots[i - 2][j - 2] == color
                        && slots[i - 3][j - 3] == color) {
                    winner = color;
                    gameOver();
                }
            }
        }

        // diagonal \
        for (int i = 3; i < 6; i++) {
            for (int j = 0; j < 4; j++) {
                if (slots[i][j] == color && slots[i - 1][j + 1] == color
                        && slots[i - 2][j + 2] == color
                        && slots[i - 3][j + 3] == color) {
                    winner = color;
                    gameOver();
                }
            }
        }
    }

    public void gameOver() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Four In A Row");
        dialog.setContentText("Do you want to play again?");
        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No, Exit");
        dialog.getDialogPane().getButtonTypes().add(yesButton);
        dialog.getDialogPane().getButtonTypes().add(noButton);
        Platform.runLater(()-> {
            Optional<ButtonType> buttonClicked = dialog.showAndWait();
            if(buttonClicked.isPresent() && buttonClicked.get() == yesButton) {
                reset();
            }
            else{
                Platform.exit();
                System.exit(0);
            }
        });
    }

    public void reset() {
        getChildren().clear();
        setDisable(false);
        p1ayerTurn = true;
        this.setDisable(true);
        FourInARow.buttonPlay.setDisable(false);
        for (int a = 0; a < 6; a++) {
            for (int b = 0; b < 7; b++) {
                slots[a][b] = 0;
            }
        }
        createPlayground();
    }

    void AI() {
        this.setDisable(false);
        playingVsAI = true;
    }

    public void addDisc(Circle[][] cel, int column) {
        Color color;
            if (p1ayerTurn) {
                color = Color.ROYALBLUE;
            } else {
                color = Color.ORANGERED;
        }
        boolean found = false;
        int foundIt = 0;
        for (int a = 5; !found && a > -1; a--) {
            if (cel[a][column].getFill() == Color.BLACK) {
                foundIt = a;
                found = true;
            }
        }
        cel[foundIt][column].setFill(color);
        if (cel[foundIt][column].getFill() == Color.ROYALBLUE) {
            slots[foundIt][column] = 1;
            win(1);
            p1ayerTurn = false;
        } else {
                slots[foundIt][column] = 2;
                p1ayerTurn = true;
                win(2);
            }
    }

    void playAI() {
        for (int a = 0; a < 6; a++) {
            for (int b = 0; b < 7; b++) {
                slots[a][b] = 0;
            }
        }

        for (int a = 0; a < 7; a++) {
            if (slots[0][a] == 0) {
                final int c = a;

                columns[a].setOnMouseClicked(e -> {
                    if (slots[0][c] == 0) {
                        addDisc(circles, c);
                        Point2D[] moves = getPosMoves();
                        move(moves);
                        win(2);
                        p1ayerTurn = true;
                    }
                });
            }
        }
    }

    private Point2D[] getPosMoves() {
        Point2D[] returnable;
        ArrayList<Point2D> list = new ArrayList<>();
        boolean tro;
        for (int a = 0; a < 7; a++) {
            tro = false;
            for (int b = 5; b > -1 && !tro; b--) {
                if (slots[b][a] == 0) {
                    list.add(new Point2D(b, a));
                    tro = true;
                }
            }
        }

        returnable = list.toArray(new Point2D[0]);
        return returnable;
    }

    private void move(Point2D[] moves) {
        for (Point2D move : moves) {
            if (make4(1, move) || make4(2, move)) {
                slots[(int) move.getX()][(int) move.getY()] = 2;
                circles[(int) move.getX()][(int) move.getY()].setFill(Color.ORANGERED);
                return;
            }
        }
        for (Point2D move : moves) {
            if (make3(1, move) || make3(2, move)) {
                slots[(int) move.getX()][(int) move.getY()] = 2;
                circles[(int) move.getX()][(int) move.getY()].setFill(Color.ORANGERED);
                return;
            }
        }
        int rand = (int) (Math.random() * moves.length);
        slots[(int) moves[rand].getX()][(int) moves[rand].getY()] = 2;
        circles[(int) moves[rand].getX()][(int) moves[rand].getY()].setFill(Color.ORANGERED);

    }

    private boolean make4(int x, Point2D move) {
        Integer[][] slot = new Integer[6][7];
        for (int i = 0; i < slots.length; i++) {
            System.arraycopy(slots[i], 0, slot[i], 0, slots[i].length);
        }
        int z = (int) move.getX();
        int y = (int) move.getY();
        slot[z][y] = x;

        // horizontal
        for (int j = 0; j < 4; j++) {
            for (int i = 0; i < 6; i++) {
                if (slot[i][j] == x && slot[i][j + 1] == x && slot[i][j + 2] == x && slot[i][j + 3] == x) {
                    return true;
                }
            }
        }

        // vertical
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 7; j++) {
                if (slot[i][j] == x && slot[i + 1][j] == x && slot[i + 2][j] == x && slot[i + 3][j] == x) {
                    return true;
                }
            }
        }

        // diagonal /
        for (int i = 3; i < 6; i++) {
            for (int j = 3; j < 7; j++) {
                if (slot[i][j] == x && slot[i - 1][j - 1] == x && slot[i - 2][j - 2] == x && slot[i - 3][j - 3] == x) {
                    return true;
                }
            }
        }

        // diagonal \
        for (int i = 3; i < 6; i++) {
            for (int j = 0; j < 4; j++) {
                if (slot[i][j] == x && slot[i - 1][j + 1] == x && slot[i - 2][j + 2] == x && slot[i - 3][j + 3] == x) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean make3(int x, Point2D move) {
        Integer[][] slot = new Integer[6][7];
        for (int i = 0; i < slots.length; i++) {
            System.arraycopy(slots[i], 0, slot[i], 0, slots[i].length);
        }
        int z = (int) move.getX();
        int y = (int) move.getY();
        slot[z][y] = x;

        // horizontal
        for (int j = 0; j < 5; j++) {
            for (int i = 0; i < 6; i++) {
                if (slot[i][j] == x && slot[i][j + 1] == x && slot[i][j + 2] == x) {
                    return true;
                }
            }
        }

        // vertical
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 7; j++) {
                if (slot[i][j] == x && slot[i + 1][j] == x && slot[i + 2][j] == x) {
                    return true;
                }
            }
        }

        // diagonal /
        for (int i = 2; i < 6; i++) {
            for (int j = 2; j < 7; j++) {
                if (slot[i][j] == x && slot[i - 1][j - 1] == x && slot[i - 2][j - 2] == x) {
                    return true;
                }
            }
        }

        // diagonal \
        for (int i = 2; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                if (slot[i][j] == x && slot[i - 1][j + 1] == x && slot[i - 2][j + 2] == x) {
                    return true;
                }
            }
        }
        return false;
    }
}