
import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

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
            FadeTransition fade = new FadeTransition();
            fade.setDuration(Duration.millis(1000));
            fade.setNode(pane);
            fade.setFromValue(1);
            fade.setToValue(0);
            fade.play();
            fade.setOnFinished(ef -> {
                gameStage.moveInLevel = 0;
                this.close();
                main.readInput(main.levels[++main.level]);
                main.lvlStage.changeTitle();
                gameStage.firstLevel.print();
                winSound.stop();
                main.lvlStage.startMusic();
            });

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