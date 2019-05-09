
import com.sun.javafx.scene.control.behavior.ButtonBehavior;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import static java.util.Collections.list;
import java.util.Scanner;
import javafx.animation.KeyFrame;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcTo;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 *
 * @author anil
 */
class pipeImages extends ImageView {

    public String type;
    public String direction;
    public int initial_index_X, initial_index_Y, final_index_X, final_index_Y;
    public boolean isLevelFinished;
    public Path path = new Path();
    public int pathIndex;
    public static PathTransition pt;
    
    //no arg cons ekle
    pipeImages() {

    }

    pipeImages(Image image, String type, String direction) {
        super(image);
        this.type = type;
        this.direction = direction;
        setOnMousePressed(e -> {
            gameStage.scene.setCursor(Cursor.CLOSED_HAND);
            initial_index_X = (int) (e.getSceneY() / 100);
            initial_index_Y = (int) (e.getSceneX() / 100);

        });
        
        setOnMouseDragged(e -> {
            if(isLevelFinished == false)
                if (!this.direction.equals("PipeStatic")
                    && !this.direction.equals("Starter")
                    && !this.direction.equals("End")
                    && !this.type.equals("Free")) {

                this.setX(e.getX()-this.getFitWidth()/2);
                this.setY(e.getY()-this.getFitHeight()/2);
                this.toFront();
            }
            
        });

        setOnMouseReleased(e -> {
            
            gameStage.scene.setCursor(Cursor.OPEN_HAND);
            final_index_X = (int) (e.getSceneY() / 100);
            final_index_Y = (int) (e.getSceneX() / 100);
            if (final_index_X < 4 && final_index_Y < 4 && !(final_index_X < 0) && !(final_index_Y < 0)
                    && canMove(initial_index_X, initial_index_Y, final_index_X, final_index_Y) && !isFinished()) {

                //Kordinatlarının yer değiştirmesi
                main.images[initial_index_X][initial_index_Y].setX(main.images[final_index_X][final_index_Y].getX());
                main.images[initial_index_X][initial_index_Y].setY(main.images[final_index_X][final_index_Y].getY());
                main.images[final_index_X][final_index_Y].setX(initial_index_Y*100);
                main.images[final_index_X][final_index_Y].setY(initial_index_X * 100);
                
                switchSound();
                //Array içinde de değişiklik yapmamız gerekiyor
                pipeImages temp = main.images[initial_index_X][initial_index_Y];
                main.images[initial_index_X][initial_index_Y] = main.images[final_index_X][final_index_Y];
                main.images[final_index_X][final_index_Y] = temp;

                main.totalMove++;
                gameStage.moveInLevel++;

                path = new Path(); //bir üstteki if içinde kontrol yaparken yanlışlıkla path'i de çiziyoruz. Bu yüzden alttaki if i çalıştırdığında path'i 2. kez çiziyor.
                //Bunu önlemek için path'i yeniliyoruz burada.

                if (isFinished()) {

                    pt = new PathTransition();
                    pt.setDuration(Duration.millis(4000));
                    pt.setPath(path);
                    pt.setNode(main.mushroom);
                    main.mushroom.toFront();
                    pt.setAutoReverse(false);
                    pt.play();
                    pt.setOnFinished(eh -> {
                        if (main.level < main.levels.length - 1) {
                            nextLevelStage nextLvl = new nextLevelStage();
                            nextLvl.show();
                        } else {
                            endGame end = new endGame();
                            end.show();
                        }
                    });
                }
            } else {
                wrongMove();
                this.setX(initial_index_Y*100);
                this.setY(initial_index_X*100);
            }
        });
    }

    //Yaptığımız hareketin mümkün olup olmadığının kontrolü
    public boolean canMove(int initial_X, int initial_Y, int final_X, int final_Y) {

        if (!main.images[final_X][final_Y].type.equals("Free")) {
            return false;
        }
        if (main.images[initial_X][initial_Y].direction.equals("PipeStatic")
                || main.images[initial_X][initial_Y].direction.equals("Starter")
                || main.images[initial_X][initial_Y].direction.equals("End")) {
            return false;
        }

        //Burada ilginç bir trick yaptım: hareket ettirdiğimiz kutu empty olan kutunun sağ-sol-üst-alt'ında olup olmadığını kontrol etmek için
        // bulundukları konumların sayı karşılığını aldım mesela images[2][3] == 23, images[1][1] == 11; konumlarının indexlerini birleştirip sayı olarak aldım kısacası
        // sonrasında if içindeki işlemler sol-sağ-üst-alt olup olmadığını veriyor.
        int initialAsNumber = initial_X * 10 + initial_Y;
        int finalAsNumber = final_X * 10 + final_Y;

        if (finalAsNumber - initialAsNumber == 1
                || finalAsNumber - initialAsNumber == 10
                || initialAsNumber - finalAsNumber == 1
                || initialAsNumber - finalAsNumber == 10) {
            return true;

        } else {
            return false;
        }
        //
        //
    }

    public boolean isFinished() {

        int x = (whereIsStarter().yProperty().intValue()) / 100;
        int y = (whereIsStarter().xProperty().intValue()) / 100;

        if (main.images[x][y].type.equals("Vertical")) {
            path.getElements().add(new LineTo(main.images[x][y].getX() + 50.0f, main.images[x][y].getY() + 100.0f));
            previousMove = "toDown";
            checkNext(x + 1, y);
        } else if (main.images[x][y].type.equals("Horizontal")) {
            path.getElements().add(new LineTo(main.images[x][y].getX(), main.images[x][y].getY() + 50.0f));
            previousMove = "toLeft";
            checkNext(x, y - 1);
        }
        if (isLevelFinished) {
            return true;
        } else {
            return false;
        }
    }

    public pipeImages whereIsStarter() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (main.images[i][j].direction.equals("Starter")) {
                    path.getElements().add(new MoveTo(main.images[i][j].getX() + 50.0f, main.images[i][j].getY() + 50.0f)); // başlangıç noktası
                    return main.images[i][j];
                }
            }
        }
        return null;
    }

    String previousMove = "toDown";

    public void checkNext(int x, int y) {
        //BU METHOD AYNI ZAMANDA PATH DE ÇİZİYOR.

        //if(x < 4 && x >= 0 && y < 4 && y < 4) ekle sonradan array dışına çıkmış mı çıkmamış mı diye
        if (main.images[x][y].direction.equals("Pipe") && main.images[x][y].type.equals("Vertical")) {
            if (previousMove.equals("toDown")) {
                previousMove = "toDown";
                path.getElements().add(new LineTo(main.images[x][y].getX() + 50.0f, main.images[x][y].getY() + 100.0f));
                checkNext(x + 1, y);
            } else if (previousMove.equals("toUp")) {
                previousMove = "toUp";
                path.getElements().add(new LineTo(main.images[x][y].getX() + 50.0f, main.images[x][y].getY()));
                checkNext(x - 1, y);
            }
        }
        if (main.images[x][y].direction.equals("Pipe") && main.images[x][y].type.equals("Horizontal")) {
            if (previousMove.equals("toRight")) {
                previousMove = "toRight";
                path.getElements().add(new LineTo(main.images[x][y].getX() + 100.0f, main.images[x][y].getY() + 50.0f));
                checkNext(x, y + 1);
            } else if (previousMove.equals("toLeft")) {
                previousMove = "toLeft";
                path.getElements().add(new LineTo(main.images[x][y].getX(), main.images[x][y].getY() + 50.0f));
                checkNext(x, y - 1);
            }
        }
        if (main.images[x][y].type.equals("00")) {
            if (previousMove.equals("toDown")) {
                previousMove = "toLeft";
                path.getElements().add(new ArcTo(45, 45, 0, main.images[x][y].getX(), main.images[x][y].getY() + 50, false, false));
                checkNext(x, y - 1);

            } else if (previousMove.equals("toRight")) {
                previousMove = "toUp";
                path.getElements().add(new ArcTo(45, 45, 0, main.images[x][y].getX() + 50, main.images[x][y].getY(), false, false));
                checkNext(x - 1, y);
            }
        }
        if (main.images[x][y].type.equals("01")) {
            if (previousMove.equals("toDown")) {
                previousMove = "toRight";
                path.getElements().add(new ArcTo(45, 45, 0, main.images[x][y].getX() + 100, main.images[x][y].getY() + 50, false, false));//radiusX, radiusY, xAxisRotation, X, Y
                checkNext(x, y + 1);
            } else if (previousMove.equals("toLeft")) {
                previousMove = "toUp";
                path.getElements().add(new ArcTo(45, 45, 0, main.images[x][y].getX() + 50, main.images[x][y].getY(), false, false));
                checkNext(x - 1, y);
            }
        }
        if (main.images[x][y].type.equals("11")) {
            if (previousMove.equals("toLeft")) {
                previousMove = "toDown";
                path.getElements().add(new ArcTo(45, 45, 0, main.images[x][y].getX() + 100, main.images[x][y].getY() + 50, false, false));
                checkNext(x + 1, y);
            } else if (previousMove.equals("toUp")) {
                previousMove = "toRight";
                path.getElements().add(new ArcTo(45, 45, 0, main.images[x][y].getX() + 50, main.images[x][y].getY() + 100, false, false));
                checkNext(x, y + 1);
            }
        }
        if (main.images[x][y].type.equals("10")) {
            if (previousMove.equals("toRight")) {
                previousMove = "toDown";
                path.getElements().add(new ArcTo(45, 45, 0, main.images[x][y].getX() + 50, main.images[x][y].getY() + 100, false, false));
                checkNext(x + 1, y);
            } else if (previousMove.equals("toUp")) {
                previousMove = "toLeft";
                path.getElements().add(new ArcTo(45, 45, 0, main.images[x][y].getX(), main.images[x][y].getY() + 50, false, false));
                checkNext(x, y - 1);
            }
        }
        if (main.images[x][y].direction.equals("PipeStatic") && main.images[x][y].type.equals("Vertical")) {
            if (previousMove.equals("toDown")) {
                previousMove = "toDown";
                path.getElements().add(new LineTo(main.images[x][y].getX() + 50.0f, main.images[x][y].getY() + 100.0f));
                checkNext(x + 1, y);
            } else if (previousMove.equals("toUp")) {
                previousMove = "toUp";
                path.getElements().add(new LineTo(main.images[x][y].getX() + 50.0f, main.images[x][y].getY()));
                checkNext(x - 1, y);
            }
        }
        if (main.images[x][y].direction.equals("PipeStatic") && main.images[x][y].type.equals("Horizontal")) {
            if (previousMove.equals("toRight")) {
                previousMove = "toRight";
                path.getElements().add(new LineTo(main.images[x][y].getX() + 100.0f, main.images[x][y].getY() + 50.0f));
                checkNext(x, y + 1);
            } else if (previousMove.equals("toLeft")) {
                previousMove = "toLeft";
                path.getElements().add(new LineTo(main.images[x][y].getX(), main.images[x][y].getY() + 50.0f));
                checkNext(x, y - 1);
            }
        }
        if (main.images[x][y].direction.equals("End")) {
            if (main.images[x][y].type.equals("Horizontal")) {
                path.getElements().add(new LineTo(main.images[x][y].getX() + 50.0f, main.images[x][y].getY() + 50.0f));
            } else {
                path.getElements().add(new LineTo(main.images[x][y].getX() + 50.0f, main.images[x][y].getY() + 50.0f));
            }
            isLevelFinished = true;
        }
    }

    public void switchSound() {
        gameStage.switchEffect.stop();
        gameStage.switchEffect.play();
    }

    public void wrongMove() {
        gameStage.wrongMove.stop();
        gameStage.wrongMove.play();
    }
}

class mushroomImage extends ImageView {

    mushroomImage(Image image, double x, double y) {
        super(image);
        this.setFitHeight(50);
        this.setFitWidth(50);
        this.setX(x);
        this.setY(y);

    }
}

class ImagePane extends Pane {

    ImagePane() {
    }

    public void print() {
        ImageView bg = new ImageView(new Image("images/emptyBg.png"));
        bg.setFitHeight(400);
        bg.setFitWidth(400);
        this.getChildren().add(bg);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                this.getChildren().add(main.images[i][j]);
                main.images[i][j].setX((j * 100));
                main.images[i][j].setY((i * 100));
                if (main.images[i][j].direction.equals("Starter")) {
                    main.mushroom = new mushroomImage(new Image("images/mushroom.png"), (j * 100) + 25, (i * 100) + 25);
                }
            }
        }
        this.getChildren().add(main.mushroom); // to be top of pipes as image, this should be added in pane later.
    }
}

class endGame extends Stage {

    endGame() {

        StackPane pane = new StackPane();
        Scene scene = new Scene(pane, 660, 475);
        scene.setFill(Color.TRANSPARENT);
        pane.setBackground(Background.EMPTY);

        Image bg = new Image("images/endOfGame.png");
        ImageView endOfGame = new ImageView(bg);

        VBox vbox = new VBox();
        HBox hbox = new HBox();
        Image continueImg = new Image("images/continue.png");
        ImageView continueBtn = new ImageView(continueImg);
        continueBtn.setFitHeight(75);
        continueBtn.setFitWidth(250);

        Label nick = new Label("Nickname: ");
        nick.setFont(new Font("Arial", 20));
        nick.setTextFill(Color.web("#ffffff"));
        nick.setStyle("-fx-effect: dropshadow( one-pass-box , black , 10 , 5.0 , 0 , 0 )");
        TextField text = new TextField();

        hbox.setSpacing(20);
        hbox.getChildren().addAll(nick, text);
        hbox.setAlignment(Pos.BOTTOM_CENTER);

        Label moveText = new Label("You made : " + gameStage.moveInLevel + " moves." + "(Total:" + main.totalMove + ")");
        moveText.setFont(new Font("Arial", 22));
        moveText.setTextFill(Color.web("#ffffff"));
        moveText.setStyle("-fx-effect: dropshadow( one-pass-box , black , 10 , 5.0 , 0 , 0 )");

        vbox.setAlignment(Pos.BOTTOM_CENTER);
        vbox.setSpacing(18);
        vbox.setPadding(new Insets(30, 30, 60, 30));
        vbox.getChildren().addAll(moveText, hbox, continueBtn);

        pane.getChildren().addAll(endOfGame, vbox);

        this.setScene(scene);
        this.initStyle(StageStyle.TRANSPARENT);

        continueBtn.setOnMouseClicked(e -> {
            writeLeaderBoard(text.getText(), main.totalMove);
            creditsStage credits = new creditsStage();
            credits.show();
            this.close();
        });
    }

    public void writeLeaderBoard(String nick, int score) {
        try {
            PrintWriter file = new PrintWriter(new FileWriter("src/leaderboard.txt", true));

            file.println(score + " " + nick);

            file.close();
        } catch (Exception e) {

        }
        System.out.println(nick + " " + score);
    }
}

class pauseStage extends Stage {

    public pauseStage() {
        StackPane pane = new StackPane();
        Scene scene = new Scene(pane, 660, 475);
        scene.setFill(Color.TRANSPARENT);
        pane.setBackground(Background.EMPTY);

        Image bg = new Image("images/pause.gif");
        ImageView pauseBg = new ImageView(bg);
        pauseBg.setFitHeight(475);
        pauseBg.setFitWidth(660);

        VBox vbox = new VBox();

        StackPane closePane = new StackPane();
        Image close = new Image("images/closeButton.png");
        ImageView closeButton = new ImageView(close);
        closeButton.setFitHeight(25);
        closeButton.setFitWidth(25);
        closePane.getChildren().add(closeButton);
        closePane.setAlignment(Pos.TOP_RIGHT);
        closePane.setPadding(new Insets(60, 50, 0, 0));

        HBox hbox = new HBox();
        Image homeImage = new Image("images/mainMenu.png");
        ImageView homeBtn = new ImageView(homeImage);
        homeBtn.setFitHeight(75);
        homeBtn.setFitWidth(250);
        Image settingsImage = new Image("images/settings-2.png");
        ImageView settingsBtn = new ImageView(settingsImage);
        settingsBtn.setFitHeight(75);
        settingsBtn.setFitWidth(250);
        hbox.setSpacing(20);
        hbox.getChildren().addAll(homeBtn, settingsBtn);
        hbox.setAlignment(Pos.BOTTOM_CENTER);
        hbox.setPadding(new Insets(30, 40, 60, 30));

        vbox.setSpacing(200);
        vbox.getChildren().addAll(closePane, hbox);

        pane.getChildren().addAll(pauseBg, vbox);

        this.setScene(scene);
        this.initStyle(StageStyle.TRANSPARENT);

        closeButton.setOnMouseClicked(e -> {
            main.buttonPlay();
            this.close();
        });
        settingsBtn.setOnMouseClicked(e -> {
            main.buttonPlay();
            settingsStage settings = new settingsStage();
            settings.show();
        });
        homeBtn.setOnMouseClicked(e -> {
            main.buttonPlay();
            pipeImages.pt.stop();
            main.level = 0;
            main.totalMove = 0;
            gameStage.moveInLevel = 0;
            main.lvlStage.stopMusic();
            main.mainSound.play();
            main.lvlStage.close();
            this.close();
            main.lvlStage = new gameStage();
            main.mainStage.show();

        });
        scene.setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.ESCAPE)) {
                this.close();
            }
        });
    }
}

class settingsStage extends Stage {

    public settingsStage() {
        StackPane pane = new StackPane();
        Scene scene = new Scene(pane, 660, 475);
        scene.setFill(Color.TRANSPARENT);
        pane.setBackground(Background.EMPTY);

        Image bg = new Image("images/settingsWindow.png");
        ImageView pauseBg = new ImageView(bg);
        pauseBg.setFitHeight(475);
        pauseBg.setFitWidth(660);

        VBox vbox = new VBox();

        StackPane closePane = new StackPane();
        Image close = new Image("images/closeButton.png");
        ImageView closeButton = new ImageView(close);
        closeButton.setFitHeight(25);
        closeButton.setFitWidth(25);
        closePane.getChildren().add(closeButton);
        closePane.setAlignment(Pos.TOP_RIGHT);
        closePane.setPadding(new Insets(60, 50, 0, 0));

        VBox volumes = new VBox();

        // MAIN MUSIC SLIDER START //
        HBox volumeHbox = new HBox();
        Slider musicVol = new Slider();
        musicVol.setPrefWidth(250);
        musicVol.setMaxWidth(Region.USE_PREF_SIZE);
        musicVol.setMinWidth(30);
        musicVol.setValue(main.mainSound.getVolume() * 200);
        main.mainSound.volumeProperty().bind(musicVol.valueProperty().divide(200));
        gameStage.levelSound.volumeProperty().bind(musicVol.valueProperty().divide(200));
        volumeHbox.setAlignment(Pos.CENTER);

        Label music = new Label("Music volume: ");
        music.setFont(new Font("Arial", 20));
        music.setTextFill(Color.web("#ffffff"));
        music.setStyle("-fx-effect: dropshadow( one-pass-box , black , 10 , 5.0 , 0 , 0 )");
        volumeHbox.getChildren().addAll(music, musicVol);
        //MAIN MUSIC SLIDER END //

        // EFFECTS SLIDER START //
        HBox effectsHbox = new HBox();
        Slider effectsSlider = new Slider();
        effectsSlider.setPrefWidth(250);
        effectsSlider.setMaxWidth(Region.USE_PREF_SIZE);
        effectsSlider.setMinWidth(30);
        effectsSlider.setValue(gameStage.switchEffect.getVolume() * 100);
        gameStage.switchEffect.volumeProperty().bind(effectsSlider.valueProperty().divide(100));
        gameStage.wrongMove.volumeProperty().bind(effectsSlider.valueProperty().divide(100));
        effectsHbox.setAlignment(Pos.CENTER);

        Label effects = new Label("Effects volume: ");
        effects.setFont(new Font("Arial", 20));
        effects.setTextFill(Color.web("#ffffff"));
        effects.setStyle("-fx-effect: dropshadow( one-pass-box , black , 10 , 5.0 , 0 , 0 )");
        effectsHbox.getChildren().addAll(effects, effectsSlider);
        // EFFECTS SLIDER END //

        volumes.setPadding(new Insets(80, 0, 0, 0));
        volumes.setSpacing(20);
        volumes.getChildren().addAll(volumeHbox, effectsHbox);

        HBox hbox = new HBox();
        Image homeImage = new Image("images/goback.png");
        ImageView goBackBtn = new ImageView(homeImage);
        goBackBtn.setFitHeight(75);
        goBackBtn.setFitWidth(250);

        hbox.setSpacing(20);
        hbox.getChildren().addAll(goBackBtn);
        hbox.setAlignment(Pos.BOTTOM_CENTER);
        hbox.setPadding(new Insets(30, 40, 60, 30));

        vbox.setSpacing(40);
        vbox.getChildren().addAll(closePane, volumes, hbox);

        pane.getChildren().addAll(pauseBg, vbox);

        this.setScene(scene);
        this.initStyle(StageStyle.TRANSPARENT);

        closeButton.setOnMouseClicked(e -> {
            main.buttonPlay();
            this.close();
        });

        goBackBtn.setOnMouseClicked(e -> {
            main.buttonPlay();
            this.close();
        });

        scene.setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.ESCAPE)) {
                this.close();
            }
        });
    }
}

class nextLevelStage extends Stage {

    double x, y;
    public static MediaPlayer winSound;

    public nextLevelStage() {
        gameStage.levelSound.stop();
        playMusic();

        StackPane pane = new StackPane();
        Scene scene = new Scene(pane, 660, 475);
        scene.setFill(Color.TRANSPARENT);
        pane.setBackground(Background.EMPTY);

        Image bg = new Image("images/nextLevel.gif");
        ImageView nextLvlBg = new ImageView(bg);
        nextLvlBg.setFitHeight(475);
        nextLvlBg.setFitWidth(660);

        VBox vbox = new VBox();

        Label moveText = new Label("You made : " + gameStage.moveInLevel + " moves." + "(Total:" + main.totalMove + ")");
        moveText.setFont(new Font("Arial", 26));
        moveText.setTextFill(Color.web("#ffffff"));
        moveText.setStyle("-fx-effect: dropshadow( one-pass-box , black , 10 , 5.0 , 0 , 0 )");

        HBox hbox = new HBox();

        Image btnImage = new Image("images/nextLvl.png");
        ImageView nextBtn = new ImageView(btnImage);
        nextBtn.setFitHeight(75);
        nextBtn.setFitWidth(250);

        Image homeImage = new Image("images/mainMenu.png");
        ImageView homeButton = new ImageView(homeImage);
        homeButton.setFitHeight(75);
        homeButton.setFitWidth(250);

        hbox.setSpacing(20);
        hbox.getChildren().addAll(homeButton, nextBtn);
        hbox.setAlignment(Pos.BOTTOM_CENTER);
        hbox.setPadding(new Insets(30, 40, 60, 30));

        vbox.setSpacing(-10);
        vbox.getChildren().addAll(moveText, hbox);
        vbox.setAlignment(Pos.BOTTOM_CENTER);

        pane.getChildren().addAll(nextLvlBg, vbox);

        this.setScene(scene);
        this.initStyle(StageStyle.TRANSPARENT);

        pane.setOnMousePressed(e -> {
            x = e.getSceneX();
            y = e.getSceneY();
        });
        pane.setOnMouseDragged(e -> {
            this.getScene().getWindow().setX(e.getScreenX() - x);
            this.getScene().getWindow().setY(e.getScreenY() - y);
        });

        nextBtn.setOnMouseClicked(e -> {
            main.buttonPlay();
            gameStage.moveInLevel = 0;
            this.close();
            main.readInput(main.levels[++main.level]);
            main.lvlStage.changeTitle();
            gameStage.firstLevel.print();
            winSound.stop();
            main.lvlStage.startMusic();
        });

        homeButton.setOnMouseClicked(e -> {
            main.buttonPlay();
            main.level = 0;
            main.totalMove = 0;
            gameStage.moveInLevel = 0;
            main.mainSound.play();
            main.lvlStage.close();
            this.close();
            main.mainStage.show();
            main.lvlStage = new gameStage();

        });
    }

    public void playMusic() {
        try {
            winSound = new MediaPlayer(new Media(this.getClass().getResource("musics/levelCompleted.wav").toExternalForm()));
            winSound.setVolume(0.2);
            winSound.play();
        } catch (Exception e) {
            System.out.println("error");
        }

    }
}

class gameStage extends Stage {

    public static ImagePane firstLevel;
    public static int moveInLevel;
    public static MediaPlayer levelSound;
    public static MediaPlayer switchEffect;
    public static MediaPlayer wrongMove;
    public static Scene scene;
    gameStage() {

        firstLevel = new ImagePane();
        scene = new Scene(firstLevel, 400, 400);
        this.setScene(scene);
        this.setTitle("Level " + (main.level + 1));
        main.readInput(main.levels[main.level]);
        firstLevel.print();
        scene.setCursor(Cursor.OPEN_HAND);
        
        try {
            levelSound = new MediaPlayer(new Media(this.getClass().getResource("musics/level" + (main.level + 1) + ".mp3").toExternalForm()));
            wrongMove = new MediaPlayer(new Media(this.getClass().getResource("musics/wrongMove.wav").toExternalForm()));
            switchEffect = new MediaPlayer(new Media(this.getClass().getResource("musics/switch.wav").toExternalForm()));
            levelSound.setVolume(main.mainSound.getVolume());
        } catch (Exception e) {
            System.out.println("error");
        }
        
        scene.setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.ESCAPE)) {
                pauseStage pause = new pauseStage();
                pause.show();
            }
        });
    }

    public void changeTitle() {
        this.setTitle("Level " + (main.level + 1));
    }

    public void startMusic() {
        levelSound = new MediaPlayer(new Media(this.getClass().getResource("musics/level" + (main.level + 1) + ".mp3").toExternalForm()));
        levelSound.setVolume(main.mainSound.getVolume());
        levelSound.play();
    }

    public void stopMusic() {
        levelSound.stop();
    }
}

public class main extends Application {

    public static pipeImages[][] images = new pipeImages[4][4];
    public static mushroomImage mushroom;
    public static int totalMove;
    public static int level = 0;
    public static String[] levels = {"src/level1.txt", "src/level2.txt", "src/level3.txt", "src/level4.txt", "src/level5.txt"};
    public static gameStage lvlStage;
    public static MediaPlayer mainSound;
    public static MediaPlayer buttonSound;
    public static Stage mainStage;
    
    @Override
    public void start(Stage primaryStage) {
        mainStage = primaryStage;
        // Main scene //
        try {
            mainSound = new MediaPlayer(new Media(this.getClass().getResource("musics/mainScene.mp3").toExternalForm()));
            mainSound.setVolume(0.2);
            mainSound.play();

            buttonSound = new MediaPlayer(new Media(this.getClass().getResource("musics/menuNavigate.wav").toExternalForm()));
            buttonSound.setVolume(0.2);

        } catch (Exception e) {
            System.out.println("error");
        }
        StackPane root = new StackPane();
        Scene mainScene = new Scene(root, 1138, 480);
        
        Image bgImage = new Image("images/bg.gif");
        ImageView background = new ImageView(bgImage);

        VBox vbox = new VBox();

        Image btnImage = new Image("images/start.png");
        ImageView startButton = new ImageView(btnImage);
        startButton.setFitHeight(75);
        startButton.setFitWidth(300);

        Image btnImage2 = new Image("images/settings.png");
        ImageView button2 = new ImageView(btnImage2);
        button2.setFitHeight(75);
        button2.setFitWidth(300);

        HBox bottomHBox = new HBox();
        HBox leaderboardHbox = new HBox();
        Image leaderImg = new Image("images/cup.png");
        ImageView leaderBtn = new ImageView(leaderImg);
        leaderBtn.setFitHeight(75);
        leaderBtn.setFitWidth(75);
        leaderboardHbox.getChildren().add(leaderBtn);
        
        Image creditsImg = new Image("images/credits.png");
        ImageView creditsBtn = new ImageView(creditsImg);
        creditsBtn.setFitHeight(75);
        creditsBtn.setFitWidth(300);
        
        bottomHBox.setAlignment(Pos.BOTTOM_RIGHT);
        bottomHBox.setPadding(new Insets(0,10,5,0));
        bottomHBox.getChildren().addAll(leaderboardHbox);
        
        vbox.setAlignment(Pos.BOTTOM_CENTER);
        vbox.getChildren().addAll(startButton, button2, creditsBtn, bottomHBox);

        root.getChildren().addAll(background, vbox);

        primaryStage.setTitle("Game Project");
        primaryStage.setScene(mainScene);
        primaryStage.show();
        // Main scene end //

        // start game //
        lvlStage = new gameStage();
        
        //button actions
        startButton.setOnMouseClicked(e -> {
            main.buttonPlay();
            primaryStage.close();
            lvlStage.show();
            mainSound.stop();
            lvlStage.startMusic();
        });

        button2.setOnMouseClicked(e -> {
            main.buttonPlay();
            settingsStage settings = new settingsStage();
            settings.show();
        });
        
        creditsBtn.setOnMouseClicked(e -> {
            main.buttonPlay();
            creditsStage credits = new creditsStage();
            credits.show();
            primaryStage.close();
        });
        
        
        leaderBtn.setOnMouseClicked(e -> {
            main.buttonPlay();
            LeaderBoard leaderBoard = new LeaderBoard();
            leaderBoard.show();
            primaryStage.close();
        });
    }

    public static void buttonPlay() {
        main.buttonSound.stop();
        main.buttonSound.play();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void readInput(String file) {

        try {
            Scanner input = new Scanner(new File(file));

            while (input.hasNextLine()) {
                String read = input.nextLine();
                String[] parsed = read.split(",");

                int position = Integer.parseInt(parsed[0]);
                String type = parsed[1];
                String direction = parsed[2];
                String imageType = findImageType(type, direction);
                Image image = new Image(imageType);
                pipeImages imageView = new pipeImages(image, direction, type);

                //burda özellik veririm imageviewe
                imageView.setFitHeight(100);
                imageView.setFitWidth(100);
                //
                images[(int) (position / 4.1)][((int) Math.ceil(position % 4.1)) - 1] = imageView;
            }

        } catch (Exception e) {

        }
    }

    public static String findImageType(String type, String direction) {
        if (type.equals("Starter")) {
            if (direction.equals("Vertical")) {
                return "images/starter1.jpg";
            } else if (direction.equals("Horizontal")) {
                return "images/starter2.jpg";
            }
        } else if (type.equals("Empty")) {
            if (direction.equals("none")) {
                return "images/empty3.jpg";
            } else if (direction.equals("Free")) {
                return "images/empty.jpg";
            }
        } else if (type.equals("Pipe")) {
            if (direction.equals("Vertical")) {
                return "images/pipe1.jpg";
            } else if (direction.equals("Horizontal")) {
                return "images/pipe2.jpg";
            } else if (direction.equals("00")) {
                return "images/00.jpg";
            } else if (direction.equals("01")) {
                return "images/01.jpg";
            } else if (direction.equals("10")) {
                return "images/10.jpg";
            } else if (direction.equals("11")) {
                return "images/11.jpg";
            }
        } else if (type.equals("PipeStatic")) {
            if (direction.equals("Vertical")) {
                return "images/pipestatic1.jpg";
            } else if (direction.equals("Horizontal")) {
                return "images/pipestatic2.jpg";
            } else if (direction.equals("00")) {
                return "images/00_static.jpg";
            } else if (direction.equals("01")) {
                return "images/01_static.jpg";
            } else if (direction.equals("10")) {
                return "images/10_static.jpg";
            } else if (direction.equals("11")) {
                return "images/11_static.jpg";
            }
        } else if (type.equals("End")) {
            if (direction.equals("Vertical")) {
                return "images/end1.jpg";
            } else if (direction.equals("Horizontal")) {
                return "images/end2.jpg";
            }
        }
        return null;
    }

}

class creditsStage extends Stage {

    public static Timeline animation;
    public static Scene creditsScene;
    public static ImageView credits_text;
    creditsStage() {

        Pane root = new Pane();
        creditsScene = new Scene(root, 1138, 480);
        Image backgroundImg = new Image("images/bg.gif");
        ImageView background = new ImageView(backgroundImg);
        Image creditsImage = new Image("images/credits_text.png");
        credits_text = new ImageView(creditsImage);
        credits_text.setY(creditsScene.getHeight());

        animation = new Timeline(
                new KeyFrame(Duration.millis(20), e -> {
                    if (credits_text.getY() == -creditsImage.getHeight()) {
                        animation.stop();

                        finished();
                    }
                    credits_text.setY(credits_text.getY() - 1);
                }));
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.play();
        animation.setOnFinished(e -> {
            credits_text.setY(creditsScene.getHeight());
        });

        root.getChildren().addAll(background, credits_text);
        this.setScene(creditsScene);

        creditsScene.setOnKeyPressed(e -> {
            animation.stop();
            credits_text.setY(creditsScene.getHeight());
            finished();
        });
    }

    private void finished() {

        main.level = 0;
        main.totalMove = 0;
        gameStage.moveInLevel = 0;
        main.lvlStage.close();
        this.close();

        Stage backMain = new Stage();
        StackPane stackPane = new StackPane();

        VBox vbox = new VBox();

        Label text = new Label("Do you want to go back main menu?");
        Button yes = new Button("Yes");
        Button no = new Button("No");

        HBox hbox = new HBox();
        hbox.getChildren().addAll(yes, no);
        hbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(text, hbox);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(30);
        stackPane.getChildren().add(vbox);
        Scene backToMain = new Scene(stackPane, 300, 200);
        backMain.setScene(backToMain);

        yes.setOnAction(e -> {
            animation.stop();
            credits_text.setY(creditsScene.getHeight());
            backMain.close();
            main.lvlStage = new gameStage();
            main.mainStage.show();
        });
        no.setOnAction(e -> {
            System.exit(0);
        });

        backMain.show();
    }
}

class LeaderBoard extends Stage {
    String scoresText = "";
    String nicksText = "";
    
    public LeaderBoard() {

        sortLeaderBoard();
        StackPane root = new StackPane();
        Scene creditsScene = new Scene(root, 1138, 480);
        Image backgroundImg = new Image("images/bg.gif");
        ImageView background = new ImageView(backgroundImg);
        
        Image homeImage = new Image("images/goback.png");
        ImageView homeBtn = new ImageView(homeImage);
        homeBtn.setFitHeight(75);
        homeBtn.setFitWidth(250);
        
        VBox mainVBox = new VBox();
        HBox hbox = new HBox();
        VBox scores = new VBox();
        VBox nicks = new VBox();
        
        Label nickname = new Label("Nickname\n\n");
        nickname.setFont(new Font("Arial", 24));
        nickname.setUnderline(true);
        nickname.setTextFill(Color.web("#ffffff"));
        nickname.setStyle("-fx-effect: dropshadow( one-pass-box , black , 10 , 5.0 , 0 , 0 )");
        
        Label score = new Label("Score\n\n");
        score.setFont(new Font("Arial", 24));
        score.setUnderline(true);
        score.setTextFill(Color.web("#ffffff"));
        score.setStyle("-fx-effect: dropshadow( one-pass-box , black , 10 , 5.0 , 0 , 0 )");
        
        Label scoreList = new Label(scoresText);
        scoreList.setFont(new Font("Arial", 20));
        scoreList.setTextFill(Color.web("#ffffff"));
        scoreList.setStyle("-fx-effect: dropshadow( gaussian , rgba(0,0,0,0.5) , 10,0.8,0,0)");
        
        Label nickList = new Label(nicksText);
        nickList.setFont(new Font("Arial", 20));
        nickList.setTextFill(Color.web("#ffffff"));
        nickList.setStyle("-fx-effect: dropshadow( gaussian , rgba(0,0,0,0.5) , 10,0.8,0,0)");
        
        scores.setAlignment(Pos.TOP_CENTER);
        nicks.setAlignment(Pos.TOP_CENTER);
        hbox.setAlignment(Pos.TOP_CENTER);
        mainVBox.setAlignment(Pos.BOTTOM_RIGHT);
        
        hbox.setSpacing(30);
        
        scores.getChildren().addAll(score, scoreList);
        nicks.getChildren().addAll(nickname, nickList);
        hbox.getChildren().addAll(nicks, scores);
        mainVBox.getChildren().addAll(hbox, homeBtn);
        root.getChildren().addAll(background, mainVBox);
        
        this.setScene(creditsScene);
        
        homeBtn.setOnMouseClicked(e -> {
            this.close();
            main.mainStage.show();
            
        });
    }

    private void sortLeaderBoard() {
        ArrayList<String> leaderboard = new ArrayList<>();

        Scanner sc = null;
        try {
            sc = new Scanner(new File("src/leaderboard.txt"));
        } catch (Exception e) {

        }
        while (sc.hasNextLine()) {
            leaderboard.add(sc.nextLine());
        }

        ArrayList<Integer> scores = new ArrayList<>();
        ArrayList<String> nicks = new ArrayList<>();
        
        for (int i = 0; i < leaderboard.size(); i++) {
            scores.add(Integer.parseInt(leaderboard.get(i).split(" ")[0]));
        }

        Collections.sort(scores);

        for (int i = 0; i < leaderboard.size(); i++) {
            for (int j = 0; j < leaderboard.size(); j++) {
                if (Integer.parseInt(leaderboard.get(j).split(" ")[0]) == scores.get(i)) {
                    if (!nicks.contains(leaderboard.get(j).split(" ")[1])) {
                        nicks.add(leaderboard.get(j).split(" ")[1]);
                    }
                }
            }
        }
        
        for(int i = 0; i < scores.size(); i++){
            scoresText += scores.get(i) + "\n";
            nicksText += (i+1) + ". " + nicks.get(i) + "\n";
        }
    }
}
